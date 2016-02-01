package in.grasshoper.field.transaction.domain;


public enum TranTypeEnum {
	CREDIT(0,"Credit"),
	DEBIT(1,"Debit"),
	RESET(2,"Debit");
	
	private final Integer tranCode;
	private final String tranValue;
	TranTypeEnum(Integer tranCode, String tranValue){
		this.tranCode = tranCode;
		this.tranValue = tranValue;
	}
	
	public Integer getTranCode(){
		return this.tranCode;
	}
	
	public String getTranValue(){
		return this.tranValue;
	}
	
	
	public static TranTypeEnum fromInt(final Integer tranCode) {

		TranTypeEnum enumeration = null;
        switch (tranCode) {
            case 0:
                enumeration = TranTypeEnum.DEBIT;
            break;
            case 1:
                enumeration = TranTypeEnum.CREDIT;
            break;
            case 2:
            	enumeration = TranTypeEnum.RESET;
        }
        return enumeration;
    }
}
