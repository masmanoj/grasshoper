package in.grasshoper.user.service;

import in.grasshoper.core.infra.CommandProcessingResult;
import in.grasshoper.core.infra.CommandProcessingResultBuilder;
import in.grasshoper.core.infra.JsonCommand;
import in.grasshoper.core.security.service.PlatformPasswordEncoder;
import in.grasshoper.user.data.UserDataValidator;
import in.grasshoper.user.domain.User;
import in.grasshoper.user.domain.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserWriteServiceImpl implements UserWriteService {

	private final UserRepository userRepository;
	private final PlatformPasswordEncoder applicationPasswordEncoder;
	private final UserDataValidator userDataValidator;

	@Autowired
	public UserWriteServiceImpl(final UserRepository userRepository,
			final PlatformPasswordEncoder applicationPasswordEncoder,
			final UserDataValidator userDataValidator) {
		super();
		this.userRepository = userRepository;
		this.applicationPasswordEncoder = applicationPasswordEncoder;
		this.userDataValidator = userDataValidator;
	}

	private void generateKeyUsedForPasswordSalting(final User user) {
		this.userRepository.save(user);
	}
	@Override
	public CommandProcessingResult create(final JsonCommand command) {
		
		this.userDataValidator.validateCreate(command.getJsonCommand());

		User user = User.fromJson(command);

		generateKeyUsedForPasswordSalting(user);
		final String encodePassword = this.applicationPasswordEncoder
				.encode(user);
		user.updatePassword(encodePassword);

		this.userRepository.saveAndFlush(user);

		return new CommandProcessingResultBuilder().withResourceIdAsString(
				user.getId()).build();
	}
}
