package in.grasshoper.field.address.service;

import in.grasshoper.core.infra.CommandProcessingResult;
import in.grasshoper.core.infra.JsonCommand;

public interface AddressWriteService {

	CommandProcessingResult createAddress(JsonCommand command);

	CommandProcessingResult removeAddress(Long addressId);

}
