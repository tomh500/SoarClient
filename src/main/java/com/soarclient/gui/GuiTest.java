package com.soarclient.gui;

import com.soarclient.stk.Stk;
import com.soarclient.stk.api.Align;
import com.soarclient.stk.api.Window;

import net.minecraft.client.gui.GuiScreen;

public class GuiTest {

	public static GuiScreen getTestGui() {
		
		Window window = Stk.createWindow(1680, 1120, Align.MIDDLE_CENTER);
		
		return Stk.create(window);
	}
}
