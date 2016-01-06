package in.grasshoper.core.infra.email.service;

public interface EmailSenderService {

	void sendEmail(String[] toEmails, String[] ccEmails, String[] bccEmails,
			String subject, String body);

}
