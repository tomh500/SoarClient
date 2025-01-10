package com.soarclient.gui.modmenu;

import java.util.ArrayList;
import java.util.List;

import com.soarclient.gui.api.page.Page;
import com.soarclient.gui.api.page.PageGui;
import com.soarclient.gui.api.page.impl.ZoomOutInTransition;
import com.soarclient.gui.modmenu.pages.HomePage;
import com.soarclient.gui.modmenu.pages.ModsPage;
import com.soarclient.gui.modmenu.pages.MusicPage;
import com.soarclient.gui.modmenu.pages.ProfilePage;
import com.soarclient.gui.modmenu.pages.SettingsPage;

public class GuiModMenu extends PageGui {

	private NavigationRail navigationRail;
	
	public GuiModMenu() {
		super(new ZoomOutInTransition(false), true, true);
	}
	
	@Override
	public void init() {
		components.clear();
		navigationRail = new NavigationRail(this, getX(), getY(), 90, getHeight());
		components.add(navigationRail);
		super.init();
	}
	
	@Override
	public void setPageSize() {
		for (Page p : pages) {
			p.setX(getX() + navigationRail.getWidth());
			p.setY(getY());
			p.setWidth(getWidth() - navigationRail.getWidth());
			p.setHeight(getHeight());
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
