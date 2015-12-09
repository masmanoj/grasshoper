package in.grasshoper.core.security.data;

import in.grasshoper.user.domain.User;

import org.joda.time.DateTime;

public class SessionData {

	private final User user;
	
	private final String token;
	
	private DateTime lastUsedDate;

	public SessionData(User user, String token, DateTime lastUsedDate) {
		super();
		this.user = user;
		this.token = token;
		this.lastUsedDate = lastUsedDate;
	}

	public User getUser() {
		return this.user;
	}

	public String getToken() {
		return this.token;
	}

	public DateTime getLastUsedDate() {
		return this.lastUsedDate;
	}
	
	public boolean equals(SessionData obj) {
		boolean result = false;
		if (this.getToken() != null && obj.getToken() != null) 
			result =  this.getToken().equals(obj.getToken());
		else if (this.getUser() != null && obj.getUser() != null)
			result = this.getUser().equals(obj.getUser());
		return result;
    }
	
	@Override
	public int hashCode() {
		return token.hashCode();
	}

	public void setLastUsedDate(DateTime lastUsedDate) {
		this.lastUsedDate = lastUsedDate;
	}
	
}
