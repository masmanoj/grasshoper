package in.grasshoper.field.order.data;

import java.math.BigDecimal;
import java.util.Collection;

import in.grasshoper.field.address.data.AddressData;
import in.grasshoper.user.data.UserData;

public class OrderData {
	@SuppressWarnings("unused")private final Long id;
	@SuppressWarnings("unused")private final Long userId;
	@SuppressWarnings("unused")private final UserData user;
	@SuppressWarnings("unused")private final String orderName;
	@SuppressWarnings("unused")private final AddressData pickupAddress;
	@SuppressWarnings("unused")private final AddressData dropAddress;
	//private final StaffData hoper;
	
	@SuppressWarnings("unused")private final String additionalNote;
	@SuppressWarnings("unused")private final Integer statusCode;
	@SuppressWarnings("unused")private final String statusMsg;
	@SuppressWarnings("unused")private final BigDecimal totalamount;
	@SuppressWarnings("unused")private final Collection<OrderCartData> orderCart;
	@SuppressWarnings("unused")private final Collection<OrderHistoryData> orderHistory;
	
	
	private OrderData(Long id, Long userId, UserData user, String orderName,
			AddressData pickupAddress, AddressData dropAddress,
			//StaffData hoper, 
			String additionalNote, Integer statusCode,
			String statusMsg, BigDecimal totalamount,
			Collection<OrderCartData> orderCart,
			Collection<OrderHistoryData> orderHistory) {
		super();
		this.id = id;
		this.userId = userId;
		this.user = user;
		this.orderName = orderName;
		this.pickupAddress = pickupAddress;
		this.dropAddress = dropAddress;
		//this.hoper = hoper;
		this.additionalNote = additionalNote;
		this.statusCode = statusCode;
		this.statusMsg = statusMsg;
		this.totalamount = totalamount;
		this.orderCart = orderCart;
		this.orderHistory = orderHistory;
	}
	public static OrderData createNew(Long id, Long userId, UserData user, String orderName,
			AddressData pickupAddress, AddressData dropAddress,
			//StaffData hoper, 
			String additionalNote, Integer statusCode,
			String statusMsg, BigDecimal totalamount,
			Collection<OrderCartData> orderCart,
			Collection<OrderHistoryData> orderHistory) {
		return new OrderData(id, userId, user, orderName, pickupAddress, dropAddress, additionalNote, 
				statusCode, statusMsg, totalamount, orderCart, orderHistory);
	}
	
}
