package com.soarclient.gui.api.page;

public abstract class PageTransition {
	
	private final boolean consecutive;
	
	public PageTransition(boolean consecutive) {
		this.consecutive = consecutive;
	}
	
	public abstract void onTransition();

	public boolean isConsecutive() {
		return consecutive;
	}
}
