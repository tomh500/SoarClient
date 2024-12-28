package com.soarclient.gui.modmenu;

import java.util.ArrayList;
import java.util.List;

import com.soarclient.Soar;
import com.soarclient.gui.api.page.Page;
import com.soarclient.gui.api.page.PageGui;
import com.soarclient.gui.modmenu.pages.HomePage;
import com.soarclient.gui.modmenu.pages.ModsPage;
import com.soarclient.gui.modmenu.pages.MusicPage;
import com.soarclient.gui.modmenu.pages.ProfilePage;
import com.soarclient.gui.modmenu.pages.SettingsPage;
import com.soarclient.management.color.api.ColorPalette;
import com.soarclient.skia.Skia;
import com.soarclient.ui.component.Component;
import com.soarclient.ui.component.impl.NavigationRail;

public class GuiModMenu extends PageGui {

	private NavigationRail navigationRail;

	@Override
	public void init() {

		components.clear();
		
		navigationRail = new NavigationRail(this, getX(), getY(), 90, getHeight());
		components.add(navigationRail);
		
		for (Page p : pages) {
			p.setX(getX() + navigationRail.getWidth());
			p.setY(getY());
			p.setWidth(getWidth() - navigationRail.getHeight());
			p.setHeight(getHeight());
		}
	}

	@Override
	public void draw(int mouseX, int mouseY) {

		ColorPalette palette = Soar.getInstance().getColorManager().getPalette();

		Skia.drawRoundedRect(getX(), getY(), getWidth(), getHeight(), 35, palette.getSurfaceContainer());


		if (currentPage != null) {
			currentPage.draw(mouseX, mouseY);
		}
		
		for (Component c : components) {
			c.draw(mouseX, mouseY);
		}
	}

	@Override
	public List<Page> createPages() {

		List<Page> pages = new ArrayList<>();

		pages.add(new HomePage(this));
		pages.add(new ModsPage(this));
		pages.add(new MusicPage(this));
		pages.add(new ProfilePage(this));
		pages.add(new SettingsPage(this));

		return pages;
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
