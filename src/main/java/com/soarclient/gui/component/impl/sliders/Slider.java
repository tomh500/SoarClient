package com.soarclient.gui.component.impl.sliders;

import com.soarclient.Soar;
import com.soarclient.animation.SimpleAnimation;
import com.soarclient.gui.component.Component;
import com.soarclient.gui.component.handler.impl.SliderHandler;
import com.soarclient.management.color.api.ColorPalette;
import com.soarclient.nanovg.NanoVGHelper;
import com.soarclient.utils.math.MathUtils;
import com.soarclient.utils.mouse.MouseUtils;

public class Slider extends Component {

	private SimpleAnimation slideAnimation = new SimpleAnimation();

	private boolean dragging;
	private float value, minValue, maxValue;
	private float step;

	public Slider(float x, float y, float width, float value, float minValue, float maxValue, float step) {
		super(x, y);
		this.width = width;
		this.height = 44;
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.step = step;
		this.setValue(value);
	}

	@Override
	public void draw(int mouseX, int mouseY) {

		NanoVGHelper nvg = NanoVGHelper.getInstance();
		ColorPalette palette = Soar.getInstance().getColorManager().getPalette();

		slideAnimation.onTick(((getValue() - minValue) / (maxValue - minValue)) * width, 20);

		float padding = 6;
		float selWidth = 4;
		float barHeight = 16;
		float offsetY = (height / 2) - (barHeight / 2);

		float slideValue = Math.abs(slideAnimation.getValue());

		nvg.drawRoundedRect(x + slideValue - (selWidth / 2), y, selWidth, 44, 3, palette.getPrimary());
		nvg.drawRoundedRectVarying(x, y + offsetY, slideValue - (selWidth / 2) - padding, barHeight, 8, 4, 8, 4,
				palette.getPrimary());
		nvg.drawRoundedRectVarying(x + padding + (selWidth / 2) + slideValue, y + offsetY, width - slideValue - padding,
				barHeight, 4, 8, 4, 8, palette.getPrimaryContainer());

		if (dragging) {

			float rawValue = Math.min(1, Math.max(0, (mouseX - x) / width));
			float actualValue = rawValue * (maxValue - minValue) + minValue;
			float steppedValue = Math.round(actualValue / step) * step;

			value = (steppedValue - minValue) / (maxValue - minValue);

			if (handler instanceof SliderHandler) {
				((SliderHandler) handler).onValueChanged(getValue());
			}
		}
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {

		if (MouseUtils.isInside(mouseX, mouseY, x, y, width, height) && mouseButton == 0) {

			dragging = true;

			if (handler instanceof SliderHandler) {
				((SliderHandler) handler).onClicked(getValue());
			}
		}
	}

	@Override
	public void mouseReleased(int mouseX, int mouseY, int mouseButton) {

		if (dragging) {

			dragging = false;

			if (handler instanceof SliderHandler) {
				((SliderHandler) handler).onReleased(getValue());
			}
		}
	}

	public float getValue() {
		return MathUtils.roundToPlace(value * (maxValue - minValue) + minValue, 2);
	}

	public void setValue(float value) {
		float steppedValue = Math.round(value / step) * step;
		this.value = (steppedValue - minValue) / (maxValue - minValue);
	}
}
