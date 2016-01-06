package in.grasshoper.core.infra.conf.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Table(name = "g_config")
public class Config extends AbstractPersistable<Long>{
	@Column(name = "name", nullable = false, length = 150)
	private String name;
	@Column(name = "value", nullable = false)
	private String value;
	
	@Column(name = "is_active", nullable = false)
	private Boolean isActive;
	
	@Column(name = "is_internal", nullable = false)
	private Boolean isInternal;
	
	protected Config(){}

	public Config(String name, String value, Boolean isActive,
			Boolean isInternal) {
		super();
		this.name = name;
		this.value = value;
		this.isActive = isActive;
		this.isInternal = isInternal;
	}

	public String getName() {
		return this.name;
	}

	public String getValue() {
		return this.value;
	}

	public Boolean getIsActive() {
		return this.isActive;
	}

	public Boolean getIsInternal() {
		return this.isInternal;
	};
	
	
	
}
