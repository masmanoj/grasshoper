package in.grasshoper.core.infra;

import in.grasshoper.core.security.domain.BasicPasswordEncodablePublicUser;
import in.grasshoper.core.security.domain.PublicUser;
import in.grasshoper.core.security.service.PlatformPasswordEncoder;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDate;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

public final class JsonCommand {
	
	private final String jsonCommand;
    private final JsonElement parsedCommand;
    private final FromJsonHelper fromApiJsonHelper;
    private final Long resourceId;
	
	public static JsonCommand from(final Long resourceId, final String jsonCommand, final JsonElement parsedCommand,
			final FromJsonHelper fromApiJsonHelper) {
		return new JsonCommand(jsonCommand, parsedCommand, fromApiJsonHelper, resourceId);
	}
	public static JsonCommand from(final String jsonCommand, final JsonElement parsedCommand,
			final FromJsonHelper fromApiJsonHelper) {
		return new JsonCommand(jsonCommand, parsedCommand, fromApiJsonHelper, null);
	}
	
	public JsonCommand(final String jsonCommand, final JsonElement parsedCommand,
            final FromJsonHelper fromApiJsonHelper, final Long resourceId) {
		this.jsonCommand = jsonCommand;
		this.parsedCommand = parsedCommand;
		this.fromApiJsonHelper = fromApiJsonHelper;
		this.resourceId = resourceId;
	}
	
	public Long resourceId(){
		return this.resourceId;
	}
	
	public String stringValueOfParameterNamed(final String parameterName) {
        String value = this.fromApiJsonHelper.extractStringNamed(parameterName, this.parsedCommand);
        return StringUtils.defaultIfEmpty(value, "");
    }
	
	
	public Boolean booleanValueOfParameterNamed(final String parameterName) {
		return this.fromApiJsonHelper.extractBooleanNamed(parameterName, this.parsedCommand);
	}
	public Boolean booleanValueOfParameterNamedFalseIfNull(final String parameterName) {
		return (null == this.fromApiJsonHelper.extractBooleanNamed(parameterName, this.parsedCommand)? false :
			this.fromApiJsonHelper.extractBooleanNamed(parameterName, this.parsedCommand));
			
	}
	
	
	private boolean differenceExists(final String baseValue, final String workingCopyValue) {
        boolean differenceExists = false;

        if (StringUtils.isNotBlank(baseValue)) {
            differenceExists = !baseValue.equals(workingCopyValue);
        } else {
            differenceExists = StringUtils.isNotBlank(workingCopyValue);
        }

        return differenceExists;
    }
	
	public boolean parameterExists(final String parameterName) {
        return this.fromApiJsonHelper.parameterExists(parameterName, this.parsedCommand);
    }

	public String getJsonCommand() {
		return jsonCommand;
	}
	
	public JsonElement getParsedCommand() {
		return parsedCommand;
	}
	
	public boolean isChangeInStringParameterNamed(final String parameterName, final String existingValue) {
        boolean isChanged = false;
        if (parameterExists(parameterName)) {
            final String workingValue = stringValueOfParameterNamed(parameterName);
            isChanged = differenceExists(existingValue, workingValue);
        }
        return isChanged;
    }

    public String stringValueOfParameterNamedAllowingNull(final String parameterName) {
        return this.fromApiJsonHelper.extractStringNamed(parameterName, this.parsedCommand);
    }

    public Map<String, String> mapValueOfParameterNamed(final String json) {
        final Type typeOfMap = new TypeToken<Map<String, String>>() {}.getType();
        final Map<String, String> value = this.fromApiJsonHelper.extractDataMap(typeOfMap, json);
        return value;
    }

    public boolean isChangeInBigDecimalParameterNamedDefaultingZeroToNull(final String parameterName, final BigDecimal existingValue) {
        boolean isChanged = false;
        if (parameterExists(parameterName)) {
            final BigDecimal workingValue = bigDecimalValueOfParameterNamedDefaultToNullIfZero(parameterName);
            isChanged = differenceExists(existingValue, workingValue);
        }
        return isChanged;
    }

    public boolean isChangeInBigDecimalParameterNamed(final String parameterName, final BigDecimal existingValue) {
        boolean isChanged = false;
        if (parameterExists(parameterName)) {
            final BigDecimal workingValue = bigDecimalValueOfParameterNamed(parameterName);
            isChanged = differenceExists(existingValue, workingValue);
        }
        return isChanged;
    }

    public boolean isChangeInBigDecimalParameterNamed(final String parameterName, final BigDecimal existingValue, final Locale locale) {
        boolean isChanged = false;
        if (parameterExists(parameterName)) {
            final BigDecimal workingValue = bigDecimalValueOfParameterNamed(parameterName, locale);
            isChanged = differenceExists(existingValue, workingValue);
        }
        return isChanged;
    }

    public boolean isChangeInBigDecimalParameterNamedWithNullCheck(final String parameterName, final BigDecimal existingValue) {
        boolean isChanged = false;
        if (parameterExists(parameterName)) {
            final BigDecimal workingValue = bigDecimalValueOfParameterNamed(parameterName);
            if (workingValue == null && existingValue != null) {
                isChanged = true;
            } else {
                isChanged = differenceExists(existingValue, workingValue);
            }
        }
        return isChanged;
    }

