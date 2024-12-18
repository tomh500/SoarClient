package com.soarclient.management.proxy;

public enum ProxyType {
	SOCKS("proxy.socks"), HTTP("proxy.http");

	private String id;

	private ProxyType(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public static ProxyType getById(String id) {

		for (ProxyType t : values()) {
			if (t.getId().equals(id)) {
				return t;
			}
		}

		return SOCKS;
	}
}