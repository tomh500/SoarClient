package com.soarclient.management.mods.impl.hud;

import java.util.Arrays;

import com.soarclient.event.EventHandler;
import com.soarclient.event.impl.RenderBlurEvent;
import com.soarclient.event.impl.RenderGameOverlayEvent;
import com.soarclient.gui.edithud.api.HUDCore;
import com.soarclient.management.mods.api.HUDMod;
import com.soarclient.management.mods.settings.impl.ComboSetting;
import com.soarclient.nanovg.NanoVGHelper;
import com.soarclient.nanovg.font.Icon;
import com.soarclient.utils.render.GlUtils;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class ArmorStatusMod extends HUDMod {

	private ComboSetting directionSetting = new ComboSetting("setting.direction", "setting.direction.description",
			Icon.OPEN_WITH, this, Arrays.asList("setting.vertical", "setting.horizontal"), "setting.horizontal");
	private ComboSetting designSetting = new ComboSetting("setting.design", "setting.design.description", Icon.BRUSH,
			this, Arrays.asList("setting.simple", "setting.fancy"), "setting.fancy");

	public ArmorStatusMod() {
		super("mod.armorstatus.name", "mod.armorstatus.description", Icon.SHIELD);
	}

	@EventHandler
	public void onRenderGameOverlay(RenderGameOverlayEvent event) {

		NanoVGHelper nvg = NanoVGHelper.getInstance();

		boolean vertical = directionSetting.getOption().equals("setting.vertical");
		boolean fancy = designSetting.getOption().equals("setting.fancy");
		int margin = fancy ? 16 : 16;

		ItemStack[] fakeStack = new ItemStack[4];

		fakeStack[3] = new ItemStack(Items.diamond_helmet);
		fakeStack[2] = new ItemStack(Items.diamond_chestplate);
		fakeStack[1] = new ItemStack(Items.diamond_leggings);
		fakeStack[0] = new ItemStack(Items.diamond_boots);

		if (fancy) {
			nvg.setupAndDraw(() -> {
				renderer.drawBackground(vertical ? margin : margin * 4, vertical ? margin * 4 : margin);
			});
		}

		GlUtils.startScale(position.getX(), position.getY(), position.getScale());
		this.renderItems(HUDCore.isEditing ? fakeStack : mc.thePlayer.inventory.armorInventory, vertical, margin);
		GlUtils.stopScale();

		if (fancy) {
			position.setSize(vertical ? margin : margin * 4, vertical ? margin * 4 : margin);
		} else {
			position.setSize(vertical ? margin : margin * 4, vertical ? margin * 4 : margin);
		}
	}

	@Override
	@EventHandler
	public void onRenderBlur(RenderBlurEvent event) {

		boolean fancy = designSetting.getOption().equals("setting.fancy");

		if (fancy) {
			super.onRenderBlur(event);
		}
	}

	private void renderItems(ItemStack[] items, boolean vertical, int margin) {

		for (int i = 0; i < 4; i++) {

			ItemStack item = items[Math.abs(3 - i)];
			int addX = vertical ? 0 : margin * i;
			int addY = vertical ? margin * i : 0;

			if (item != null) {
				this.drawItemStack(item, (int) (position.getX() + addX), (int) (position.getY() + addY));
			}
		}
	}

	private void drawItemStack(ItemStack stack, int x, int y) {

		GlStateManager.pushMatrix();
		RenderHelper.enableGUIStandardItemLighting();
		RenderItem itemRender = mc.getRenderItem();

		GlStateManager.translate(0, 0, 32);
		float prevZ = itemRender.zLevel;

		itemRender.zLevel = 0.0F;

		itemRender.renderItemAndEffectIntoGUI(stack, x, y);

		itemRender.renderItemOverlayIntoGUI(fr, stack, x, y, "");

		RenderHelper.disableStandardItemLighting();
		GlStateManager.disableLighting();
		GlStateManager.popMatrix();
		itemRender.zLevel = prevZ;
	}
}
