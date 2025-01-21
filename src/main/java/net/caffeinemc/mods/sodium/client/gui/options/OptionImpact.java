package net.caffeinemc.mods.sodium.client.gui.options;

import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

public enum OptionImpact implements TextProvider {
    LOW(EnumChatFormatting.GREEN, "sodium.option_impact.low"),
    MEDIUM(EnumChatFormatting.YELLOW, "sodium.option_impact.medium"),
    HIGH(EnumChatFormatting.GOLD, "sodium.option_impact.high"),
    VARIES(EnumChatFormatting.WHITE, "sodium.option_impact.varies");

    private final IChatComponent text;

    OptionImpact(EnumChatFormatting formatting, String text) {
        this.text = new ChatComponentTranslation(text)
                .setChatStyle(new ChatStyle()
                        .setColor(formatting));
    }

    @Override
    public IChatComponent getLocalizedName() {
        return this.text;
    }
}
