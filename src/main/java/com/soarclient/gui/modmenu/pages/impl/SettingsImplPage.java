package com.soarclient.gui.modmenu.pages.impl;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;

import com.soarclient.Soar;
import com.soarclient.gui.Page;
import com.soarclient.gui.PageDirection;
import com.soarclient.gui.component.impl.settings.SettingBar;
import com.soarclient.gui.component.impl.text.SearchBar;
import com.soarclient.gui.modmenu.GuiModMenu;
import com.soarclient.management.mods.Mod;
import com.soarclient.management.mods.settings.Setting;
import com.soarclient.nanovg.font.Icon;
import com.soarclient.utils.SearchUtils;
import com.soarclient.utils.language.I18n;
import com.soarclient.utils.mouse.ScrollHelper;

public class SettingsImplPage extends Page {

	private List<SettingBar> bars = new ArrayList<>();
	private SearchBar searchBar;
	private ScrollHelper scrollHelper = new ScrollHelper();
	private GuiModMenu parent;
	private Class<? extends Page> prevPage;

	public SettingsImplPage(GuiModMenu parent, Class<? extends Page> prevPage, float x, float y, float width,
			float height, Mod mod) {
		super(PageDirection.RIGHT, "text.mods", Icon.SETTINGS, x, y, width, height);

		this.prevPage = prevPage;
		this.parent = parent;
		this.parent.setCloseable(false);

		searchBar = new SearchBar(x + width - 260 - 32, y + 32, 260, () -> {
			scrollHelper.reset();
		});

		for (Setting s : Soar.getInstance().getModManager().getSettingsByMod(mod)) {
			SettingBar bar = new SettingBar(s, x + 32, y + 32, width - 64);
			bars.add(bar);
		}
	}

	@Override
	public void draw(int mouseX, int mouseY) {

		float offsetY = 96;

		searchBar.draw(mouseX, mouseY);

		for (SettingBar b : bars) {

			if (!searchBar.getText().isEmpty()
					&& !SearchUtils.isSimillar(I18n.get(b.getTitle()), searchBar.getText())) {
				continue;
			}

			b.setY(y + offsetY);
			b.draw(mouseX, mouseY);

			offsetY += b.getHeight() + 22;
		}
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		searchBar.mouseClicked(mouseX, mouseY, mouseButton);

		for (SettingBar b : bars) {
			b.mouseClicked(mouseX, mouseY, mouseButton);
		}
	}

	@Override
	public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
		for (SettingBar b : bars) {
			b.mouseReleased(mouseX, mouseY, mouseButton);
		}
	}

	@Override
	public void keyTyped(char typedChar, int keyCode) {

		searchBar.keyTyped(typedChar, keyCode);

		for (SettingBar b : bars) {
			b.keyTyped(typedChar, keyCode);
		}

		if (keyCode == Keyboard.KEY_ESCAPE) {
			parent.setPage(prevPage);
		}
	}

	@Override
	public void onClosed() {
		parent.setCloseable(true);
	}
}
