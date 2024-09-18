package com.soarclient.libraries.sodium.client.gui.options.control;

import net.minecraft.util.ChatComponentTranslation;

public interface ControlValueFormatter {
	static ControlValueFormatter guiScale() {
		return v -> v == 0
				? new ChatComponentTranslation("options.guiScale.auto", new Object[0]).getFormattedText()
				: new ChatComponentTranslation(v + "x", new Object[0]).getFormattedText();
	}

	static ControlValueFormatter fpsLimit() {
		return v -> v == 260 ? new ChatComponentTranslation("options.framerateLimit.max", new Object[0]).getFormattedText() : v + " FPS";
	}

	static ControlValueFormatter chunks() {
		return v -> {
			if (v < 4) {
				return new ChatComponentTranslation("options.renderDistance.tiny", new Object[0]).getFormattedText();
			} else if (v < 8) {
				return new ChatComponentTranslation("options.renderDistance.short", new Object[0]).getFormattedText();
			} else if (v < 16) {
				return new ChatComponentTranslation("options.renderDistance.normal", new Object[0]).getFormattedText();
			} else {
				return v <= 24 ? new ChatComponentTranslation("options.renderDistance.far", new Object[0]).getFormattedText() : v + " chunks";
			}
		};
	}

	static ControlValueFormatter brightness() {
		return v -> {
			if (v == 0) {
				return new ChatComponentTranslation("options.gamma.min", new Object[0]).getFormattedText();
			} else {
				return v == 100 ? new ChatComponentTranslation("options.gamma.max", new Object[0]).getFormattedText() : v + "%";
			}
		};
	}

	String format(int integer);

	static ControlValueFormatter percentage() {
		return v -> v + "%";
	}

	static ControlValueFormatter blocks() {
		return v -> v + " blocks";
	}

	static ControlValueFormatter multiplier() {
		return v -> new ChatComponentTranslation(v + "x", new Object[0]).getFormattedText();
	}

	static ControlValueFormatter quantity(String name) {
		return v -> new ChatComponentTranslation(name, new Object[]{v}).getFormattedText();
	}

	static ControlValueFormatter quantityOrDisabled(String name, String disableText) {
		return v -> new ChatComponentTranslation(v == 0 ? disableText : name, new Object[]{v}).getFormattedText();
	}

	static ControlValueFormatter number() {
		return String::valueOf;
	}
}
