package com.soarclient.utils.mouse;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import com.soarclient.animation.SimpleAnimation;
import com.soarclient.utils.math.MathUtils;

public class ScrollHelper {

	private SimpleAnimation animation = new SimpleAnimation();
	private float scroll, maxScroll, minScroll;

	public ScrollHelper() {
		maxScroll = Float.MAX_VALUE;
		minScroll = 0;
	}

	public void onScroll() {

		float dWheel = Mouse.getDWheel();

		scroll += dWheel * (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) ? 100F : 60F);

		animation.onTick(scroll, 18);
		scroll = Math.max(Math.min(minScroll, scroll), -maxScroll);
	}

	public float getValue() {
		return animation.getValue();
	}

	public void setMaxScroll(float offsetY, int size, float viewHeight, float rowHeight, float rowSpacing,
			int rowCount) {
		int totalRows = (int) Math.ceil((double) size / rowCount);
		float totalContentHeight = offsetY + (totalRows * rowHeight) + ((totalRows - 1) * rowSpacing);
		float maxScroll = Math.max(0, totalContentHeight - viewHeight);

		this.maxScroll = MathUtils.clamp(maxScroll, 0, maxScroll);
	}
}