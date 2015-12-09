package in.grasshoper.core.security.domain;


public interface PublicUserRepository {
	PublicUser findAppUserByEmailAndIsActive( String email,  boolean isActive);
}
