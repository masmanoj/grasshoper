package in.grasshoper.field.tag.service;

import java.util.Map;

import in.grasshoper.core.exception.PlatformDataIntegrityException;
import in.grasshoper.core.exception.ResourceNotFoundException;
import in.grasshoper.core.infra.CommandProcessingResult;
import in.grasshoper.core.infra.CommandProcessingResultBuilder;
import in.grasshoper.core.infra.JsonCommand;
import in.grasshoper.field.tag.domain.SubTag;
import in.grasshoper.field.tag.domain.SubTagRepository;
import in.grasshoper.field.tag.domain.Tag;
import in.grasshoper.field.tag.domain.TagRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
public class SubTagWriteServiceImpl implements SubTagWriteService {
	
	private final TagRepository tagRepository;
	private final SubTagRepository subTagRepository;
	@Autowired
	public SubTagWriteServiceImpl(final TagRepository tagRepository,
			final SubTagRepository subTagRepository) {
		super();
		this.tagRepository = tagRepository;
		this.subTagRepository = subTagRepository;
	}
	
	@Override
	@Transactional
	public CommandProcessingResult createSubTag(final JsonCommand command) {
		try {
			// this.dataValidator.validateForCreate(command.getJsonCommand());
			final Long tagId = command.resourceId();
			final Tag tag = this.tagRepository.findOne(tagId);
			if (tag == null) {
				throw new ResourceNotFoundException(
						"error.entity.tag.not.found", "Tag with id " + tagId
								+ "not found", tagId);
			}
			final SubTag subtTag = SubTag.fromJson(tag, command);
			this.subTagRepository.save(subtTag);

			return new CommandProcessingResultBuilder().withResourceIdAsString(
					subtTag.getId()).build();

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
	public CommandProcessingResult updateSubTag(final Long subTagId,
			final JsonCommand command) {
		try {
			// this.dataValidator.validateForUpdate(command.getJsonCommand());
			final SubTag subTag = this.subTagRepository.findOne(subTagId);
			if (subTag == null) {
				throw new ResourceNotFoundException(
						"error.entity.subtag.not.found", "Sub tag with id " + subTag
								+ "not found", subTag);
			}
			final Map<String, Object> changes = subTag.update(command);

			if (!changes.isEmpty()) {
				this.subTagRepository.save(subTag);
			}

			return new CommandProcessingResultBuilder() //
					.withResourceIdAsString(subTagId) //
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
	public CommandProcessingResult removeSubTag(final Long subTagId) {
		try {
			// this.dataValidator.validateForUpdate(command.getJsonCommand());
			final SubTag subTag = this.subTagRepository.findOne(subTagId);
			if (subTag == null) {
				throw new ResourceNotFoundException(
						"error.entity.subtag.not.found", "Sub tag with id " + subTag
								+ "not found", subTag);
			}
			
			subTag.getTag().removeSubtag(subTag);
			
			this.tagRepository.saveAndFlush(subTag.getTag());
			
			
			return new CommandProcessingResultBuilder() //
			.withResourceIdAsString(subTagId) //
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
