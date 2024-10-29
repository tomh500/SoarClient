package com.soarclient.gui.modmenu.pages;

import java.util.ArrayList;
import java.util.List;

import com.soarclient.Soar;
import com.soarclient.animation.Animation;
import com.soarclient.animation.Duration;
import com.soarclient.animation.cubicbezier.impl.EaseStandard;
import com.soarclient.animation.other.DummyAnimation;
import com.soarclient.gui.Page;
import com.soarclient.management.color.api.ColorPalette;
import com.soarclient.management.mods.Mod;
import com.soarclient.nanovg.NanoVGHelper;
import com.soarclient.nanovg.font.Fonts;
import com.soarclient.nanovg.font.Icon;
import com.soarclient.utils.ColorUtils;
import com.soarclient.utils.language.I18n;
import com.soarclient.utils.mouse.MouseUtils;
import com.soarclient.utils.tuples.Pair;

public class ModsPage extends Page {

	private List<Pair<Mod, Animation>> items = new ArrayList<>();

	public ModsPage(float x, float y, float width, float height) {
		super("text.mods", Icon.INVENTORY_2, x, y, width, height);

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

		for (Pair<Mod, Animation> i : items) {

			Mod m = i.getFirst();
			Animation a = i.getSecond();

			if ((a.getValue() == 0 && m.isEnabled()) || (a.getValue() == 1 && !m.isEnabled())) {
				i.setSecond(new EaseStandard(Duration.MEDIUM_2, i.getSecond().getValue(), m.isEnabled() ? 1 : 0));
			}

			nvg.drawRoundedRectVarying(x + offsetX, y + 110 + offsetY, 244, 116, 26, 26, 0, 0, palette.getSurface());
			nvg.drawRoundedRectVarying(x + offsetX, y + 226 + offsetY, 244, 35, 0, 0, 26, 26,
					palette.getSurfaceContainerHigh());
			nvg.drawRoundedRectVarying(x + offsetX, y + 226 + offsetY, 244, 35, 0, 0, 26, 26,
					ColorUtils.applyAlpha(palette.getPrimaryContainer(), a.getValue()));
			nvg.drawAlignCenteredText(I18n.get(m.getName()), x + offsetX + (244 / 2), y + 226 + (35 / 2) + offsetY,
					palette.getOnSurfaceVariant(), 16, Fonts.REGULAR);
			nvg.drawAlignCenteredText(m.getIcon(), x + offsetX + (244 / 2), y + 110 + (116 / 2) + offsetY,
					palette.getOnSurfaceVariant(), 68, Fonts.ICON_FILL);

			index++;
			offsetX += 32 + 244;

			if (index % 3 == 0) {
				offsetX = 32;
				offsetY += 22 + 151;
			}
		}
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {

		int index = 0;
		float offsetX = 32;
		float offsetY = 0;

		for (Pair<Mod, Animation> i : items) {

			Mod m = i.getFirst();

			if (mouseButton == 0) {
				if (MouseUtils.isInside(mouseX, mouseY, x + offsetX, y + 226 + offsetY, 244, 35)) {
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
}
