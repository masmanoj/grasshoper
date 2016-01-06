package in.grasshoper.core.infra;

import java.util.Map;



public class CommandProcessingResultBuilder {
	
	private Long resourceIdentifier;
	private String status;
	private Map<String, Object> changes;
	
	public CommandProcessingResult build() {
        return CommandProcessingResult.fromDetails(this.resourceIdentifier,
        		this.changes, this.status);
    }
	
	public CommandProcessingResultBuilder withResourceIdAsString(final Long withResourceIdentifier) {
        this.resourceIdentifier = withResourceIdentifier;
        return this;
    }
	
	public CommandProcessingResultBuilder withChanges(final Map<String, Object> changes) {
        this.changes = changes;
        return this;
    }
	
	public CommandProcessingResultBuilder withSuccessStatus(){
		this.status="Success";
		return this;
	}
	public CommandProcessingResultBuilder withFailureStatus(){
		this.status="Failed";
		return this;
	}

}
