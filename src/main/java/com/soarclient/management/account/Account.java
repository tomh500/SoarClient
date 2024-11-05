package com.soarclient.management.account;

public class Account {

	private String name;
	private String uuid;
	private String refreshToken;
	private AccountType type;
	
	public Account(String name, String uuid, String refreshToken, AccountType type) {
		this.name = name;
		this.uuid = uuid;
		this.refreshToken = refreshToken;
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public String getUuid() {
		return uuid;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public AccountType getType() {
		return type;
	}
}
