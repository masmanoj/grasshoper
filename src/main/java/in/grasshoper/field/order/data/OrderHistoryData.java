package in.grasshoper.field.order.data;

import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class OrderHistoryData {
	@SuppressWarnings("unused")private final Long id;
	@SuppressWarnings("unused")private final Long orderId;
	@SuppressWarnings("unused")private final Integer statusCode;
	@SuppressWarnings("unused")private final String statusMsg;
	@SuppressWarnings("unused")private final String description;
	@SuppressWarnings("unused")private final Long userId;
	@SuppressWarnings("unused")private final String userName;
	@SuppressWarnings("unused")private final String userEmail; 
	@SuppressWarnings("unused")private final String createTime;
	private OrderHistoryData(Long id, Long orderId, Integer statusCode,
			String statusMsg, String description, Long userId, String userName,
			String userEmail, final String createTime) {
		super();
		this.id = id;
		this.orderId = orderId;
		this.statusCode = statusCode;
		this.statusMsg = statusMsg;
		this.description = description;
		this.userId = userId;
		this.userName = userName;
		this.userEmail = userEmail;
		this.createTime= createTime;
	}
	public static OrderHistoryData createNew(Long id, Long orderId, Integer statusCode,
			String statusMsg, String description, Long userId, String userName, String userEmail,
			 final LocalDateTime createTime) {
		DateTimeFormatter FORMATTER =
			    DateTimeFormat.forPattern("dd-MM-yyyy  hh:mm:ss a");
		String createdTimeStr = FORMATTER.print(createTime);
		return new OrderHistoryData(id, orderId, statusCode, statusMsg, description, userId, 
				userName, userEmail, createdTimeStr);
	}
	
}
