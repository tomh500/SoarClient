package com.soarclient.gui.api.page;

import java.util.ArrayList;
import java.util.List;

import com.soarclient.Soar;
import com.soarclient.gui.api.SoarGui;
import com.soarclient.management.color.api.ColorPalette;
import com.soarclient.skia.Skia;
import com.soarclient.ui.component.Component;

public abstract class PageGui extends SoarGui {

	protected List<Component> components = new ArrayList<>();
	
	protected List<Page> pages;
	protected Page currentPage;
	
	public PageGui(List<Page> pages) {
		this.pages = pages;
		
		if(!pages.isEmpty()) {
			this.currentPage = pages.getFirst();
		}
	}
	
	@Override
	public void init() {
		for(Page p : pages) {
			p.setX(getX());
			p.setY(getY());
			p.setWidth(getWidth());
			p.setHeight(getHeight());
		}
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
	public void mousePressed(int mouseX, int mouseY, int mouseButton) {
		
		if(currentPage != null) {
			currentPage.mousePressed(mouseX, mouseY, mouseButton);
		}
		
		for(Component c : components) {
			c.mousePressed(mouseX, mouseY, mouseButton);
		}
	}

	@Override
	public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
		
		if(currentPage != null) {
			currentPage.mouseReleased(mouseX, mouseY, mouseButton);
		}
		
		for(Component c : components) {
			c.mouseReleased(mouseX, mouseY, mouseButton);
		}
	}

	@Override
	public void keyTyped(char typedChar, int keyCode) {
		
		if(currentPage != null) {
			currentPage.keyTyped(typedChar, keyCode);
		}
		
		for(Component c : components) {
			c.keyTyped(typedChar, keyCode);
		}
	}

	@Override
	public void onClosed() {
		
	}
	
	public void setCurrentPage(Page page) {
		this.currentPage = page;
	}
	
	public void setCurrentPage(Class<? extends Page> clazz) {
		
		Page page = null;
		
		for(Page p : pages) {
			if(p.getClass().equals(clazz)) {
				page = p;
				break;
			}
		}
		
		if(page != null) {
			setCurrentPage(page);
		}
	}
	
	public abstract float getX();
	public abstract float getY();
	public abstract float getWidth();
	public abstract float getHeight();
}
