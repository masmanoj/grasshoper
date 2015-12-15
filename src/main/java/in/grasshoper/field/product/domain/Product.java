package in.grasshoper.field.product.domain;

import static in.grasshoper.field.product.productConstants.Desc0ParamName;
import static in.grasshoper.field.product.productConstants.Desc1ParamName;
import static in.grasshoper.field.product.productConstants.Desc2ParamName;
import static in.grasshoper.field.product.productConstants.IsActiveParamName;
import static in.grasshoper.field.product.productConstants.IsSoldOutParamName;
import static in.grasshoper.field.product.productConstants.NameParamName;
import static in.grasshoper.field.product.productConstants.QuantityParamName;
import static in.grasshoper.field.product.productConstants.QuantityUnitParamName;
import in.grasshoper.core.infra.JsonCommand;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Table(name = "g_product")
public class Product extends AbstractPersistable<Long>{
	@Column(name = "name", nullable = false, length = 150)
	private String name;
	@Column(name = "desc0", nullable = false, length = 350)
	private String desc0;
	@Column(name = "desc1", length = 350)
	private String desc1;
	@Column(name = "desc2", length = 350)
	private String desc2;
	@Column(name = "quantity", nullable = false)
	private BigDecimal quantity;
	@Column(name = "quantity_unit", nullable = false, length = 10)
	private String quantityUnit;
	@Column(name = "is_sold_out", nullable = false)
	private Boolean isSoldOut;
	@Column(name = "is_active", nullable = false)
	private Boolean isActive;
	
	protected Product(){}

	private Product(String name, String desc0, String desc1, String desc2,
			BigDecimal quantity, String quantityUnit, Boolean isSoldOut,
			Boolean isActive) {
		super();
		this.name = name;
		this.desc0 = desc0;
		this.desc1 = desc1;
		this.desc2 = desc2;
		this.quantity = quantity;
		this.quantityUnit = quantityUnit;
		this.isSoldOut = isSoldOut;
		this.isActive = isActive;
	}
	
	
	public static Product fromJson(final JsonCommand command) {
        final String name = command.stringValueOfParameterNamed(NameParamName);
        final String desc0 = command.stringValueOfParameterNamed(Desc0ParamName);
        final String desc1 = command.stringValueOfParameterNamed(Desc1ParamName);
        final String desc2 = command.stringValueOfParameterNamed(Desc2ParamName);
        final BigDecimal quantity = command.bigDecimalValueOfParameterNamed(QuantityParamName);
        final String quantityUnit = command.stringValueOfParameterNamed(QuantityUnitParamName);
        final Boolean isSoldOut = command.booleanValueOfParameterNamed(IsSoldOutParamName);
        final Boolean isActive = command.booleanValueOfParameterNamed(IsActiveParamName);
        return new Product(name, desc0, desc1, desc2, quantity, quantityUnit, isSoldOut, isActive);
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
        return actualChanges;
	}
}
