package in.grasshoper.field.order.data;

import static in.grasshoper.field.order.OrderConstants.AdditionalNoteParamName;
import static in.grasshoper.field.order.OrderConstants.CartProductPkgStyleParamName;
import static in.grasshoper.field.order.OrderConstants.CartProductQuantityParamName;
import static in.grasshoper.field.order.OrderConstants.CartProductUidParam;
import static in.grasshoper.field.order.OrderConstants.CreateOrderParams;
import static in.grasshoper.field.order.OrderConstants.DropAddressIdParamName;
import static in.grasshoper.field.order.OrderConstants.EmailCheckParamName;
import static in.grasshoper.field.order.OrderConstants.ORDER_RESOURCE;
import static in.grasshoper.field.order.OrderConstants.OrderCartListParamName;
import static in.grasshoper.field.order.OrderConstants.OrderCartParams;
import static in.grasshoper.field.order.OrderConstants.OrderStatusParamName;
import static in.grasshoper.field.order.OrderConstants.PickupAddressIdParamName;
import static in.grasshoper.field.order.OrderConstants.StatusNoteParamName;
import static in.grasshoper.field.order.OrderConstants.UpdateOrderStatusParams;
import static in.grasshoper.user.UserConstants.NameParamName;
import in.grasshoper.core.data.ApiParameterError;
import in.grasshoper.core.data.DataValidatorBuilder;
import in.grasshoper.core.exception.InvalidJsonException;
import in.grasshoper.core.infra.FromJsonHelper;
import in.grasshoper.field.order.domain.OrderStatus;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;


@Component
public class OrderDataValidator {
	private final FromJsonHelper fromApiJsonHelper;

	@Autowired
	public OrderDataValidator(final FromJsonHelper fromApiJsonHelper) {
		super();
		this.fromApiJsonHelper = fromApiJsonHelper;
	}
	
	public void validateForCreate(final String json){
		if (StringUtils.isBlank(json)) {
			throw new InvalidJsonException();
		}
		final List<ApiParameterError> dataValidationErrors = new ArrayList<>();
		final DataValidatorBuilder baseDataValidator = new DataValidatorBuilder(
				dataValidationErrors).resource(ORDER_RESOURCE);
		
		final Type typeOfMap = new TypeToken<Map<String, Object>>() {}.getType();
		
		this.fromApiJsonHelper.checkForUnsupportedParameters(typeOfMap, json , CreateOrderParams);
		
		final JsonElement element = this.fromApiJsonHelper.parse(json);
	        
		final String name = this.fromApiJsonHelper.extractStringNamed(NameParamName, element);
        baseDataValidator.reset().parameter(NameParamName).value(name).notExceedingLengthOf(100);
        
        final String note = this.fromApiJsonHelper.extractStringNamed(AdditionalNoteParamName, element);
        baseDataValidator.reset().parameter(AdditionalNoteParamName).value(note).notExceedingLengthOf(300) ;
        
        final Long pickUpAddress = this.fromApiJsonHelper.extractLongNamed(PickupAddressIdParamName, element);
        baseDataValidator.reset().parameter(PickupAddressIdParamName).value(pickUpAddress).longGreaterThanZero();
        
        final Long dropAddress = this.fromApiJsonHelper.extractLongNamed(DropAddressIdParamName, element);
        baseDataValidator.reset().parameter(DropAddressIdParamName).value(dropAddress).notNull().longGreaterThanZero();
        
        if (element.isJsonObject()) {
			final JsonObject topLevelJsonElement = element.getAsJsonObject();
			if (topLevelJsonElement.has(OrderCartListParamName)
					&& topLevelJsonElement.get(OrderCartListParamName)
					.isJsonArray()) {
				final JsonArray array = topLevelJsonElement.get(
						OrderCartListParamName).getAsJsonArray();
				baseDataValidator.reset().parameter(OrderCartListParamName)
				.value(array).jsonArrayNotEmpty();
				
				for (int i = 0; i < array.size(); i++) {
					final JsonObject cartElemnt = array.get(i)
							.getAsJsonObject();
					 this.fromApiJsonHelper.checkForUnsupportedParameters(typeOfMap, this.fromApiJsonHelper.toJson(cartElemnt),OrderCartParams);
					 final String productUid = this.fromApiJsonHelper.extractStringNamed(CartProductUidParam, cartElemnt);
				        baseDataValidator.reset().parameter(CartProductUidParam).value(productUid).notNull().notExceedingLengthOf(15) ;
				        
				     final BigDecimal qty = this.fromApiJsonHelper.extractBigDecimalWithLocaleNamed(CartProductQuantityParamName, cartElemnt);
				        baseDataValidator.reset().parameter(CartProductQuantityParamName).value(qty).notNull().positiveAmount();
				     
				     final BigDecimal pkgStyleId = this.fromApiJsonHelper.extractBigDecimalWithLocaleNamed(CartProductPkgStyleParamName, cartElemnt);
				        baseDataValidator.reset().parameter(CartProductPkgStyleParamName).value(pkgStyleId).notNull().positiveAmount();
				        
				}
			}
		}
        
        
        baseDataValidator.throwExceptionIfValidationWarningsExist(dataValidationErrors);
	}

	public void validateForUpdateStatus(final String json){
		if (StringUtils.isBlank(json)) {
			throw new InvalidJsonException();
		}
		final List<ApiParameterError> dataValidationErrors = new ArrayList<>();
		final DataValidatorBuilder baseDataValidator = new DataValidatorBuilder(
				dataValidationErrors).resource(ORDER_RESOURCE);
		
		final Type typeOfMap = new TypeToken<Map<String, Object>>() {}.getType();
		
		this.fromApiJsonHelper.checkForUnsupportedParameters(typeOfMap, json , UpdateOrderStatusParams);
		
		final JsonElement element = this.fromApiJsonHelper.parse(json);
		
		final Integer statusCode = this.fromApiJsonHelper.extractIntegerWithLocaleNamed(OrderStatusParamName, element);
        baseDataValidator.reset().parameter(OrderStatusParamName).value(statusCode).notNull().integerGreaterThanZero();
        
        if(!OrderStatus.getAllStatusCodes().contains(statusCode)){
        	baseDataValidator.reset().parameter(OrderStatusParamName).value(statusCode).failWithCode("error.msg.invalid.status", "Status Provided is invalid");
        }
        
        final Boolean chkEmail = this.fromApiJsonHelper.extractBooleanNamed(EmailCheckParamName, element);
        baseDataValidator.reset().parameter(EmailCheckParamName).value(chkEmail).notNull().trueOrFalseRequired(true);
        
        if(this.fromApiJsonHelper.parameterExists(StatusNoteParamName, element)){
	        final String note = this.fromApiJsonHelper.extractStringNamed(StatusNoteParamName, element);
	        baseDataValidator.reset().parameter(StatusNoteParamName).value(note).notNull().notExceedingLengthOf(200);
        }
        
        baseDataValidator.throwExceptionIfValidationWarningsExist(dataValidationErrors);
	}
	    
}
