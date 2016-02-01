package in.grasshoper.user;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public interface UserConstants {
	String NameParamName = "name";
	String EmailParamName ="email";
	String PasswordParamName = "password";
	String OldPasswordParamName = "oldPassword";
	//String ConfirmPasswordParamName = "cnfrmpassword";
	String PhoneNumberParamName = "phone";
	String IsPublicUserParamName = "ispublicuser";
	String ReturnUrlParamName = "returnUrl";
	
	//resource
	String USER_RESOURCE = "user";
	
	
	//supported params
	Set<String>  CreateUserParams = new HashSet<>(
			Arrays.asList(NameParamName, EmailParamName, PasswordParamName, PhoneNumberParamName, ReturnUrlParamName, "locale"));
	
}
