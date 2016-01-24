package in.grasshoper.field.tag.domain;

import static in.grasshoper.field.tag.TagConstants.LabelParamName;
import static in.grasshoper.field.tag.TagConstants.TagParamName;
import in.grasshoper.core.infra.JsonCommand;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Table(name = "g_tag")
public class Tag extends AbstractPersistable<Long>{
	@Column(name = "tag", nullable = false, length = 100)
	private String tag;
	@Column(name = "label", nullable = false, length = 100)
	private String label;
	@Column(name = "is_internal")
	Boolean isInternal;
	
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "tag", orphanRemoval = true)
	private Set<SubTag> subTags;
	
	protected Tag(){}
	
	private Tag(final String tag, final String label,  final Boolean isInternal) {
		super();
		this.tag = tag;
		this.label = label;
		this.isInternal = isInternal;
	}

	public static Tag fromJson(final JsonCommand command) {
        final String tag = command.stringValueOfParameterNamed(TagParamName);
        final String label = command.stringValueOfParameterNamed(LabelParamName);
        final Boolean isInternal = false;
        return new Tag(tag, label, isInternal);
    }
	
	public Map<String, Object> update(final JsonCommand command) {

        final Map<String, Object> actualChanges = new LinkedHashMap<>(1);
        if (command.isChangeInStringParameterNamed(TagParamName, this.tag)) {
        	final String newValue = command.stringValueOfParameterNamed(TagParamName);
            actualChanges.put(TagParamName, newValue);
            actualChanges.put(TagParamName + "_old", this.tag);
            this.tag = StringUtils.defaultIfEmpty(newValue, null);
        }
        
        if (command.isChangeInStringParameterNamed(LabelParamName, this.label)) {
            final String newValue = command.stringValueOfParameterNamed(LabelParamName);
            actualChanges.put(LabelParamName, newValue);
            actualChanges.put(LabelParamName + "_old", this.label);
            this.label = StringUtils.defaultIfEmpty(newValue, "");
        }       
        return actualChanges;
    }
	public boolean removeSubtag(final SubTag subTag){
		return this.subTags.remove(subTag);
	}
}