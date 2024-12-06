package com.soarclient.management.color;

import com.soarclient.libraries.material3.hct.Hct;
import com.soarclient.management.color.api.ColorPalette;
import com.soarclient.management.mods.impl.settings.ModMenuSetting;

public class ColorManager {

	private ColorPalette palette;
	
	public ColorManager() {
		updatePalette();
	}

	public void onTick() {

		ModMenuSetting m = ModMenuSetting.getInstance();
		Hct hct = m.getHctColorSetting().getHct();

		if (palette == null) {
			updatePalette();
		}

		if (palette.isDarkMode() != m.getDarkModeSetting().isEnabled()) {
			updatePalette();
		}

		if (palette.getHct() != hct) {
			updatePalette();
		}
	}

	private void updatePalette() {

		ModMenuSetting m = ModMenuSetting.getInstance();

		palette = new ColorPalette(m.getHctColorSetting().getHct(), m.getDarkModeSetting().isEnabled());
	}
	
	public ColorPalette getPalette() {
		return palette;
	}
}
