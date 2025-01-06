package com.soarclient.skia.font;

import io.github.humbleui.skija.Font;

public class Fonts {

	public static Font getRegular(float size) {
		return FontHelper.load("Inter-Regular-CJKsc.ttf", size);
	}

	public static Font getMedium(float size) {
		return FontHelper.load("Inter-Medium-CJKsc.ttf", size);
	}

	public static Font getLight(float size) {
		return FontHelper.load("Inter-Light-CJKsc.ttf", size);
	}

	public static Font getBold(float size) {
		return FontHelper.load("Inter-Bold-CJKsc.ttf", size);
	}

	public static Font getIconFill(float size) {
		return FontHelper.load("MaterialSymbolsRounded_Fill.ttf", size);
	}

	public static Font getIcon(float size) {
		return FontHelper.load("MaterialSymbolsRounded.ttf", size);
	}

	public static Font getNotoColorEmoji(float size) {
		return FontHelper.load("NotoColorEmoji.ttf", size);
	}
}
