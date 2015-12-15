package in.grasshoper.field.product.service;

import in.grasshoper.core.infra.CommandProcessingResult;
import in.grasshoper.core.infra.JsonCommand;

public interface ProductWriteService {

	CommandProcessingResult createProduct(JsonCommand command);

	CommandProcessingResult updateProduct(Long productId, JsonCommand command);

}
