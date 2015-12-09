package in.grasshoper.core.infra;

import java.io.Serializable;
import java.util.Map;

public class CommandProcessingResult implements Serializable {
	
	private final Long resourceId;
	private final Map<String, Object> changes;
	
	public static CommandProcessingResult empty() {
        return new CommandProcessingResult(null, null);
    }
	
	public static CommandProcessingResult fromDetails(final Long resourceId, final Map<String, Object> changes) {
		return new CommandProcessingResult(resourceId, changes);
	}

	private CommandProcessingResult(final Long resourceId,
			final Map<String, Object> changes) {
		this.resourceId = resourceId;
		this.changes = changes;
	}

	public Long getResourceId() {
		return resourceId;
	}
}
