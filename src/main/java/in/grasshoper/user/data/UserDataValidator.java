package in.grasshoper.user.data;

import static in.grasshoper.user.UserConstants.CreateUserParams;
import static in.grasshoper.user.UserConstants.EmailParamName;
import static in.grasshoper.user.UserConstants.NameParamName;
import static in.grasshoper.user.UserConstants.PasswordParamName;
import static in.grasshoper.user.UserConstants.PhoneNumberParamName;
import static in.grasshoper.user.UserConstants.ReturnUrlParamName;
import static in.grasshoper.user.UserConstants.USER_RESOURCE;
import in.grasshoper.core.data.ApiParameterError;
import in.grasshoper.core.data.DataValidatorBuilder;
import in.grasshoper.core.exception.InvalidJsonException;
import in.grasshoper.core.infra.FromJsonHelper;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

@Component
public class UserDataValidator {

	private final FromJsonHelper fromApiJsonHelper;

	@Autowired
	public UserDataValidator(FromJsonHelper fromApiJsonHelper) {
		super();
		this.fromApiJsonHelper = fromApiJsonHelper;
	}
	
	
	public void validateCreate(final String json){
		if (StringUtils.isBlank(json)) {
			throw new InvalidJsonException();
		}
		final List<ApiParameterError> dataValidationErrors = new ArrayList<>();
		final DataValidatorBuilder baseDataValidator = new DataValidatorBuilder(
				dataValidationErrors).resource(USER_RESOURCE);
		
		
		final Type typeOfMap = new TypeToken<Map<String, Object>>() {}.getType();
        this.fromApiJsonHelper.checkForUnsupportedParameters(typeOfMap, json , CreateUserParams);
        
        final JsonElement element = this.fromApiJsonHelper.parse(json);
        
        final String name = this.fromApiJsonHelper.extractStringNamed(NameParamName, element);
        baseDataValidator.reset().parameter(NameParamName).value(name).notNull().notExceedingLengthOf(100) ;
        final String email = this.fromApiJsonHelper.extractStringNamed(EmailParamName, element);
        baseDataValidator.reset().parameter(EmailParamName).value(email).notNull().notExceedingLengthOf(100).isValidEmail() ;
        //add validate email
       
        
        
        final String url = this.fromApiJsonHelper.extractStringNamed(ReturnUrlParamName, element);
        baseDataValidator.reset().parameter(ReturnUrlParamName).value(url).notNull().notExceedingLengthOf(255) ;
        
        
        final String passwd = this.fromApiJsonHelper.extractStringNamed(PasswordParamName, element);
        baseDataValidator.reset().parameter(PasswordParamName).value(passwd).notNull().notExceedingLengthOf(100) ;
        
        //final String passwdC = this.fromApiJsonHelper.extractStringNamed(ConfirmPasswordParamName, element);
        //baseDataValidator.reset().parameter(ConfirmPasswordParamName).value(passwdC).notNull()
        //	.notExceedingLengthOf(100).equalToParameter(PasswordParamName, passwd) ;
        
        final String phone = this.fromApiJsonHelper.extractStringNamed(PhoneNumberParamName, element);
        baseDataValidator.reset().parameter(PhoneNumberParamName).value(phone).notNull().notExceedingLengthOf(12).validatePhoneNumber() ;
        
        baseDataValidator.throwExceptionIfValidationWarningsExist(dataValidationErrors);
	}
}
