package in.grasshoper.field.order.api;

import java.util.Collection;

import javax.websocket.server.PathParam;

import in.grasshoper.core.infra.ApiSerializer;
import in.grasshoper.core.infra.CommandProcessingResult;
import in.grasshoper.core.infra.FromJsonHelper;
import in.grasshoper.core.infra.JsonCommand;
import in.grasshoper.core.security.service.PlatformSecurityContext;
import in.grasshoper.field.order.data.OrderData;
import in.grasshoper.field.order.service.OrderReadService;
import in.grasshoper.field.order.service.OrderWriteService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
		return this.apiJsonSerializerService.serialize(count);
	}
	
	
	@RequestMapping(method = RequestMethod.GET)
	@Transactional(readOnly = true)
	@ResponseBody
    public String getAllOrders(@RequestParam(value="limit", required = false)  Integer limit,
    		@RequestParam(value="offset", required = false)  Integer offset,
    		@RequestParam(value="status", required = false)  Integer statusCode) {
		this.context.restrictPublicUser();
		final Collection<OrderData> result = this.orderReadService.retriveAll(limit, offset, statusCode);
		return this.apiJsonSerializerService.serialize(result);
	}
	
	
	@RequestMapping(value="/{orderId}" , method = RequestMethod.GET)
	@Transactional(readOnly = true)
	@ResponseBody
    public String getOneOrder(@PathVariable("orderId") Long orderId) {
		this.context.restrictPublicUser();
		final OrderData result = this.orderReadService.retriveOne(orderId);
		return this.apiJsonSerializerService.serialize(result);
	}
}
