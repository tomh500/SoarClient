package com.soarclient.stk;

import com.soarclient.stk.api.Align;
import com.soarclient.stk.api.Window;
import com.soarclient.stk.component.impl.Box;
import com.soarclient.stk.gui.GuiSoarScreen;

public class Stk {
	
	public static GuiSoarScreen create(Window window) {
		return new GuiSoarScreen(window);
	}
	
	public static Window createWindow(float width, float height, Align align) {
		return new Window(width, height, align);
	}
	
	public static Box createBox(float width, float height, Align align) {
		return new Box(width, height, align);
	}
}
