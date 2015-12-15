package in.grasshoper.core.exception;

public class AbstractPlatformRuleException extends RuntimeException {

    private final String globalisationMessageCode;
    private final String defaultUserMessage;
    private final Object[] defaultUserMessageArgs;

    public AbstractPlatformRuleException(final String globalisationMessageCode, final String defaultUserMessage,
            final Object... defaultUserMessageArgs) {
        this.globalisationMessageCode = globalisationMessageCode;
        this.defaultUserMessage = defaultUserMessage;
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
}