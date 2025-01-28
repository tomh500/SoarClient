package com.soarclient.utils.mouse;

import com.soarclient.animation.SimpleAnimation;

public class ScrollHelper {

	private SimpleAnimation animation = new SimpleAnimation();
	private float scroll, maxScroll, minScroll;

	public ScrollHelper() {
		maxScroll = Float.MAX_VALUE;
		minScroll = 0;
	}

	public void onScroll(double amount) {
		scroll += amount * 60;
	}
	
	public void onUpdate() {
		animation.onTick(scroll, 18);
		scroll = Math.max(Math.min(minScroll, scroll), -maxScroll);
	}

	public float getValue() {
		return animation.getValue();
	}

	public void reset() {
		this.scroll = 0;
	}
}