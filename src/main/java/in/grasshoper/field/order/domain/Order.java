package in.grasshoper.field.order.domain;

import static in.grasshoper.field.order.OrderConstants.NameParamName;
import in.grasshoper.core.domain.AbstractAuditableCustom;
import in.grasshoper.core.infra.JsonCommand;
import in.grasshoper.field.address.domain.Address;
import in.grasshoper.field.staff.domain.Staff;
import in.grasshoper.user.domain.User;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "g_order")
public class Order extends AbstractAuditableCustom<User, Long>{
	
	@Column(name = "name", nullable = false, length = 100)
	private String name;
	
	@ManyToOne
    @JoinColumn(name = "pickup_address", nullable = false)
	private Address pickUpAddress;
	
	@ManyToOne
    @JoinColumn(name = "drop_address", nullable = false)
	private Address dropAddress;
	
	@ManyToOne
	@JoinColumn(name = "assigned_hoper", nullable=true)
	private Staff assignedHoper;
	
	@Column(name = "status_code", nullable = false)
	private Integer statusCode;

	protected Order(){}
	
	private Order(final String name, final Address pickUpAddress, final Address dropAddress,
			final Staff assignedHoper, final Integer statusCode) {
		super();
		this.name = name;
		this.pickUpAddress = pickUpAddress;
		this.dropAddress = dropAddress;
		this.assignedHoper = assignedHoper;
		this.statusCode = statusCode;
	}
	
	public Order fromJson(final JsonCommand command){
		final String name = command.stringValueOfParameterNamed(NameParamName);
		final Address pickUpAddress = null;
		final Address dropAddress = null;
		final Staff assignedHoper = null;
		final Integer statusCode = OrderStatus.Unassigned.getStatusCode();
		
		return new Order(name, pickUpAddress, dropAddress, 
				assignedHoper, statusCode);
	}
	
	
}
