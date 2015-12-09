package in.grasshoper.core.security.service;

import in.grasshoper.user.domain.User;

public interface PlatformPasswordEncoder {

	String encode(User user);

}
