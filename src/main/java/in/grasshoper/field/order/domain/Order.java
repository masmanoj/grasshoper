package in.grasshoper.field.order.domain;

import static in.grasshoper.field.order.OrderConstants.AdditionalNoteParamName;
import static in.grasshoper.field.order.OrderConstants.NameParamName;
import in.grasshoper.core.domain.AbstractAuditableCustom;
import in.grasshoper.core.infra.JsonCommand;
import in.grasshoper.field.address.domain.Address;
import in.grasshoper.field.staff.domain.Staff;
import in.grasshoper.user.domain.User;

import java.util.Iterator;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

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
	
	@Column(name = "additional_note", nullable = true, length = 300)
	private String additionalNote;
	
	@LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "order", orphanRemoval = true)
    private List<OrderCart> orderCart; 
	
	/*@LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany( cascade = CascadeType.ALL, mappedBy = "request", orphanRemoval = true)
    private List<OrderHistory> history;
*/
	protected Order(){}
	
	private Order(final String name, final Address pickUpAddress, final Address dropAddress,
			final Staff assignedHoper, final Integer statusCode, final String additionalNote,
			final List<OrderCart> orderCart) {
		super();
		this.name = name;
		this.pickUpAddress = pickUpAddress;
		this.dropAddress = dropAddress;
		this.assignedHoper = assignedHoper;
		this.statusCode = statusCode;
		this.orderCart = orderCart;
		this.additionalNote = additionalNote;
	}
	
	public static Order fromJson(final JsonCommand command, final Address dropAddress, List<OrderCart> orderCart){
		final String name = command.stringValueOfParameterNamed(NameParamName);
		final String additionalNote = command.stringValueOfParameterNamed(AdditionalNoteParamName);
		final Address pickUpAddress = null;
		final Staff assignedHoper = null;
		final Integer statusCode = OrderStatus.Received.getStatusCode();
		//final List<OrderCart> orderCart =  new ArrayList<>();
		return new Order(name, pickUpAddress, dropAddress, 
				assignedHoper, statusCode, additionalNote, orderCart);
	}
	
	
	public List<OrderCart> getOrderCarts(){
		return this.orderCart;
	}
	public OrderCart getOrderCart(Long id){
		for(OrderCart cart : this.orderCart){
			if(cart.getId().equals(id))
				return cart;
		}
		
		return null;
	}
	public void addOrderCart(final OrderCart cart){
		this.orderCart.add(cart);
	}
	public OrderCart lastAddedOrderCart(){
		OrderCart lastAddedCart = null;
		Iterator<OrderCart> iterator = this.orderCart.iterator();
		while (iterator.hasNext()) { lastAddedCart = iterator.next(); }
		return lastAddedCart;
	}
	
	public void updateOrderCart(final List<OrderCart> orderCart){
		this.orderCart = orderCart;
	}
	public void setName (final String name){
		this.name  = name ;
	}
	
}
