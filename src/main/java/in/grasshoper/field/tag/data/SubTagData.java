package in.grasshoper.field.tag.data;

public class SubTagData {
	private final Long id;
	private final Long tagId;
	private final String subTag;
	private final String label;
	private final Integer displayOrder;
	private final String tag;
	private final String taglabel;
	private  SubTagData(final Long id, final Long tagId, final String subTag, final String label,
			final Integer displayOrder, final String tag, final String tagLabel) {
		super();
		this.id = id;
		this.tagId = tagId;
		this.subTag = subTag;
		this.label = label;
		this.displayOrder = displayOrder;
		this.tag = tag;
		this.taglabel = tagLabel;
	}
	
	public static SubTagData createNew(final Long id, final Long tagId, final String subTag, final String label,
			final Integer displayOrder, final String tag, final String tagLabel){
		return new SubTagData(id, tagId, subTag, label, displayOrder, tag, tagLabel);
	}

	public Long getId() {
		return this.id;
	}

	public Long getTagId() {
		return this.tagId;
	}

	public String getSubTag() {
		return this.subTag;
	}

	public String getLabel() {
		return this.label;
	}

	public Integer getDisplayOrder() {
		return this.displayOrder;
	}

	public String getTag() {
		return this.tag;
	}

	public String getTaglabel() {
		return this.taglabel;
	}
	
	
}
