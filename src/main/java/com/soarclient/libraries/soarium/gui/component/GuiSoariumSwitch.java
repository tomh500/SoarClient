package com.soarclient.libraries.soarium.gui.component;

import com.soarclient.libraries.soarium.gui.component.handler.SoariumSwitchHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

public class GuiSoariumSwitch extends GuiButton {

	private boolean state;
	private String suffix;
	private String buttonText;
	private SoariumSwitchHandler handler;
	
	public GuiSoariumSwitch(int x, int y, String buttonText, boolean state, SoariumSwitchHandler handler) {
		super(-1, x, y, 150, 20, "");
		this.handler = handler;
		this.state = state;
		this.buttonText = buttonText;
		this.suffix = getSuffix();
		this.displayString = buttonText + suffix;
	}
	
	@Override
	public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
		
		if (super.mousePressed(mc, mouseX, mouseY)) {
			state = !state;
			handler.onAction(state);
			this.displayString = buttonText + getSuffix();
			return true;
		}
		
		return false;
	}
	
	private String getSuffix() {
		return suffix = ": " + (state ? "ON" : "OFF");
	}
}
