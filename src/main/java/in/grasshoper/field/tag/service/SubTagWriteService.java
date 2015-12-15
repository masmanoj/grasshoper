package in.grasshoper.field.tag.service;

import in.grasshoper.core.infra.CommandProcessingResult;
import in.grasshoper.core.infra.JsonCommand;

public interface SubTagWriteService {

	CommandProcessingResult createSubTag(JsonCommand command);

	CommandProcessingResult updateTag(Long subTagId, JsonCommand command);

}
