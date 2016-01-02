package in.grasshoper.core.security.service;

import in.grasshoper.core.security.domain.PublicUser;
import in.grasshoper.core.security.domain.PublicUserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Used in securityContext.xml as implementation of spring security's
 * {@link UserDetailsService}.
 */
@Service(value = "userDetailsService")
public class GrassHoperUserDetailService implements PlatformUserDetailsService{
	
	@Autowired
	private PublicUserRepository publicUserRepository;

	@Override
	public UserDetails loadUserByUsername(final String email)
			throws UsernameNotFoundException, DataAccessException {
		
    	final boolean isActive = true;
    	final PublicUser user =  this.publicUserRepository.findUserByEmailAndIsActive(email, isActive);
    	if (user == null) { throw new UsernameNotFoundException(email + ": not found"); }
    	
    	return user;
        

	}

}
