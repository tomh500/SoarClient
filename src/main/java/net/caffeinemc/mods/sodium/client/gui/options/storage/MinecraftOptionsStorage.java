package net.caffeinemc.mods.sodium.client.gui.options.storage;

import net.caffeinemc.mods.sodium.client.SodiumClientMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;

public class MinecraftOptionsStorage implements OptionStorage<GameSettings> {
    private final Minecraft minecraft;

    public MinecraftOptionsStorage() {
        this.minecraft = Minecraft.getMinecraft();
    }

    @Override
    public GameSettings getData() {
        return this.minecraft.gameSettings;
    }

    @Override
    public void save() {
        this.getData().saveOptions();

        SodiumClientMod.logger().info("Flushed changes to Minecraft configuration");
    }
}
