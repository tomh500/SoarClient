package com.soarclient.ui;

import com.soarclient.nanovg.NanoVGHelper;
import com.soarclient.ui.color.Colors;
import com.soarclient.ui.math.Style;

public class UIRenderer {

	public static void drawWindow(float x, float y, float width, float height, float radius) {
		
		NanoVGHelper nvg = NanoVGHelper.getInstance();
		
		nvg.drawRoundedRect(x, y, width, height, radius, Colors.WINDOW);
		nvg.drawOutline(x, y, width, height, radius, Style.px(1.5F), Colors.STROKE_WINDOW);
	}
}
