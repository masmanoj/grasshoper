package in.grasshoper.field.transaction.service;

import in.grasshoper.field.order.domain.Order;
import in.grasshoper.field.product.domain.Product;

import java.math.BigDecimal;

public interface ProductQuantityTransactionWriteService {

	Long createProductQuantityTransation(Product product, BigDecimal quantity,
			String quantityUnit, Integer tranTypeCode, Order order,
			Boolean isReverse);

}
