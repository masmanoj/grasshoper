package in.grasshoper.field.order.data;

import in.grasshoper.field.tag.data.SubTagData;

import java.math.BigDecimal;

public class OrderCartData {
	@SuppressWarnings("unused")private final Long cartId;
	@SuppressWarnings("unused")private final Long orderId;
	@SuppressWarnings("unused")private final BigDecimal quantity;
	@SuppressWarnings("unused")private final String quantityUnit;
	@SuppressWarnings("unused")private final BigDecimal itemTotalAmount;
	@SuppressWarnings("unused")private final Long productId;
	@SuppressWarnings("unused")private final String productUid;
	@SuppressWarnings("unused")private final String productName;
	@SuppressWarnings("unused")private final BigDecimal pricePerUnit;
	@SuppressWarnings("unused")private final String pkngStyle;
	@SuppressWarnings("unused")private final String pkngStyleLabel;
	@SuppressWarnings("unused")private final Long pkngStyleId;
	
	private OrderCartData(Long cartId, Long orderId, BigDecimal quantity,final String quantityUnit,
			BigDecimal itemTotalAmount, Long productId, String productUid,
			String productName, BigDecimal pricePerUnit, String pkngStyle,
			String pkngStyleLabel, Long pkngStyleId) {
		super();
		this.cartId = cartId;
		this.orderId = orderId;
		this.quantity = quantity;
		this.itemTotalAmount = itemTotalAmount;
		this.productId = productId;
		this.productUid = productUid;
		this.productName = productName;
		this.pricePerUnit = pricePerUnit;
		this.pkngStyle = pkngStyle;
		this.pkngStyleLabel = pkngStyleLabel;
		this.pkngStyleId = pkngStyleId;
		this.quantityUnit = quantityUnit;
	}
	
	public static OrderCartData CreateNew(Long cartId, Long orderId, BigDecimal quantity,
			final String quantityUnit, BigDecimal itemTotalAmount, Long productId, String productUid,
			String productName, BigDecimal pricePerUnit, String pkngStyle,
			String pkngStyleLabel, Long pkngStyleId) {
		return new OrderCartData(cartId, orderId, quantity, quantityUnit, itemTotalAmount, productId, productUid, 
				productName, pricePerUnit, pkngStyle, pkngStyleLabel, pkngStyleId);
	}
	
}
