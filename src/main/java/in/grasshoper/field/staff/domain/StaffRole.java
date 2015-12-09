package in.grasshoper.field.staff.domain;

public enum StaffRole {
	HoperBoy(1000,"StaffRole.hopperboy");
	
	private final Integer roleCode;
	private final String roleValue;
	
	private StaffRole(Integer roleCode, String roleValue) {
		this.roleCode = roleCode;
		this.roleValue = roleValue;
	}

	public Integer getRoleCode() {
		return this.roleCode;
	}

	public String getRoleValue() {
		return this.roleValue;
	}
	
}
