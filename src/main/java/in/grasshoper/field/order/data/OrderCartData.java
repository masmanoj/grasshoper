package in.grasshoper.field.order.data;

import in.grasshoper.field.tag.data.SubTagData;

import java.math.BigDecimal;

public class OrderCartData {
	@SuppressWarnings("unused")private final Long id;
	@SuppressWarnings("unused")private final Long orderId;
	@SuppressWarnings("unused")private final Long productId;
	@SuppressWarnings("unused")private final BigDecimal quantity;
	@SuppressWarnings("unused")private final SubTagData packageStyle;
	@SuppressWarnings("unused")private final BigDecimal itemTotalPrice;
	private OrderCartData(Long id, Long orderId, Long productId,
			BigDecimal quantity, SubTagData packageStyle,
			BigDecimal itemTotalPrice) {
		super();
		this.id = id;
		this.orderId = orderId;
		this.productId = productId;
		this.quantity = quantity;
		this.packageStyle = packageStyle;
		this.itemTotalPrice = itemTotalPrice;
	}
	
	public static OrderCartData createNew(Long id, Long orderId, Long productId,
			BigDecimal quantity, SubTagData packageStyle,
			BigDecimal itemTotalPrice) {
		return new OrderCartData(id, orderId, productId, quantity, packageStyle, itemTotalPrice);
	}
}
