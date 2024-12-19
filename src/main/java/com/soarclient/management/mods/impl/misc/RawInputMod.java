package com.soarclient.management.mods.impl.misc;

import com.soarclient.management.mods.Mod;
import com.soarclient.management.mods.ModCategory;
import com.soarclient.nanovg.font.Icon;

public class RawInputMod extends Mod {

	private static RawInputMod instance;

	public RawInputMod() {
		super("mod.rawinput.name", "mod.rawinput.description", Icon.HIGHLIGHT_MOUSE_CURSOR, ModCategory.MISC);

		instance = this;
	}

	public static RawInputMod getInstance() {
		return instance;
	}
}