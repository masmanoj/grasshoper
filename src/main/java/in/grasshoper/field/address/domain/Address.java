package in.grasshoper.field.address.domain;

import static in.grasshoper.field.address.AddressContants.AddressLine1ParamName;
import static in.grasshoper.field.address.AddressContants.AddressLine2ParamName;
import static in.grasshoper.field.address.AddressContants.AddressLine3ParamName;
import static in.grasshoper.field.address.AddressContants.AddressTypeParamName;
import static in.grasshoper.field.address.AddressContants.AreaParamName;
import static in.grasshoper.field.address.AddressContants.CityParamName;
import static in.grasshoper.field.address.AddressContants.ContactNumberParamName;
import static in.grasshoper.field.address.AddressContants.ExtraInfoParamName;
import static in.grasshoper.field.address.AddressContants.LandmarkParamName;
import static in.grasshoper.field.address.AddressContants.LatitudeParamName;
import static in.grasshoper.field.address.AddressContants.LongitudeParamName;
import static in.grasshoper.field.address.AddressContants.NameParamName;
import static in.grasshoper.field.address.AddressContants.PinParamName;
import in.grasshoper.core.infra.JsonCommand;
import in.grasshoper.user.domain.User;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Table(name = "g_address")
public class Address extends AbstractPersistable<Long>{
	@Column(name = "name", nullable = false, length = 100)
	private String name;
	
	@Column(name = "address_line1", nullable = false, length = 100)
	private String addressLine1;
	
	@Column(name = "address_line2", nullable = true, length = 100)
	private String addressLine2;
	
	@Column(name = "address_line3", nullable = true, length = 100)
	private String addressLine3;
	
	@Column(name = "area", nullable = true,  length = 50)
	private String area;
	
	@Column(name = "landmark", nullable = false, length = 50)
	private String landmark;
	
	@Column(name = "city", nullable = false, length = 50)
	private String city;
	
	@Column(name = "pin", nullable = false, length = 6)
	private String pin;
	
	@Column(name = "contact_number", nullable = false, length = 12)
	private String contactNumber;
	
	@Column(name = "extra_info", nullable = false, length = 255)
	private String extraInfo;
	
	@Column(name = "latitude", nullable = true)
	private BigDecimal latitude;
	
	@Column(name = "longitude", nullable = true)
	private BigDecimal longitude;
	
	@Column(name = "address_type", nullable = false)
	private Integer addressType;
	
	@ManyToOne
    @JoinColumn(name = "owner_user_id", nullable = true)
	private User ownerUser;
	
	@Column(name = "is_deleted", nullable = false)
	private Boolean isDeleted;
	
	
	
	public static Address fromJson(final User user, final JsonCommand command){
		final String name = command.stringValueOfParameterNamed(NameParamName);
		final String addressLine1 = command.stringValueOfParameterNamed(AddressLine1ParamName);
		final String addressLine2 = command.stringValueOfParameterNamed(AddressLine2ParamName);
		final String addressLine3 = command.stringValueOfParameterNamed(AddressLine3ParamName);
		final String area = command.stringValueOfParameterNamed(AreaParamName);
		final String landmark = command.stringValueOfParameterNamed(LandmarkParamName);
		final String city = command.stringValueOfParameterNamed(CityParamName);
		final String pin = command.stringValueOfParameterNamed(PinParamName);
		final String contactNumber = command.stringValueOfParameterNamed(ContactNumberParamName);
		final String extraInfo = command.stringValueOfParameterNamed(ExtraInfoParamName);
		final BigDecimal latitude = command.bigDecimalValueOfParameterNamed(LatitudeParamName);
		final BigDecimal longitude = command.bigDecimalValueOfParameterNamed(LongitudeParamName);
		final Integer addressType = command.integerValueOfParameterNamed(AddressTypeParamName);
		final User ownerUser = user;
		final Boolean isDeleted = false;
		return new Address(name, addressLine1, addressLine2, addressLine3, area, 
				landmark, city, pin, contactNumber, extraInfo, latitude, 
				longitude, addressType == null ? 0 : addressType, ownerUser, isDeleted);
	}
	
	protected Address() {
		// TODO Auto-generated constructor stub
	}


	private Address(final String name, final String addressLine1, final String addressLine2,
			final String addressLine3, final String area, final String landmark, final String city,
			final String pin, final String contactNumber, final String extraInfo,
			final BigDecimal latitude, final BigDecimal longitude, final Integer addressType,
			final User ownerUser, final Boolean isDeleted) {
		super();
		this.name = name;
		this.addressLine1 = addressLine1;
		this.addressLine2 = addressLine2;
		this.addressLine3 = addressLine3;
		this.area = area;
		this.landmark = landmark;
		this.city = city;
		this.pin = pin;
		this.contactNumber = contactNumber;
		this.extraInfo = extraInfo;
		this.latitude = latitude;
		this.longitude = longitude;
		this.addressType = addressType;
		this.ownerUser = ownerUser;
		this.isDeleted = isDeleted;
	}
	
	
	public void updateOwnerUser(User ownerUser){
		this.ownerUser = ownerUser;
	}
	
	public User getOwnerUser(){
		return this.ownerUser;
	}
	
	public void deleteAddress(){
		this.isDeleted = true;
	}
}
