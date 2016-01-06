package in.grasshoper.field.order.domain;

import static in.grasshoper.field.order.OrderConstants.QuantityParamName;
import in.grasshoper.core.infra.JsonCommand;
import in.grasshoper.field.product.domain.Product;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Table(name = "g_order")
public class OrderCart extends AbstractPersistable<Long>{
	@ManyToOne(optional = false)
    @JoinColumn(name = "order_id", referencedColumnName = "id", nullable = false)
	private Order order;
	
	@ManyToOne(optional = false)
    @JoinColumn(name = "product_id", referencedColumnName = "id", nullable = false)
	private Product product;
	
	@Column(name = "quantity", nullable = false)
	private BigDecimal quantity;
	
	protected OrderCart(){}

	private OrderCart(Order order, Product product, BigDecimal quantity) {
		super();
		this.order = order;
		this.product = product;
		this.quantity = quantity;
	}
	
	public static OrderCart fromJson(final JsonCommand command, final Product product,
			final Order order) {
		final BigDecimal quantity = command.bigDecimalValueOfParameterNamed(QuantityParamName);
		return new OrderCart(order, product, quantity);
	}
	
	public static OrderCart create( final Product product,
			final Order order, final BigDecimal quantity) {
		return new OrderCart(order, product, quantity);
	}
}
