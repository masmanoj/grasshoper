package in.grasshoper.field.product.data;

import in.grasshoper.core.GrassHoperMainConstants;

import java.math.BigDecimal;

public class ProductData {
	private final Long id;
	private final String name;
	private final String productUid;
	private final String desc0;
	private final String desc1;
	private final String desc2;
	private final BigDecimal quantity;
	private final String quantityUnit;
	private final BigDecimal pricePerUnit;
	private final String currencyCode= GrassHoperMainConstants.CurrencyCode;
	private final Boolean isSoldOut;
	private final Boolean isActive;
	@SuppressWarnings("unused") private final BigDecimal minimumQuantity;
	private ProductData(Long id, String name, String productUid, String desc0,
			String desc1, String desc2, BigDecimal quantity,
			String quantityUnit, Boolean isSoldOut, Boolean isActive,
			final BigDecimal pricePerUnit, final BigDecimal minimumQuantity) {
		super();
		this.id = id;
		this.name = name;
		this.productUid = productUid;
		this.desc0 = desc0;
		this.desc1 = desc1;
		this.desc2 = desc2;
		this.quantity = quantity;
		this.quantityUnit = quantityUnit;
		this.isSoldOut = isSoldOut;
		this.isActive = isActive;
		this.pricePerUnit = pricePerUnit;
		this.minimumQuantity = minimumQuantity;
	}
	public static ProductData createNew(Long id, String name, String productUid, String desc0,
			String desc1, String desc2, BigDecimal quantity,
			String quantityUnit, Boolean isSoldOut, Boolean isActive,
			final BigDecimal pricePerUnit, final BigDecimal minimumQuantity) {
		return new ProductData(id, name, productUid, desc0, desc1, desc2, quantity,
				quantityUnit, isSoldOut, isActive, pricePerUnit, minimumQuantity);
	}
	public Long getId() {
		return this.id;
	}
	public String getName() {
		return this.name;
	}
	public String getProductUid() {
		return this.productUid;
	}
	public String getDesc0() {
		return this.desc0;
	}
	public String getDesc1() {
		return this.desc1;
	}
	public String getDesc2() {
		return this.desc2;
	}
	public BigDecimal getQuantity() {
		return this.quantity;
	}
	public String getQuantityUnit() {
		return this.quantityUnit;
	}
	public Boolean getIsSoldOut() {
		return this.isSoldOut;
	}
	public Boolean getIsActive() {
		return this.isActive;
	}
	public BigDecimal getPricePerUnit() {
		return this.pricePerUnit;
	}
	public String getCurrencyCode() {
		return this.currencyCode;
	}
	
	
}
