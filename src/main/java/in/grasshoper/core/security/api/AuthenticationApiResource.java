package in.grasshoper.core.security.api;

import in.grasshoper.core.infra.ApiSerializer;
import in.grasshoper.core.security.data.AuthenticatedUserData;
import in.grasshoper.core.security.service.SessionService;
import in.grasshoper.user.domain.User;

import java.text.SimpleDateFormat;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;



@RestController  
@RequestMapping("/authentication") 
public class AuthenticationApiResource {
	 private final DaoAuthenticationProvider customAuthenticationProvider;
	 private final SessionService sessionService;
	 private final ApiSerializer<AuthenticatedUserData> apiJsonSerializerService;
	 
	 @Autowired
	    public AuthenticationApiResource(
	            @Qualifier("customAuthenticationProvider") final DaoAuthenticationProvider customAuthenticationProvider,
	            final SessionService sessionService, final ApiSerializer<AuthenticatedUserData> apiJsonSerializerService){
		 this.customAuthenticationProvider = customAuthenticationProvider;
		 this.sessionService = sessionService;
		 this.apiJsonSerializerService = apiJsonSerializerService;
	 }
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	 public String authenticate(@RequestBody final String apiRequestBodyAsJson,
			 @RequestHeader("Referer") String referer) {
		final JsonObject requestJson = new JsonParser().parse(apiRequestBodyAsJson).getAsJsonObject(); 
        final String username = requestJson.get("username").getAsString();
        final String password = requestJson.get("password").getAsString();
        
        System.out.println("referer : " +referer);
        
        final Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);
        
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String nowDate = sf.format(DateTime.now().toDate());
        String text = username + password+nowDate;
        
        final Authentication authenticationCheck = this.customAuthenticationProvider.authenticate(authentication);
        
        byte[] base64EncodedAuthenticationKey = Base64.encode(text.getBytes());
        
        final User principal = (User) authenticationCheck.getPrincipal();
        
        sessionService.createSession(principal, new String(base64EncodedAuthenticationKey));
        AuthenticatedUserData  authenticatedUserData =  null;
        if(principal.isPublicUser())
        	authenticatedUserData = new AuthenticatedUserData(principal.getName(), new String(base64EncodedAuthenticationKey),
            		principal.getUsername(), principal.doesPasswordHasToBeRenewed(), authenticationCheck.isAuthenticated());
        else
        	authenticatedUserData = new AuthenticatedUserData(principal.getName(), principal.getId(), new String(base64EncodedAuthenticationKey),
        		principal.getUsername(), principal.doesPasswordHasToBeRenewed(), authenticationCheck.isAuthenticated());
       
        
        return this.apiJsonSerializerService.serialize(authenticatedUserData);
	 }
	
	@RequestMapping(value="/logout", method = RequestMethod.POST)
	@ResponseBody
	 public String logout(@RequestHeader("Authorization") String token) {
        token = token.replaceFirst("Custom ", "");
        this.sessionService.deleteSession(token);
        return "{}";
    }
    
}
