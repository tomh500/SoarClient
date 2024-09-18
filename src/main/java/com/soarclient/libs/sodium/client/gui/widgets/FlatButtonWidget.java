package com.soarclient.libs.sodium.client.gui.widgets;

import com.soarclient.libs.sodium.client.gui.utils.Drawable;
import com.soarclient.libs.sodium.client.util.Dim2i;

import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

public class FlatButtonWidget extends AbstractWidget implements Drawable {
	private final Dim2i dim;
	private final IChatComponent label;
	private final Runnable action;
	private boolean selected;
	private boolean enabled = true;
	private boolean visible = true;

	public FlatButtonWidget(Dim2i dim, String label, Runnable action) {
		this(dim, new ChatComponentText(label), action);
	}

	public FlatButtonWidget(Dim2i dim, IChatComponent label, Runnable action) {
		this.dim = dim;
		this.label = label;
		this.action = action;
	}

	@Override
	public void render(int mouseX, int mouseY, float delta) {
		if (this.visible) {
			boolean hovered = this.dim.containsCursor((double)mouseX, (double)mouseY);
			int backgroundColor = this.enabled ? (hovered ? -536870912 : -1879048192) : 1610612736;
			int textColor = this.enabled ? -1 : -1862270977;
			int strWidth = this.font.getStringWidth(this.label.getFormattedText());
			this.drawRect((double)this.dim.getOriginX(), (double)this.dim.getOriginY(), (double)this.dim.getLimitX(), (double)this.dim.getLimitY(), backgroundColor);
			this.drawString(this.label.getFormattedText(), this.dim.getCenterX() - strWidth / 2, this.dim.getCenterY() - 4, textColor);
			if (this.enabled && this.selected) {
				this.drawRect((double)this.dim.getOriginX(), (double)(this.dim.getLimitY() - 1), (double)this.dim.getLimitX(), (double)this.dim.getLimitY(), -7019309);
			}
		}
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if (!this.enabled || !this.visible) {
			return false;
		} else if (button == 0 && this.dim.containsCursor(mouseX, mouseY)) {
			this.action.run();
			this.playClickSound();
			return true;
		} else {
			return false;
		}
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}
}
