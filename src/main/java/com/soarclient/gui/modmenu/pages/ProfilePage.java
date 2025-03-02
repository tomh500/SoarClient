package com.soarclient.gui.modmenu.pages;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.soarclient.Soar;
import com.soarclient.animation.SimpleAnimation;
import com.soarclient.gui.api.SoarGui;
import com.soarclient.gui.api.page.Page;
import com.soarclient.gui.api.page.impl.RightLeftTransition;
import com.soarclient.gui.modmenu.pages.profile.ProfileAddPage;
import com.soarclient.management.color.api.ColorPalette;
import com.soarclient.management.profile.Profile;
import com.soarclient.management.profile.ProfileIcon;
import com.soarclient.skia.Skia;
import com.soarclient.skia.font.Icon;
import com.soarclient.ui.component.handler.impl.ButtonHandler;
import com.soarclient.ui.component.impl.IconButton;
import com.soarclient.utils.SearchUtils;

public class ProfilePage extends Page {

	private final List<Item> items = new ArrayList<>();
	private final IconButton addButton;

	public ProfilePage(SoarGui parent) {
		super(parent, "text.profile", Icon.DESCRIPTION, new RightLeftTransition(true));

		for(Profile p : Soar.getInstance().getProfileManager().getProfiles()) {
			items.add(new Item(p));
		}

		addButton = new IconButton(Icon.ADD, 0, 0, IconButton.Size.NORMAL, IconButton.Style.PRIMARY);
		addButton.setHandler(new ButtonHandler() {
			@Override
			public void onAction() {
				parent.setCurrentPage(new ProfileAddPage(parent, ProfilePage.this.getClass()));
			}
		});
	}

	@Override
	public void init() {
		super.init();

		for (Item i : items) {
			i.xAnimation.setFirstTick(true);
			i.yAnimation.setFirstTick(true);
		}
	}

	@Override
	public void draw(double mouseX, double mouseY) {
		super.draw(mouseX, mouseY);

		ColorPalette palette = Soar.getInstance().getColorManager().getPalette();

		mouseY = mouseY - scrollHelper.getValue();

		int index = 0;
		float offsetX = 26;
		float offsetY = 0;

		Skia.save();
		Skia.translate(0, scrollHelper.getValue());

		for(Item i : items) {

			Profile p = i.profile;
			SimpleAnimation xAnimation = i.xAnimation;
			SimpleAnimation yAnimation = i.yAnimation;
			Object icon = p.getIcon();

			float itemX = x + offsetX;
			float itemY = y + 96 + offsetY;

			if (!searchBar.getText().isEmpty() && !SearchUtils.isSimilar(p.getName() + " " + p.getAuthor(), searchBar.getText())) {
				continue;
			}

			xAnimation.onTick(itemX, 14);
			yAnimation.onTick(itemY, 14);

			itemX = xAnimation.getValue();
			itemY = yAnimation.getValue();
			
			Skia.drawRoundedRect(itemX, itemY, 100, 200, 12, palette.getSurface());

			if(icon instanceof ProfileIcon) {

			} else if(icon instanceof File) {

			}

			index++;
			offsetX += 32 + 100;

			if (index % 3 == 0) {
				offsetX = 26;
				offsetY += 22 + 151;
			}
		}

		Skia.restore();
	}

	@Override
	public void mousePressed(double mouseX, double mouseY, int button) {

	}

	@Override
	public void mouseReleased(double mouseX, double mouseY, int button) {

	}

	private class Item {

		private final Profile profile;
		private final SimpleAnimation xAnimation = new SimpleAnimation();
		private final SimpleAnimation yAnimation = new SimpleAnimation();

		private Item(Profile profile) {
			this.profile = profile;
		}
	}
}
