package in.grasshoper.user.api;

import in.grasshoper.core.infra.ApiSerializer;
import in.grasshoper.core.infra.CommandProcessingResult;
import in.grasshoper.core.infra.FromJsonHelper;
import in.grasshoper.core.infra.JsonCommand;
import in.grasshoper.core.security.service.PlatformSecurityContext;
import in.grasshoper.user.data.UserData;
import in.grasshoper.user.service.UserReadService;
import in.grasshoper.user.service.UserWriteService;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.JsonParser;

@RestController  
@RequestMapping("/user")
public class UserApiResource {
	
	private final UserWriteService userWriteServce;
	private final UserReadService userReadService;
	private final FromJsonHelper fromApiJsonHelper;
	private final ApiSerializer<UserData> apiJsonSerializerService;
	private final PlatformSecurityContext context;
	
	
	@Autowired
	public UserApiResource(final UserWriteService userWriteServce,
			 final UserReadService userReadService,
			final FromJsonHelper fromApiJsonHelper,
			ApiSerializer<UserData> apiJsonSerializerService,
			final PlatformSecurityContext context) {
		super();
		this.userWriteServce = userWriteServce;
		this.fromApiJsonHelper = fromApiJsonHelper;
		this.userReadService = userReadService;
		this.apiJsonSerializerService = apiJsonSerializerService;
		this.context = context;
	}



	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
    public CommandProcessingResult createUser(@RequestBody final  String reqBody) {
		this.context.restrictPublicUser();
		//JsonObject  a = new JsonParser().parse(reqBody).getAsJsonObject();
		return this.userWriteServce.create(JsonCommand.from(reqBody,
				new JsonParser().parse(reqBody), fromApiJsonHelper));
    }
	
	@RequestMapping(method = RequestMethod.GET)  
	@Transactional(readOnly = true)
    public String retrieveUsers(@RequestParam("limit") Integer limit, @RequestParam("offset") Integer offset) {
		this.context.restrictPublicUser();
        final Collection<UserData> result = this.userReadService.retriveAll( limit, offset);
        
        return this.apiJsonSerializerService.serialize(result);
    }
	
	@RequestMapping(value="/{userId}", method = RequestMethod.GET)  
	@Transactional(readOnly = true)
    public String retrieveOne(@PathVariable("userId")final Long userId) {
		this.context.restrictPublicUser();
        final UserData result = this.userReadService.retriveOne( userId );
        
        return this.apiJsonSerializerService.serialize(result);
    }
	
	@RequestMapping(value="/{userId}/passwd", method = RequestMethod.PUT)  
	@Transactional(readOnly = true)
    public CommandProcessingResult updatePasswd(@PathVariable("userId")final Long userId, @RequestBody final  String reqBody) {
		this.context.restrictPublicUser();
		return this.userWriteServce.updatePassword(userId, JsonCommand.from(reqBody,
				new JsonParser().parse(reqBody), fromApiJsonHelper));
    }
}
