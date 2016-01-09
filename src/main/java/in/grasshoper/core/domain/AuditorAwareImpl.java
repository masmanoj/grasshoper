package in.grasshoper.core.domain;

import in.grasshoper.user.domain.User;
import in.grasshoper.user.domain.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
@Component
public class AuditorAwareImpl implements AuditorAware<User> {

	   // @Autowired
	   // private PlatformSecurityContext context;
	    
	    @Autowired 
	    private UserRepository userRepository;

	    @Override
	    public User getCurrentAuditor() {
	    	User user = null;
	    	final SecurityContext securityContext = SecurityContextHolder.getContext();
	        if (securityContext != null) {
	            final Authentication authentication = securityContext.getAuthentication();
	            if (authentication != null) {
	                user = (User) authentication.getPrincipal();
	            } 
	        }  
	    	//User user = SecurityContextHolder.getContext().getAuthentication().getPrincipal().
	    	//User user = context.authenticatedUser();
	    	if(null == user)
	    		user = this.userRepository.findUserByUserName("masmatrics_sys");
	    	return user;
	    }
}
