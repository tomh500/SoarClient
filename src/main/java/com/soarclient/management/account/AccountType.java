package com.soarclient.management.account;

public enum AccountType {
	CRACKED(0), PREMIUM(1);
	
	private int id;
	
	private AccountType(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}
	
	public static AccountType get(int id) {
		
		for(AccountType at : AccountType.values()) {
			if(id == at.getId()) {
				return at;
			}
		}
		
		return null;
	}
}
