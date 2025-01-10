package com.soarclient.gui.modmenu.pages;

import com.soarclient.gui.api.page.Page;
import com.soarclient.gui.api.page.PageGui;
import com.soarclient.gui.api.page.impl.LeftTransition;
import com.soarclient.skia.font.Icon;

public class MusicPage extends Page {

	public MusicPage(PageGui parent) {
		super(parent, "text.music", Icon.MUSIC_NOTE, new LeftTransition(true));
	}
}
