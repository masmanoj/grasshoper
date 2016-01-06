package in.grasshoper.core.security.service;

import in.grasshoper.core.exception.GeneralPlatformRuleException;
import in.grasshoper.core.security.exception.PlatformUnknownDBException;
import in.grasshoper.core.security.exception.ResetPasswordException;
import in.grasshoper.core.security.exception.UnAuthenticatedUserException;
import in.grasshoper.user.domain.User;

import org.springframework.dao.DataAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * Wrapper around spring security's {@link SecurityContext} for extracted the
 * current authenticated {@link User}.
 */

@Service
public class SpringSecurityPlatformSecurityContext implements
		PlatformSecurityContext {

	
	@Override
    public User authenticatedUser() {
        try{
        	User currentUser = null;
        	final SecurityContext context = SecurityContextHolder.getContext();
        	if (context != null) {
                final Authentication auth = context.getAuthentication();
                if (auth != null) {
                	currentUser = (User) auth.getPrincipal();
                }
        	}     
        	if (currentUser == null) { throw new UnAuthenticatedUserException(); }
        	
        	 if (this.doesPasswordHasToBeRenewed(currentUser)) { throw new ResetPasswordException(currentUser.getId()); }
        	    
            return currentUser;
        	
        } catch (DataAccessException exception){
        	exception.printStackTrace();
            throw new PlatformUnknownDBException();
        }
    }
	@Override
	public boolean doesPasswordHasToBeRenewed(User currentUser) {
		 try{
			 
			 if(currentUser.doesPasswordHasToBeRenewed())	return true;
			 return false;
		 } catch (DataAccessException exception){
	        	exception.printStackTrace();
	            throw new PlatformUnknownDBException();
	     }

	}
	@Override
	public void restrictPublicUser(){
		if(this.authenticatedUser().isPublicUser()){
			throw new GeneralPlatformRuleException("error.user.not.entitiled.to.execute.srv",
					"The User is not entitled to execute this Service");
		}
	}
}
