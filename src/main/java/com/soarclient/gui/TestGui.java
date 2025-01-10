package com.soarclient.gui;

import com.soarclient.gui.api.SoarGui;
import com.soarclient.gui.api.page.impl.DownTransition;

public class TestGui extends SoarGui {

	public TestGui() {
		super(new DownTransition(false), true, true);
	}

	@Override
	public float getX() {
		return (mc.displayWidth / 2) - (getWidth() / 2);
	}

	@Override
	public float getY() {
		return (mc.displayHeight / 2) - (getHeight() / 2);
	}

	@Override
	public float getWidth() {
		return 938;
	}

	@Override
	public float getHeight() {
		return 580;
	}
}
