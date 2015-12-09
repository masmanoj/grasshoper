package in.grasshoper.field.staff.domain;

import java.util.Date;

import in.grasshoper.user.domain.User;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Table(name = "g_staff")
public class Staff extends AbstractPersistable<Long>{
	
	@ManyToOne
    @JoinColumn(name = "user_id", nullable = true)
	private User user;
	
	@Column(name = "name", nullable = false, length = 100)
	private String name;
	
	@Column(name = "display_name", nullable = false, length = 100)
	private String displayName;
	
	@Column(name = "role", nullable = false, length = 100)
	private Integer role;
	
	@Column(name = "phone_num", nullable = false, length = 12)
	private String phoneNum;
	
	@Column(name = "vehicle_num", nullable = false, length = 30)
	private String vehicleNum;
	
	@Column(name = "vehicle_desc", nullable = false, length = 100)
	private String vehicleDesc;
	
	@Column(name = "id_or_dl_num", nullable = false, length = 30)
	private String idOrDlNum;
	
	@Column(name = "address_line1", nullable = false, length = 100)
	private String addressLine1;
	
	@Column(name = "address_line2", nullable = true, length = 100)
	private String addressLine2;
	
	@Column(name = "address_line3", nullable = true, length = 100)
	private String addressLine3;
	
	@Column(name = "city", nullable = false, length = 50)
	private String city;
	
	@Column(name = "pin", nullable = false, length = 6)
	private String pin;
	
	@Column(name = "joining_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date joiningDate;
	
	@Column(name = "is_active", nullable = false)
	private Boolean isActive;

	
	protected Staff(){}


	private Staff(final User user, final String name, final String displayName, final Integer role,
			final String phoneNum, final String vehicleNum, final String vehicleDesc,
			final String idOrDlNum, final String addressLine1, final String addressLine2,
			final String addressLine3, final String city, final String pin, final Date joiningDate,
			final Boolean isActive) {
		super();
		this.user = user;
		this.name = name;
		this.displayName = displayName;
		this.role = role;
		this.phoneNum = phoneNum;
		this.vehicleNum = vehicleNum;
		this.vehicleDesc = vehicleDesc;
		this.idOrDlNum = idOrDlNum;
		this.addressLine1 = addressLine1;
		this.addressLine2 = addressLine2;
		this.addressLine3 = addressLine3;
		this.city = city;
		this.pin = pin;
		this.joiningDate = joiningDate;
		this.isActive = isActive;
	}
	
   public static Staff createNew(final User user, final String name, final String displayName, final Integer role,
			final String phoneNum, final String vehicleNum, final String vehicleDesc,
			final String idOrDlNum, final String addressLine1, final String addressLine2,
			final String addressLine3, final String city, final String pin, final Date joiningDate,
			final Boolean isActive){
	   return new Staff(user, name, displayName, role, phoneNum, vehicleNum, 
			   vehicleDesc, idOrDlNum, addressLine1, addressLine2, addressLine3, 
			   city, pin, joiningDate, isActive);
   }
	
}
