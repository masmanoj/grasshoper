package in.grasshoper.core.exception;


public class GeneralPlatformRuleException extends AbstractPlatformRuleException {

    public GeneralPlatformRuleException(final String globalisationMessageCode, final String defaultUserMessage,
            final Object... defaultUserMessageArgs) {
        super(globalisationMessageCode, defaultUserMessage, defaultUserMessageArgs);
    }
}