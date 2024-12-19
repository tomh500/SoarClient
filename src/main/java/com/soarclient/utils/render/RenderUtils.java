package com.soarclient.utils.render;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import com.soarclient.utils.ColorUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;

public class RenderUtils {

	private static Minecraft mc = Minecraft.getMinecraft();
	
	public static void drawRect(float x, float y, float width, float height, Color color) {

		GL11.glEnable(3042);
		GL11.glDisable(3553);
		GL11.glBlendFunc(770, 771);
		GL11.glEnable(2848);

		GL11.glPushMatrix();
		ColorUtils.setColor(color);
		GL11.glBegin(7);
		GL11.glVertex2d(x + width, y);
		GL11.glVertex2d(x, y);
		GL11.glVertex2d(x, y + height);
		GL11.glVertex2d(x + width, y + height);
		GL11.glEnd();
		GL11.glPopMatrix();

		GL11.glEnable(3553);
		GL11.glDisable(3042);
		GL11.glDisable(2848);
		GlStateManager.enableTexture2D();
		GlStateManager.disableBlend();
		ColorUtils.setColor(Color.WHITE);
	}
	
	public static void drawItemStack(ItemStack stack, float x, float y) {
		GlStateManager.pushMatrix();
		RenderHelper.enableGUIStandardItemLighting();
		GlStateManager.disableAlpha();
		GlStateManager.clear(256);
		mc.getRenderItem().zLevel = -150.0F;
		GlStateManager.disableLighting();
		GlStateManager.disableDepth();
		GlStateManager.disableBlend();
		GlStateManager.enableLighting();
		GlStateManager.enableDepth();
		GlStateManager.disableLighting();
		GlStateManager.disableDepth();
		GlStateManager.disableTexture2D();
		GlStateManager.disableAlpha();
		GlStateManager.disableBlend();
		GlStateManager.enableBlend();
		GlStateManager.enableAlpha();
		GlStateManager.enableTexture2D();
		GlStateManager.enableLighting();
		GlStateManager.enableDepth();
		mc.getRenderItem().renderItemIntoGUI(stack, (int) x, (int) y);
		mc.getRenderItem().renderItemOverlayIntoGUI(mc.fontRendererObj, stack, (int) x, (int) y, null);
		mc.getRenderItem().zLevel = 0.0F;
		GlStateManager.enableAlpha();
		RenderHelper.disableStandardItemLighting();
		GlStateManager.popMatrix();
	}
	
	public static void drawTexturedModalRect(float x, float y, float textureX, float textureY, float width,
			float height, float zLevel) {
		float f = 0.00390625F;
		float f1 = 0.00390625F;
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
		worldrenderer.pos((double) (x + 0), (double) (y + height), (double) zLevel)
				.tex((double) ((float) (textureX + 0) * f), (double) ((float) (textureY + height) * f1)).endVertex();
		worldrenderer.pos((double) (x + width), (double) (y + height), (double) zLevel)
				.tex((double) ((float) (textureX + width) * f), (double) ((float) (textureY + height) * f1))
				.endVertex();
		worldrenderer.pos((double) (x + width), (double) (y + 0), (double) zLevel)
				.tex((double) ((float) (textureX + width) * f), (double) ((float) (textureY + 0) * f1)).endVertex();
		worldrenderer.pos((double) (x + 0), (double) (y + 0), (double) zLevel)
				.tex((double) ((float) (textureX + 0) * f), (double) ((float) (textureY + 0) * f1)).endVertex();
		tessellator.draw();
	}

	public static void drawTexturedModalRect(float x, float y, float textureX, float textureY, float width,
			float height) {
		drawTexturedModalRect(x, y, textureX, textureY, width, height, 0);
	}
}
