package com.soarclient.management.mods.impl.hud;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import com.soarclient.event.EventHandler;
import com.soarclient.event.impl.LoadWorldEvent;
import com.soarclient.event.impl.RenderGameOverlayEvent;
import com.soarclient.management.mods.api.HUDMod;
import com.soarclient.management.mods.impl.hud.minimap.ChunkAtlas;
import com.soarclient.management.mods.impl.hud.minimap.ChunkTile;
import com.soarclient.management.mods.settings.impl.NumberSetting;
import com.soarclient.nanovg.NanoVGHelper;
import com.soarclient.nanovg.font.Icon;
import com.soarclient.shaders.screen.ScreenWrapper;
import com.soarclient.utils.render.GlUtils;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;

public class MinimapMod extends HUDMod {

	private NumberSetting widthSetting = new NumberSetting("setting.width", "setting.width.description", Icon.WIDTH,
			this, 150, 10, 180, 1);
	private NumberSetting heightSetting = new NumberSetting("setting.height", "setting.height.description", Icon.HEIGHT,
			this, 70, 10, 180, 1);
	private NumberSetting alphaSetting = new NumberSetting("setting.alpha", "setting.alpha.description", Icon.OPACITY,
			this, 1F, 0.0F, 1F, 0.1F);

	private ScreenWrapper wrapper = new ScreenWrapper();
	private ChunkAtlas chunkAtlas;

	public MinimapMod() {
		super("mod.minimap.name", "mod.minimap.description", Icon.MAP);

		chunkAtlas = new ChunkAtlas(10);
	}

	@EventHandler
	public void onRenderGameOverlay(RenderGameOverlayEvent event) {

		NanoVGHelper nvg = NanoVGHelper.getInstance();
		float posX = position.getX();
		float posY = position.getY();
		float width = widthSetting.getValue();
		float height = heightSetting.getValue();
		float scale = position.getScale();

		nvg.setupAndDraw(() -> {
			nvg.drawShadow(posX, posY, width * scale, height * scale, 8 * scale);
			renderer.drawRoundedRect(0, 0, width, height, 8, new Color(138, 176, 254));
		});

		GlStateManager.enableTexture2D();
		GlStateManager.disableBlend();

		wrapper.wrap(() -> {
			nvg.setupAndDraw(() -> drawMap(event.getPartialTicks()));
		}, posX, posY, width * scale, height * scale, 8 * scale, 1F, alphaSetting.getValue(), true);

		position.setSize(width, height);
	}

	private void drawMap(float partialTicks) {

		float posX = position.getX();
		float posY = position.getY();
		float width = widthSetting.getValue();
		float height = heightSetting.getValue();
		float scale = position.getScale();

		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldRenderer = tessellator.getWorldRenderer();
		EntityPlayer p = mc.thePlayer;

		double x = lerp(p.prevPosX, p.posX, partialTicks);
		double z = lerp(p.prevPosZ, p.posZ, partialTicks);
		double yaw = lerp(p.prevRotationYaw, p.rotationYaw, partialTicks);

		chunkAtlas.loadChunks((int) x >> 4, (int) z >> 4);

		GlUtils.startTranslate(posX + (width / 2) * scale, posY + (height / 2) * scale);

		GL11.glRotated(180 - yaw, 0, 0, 1);

		GlStateManager.color(1F, 1F, 1F);
		GlStateManager.enableTexture2D();
		GlStateManager.enableAlpha();
		GlStateManager.bindTexture(chunkAtlas.getTextureHandle());

		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

		worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);

		double chunkWidth = chunkAtlas.getSpriteWidth();
		double chunkHeight = chunkAtlas.getSpriteHeight();

		for (ChunkTile sprite : chunkAtlas) {

			double minX = chunkAtlas.getSpriteX(sprite.getOffset());
			double minY = chunkAtlas.getSpriteY(sprite.getOffset());

			double maxX = minX + chunkWidth;
			double maxY = minY + chunkHeight;

			double renderX = (sprite.getChunkX() << 4) - x;
			double renderY = (sprite.getChunkZ() << 4) - z;

			worldRenderer.pos(renderX, renderY, 0).tex(minX, minY).endVertex();
			worldRenderer.pos(renderX, renderY + 16, 0).tex(minX, maxY).endVertex();
			worldRenderer.pos(renderX + 16, renderY + 16, 0).tex(maxX, maxY).endVertex();
			worldRenderer.pos(renderX + 16, renderY + 0, 0).tex(maxX, minY).endVertex();
		}

		tessellator.draw();

		GlUtils.stopTranslate();
	}

	private double lerp(double prev, double current, float partialTicks) {
		return prev + (current - prev) * partialTicks;
	}

	@EventHandler
	public void onLoadWorld(LoadWorldEvent event) {
		chunkAtlas.clear();
	}

	@Override
	public void onEnable() {
		super.onEnable();
		chunkAtlas.clear();
	}
}
