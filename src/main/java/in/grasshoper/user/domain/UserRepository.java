package in.grasshoper.user.domain;

import in.grasshoper.core.security.domain.PublicUserRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository  extends JpaRepository<User, Long>, JpaSpecificationExecutor<User>, PublicUserRepository{
	
	@Query("select u from User u where u.email = :email")
    User findUserByUserName(@Param("email") String email);
	
}
