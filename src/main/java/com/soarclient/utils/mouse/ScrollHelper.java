package com.soarclient.utils.mouse;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import com.soarclient.animation.Animation;
import com.soarclient.animation.Duration;
import com.soarclient.animation.cubicbezier.impl.EaseEmphasizedDecelerate;
import com.soarclient.animation.cubicbezier.impl.EaseStandard;
import com.soarclient.utils.math.MathUtils;

public class ScrollHelper {

	private Animation animation;
	private float scroll, maxScroll, minScroll, rawScroll;
	private float prevMaxScroll;

	public ScrollHelper() {
		maxScroll = Float.MAX_VALUE;
		minScroll = 0;
		rawScroll = 0;
	}

	public void onScroll() {

		float dWheel = Mouse.getDWheel();

		scroll = animation == null ? rawScroll : animation.getValue();

		rawScroll += dWheel * (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) ? 100F : 60F);
		rawScroll = Math.max(Math.min(minScroll, rawScroll), -maxScroll);

		if (dWheel != 0) {
			animation = new EaseStandard(Duration.SHORT_3, scroll, rawScroll);
		} else if (prevMaxScroll != maxScroll) {
			this.prevMaxScroll = maxScroll;
			animation = new EaseEmphasizedDecelerate(Duration.MEDIUM_4, scroll, rawScroll);
		}
	}

	public float getValue() {
		return scroll;
	}

	public void setMaxScroll(float maxScroll) {
		this.prevMaxScroll = this.maxScroll;
		this.maxScroll = MathUtils.clamp(maxScroll, 0, maxScroll);
	}
}