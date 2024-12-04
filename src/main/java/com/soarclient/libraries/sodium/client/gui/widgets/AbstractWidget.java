package com.soarclient.libraries.sodium.client.gui.widgets;

import java.util.function.Consumer;

import com.soarclient.libraries.sodium.client.gui.utils.Drawable;
import com.soarclient.libraries.sodium.client.gui.utils.Element;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ResourceLocation;

public abstract class AbstractWidget implements Drawable, Element {
	protected final FontRenderer font = Minecraft.getMinecraft().fontRendererObj;

	protected AbstractWidget() {
	}

	protected void drawString(String str, int x, int y, int color) {
		this.font.drawString(str, x, y, color);
	}

	protected void drawRect(double x1, double y1, double x2, double y2, int color) {
		float a = (float) (color >> 24 & 0xFF) / 255.0F;
		float r = (float) (color >> 16 & 0xFF) / 255.0F;
		float g = (float) (color >> 8 & 0xFF) / 255.0F;
		float b = (float) (color & 0xFF) / 255.0F;
		this.drawQuads(vertices -> addQuad(vertices, x1, y1, x2, y2, a, r, g, b));
	}

	protected void drawQuads(Consumer<WorldRenderer> consumer) {
		GlStateManager.enableBlend();
		GlStateManager.disableTexture2D();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer bufferBuilder = tessellator.getWorldRenderer();
		bufferBuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
		consumer.accept(bufferBuilder);
		tessellator.draw();
		GlStateManager.enableTexture2D();
		GlStateManager.disableBlend();
	}

	protected static void addQuad(WorldRenderer consumer, double x1, double y1, double x2, double y2, float a, float r,
			float g, float b) {
		consumer.pos(x2, y1, 0.0).color(r, g, b, a).endVertex();
		consumer.pos(x1, y1, 0.0).color(r, g, b, a).endVertex();
		consumer.pos(x1, y2, 0.0).color(r, g, b, a).endVertex();
		consumer.pos(x2, y2, 0.0).color(r, g, b, a).endVertex();
	}

	protected void playClickSound() {
		Minecraft.getMinecraft().getSoundHandler()
				.playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
	}

	protected int getStringWidth(String text) {
		return this.font.getStringWidth(text);
	}

	protected int getTextWidth(IChatComponent text) {
		return this.font.getStringWidth(text.getFormattedText());
	}
}
