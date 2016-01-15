package in.grasshoper.field.order.api;

import in.grasshoper.core.infra.ApiSerializer;
import in.grasshoper.core.infra.CommandProcessingResult;
import in.grasshoper.core.infra.FromJsonHelper;
import in.grasshoper.core.infra.JsonCommand;
import in.grasshoper.core.security.service.PlatformSecurityContext;
import in.grasshoper.field.order.data.OrderData;
import in.grasshoper.field.order.service.OrderReadService;
import in.grasshoper.field.order.service.OrderWriteService;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.JsonParser;

@RestController  
@RequestMapping("/order")
public class OrderApiResource {
	private final OrderWriteService orderWriteService;
	private final OrderReadService orderReadService;
	private final PlatformSecurityContext context;
	private final FromJsonHelper fromApiJsonHelper;
	private final ApiSerializer<Object> apiJsonSerializerService;
	
	@Autowired
	public OrderApiResource(final OrderWriteService orderWriteService,
			final PlatformSecurityContext context,
			final FromJsonHelper fromApiJsonHelper,
			final OrderReadService orderReadService,
			final ApiSerializer<Object> apiJsonSerializerService){
		this.orderWriteService = orderWriteService;
		this.context = context;
		this.fromApiJsonHelper = fromApiJsonHelper;
		this.orderReadService = orderReadService;
		this.apiJsonSerializerService = apiJsonSerializerService;
	}
	
	@RequestMapping(value="/{orderId}", method = RequestMethod.PUT)
	@ResponseBody
    public CommandProcessingResult updateStaus(@PathVariable("orderId") final Long orderId,
    		@RequestBody final  String reqBody) {
		this.context.restrictPublicUser();
		return this.orderWriteService.updateStatus(orderId, JsonCommand.from(orderId, reqBody,
				new JsonParser().parse(reqBody), fromApiJsonHelper));
	}
	
	
	
	@RequestMapping(value="/ordernoti", method = RequestMethod.GET)
	@ResponseBody
    public String getNewOrderCount() {
		this.context.restrictPublicUser();
		Integer count =  orderReadService.getNewOrderCount();
		//Map<String, Integer> a = new HashMap<>();
		//a.put("coount", count);
		return this.apiJsonSerializerService.serialize(count);
	}
	
}
