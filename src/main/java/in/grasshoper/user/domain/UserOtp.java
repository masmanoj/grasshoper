package in.grasshoper.user.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.jpa.domain.AbstractPersistable;
@Entity
@Table(name = "g_user_otp")
public class UserOtp extends AbstractPersistable<Long>{
	@ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
	private User user;
	
	@Column(name = "email", nullable = false, length = 100)
	private String email;
	
	@Column(name = "otp", nullable = false, length = 100)
	private String otp;
	@Column(name = "return_url", nullable = false, length = 100)
	private String returnUrl;
	
	@Column(name = "created_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdTime;
	
	protected UserOtp(){}

	private UserOtp(final User user, final String email, final String otp, final String returnUrl) {
		super();
		this.user = user;
		this.email = email;
		this.otp = otp;
		this.returnUrl = returnUrl;
		this.createdTime = new Date();
	}
	
	public static UserOtp createOtp(final User user, final String email, final String otp, final String returnUrl) {
		if(otp.length() > 250)
			return new UserOtp(user, email, otp.substring(0, 250), returnUrl);
		return new UserOtp(user, email, otp, returnUrl);
	}
	
	public String getOtp(){
		return this.otp;
	}
	
	public User getThisUser(){
		return this.user;
	}
	public String getReturnUrl(){
		return this.returnUrl;
	}
}
