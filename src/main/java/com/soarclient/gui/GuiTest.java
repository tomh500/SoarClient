package com.soarclient.gui;

import com.soarclient.skia.Skia;
import com.soarclient.skia.ui.SkiaUI;

public class GuiTest extends SkiaUI {

	@Override
	public void draw(float mouseX, float mouseY) {
		Skia.drawImage("background.png", 0, 0, 100, 100);
	}

	@Override
	public void mousePressed(float mouseX, float mouseY, int mouseButton) {

	}

	@Override
	public void mouseReleased(float mouseX, float mouseY, int mouseButton) {

	}
}
