package in.grasshoper.core.security.service;


import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;


public class GHDaoAuthenticationProvider extends DaoAuthenticationProvider {

    

    public GHDaoAuthenticationProvider() {
        super();
        
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication)
            throws AuthenticationException {
        Object salt = null;

        if (getSaltSource() != null) {
            salt = getSaltSource().getSalt(userDetails);
        }

        if (authentication.getCredentials() == null) {
            logger.debug("Authentication failed: no credentials provided");

           /* throw new BadCredentialsException(messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials",
                    "Bad credentials"), userDetails);*/
            throw new BadCredentialsException(messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials",
                    "Bad credentials"));
        }

        String presentedPassword = authentication.getCredentials().toString();

        if (!getPasswordEncoder().isPasswordValid(userDetails.getPassword(), presentedPassword, salt)) {
            logger.debug("Authentication failed: password does not match stored value");

            throw new BadCredentialsException(messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials",
                    "Bad credentials"));
        }
    }

}
