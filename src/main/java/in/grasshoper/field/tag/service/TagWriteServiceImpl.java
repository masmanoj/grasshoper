package in.grasshoper.field.tag.service;

import in.grasshoper.core.exception.PlatformDataIntegrityException;
import in.grasshoper.core.exception.ResourceNotFoundException;
import in.grasshoper.core.infra.CommandProcessingResult;
import in.grasshoper.core.infra.CommandProcessingResultBuilder;
import in.grasshoper.core.infra.JsonCommand;
import in.grasshoper.field.tag.domain.Tag;
import in.grasshoper.field.tag.domain.TagRepository;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TagWriteServiceImpl implements TagWriteService {

	private final TagRepository tagRepository;

	@Autowired
	public TagWriteServiceImpl(final TagRepository tagRepository) {
		super();
		this.tagRepository = tagRepository;
	}

	@Override
	@Transactional
	public CommandProcessingResult createTag(final JsonCommand command) {
		try {
			// this.dataValidator.validateForCreate(command.getJsonCommand());
			final Tag tag = Tag.fromJson(command);
			this.tagRepository.save(tag);

			return new CommandProcessingResultBuilder().withResourceIdAsString(
					tag.getId()).build();

		} catch (DataIntegrityViolationException ex) {
			ex.printStackTrace();
			final Throwable realCause = ex.getMostSpecificCause();
			throw new PlatformDataIntegrityException(
					"error.msg.unknown.data.integrity.issue",
					"Unknown data integrity issue with resource: "
							+ realCause.getMessage());
		}
	}

	@Override
	@Transactional
	public CommandProcessingResult updateTag(final Long tagId,
			final JsonCommand command) {
		try {
			// this.dataValidator.validateForUpdate(command.getJsonCommand());
			final Tag tag = this.tagRepository.findOne(tagId);
			if (tag == null) {
				throw new ResourceNotFoundException(
						"error.entity.tag.not.found", "Tag with id " + tagId
								+ "not found", tagId);
			}
			final Map<String, Object> changes = tag.update(command);

			if (!changes.isEmpty()) {
				this.tagRepository.save(tag);
			}

			return new CommandProcessingResultBuilder() //
					.withResourceIdAsString(tagId) //
					.withChanges(changes) //
					.build();
		} catch (DataIntegrityViolationException ex) {
			ex.printStackTrace();
			final Throwable realCause = ex.getMostSpecificCause();
			throw new PlatformDataIntegrityException(
					"error.msg.unknown.data.integrity.issue",
					"Unknown data integrity issue with resource: "
							+ realCause.getMessage());
		}
	}
	@Override
	@Transactional
	public CommandProcessingResult removeTag(final Long tagId) {
		try {
			// this.dataValidator.validateForUpdate(command.getJsonCommand());
			final Tag tag = this.tagRepository.findOne(tagId);
			if (tag == null) {
				throw new ResourceNotFoundException(
						"error.entity.tag.not.found", "Tag with id " + tagId
								+ "not found", tagId);
			}
			
			this.tagRepository.delete(tag);
			

			return new CommandProcessingResultBuilder() //
					.withResourceIdAsString(tagId) //
					.build();
		} catch (DataIntegrityViolationException ex) {
			ex.printStackTrace();
			final Throwable realCause = ex.getMostSpecificCause();
			throw new PlatformDataIntegrityException(
					"error.msg.unknown.data.integrity.issue",
					"Unknown data integrity issue with resource: "
							+ realCause.getMessage());
		}
	}
}
