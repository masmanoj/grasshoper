package in.grasshoper.field.product.service;

import in.grasshoper.field.product.data.ProductData;

import java.util.Collection;

public interface ProductReadService {

	Collection<ProductData> retriveAll();

	ProductData retriveOne(Long productId);

}
