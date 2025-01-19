package com.soarclient.gui.modmenu.pages;

import org.lwjgl.input.Keyboard;

import com.soarclient.gui.api.page.Page;
import com.soarclient.gui.api.page.PageGui;
import com.soarclient.gui.api.page.impl.RightTransition;
import com.soarclient.skia.Skia;
import com.soarclient.skia.font.Icon;
import com.soarclient.ui.component.impl.text.SearchBar;
import com.soarclient.utils.mouse.ScrollHelper;

public class SettingsImplPage extends Page {

	private Class<? extends Page> prevPage;
	private ScrollHelper scrollHelper = new ScrollHelper();
	private SearchBar searchBar;
	
	public SettingsImplPage(PageGui parent, Class<? extends Page> prevPage) {
		super(parent, "text.mods", Icon.SETTINGS, new RightTransition(true));
		this.prevPage = prevPage;
	}
	
	@Override
	public void init() {

		searchBar = new SearchBar(x + width - 260 - 32, y + 32, 260, () -> {
			scrollHelper.reset();
		});
		
		parent.setClosable(false);
	}
	
	@Override
	public void draw(int mouseX, int mouseY) {
		
		scrollHelper.onScroll();
		mouseY = (int) (mouseY - scrollHelper.getValue());

		Skia.save();
		Skia.translate(0, scrollHelper.getValue());

		searchBar.draw(mouseX, mouseY);
		
		Skia.restore();
	}
	
	@Override
	public void mousePressed(int mouseX, int mouseY, int mouseButton) {
		
		mouseY = (int) (mouseY - scrollHelper.getValue());

		searchBar.mousePressed(mouseX, mouseY, mouseButton);
	}
	
	@Override
	public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
		
		mouseY = (int) (mouseY - scrollHelper.getValue());

		searchBar.mousePressed(mouseX, mouseY, mouseButton);
	}
	
	@Override
	public void keyTyped(char typedChar, int keyCode) {
		
		searchBar.keyTyped(typedChar, keyCode);
		
		if (keyCode == Keyboard.KEY_ESCAPE) {
			parent.setClosable(true);
			parent.setCurrentPage(prevPage);
		}
	}
	
	@Override
	public void onClosed() {
		if(!parent.isClosable()) {
			parent.setClosable(true);
			parent.getPage(prevPage).onClosed();
		}
	}
}
