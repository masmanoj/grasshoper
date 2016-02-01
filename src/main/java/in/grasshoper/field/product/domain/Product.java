package in.grasshoper.field.product.domain;

import static in.grasshoper.field.product.productConstants.Desc0ParamName;
import static in.grasshoper.field.product.productConstants.Desc1ParamName;
import static in.grasshoper.field.product.productConstants.Desc2ParamName;
import static in.grasshoper.field.product.productConstants.IsActiveParamName;
import static in.grasshoper.field.product.productConstants.IsSoldOutParamName;
import static in.grasshoper.field.product.productConstants.MinimumQuantityParamName;
import static in.grasshoper.field.product.productConstants.NameParamName;
import static in.grasshoper.field.product.productConstants.PricePerUnitParamName;
import static in.grasshoper.field.product.productConstants.ProductUidParamName;
import static in.grasshoper.field.product.productConstants.QuantityParamName;
import static in.grasshoper.field.product.productConstants.QuantityUnitParamName;
import in.grasshoper.core.infra.JsonCommand;
import in.grasshoper.field.tag.domain.SubTag;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Table(name = "g_product")
public class Product extends AbstractPersistable<Long>{
	@Column(name = "product_uid", nullable = false, length = 150)
	private String productUid;
	@Column(name = "name", nullable = false, length = 150)
	private String name;
	@Column(name = "desc0", nullable = false, length = 350)
	private String desc0;
	@Column(name = "desc1", length = 350)
	private String desc1;
	@Column(name = "desc2", length = 350)
	private String desc2;
	@Column(name = "quantity", nullable = true)
	private BigDecimal quantity;
	@Column(name = "quantity_unit", nullable = true, length = 10)
	private String quantityUnit;
	@Column(name = "price_per_unit", nullable = true)
	private BigDecimal pricePerUnit;
	@Column(name="min_quantity", nullable= true)
	private BigDecimal minimumQuantity;
	@Column(name = "is_sold_out", nullable = false)
	private Boolean isSoldOut;
	@Column(name = "is_active", nullable = false)
	private Boolean isActive;
	
	@ManyToMany
	@JoinTable(name = "g_product_packing_styles", joinColumns = @JoinColumn(name = "product_id"), inverseJoinColumns = @JoinColumn(name = "style_id"))
	private Set<SubTag> packingStyles;  
	@ManyToMany
	@JoinTable(name = "g_product_categories", joinColumns = @JoinColumn(name = "product_id"), inverseJoinColumns = @JoinColumn(name = "category_id"))
	private Set<SubTag> categories;  
	
