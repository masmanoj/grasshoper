package in.grasshoper.field.address;

import in.grasshoper.core.GrassHoperMainConstants;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


public interface AddressContants {
	String NameParamName = "name";
	String AddressLine1ParamName = "addressLine1"; 
	String AddressLine2ParamName = "addressLine2";
	String AddressLine3ParamName = "addressLine3";
	String AreaParamName = "area";
	String LandmarkParamName = "landmark";
	String CityParamName = "city";
	String PinParamName = "pin";
	String ContactNumberParamName = "contactNumber";
	String ExtraInfoParamName = "extraInfo";
	String LatitudeParamName = "latitude";
	String LongitudeParamName = "longitude";
	String AddressTypeParamName = "addressType";
	String OwnerUserIdParamName = "ownerUserId";
	
	
	String ADDRESS_RESOURCE = "address";
	
	//supported params
		Set<String>  CreateAddressParams = new HashSet<>(
			Arrays.asList(NameParamName, AddressLine1ParamName, AddressLine2ParamName,
					AddressLine3ParamName, AreaParamName, LandmarkParamName, CityParamName,
					PinParamName, ContactNumberParamName, ExtraInfoParamName, LatitudeParamName,
					LongitudeParamName, AddressTypeParamName, GrassHoperMainConstants.LocaleParamName));
}
