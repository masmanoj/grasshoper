package in.grasshoper.field.tag.service;

import in.grasshoper.field.tag.data.SubTagData;
import in.grasshoper.field.tag.data.TagData;

import java.util.Collection;

public interface TagReadService {


	TagData retriveOneTag(Long tagId);

	Collection<TagData> retriveAllTags();
	

	Collection<SubTagData> retriveAllSubTagsForTag(Long tagId);
	
	SubTagData retriveOneSubTag(Long subTagId);

	Collection<SubTagData> retriveAllSubTagsForTag(String tag);

	SubTagData retriveOneInternalSubTagsForTagAndSubTagLabel(String tag,
			String subTagLabel);

	SubTagData retriveOneSubTag(String tag, String subTag);



}
