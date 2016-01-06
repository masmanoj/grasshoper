package in.grasshoper.user.api;

import in.grasshoper.core.GrassHoperMainConstants;
import in.grasshoper.core.infra.CommandProcessingResult;
import in.grasshoper.core.infra.FromJsonHelper;
import in.grasshoper.core.infra.JsonCommand;
import in.grasshoper.user.service.UserWriteService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.JsonParser;

@RestController  
@RequestMapping("/userapi") 
public class PublicUserApiResource {
	
	private final UserWriteService userWriteServce;
	private final FromJsonHelper fromApiJsonHelper;
	
	@Autowired
	public PublicUserApiResource(UserWriteService userWriteServce,
			 final FromJsonHelper fromApiJsonHelper) {
		super();
		this.userWriteServce = userWriteServce;
		this.fromApiJsonHelper = fromApiJsonHelper;
	}
	
	
	@RequestMapping(value="/signup", method = RequestMethod.POST)
	@ResponseBody
    public CommandProcessingResult createUser(@RequestBody final  String reqBody) {
		
		//JsonObject  a = new JsonParser().parse(reqBody).getAsJsonObject();
		return this.userWriteServce.createPublicUser(JsonCommand.from(reqBody,
				new JsonParser().parse(reqBody), fromApiJsonHelper));
    }
	@RequestMapping(value="/activate", method = RequestMethod.GET,
			produces = "text/html;charset=UTF-8")
	@ResponseBody
    public String activate(@RequestParam(value="e") String email,
    		@RequestParam(value="uas") String otp) {
		
		 
		 final String returnUrl = this.userWriteServce.activateUser(email, otp);
		 final StringBuffer resultPage = new StringBuffer()
		 .append("<html><head><title>").append(GrassHoperMainConstants.DefaultSiteName)
		 .append("</title></head><body style='text-align: center;font-family: verdana;'><h1>")
		 .append(GrassHoperMainConstants.DefaultSiteName)
		 .append("</h1><hr><br/><br/><br/><br/><br/><br/><br/>");
		 
		 if(null != returnUrl){
			 resultPage.append("<h4>Email verified successfully<br><br/></h4>")
			 .append("<a style='background-color:#73AD21;padding: 16px 100px;color: white;text-decoration: none;' href='")
			 .append(returnUrl)
			 .append("'>Login now</a> </body></html>");
		 }else{
			 resultPage.append("<h4>Something went wrong, contact support</h4> </body></html>");
		 }
		 
		 return resultPage.toString();
    }
}
