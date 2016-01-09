package in.grasshoper.field.product.service;

import in.grasshoper.field.product.data.ProductData;

import java.util.Collection;

public interface ProductReadService {

	Collection<ProductData> retriveAll();

	ProductData retriveOne(Long productId);

	ProductData generateTemplate();

	Collection<ProductData> retriveAllProductsSearch(String searchQry,
			Boolean notSoldOut, Integer limit, Integer offset, String orderby,
			String category, String productUId);

}
