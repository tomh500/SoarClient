package com.soarclient.libraries.sodium.client.gui.options.control;

import com.soarclient.libraries.sodium.client.gui.options.Option;
import com.soarclient.libraries.sodium.client.gui.utils.Rect2i;
import com.soarclient.libraries.sodium.client.util.Dim2i;

public class TickBoxControl implements Control<Boolean> {
	private final Option<Boolean> option;

	public TickBoxControl(Option<Boolean> option) {
		this.option = option;
	}

	@Override
	public ControlElement<Boolean> createElement(Dim2i dim) {
		return new TickBoxControl.TickBoxControlElement(this.option, dim);
	}

	@Override
	public int getMaxWidth() {
		return 30;
	}

	@Override
	public Option<Boolean> getOption() {
		return this.option;
	}

	private static class TickBoxControlElement extends ControlElement<Boolean> {
		private final Rect2i button;

		public TickBoxControlElement(Option<Boolean> option, Dim2i dim) {
			super(option, dim);
			this.button = new Rect2i(dim.getLimitX() - 16, dim.getCenterY() - 5, 10, 10);
		}

		@Override
		public void render(int mouseX, int mouseY, float delta) {
			super.render(mouseX, mouseY, delta);
			int x = this.button.getX();
			int y = this.button.getY();
			int w = x + this.button.getWidth();
			int h = y + this.button.getHeight();
			boolean enabled = this.option.isAvailable();
			boolean ticked = enabled && this.option.getValue();
			int color;
			if (enabled) {
				color = ticked ? -7019309 : -1;
			} else {
				color = -5592406;
			}

			if (ticked) {
				this.drawRect((double) (x + 2), (double) (y + 2), (double) (w - 2), (double) (h - 2), color);
			}

			this.drawRectOutline(x, y, w, h, color);
		}

		@Override
		public boolean mouseClicked(double mouseX, double mouseY, int button) {
			if (this.option.isAvailable() && button == 0 && this.dim.containsCursor(mouseX, mouseY)) {
				this.option.setValue(!this.option.getValue());
				this.playClickSound();
				return true;
			} else {
				return false;
			}
		}

		protected void drawRectOutline(int x, int y, int w, int h, int color) {
			float a = (float) (color >> 24 & 0xFF) / 255.0F;
			float r = (float) (color >> 16 & 0xFF) / 255.0F;
			float g = (float) (color >> 8 & 0xFF) / 255.0F;
			float b = (float) (color & 0xFF) / 255.0F;
			this.drawQuads(vertices -> {
				addQuad(vertices, (double) x, (double) y, (double) w, (double) (y + 1), a, r, g, b);
				addQuad(vertices, (double) x, (double) (h - 1), (double) w, (double) h, a, r, g, b);
				addQuad(vertices, (double) x, (double) y, (double) (x + 1), (double) h, a, r, g, b);
				addQuad(vertices, (double) (w - 1), (double) y, (double) w, (double) h, a, r, g, b);
			});
		}
	}
}
