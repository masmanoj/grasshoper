package in.grasshoper.field.address.api;

import in.grasshoper.core.infra.ApiSerializer;
import in.grasshoper.core.infra.CommandProcessingResult;
import in.grasshoper.core.infra.FromJsonHelper;
import in.grasshoper.core.infra.JsonCommand;
import in.grasshoper.field.address.data.AddressData;
import in.grasshoper.field.address.service.AddressReadService;
import in.grasshoper.field.address.service.AddressWriteService;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.JsonParser;


@RestController  
@RequestMapping("/address")
public class PublicAddressApiResource {
	private final AddressWriteService addressWriteServce;
	private final FromJsonHelper fromApiJsonHelper;
	private final AddressReadService addressReadService;
	 private final ApiSerializer<AddressData> apiJsonSerializerService;
	
	
	@Autowired
	public PublicAddressApiResource(final AddressWriteService addressWriteServce,
			final FromJsonHelper fromApiJsonHelper,
			final AddressReadService addressReadService,
			final ApiSerializer<AddressData> apiJsonSerializerService) {
		super();
		this.addressWriteServce = addressWriteServce;
		this.fromApiJsonHelper = fromApiJsonHelper;
		this.addressReadService =  addressReadService;
		this.apiJsonSerializerService = apiJsonSerializerService;
	}

	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
    public CommandProcessingResult createAddress(@RequestBody final  String reqBody) {
		return this.addressWriteServce.createAddress(JsonCommand.from(reqBody,
				new JsonParser().parse(reqBody), fromApiJsonHelper));
    }
	
	
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
    public String getallAddressesFortheUer() {
		
		Collection<AddressData> result = this.addressReadService.retriveAllAddresOfLogedInUser();
		
		return apiJsonSerializerService.serialize(result);
    }
	
	@RequestMapping(value="/{addressId}", method = RequestMethod.DELETE)
	@ResponseBody
	public CommandProcessingResult deleteAddress(@PathVariable("addressId") final Long addressId){
		return this.addressWriteServce.removeAddress(addressId);	
	}
}
