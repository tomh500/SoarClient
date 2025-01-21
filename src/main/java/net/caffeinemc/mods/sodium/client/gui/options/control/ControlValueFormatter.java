package net.caffeinemc.mods.sodium.client.gui.options.control;


import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.Util;

public interface ControlValueFormatter {
    static ControlValueFormatter guiScale() {
        return (v) -> (v == 0) ? new ChatComponentTranslation("options.guiScale.auto") : new ChatComponentText(v + "x");
    }

    static ControlValueFormatter resolution() {
        return (v) -> {
            if (Util.getOSType() != Util.EnumOS.WINDOWS) {
                return new ChatComponentTranslation("options.fullscreen.unavailable");
            } else if (0 == v) {
                return new ChatComponentTranslation("options.fullscreen.current");
            }
            return null;
        };
    }
    static ControlValueFormatter fpsLimit() {
        return (v) -> (v == 260) ? new ChatComponentTranslation("options.framerateLimit.max") : new ChatComponentText(v + " FPS");
    }

    static ControlValueFormatter brightness() {
        return (v) -> {
            if (v == 0) {
                return new ChatComponentTranslation("options.gamma.min");
            } else if (v == 100) {
                return new ChatComponentTranslation("options.gamma.max");
            } else {
                return new ChatComponentText(v + "%");
            }
        };
    }

    static ControlValueFormatter biomeBlend() {
        return (v) -> (v == 0) ? new ChatComponentTranslation("gui.none") : new ChatComponentTranslation("sodium.options.biome_blend.value", v);
    }

    IChatComponent format(int value);

    static ControlValueFormatter chunks() {
        return (v) -> {
            if (v < 4) {
                new ChatComponentTranslation("options.renderDistance.tiny").getFormattedText();
            } else if (v < 8) {
                new ChatComponentTranslation("options.renderDistance.short").getFormattedText();
            } else if (v < 16) {
                new ChatComponentTranslation("options.renderDistance.normal").getFormattedText();
            } else if (v <= 24) {
                new ChatComponentTranslation("options.renderDistance.far").getFormattedText();
            }
            return new ChatComponentText(v + " chunks");
        };
    }

    static ControlValueFormatter translateVariable(String key) {
        return (v) -> new ChatComponentTranslation(key, v);
    }

    static ControlValueFormatter percentage() {
        return (v) -> new ChatComponentText(v + "%");
    }

    static ControlValueFormatter multiplier() {
        return (v) -> new ChatComponentText(v + "x");
    }

    static ControlValueFormatter quantityOrDisabled(String name, String disableText) {
        return (v) -> new ChatComponentText(v == 0 ? disableText : v + " " + name);
    }

    static ControlValueFormatter number() {
        return (v) -> new ChatComponentText(String.valueOf(v));
    }
}
