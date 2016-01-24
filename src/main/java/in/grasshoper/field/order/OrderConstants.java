package in.grasshoper.field.order;

import in.grasshoper.core.GrassHoperMainConstants;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public interface OrderConstants {
	String NameParamName = "name" ; 
	String AdditionalNoteParamName = "additionalNote";
	String PickupAddressIdParamName = "pickupAddressId";
	String DropAddressIdParamName = "dropAddressId";
	String OrderStatusParamName = "statusCode";
	String StatusNoteParamName = "statusNote";
	String EmailCheckParamName = "emailchk";
	
	//String QuantityParamName = "quantity";
	
	String OrderCartListParamName = "orderCartList";
	String CartProductUidParam = "productUid";
	String CartProductQuantityParamName = "quantity";
	String CartProductPkgStyleParamName = "pkgStyleId";
	
	
	
	
	
	
	//resource
	String ORDER_RESOURCE = "order";
	
	
	//supported params
	Set<String>  CreateOrderParams = new HashSet<>(
		Arrays.asList(NameParamName, PickupAddressIdParamName, DropAddressIdParamName, OrderCartListParamName,
				AdditionalNoteParamName));
	Set<String> OrderCartParams = new HashSet<>(
			Arrays.asList(CartProductUidParam, CartProductQuantityParamName, CartProductPkgStyleParamName,
					GrassHoperMainConstants.LocaleParamName));
	
	Set<String>  UpdateOrderStatusParams = new HashSet<>(
			Arrays.asList(OrderStatusParamName, StatusNoteParamName, EmailCheckParamName, GrassHoperMainConstants.LocaleParamName));
}
