package com.soarclient.libraries.soarium.gui.component;

import com.soarclient.libraries.soarium.gui.component.handler.SoariumComboBoxHandler;
import com.soarclient.libraries.soarium.gui.locale.SoariumI18n;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

public class GuiSoariumComboBox extends GuiButton {

	private String[] options;
	private int currentIndex;
	private String name;
	private SoariumComboBoxHandler handler;

	public GuiSoariumComboBox(int x, int y, String name, String defaultOption, String[] options,
			SoariumComboBoxHandler handler) {
		super(-1, x, y, 150, 20, "");
		this.name = name;
		this.options = options;
		this.currentIndex = findIndex(defaultOption);
		this.handler = handler;
		updateDisplayString();
	}

	private void updateDisplayString() {
		this.displayString = name + ": " + SoariumI18n.get(getCurrentOption());
	}

	@Override
	public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
		if (super.mousePressed(mc, mouseX, mouseY)) {
			currentIndex = (currentIndex + 1) % options.length;
			handler.onAction(getCurrentOption());
			updateDisplayString();
			return true;
		}
		return false;
	}

	public String getCurrentOption() {
		if (options != null && options.length > 0) {
			return options[currentIndex];
		}
		return options[0];
	}

	public int getCurrentIndex() {
		return currentIndex;
	}

	private int findIndex(String defaultValue) {
		if (options != null && defaultValue != null) {
			for (int i = 0; i < options.length; i++) {
				if (options[i].equals(defaultValue)) {
					return i;
				}
			}
		}
		return 0;
	}
}
