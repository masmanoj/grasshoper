package in.grasshoper.field.dlchat.domain;

import java.math.BigDecimal;

import in.grasshoper.field.order.domain.Order;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.AbstractPersistable;


@Entity
@Table(name = "order_chat")
public class DlOrderChat extends AbstractPersistable<Long>{

	@ManyToOne(optional = false)
    @JoinColumn(name = "order_id", referencedColumnName = "id", nullable = false)
	private DlOrder order;
	
	
	@ManyToOne(optional = false)
    @JoinColumn(name = "chat_id", referencedColumnName = "id", nullable = false)
	private Chat chat;
	
	
	@Column(name = "price_per_item")
	private BigDecimal pricePerItem;
	
	
	@Column(name = "qty")
	private BigDecimal qty;
	
	
	protected DlOrderChat(){}


	private DlOrderChat(DlOrder order, Chat chat, BigDecimal pricePerItem,
			BigDecimal qty) {
		super();
		this.order = order;
		this.chat = chat;
		this.pricePerItem = pricePerItem;
		this.qty = qty;
	}
	
	public static DlOrderChat createNew(DlOrder order, Chat chat, BigDecimal pricePerItem,
			BigDecimal qty) {
		return new  DlOrderChat(order, chat, pricePerItem, qty);
	}
	
	
	
}
