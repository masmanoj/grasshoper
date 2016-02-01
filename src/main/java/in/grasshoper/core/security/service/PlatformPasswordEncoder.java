package in.grasshoper.core.security.service;

import in.grasshoper.core.security.domain.PublicUser;
import in.grasshoper.user.domain.User;

public interface PlatformPasswordEncoder {

	String encode(PublicUser user);

	String encode(String password, User user);

}
