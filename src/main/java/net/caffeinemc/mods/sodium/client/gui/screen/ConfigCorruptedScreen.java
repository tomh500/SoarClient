package net.caffeinemc.mods.sodium.client.gui.screen;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.jetbrains.annotations.Nullable;

import net.caffeinemc.mods.sodium.client.SodiumClientMod;
import net.caffeinemc.mods.sodium.client.console.Console;
import net.caffeinemc.mods.sodium.client.console.message.MessageLevel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

public class ConfigCorruptedScreen extends GuiScreen {
    private static final String TEXT_BODY_RAW = """
        A problem occurred while trying to load the configuration file. This
        can happen when the file has been corrupted on disk, or when trying
        to manually edit the file by hand.
        
        If you continue, the configuration file will be reset back to known-good
        defaults, and you will lose any changes that have since been made to your
        Video Settings.
        
        More information about the error can be found in the log file.
        """;

    private static final List<IChatComponent> TEXT_BODY = Arrays.stream(TEXT_BODY_RAW.split("\n"))
            .map(ChatComponentText::new)
            .collect(Collectors.toList());

    private static final int BUTTON_WIDTH = 140;
    private static final int BUTTON_HEIGHT = 20;

    private static final int SCREEN_PADDING = 32;

    private final @Nullable GuiScreen prevScreen;
    private final Function<GuiScreen, GuiScreen> nextScreen;

    public ConfigCorruptedScreen(@Nullable GuiScreen prevScreen, @Nullable Function<GuiScreen, GuiScreen> nextScreen) {

        this.prevScreen = prevScreen;
        this.nextScreen = nextScreen;
    }

    @Override
    public void initGui() {
        super.initGui();

        int buttonY = this.height - SCREEN_PADDING - BUTTON_HEIGHT;

        this.buttonList.add(new GuiButton(
                69,
                this.width - SCREEN_PADDING - BUTTON_WIDTH, buttonY, BUTTON_WIDTH, BUTTON_HEIGHT,
                "Continue"
        ));
        this.buttonList.add(new GuiButton(
                420,
                SCREEN_PADDING, buttonY, BUTTON_WIDTH, BUTTON_HEIGHT,
                "Go back"
        ));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float delta) {
        super.drawScreen(mouseX, mouseY, delta);

        this.fontRendererObj.drawString("Radium Renderer", 32, 32, 0xffffff);
        this.fontRendererObj.drawString("Could not load the configuration file", 32, 48, 0xff0000);

        for (int i = 0; i < TEXT_BODY.size(); i++) {
            if (TEXT_BODY.get(i).getUnformattedText().isEmpty()) {
                continue;
            }

            this.fontRendererObj.drawString(TEXT_BODY.get(i).getFormattedText(), 32, 68 + (i * 12), 0xffffff);
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        try {
			super.actionPerformed(button);
		} catch (IOException e) {
			e.printStackTrace();
		}

        switch (button.id) {
            case 69 -> Minecraft.getMinecraft().displayGuiScreen(this.prevScreen);
            case 420 -> {
                Console.instance().logMessage(MessageLevel.INFO, "sodium.console.config_file_was_reset", true, 3.0);

                SodiumClientMod.restoreDefaultOptions();
                assert this.nextScreen != null;
                Minecraft.getMinecraft().displayGuiScreen(this.nextScreen.apply(this.prevScreen));
            }
        }
    }
}
