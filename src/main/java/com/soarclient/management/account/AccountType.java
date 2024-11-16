package com.soarclient.management.account;

public enum AccountType {
	BEDROCK("bedrock"), MICROSOFT("microsoft"), OFFLINE("offline"), NULL("null");
	
	private String id;
	
	private AccountType(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}
	
	public static AccountType getById(String id) {
		
		for(AccountType t : AccountType.values()) {
			if(t.getId().equals(id)) {
				return t;
			}
		}
		
		return NULL;
	}
}
