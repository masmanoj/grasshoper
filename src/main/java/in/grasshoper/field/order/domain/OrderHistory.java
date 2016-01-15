package in.grasshoper.field.order.domain;

import in.grasshoper.core.domain.AbstractAuditableCustom;
import in.grasshoper.user.domain.User;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "g_order_history")
public class OrderHistory extends AbstractAuditableCustom<User, Long>{
	@ManyToOne(optional = false)
    @JoinColumn(name = "order_id", referencedColumnName = "id", nullable = false)
	private Order order;
	
	@Column(name = "status", nullable = false)
	private Integer statusCode;
	
	@Column(name = "description", nullable = true, length = 255)
	private String description;
	
	protected OrderHistory(){}

	private OrderHistory(final Order order, final Integer statusCode, final String description) {
		super();
		this.order = order;
		this.statusCode = statusCode;
		this.description = description;
	}
	
	public static OrderHistory create(final Order order, final Integer statusCode, 
			final String description)  {
		return new OrderHistory(order, statusCode, description);
	}
	
}