	@LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "product", orphanRemoval = true)
    private List<ProductImage> productImages = null;
	
	protected Product(){}

	private Product(final String productUid, final String name, final String desc0, final String desc1, 
			final String desc2, final BigDecimal quantity, final String quantityUnit, final Boolean isSoldOut,
			final Boolean isActive,final BigDecimal pricePerUnit, final BigDecimal minimumQuantity,
			final Set<SubTag> packingStyles, final Set<SubTag> categories) {
		super();
		this.productUid = productUid;
		this.name = name;
		this.desc0 = desc0;
		this.desc1 = desc1;
		this.desc2 = desc2;
		this.quantity = quantity;
		this.quantityUnit = quantityUnit;
		this.isSoldOut = isSoldOut;
		this.isActive = isActive;
		this.packingStyles = packingStyles;
		this.pricePerUnit = pricePerUnit;
		this.minimumQuantity = minimumQuantity;
		this.categories = categories;
		this.productImages = new LinkedList<>();
	}
	
	
	public static Product fromJson(final JsonCommand command, final Set<SubTag> packingStyles,
			final Set<SubTag> categories) {
        final String name = command.stringValueOfParameterNamed(NameParamName);
        final String productUid = command.stringValueOfParameterNamed(ProductUidParamName);
        final String desc0 = command.stringValueOfParameterNamed(Desc0ParamName);
        final String desc1 = command.stringValueOfParameterNamed(Desc1ParamName);
        final String desc2 = command.stringValueOfParameterNamed(Desc2ParamName);
        final BigDecimal quantity = command.bigDecimalValueOfParameterNamedZeroIfNull(QuantityParamName);
        final String quantityUnit = command.stringValueOfParameterNamed(QuantityUnitParamName);
        final Boolean isSoldOut = command.booleanValueOfParameterNamedFalseIfNull(IsSoldOutParamName);
        final Boolean isActive = command.booleanValueOfParameterNamedFalseIfNull(IsActiveParamName);
        final BigDecimal pricePerUnit = command.bigDecimalValueOfParameterNamedZeroIfNull(PricePerUnitParamName);
        final BigDecimal minimumQuantity = command.bigDecimalValueOfParameterNamedZeroIfNull(MinimumQuantityParamName);
        return new Product(productUid, name, desc0, desc1, desc2, quantity, quantityUnit, 
        		isSoldOut, isActive, pricePerUnit, minimumQuantity, packingStyles, categories);
    }
	

	public Map<String, Object> update(final JsonCommand command) {

        final Map<String, Object> actualChanges = new LinkedHashMap<>(1);
        if (command.isChangeInStringParameterNamed(NameParamName, this.name)) {
            final String newValue = command.stringValueOfParameterNamed(NameParamName);
            actualChanges.put(NameParamName, newValue);
            actualChanges.put(NameParamName + "_old", this.name);
            this.name = StringUtils.defaultIfEmpty(newValue, null);
        }
        
        if (command.isChangeInStringParameterNamed(Desc0ParamName, this.desc0)) {
            final String newValue = command.stringValueOfParameterNamed(Desc0ParamName);
            actualChanges.put(Desc0ParamName, newValue);
            actualChanges.put(Desc0ParamName + "_old", this.desc0);
            this.desc0 = StringUtils.defaultIfEmpty(newValue, null);
        }
        
        if (command.isChangeInStringParameterNamed(Desc1ParamName, this.desc1)) {
            final String newValue = command.stringValueOfParameterNamed(Desc1ParamName);
            actualChanges.put(Desc1ParamName, newValue);
            actualChanges.put(Desc1ParamName + "_old", this.desc1);
            this.desc1 = StringUtils.defaultIfEmpty(newValue, null);
        }
        
        if (command.isChangeInStringParameterNamed(Desc2ParamName, this.desc2)) {
            final String newValue = command.stringValueOfParameterNamed(Desc2ParamName);
            actualChanges.put(Desc2ParamName, newValue);
            actualChanges.put(Desc2ParamName + "_old", this.desc2);
            this.desc2 = StringUtils.defaultIfEmpty(newValue, null);
        }
        
        if (command.isChangeInBigDecimalParameterNamed(QuantityParamName, this.quantity)) {
        	final BigDecimal newValue = command.bigDecimalValueOfParameterNamed(QuantityParamName);
            actualChanges.put(QuantityParamName, newValue);
            actualChanges.put(QuantityParamName + "_old", this.quantity);
            this.quantity = newValue;
        }
        
        if (command.isChangeInStringParameterNamed(QuantityUnitParamName, this.quantityUnit)) {
            final String newValue = command.stringValueOfParameterNamed(QuantityUnitParamName);
            actualChanges.put(QuantityUnitParamName, newValue);
            actualChanges.put(QuantityUnitParamName + "_old", this.quantityUnit);
            this.quantityUnit = StringUtils.defaultIfEmpty(newValue, null);
        }
        
        if (command.isChangeInBooleanParameterNamed(IsSoldOutParamName, this.isSoldOut)) {
        	final Boolean newValue = command.booleanValueOfParameterNamed(IsSoldOutParamName);
            actualChanges.put(IsSoldOutParamName, newValue);
            actualChanges.put(IsSoldOutParamName + "_old", this.isSoldOut);
            this.isSoldOut = newValue;
        }
        
        if (command.isChangeInBooleanParameterNamed(IsActiveParamName, this.isActive)) {
            final Boolean newValue = command.booleanValueOfParameterNamed(IsActiveParamName);
            actualChanges.put(IsActiveParamName, newValue);
            actualChanges.put(IsActiveParamName + "_old", this.isActive);
            this.isActive = newValue;
        }
        
        if (command.isChangeInBigDecimalParameterNamed(PricePerUnitParamName, this.pricePerUnit)) {
        	final BigDecimal newValue = command.bigDecimalValueOfParameterNamed(PricePerUnitParamName);
            actualChanges.put(PricePerUnitParamName, newValue);
            actualChanges.put(PricePerUnitParamName + "_old", this.pricePerUnit);
            this.pricePerUnit = newValue;
        }
        
        if (command.isChangeInBigDecimalParameterNamed(MinimumQuantityParamName, this.minimumQuantity)) {
        	final BigDecimal newValue = command.bigDecimalValueOfParameterNamed(MinimumQuantityParamName);
            actualChanges.put(MinimumQuantityParamName, newValue);
            actualChanges.put(MinimumQuantityParamName + "_old", this.minimumQuantity);
            this.minimumQuantity = newValue;
        }
        return actualChanges;
	}
	public Set<SubTag> getPackingStyles(){
		return this.packingStyles;
	}
	public void updatePackingStyles(Set<SubTag> packingStyles){
		this.packingStyles = packingStyles;
	}
	public Set<SubTag> getCategories(){
		return this.categories;
	}
	public void updateCategories(Set<SubTag> categories){
		this.categories = categories;
	}
	
	public List<ProductImage> getProductImages(){
		return this.productImages;
	}
	public ProductImage getProductImageById(Long id){
		for(ProductImage img : this.productImages){
			if(img.getId().equals(id))
				return img;
		}
		
		return null;
	}
	public void addProductImage(final ProductImage productImage){
		this.productImages.add(productImage);
	}
	public ProductImage lastAddedProductImage(){
		ProductImage lastProductImage = null;
		Iterator<ProductImage> iterator = this.productImages.iterator();
		while (iterator.hasNext()) { lastProductImage = iterator.next(); }
		return lastProductImage;
	}
	public void removeProductImages(final ProductImage productImage){
		this.productImages.remove(productImage);
	}
	
	public BigDecimal getQuantity(){
		return this.quantity;
	}
	public void updateQuantity(BigDecimal quantity){
		this.quantity = quantity;
	}
	
	public BigDecimal getPricePerUnit(){
		return pricePerUnit;
	}
	
	public BigDecimal getMinQuantity(){
		return this.minimumQuantity;
	}
	public String getQuantityUnit(){
		return this.quantityUnit;
	}

	public Map<String, Object> updateQuantityFromCommand(JsonCommand command) {
		final Map<String, Object> actualChanges = new LinkedHashMap<>(1);
		if (command.isChangeInBigDecimalParameterNamed(QuantityParamName, this.quantity)) {
        	final BigDecimal newValue = command.bigDecimalValueOfParameterNamed(QuantityParamName);
            actualChanges.put(QuantityParamName, newValue);
            actualChanges.put(QuantityParamName + "_old", this.quantity);
            this.quantity = newValue;
		}
		return actualChanges;
	}
}
