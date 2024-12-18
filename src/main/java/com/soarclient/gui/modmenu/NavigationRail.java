package com.soarclient.gui.modmenu;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import com.soarclient.Soar;
import com.soarclient.animation.Animation;
import com.soarclient.animation.Duration;
import com.soarclient.animation.cubicbezier.impl.EaseStandard;
import com.soarclient.animation.other.DummyAnimation;
import com.soarclient.gui.Page;
import com.soarclient.gui.component.Component;
import com.soarclient.gui.component.api.Size;
import com.soarclient.gui.component.api.Style;
import com.soarclient.gui.component.handler.impl.ButtonHandler;
import com.soarclient.gui.component.impl.buttons.IconButton;
import com.soarclient.gui.edithud.GuiEditHUD;
import com.soarclient.management.color.api.ColorPalette;
import com.soarclient.nanovg.NanoVGHelper;
import com.soarclient.nanovg.font.Font;
import com.soarclient.nanovg.font.Fonts;
import com.soarclient.nanovg.font.Icon;
import com.soarclient.utils.ColorUtils;
import com.soarclient.utils.language.I18n;
import com.soarclient.utils.mouse.MouseUtils;

public class NavigationRail extends Component {

	private List<Navigation> navigations = new ArrayList<>();
	private GuiModMenu parent;
	private Navigation currentNavigation;
	private IconButton editButton;

	public NavigationRail(GuiModMenu parent, float x, float y, float width, float height) {
		super(x, y);
		this.parent = parent;
		this.width = width;
		this.height = height;

		for (Page p : parent.getPages()) {

			Navigation n = new Navigation(p);

			if (p.equals(parent.getCurrentPage())) {
				currentNavigation = n;
				n.animation = new EaseStandard(Duration.MEDIUM_3, 0, 1);
			}

			navigations.add(n);
		}

		editButton = new IconButton(Icon.EDIT, x, y + 44, Size.NORMAL, Style.TERTIARY);
		editButton.setX(x + (width / 2) - (editButton.getWidth() / 2));
		editButton.setHandler(new ButtonHandler() {
			@Override
			public void onClicked() {
				parent.close(new GuiEditHUD(mc.currentScreen).create());
			}
		});
	}

	@Override
	public void draw(int mouseX, int mouseY) {

		NanoVGHelper nvg = NanoVGHelper.getInstance();
		ColorPalette palette = Soar.getInstance().getColorManager().getPalette();

		nvg.drawRoundedRectVarying(x, y, width, height, 45, 0, 45, 0, palette.getSurface());
		editButton.draw(mouseX, mouseY);

		float offsetY = 140;

		for (Navigation n : navigations) {

			Page p = n.page;
			String title = p.getTitle();
			String icon = p.getIcon();
			Font font = currentNavigation.equals(n) ? Fonts.ICON_FILL : Fonts.ICON;
			Color c0 = currentNavigation.equals(n) ? palette.getOnSecondaryContainer() : palette.getOnSurfaceVariant();
			Color c1 = currentNavigation.equals(n) ? palette.getOnSurface() : palette.getOnSurfaceVariant();

			Animation animation = n.animation;

			if (animation.getEnd() != 0 || !animation.isFinished()) {
				nvg.drawRoundedRect(x + (width / 2) - (56 / 2) + (56 - 56 * animation.getValue()) / 2, y + offsetY,
						56 * animation.getValue(), 32, 16,
						ColorUtils.applyAlpha(palette.getSecondaryContainer(), animation.getValue()));
			}

			nvg.drawAlignCenteredText(icon, x + (width / 2) - (56 / 2) + (56 / 2), y + offsetY + (32 / 2), c0, 24,
					font);
			nvg.drawCenteredText(I18n.get(title), x + (width / 2), y + offsetY + 36, c1, 12, Fonts.MEDIUM);

			offsetY += 68;
		}
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {

		editButton.mouseClicked(mouseX, mouseY, mouseButton);

		float offsetY = 140;

		for (Navigation n : navigations) {
			if (MouseUtils.isInside(mouseX, mouseY, x + (width / 2) - (56 / 2), y + offsetY, 56, 32) && mouseButton == 0
					&& !currentNavigation.equals(n)) {

				currentNavigation.animation = new EaseStandard(Duration.MEDIUM_3, 1, 0);
				currentNavigation = n;
				parent.setPage(n.page);
				currentNavigation.animation = new EaseStandard(Duration.MEDIUM_3, 0, 1);
			}

			offsetY += 68;
		}
	}

	private class Navigation {

		private Page page;
		private Animation animation;

		private Navigation(Page page) {
			this.page = page;
			this.animation = new DummyAnimation();
		}
	}
}