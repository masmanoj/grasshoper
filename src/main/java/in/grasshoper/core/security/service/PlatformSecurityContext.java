package in.grasshoper.core.security.service;

import in.grasshoper.user.domain.User;

public interface PlatformSecurityContext {

	User authenticatedUser();

	boolean doesPasswordHasToBeRenewed(User currentUser);

	void restrictPublicUser();

}
