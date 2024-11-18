package com.soarclient.gui.modmenu.pages.impl;

import com.soarclient.gui.Page;
import com.soarclient.gui.PageDirection;
import com.soarclient.nanovg.font.Icon;

public class HomePage extends Page {

	public HomePage(float x, float y, float width, float height) {
		super(PageDirection.LEFT, "text.home", Icon.HOME, x, y, width, height);
	}
}
