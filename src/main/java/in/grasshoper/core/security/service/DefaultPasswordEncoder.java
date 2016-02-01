package in.grasshoper.core.security.service;

import in.grasshoper.core.security.domain.PublicUser;
import in.grasshoper.user.domain.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.stereotype.Service;

@SuppressWarnings("deprecation")
@Service(value = "applicationPasswordEncoder")
@Scope("singleton")
public class DefaultPasswordEncoder implements PlatformPasswordEncoder {
	
	 private final PasswordEncoder passwordEncoder;
	    private final SaltSource saltSource;

	    @Autowired
	    public DefaultPasswordEncoder(final PasswordEncoder passwordEncoder, final SaltSource saltSource) {
	        this.passwordEncoder = passwordEncoder;
	        this.saltSource = saltSource;
	    }

	    @Override
	    public String encode(final PublicUser user) {
	        return this.passwordEncoder.encodePassword(user.getPassword(), this.saltSource.getSalt(user));
	    }
	    @Override
	    public String encode(final String password, final User user) {
	        return this.passwordEncoder.encodePassword(password, this.saltSource.getSalt(user));
	    }
}