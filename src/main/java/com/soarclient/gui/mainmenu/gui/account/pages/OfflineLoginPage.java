package com.soarclient.gui.mainmenu.gui.account.pages;

import com.soarclient.gui.api.page.Page;
import com.soarclient.gui.api.page.PageGui;
import com.soarclient.gui.api.page.impl.LeftTransition;

public class OfflineLoginPage extends Page {

	public OfflineLoginPage(PageGui parent) {
		super(parent, "", "", new LeftTransition(true));
	}

}
