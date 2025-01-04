package com.soarclient.gui.api.page;

import java.util.ArrayList;
import java.util.List;

import com.soarclient.Soar;
import com.soarclient.animation.Animation;
import com.soarclient.animation.Duration;
import com.soarclient.animation.cubicbezier.impl.EaseEmphasizedDecelerate;
import com.soarclient.animation.other.DummyAnimation;
import com.soarclient.gui.api.SoarGui;
import com.soarclient.management.color.api.ColorPalette;
import com.soarclient.skia.Skia;
import com.soarclient.ui.component.Component;

public abstract class PageGui extends SoarGui {

	protected Animation pageAnimation;
	protected List<Component> components = new ArrayList<>();
	
	protected List<Page> pages;
	
	protected Page currentPage;
	protected Page lastPage;
	
	public PageGui() {
		super(false);
		this.pages = createPages();
		
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
		
		pageAnimation = new DummyAnimation(0, 1);
	}

	@Override
	public void draw(int mouseX, int mouseY) {
		
		ColorPalette palette = Soar.getInstance().getColorManager().getPalette();
		
		Skia.drawRoundedRect(getX(), getY(), getWidth(), getHeight(), 35, palette.getSurfaceContainer());
		drawPage(mouseX, mouseY);
	}
	
	protected void drawPage(int mouseX, int mouseY) {
		
		Skia.save();
		Skia.clip(getX(), getY(), getWidth(), getHeight(), 35);
		
		if(currentPage != null && lastPage == null) {
			currentPage.draw(mouseX, mouseY);
		}
		

		if(lastPage != null) {
			
			PageTransition transition = lastPage.getTransition();
			float moveValue = pageAnimation.getEnd() * lastPage.getWidth();
			float lastValueX = 0;
			float currentValueX = 0;

			if (transition.equals(PageTransition.LEFT)) {
				lastValueX = -pageAnimation.getValue() * lastPage.getWidth();
				currentValueX = moveValue - (pageAnimation.getValue() * lastPage.getWidth());
			}

			if (transition.equals(PageTransition.RIGHT)) {
				lastValueX = pageAnimation.getValue() * lastPage.getWidth();
				currentValueX = -moveValue + (pageAnimation.getValue() * lastPage.getWidth());
			}

			Skia.save();
			Skia.translate(lastValueX, 0);
			lastPage.draw(mouseX, mouseY);
			Skia.restore();

			Skia.save();
			Skia.translate(currentValueX, 0);
			currentPage.draw(mouseX, mouseY);
			Skia.restore();

			if (pageAnimation.isFinished()) {
				lastPage = null;
				pageAnimation = new DummyAnimation(0, 0);
			}
		}
		
		for(Component c : components) {
			c.draw(mouseX, mouseY);
		}
		
		Skia.restore();
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

	public Page getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Page page) {
		
		if(currentPage != null) {
			lastPage = currentPage;
			currentPage.onClosed();
		}
		
		this.currentPage = page;
		pageAnimation = new EaseEmphasizedDecelerate(Duration.MEDIUM_4, 0, 1);
		
		if(currentPage != null) {
			currentPage.init();
		}
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
	
	public abstract List<Page> createPages();
	public abstract float getX();
	public abstract float getY();
	public abstract float getWidth();
	public abstract float getHeight();

	public List<Page> getPages() {
		return pages;
	}
}
