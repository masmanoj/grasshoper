package in.grasshoper.field.order.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public enum OrderStatus {
	Received(100,"Received"),
	Accepted(200,"Accepted"),
	Dispatched(300,"Dispatched"),
	Delivered(400,"Delivered"),
	Cancelled(500,"Cancelled");
	
	private final Integer statusCode;
	private final String statusValue;
	OrderStatus(Integer statusCode, String statusValue){
		this.statusCode = statusCode;
		this.statusValue = statusValue;
	}
	
	public Integer getStatusCode(){
		return this.statusCode;
	}
	
	public String getStatusName(){
		return this.statusValue;
	}
	
	public static OrderStatus fromInt(final Integer statusCode) {

		OrderStatus enumeration = OrderStatus.Received;
        switch (statusCode) {
            case 100:
                enumeration = OrderStatus.Received;
            break;
            case 200:
                enumeration = OrderStatus.Accepted;
            break;
            case 300:
                enumeration = OrderStatus.Dispatched;
            break;
            case 400:
                enumeration = OrderStatus.Delivered;
            break;
            case 500:
                enumeration = OrderStatus.Cancelled;
            break;
        }
        return enumeration;
    }
	
	public static String getstatusDesc(OrderStatus status){
		switch(status){
		case Received : return "Order Created, waiting for Acceptance.";
		case Accepted : return "Order is accepted, will be dispatched soon.";
		case Dispatched : return "Order is Dispatched.";
		case Delivered : return "Order is Delivered.";
		case Cancelled : return "Order Cancelled.";
		default : return "Invalid Status";
		}
	}
	
	public static List<Map<String, Object>> getAllAsMap(){
		List<Map<String, Object>> allStatus = new ArrayList<>();
		Map<String,  Object> stsObj  = null;
		for(OrderStatus status : OrderStatus.values()){
			stsObj  =  new HashMap<>();
			stsObj.put("statusCode", status.statusCode);
			stsObj.put("statusMsg", status.statusValue);
			allStatus.add(stsObj);
		}
		return allStatus;
	}
    
	public static List<Integer> getAllStatusCodes(){
		List<Integer> statusCodes = new ArrayList<>();
		for(OrderStatus status : OrderStatus.values()){
			statusCodes.add( status.statusCode);
		}
		return statusCodes;
	}
}
