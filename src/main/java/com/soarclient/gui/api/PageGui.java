package com.soarclient.gui.api;

import java.util.List;

import com.soarclient.Soar;
import com.soarclient.management.color.api.ColorPalette;
import com.soarclient.skia.Skia;
import com.soarclient.skia.ui.SkiaUI;

public abstract class PageGui extends SkiaUI {

	private List<Page> pages;
	private Page currentPage;
	
	public PageGui(List<Page> pages) {
		this.pages = pages;
		this.currentPage = pages.getFirst();
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
	public void draw(float mouseX, float mouseY) {
		
		ColorPalette palette = Soar.getInstance().getColorManager().getPalette();
		
		Skia.drawRoundedRect(getX(), getY(), getWidth(), getHeight(), 35, palette.getSurfaceContainer());
		
		if(currentPage != null) {
			currentPage.draw(mouseX, mouseY);
		}
	}

	@Override
	public void mousePressed(float mouseX, float mouseY, int mouseButton) {
		if(currentPage != null) {
			currentPage.mousePressed(mouseX, mouseY, mouseButton);
		}
	}

	@Override
	public void mouseReleased(float mouseX, float mouseY, int mouseButton) {
		if(currentPage != null) {
			currentPage.mouseReleased(mouseX, mouseY, mouseButton);
		}
	}

	@Override
	public void keyTyped(char typedChar, int keyCode) {
		if(currentPage != null) {
			currentPage.keyTyped(typedChar, keyCode);
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
