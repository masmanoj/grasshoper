package in.grasshoper.core.infra.conf.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ConfigRepository extends JpaRepository<Config, Long>, JpaSpecificationExecutor<Config>{

	public Config findByName(String name);
}
