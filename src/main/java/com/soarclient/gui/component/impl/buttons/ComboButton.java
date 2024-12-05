package com.soarclient.gui.component.impl.buttons;

import java.awt.Color;
import java.util.List;

import com.soarclient.Soar;
import com.soarclient.animation.Animation;
import com.soarclient.animation.Duration;
import com.soarclient.animation.cubicbezier.impl.EaseStandard;
import com.soarclient.animation.other.DummyAnimation;
import com.soarclient.gui.component.Component;
import com.soarclient.gui.component.handler.impl.ComboButtonHandler;
import com.soarclient.management.color.api.ColorPalette;
import com.soarclient.nanovg.NanoVGHelper;
import com.soarclient.nanovg.font.Fonts;
import com.soarclient.utils.ColorUtils;
import com.soarclient.utils.language.I18n;
import com.soarclient.utils.mouse.MouseUtils;

public class ComboButton extends Component {

	private Animation animation;
	private List<String> options;
	private String option;

	public ComboButton(float x, float y, List<String> options, String option) {
		super(x, y);
		this.options = options;
		this.option = option;
		this.animation = new DummyAnimation(1);
		width = 126;
		height = 32;
	}

	@Override
	public void draw(int mouseX, int mouseY) {

		NanoVGHelper nvg = NanoVGHelper.getInstance();
		ColorPalette palette = Soar.getInstance().getColorManager().getPalette();

		nvg.drawRoundedRect(x, y, width, height, 12, palette.getPrimary());
		nvg.drawText("<", x + 6, y + 9, palette.getSurface(), 14, Fonts.REGULAR);
		nvg.drawText(">", x + width - 16, y + 9, palette.getSurface(), 14, Fonts.REGULAR);
		nvg.drawAlignCenteredText(I18n.get(option),
				x + (width / 2) + ((animation.getEnd() - animation.getValue()) * 22), y + (height / 2),
				ColorUtils.applyAlpha(palette.getSurface(), Math.abs(animation.getValue())), 14, Fonts.MEDIUM);
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {

		int max = options.size();
		int index = options.indexOf(option);

		if (mouseButton == 0) {

			if (MouseUtils.isInside(mouseX, mouseY, x, y, 32, 32)) {

				animation = new EaseStandard(Duration.MEDIUM_3, 0, 1);

				if (index > 0) {
					index--;
				} else {
					index = max - 1;
				}

				setOption(options.get(index));
			}

			if (MouseUtils.isInside(mouseX, mouseY, x + width - 32, y, 32, 32)) {

				animation = new EaseStandard(Duration.MEDIUM_3, 0, -1);

				if (index < max - 1) {
					index++;
				} else {
					index = 0;
				}

				setOption(options.get(index));
			}
		}
	}

	public String getOption() {
		return option;
	}

	public void setOption(String option) {

		this.option = option;

		if (handler instanceof ComboButtonHandler) {
			((ComboButtonHandler) handler).onChanged(option);
		}
	}
}
