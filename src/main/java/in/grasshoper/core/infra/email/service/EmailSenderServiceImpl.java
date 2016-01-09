package in.grasshoper.core.infra.email.service;

import in.grasshoper.core.exception.GeneralPlatformRuleException;
import in.grasshoper.core.infra.conf.domain.ConfigRepositoryWrapper;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
@Service
public class EmailSenderServiceImpl implements EmailSenderService{

	private final ConfigRepositoryWrapper configRepository;

	@Autowired
	public EmailSenderServiceImpl(ConfigRepositoryWrapper configRepository) {
		super();
		this.configRepository = configRepository;
	}
	@Override
	@Async
	public void  sendEmail(String[] toEmails, String[] ccEmails, String[] bccEmails, String subject, String body){
		String authUser = this.configRepository.findByNameExceptionIfNotFound("default-email-user-name").getValue(); 
		String authUserPasswd = this.configRepository.findByNameExceptionIfNotFound("default-email-passwd").getValue();
		String host = this.configRepository.findByNameExceptionIfNotFound("default-email-host-name").getValue();
		String port = this.configRepository.findByNameExceptionIfNotFound("default-email-port").getValue();
		String fromName = this.configRepository.findByNameExceptionIfNotFound("default-email-from-name").getValue();
		final MultiPartEmail email = new MultiPartEmail();
		email.setAuthenticator(new DefaultAuthenticator(authUser, authUserPasswd));//authentication
		email.setDebug(false); // no debug
        email.setHostName(host);
        
        try {
        	 email.getMailSession().getProperties()
             .put("mail.smtp.starttls.enable",true);
        	 email.getMailSession().getProperties().put("mail.smtp.port", port);
        	 email.setFrom(authUser, fromName);
        	 email.addTo(toEmails);
        	 if (ccEmails != null) {
                 email.addCc(ccEmails);
             }
             if (bccEmails != null) {
                 email.addBcc(bccEmails);
             }
             email.setSubject(subject);
             email.setContent(body, "text/html");
             
             
             email.send();
        } catch (final EmailException e) {
        	e.printStackTrace();
            throw new GeneralPlatformRuleException("error.email.address.invalid", "Invalid email");
        }
	}
	
}
