package com.soarclient.nanovg.font;

import java.util.ArrayList;
import java.util.Arrays;

public class Fonts {

	private static final String PATH = "/assets/soar/fonts";

	public static final Font LIGHT = new Font("light", PATH + "Inter-Light-CJKsc.ttf");
	public static final Font REGULAR = new Font("regular", PATH + "Inter-Regular-CJKsc.ttf");
	public static final Font MEDIUM = new Font("medium", PATH + "Inter-Medium-CJKsc.ttf");
	public static final Font BOLD = new Font("bold", PATH + "Inter-Bold-CJKsc.ttf");
	public static final Font ICON = new Font("icon", PATH + "MaterialSymbolsRounded.ttf");
	public static final Font ICON_FILL = new Font("icon-fill", PATH + "MaterialSymbolsRounded_Fill.ttf");

	public static ArrayList<Font> getFonts() {
		return new ArrayList<Font>(Arrays.asList(REGULAR, LIGHT, MEDIUM, BOLD, ICON, ICON_FILL));
	}
}
