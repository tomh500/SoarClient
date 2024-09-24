package com.soarclient.stk;

import com.soarclient.event.EventHandler;
import com.soarclient.event.impl.RenderBlurEvent;
import com.soarclient.stk.api.Window;
import com.soarclient.stk.gui.GuiSoarScreen;
import com.soarclient.stk.styles.Style;

import net.minecraft.client.Minecraft;

public class StkHandler {

	@EventHandler
	public void onRenderBlur(RenderBlurEvent event) {

		if (Minecraft.getMinecraft().currentScreen instanceof GuiSoarScreen) {

			GuiSoarScreen screen = (GuiSoarScreen) Minecraft.getMinecraft().currentScreen;
			Window window = screen.getWindow();

			event.setupAndDraw(() -> {
				event.drawRoundedRect(window.getX(), window.getY(), window.getWidth(), window.getHeight(),
						Style.dpToPixel(50), 1F);
			});
		}
	}
}
