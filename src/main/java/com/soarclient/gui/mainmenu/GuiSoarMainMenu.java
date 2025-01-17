package com.soarclient.gui.mainmenu;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.soarclient.Soar;
import com.soarclient.gui.api.SimpleSoarGui;
import com.soarclient.gui.api.SoarGui;
import com.soarclient.gui.mainmenu.gui.MainGui;
import com.soarclient.gui.mainmenu.gui.account.AccountGui;
import com.soarclient.management.account.Account;
import com.soarclient.management.account.AccountManager;
import com.soarclient.management.color.api.ColorPalette;
import com.soarclient.skia.Skia;
import com.soarclient.skia.font.Fonts;
import com.soarclient.utils.file.FileLocation;

import io.github.humbleui.types.Rect;

public class GuiSoarMainMenu extends SimpleSoarGui {

	private final List<SoarGui> guis = new ArrayList<>();
	private SoarGui currentGui;

	public GuiSoarMainMenu() {
		super(false);

		guis.add(new MainGui(this));
		guis.add(new AccountGui(this));
		currentGui = guis.get(0);
	}

	@Override
	public void init() {
		if (currentGui != null) {
			currentGui.init();
		}
	}

	@Override
	public void draw(int mouseX, int mouseY) {

		Soar instance = Soar.getInstance();
		AccountManager accountManager = instance.getAccountManager();
		Account currentAccount = accountManager.getCurrentAccount();
		ColorPalette palette = instance.getColorManager().getPalette();

		Skia.drawGradientRoundedRect(0, 0, mc.displayWidth, mc.displayHeight, 0, palette.getPrimaryContainer(),
				palette.getTertiaryContainer());

		if (currentAccount != null) {

			String name = currentAccount.getName();
			Rect bounds = Skia.getTextBounds(name, Fonts.getRegular(16));

			Skia.drawRoundedRect(10, 10, 38 + bounds.getWidth() + 6, 38, 12, palette.getSurface());
			Skia.drawPlayerHead(new File(FileLocation.CACHE_DIR, currentAccount.getUUID().toString().replace("-", "")),
					16, 16, 26, 26, 6);
			Skia.drawHeightCenteredText(name, 48, 10 + 38 / 2, palette.getOnSurface(), Fonts.getRegular(16));
		}

		if (currentGui != null) {
			currentGui.draw(mouseX, mouseY);
		}
	}

	@Override
	public void mousePressed(int mouseX, int mouseY, int mouseButton) {

		if (currentGui != null) {
			currentGui.mousePressed(mouseX, mouseY, mouseButton);
		}
	}

	@Override
	public void mouseReleased(int mouseX, int mouseY, int mouseButton) {

		if (currentGui != null) {
			currentGui.mouseReleased(mouseX, mouseY, mouseButton);
		}
	}

	@Override
	public void keyTyped(char typedChar, int keyCode) {

		if (currentGui != null) {
			currentGui.keyTyped(typedChar, keyCode);
		}
	}

	@Override
	public void onClosed() {
	}

	public void setCurrentGui(Class<? extends SoarGui> clazz) {

		SoarGui g = getGui(clazz);

		if (currentGui != null) {
			currentGui.close(Soar.getInstance().getMainMenu());
			currentGui.onClosed();
		}

		currentGui = g;

		if (currentGui != null) {
			currentGui.init();
		}
	}

	public SoarGui getGui(Class<? extends SoarGui> clazz) {

		if (clazz == null) {
			return null;
		}

		for (SoarGui g : guis) {
			if (g.getClass().equals(clazz)) {
				return g;
			}
		}

		return null;
	}
}
