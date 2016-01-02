package in.grasshoper.core.infra.email.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.grasshoper.core.infra.conf.domain.ConfigRepositoryWrapper;
@Service
public class EmailSenderServiceImpl implements EmailSenderService{

	private final ConfigRepositoryWrapper configRepository;

	@Autowired
	public EmailSenderServiceImpl(ConfigRepositoryWrapper configRepository) {
		super();
		this.configRepository = configRepository;
	}
	public void  sendEmail(String[] toEmails, String[] ccEmails, String[] bccEmails, 
			String frmUsrName, String subject, String body){
		//final MultiPartEmail email = new MultiPartEmail();
		
	}
	
}
