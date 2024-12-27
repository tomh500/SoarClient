package com.soarclient.gui.modmenu;

import com.soarclient.Soar;
import com.soarclient.gui.api.page.Page;
import com.soarclient.gui.api.page.PageGui;
import com.soarclient.management.color.api.ColorPalette;
import com.soarclient.skia.Skia;
import com.soarclient.skia.font.Icon;
import com.soarclient.ui.component.Component;
import com.soarclient.ui.component.api.Size;
import com.soarclient.ui.component.api.Style;
import com.soarclient.ui.component.impl.button.IconButton;

import io.jsonwebtoken.lang.Arrays;
import net.minecraft.client.Minecraft;

public class GuiModMenu extends PageGui {

	private Minecraft mc = Minecraft.getMinecraft();
	
	public GuiModMenu() {
		super(Arrays.asList(null));
	}
	
	@Override
	public void init() {
		
		components.clear();
		
		for(Page p : pages) {
			p.setX(getX());
			p.setY(getY());
			p.setWidth(getWidth());
			p.setHeight(getHeight());
		}
		
		components.add(new IconButton(Icon.EDIT, getX() + 10, getY() + 10, Size.NORMAL, Style.PRIMARY));
	}

	@Override
	public void draw(int mouseX, int mouseY) {
		
		ColorPalette palette = Soar.getInstance().getColorManager().getPalette();
		
		Skia.drawRoundedRect(getX(), getY(), getWidth(), getHeight(), 35, palette.getSurfaceContainer());
		
		if(currentPage != null) {
			currentPage.draw(mouseX, mouseY);
		}
		
		for(Component c : components) {
			c.draw(mouseX, mouseY);
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
