package in.grasshoper.field.address.data;

import static in.grasshoper.field.address.AddressContants.*;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import in.grasshoper.core.data.ApiParameterError;
import in.grasshoper.core.data.DataValidatorBuilder;
import in.grasshoper.core.exception.InvalidJsonException;
import in.grasshoper.core.infra.FromJsonHelper;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

@Component
public class AddressDataValidator {
	private final FromJsonHelper fromApiJsonHelper;

	@Autowired
	public AddressDataValidator(final FromJsonHelper fromApiJsonHelper) {
		super();
		this.fromApiJsonHelper = fromApiJsonHelper;
	}
	
	public void validateForCreate(String json) {
		if (StringUtils.isBlank(json)) {
			throw new InvalidJsonException();
		}
		final List<ApiParameterError> dataValidationErrors = new ArrayList<>();
		final DataValidatorBuilder baseDataValidator = new DataValidatorBuilder(
				dataValidationErrors).resource(ADDRESS_RESOURCE);
		
		final Type typeOfMap = new TypeToken<Map<String, Object>>() {}.getType();
		
		this.fromApiJsonHelper.checkForUnsupportedParameters(typeOfMap, json , CreateAddressParams);
		
		final JsonElement element = this.fromApiJsonHelper.parse(json);
		
		final String name = this.fromApiJsonHelper.extractStringNamed(NameParamName, element);
        baseDataValidator.reset().parameter(NameParamName).value(name).notNull().notExceedingLengthOf(100) ;
        
        final String addrln1 = this.fromApiJsonHelper.extractStringNamed(AddressLine1ParamName, element);
        baseDataValidator.reset().parameter(AddressLine1ParamName).value(addrln1).notNull().notExceedingLengthOf(100);
        
        final String addrln2 = this.fromApiJsonHelper.extractStringNamed(AddressLine2ParamName, element);
        baseDataValidator.reset().parameter(AddressLine2ParamName).value(addrln2).notExceedingLengthOf(100) ;
        
        final String addrln3 = this.fromApiJsonHelper.extractStringNamed(AddressLine3ParamName, element);
        baseDataValidator.reset().parameter(AddressLine3ParamName).value(addrln3).notExceedingLengthOf(100) ;
        
        final String area = this.fromApiJsonHelper.extractStringNamed(AreaParamName, element);
        baseDataValidator.reset().parameter(AreaParamName).value(area).notExceedingLengthOf(100) ;
        
        final String landmark = this.fromApiJsonHelper.extractStringNamed(LandmarkParamName, element);
        baseDataValidator.reset().parameter(LandmarkParamName).value(landmark).notNull().notExceedingLengthOf(100);
        
        final String city = this.fromApiJsonHelper.extractStringNamed(CityParamName, element);
        baseDataValidator.reset().parameter(CityParamName).value(city).notNull().notExceedingLengthOf(100);
        
        final String pin = this.fromApiJsonHelper.extractStringNamed(PinParamName, element);
        baseDataValidator.reset().parameter(PinParamName).value(pin).notNull().validatePinNumber();
        
        final String phone = this.fromApiJsonHelper.extractStringNamed(ContactNumberParamName, element);
        baseDataValidator.reset().parameter(ContactNumberParamName).value(phone).notNull().validatePhoneNumber();
        

        final String extra = this.fromApiJsonHelper.extractStringNamed(ExtraInfoParamName, element);
        baseDataValidator.reset().parameter(ExtraInfoParamName).value(extra).notExceedingLengthOf(255);
        
        final BigDecimal latitude = this.fromApiJsonHelper.extractBigDecimalWithLocaleNamed(LatitudeParamName, element);
        baseDataValidator.reset().parameter(LatitudeParamName).value(latitude);
        
        final Integer type = this.fromApiJsonHelper.extractIntegerWithLocaleNamed(AddressTypeParamName, element);
        baseDataValidator.reset().parameter(AddressTypeParamName).value(type);
       
        
        baseDataValidator.throwExceptionIfValidationWarningsExist(dataValidationErrors);
	}

}
