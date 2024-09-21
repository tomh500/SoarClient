package com.soarclient.stk.palette;

import com.soarclient.libraries.materialyou.hct.Hct;

public class ColorHelper {

	private static ColorPalette palette;
	
	static {
		palette = new ColorPalette(Hct.from(220, 26, 6), false);
	}

	public static ColorPalette getPalette() {
		return palette;
	}

	public static void setPalette(ColorPalette palette) {
		ColorHelper.palette = palette;
	}
}
