package com.soarclient.gui.mainmenu.api;

import com.soarclient.gui.Page;
import com.soarclient.management.color.api.ColorPalette;

public class MainMenuAPI {

	private static ColorPalette palette;
	private static Page currentPage;

	public static ColorPalette getPalette() {
		return palette;
	}

	public static void setPalette(ColorPalette palette) {
		MainMenuAPI.palette = palette;
	}

	public static Page getCurrentPage() {
		return currentPage;
	}

	public static void setCurrentPage(Page currentPage) {

		if (MainMenuAPI.currentPage != null) {
			MainMenuAPI.currentPage.onClosed();
		}

		MainMenuAPI.currentPage = currentPage;
		currentPage.init();
	}
}
