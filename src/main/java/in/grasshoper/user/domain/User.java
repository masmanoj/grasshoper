package in.grasshoper.user.domain;

import static in.grasshoper.user.UserConstants.EmailParamName;
import static in.grasshoper.user.UserConstants.IsPublicUserParamName;
import static in.grasshoper.user.UserConstants.NameParamName;
import static in.grasshoper.user.UserConstants.PasswordParamName;
import static in.grasshoper.user.UserConstants.PhoneNumberParamName;
import in.grasshoper.core.infra.JsonCommand;
import in.grasshoper.core.security.domain.PublicUser;

import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.springframework.data.jpa.domain.AbstractPersistable;
import org.springframework.security.core.GrantedAuthority;


@Entity
@Table(name = "g_user", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "email" })})
public class User extends AbstractPersistable<Long> implements PublicUser{
	
	@Column(name = "name", nullable = false, length = 100)
	private String name;
	
	@Column(name = "email", nullable = false, length = 100)
	private String email;
	
	@Column(name = "phone_num", nullable = false, length = 12)
	private String phoneNum;
	
	@Column(name = "password", nullable = false)
    private String password;
	
	@Column(name = "image_url", nullable = false, length = 100)
	private String imageUrl;
	
	@Column(name = "is_active", nullable = false)
	private boolean isActive;
	
	@Column(name = "is_password_change_needed_on_next_login", nullable = false)
	private boolean isPasswordChangeNeeded;
	
	@Column(name = "is_public_user", nullable = false)
	private boolean isPublicUser;
	
	
	public static User fromJson( final JsonCommand command) {
		final String name = command.stringValueOfParameterNamed(NameParamName);
		final String email = command.stringValueOfParameterNamed(EmailParamName);
		final String password  = command.stringValueOfParameterNamed(PasswordParamName);
		String phoneNum = command.stringValueOfParameterNamed(PhoneNumberParamName);
		final Boolean isPublicUser = command.booleanValueOfParameterNamed(IsPublicUserParamName);
		return new User(name, email, phoneNum, password, isPublicUser);
	}

	private User(String name, String email, String phoneNum, String password, Boolean isPublicUser) {
		super();
		this.name = name;
		this.email = email;
		this.phoneNum = phoneNum;
		this.password = password;
		//this.imageUrl = imageUrl;
		this.isActive = true;
		if(isPublicUser == null)
			this.isPublicUser = true;
		else 
			this.isPublicUser = isPublicUser;
		
	}
	
	protected User() {
    }

	public void updatePassword(String passowrd){
		this.password = passowrd;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public String getUsername() {
		return this.email;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return this.isActive;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		return this.isActive;
	}
	
	public boolean doesPasswordHasToBeRenewed(){
		return this.isPasswordChangeNeeded;
	}
	
	public String getName(){
		return this.name;
	}
        
	
}
