package in.grasshoper.field.order.service;

import static in.grasshoper.field.order.OrderConstants.*;
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
import in.grasshoper.field.order.domain.OrderHistory;
import in.grasshoper.field.order.domain.OrderRepository;
import in.grasshoper.field.order.domain.OrderStatus;
import in.grasshoper.field.product.domain.Product;
import in.grasshoper.field.product.domain.ProductRepository;
import in.grasshoper.field.tag.domain.SubTag;
import in.grasshoper.field.tag.domain.SubTagRepository;
import in.grasshoper.field.tag.domain.Tag;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
			final List<OrderHistory> orderhistory = new ArrayList<>();
			final Order order = Order.fromJson(command, this.context.authenticatedUser(), dropAddress, 
					orderCart, orderhistory);
			//init draft history
			OrderHistory hist = OrderHistory.create(order, OrderStatus.Received.getStatusCode(), OrderStatus.getstatusDesc(OrderStatus.Received));
			orderhistory.add(hist);		
					
			//this.orderRepository.save(order);
			BigDecimal totalPrice = getOrderCartFromCommand(element, orderCart, order);
			order.updateAmount(totalPrice);
			
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
	
	private BigDecimal getOrderCartFromCommand(final JsonElement element, final List<OrderCart> orderCarts, final Order order){
		BigDecimal totalPrice = BigDecimal.ZERO;
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
			
			if(quantity.compareTo(product.getMinQuantity()) < 1){
				throw new GeneralPlatformRuleException("error.quantity.must.be.above.minimum.quantity",
						"Quantity must be above product minimum quantity "+product.getMinQuantity(),
						product.getMinQuantity());
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
			BigDecimal price = quantity.multiply(product.getPricePerUnit());
			totalPrice = totalPrice.add(price);
			final OrderCart cart = OrderCart.create(product, order, quantity, subTag, price);
			
			orderCarts.add(cart);
			
			adjustProductQuantity(product, quantity);
		}
		
		return totalPrice;
	}
	
	private void adjustProductQuantity(final Product product, final BigDecimal quantity) {
		product.updateQuantity(product.getQuantity().subtract(quantity));
		this.productRepository.save(product);
	}


	@Override
	@Transactional
	public CommandProcessingResult updateStatus(final Long orderId, final JsonCommand command) {
		try {
			// this.dataValidator.validateForUpdate(command.getJsonCommand());
			final Order order = this.orderRepository.findOne(orderId);
			if (order == null) {
				throw new ResourceNotFoundException(
						"error.entity.order.not.found", "Order with id " + orderId
								+ "not found", orderId);
			}
			final Map<String, Object> changes = order.update(command);

			if (!changes.isEmpty()) {
				Integer statusId =(Integer) changes.get(OrderStatusParamName);
				
				OrderHistory hist = OrderHistory.create(order, statusId,  OrderStatus.getstatusDesc(OrderStatus.fromInt(statusId)));
				order.addOrderHistory(hist);	
				
				this.orderRepository.save(order);
			}

			return new CommandProcessingResultBuilder() //
					.withResourceIdAsString(orderId) //
					.withSuccessStatus()
					.withChanges(changes) //
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
}
