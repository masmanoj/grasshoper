package in.grasshoper.field.tag.service;

import in.grasshoper.core.infra.CommandProcessingResult;
import in.grasshoper.core.infra.JsonCommand;

public interface SubTagWriteService {

	CommandProcessingResult createSubTag(JsonCommand command);

	CommandProcessingResult updateSubTag(Long subTagId, JsonCommand command);

	CommandProcessingResult removeSubTag(Long subTagId);

}
