package com.soarclient.gui;

import java.awt.Color;

import com.soarclient.animation.SimpleAnimation;
import com.soarclient.nanovg.NanoVGHelper;
import com.soarclient.stk.renderer.AppleRenderer;
import com.soarclient.utils.TimerUtils;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;

public class GuiAnimation extends GuiScreen {

	private float x, y, width, height;
	
	private SimpleAnimation animation = new SimpleAnimation();
	private TimerUtils timer = new TimerUtils();
	
	@Override
	public void initGui() {
		width = 200;
		height = 300;
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		
		NanoVGHelper nvg = NanoVGHelper.getInstance();
		
		ScaledResolution sr = new ScaledResolution(mc);
		
		x = (sr.getScaledWidth() / 2) - (width / 2);
		y = (sr.getScaledHeight() / 2) - (height / 2);
		
		if(timer.delay(5)) {
			
			if(width >= 850) {
				width = 200;
			} else {
				width++;
			}
			
			timer.reset();
		}
		
		nvg.setupAndDraw(() -> {
			nvg.drawRect(0, 0, 2000, 2000, Color.BLACK);
			AppleRenderer.drawWindow(x, y, width, height, 6);
		});
	}
}
