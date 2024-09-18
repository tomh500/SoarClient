package com.soarclient.libraries.sodium.client.gui.options.control;

import net.minecraft.util.MathHelper;
import org.apache.commons.lang3.Validate;

import com.soarclient.libraries.sodium.client.gui.options.Option;
import com.soarclient.libraries.sodium.client.gui.utils.Rect2i;
import com.soarclient.libraries.sodium.client.util.Dim2i;

public class SliderControl implements Control<Integer> {
	private final Option<Integer> option;
	private final int min;
	private final int max;
	private final int interval;
	private final ControlValueFormatter mode;

	public SliderControl(Option<Integer> option, int min, int max, int interval, ControlValueFormatter mode) {
		Validate.isTrue(max > min, "The maximum value must be greater than the minimum value", new Object[0]);
		Validate.isTrue(interval > 0, "The slider interval must be greater than zero", new Object[0]);
		Validate.isTrue((max - min) % interval == 0, "The maximum value must be divisable by the interval", new Object[0]);
		Validate.notNull(mode, "The slider mode must not be null", new Object[0]);
		this.option = option;
		this.min = min;
		this.max = max;
		this.interval = interval;
		this.mode = mode;
	}

	@Override
	public ControlElement<Integer> createElement(Dim2i dim) {
		return new SliderControl.Button(this.option, dim, this.min, this.max, this.interval, this.mode);
	}

	@Override
	public Option<Integer> getOption() {
		return this.option;
	}

	@Override
	public int getMaxWidth() {
		return 130;
	}

	private static class Button extends ControlElement<Integer> {
		private static final int THUMB_WIDTH = 2;
		private static final int TRACK_HEIGHT = 1;
		private final Rect2i sliderBounds;
		private final ControlValueFormatter formatter;
		private final int min;
		private final int range;
		private final int interval;
		private double thumbPosition;

		public Button(Option<Integer> option, Dim2i dim, int min, int max, int interval, ControlValueFormatter formatter) {
			super(option, dim);
			this.min = min;
			this.range = max - min;
			this.interval = interval;
			this.thumbPosition = this.getThumbPositionForValue(option.getValue());
			this.formatter = formatter;
			this.sliderBounds = new Rect2i(dim.getLimitX() - 96, dim.getCenterY() - 5, 90, 10);
		}

		@Override
		public void render(int mouseX, int mouseY, float delta) {
			super.render(mouseX, mouseY, delta);
			if (this.option.isAvailable() && this.hovered) {
				this.renderSlider();
			} else {
				this.renderStandaloneValue();
			}
		}

		@Override
		public boolean mouseDragged(double mouseX, double mouseY, int button) {
			if (this.option.isAvailable() && this.sliderBounds.contains((int)mouseX, (int)mouseY)) {
				this.setValueFromMouse(mouseX);
				return true;
			} else {
				return false;
			}
		}

		private void renderStandaloneValue() {
			int sliderX = this.sliderBounds.getX();
			int sliderY = this.sliderBounds.getY();
			int sliderWidth = this.sliderBounds.getWidth();
			int sliderHeight = this.sliderBounds.getHeight();
			String label = this.formatter.format(this.option.getValue());
			int labelWidth = this.font.getStringWidth(label);
			this.drawString(label, sliderX + sliderWidth - labelWidth, sliderY + sliderHeight / 2 - 4, -1);
		}

		private void renderSlider() {
			int sliderX = this.sliderBounds.getX();
			int sliderY = this.sliderBounds.getY();
			int sliderWidth = this.sliderBounds.getWidth();
			int sliderHeight = this.sliderBounds.getHeight();
			this.thumbPosition = this.getThumbPositionForValue(this.option.getValue());
			double thumbOffset = MathHelper.clamp_double((double)(this.getIntValue() - this.min) / (double)this.range * (double)sliderWidth, 0.0, (double)sliderWidth);
			double thumbX = (double)sliderX + thumbOffset - 2.0;
			double trackY = (double)(sliderY + sliderHeight / 2) - 0.5;
			this.drawRect(thumbX, (double)sliderY, thumbX + 4.0, (double)(sliderY + sliderHeight), -1);
			this.drawRect((double)sliderX, trackY, (double)(sliderX + sliderWidth), trackY + 1.0, -1);
			String label = String.valueOf(this.getIntValue());
			int labelWidth = this.font.getStringWidth(label);
			this.drawString(label, sliderX - labelWidth - 6, sliderY + sliderHeight / 2 - 4, -1);
		}

		public int getIntValue() {
			return this.min + this.interval * (int)Math.round(this.getSnappedThumbPosition() / (double)this.interval);
		}

		public double getSnappedThumbPosition() {
			return this.thumbPosition / (1.0 / (double)this.range);
		}

		public double getThumbPositionForValue(int value) {
			return (double)(value - this.min) * (1.0 / (double)this.range);
		}

		@Override
		public boolean mouseClicked(double mouseX, double mouseY, int button) {
			if (this.option.isAvailable() && button == 0 && this.sliderBounds.contains((int)mouseX, (int)mouseY)) {
				this.setValueFromMouse(mouseX);
				return true;
			} else {
				return false;
			}
		}

		private void setValueFromMouse(double d) {
			this.setValue((d - (double)this.sliderBounds.getX()) / (double)this.sliderBounds.getWidth());
		}

		private void setValue(double d) {
			this.thumbPosition = MathHelper.clamp_double(d, 0.0, 1.0);
			int value = this.getIntValue();
			if (this.option.getValue() != value) {
				this.option.setValue(value);
			}
		}
	}
}
