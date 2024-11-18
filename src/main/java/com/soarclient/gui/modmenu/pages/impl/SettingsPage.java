package com.soarclient.gui.modmenu.pages.impl;

import com.soarclient.gui.Page;
import com.soarclient.gui.PageDirection;
import com.soarclient.nanovg.font.Icon;

public class SettingsPage extends Page {

	public SettingsPage(float x, float y, float width, float height) {
		super(PageDirection.LEFT, "text.settings", Icon.SETTINGS, x, y, width, height);
	}

}
