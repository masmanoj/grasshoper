package in.grasshoper.field.product.domain;

import static in.grasshoper.field.product.productConstants.DisplayOrderParamName;
import static in.grasshoper.field.product.productConstants.ImageLabelParamName;
import static in.grasshoper.field.product.productConstants.ImageUrlParamName;
import static in.grasshoper.field.product.productConstants.IsActiveParamName;
import in.grasshoper.core.infra.JsonCommand;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Table(name = "g_product_images")
public class ProductImage extends AbstractPersistable<Long>{
	@ManyToOne(optional = false)
    @JoinColumn(name = "product_id", referencedColumnName = "id", nullable = false)
	private Product product;
	
	@Column(name = "image_url", nullable = false, length = 250)
	private String imageUrl;
	
	@Column(name = "display_order")
    private Integer displayOrder;
	
	@Column(name = "label", nullable = false, length = 150)
	private String label;
	
	@Column(name = "is_active", nullable = false)
	private Boolean isActive;
	
	protected ProductImage(){}
	
	private ProductImage(Product product, String imageUrl,
			Integer displayOrder, String label, Boolean isActive) {
		super();
		this.product = product;
		this.imageUrl = imageUrl;
		this.displayOrder = displayOrder;
		this.label = label;
		this.isActive = isActive;
	}

	public static ProductImage fromJson(final JsonCommand command, final Product product) {
		final String imageUrl =  command.stringValueOfParameterNamed(ImageUrlParamName);
		final Integer displayOrder = command.integerValueSansLocaleOfParameterNamedZeroIfNull(DisplayOrderParamName);
		final String label = command.stringValueOfParameterNamed(ImageLabelParamName);
		final Boolean isActive = command.booleanValueOfParameterNamedFalseIfNull(IsActiveParamName);
		
		return new ProductImage(product, imageUrl, displayOrder, label, isActive);
	}
	
	public Map<String, Object> update(final JsonCommand command) {

        final Map<String, Object> actualChanges = new LinkedHashMap<>(1);
        if (command.isChangeInStringParameterNamed(ImageUrlParamName, this.imageUrl)) {
            final String newValue = command.stringValueOfParameterNamed(ImageUrlParamName);
            actualChanges.put(ImageUrlParamName, newValue);
            actualChanges.put(ImageUrlParamName + "_old", this.imageUrl);
            this.imageUrl = StringUtils.defaultIfEmpty(newValue, null);
        }
        if (command.isChangeInIntegerParameterNamed(DisplayOrderParamName, this.displayOrder)) {
            final Integer newValue = command.integerValueOfParameterNamed(DisplayOrderParamName);
            actualChanges.put(DisplayOrderParamName, newValue);
            actualChanges.put(DisplayOrderParamName + "_old", this.displayOrder);
            this.displayOrder = newValue == null ? 0 : newValue;
        } 
        if (command.isChangeInStringParameterNamed(ImageLabelParamName, this.label)) {
            final String newValue = command.stringValueOfParameterNamed(ImageLabelParamName);
            actualChanges.put(ImageLabelParamName, newValue);
            actualChanges.put(ImageLabelParamName + "_old", this.label);
            this.label = StringUtils.defaultIfEmpty(newValue, null);
        }
        if (command.isChangeInBooleanParameterNamed(IsActiveParamName, this.isActive)) {
            final Boolean newValue = command.booleanValueOfParameterNamed(IsActiveParamName);
            actualChanges.put(IsActiveParamName, newValue);
            actualChanges.put(IsActiveParamName + "_old", this.isActive);
            this.isActive = newValue;
        }     
        return actualChanges;
	}
}