    private static BigDecimal defaultToZeroIfNull(final BigDecimal value) {
        BigDecimal result = value;
        if (value == null) {
            result = BigDecimal.ZERO;
        }
        return result;
    }
    private static BigDecimal defaultToNullIfZero(final BigDecimal value) {
        BigDecimal result = value;
        if (value != null && BigDecimal.ZERO.compareTo(value) == 0) {
            result = null;
        }
        return result;
    }

    private static Integer defaultToNullIfZero(final Integer value) {
        Integer result = value;
        if (value != null && value == 0) {
            result = null;
        }
        return result;
    }
    
    private static Integer defaultToZeroIfNull(final Integer value) {
        Integer result = value;
        if (value == null) {
            result = new Integer(0);
        }
        return result;
    }

    public BigDecimal bigDecimalValueOfParameterNamed(final String parameterName) {
        return this.fromApiJsonHelper.extractBigDecimalWithLocaleNamed(parameterName, this.parsedCommand);
    }
    public BigDecimal bigDecimalValueOfParameterNamedZeroIfNull(final String parameterName) {
        return defaultToZeroIfNull(this.fromApiJsonHelper.extractBigDecimalWithLocaleNamed(parameterName, this.parsedCommand));
    }
    
    public BigDecimal bigDecimalValueOfParameterNamed(final String parameterName, final Locale locale) {
        return this.fromApiJsonHelper.extractBigDecimalNamed(parameterName, this.parsedCommand, locale);
    }

    public BigDecimal bigDecimalValueOfParameterNamedDefaultToNullIfZero(final String parameterName) {
        return defaultToNullIfZero(bigDecimalValueOfParameterNamed(parameterName));
    }

    public boolean isChangeInIntegerParameterNamedDefaultingZeroToNull(final String parameterName, final Integer existingValue) {
        boolean isChanged = false;
        if (parameterExists(parameterName)) {
            final Integer workingValue = integerValueOfParameterNamedDefaultToNullIfZero(parameterName);
            isChanged = differenceExists(existingValue, workingValue);
        }
        return isChanged;
    }

    public boolean isChangeInIntegerParameterNamed(final String parameterName, final Integer existingValue) {
        boolean isChanged = false;
        if (parameterExists(parameterName)) {
            final Integer workingValue = integerValueOfParameterNamed(parameterName);
            isChanged = differenceExists(existingValue, workingValue);
        }
        return isChanged;
    }

    public boolean isChangeInIntegerParameterNamed(final String parameterName, final Integer existingValue, final Locale locale) {
        boolean isChanged = false;
        if (parameterExists(parameterName)) {
            final Integer workingValue = integerValueOfParameterNamed(parameterName, locale);
            isChanged = differenceExists(existingValue, workingValue);
        }
        return isChanged;
    }

    public boolean isChangeInIntegerParameterNamedWithNullCheck(final String parameterName, final Integer existingValue) {
        boolean isChanged = false;
        if (parameterExists(parameterName)) {
            final Integer workingValue = integerValueOfParameterNamed(parameterName);
            if (workingValue == null && existingValue != null) {
                isChanged = true;
            } else {
                isChanged = differenceExists(existingValue, workingValue);
            }
        }
        return isChanged;
    }

    public Integer integerValueOfParameterNamed(final String parameterName) {
        return this.fromApiJsonHelper.extractIntegerWithLocaleNamed(parameterName, this.parsedCommand);
    }

    public Integer integerValueOfParameterNamed(final String parameterName, final Locale locale) {
        return this.fromApiJsonHelper.extractIntegerNamed(parameterName, this.parsedCommand, locale);
    }

    public Integer integerValueOfParameterNamedDefaultToNullIfZero(final String parameterName) {
        return defaultToNullIfZero(integerValueOfParameterNamed(parameterName));
    }

    public boolean isChangeInIntegerSansLocaleParameterNamed(final String parameterName, final Integer existingValue) {
        boolean isChanged = false;
        if (parameterExists(parameterName)) {
            final Integer workingValue = integerValueSansLocaleOfParameterNamed(parameterName);
            isChanged = differenceExists(existingValue, workingValue);
        }
        return isChanged;
    }

    public Integer integerValueSansLocaleOfParameterNamed(final String parameterName) {
        return this.fromApiJsonHelper.extractIntegerSansLocaleNamed(parameterName, this.parsedCommand);
    }
    
    public Integer integerValueSansLocaleOfParameterNamedZeroIfNull(final String parameterName) {
        return defaultToZeroIfNull(this.fromApiJsonHelper.extractIntegerSansLocaleNamed(parameterName, this.parsedCommand));
    }

    public boolean isChangeInBooleanParameterNamed(final String parameterName, final Boolean existingValue) {
        boolean isChanged = false;
        if (parameterExists(parameterName)) {
            final Boolean workingValue = booleanObjectValueOfParameterNamed(parameterName);
            isChanged = differenceExists(existingValue, workingValue);
        }
        return isChanged;
    }

