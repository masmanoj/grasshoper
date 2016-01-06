package in.grasshoper.user.service;

import static in.grasshoper.user.UserConstants.ReturnUrlParamName;
import static in.grasshoper.core.GrassHoperMainConstants.DefaultAppUrl;
import in.grasshoper.core.infra.CommandProcessingResult;
import in.grasshoper.core.infra.CommandProcessingResultBuilder;
import in.grasshoper.core.infra.FromJsonHelper;
import in.grasshoper.core.infra.JsonCommand;
import in.grasshoper.core.infra.email.service.EmailSenderService;
import in.grasshoper.core.infra.email.template.EmailTemplates;
import in.grasshoper.core.security.service.PlatformPasswordEncoder;
import in.grasshoper.user.data.UserDataValidator;
import in.grasshoper.user.domain.User;
import in.grasshoper.user.domain.UserOtp;
import in.grasshoper.user.domain.UserOtpRepository;
import in.grasshoper.user.domain.UserRepository;

import java.text.SimpleDateFormat;

import javax.transaction.Transactional;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.stereotype.Service;

import com.google.gson.JsonElement;

@Service
public class UserWriteServiceImpl implements UserWriteService {

	private final UserRepository userRepository;
	private final PlatformPasswordEncoder applicationPasswordEncoder;
	private final UserDataValidator userDataValidator;
	private final UserOtpRepository userOtpRepository;
	private final FromJsonHelper fromJsonHelper;
	private final EmailSenderService emailSenderService;

	@Autowired
	public UserWriteServiceImpl(final UserRepository userRepository,
			final PlatformPasswordEncoder applicationPasswordEncoder,
			final UserDataValidator userDataValidator,
			final UserOtpRepository userOtpRepository,final FromJsonHelper fromJsonHelper,
			final EmailSenderService emailSenderService) {
		super();
		this.userRepository = userRepository;
		this.applicationPasswordEncoder = applicationPasswordEncoder;
		this.userDataValidator = userDataValidator;
		this.userOtpRepository = userOtpRepository;
		this.fromJsonHelper = fromJsonHelper;
		this.emailSenderService = emailSenderService;
	}

	private void generateKeyUsedForPasswordSalting(final User user) {
		this.userRepository.save(user);
	}
	@Override
	@Transactional
	public CommandProcessingResult create(final JsonCommand command) {
		
		this.userDataValidator.validateCreate(command.getJsonCommand());

		User user = User.fromJson(command, true, false);

		generateKeyUsedForPasswordSalting(user);
		final String encodePassword = this.applicationPasswordEncoder
				.encode(user);
		user.updatePassword(encodePassword);

		this.userRepository.saveAndFlush(user);

		return new CommandProcessingResultBuilder().withResourceIdAsString(
				user.getId()).build();
	}
	
	//public functions
	@Override
	@Transactional
	public CommandProcessingResult createPublicUser(final JsonCommand command) {
		
		this.userDataValidator.validateCreate(command.getJsonCommand());

		User user = User.fromJson(command, false, true);

		generateKeyUsedForPasswordSalting(user);
		final String encodePassword = this.applicationPasswordEncoder
				.encode(user);
		user.updatePassword(encodePassword);

		this.userRepository.save(user);
		
		final JsonElement element = this.fromJsonHelper.parse(command.getJsonCommand());
		final String returnUrl =  this.fromJsonHelper.extractStringNamed(ReturnUrlParamName, element);
		
		final String email =  user.getUsername();
		final SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		final String nowDate = sf.format(DateTime.now().toDate());
		final String text = nowDate + email + nowDate + Math.random();

		final String otp = new String(Base64.encode(text .getBytes()));
		final UserOtp userOtp = UserOtp.createOtp(user, email, otp.substring(3, otp.length() -5), returnUrl);
		
		this.userOtpRepository.save(userOtp);
		
		final String finalOtp = userOtp.getOtp();
		final String verificationLink = DefaultAppUrl+"userapi/activate"
				+"?e="+email+"&uas="+finalOtp;
		String toEmails[] = new String[]{email};
		//this.emailSenderService.sendEmail(toEmails, null, null, EmailTemplates.activateUserEmailSubject(), 
			//	EmailTemplates.activateUserEmailTemplate(user.getName(), verificationLink));
		return new CommandProcessingResultBuilder().withSuccessStatus().build();
	}
	@Override
	public String activateUser(final String email, final String otp) {
		try{
			final UserOtp userOtp = this.userOtpRepository.findUserOtpByUserNameAndOtp(email, otp);
			if(null == userOtp){
				return null;
				//throw new ResourceNotFoundException(
				//		"error.entity.user.found", "User with given details does not exist");
			}
			final User user = userOtp.getThisUser();
			user.activate();
			this.userRepository.saveAndFlush(user);
			//after successful activation delete otp
			this.userOtpRepository.delete(userOtp);
			return userOtp.getReturnUrl();
		}catch(Exception e ){
			e.printStackTrace();
			return null;
		}
	}
}
