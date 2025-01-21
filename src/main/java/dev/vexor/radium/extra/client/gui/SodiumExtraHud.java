package dev.vexor.radium.extra.client.gui;

import java.util.List;

import dev.vexor.radium.extra.client.SodiumExtraClientMod;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.Vec3;

public class SodiumExtraHud {

    private final List<IChatComponent> textList = new ObjectArrayList<>();

    private final Minecraft client = Minecraft.getMinecraft();

    public void onStartTick(Minecraft client) {
        // Clear the textList to start fresh (this might not be ideal but hey it's still better than whatever the fuck debug hud is doing)
        this.textList.clear();
        if (SodiumExtraClientMod.options().extraSettings.showFps) {
            int currentFPS = Minecraft.getDebugFPS();

            IChatComponent text = new ChatComponentTranslation("sodium-extra.overlay.fps", currentFPS);

            if (SodiumExtraClientMod.options().extraSettings.showFPSExtended)
                text = new ChatComponentText(String.format("%s %s", text.getFormattedText(), new ChatComponentTranslation("sodium-extra.overlay.fps_extended", SodiumExtraClientMod.getClientTickHandler().getHighestFps(), SodiumExtraClientMod.getClientTickHandler().getAverageFps(),
                        SodiumExtraClientMod.getClientTickHandler().getLowestFps()).getFormattedText()));

            this.textList.add(text);
        }

        if (SodiumExtraClientMod.options().extraSettings.showCoords && this.client.thePlayer != null) {
            Vec3 pos = this.client.thePlayer.getPositionVector();

            ChatComponentTranslation text = new ChatComponentTranslation("sodium-extra.overlay.coordinates", String.format("%.2f", pos.xCoord), String.format("%.2f", pos.yCoord), String.format("%.2f", pos.zCoord));
            this.textList.add(text);
        }
    }

    public void onHudRender() {
        if (this.client.gameSettings.showDebugInfo && !this.client.gameSettings.hideGUI) {
            SodiumExtraGameOptions.OverlayCorner overlayCorner = SodiumExtraClientMod.options().extraSettings.overlayCorner;
            // Calculate starting position based on the overlay corner
            int x;
            int y = overlayCorner == SodiumExtraGameOptions.OverlayCorner.BOTTOM_LEFT || overlayCorner == SodiumExtraGameOptions.OverlayCorner.BOTTOM_RIGHT ?
                    ScaledResolution.get(this.client).getScaledHeight() - this.client.fontRendererObj.FONT_HEIGHT - 2 : 2;
            // Render each text in the list
            for (IChatComponent text : this.textList) {
                if (overlayCorner == SodiumExtraGameOptions.OverlayCorner.TOP_RIGHT || overlayCorner == SodiumExtraGameOptions.OverlayCorner.BOTTOM_RIGHT) {
                    x = ScaledResolution.get(this.client).getScaledWidth() - this.client.fontRendererObj.getStringWidth(text.getFormattedText()) - 2;
                } else {
                    x = 2;
                }
                this.drawString(text, x, y);
                if (overlayCorner == SodiumExtraGameOptions.OverlayCorner.BOTTOM_LEFT || overlayCorner == SodiumExtraGameOptions.OverlayCorner.BOTTOM_RIGHT) {
                    y -= client.fontRendererObj.FONT_HEIGHT + 2;
                } else {
                    y += client.fontRendererObj.FONT_HEIGHT + 2; // Increase the y-position for the next text
                }
            }
        }
    }

    private void drawString(IChatComponent text, int x, int y) {
        int textColor = 0xffffffff; // Default text color

        if (SodiumExtraClientMod.options().extraSettings.textContrast == SodiumExtraGameOptions.TextContrast.BACKGROUND) {
            Gui.drawRect(x - 1, y - 1, x + this.client.fontRendererObj.getStringWidth(text.getFormattedText()) + 1, y + this.client.fontRendererObj.FONT_HEIGHT + 1, -1873784752);
        }

        this.client.fontRendererObj.drawString(text.getFormattedText(), x, y, textColor, SodiumExtraClientMod.options().extraSettings.textContrast == SodiumExtraGameOptions.TextContrast.SHADOW);
    }
}
