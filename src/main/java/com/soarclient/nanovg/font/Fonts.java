package com.soarclient.nanovg.font;

import java.util.ArrayList;
import java.util.Arrays;

import net.minecraft.util.ResourceLocation;

public class Fonts {

	private static final String PATH = "soar/fonts/";

	public static final Font LIGHT = new Font("light", new ResourceLocation(PATH + "Inter-Light-CJKsc.ttf"));
	public static final Font REGULAR = new Font("regular", new ResourceLocation(PATH + "Inter-Regular-CJKsc.ttf"));
	public static final Font MEDIUM = new Font("medium", new ResourceLocation(PATH + "Inter-Medium-CJKsc.ttf"));
	public static final Font BOLD = new Font("bold", new ResourceLocation(PATH + "Inter-Bold-CJKsc.ttf"));
	public static final Font ICON = new Font("icon", new ResourceLocation(PATH + "MaterialSymbolsRounded.ttf"));
	public static final Font ICON_FILL = new Font("icon-fill",
			new ResourceLocation(PATH + "MaterialSymbolsRounded_Fill.ttf"));

	public static ArrayList<Font> getFonts() {
		return new ArrayList<Font>(Arrays.asList(REGULAR, LIGHT, MEDIUM, BOLD, ICON, ICON_FILL));
	}
}
