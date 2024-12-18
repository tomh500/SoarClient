package com.soarclient.management.proxy;

public class ProxyManager {

	private boolean enabled;
	private String address;
	private ProxyType type;

	public ProxyManager() {
		enabled = false;
		address = "";
		type = ProxyType.HTTP;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public ProxyType getType() {
		return type;
	}

	public void setType(ProxyType type) {
		this.type = type;
	}
}