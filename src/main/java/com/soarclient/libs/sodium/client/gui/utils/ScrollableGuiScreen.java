package com.soarclient.libs.sodium.client.gui.utils;

import java.io.IOException;
import java.util.List;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Mouse;

public abstract class ScrollableGuiScreen extends GuiScreen {
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.handleMouseScroll(mouseX, mouseY, partialTicks);
	}

	public abstract List<? extends Element> children();

	protected void handleMouseScroll(int mouseX, int mouseY, float partialTicks) {
		while (!this.mc.gameSettings.touchscreen && Mouse.next()) {
			int dWheel = Mouse.getEventDWheel();
			if (dWheel != 0) {
				for (Element child : this.children()) {
					child.mouseScrolled((double)mouseX, (double)mouseY, (double)dWheel);
				}
			}

			try {
				this.mc.currentScreen.handleMouseInput();
			} catch (IOException e) {}
		}
	}
}
