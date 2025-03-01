package com.soarclient.gui.modmenu.pages;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.soarclient.Soar;
import com.soarclient.animation.SimpleAnimation;
import com.soarclient.gui.api.SoarGui;
import com.soarclient.gui.api.page.Page;
import com.soarclient.gui.api.page.impl.RightLeftTransition;
import com.soarclient.management.profile.Profile;
import com.soarclient.management.profile.ProfileIcon;
import com.soarclient.skia.Skia;
import com.soarclient.skia.font.Icon;

public class ProfilePage extends Page {

	private final List<Item> items = new ArrayList<>();

	public ProfilePage(SoarGui parent) {
		super(parent, "text.profile", Icon.DESCRIPTION, new RightLeftTransition(true));

		for(Profile p : Soar.getInstance().getProfileManager().getProfiles()) {
			items.add(new Item(p));
		}
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

		mouseY = mouseY - scrollHelper.getValue();

		Skia.save();
		Skia.translate(0, scrollHelper.getValue());

		for(Item i : items) {

			Profile p = i.profile;
			SimpleAnimation xAnimation = i.xAnimation;
			SimpleAnimation yAnimation = i.yAnimation;
			Object icon = p.getIcon();

			if(icon instanceof ProfileIcon) {

			} else if(icon instanceof File) {

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
