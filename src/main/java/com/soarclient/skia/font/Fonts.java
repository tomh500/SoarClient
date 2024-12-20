package com.soarclient.skia.font;

import io.github.humbleui.skija.Font;

public class Fonts {

	public static Font getRegular(float size) {
		return FontHelper.load("Inter-Regular-CJKsc.ttf", size);
	}
}
