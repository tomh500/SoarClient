package com.soarclient.gui;

import java.awt.Color;

import org.lwjgl.glfw.GLFW;

import com.soarclient.gui.api.SimpleSoarGui;
import com.soarclient.skia.Skia;

public class TestGui extends SimpleSoarGui {

	public TestGui() {
		super(true);
	}

	@Override
	public void draw(double mouseX, double mouseY) {
		Skia.drawRect(0, 0, 100, 100, Color.WHITE);
	}
	
	@Override
	public void keyPressed(int keyCode, int scanCode, int modifiers) {
		
		if(keyCode == GLFW.GLFW_KEY_ESCAPE) {
			client.setScreen(null);
		}
	}
}
