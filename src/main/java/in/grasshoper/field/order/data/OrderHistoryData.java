package in.grasshoper.field.order.data;

public class OrderHistoryData {
	@SuppressWarnings("unused")private final Long id;
	@SuppressWarnings("unused")private final Long orderId;
	@SuppressWarnings("unused")private final Integer satusCode;
	@SuppressWarnings("unused")private final String statusMsg;
	@SuppressWarnings("unused")private final String description;
	private OrderHistoryData(Long id, Long orderId, Integer satusCode,
			String statusMsg, String description) {
		super();
		this.id = id;
		this.orderId = orderId;
		this.satusCode = satusCode;
		this.statusMsg = statusMsg;
		this.description = description;
	}
	
	final static OrderHistoryData createNew(Long id, Long orderId, Integer satusCode,
			String statusMsg, String description) {
		return new OrderHistoryData(id, orderId, satusCode, statusMsg, description);
	}
}
