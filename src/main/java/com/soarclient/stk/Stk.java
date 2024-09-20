package com.soarclient.stk;

import com.soarclient.stk.api.Align;
import com.soarclient.stk.api.ColorPalette;
import com.soarclient.stk.api.Size;
import com.soarclient.stk.api.Window;
import com.soarclient.stk.gui.GuiSoarScreen;

public class Stk {
	
	public static GuiSoarScreen create(Window window) {
		return new GuiSoarScreen(window);
	}
	
	public static Window createWindow(Size size, ColorPalette palette, Align align) {
		return new Window(size, palette, align);
	}
}
