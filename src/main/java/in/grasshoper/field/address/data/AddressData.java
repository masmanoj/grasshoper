package in.grasshoper.field.address.data;

import java.math.BigDecimal;

public class AddressData {
	@SuppressWarnings("unused") private final Long id;
	@SuppressWarnings("unused") private final String name;
	@SuppressWarnings("unused") private final String addressLine1;
	@SuppressWarnings("unused") private final String addressLine2;
	@SuppressWarnings("unused") private final String addressLine3;
	@SuppressWarnings("unused") private final String area;
	@SuppressWarnings("unused") private final String landmark;
	@SuppressWarnings("unused") private final String city;
	@SuppressWarnings("unused") private final String pin;
	@SuppressWarnings("unused") private final String contactNumber;
	@SuppressWarnings("unused") private final String extraInfo;
	@SuppressWarnings("unused") private final BigDecimal latitude;
	@SuppressWarnings("unused") private final BigDecimal longitude;
	@SuppressWarnings("unused") private final Integer addressType;
	@SuppressWarnings("unused") private final Long ownerUserId;
	private AddressData(final Long id, final String name, final String addressLine1,
			final String addressLine2, final String addressLine3, final String area,
			final String landmark, final String city, final String pin, final String contactNumber,
			final String extraInfo, final BigDecimal latitude, final BigDecimal longitude,
			final Integer addressType, final Long ownerUserId) {
		super();
		this.id = id;
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
		this.ownerUserId = ownerUserId;
	}
	
	public static AddressData createNew(final Long id, final String name, final String addressLine1,
			final String addressLine2, final String addressLine3, final String area,
			final String landmark, final String city, final String pin, final String contactNumber,
			final String extraInfo, final BigDecimal latitude, final BigDecimal longitude,
			final Integer addressType, final Long ownerUserId){
		return new AddressData(id, name, addressLine1, addressLine2, addressLine3, area,
				landmark, city, pin, contactNumber, extraInfo, latitude,
				longitude, addressType, ownerUserId);
	}
}
