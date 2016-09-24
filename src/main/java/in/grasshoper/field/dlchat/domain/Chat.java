package in.grasshoper.field.dlchat.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Table(name = "chat")
public class Chat  extends AbstractPersistable<Long> {
	@Column(name = "name")
	private String name;
	
	@Column(name = "price_per_unit")
	private BigDecimal pricePerUnit;
	
	protected Chat(){}

	private Chat(String name, BigDecimal pricePerUnit) {
		super();
		this.name = name;
		this.pricePerUnit = pricePerUnit;
	}
	
	public static Chat createNew(String name, BigDecimal pricePerUnit) {
		return new Chat(name, pricePerUnit);
	}
	
	
}
