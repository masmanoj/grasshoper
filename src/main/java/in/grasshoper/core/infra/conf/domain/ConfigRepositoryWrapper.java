package in.grasshoper.core.infra.conf.domain;

import in.grasshoper.core.exception.ResourceNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConfigRepositoryWrapper {
	private final ConfigRepository configRepository;

	@Autowired
	public ConfigRepositoryWrapper(ConfigRepository configRepository) {
		super();
		this.configRepository = configRepository;
	}
	
	public Config findByNameExceptionIfNotFound(String name){
		Config cfg = this.configRepository.findByName(name);
		if(cfg == null){
			throw new ResourceNotFoundException(
					"error.config.not.found", "Config with name " + name
							+ "not found", name);
		}
		return cfg;
	}
	
}
