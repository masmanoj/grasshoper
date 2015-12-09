package in.grasshoper.field.order.data;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import in.grasshoper.core.data.ApiParameterError;
import in.grasshoper.core.data.DataValidatorBuilder;
import in.grasshoper.core.exception.InvalidJsonException;
import in.grasshoper.core.infra.FromJsonHelper;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import static in.grasshoper.field.order.OrderConstants.*;
import static in.grasshoper.user.UserConstants.NameParamName;


@Component
public class OrderDataValidator {
	private final FromJsonHelper fromApiJsonHelper;

	@Autowired
	public OrderDataValidator(final FromJsonHelper fromApiJsonHelper) {
		super();
		this.fromApiJsonHelper = fromApiJsonHelper;
	}
	
	public void validateCreate(final String json){
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
        baseDataValidator.reset().parameter(NameParamName).value(name).notNull().notExceedingLengthOf(100) ;
        
        final Long pickUpAddress = this.fromApiJsonHelper.extractLongNamed(pickupAddressIdParamName, element);
        baseDataValidator.reset().parameter(pickupAddressIdParamName).value(pickUpAddress).notNull().longGreaterThanZero();
        
        final Long dropAddress = this.fromApiJsonHelper.extractLongNamed(dropAddressIdParamName, element);
        baseDataValidator.reset().parameter(dropAddressIdParamName).value(dropAddress).notNull().longGreaterThanZero();
        
        baseDataValidator.throwExceptionIfValidationWarningsExist(dataValidationErrors);
	}

}
