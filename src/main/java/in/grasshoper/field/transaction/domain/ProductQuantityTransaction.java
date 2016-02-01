package in.grasshoper.field.transaction.domain;

import java.math.BigDecimal;

import in.grasshoper.core.domain.AbstractAuditableCustom;
import in.grasshoper.field.order.domain.Order;
import in.grasshoper.field.product.domain.Product;
import in.grasshoper.user.domain.User;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name = "g_product_quantity_tran")
public class ProductQuantityTransaction  extends AbstractAuditableCustom<User, Long>{

	@ManyToOne(optional = false)
    @JoinColumn(name = "product_id", referencedColumnName = "id", nullable = false)
	private Product product;
	
	@Column(name = "quantity", nullable = false)
	private BigDecimal quantity;
	
	@Column(name = "quantity_unit", nullable = true, length = 10)
	private String quantityUnit;
	
	@Column(name = "tran_type_enum", nullable = false)
	private Integer tranTypeCode;
	
	@ManyToOne
    @JoinColumn(name = "order_id", nullable = true)
	private Order order;
	
	@Column(name = "is_reverse", nullable = false)
	private Boolean isReverse;
	
	protected ProductQuantityTransaction(){}

	private ProductQuantityTransaction(Product product, BigDecimal quantity,
			String quantityUnit, Integer tranTypeCode, Order order,
			Boolean isReverse) {
		super();
		this.product = product;
		this.quantity = quantity;
		this.quantityUnit = quantityUnit;
		this.tranTypeCode = tranTypeCode;
		this.order = order;
		this.isReverse = isReverse;
	}
	
	public static ProductQuantityTransaction create(Product product, BigDecimal quantity,
			String quantityUnit, Integer tranTypeCode, Order order,
			Boolean isReverse) {
		return new ProductQuantityTransaction(product, quantity, quantityUnit, tranTypeCode, order, isReverse);
	}
	
}
