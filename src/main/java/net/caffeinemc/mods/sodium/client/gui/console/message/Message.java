package net.caffeinemc.mods.sodium.client.gui.console.message;

import net.minecraft.util.IChatComponent;

public record Message(MessageLevel level, IChatComponent text, double duration) {

}
