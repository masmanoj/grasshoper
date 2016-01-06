package in.grasshoper.field.order.domain;

public enum OrderStatus {
	Received(100,"Received");
	
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
}
