package in.grasshoper.user.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserOtpRepository extends JpaRepository<UserOtp, Long>, JpaSpecificationExecutor<UserOtp> {
	@Query("select o from UserOtp o where o.email = :email and o.otp = :otp")
    UserOtp findUserOtpByUserNameAndOtp(@Param("email") String email, @Param("otp") String otp);
}
