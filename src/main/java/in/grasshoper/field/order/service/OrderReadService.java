package in.grasshoper.field.order.service;

import in.grasshoper.field.order.data.OrderData;

import java.util.Collection;

public interface OrderReadService {

	Integer getNewOrderCount();

	OrderData retriveOne(Long orderId);

	Collection<OrderData> retriveAll(Integer limit, Integer offset,
			Integer statusCode);

}
