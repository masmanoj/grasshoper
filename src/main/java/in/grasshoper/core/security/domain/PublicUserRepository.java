package in.grasshoper.core.security.domain;


public interface PublicUserRepository {
	PublicUser findUserByEmailAndIsActive( String email,  boolean isActive);
}
