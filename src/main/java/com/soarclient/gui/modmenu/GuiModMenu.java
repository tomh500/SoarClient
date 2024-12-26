package com.soarclient.gui.modmenu;

import com.soarclient.Soar;
import com.soarclient.gui.api.page.PageGui;
import com.soarclient.management.color.api.ColorPalette;
import com.soarclient.skia.Skia;

import io.jsonwebtoken.lang.Arrays;
import net.minecraft.client.Minecraft;

public class GuiModMenu extends PageGui {

	private Minecraft mc = Minecraft.getMinecraft();
	
	public GuiModMenu() {
		super(Arrays.asList(null));
	}
	
	@Override
	public void draw(int mouseX, int mouseY) {
		
		ColorPalette palette = Soar.getInstance().getColorManager().getPalette();
		
		Skia.drawRoundedRect(getX(), getY(), getWidth(), getHeight(), 35, palette.getSurfaceContainer());
		
		if(currentPage != null) {
			currentPage.draw(mouseX, mouseY);
		}
	}

	@Override
	public float getX() {
		return (mc.displayWidth / 2) - (getWidth() / 2);
	}

	@Override
	public float getY() {
		return (mc.displayHeight / 2) - (getHeight() / 2);
	}

	@Override
	public float getWidth() {
		return 938;
	}

	@Override
	public float getHeight() {
		return 580;
	}
}
