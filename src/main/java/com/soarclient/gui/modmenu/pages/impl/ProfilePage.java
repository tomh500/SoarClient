package com.soarclient.gui.modmenu.pages.impl;

import com.soarclient.gui.Page;
import com.soarclient.gui.PageDirection;
import com.soarclient.nanovg.font.Icon;

public class ProfilePage extends Page {

	public ProfilePage(float x, float y, float width, float height) {
		super(PageDirection.LEFT, "text.profile", Icon.DESCRIPTION, x, y, width, height);
	}

}
