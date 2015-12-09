package in.grasshoper.core.infra;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.JsonElement;

public final class JsonCommand {
	
	private final String jsonCommand;
    private final JsonElement parsedCommand;
    private final FromJsonHelper fromApiJsonHelper;
	
	public static JsonCommand from(final String jsonCommand, final JsonElement parsedCommand,
			final FromJsonHelper fromApiJsonHelper) {
		return new JsonCommand(jsonCommand, parsedCommand, fromApiJsonHelper);
	}
	
	public JsonCommand(final String jsonCommand, final JsonElement parsedCommand,
            final FromJsonHelper fromApiJsonHelper) {
		this.jsonCommand = jsonCommand;
		this.parsedCommand = parsedCommand;
		this.fromApiJsonHelper = fromApiJsonHelper;
	}
	
	public boolean isChangeInStringParameterNamed(final String parameterName, final String existingValue) {
        boolean isChanged = false;
        if (parameterExists(parameterName)) {
            final String workingValue = stringValueOfParameterNamed(parameterName);
            isChanged = differenceExists(existingValue, workingValue);
        }
        return isChanged;
    }
	
	public String stringValueOfParameterNamed(final String parameterName) {
        return  this.fromApiJsonHelper.extractStringNamed(parameterName, this.parsedCommand);
        //return StringUtils.defaultIfEmpty(value, "");
    }
	
	public BigDecimal bigDecimalValueOfParameterNamed(final String parameterName) {
		return this.fromApiJsonHelper.extractBigDecimalNamedWithLocaleEN(parameterName, this.parsedCommand);
	}
	public Boolean booleanValueOfParameterNamed(final String parameterName) {
		return this.fromApiJsonHelper.extractBooleanNamed(parameterName, this.parsedCommand);
	}
	
	public Integer integerValueOfParameterNamed(final String parameterName) {
		return this.fromApiJsonHelper.extractIntegerWithLocaleNamed(parameterName, this.parsedCommand);
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

}
