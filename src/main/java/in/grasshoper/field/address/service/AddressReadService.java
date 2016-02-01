package in.grasshoper.field.address.service;

import in.grasshoper.field.address.data.AddressData;

import java.util.Collection;

public interface AddressReadService {

	Collection<AddressData> retriveAllAddresOfLogedInUser();

	boolean isaddressLinkedwithOrder(Long addressId);

}
