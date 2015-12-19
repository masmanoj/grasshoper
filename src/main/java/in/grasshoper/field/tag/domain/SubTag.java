package in.grasshoper.field.tag.domain;

import static in.grasshoper.field.tag.TagConstants.DisplayOrderParamName;
import static in.grasshoper.field.tag.TagConstants.LabelParamName;
import static in.grasshoper.field.tag.TagConstants.SubTagParamName;
import in.grasshoper.core.infra.JsonCommand;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Table(name = "g_sub_tag", uniqueConstraints = { @UniqueConstraint(columnNames = { "tag_id", "sub_tag" }, name = "sub_tag_duplicate") })
public class SubTag extends AbstractPersistable<Long>{
	
	@Column(name = "sub_tag", length = 150 , nullable = false)
    private String subTag;
	@ManyToOne
	@JoinColumn(name = "tag_id", nullable = false)
	private Tag tag;
	@Column(name = "label", length = 150, nullable = false)
	private String label;
    @Column(name = "display_order")
    private int displayOrder;
    
    protected SubTag(){}

	private SubTag(Tag tag, String subTag, String label, int displayOrder) {
		super();
		this.tag = tag;
		this.subTag = subTag;
		this.label = label;
		this.displayOrder = displayOrder;
	}
    
	public static SubTag fromJson(final Tag tag, final JsonCommand command) {

        final String subTag = command.stringValueOfParameterNamed(SubTagParamName);
        Integer displayOrder = command.integerValueSansLocaleOfParameterNamed(DisplayOrderParamName);
        if (displayOrder == null) {
        	displayOrder = new Integer(0);
        }
        final String label = command.stringValueOfParameterNamed(LabelParamName);
        
        return new SubTag(tag, subTag,label, displayOrder);
    }
	
	public Map<String, Object> update(final JsonCommand command) {

        final Map<String, Object> actualChanges = new LinkedHashMap<>(1);
        if (command.isChangeInStringParameterNamed(SubTagParamName, this.subTag)) {
        	final String newValue = command.stringValueOfParameterNamed(SubTagParamName);
            actualChanges.put(SubTagParamName, newValue);
            actualChanges.put(SubTagParamName + "_old", this.subTag);
            this.subTag = StringUtils.defaultIfEmpty(newValue, null);
        }
        
        if (command.isChangeInStringParameterNamed(LabelParamName, this.label)) {
            final String newValue = command.stringValueOfParameterNamed(LabelParamName);
            actualChanges.put(LabelParamName, newValue);
            actualChanges.put(LabelParamName + "_old", this.label);
            this.label = StringUtils.defaultIfEmpty(newValue, "");
        }   
        
        if (command.isChangeInIntegerParameterNamed(DisplayOrderParamName, this.displayOrder)) {
            final Integer newValue = command.integerValueOfParameterNamed(DisplayOrderParamName);
            actualChanges.put(DisplayOrderParamName, newValue);
            actualChanges.put(DisplayOrderParamName + "_old", this.displayOrder);
            this.displayOrder = newValue == null ? 0 : newValue;
        }    
        return actualChanges;
    }
	
	public Tag getTag(){
		return this.tag;
	}
}
