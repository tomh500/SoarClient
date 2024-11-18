package com.soarclient.gui.modmenu.pages.impl;

import java.util.ArrayList;
import java.util.List;

import com.soarclient.Soar;
import com.soarclient.animation.Animation;
import com.soarclient.animation.Duration;
import com.soarclient.animation.cubicbezier.impl.EaseStandard;
import com.soarclient.animation.other.DummyAnimation;
import com.soarclient.gui.Page;
import com.soarclient.gui.PageDirection;
import com.soarclient.gui.modmenu.GuiModMenu;
import com.soarclient.management.color.api.ColorPalette;
import com.soarclient.management.mods.Mod;
import com.soarclient.nanovg.NanoVGHelper;
import com.soarclient.nanovg.font.Fonts;
import com.soarclient.nanovg.font.Icon;
import com.soarclient.utils.ColorUtils;
import com.soarclient.utils.language.I18n;
import com.soarclient.utils.mouse.MouseUtils;
import com.soarclient.utils.mouse.ScrollHelper;
import com.soarclient.utils.tuples.Pair;

public class ModsPage extends Page {

	private List<Pair<Mod, Animation>> items = new ArrayList<>();
	private ScrollHelper scrollHelper = new ScrollHelper();
	private GuiModMenu parent;

	public ModsPage(GuiModMenu parent, float x, float y, float width, float height) {
		super(PageDirection.LEFT, "text.mods", Icon.INVENTORY_2, x, y, width, height);
		this.parent = parent;

		for (Mod m : Soar.getInstance().getModManager().getMods()) {
			items.add(Pair.of(m, new DummyAnimation(0, m.isEnabled() ? 1 : 0)));
		}
	}

	@Override
	public void draw(int mouseX, int mouseY) {

		NanoVGHelper nvg = NanoVGHelper.getInstance();
		ColorPalette palette = Soar.getInstance().getColorManager().getPalette();

		int index = 0;
		float offsetX = 32;
		float offsetY = 0;

		scrollHelper.onScroll();

		nvg.save();
		nvg.translate(0, scrollHelper.getValue());

		for (Pair<Mod, Animation> i : items) {

			Mod m = i.getFirst();
			Animation a = i.getSecond();

			if (m.isHidden()) {
				continue;
			}

			float itemX = x + offsetX;
			float itemY = y + 110 + offsetY;

			if (itemY + 151 < y - scrollHelper.getValue() || itemY > y + height - scrollHelper.getValue()) {
				index++;
				offsetX += 32 + 244;
				if (index % 3 == 0) {
					offsetX = 32;
					offsetY += 22 + 151;
				}
				continue;
			}

			if ((a.getValue() == 0 && m.isEnabled()) || (a.getValue() == 1 && !m.isEnabled())) {
				i.setSecond(new EaseStandard(Duration.MEDIUM_2, i.getSecond().getValue(), m.isEnabled() ? 1 : 0));
			}

			nvg.drawRoundedRectVarying(itemX, itemY, 244, 116, 26, 26, 0, 0, palette.getSurface());
			nvg.drawRoundedRectVarying(itemX, itemY + 116, 244, 35, 0, 0, 26, 26, palette.getSurfaceContainerHigh());
			nvg.drawRoundedRectVarying(itemX, itemY + 116, 244, 35, 0, 0, 26, 26,
					ColorUtils.applyAlpha(palette.getPrimaryContainer(), a.getValue()));
			nvg.drawAlignCenteredText(I18n.get(m.getName()), itemX + (244 / 2), itemY + 116 + (35 / 2),
					palette.getOnSurfaceVariant(), 16, Fonts.REGULAR);
			nvg.drawAlignCenteredText(m.getIcon(), itemX + (244 / 2), itemY + (116 / 2), palette.getOnSurfaceVariant(),
					68, Fonts.ICON);

			index++;
			offsetX += 32 + 244;

			if (index % 3 == 0) {
				offsetX = 32;
				offsetY += 22 + 151;
			}
		}

		nvg.restore();
		
		scrollHelper.setMaxScroll(110 + 26, items.size(), height, 151, 22, 3);
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {

		int index = 0;
		float offsetX = 32;
		float offsetY = 0;

		mouseY = (int) (mouseY - scrollHelper.getValue());

		if (mouseX < x || mouseX > x + width || mouseY + scrollHelper.getValue() < y
				|| mouseY + scrollHelper.getValue() > y + height) {
			return;
		}

		for (Pair<Mod, Animation> i : items) {

			Mod m = i.getFirst();

			if (m.isHidden()) {
				continue;
			}

			float itemX = x + offsetX;
			float itemY = y + 110 + offsetY;

			if (itemY + 151 < y - scrollHelper.getValue() || itemY > y + height - scrollHelper.getValue()) {
				index++;
				offsetX += 32 + 244;
				if (index % 3 == 0) {
					offsetX = 32;
					offsetY += 22 + 151;
				}
				continue;
			}

			if (mouseButton == 0) {
				
				if (MouseUtils.isInside(mouseX, mouseY, itemX, itemY + 116, 244, 35)) {
					m.toggle();
				}
			}

			index++;
			offsetX += 32 + 244;

			if (index % 3 == 0) {
				offsetX = 32;
				offsetY += 22 + 151;
			}
		}
	}
	
	@Override
	public void keyTyped(char typedChar, int keyCode) {
		
	}
}