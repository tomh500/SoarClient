package com.soarclient;

import com.soarclient.event.EventHandler;
import com.soarclient.event.impl.UpdateFramebufferSizeEvent;
import com.soarclient.ui.math.Style;

import net.minecraft.client.Minecraft;

public class SoarHandler {

	private Minecraft mc = Minecraft.getMinecraft();
	
	@EventHandler
	public void onUpdateFramebufferSize(UpdateFramebufferSizeEvent event) {
		
		float baseWidth = 1920;
		float baseHeight = 1080;
		float resolutionWidth = mc.displayWidth;
		float resolutionHeight = mc.displayHeight;
		float baseDpi = 100;
        double baseDiagonalPixels = Math.sqrt(baseWidth * baseWidth + baseHeight * baseHeight);
        double currentDiagonalPixels = Math.sqrt(resolutionWidth * resolutionWidth + resolutionHeight * resolutionHeight);
        double relativeDpi = (currentDiagonalPixels / baseDiagonalPixels) * baseDpi;
        
        Style.DPI = (float) Math.ceil(relativeDpi);
        mc.displayGuiScreen(mc.currentScreen);
	}
}
