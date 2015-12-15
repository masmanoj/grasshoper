package in.grasshoper.core.exception;

public class ResourceNotFoundException extends PlatformException {
	
    public ResourceNotFoundException(final String globalisationMessageCode, final String defaultUserMessage,
            final Object... defaultUserMessageArgs) {
    	super(globalisationMessageCode, defaultUserMessage, defaultUserMessageArgs);
    }

}
