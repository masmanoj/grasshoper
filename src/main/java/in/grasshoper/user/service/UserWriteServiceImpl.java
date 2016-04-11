package in.grasshoper.user.service;

import static in.grasshoper.core.GrassHoperMainConstants.DefaultAppUrl;
import static in.grasshoper.user.UserConstants.OldPasswordParamName;
import static in.grasshoper.user.UserConstants.ReturnUrlParamName;
import in.grasshoper.core.exception.GeneralPlatformRuleException;
import in.grasshoper.core.exception.PlatformDataIntegrityException;
import in.grasshoper.core.exception.ResourceNotFoundException;
import in.grasshoper.core.infra.CommandProcessingResult;
import in.grasshoper.core.infra.CommandProcessingResultBuilder;
import in.grasshoper.core.infra.FromJsonHelper;
import in.grasshoper.core.infra.JsonCommand;
import in.grasshoper.core.infra.email.service.EmailSenderService;
import in.grasshoper.core.infra.email.template.EmailTemplates;
import in.grasshoper.core.security.service.PlatformPasswordEncoder;
import in.grasshoper.core.security.service.PlatformSecurityContext;
import in.grasshoper.user.data.UserDataValidator;
import in.grasshoper.user.domain.User;
import in.grasshoper.user.domain.UserOtp;
import in.grasshoper.user.domain.UserOtpRepository;
import in.grasshoper.user.domain.UserRepository;

import java.text.SimpleDateFormat;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.JsonElement;

@Service
public class UserWriteServiceImpl implements UserWriteService {

	private final UserRepository userRepository;
	private final PlatformPasswordEncoder applicationPasswordEncoder;
	private final UserDataValidator userDataValidator;
	private final UserOtpRepository userOtpRepository;
	private final FromJsonHelper fromJsonHelper;
	private final EmailSenderService emailSenderService;
	private final PlatformSecurityContext context;

	@Autowired
	public UserWriteServiceImpl(final UserRepository userRepository,
			final PlatformPasswordEncoder applicationPasswordEncoder,
			final UserDataValidator userDataValidator,
			final UserOtpRepository userOtpRepository,final FromJsonHelper fromJsonHelper,
			final EmailSenderService emailSenderService, final PlatformSecurityContext context) {
		super();
		this.userRepository = userRepository;
		this.applicationPasswordEncoder = applicationPasswordEncoder;
		this.userDataValidator = userDataValidator;
		this.userOtpRepository = userOtpRepository;
		this.fromJsonHelper = fromJsonHelper;
		this.emailSenderService = emailSenderService;
		this.context =  context;
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
		try {
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
			this.emailSenderService.sendEmail(toEmails, null, null, EmailTemplates.activateUserEmailSubject(), 
					EmailTemplates.activateUserEmailTemplate(user.getName(), verificationLink));
			return new CommandProcessingResultBuilder().withSuccessStatus().build();
		} catch (DataIntegrityViolationException ex) {
			ex.printStackTrace();
			final Throwable realCause = ex.getCause();
            if (realCause.getMessage().toLowerCase().contains("email")) {
                throw new PlatformDataIntegrityException(
    					"error.msg.email.already.exist",
    					"The email provided already exitst in the system."
    							+ realCause.getMessage());
            }
			throw new PlatformDataIntegrityException(
					"error.msg.unknown.data.integrity.issue",
					"Unknown data integrity issue with resource: "
							+ realCause.getMessage());
		}
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
	
	@Override
	@Transactional(readOnly = false)
	public CommandProcessingResult updatePassword(Long userId, final JsonCommand command) {
		User user = this.userRepository.findOne(userId);
		
		if (user == null) {
			throw new ResourceNotFoundException(
					"error.entity.user.not.found", "User with id " + userId
							+ " not found", userId);
		}
		
		if(this.context.authenticatedUser().isPublicUser() || command.parameterExists(OldPasswordParamName)){
			//validate old password
			String oldPassword = command.stringValueOfParameterNamed(OldPasswordParamName);
			if(!user.getPassword().equals(this.applicationPasswordEncoder
					.encode(oldPassword, user))){
				throw new GeneralPlatformRuleException("error.old.password.invalid", "Old Password is Incorrect");
			}
		}
		user.updatePasswordFromCommand(command);
		generateKeyUsedForPasswordSalting(user);
		final String encodePassword = this.applicationPasswordEncoder
				.encode(user);
		user.updatePassword(encodePassword);

		this.userRepository.saveAndFlush(user);

		return new CommandProcessingResultBuilder().withResourceIdAsString(
				user.getId()).build();
	}
}
