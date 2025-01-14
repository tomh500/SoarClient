package com.soarclient.gui.mainmenu.gui.account.pages;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.soarclient.Soar;
import com.soarclient.animation.SimpleAnimation;
import com.soarclient.gui.api.page.Page;
import com.soarclient.gui.api.page.PageGui;
import com.soarclient.gui.api.page.impl.LeftTransition;
import com.soarclient.management.account.Account;
import com.soarclient.management.account.AccountAuth;
import com.soarclient.management.account.AccountManager;
import com.soarclient.management.account.impl.BedrockAccount;
import com.soarclient.management.color.api.ColorPalette;
import com.soarclient.skia.Skia;
import com.soarclient.skia.font.Fonts;
import com.soarclient.skia.font.Icon;
import com.soarclient.ui.component.api.Size;
import com.soarclient.ui.component.api.Style;
import com.soarclient.ui.component.handler.impl.ButtonHandler;
import com.soarclient.ui.component.impl.IconButton;
import com.soarclient.ui.component.impl.text.SearchBar;
import com.soarclient.utils.SearchUtils;
import com.soarclient.utils.file.FileLocation;
import com.soarclient.utils.mouse.ScrollHelper;

public class AccountListPage extends Page {

	private List<Item> items = new ArrayList<>();
	private ScrollHelper scrollHelper = new ScrollHelper();
	private SearchBar searchBar;
	private IconButton addButton;
	private int lastSize;

	public AccountListPage(PageGui parent) {
		super(parent, "", "", new LeftTransition(true));

		List<Account> accounts = Soar.getInstance().getAccountManager().getAccounts();

		for (Account acc : accounts) {
			items.add(new Item(acc));
		}

		lastSize = accounts.size();
	}

	public void init() {

		searchBar = new SearchBar(x + width - 260 - 23, y + 23, 260, () -> {
			scrollHelper.reset();
		});

		addButton = new IconButton(Icon.ADD, 0, 0, Size.LARGE, Style.TERTIARY);
		addButton.setHandler(new ButtonHandler() {

			@Override
			public void onAction() {
				AccountAuth.handleMicrosoftLogin();
			}
		});

		addButton.setX(x + width - addButton.getWidth() - 23);
		addButton.setY(y + height - addButton.getHeight() - 23);

		for (Item i : items) {
			i.xAnimation.setFirstTick(true);
			i.yAnimation.setFirstTick(true);
		}
	}

	public void draw(int mouseX, int mouseY) {

		Soar instance = Soar.getInstance();
		ColorPalette palette = instance.getColorManager().getPalette();
		AccountManager accountManager = instance.getAccountManager();

		if (accountManager.getAccounts().size() != lastSize) {
			lastSize = accountManager.getAccounts().size();
			items.clear();
			for (Account acc : accountManager.getAccounts()) {
				items.add(new Item(acc));
			}
		}

		float offsetX = 23;
		float offsetY = 88;

		scrollHelper.onScroll();
		mouseY = (int) (mouseY - scrollHelper.getValue());

		Skia.save();
		Skia.translate(0, scrollHelper.getValue());

		for (Item i : items) {

			Account acc = i.account;
			SimpleAnimation xAnimation = i.xAnimation;
			SimpleAnimation yAnimation = i.yAnimation;

			float itemX = x + offsetX;
			float itemY = y + offsetY;

			if (!searchBar.getText().isEmpty() && !SearchUtils.isSimilar(acc.getName(), searchBar.getText())) {
				continue;
			}

			xAnimation.onTick(itemX, 14);
			yAnimation.onTick(itemY, 14);

			itemX = xAnimation.getValue();
			itemY = yAnimation.getValue();

			String uuid = acc instanceof BedrockAccount ? ((BedrockAccount) acc).getMcChain().getXuid()
					: acc.getUUID().toString().replace("-", "");

			Skia.drawRoundedRect(itemX, itemY, width - (23 * 2), 88, 20, palette.getSurface());
			Skia.drawPlayerHead(new File(FileLocation.CACHE_DIR, uuid), itemX + 12, itemY + 12, 64, 64, 12);
			Skia.drawText(acc.getDisplayString(), itemX + 88, itemY + 26, palette.getOnSurface(), Fonts.getRegular(21));
			Skia.drawText(uuid, itemX + 88, itemY + 49, palette.getOnSurface(), Fonts.getRegular(16));

			offsetY += 88 + 23;
		}

		searchBar.draw(mouseX, mouseY);

		Skia.restore();

		mouseY = (int) (mouseY + scrollHelper.getValue());
		addButton.draw(mouseX, mouseY);
	}

	public void mousePressed(int mouseX, int mouseY, int mouseButton) {

		searchBar.mousePressed(mouseX, mouseY, mouseButton);

		addButton.mousePressed(mouseX, mouseY, mouseButton);
	}

	public void mouseReleased(int mouseX, int mouseY, int mouseButton) {

		searchBar.mouseReleased(mouseX, mouseY, mouseButton);

		addButton.mouseReleased(mouseX, mouseY, mouseButton);
	}

	@Override
	public void keyTyped(char typedChar, int keyCode) {
		searchBar.keyTyped(typedChar, keyCode);
		super.keyTyped(typedChar, keyCode);
	}

	public void onClosed() {
	}

	private class Item {

		private Account account;
		private SimpleAnimation xAnimation = new SimpleAnimation();
		private SimpleAnimation yAnimation = new SimpleAnimation();

		private Item(Account account) {
			this.account = account;
		}
	}
}
