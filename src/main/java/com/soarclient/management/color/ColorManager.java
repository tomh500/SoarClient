package com.soarclient.management.color;

import com.soarclient.libraries.material3.hct.Hct;
import com.soarclient.management.color.api.ColorPalette;

public class ColorManager {

	private ColorPalette palette;
	
	public ColorManager() {
		palette = new ColorPalette(Hct.from(220, 26, 6), false);
	}

	public ColorPalette getPalette() {
		return palette;
	}
}
