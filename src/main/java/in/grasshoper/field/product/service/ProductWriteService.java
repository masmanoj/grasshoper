package in.grasshoper.field.product.service;

import in.grasshoper.core.infra.CommandProcessingResult;
import in.grasshoper.core.infra.JsonCommand;

public interface ProductWriteService {

	CommandProcessingResult createProduct(JsonCommand command);

	CommandProcessingResult updateProduct(Long productId, JsonCommand command);

	CommandProcessingResult addProductImage(Long productId, JsonCommand command);

	CommandProcessingResult updateProductImage(Long productId,
			Long productImageId, JsonCommand command);

	CommandProcessingResult deleteProductImage(Long productId,
			Long productImageId);

}
