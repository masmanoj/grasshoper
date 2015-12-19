package in.grasshoper.field.tag.data;

import java.util.Collection;

public class TagData {
	private final Long id;
	private final String tag;
	private final String label;
	
	private final Collection<SubTagData> subTags;

	private TagData(final Long id, final String tag, final String label,
			final Collection<SubTagData> subTags) {
		super();
		this.id = id;
		this.tag = tag;
		this.label = label;
		this.subTags = subTags;
	}
	
	public static TagData createNew(final Long id, final String tag, final String label,
			final Collection<SubTagData> subTags){
		return new TagData(id, tag, label, subTags);
	}

	public Long getId() {
		return this.id;
	}

	public String getTag() {
		return this.tag;
	}

	public String getLabel() {
		return this.label;
	}

	public Collection<SubTagData> getSubTags() {
		return this.subTags;
	}
}
