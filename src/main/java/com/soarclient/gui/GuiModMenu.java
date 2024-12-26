package com.soarclient.gui;

import java.awt.Color;

import com.soarclient.skia.Skia;
import com.soarclient.skia.ui.SkiaUI;

public class GuiModMenu extends SkiaUI {

	@Override
	public void draw(float mouseX, float mouseY) {
		Skia.drawRect(0, 0, 100, 100, Color.WHITE);
		
	}

	@Override
	public void mousePressed(float mouseX, float mouseY, int mouseButton) {
		
	}

	@Override
	public void mouseReleased(float mouseX, float mouseY, int mouseButton) {
		
	}

	@Override
	public void keyTyped(char typedChar, int keyCode) {
		
	}
}
