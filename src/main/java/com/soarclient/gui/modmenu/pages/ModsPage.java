package com.soarclient.gui.modmenu.pages;

import com.soarclient.gui.api.page.Page;
import com.soarclient.gui.api.page.PageGui;
import com.soarclient.gui.api.page.impl.LeftTransition;
import com.soarclient.skia.font.Icon;

public class ModsPage extends Page {

	public ModsPage(PageGui parent) {
		super(parent, "text.mods", Icon.INVENTORY_2, new LeftTransition(true));
	}
}
