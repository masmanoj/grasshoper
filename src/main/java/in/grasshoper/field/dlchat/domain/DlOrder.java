package in.grasshoper.field.dlchat.domain;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.jpa.domain.AbstractPersistable;


@Entity
@Table(name = "order")
public class DlOrder  extends AbstractPersistable<Long> {
	@Column(name = "token_num")
	private Integer tokenNum;
	
	@Column(name = "total_amount")
	private BigDecimal totalAmount;
	
	@Column(name = "status")
	private String status;
	
	
	@Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
	
	protected DlOrder(){}

	private DlOrder(Integer tokenNum, BigDecimal totalAmount, String status,
			Date createdDate) {
		super();
		this.tokenNum = tokenNum;
		this.totalAmount = totalAmount;
		this.status = status;
		this.createdDate = createdDate;
	}
	
	public static DlOrder createNew (Integer tokenNum, BigDecimal totalAmount, String status,
			Date createdDate) {
		return new DlOrder(tokenNum, totalAmount, status, createdDate);
	}
}
