package in.grasshoper.field.product.service;

import java.math.BigDecimal;

import in.grasshoper.core.infra.CommandProcessingResult;
import in.grasshoper.core.infra.JsonCommand;
import in.grasshoper.field.order.domain.Order;
import in.grasshoper.field.product.domain.Product;

public interface ProductWriteService {

	CommandProcessingResult createProduct(JsonCommand command);

	CommandProcessingResult updateProduct(Long productId, JsonCommand command);

	CommandProcessingResult addProductImage(Long productId, JsonCommand command);

	CommandProcessingResult updateProductImage(Long productId,
			Long productImageId, JsonCommand command);

	CommandProcessingResult deleteProductImage(Long productId,
			Long productImageId);

	void debitProductQuantity(Product product, Order order, BigDecimal quantity);

	void reverseDebitProductQuantity(Product product, Order order,
			BigDecimal quantity);

	CommandProcessingResult resetProductQuantity(Long productId);

	CommandProcessingResult updateProductQuantity(Long productId,
			JsonCommand command);

}