    /**
     * Returns {@link Boolean} that could possibly be null.
     */
    public Boolean booleanObjectValueOfParameterNamed(final String parameterName) {
        return this.fromApiJsonHelper.extractBooleanNamed(parameterName, this.parsedCommand);
    }

    /**
     * always returns true or false
     */
    public boolean booleanPrimitiveValueOfParameterNamed(final String parameterName) {
        final Boolean value = this.fromApiJsonHelper.extractBooleanNamed(parameterName, this.parsedCommand);
        return (Boolean) ObjectUtils.defaultIfNull(value, Boolean.FALSE);
    }

    public boolean isChangeInArrayParameterNamed(final String parameterName, final String[] existingValue) {
        boolean isChanged = false;
        if (parameterExists(parameterName)) {
            final String[] workingValue = arrayValueOfParameterNamed(parameterName);
            isChanged = differenceExists(existingValue, workingValue);
        }
        return isChanged;
    }

    private boolean differenceExists(final LocalDate baseValue, final LocalDate workingCopyValue) {
        boolean differenceExists = false;

        if (baseValue != null) {
            differenceExists = !baseValue.equals(workingCopyValue);
        } else {
            differenceExists = workingCopyValue != null;
        }

        return differenceExists;
    }

    private boolean differenceExists(final String[] baseValue, final String[] workingCopyValue) {
        Arrays.sort(baseValue);
        Arrays.sort(workingCopyValue);
        return !Arrays.equals(baseValue, workingCopyValue);
    }

    private boolean differenceExists(final Number baseValue, final Number workingCopyValue) {
        boolean differenceExists = false;

        if (baseValue != null) {
            if (workingCopyValue != null) {
                differenceExists = !baseValue.equals(workingCopyValue);
            } else {
                differenceExists = true;
            }
        } else {
            differenceExists = workingCopyValue != null;
        }

        return differenceExists;
    }

    private boolean differenceExists(final BigDecimal baseValue, final BigDecimal workingCopyValue) {
        boolean differenceExists = false;

        if (baseValue != null) {
            if (workingCopyValue != null) {
                differenceExists = baseValue.compareTo(workingCopyValue) != 0;
            } else {
                differenceExists = true;
            }
        } else {
            differenceExists = workingCopyValue != null;
        }

        return differenceExists;
    }

    private boolean differenceExists(final Boolean baseValue, final Boolean workingCopyValue) {
        boolean differenceExists = false;

        if (baseValue != null) {
            differenceExists = !baseValue.equals(workingCopyValue);
        } else {
            differenceExists = workingCopyValue != null;
        }

        return differenceExists;
    }
 
    public String[] arrayValueOfParameterNamed(final String parameterName) {
        return this.fromApiJsonHelper.extractArrayNamed(parameterName, this.parsedCommand);
    }

    public JsonArray arrayOfParameterNamed(final String parameterName) {
        return this.fromApiJsonHelper.extractJsonArrayNamed(parameterName, this.parsedCommand);
    }

    public boolean isChangeInPasswordParameterNamed(final String parameterName, final String existingValue,
            final PlatformPasswordEncoder platformPasswordEncoder, final Long saltValue) {
        boolean isChanged = false;
        if (parameterExists(parameterName)) {
            final String workingValue = passwordValueOfParameterNamed(parameterName, platformPasswordEncoder, saltValue);
            isChanged = differenceExists(existingValue, workingValue);
        }
        return isChanged;
    }

    public String passwordValueOfParameterNamed(final String parameterName, final PlatformPasswordEncoder platformPasswordEncoder,
            final Long saltValue) {
        final String passwordPlainText = stringValueOfParameterNamed(parameterName);

        final PublicUser dummyPlatformUser = new BasicPasswordEncodablePublicUser(saltValue, "", passwordPlainText);
        return platformPasswordEncoder.encode(dummyPlatformUser);
    }

    public Locale extractLocale() {
        return this.fromApiJsonHelper.extractLocaleParameter(this.parsedCommand.getAsJsonObject());
    }

    public void checkForUnsupportedParameters(final Type typeOfMap, final String json, final Set<String> requestDataParameters) {
        this.fromApiJsonHelper.checkForUnsupportedParameters(typeOfMap, json, requestDataParameters);
    }

    public JsonObject jsonObjectOfParameterNamed(final String parameterName) {
        return this.fromApiJsonHelper.extractJsonObjectOfParameterNamed(parameterName, this.parsedCommand);
    }
    public boolean isChangeInLongParameterNamed(final String parameterName, final Long existingValue) {
        boolean isChanged = false;
        if (parameterExists(parameterName)) {
            final Long workingValue = longValueOfParameterNamed(parameterName);
            isChanged = differenceExists(existingValue, workingValue);
        }
        return isChanged;
    }
    public Long longValueOfParameterNamed(final String parameterName) {
        return this.fromApiJsonHelper.extractLongNamed(parameterName, this.parsedCommand);
    }

    
}
