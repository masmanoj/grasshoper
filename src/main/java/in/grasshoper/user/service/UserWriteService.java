package in.grasshoper.user.service;

import in.grasshoper.core.infra.CommandProcessingResult;
import in.grasshoper.core.infra.JsonCommand;

public interface UserWriteService {

	CommandProcessingResult create(JsonCommand command);

	CommandProcessingResult createPublicUser(JsonCommand command);

	String activateUser(String email, String otp);


	CommandProcessingResult updatePassword(Long userId, JsonCommand command);

}
