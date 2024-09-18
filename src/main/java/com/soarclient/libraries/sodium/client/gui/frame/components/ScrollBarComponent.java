package com.soarclient.libraries.sodium.client.gui.frame.components;

import java.util.function.Consumer;

import com.soarclient.libraries.sodium.client.gui.widgets.AbstractWidget;
import com.soarclient.libraries.sodium.client.util.Dim2i;

import net.minecraft.util.MathHelper;

public class ScrollBarComponent extends AbstractWidget {
	protected static final int SCROLL_OFFSET = 6;
	protected final Dim2i dim;
	private final ScrollBarComponent.Mode mode;
	private final int frameLength;
	private final int viewPortLength;
	private final int maxScrollBarOffset;
	private final Consumer<Integer> onSetOffset;
	private int offset = 0;
	private boolean isDragging;
	private Dim2i scrollThumb = null;
	private int scrollThumbClickOffset;
	private Dim2i extendedScrollArea = null;

	public ScrollBarComponent(Dim2i trackArea, ScrollBarComponent.Mode mode, int frameLength, int viewPortLength, Consumer<Integer> onSetOffset) {
		this.dim = trackArea;
		this.mode = mode;
		this.frameLength = frameLength;
		this.viewPortLength = viewPortLength;
		this.onSetOffset = onSetOffset;
		this.maxScrollBarOffset = this.frameLength - this.viewPortLength;
	}

	public ScrollBarComponent(
		Dim2i scrollBarArea, ScrollBarComponent.Mode mode, int frameLength, int viewPortLength, Consumer<Integer> onSetOffset, Dim2i extendedTrackArea
	) {
		this(scrollBarArea, mode, frameLength, viewPortLength, onSetOffset);
		this.extendedScrollArea = extendedTrackArea;
	}

	public void updateThumbPosition() {
		int scrollThumbLength = this.viewPortLength
			* (this.mode == ScrollBarComponent.Mode.VERTICAL ? this.dim.getHeight() : this.dim.getWidth() - 6)
			/ this.frameLength;
		int maximumScrollThumbOffset = this.viewPortLength - scrollThumbLength;
		int scrollThumbOffset = this.offset * maximumScrollThumbOffset / this.maxScrollBarOffset;
		this.scrollThumb = new Dim2i(
			this.dim.getOriginX() + 2 + (this.mode == ScrollBarComponent.Mode.HORIZONTAL ? scrollThumbOffset : 0),
			this.dim.getOriginY() + 2 + (this.mode == ScrollBarComponent.Mode.VERTICAL ? scrollThumbOffset : 0),
			(this.mode == ScrollBarComponent.Mode.VERTICAL ? this.dim.getWidth() : scrollThumbLength) - 4,
			(this.mode == ScrollBarComponent.Mode.VERTICAL ? scrollThumbLength : this.dim.getHeight()) - 4
		);
	}

	@Override
	public void render(int mouseX, int mouseY, float delta) {
		this.drawRectOutline((double)this.dim.getOriginX(), (double)this.dim.getOriginY(), (double)this.dim.getLimitX(), (double)this.dim.getLimitY(), -5592406);
		this.drawRect(
			(double)this.scrollThumb.getOriginX(),
			(double)this.scrollThumb.getOriginY(),
			(double)this.scrollThumb.getLimitX(),
			(double)this.scrollThumb.getLimitY(),
			-5592406
		);
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if (this.dim.containsCursor(mouseX, mouseY)) {
			if (this.scrollThumb.containsCursor(mouseX, mouseY)) {
				if (this.mode == ScrollBarComponent.Mode.VERTICAL) {
					this.scrollThumbClickOffset = (int)(mouseY - (double)this.scrollThumb.getCenterY());
				} else {
					this.scrollThumbClickOffset = (int)(mouseX - (double)this.scrollThumb.getCenterX());
				}

				this.isDragging = true;
			} else {
				int value;
				if (this.mode == ScrollBarComponent.Mode.VERTICAL) {
					value = (int)(
						(mouseY - (double)this.dim.getOriginY() - (double)(this.scrollThumb.getHeight() / 2))
							/ (double)(this.dim.getHeight() - this.scrollThumb.getHeight())
							* (double)this.maxScrollBarOffset
					);
				} else {
					value = (int)(
						(mouseX - (double)this.dim.getOriginX() - (double)(this.scrollThumb.getWidth() / 2))
							/ (double)(this.dim.getWidth() - this.scrollThumb.getWidth())
							* (double)this.maxScrollBarOffset
					);
				}

				this.setOffset(value);
				this.isDragging = false;
			}

			return true;
		} else {
			this.isDragging = false;
			return false;
		}
	}

	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int button) {
		if (button == 0) {
			this.isDragging = false;
		}

		return false;
	}

	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int button) {
		if (this.isDragging) {
			int value;
			if (this.mode == ScrollBarComponent.Mode.VERTICAL) {
				value = (int)(
					(mouseY - (double)this.scrollThumbClickOffset - (double)this.dim.getOriginY() - (double)(this.scrollThumb.getHeight() / 2))
						/ (double)(this.dim.getHeight() - this.scrollThumb.getHeight())
						* (double)this.maxScrollBarOffset
				);
			} else {
				value = (int)(
					(mouseX - (double)this.scrollThumbClickOffset - (double)this.dim.getOriginX() - (double)(this.scrollThumb.getWidth() / 2))
						/ (double)(this.dim.getWidth() - this.scrollThumb.getWidth())
						* (double)this.maxScrollBarOffset
				);
			}

			this.setOffset(value);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
		if ((this.dim.containsCursor(mouseX, mouseY) || this.extendedScrollArea != null && this.extendedScrollArea.containsCursor(mouseX, mouseY))
			&& this.offset <= this.maxScrollBarOffset
			&& this.offset >= 0) {
			int value = (int)((double)this.offset - amount * 6.0);
			this.setOffset(value);
			return true;
		} else {
			return false;
		}
	}

	public void setOffset(int value) {
		this.offset = MathHelper.clamp_int(value, 0, this.maxScrollBarOffset);
		this.updateThumbPosition();
		this.onSetOffset.accept(this.offset);
	}

	protected void drawRectOutline(double x, double y, double w, double h, int color) {
		float a = (float)(color >> 24 & 0xFF) / 255.0F;
		float r = (float)(color >> 16 & 0xFF) / 255.0F;
		float g = (float)(color >> 8 & 0xFF) / 255.0F;
		float b = (float)(color & 0xFF) / 255.0F;
		this.drawQuads(vertices -> {
			addQuad(vertices, x, y, w, y + 1.0, a, r, g, b);
			addQuad(vertices, x, h - 1.0, w, h, a, r, g, b);
			addQuad(vertices, x, y, x + 1.0, h, a, r, g, b);
			addQuad(vertices, w - 1.0, y, w, h, a, r, g, b);
		});
	}

	public int getOffset() {
		return this.offset;
	}

	public static enum Mode {
		HORIZONTAL,
		VERTICAL;
	}
}
