package in.grasshoper.field.order.service;

import in.grasshoper.core.infra.CommandProcessingResult;
import in.grasshoper.core.infra.JsonCommand;

public interface OrderWriteService {

	CommandProcessingResult createOrder(JsonCommand command);

	CommandProcessingResult updateStatus(Long orderId, JsonCommand command);

}
