package in.grasshoper.core.infra.srvmanager.service;

import in.grasshoper.core.exception.GeneralPlatformRuleException;
import in.grasshoper.core.infra.CommandProcessingResult;
import in.grasshoper.core.infra.CommandProcessingResultBuilder;
import in.grasshoper.core.infra.JsonCommand;
import in.grasshoper.core.infra.srvmanager.domain.GlobalServiceEnum;
import in.grasshoper.core.security.service.PlatformSecurityContext;
import in.grasshoper.field.order.service.OrderWriteService;
import in.grasshoper.field.product.service.ProductWriteService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class SynchronousServiceExecutionService {
	private final ApplicationContext applicationContext;
	private final PlatformSecurityContext context;
	private final ProductWriteService productWriteService;
	@Autowired
	public SynchronousServiceExecutionService(
			ApplicationContext applicationContext,
			PlatformSecurityContext context,
			final ProductWriteService productWriteService) {
		super();
		this.applicationContext = applicationContext;
		this.context = context;
		this.productWriteService = productWriteService;
	}
	public CommandProcessingResult executeInternalWriteService(final GlobalServiceEnum srvCode, final JsonCommand jsonCommand){
		
		//make sure the user is not public
		if(context.authenticatedUser().isPublicUser()){
			throw new GeneralPlatformRuleException("error.user.not.entitiled.to.execute.srv",
					"The User is not entitled to execute this Service");
		}
		
		switch(srvCode){
		case PRODUCTCREATE :
			//check proper permissions
			
			//ProductWriteService srv = this.applicationContext.getBean("ProductWriteService", ProductWriteService.class);
			return this.productWriteService.createProduct(jsonCommand);
		case PRODUCTUPDATE :
			return new CommandProcessingResultBuilder().build();
			
		default: 
			throw new GeneralPlatformRuleException("error.service.not.found",
					"Service not found");
		}

	}
	
	public CommandProcessingResult executePublicWriteService(final GlobalServiceEnum srvCode, final JsonCommand jsonCommand){
		
		//make sure the user is not public
		if(!context.authenticatedUser().isPublicUser()){
			throw new GeneralPlatformRuleException("error.user.not.entitiled.to.execute.srv",
					"The User is not entitled to execute this Service");
		}
		
		switch(srvCode){
		case P_ODER_CREATE :
			OrderWriteService srv = this.applicationContext.getBean("OrderWriteService", OrderWriteService.class);
			return srv.createOrder(jsonCommand);
		default: 
			throw new GeneralPlatformRuleException("error.service.not.found",
					"Service not found");
			
		}
	}
}
