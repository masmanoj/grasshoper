package in.grasshoper.field.address.service;

import in.grasshoper.core.infra.CommandProcessingResult;
import in.grasshoper.core.infra.CommandProcessingResultBuilder;
import in.grasshoper.core.infra.JsonCommand;
import in.grasshoper.core.security.service.PlatformSecurityContext;
import in.grasshoper.field.address.data.AddressDataValidator;
import in.grasshoper.field.address.domain.Address;
import in.grasshoper.field.address.domain.AddressRepository;
import in.grasshoper.user.domain.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class AddressWriteServiceImpl implements AddressWriteService {

	private final  AddressRepository addressRepository;
	private final AddressDataValidator dataValidator;
	private final PlatformSecurityContext context;
	@Autowired
	public AddressWriteServiceImpl(final AddressRepository addressRepository,
			final AddressDataValidator dataValidator,
			final PlatformSecurityContext context) {
		super();
		this.addressRepository = addressRepository;
		this.dataValidator = dataValidator;
		this.context = context;
	}
	
	@Override
	public CommandProcessingResult createAddress(final JsonCommand command){
		this.dataValidator.validateForCreate(command.getJsonCommand());
		
		final Address address =  Address.fromJson(command);
		
		final User thisUser = this.context.authenticatedUser();
		
		address.updateOwnerUser(thisUser);
		this.addressRepository.save(address);
		
		return new CommandProcessingResultBuilder().withResourceIdAsString(
				address.getId()).build();
	}
	
}
