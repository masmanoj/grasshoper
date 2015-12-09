package in.grasshoper.field.order;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public interface OrderConstants {
	String NameParamName = "name" ; 
	String pickupAddressIdParamName = "pickupAddressId";
	String dropAddressIdParamName = "dropAddressId";
	
	
	
	//resource
	String ORDER_RESOURCE = "order";
	
	
	//supported params
	Set<String>  CreateOrderParams = new HashSet<>(
		Arrays.asList(NameParamName, pickupAddressIdParamName, dropAddressIdParamName));
}
