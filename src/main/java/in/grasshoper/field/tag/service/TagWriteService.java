package in.grasshoper.field.tag.service;

import in.grasshoper.core.infra.CommandProcessingResult;
import in.grasshoper.core.infra.JsonCommand;

public interface TagWriteService {

	CommandProcessingResult createTag(JsonCommand command);

	CommandProcessingResult updateTag(Long tagId, JsonCommand command);

	CommandProcessingResult removeTag(Long tagId);

}
