package com.soarclient.management.mods.impl.hud;

import java.util.ArrayList;

import org.lwjgl.input.Mouse;

import com.soarclient.event.EventHandler;
import com.soarclient.event.impl.ClientTickEvent;
import com.soarclient.event.impl.MouseClickEvent;
import com.soarclient.management.mods.api.SimpleHUDMod;
import com.soarclient.management.mods.settings.impl.BooleanSetting;
import com.soarclient.nanovg.font.Icon;

public class CPSDisplayMod extends SimpleHUDMod {

	private ArrayList<Long> leftPresses = new ArrayList<Long>();
	private ArrayList<Long> rightPresses = new ArrayList<Long>();

	private BooleanSetting rightClickSetting = new BooleanSetting("setting.rightclick",
			"setting.cps.rightclick.description", Icon.RIGHT_CLICK, this, true);

	public CPSDisplayMod() {
		super("mod.cpsdisplay.name", "mod.cpsdisplay.description", Icon.LEFT_CLICK);
	}

	@EventHandler
	public void onMouseClick(MouseClickEvent event) {

		if (Mouse.getEventButtonState()) {

			if (event.getButton() == 0) {
				leftPresses.add(System.currentTimeMillis());
			}

			if (event.getButton() == 1) {
				rightPresses.add(System.currentTimeMillis());
			}
		}
	}

	@EventHandler
	public void onClientTick(ClientTickEvent event) {
		leftPresses.removeIf(t -> System.currentTimeMillis() - t > 1000);
		rightPresses.removeIf(t -> System.currentTimeMillis() - t > 1000);
	}

	@Override
	public String getText() {
		return (rightClickSetting.isEnabled() ? leftPresses.size() + " | " + rightPresses.size() : leftPresses.size())
				+ " CPS";
	}

	@Override
	public String getIcon() {
		return Icon.MOUSE;
	}
}
