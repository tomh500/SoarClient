package com.soarclient.libs.sodium.client.gui.options;

import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;

public enum OptionImpact {
	LOW(EnumChatFormatting.GREEN, new ChatComponentTranslation("sodium.option_impact.low", new Object[0]).getFormattedText()),
	MEDIUM(EnumChatFormatting.YELLOW, new ChatComponentTranslation("sodium.option_impact.medium", new Object[0]).getFormattedText()),
	HIGH(EnumChatFormatting.GOLD, new ChatComponentTranslation("sodium.option_impact.high", new Object[0]).getFormattedText()),
	EXTREME(EnumChatFormatting.RED, new ChatComponentTranslation("sodium.option_impact.extreme", new Object[0]).getFormattedText()),
	VARIES(EnumChatFormatting.WHITE, new ChatComponentTranslation("sodium.option_impact.varies", new Object[0]).getFormattedText());

	private final EnumChatFormatting color;
	private final String text;

	private OptionImpact(EnumChatFormatting color, String text) {
		this.color = color;
		this.text = text;
	}

	public String toDisplayString() {
		return this.color + this.text;
	}
}
