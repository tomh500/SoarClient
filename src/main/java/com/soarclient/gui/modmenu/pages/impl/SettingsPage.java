package com.soarclient.gui.modmenu.pages.impl;

import java.util.ArrayList;
import java.util.List;

import com.soarclient.Soar;
import com.soarclient.gui.Page;
import com.soarclient.gui.PageDirection;
import com.soarclient.gui.component.impl.text.SearchBar;
import com.soarclient.gui.modmenu.GuiModMenu;
import com.soarclient.management.color.api.ColorPalette;
import com.soarclient.management.mods.Mod;
import com.soarclient.nanovg.NanoVGHelper;
import com.soarclient.nanovg.font.Fonts;
import com.soarclient.nanovg.font.Icon;
import com.soarclient.utils.language.I18n;
import com.soarclient.utils.mouse.MouseUtils;
import com.soarclient.utils.mouse.ScrollHelper;

public class SettingsPage extends Page {

	private GuiModMenu parent;

	private List<Mod> mods = new ArrayList<>();
	private ScrollHelper scrollHelper = new ScrollHelper();
	private SearchBar searchBar;

	public SettingsPage(GuiModMenu parent, float x, float y, float width, float height) {
		super(PageDirection.LEFT, "text.settings", Icon.SETTINGS, x, y, width, height);

		this.parent = parent;

		searchBar = new SearchBar(x + width - 260 - 32, y + 32, 260, () -> {
			scrollHelper.reset();
		});

		for (Mod m : Soar.getInstance().getModManager().getMods()) {
			if (m.isHidden()) {
				mods.add(m);
			}
		}
	}

	@Override
	public void draw(int mouseX, int mouseY) {

		float barWidth = width - 64;
		float barHeight = 68;
		float offsetX = 32;
		float offsetY = 96;

		searchBar.draw(mouseX, mouseY);

		for (Mod m : mods) {

			NanoVGHelper nvg = NanoVGHelper.getInstance();
			ColorPalette palette = Soar.getInstance().getColorManager().getPalette();

			nvg.drawRoundedRect(x + offsetX, y + offsetY, barWidth, barHeight, 18, palette.getSurface());
			nvg.drawText(m.getIcon(), x + 20 + offsetX, y + 21 + offsetY, palette.getOnSurface(), 32, Fonts.ICON);
			nvg.drawText(I18n.get(m.getName()), x + 58 + offsetX, y + 19 + offsetY, palette.getOnSurface(), 18,
					Fonts.REGULAR);
			nvg.drawText(I18n.get(m.getDescription()), x + 58 + offsetX, y + 37 + offsetY,
					palette.getOnSurfaceVariant(), 14, Fonts.REGULAR);
			nvg.drawText(">", x + offsetX + barWidth - 30, y + 24 + offsetY, palette.getOnSurface(), 20, Fonts.REGULAR);

			offsetY += barHeight + 22;
		}
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {

		float barWidth = width - 64;
		float barHeight = 68;
		float offsetX = 32;
		float offsetY = 96;

		searchBar.mouseClicked(mouseX, mouseY, mouseButton);
		
		for (Mod m : mods) {

			if (MouseUtils.isInside(mouseX, mouseY, x + offsetX + barWidth - 60, y + offsetY, 60, barHeight)
					&& !Soar.getInstance().getModManager().getSettingsByMod(m).isEmpty()) {
				parent.setPage(new SettingsImplPage(parent, this.getClass(), x, y, width, height, m));
			}

			offsetY += barHeight + 22;
		}
	}

	@Override
	public void keyTyped(char typedChar, int keyCode) {
		searchBar.keyTyped(typedChar, keyCode);
	}
}
