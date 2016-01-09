package in.grasshoper.field.order.service;

import static in.grasshoper.field.order.OrderConstants.CartProductPkgStyleParamName;
import static in.grasshoper.field.order.OrderConstants.CartProductQuantityParamName;
import static in.grasshoper.field.order.OrderConstants.CartProductUidParam;
import static in.grasshoper.field.order.OrderConstants.DropAddressIdParamName;
import static in.grasshoper.field.order.OrderConstants.OrderCartListParamName;
import in.grasshoper.core.GrassHoperMainConstants;
import in.grasshoper.core.exception.GeneralPlatformRuleException;
import in.grasshoper.core.exception.PlatformDataIntegrityException;
import in.grasshoper.core.exception.ResourceNotFoundException;
import in.grasshoper.core.infra.CommandProcessingResult;
import in.grasshoper.core.infra.CommandProcessingResultBuilder;
import in.grasshoper.core.infra.FromJsonHelper;
import in.grasshoper.core.infra.JsonCommand;
import in.grasshoper.core.security.service.PlatformSecurityContext;
import in.grasshoper.field.address.domain.Address;
import in.grasshoper.field.address.domain.AddressRepository;
import in.grasshoper.field.order.data.OrderDataValidator;
import in.grasshoper.field.order.domain.Order;
import in.grasshoper.field.order.domain.OrderCart;
import in.grasshoper.field.order.domain.OrderRepository;
import in.grasshoper.field.product.domain.Product;
import in.grasshoper.field.product.domain.ProductRepository;
import in.grasshoper.field.tag.domain.SubTag;
import in.grasshoper.field.tag.domain.SubTagRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

@Service
public class OrderWriteServiceImpl implements OrderWriteService {
	
	private final OrderRepository orderRepository;
	private final FromJsonHelper fromJsonHelper;
	private final ProductRepository productRepository;
	private final AddressRepository addressRepository;
	private final OrderDataValidator dataValidator;
	private final SubTagRepository subTagRepository;
	private final PlatformSecurityContext context;
	
	@Autowired
	public OrderWriteServiceImpl(final OrderRepository orderRepository,
			final FromJsonHelper fromJsonHelper,
			final ProductRepository productRepository,
			final AddressRepository addressRepository,
			final OrderDataValidator dataValidator,
			final PlatformSecurityContext context,
			final SubTagRepository subTagRepository) {
		super();
		this.orderRepository = orderRepository;
		this.fromJsonHelper = fromJsonHelper;
		this.productRepository = productRepository;
		this.addressRepository = addressRepository;
		this.dataValidator = dataValidator;
		this.context = context;
		this.subTagRepository = subTagRepository;
	}


	@Override
	@Transactional
	public CommandProcessingResult createOrder(final JsonCommand command) {
		try {	
			this.dataValidator.validateForCreate(command.getJsonCommand());
			final String json = command.getJsonCommand();
	    	final JsonObject jsonObj = this.fromJsonHelper.parse(json).getAsJsonObject();
	    	jsonObj.addProperty(GrassHoperMainConstants.LocaleParamName, GrassHoperMainConstants.DefaultLocale);
	    	final JsonElement element = jsonObj;
			final Long dropAddressId = this.fromJsonHelper.extractLongNamed(DropAddressIdParamName, element);
			final Address dropAddress = this.addressRepository.findOne(dropAddressId);
			if (dropAddress == null) {
				throw new ResourceNotFoundException(
						"error.entity.address.not.found", "Address with id " + dropAddressId
								+ "not found", dropAddressId);
			}
			//check whether address belong to the logged in user
			//additional integirity check
			if(!this.context.authenticatedUser().getId().equals(dropAddress.getOwnerUser().getId())){
				throw new GeneralPlatformRuleException("error.no.rights.to.delete.address", 
						"Logged in user dosent have access on this address with id "+dropAddressId,
						dropAddressId);
			}
			
			final List<OrderCart> orderCart = new ArrayList<>();
			final Order order = Order.fromJson(command, dropAddress, orderCart);
			//this.orderRepository.save(order);
			getOrderCartFromCommand(element, orderCart, order);
			//order.updateOrderCart(orderCart);
			this.orderRepository.saveAndFlush(order);
			
			String orderName  =  "ORD001"+order.getId();
			order.setName(orderName);
			
			this.orderRepository.saveAndFlush(order);
			
			return new CommandProcessingResultBuilder()
				.withResourceIdAsString(order.getId())
				.withSuccessStatus()
				.build();
		} catch (DataIntegrityViolationException ex) {
			ex.printStackTrace();
			final Throwable realCause = ex.getMostSpecificCause();
			throw new PlatformDataIntegrityException(
					"error.msg.unknown.data.integrity.issue",
					"Unknown data integrity issue with resource: "
							+ realCause.getMessage());
		}
	}
	
	private void getOrderCartFromCommand(final JsonElement element, final List<OrderCart> orderCarts, final Order order){
		final JsonArray orderCartArray =  this.fromJsonHelper.extractJsonArrayNamed(OrderCartListParamName, element);
		for (int i = 0; i < orderCartArray.size(); i++) {
			final JsonObject orderCartJson = orderCartArray.get(i).getAsJsonObject();
			//validate this json
			
			final String productUid = this.fromJsonHelper.extractStringNamed(CartProductUidParam, orderCartJson);
			final Product product = this.productRepository.findByProductUid(productUid);
			if (product == null) {
				throw new ResourceNotFoundException(
						"error.entity.product.not.found", "Product with id " + productUid
								+ "not found", productUid);
			}
			
			orderCartJson.addProperty(GrassHoperMainConstants.LocaleParamName, GrassHoperMainConstants.DefaultLocale);
			final BigDecimal quantity = this.fromJsonHelper.extractBigDecimalWithLocaleNamed(CartProductQuantityParamName, orderCartJson);
			if(quantity.compareTo(product.getQuantity()) > 1){
				throw new GeneralPlatformRuleException("error.insufficient.quantity",
						"Insufficient quantity, try quantity less than "+product.getQuantity(),
						product.getQuantity());
			}
			
			final Long pkgStyleId = this.fromJsonHelper.extractLongNamed(CartProductPkgStyleParamName, orderCartJson);
			final SubTag subTag = subTagRepository.findOne(pkgStyleId);
			if (subTag == null) {
				throw new ResourceNotFoundException(
						"error.entity.pkg.style.not.found", "Pkg style with id " + pkgStyleId
								+ "not found", pkgStyleId);
			}
			
			if(product.getPackingStyles()!= null) 
				if(!product.getPackingStyles().contains(subTag)){
					throw new GeneralPlatformRuleException("error.pkg.style.not.allowed.for.product", 
							"Package style not allowed for this product");
				}
			
			
			final OrderCart cart = OrderCart.create(product, order, quantity, subTag);
			
			orderCarts.add(cart);
		}
	}
}
