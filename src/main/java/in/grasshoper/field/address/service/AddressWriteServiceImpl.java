package in.grasshoper.field.address.service;

import in.grasshoper.core.exception.GeneralPlatformRuleException;
import in.grasshoper.core.exception.PlatformDataIntegrityException;
import in.grasshoper.core.exception.ResourceNotFoundException;
import in.grasshoper.core.infra.CommandProcessingResult;
import in.grasshoper.core.infra.CommandProcessingResultBuilder;
import in.grasshoper.core.infra.JsonCommand;
import in.grasshoper.core.security.service.PlatformSecurityContext;
import in.grasshoper.field.address.data.AddressDataValidator;
import in.grasshoper.field.address.domain.Address;
import in.grasshoper.field.address.domain.AddressRepository;
import in.grasshoper.user.domain.User;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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
	@Transactional
	public CommandProcessingResult createAddress(final JsonCommand command){
	
		this.dataValidator.validateForCreate(command.getJsonCommand());
		try {	
			final User thisUser = this.context.authenticatedUser();
			final Address address =  Address.fromJson(thisUser, command);
	
			this.addressRepository.save(address);
			
			return new CommandProcessingResultBuilder().withResourceIdAsString(
				address.getId()).withSuccessStatus().build();
		} catch (DataIntegrityViolationException ex) {
			ex.printStackTrace();
			final Throwable realCause = ex.getMostSpecificCause();
			throw new PlatformDataIntegrityException(
					"error.msg.unknown.data.integrity.issue",
					"Unknown data integrity issue with resource: "
							+ realCause.getMessage());
		}
	}
	
	@Override
	@Transactional
	public CommandProcessingResult removeAddress(final Long addressId){
		try {
			// this.dataValidator.validateForUpdate(command.getJsonCommand());
			final Address address = this.addressRepository.findOne(addressId);
			if (address == null) {
				throw new ResourceNotFoundException(
						"error.entity.address.not.found", "Address with id " + addressId
								+ "not found", addressId);
			}
			
			//check whether address belong to the logged in user
			//additional integirity check
			if(!this.context.authenticatedUser().getId().equals(address.getOwnerUser().getId())){
				throw new GeneralPlatformRuleException("error.no.rights.to.delete.address", 
						"Logged in user dosent have access on this address with id "+addressId,
						addressId);
			}
			
			this.addressRepository.delete(address);
			

			return new CommandProcessingResultBuilder() //
					.withResourceIdAsString(addressId) //
					.withSuccessStatus()
					.build();
		} catch (DataIntegrityViolationException ex) {
			ex.printStackTrace();
			final Throwable realCause = ex.getMostSpecificCause();
			throw new PlatformDataIntegrityException(
					"error.msg.unknown.data.integrity.issue",
					"Unknown data integrity issue with resource: "
							+ realCause.getMessage());
		}
	}
	
}
