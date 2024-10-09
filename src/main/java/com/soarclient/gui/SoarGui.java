package com.soarclient.gui;

import com.soarclient.event.EventBus;
import com.soarclient.event.impl.RenderBlurEvent;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

public class SoarGui {

	protected Minecraft mc = Minecraft.getMinecraft();
	
	private static GuiScreen currentScreen;
	private static SoarGuiHandler handler;

	public void init() {
	}

	public void draw(int mouseX, int mouseY) {
	}

	public void drawBlur(RenderBlurEvent event) {
	}

	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
	}

	public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
	}

	public void keyTyped(char typedChar, int keyCode) {
	}

	public void onClosed() {
	}

	public static GuiScreen create(SoarGui gui) {

		if (handler == null) {
			handler = new SoarGuiHandler(gui);
			EventBus.getInstance().register(handler);
		}

		handler.setGui(gui);

		GuiScreen screen = new GuiScreen() {

			@Override
			public void initGui() {
				gui.init();
			}

			@Override
			public void drawScreen(int mouseX, int mouseY, float partialTicks) {
				gui.draw(mouseX, mouseY);
			}

			@Override
			public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
				gui.mouseClicked(mouseX, mouseY, mouseButton);
			}

			@Override
			public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
				gui.mouseReleased(mouseX, mouseY, mouseButton);
			}

			@Override
			public void keyTyped(char typedChar, int keyCode) {
				gui.keyTyped(typedChar, keyCode);
			}

			@Override
			public void onGuiClosed() {
				gui.onClosed();
			}
			
			@Override
			public boolean doesGuiPauseGame() {
				return false;
			}
		};
		
		currentScreen = screen;
		
		return screen;
	}

	public static GuiScreen getCurrentScreen() {
		return currentScreen;
	}
}