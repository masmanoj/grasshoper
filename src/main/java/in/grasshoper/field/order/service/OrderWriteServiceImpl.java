package in.grasshoper.field.order.service;

import static in.grasshoper.field.order.OrderConstants.CartProductQuantity;
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
import in.grasshoper.field.address.domain.Address;
import in.grasshoper.field.address.domain.AddressRepository;
import in.grasshoper.field.order.domain.Order;
import in.grasshoper.field.order.domain.OrderCart;
import in.grasshoper.field.order.domain.OrderRepository;
import in.grasshoper.field.product.domain.Product;
import in.grasshoper.field.product.domain.ProductRepository;

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
	
	@Autowired
	public OrderWriteServiceImpl(final OrderRepository orderRepository,
			final FromJsonHelper fromJsonHelper,
			final ProductRepository productRepository,
			final AddressRepository addressRepository) {
		super();
		this.orderRepository = orderRepository;
		this.fromJsonHelper = fromJsonHelper;
		this.productRepository = productRepository;
		this.addressRepository = addressRepository;
	}


	@Override
	@Transactional
	public CommandProcessingResult createOrder(final JsonCommand command) {
		try {	
			//this.dataValidator.validateForCreate(command.getJsonCommand());
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
			
			final List<OrderCart> orderCart = new ArrayList<>();
			final Order order = Order.fromJson(command, dropAddress, orderCart);
			getOrderCartFromCommand(element, orderCart, order);
			this.orderRepository.save(order);
			
			return new CommandProcessingResultBuilder().withResourceIdAsString(
					order.getId()).build();
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
			orderCartJson.addProperty(GrassHoperMainConstants.LocaleParamName, GrassHoperMainConstants.DefaultLocale);
			final BigDecimal quantity = this.fromJsonHelper.extractBigDecimalWithLocaleNamed(CartProductQuantity, orderCartJson);
			if(quantity.compareTo(product.getQuantity()) > 1){
				throw new GeneralPlatformRuleException("error.insufficient.quantity",
						"Insufficient quantity, try quantity less than "+product.getQuantity(),
						product.getQuantity());
			}
			
			final OrderCart cart = OrderCart.create(product, order, quantity);
			
			orderCarts.add(cart);
		}
	}
}
