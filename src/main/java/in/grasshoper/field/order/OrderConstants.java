package in.grasshoper.field.order;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public interface OrderConstants {
	String NameParamName = "name" ; 
	String PickupAddressIdParamName = "pickupAddressId";
	String DropAddressIdParamName = "dropAddressId";
	String QuantityParamName = "quantity";
	
	String OrderCartListParamName = "orderCartList";
	String CartProductUidParam = "productUid";
	String CartProductQuantity = "quantity";
	
	
	
	
	
	
	//resource
	String ORDER_RESOURCE = "order";
	
	
	//supported params
	Set<String>  CreateOrderParams = new HashSet<>(
		Arrays.asList(NameParamName, PickupAddressIdParamName, DropAddressIdParamName));
}
