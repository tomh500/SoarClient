package com.soarclient.management.mod.impl.hud;

import com.soarclient.management.mod.api.hud.SimpleHUDMod;
import com.soarclient.skia.font.Icon;

public class DayCounterMod extends SimpleHUDMod {

	public DayCounterMod() {
		super("mod.daycounter.name", "mod.daycounter.description", Icon.TODAY);
	}

	@Override
	public String getText() {
		long time = mc.theWorld.getWorldInfo().getWorldTotalTime() / 24000L;
		return time + " Day" + (time != 1L ? "s" : "");
	}

	@Override
	public String getIcon() {
		return Icon.TODAY;
	}
}