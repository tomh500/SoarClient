package com.soarclient.stk.gui;

import com.soarclient.stk.api.Window;

import net.minecraft.client.gui.GuiScreen;

public class GuiSoarScreen extends GuiScreen {

	private Window window;
	
	public GuiSoarScreen(Window window) {
		this.window = window;
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		window.draw(mouseX, mouseY);
	}
	
	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		window.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	@Override
	public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
		window.mouseReleased(mouseX, mouseY, mouseButton);
	}
	
	@Override
    public boolean doesGuiPauseGame() {
        return false;
    }

	public Window getWindow() {
		return window;
	}
}
