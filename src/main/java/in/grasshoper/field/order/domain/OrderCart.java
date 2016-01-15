package in.grasshoper.field.order.domain;

import static in.grasshoper.field.order.OrderConstants.CartProductQuantityParamName;
import in.grasshoper.core.infra.JsonCommand;
import in.grasshoper.field.product.domain.Product;
import in.grasshoper.field.tag.domain.SubTag;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Table(name = "g_order_cart")
public class OrderCart extends AbstractPersistable<Long>{
	@ManyToOne(optional = false)
    @JoinColumn(name = "order_id", referencedColumnName = "id", nullable = false)
	private Order order;
	
	@ManyToOne(optional = false)
    @JoinColumn(name = "product_id", referencedColumnName = "id", nullable = false)
	private Product product;
	
	@ManyToOne(optional = false)
    @JoinColumn(name = "pkg_style_id", referencedColumnName = "id", nullable = false)
	private SubTag pkgStyle;
	
	@Column(name = "quantity", nullable = false)
	private BigDecimal quantity;
	
	@Column(name = "item_total_amount", nullable = false)
	private BigDecimal itemTotalPrice;
	
	protected OrderCart(){}

	private OrderCart(Order order, Product product, BigDecimal quantity,
			SubTag pkgStyle,  BigDecimal itemTotalPrice) {
		super();
		this.order = order;
		this.product = product;
		this.quantity = quantity;
		this.pkgStyle = pkgStyle;
		this.itemTotalPrice = itemTotalPrice;
	}
	
	public static OrderCart fromJson(final JsonCommand command, final Product product,
			final Order order, final SubTag pkgStyle, BigDecimal itemTotalPrice) {
		final BigDecimal quantity = command.bigDecimalValueOfParameterNamed(CartProductQuantityParamName);
		return new OrderCart(order, product, quantity, pkgStyle, itemTotalPrice);
	}
	
	public static OrderCart create( final Product product,
			final Order order, final BigDecimal quantity, final SubTag pkgStyle, BigDecimal itemTotalPrice) {
		return new OrderCart(order, product, quantity, pkgStyle, itemTotalPrice);
	}
}
