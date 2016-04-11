package in.grasshoper.field.order.api;

import in.grasshoper.core.infra.ApiSerializer;
import in.grasshoper.core.infra.CommandProcessingResult;
import in.grasshoper.core.infra.FromJsonHelper;
import in.grasshoper.core.infra.JsonCommand;
import in.grasshoper.core.infra.srvmanager.domain.GlobalServiceEnum;
import in.grasshoper.core.infra.srvmanager.service.SynchronousServiceExecutionService;
import in.grasshoper.field.address.data.AddressData;
import in.grasshoper.field.order.service.OrderReadService;
import in.grasshoper.field.order.service.OrderWriteService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.JsonParser;

@RestController  
@RequestMapping("/item-order")
public class PublicOrderApiResource {
	private final OrderWriteService orderWriteServce;
	private final FromJsonHelper fromApiJsonHelper;
	private final SynchronousServiceExecutionService srvExecuter;
	private final OrderReadService orderReadService;
	private final ApiSerializer<AddressData> apiJsonSerializerService;
	
	@Autowired
	public PublicOrderApiResource(final OrderWriteService orderWriteServce,
			final FromJsonHelper fromApiJsonHelper,
			final OrderReadService orderReadService,
			final ApiSerializer<AddressData> apiJsonSerializerService,
			final SynchronousServiceExecutionService srvExecuter) {
		super();
		this.orderWriteServce = orderWriteServce;
		this.fromApiJsonHelper = fromApiJsonHelper;
		this.orderReadService =  orderReadService;
		this.apiJsonSerializerService = apiJsonSerializerService;
		this.srvExecuter = srvExecuter;
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
    public CommandProcessingResult create(@RequestBody final  String reqBody) {
		
		//return this.srvExecuter.executePublicWriteService(GlobalServiceEnum.P_ODER_CREATE,JsonCommand.from(reqBody,
			//	new JsonParser().parse(reqBody), fromApiJsonHelper));
		
		return this.orderWriteServce.createOrder(JsonCommand.from(reqBody,
				new JsonParser().parse(reqBody), fromApiJsonHelper));
    }
	//get all order status
	
	//single order status

}
