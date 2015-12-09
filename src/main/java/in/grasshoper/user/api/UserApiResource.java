package in.grasshoper.user.api;

import in.grasshoper.core.infra.CommandProcessingResult;
import in.grasshoper.core.infra.FromJsonHelper;
import in.grasshoper.core.infra.JsonCommand;
import in.grasshoper.user.service.UserWriteService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@RestController  
@RequestMapping("/user")
public class UserApiResource {
	
	private final UserWriteService userWriteServce;
	private final FromJsonHelper fromApiJsonHelper;
	
	
	@Autowired
	public UserApiResource(final UserWriteService userWriteServce,
			final FromJsonHelper fromApiJsonHelper) {
		super();
		this.userWriteServce = userWriteServce;
		this.fromApiJsonHelper = fromApiJsonHelper;
	}



	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
    public CommandProcessingResult createUser(@RequestBody final  String reqBody) {
		
		JsonObject  a = new JsonParser().parse(reqBody).getAsJsonObject();
		return this.userWriteServce.create(JsonCommand.from(reqBody,
				new JsonParser().parse(reqBody), fromApiJsonHelper));
    }
	
	@RequestMapping(method = RequestMethod.GET)  
	@Transactional(readOnly = true)
    public String retrieveUsers() {
        //final Collection<PoliticianData> politicians = this.politicianReadPlatformService.retrieveAll();
        //return this.jsonSerializer.serialize(politicians);
		return "Helloworld";
    }
}
