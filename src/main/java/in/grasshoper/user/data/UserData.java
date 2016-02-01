package in.grasshoper.user.data;

import java.util.Collection;

public class UserData {
	@SuppressWarnings("unused") private final Long id;
	@SuppressWarnings("unused") private final String name;
	private final String email;
	@SuppressWarnings("unused") private final String phoneNum;
	@SuppressWarnings("unused") private final String imageUrl;
	@SuppressWarnings("unused") private final Boolean isActive;
	@SuppressWarnings("unused") private final Boolean isPasswordChangeNeeded;
	@SuppressWarnings("unused") private final Boolean isPublicUser;
	@SuppressWarnings("unused") private final Collection<RoleData> roles;
	private UserData(Long id, String name, final String email,
			String phoneNum, String imageUrl,
			Boolean isActive, Boolean isPasswordChangeNeeded,
			Boolean isPublicUser, final Collection<RoleData> roles) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.phoneNum = phoneNum;
		this.imageUrl = imageUrl;
		this.isActive = isActive;
		this.isPasswordChangeNeeded = isPasswordChangeNeeded;
		this.isPublicUser = isPublicUser;
		this.roles = roles;
	}
	
	public static UserData createNew(Long id, String name, final String email,
			String phoneNum, String imageUrl,
			Boolean isActive, Boolean isPasswordChangeNeeded,
			Boolean isPublicUser, final Collection<RoleData> roles) {
		return new UserData(id, name, email, phoneNum, imageUrl, isActive, 
				isPasswordChangeNeeded, isPublicUser, roles);
	}
	
	public String getEmail(){
		return this.email;
	}
	
}
