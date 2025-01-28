package com.soarclient.gui.modmenu.pages;

import com.soarclient.gui.api.SoarGui;
import com.soarclient.gui.api.page.Page;
import com.soarclient.gui.api.page.impl.RightLeftTransition;
import com.soarclient.skia.font.Icon;

public class ModsPage extends Page {

	public ModsPage(SoarGui parent) {
		super(parent, "text.mods", Icon.INVENTORY_2, new RightLeftTransition(true));
	}

}
