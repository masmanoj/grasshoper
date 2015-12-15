package in.grasshoper.core.security.service;

import in.grasshoper.core.security.domain.PublicUser;

public interface PlatformPasswordEncoder {

	String encode(PublicUser user);

}
