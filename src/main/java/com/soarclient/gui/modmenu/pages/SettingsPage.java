package com.soarclient.gui.modmenu.pages;

import com.soarclient.gui.api.page.Page;
import com.soarclient.gui.api.page.PageGui;
import com.soarclient.gui.api.page.impl.RightLeftTransition;
import com.soarclient.skia.font.Icon;

public class SettingsPage extends Page {

	public SettingsPage(PageGui parent) {
		super(parent, "text.settings", Icon.SETTINGS, new RightLeftTransition(true));
	}

}
