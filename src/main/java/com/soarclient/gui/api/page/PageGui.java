package com.soarclient.gui.api.page;

import java.util.ArrayList;
import java.util.List;

import com.soarclient.animation.Animation;
import com.soarclient.gui.api.GuiTransition;
import com.soarclient.gui.api.SoarGui;
import com.soarclient.ui.component.Component;

public abstract class PageGui extends SoarGui {

	protected final List<Component> components = new ArrayList<>();
	protected List<Page> pages;

	private Animation pageAnimation;

	protected Page currentPage;
	protected Page lastPage;

	public PageGui(GuiTransition transition, boolean background, boolean blur) {
		super(transition, background, blur);
		this.pages = createPages();

		if (!pages.isEmpty()) {
			this.currentPage = pages.getFirst();
		}
	}

	@Override
	public void init() {

		for (Page p : pages) {
			p.setX(getX());
			p.setY(getY());
			p.setWidth(getWidth());
			p.setHeight(getHeight());
		}
		
		super.init();
	}
	
	@Override
	public void drawSkia(int mouseX, int mouseY) {
		for (Component c : components) {
			c.draw(mouseX, mouseY);
		}
	}

	@Override
	public void mousePressed(int mouseX, int mouseY, int mouseButton) {

		if (currentPage != null) {
			currentPage.mousePressed(mouseX, mouseY, mouseButton);
		}

		for (Component c : components) {
			c.mousePressed(mouseX, mouseY, mouseButton);
		}
		
		super.mousePressed(mouseX, mouseY, mouseButton);
	}

	@Override
	public void mouseReleased(int mouseX, int mouseY, int mouseButton) {

		if (currentPage != null) {
			currentPage.mouseReleased(mouseX, mouseY, mouseButton);
		}

		for (Component c : components) {
			c.mouseReleased(mouseX, mouseY, mouseButton);
		}
		
		super.mouseReleased(mouseX, mouseY, mouseButton);
	}

	@Override
	public void keyTyped(char typedChar, int keyCode) {

		if (currentPage != null) {
			currentPage.keyTyped(typedChar, keyCode);
		}

		for (Component c : components) {
			c.keyTyped(typedChar, keyCode);
		}
		
		super.keyTyped(typedChar, keyCode);
	}

	@Override
	public void onClosed() {
		for (Page p : pages) {
			p.onClosed();
		}
	}

	public Page getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Page page) {

		if (currentPage != null) {
			lastPage = currentPage;
			currentPage.onClosed();
		}

		this.currentPage = page;

		if (currentPage != null) {
			currentPage.init();
		}
	}

	public void setCurrentPage(Class<? extends Page> clazz) {

		Page page = null;

		for (Page p : pages) {
			if (p.getClass().equals(clazz)) {
				page = p;
				break;
			}
		}

		if (page != null) {
			setCurrentPage(page);
		}
	}

	public abstract List<Page> createPages();

	public List<Page> getPages() {
		return pages;
	}
}
