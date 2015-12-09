package in.grasshoper.user.service;

import in.grasshoper.core.infra.CommandProcessingResult;
import in.grasshoper.core.infra.JsonCommand;

public interface UserWriteService {

	CommandProcessingResult create(JsonCommand command);

}
