package in.grasshoper.field.order.api;

import in.grasshoper.core.infra.ApiSerializer;
import in.grasshoper.core.infra.FromJsonHelper;
import in.grasshoper.field.address.data.AddressData;
import in.grasshoper.field.order.service.OrderReadService;
import in.grasshoper.field.order.service.OrderWriteService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController  
@RequestMapping("/order")
public class OrderApiResource {
	private final OrderWriteService orderWriteServce;
	private final FromJsonHelper fromApiJsonHelper;
	private final OrderReadService orderReadService;
	private final ApiSerializer<AddressData> apiJsonSerializerService;
	
	@Autowired
	public OrderApiResource(final OrderWriteService orderWriteServce,
			final FromJsonHelper fromApiJsonHelper,
			final OrderReadService orderReadService,
			final ApiSerializer<AddressData> apiJsonSerializerService) {
		super();
		this.orderWriteServce = orderWriteServce;
		this.fromApiJsonHelper = fromApiJsonHelper;
		this.orderReadService =  orderReadService;
		this.apiJsonSerializerService = apiJsonSerializerService;
	}
	
	

}
