package com.soarclient.gui.modmenu.pages;

import com.soarclient.gui.api.page.Page;
import com.soarclient.gui.api.page.PageGui;
import com.soarclient.gui.api.page.impl.LeftTransition;
import com.soarclient.skia.font.Icon;

public class SettingsPage extends Page {

	public SettingsPage(PageGui parent) {
		super(parent, "text.settings", Icon.SETTINGS, new LeftTransition(true));
	}

}
