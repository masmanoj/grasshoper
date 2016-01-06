package in.grasshoper.core.infra;

import java.io.Serializable;
import java.util.Map;

public class CommandProcessingResult implements Serializable {
	
	private final Long resourceId;
	private final Map<String, Object> changes;
	private final String statusMsg;
	
	public static CommandProcessingResult empty() {
        return new CommandProcessingResult(null, null, null);
    }
	
	public static CommandProcessingResult fromDetails(final Long resourceId, final Map<String, Object> changes,
			final String status) {
		return new CommandProcessingResult(resourceId, changes, status);
	}

	private CommandProcessingResult(final Long resourceId,
			final Map<String, Object> changes,final  String status) {
		this.resourceId = resourceId;
		this.changes = changes;
		this.statusMsg =status;
	}

	public Long getResourceId() {
		return resourceId;
	}
	public String getStatusMsg() {
		return statusMsg;
	}
}
