package in.grasshoper.core.security.exception;


public class PlatformUnknownDBException extends RuntimeException {
	
	private final String globalisationMessageCode;
    private final String defaultUserMessage;
    private final String parameterName;
    private final Object[] defaultUserMessageArgs;

    public PlatformUnknownDBException() {
        this.globalisationMessageCode = "unknown.db.exception";
        this.defaultUserMessage = "Unknow Data base exception. Please contact system administrator.";
        this.parameterName = null;
        this.defaultUserMessageArgs = null;
    }

    public PlatformUnknownDBException(final String globalisationMessageCode, final String defaultUserMessage,
            final String parameterName, final Object... defaultUserMessageArgs) {
        this.globalisationMessageCode = globalisationMessageCode;
        this.defaultUserMessage = defaultUserMessage;
        this.parameterName = parameterName;
        this.defaultUserMessageArgs = defaultUserMessageArgs;
    }

    public String getGlobalisationMessageCode() {
        return this.globalisationMessageCode;
    }

    public String getDefaultUserMessage() {
        return this.defaultUserMessage;
    }

    public Object[] getDefaultUserMessageArgs() {
        return this.defaultUserMessageArgs;
    }

    public String getParameterName() {
        return this.parameterName;
    }
}
