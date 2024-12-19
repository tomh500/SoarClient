package com.soarclient.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

public class SoarGui {

	protected Minecraft mc = Minecraft.getMinecraft();

	public void init() {
	}

	public void draw(int mouseX, int mouseY) {
	}

	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
	}

	public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
	}

	public void keyTyped(char typedChar, int keyCode) {
	}

	public void onClosed() {
	}

	public GuiScreen create() {
		return new GuiScreen() {

			@Override
			public void initGui() {
				SoarGui.this.init();
			}

			@Override
			public void drawScreen(int mouseX, int mouseY, float partialTicks) {
				SoarGui.this.draw(mouseX, mouseY);
			}

			@Override
			public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
				SoarGui.this.mouseClicked(mouseX, mouseY, mouseButton);
			}

			@Override
			public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
				SoarGui.this.mouseReleased(mouseX, mouseY, mouseButton);
			}

			@Override
			public void keyTyped(char typedChar, int keyCode) {
				SoarGui.this.keyTyped(typedChar, keyCode);
			}

			@Override
			public void onGuiClosed() {
				SoarGui.this.onClosed();
			}

			@Override
			public boolean doesGuiPauseGame() {
				return false;
			}
		};
	}
}
