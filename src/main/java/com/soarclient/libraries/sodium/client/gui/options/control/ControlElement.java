package com.soarclient.libraries.sodium.client.gui.options.control;

import com.soarclient.libraries.sodium.client.gui.OptionExtended;
import com.soarclient.libraries.sodium.client.gui.options.Option;
import com.soarclient.libraries.sodium.client.gui.widgets.AbstractWidget;
import com.soarclient.libraries.sodium.client.util.Dim2i;

import net.minecraft.util.EnumChatFormatting;

public class ControlElement<T> extends AbstractWidget {
	protected final Option<T> option;
	protected final Dim2i dim;
	protected boolean hovered;

	public ControlElement(Option<T> option, Dim2i dim) {
		this.option = option;
		this.dim = dim;
	}

	public boolean isHovered() {
		return this.hovered;
	}

	@Override
	public void render(int mouseX, int mouseY, float delta) {
		String name = this.option.getName();
		if (this.hovered && this.font.getStringWidth(name) > this.dim.getWidth() - this.option.getControl().getMaxWidth()) {
			name = name.substring(0, Math.min(name.length(), 10)) + "...";
		}

		String label;
		if (this.option.isAvailable()) {
			if (this.option.hasChanged()) {
				label = EnumChatFormatting.ITALIC + name + " *";
			} else {
				label31: {
					if (this.option instanceof OptionExtended<?> optionExtended && optionExtended.isHighlight()) {
						EnumChatFormatting color = optionExtended.isSelected() ? EnumChatFormatting.DARK_GREEN : EnumChatFormatting.YELLOW;
						label = color + name;
						break label31;
					}

					label = EnumChatFormatting.WHITE + name;
				}
			}
		} else {
			label = "" + EnumChatFormatting.GRAY + EnumChatFormatting.STRIKETHROUGH + name;
		}

		this.hovered = this.dim.containsCursor((double)mouseX, (double)mouseY);
		this.drawRect(
			(double)this.dim.getOriginX(),
			(double)this.dim.getOriginY(),
			(double)this.dim.getLimitX(),
			(double)this.dim.getLimitY(),
			this.hovered ? -536870912 : -1879048192
		);
		this.drawString(label, this.dim.getOriginX() + 6, this.dim.getCenterY() - 4, -1);
	}

	public Option<T> getOption() {
		return this.option;
	}

	public Dim2i getDimensions() {
		return this.dim;
	}
}
