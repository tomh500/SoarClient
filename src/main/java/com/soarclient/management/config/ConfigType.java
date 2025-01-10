package com.soarclient.management.config;

public enum ConfigType {
	MOD("mod");
	
	private final String id;
	
	private ConfigType(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}
}
