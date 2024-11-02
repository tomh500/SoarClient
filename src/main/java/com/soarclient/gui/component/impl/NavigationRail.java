package com.soarclient.gui.component.impl;

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
import com.soarclient.management.color.api.ColorPalette;
import com.soarclient.nanovg.NanoVGHelper;
import com.soarclient.nanovg.font.Font;
import com.soarclient.nanovg.font.Fonts;
import com.soarclient.nanovg.font.Icon;
import com.soarclient.utils.ColorUtils;
import com.soarclient.utils.language.I18n;
import com.soarclient.utils.mouse.MouseUtils;
import com.soarclient.utils.tuples.Pair;

public class NavigationRail extends Component {

	private static int INDEX = -1;
	private List<Pair<Page, Animation>> pages = new ArrayList<>();
	private Pair<Page, Animation> currentPage;
	private IconButton editButton;
	private boolean moveEdit;

	public NavigationRail(float x, float y, float width, float height) {
		super(x, y);
		this.width = width;
		this.height = height;
		this.moveEdit = false;
		editButton = new IconButton(Icon.EDIT, x, y + 44, Size.NORMAL, Style.TERTIARY);
		editButton.setX(x + (width / 2) - (editButton.getWidth() / 2));
		editButton.setHandler(new ButtonHandler() {

			@Override
			public void onClicked() {
				moveEdit = true;
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

		for (Pair<Page, Animation> pair : pages) {

			Page p = pair.getFirst();
			String title = p.getTitle();
			String icon = p.getIcon();
			Font font = currentPage.equals(pair) ? Fonts.ICON_FILL : Fonts.ICON;
			Color c0 = currentPage.equals(pair) ? palette.getOnSecondaryContainer() : palette.getOnSurfaceVariant();
			Color c1 = currentPage.equals(pair) ? palette.getOnSurface() : palette.getOnSurfaceVariant();

			Animation animation = pair.getSecond();

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

		for (Pair<Page, Animation> pair : pages) {

			if (MouseUtils.isInside(mouseX, mouseY, x + (width / 2) - (56 / 2), y + offsetY, 56, 32)
					&& mouseButton == 0 && !currentPage.equals(pair)) {
				currentPage.setSecond(new EaseStandard(Duration.MEDIUM_3, 1, 0));
				currentPage = pair;
				INDEX = pages.indexOf(currentPage);
				pair.setSecond(new EaseStandard(Duration.MEDIUM_3, 0, 1));
			}

			offsetY += 68;
		}
	}
	
	public void add(Page page) {
		pages.add(Pair.of(page, new DummyAnimation()));
	}

	public List<Pair<Page, Animation>> getPages() {
		return pages;
	}

	public Page getCurrentPage() {
		return currentPage.getFirst();
	}

	public void initPage() {
		if(INDEX == -1) {
			INDEX = 0;
		}
		this.currentPage = pages.get(INDEX);
		this.currentPage.setSecond(new EaseStandard(Duration.MEDIUM_3, 0, 1));
	}

	public boolean isMoveEdit() {
		return moveEdit;
	}
}
