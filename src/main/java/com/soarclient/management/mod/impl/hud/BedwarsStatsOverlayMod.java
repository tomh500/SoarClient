package com.soarclient.management.mod.impl.hud;

import com.soarclient.Soar;
import com.soarclient.event.EventBus;
import com.soarclient.event.client.RenderSkiaEvent;
import com.soarclient.management.hypixel.api.HypixelUser;
import com.soarclient.management.mod.api.hud.HUDMod;
import com.soarclient.management.mod.settings.impl.NumberSetting;
import com.soarclient.skia.Skia;
import com.soarclient.skia.font.Fonts;
import com.soarclient.skia.font.Icon;

import net.minecraft.client.network.PlayerListEntry;

public class BedwarsStatsOverlayMod extends HUDMod {

	private NumberSetting maxSetting = new NumberSetting("setting.max", "setting.max.description", Icon.MAXIMIZE, this,
			16, 1, 30, 1);

	private int index;

	public BedwarsStatsOverlayMod() {
		super("mod.bedwarsstatsoverlay.name", "mod.bedwarsstatsoverlay.description", Icon.SINGLE_BED);
	}

	public final EventBus.EventListener<RenderSkiaEvent> onRenderSkia = event -> {

		int prevIndex = 0;
		int offsetY = 34;

		this.drawBackground(getX(), getY(), 294, (index * 15) + 36);

		this.drawText("Bedwars Stats", getX() + 5.5F, getY() + 6F, Fonts.getRegular(10.5F));
		Skia.drawRect(getX(), getY() + 18, 292, 1, this.getDesign().getTextColor());

		Skia.drawCenteredText("Name", getX() + 45, getY() + 23, this.getDesign().getTextColor(), Fonts.getMedium(9.5F));
		Skia.drawCenteredText("Level", getX() + 120, getY() + 23, this.getDesign().getTextColor(),
				Fonts.getMedium(9.5F));
		Skia.drawCenteredText("WLR", getX() + 170, getY() + 23, this.getDesign().getTextColor(), Fonts.getMedium(9.5F));
		Skia.drawCenteredText("FKDR", getX() + 220, getY() + 23, this.getDesign().getTextColor(),
				Fonts.getMedium(9.5F));
		Skia.drawCenteredText("BBLR", getX() + 270, getY() + 23, this.getDesign().getTextColor(),
				Fonts.getMedium(9.5F));

		if (client.getCurrentServerEntry() != null && client.getCurrentServerEntry().address.contains("hypixel")) {
			
			for (PlayerListEntry player : client.getNetworkHandler().getPlayerList()) {

				if (player.getProfile() == null) {
					continue;
				}

				String name = player.getProfile().getName();
				String uuid = player.getProfile().getId().toString().replace("-", "");
				HypixelUser hypixelUser = Soar.getInstance().getHypixelManager().getByUuid(uuid);

				if (hypixelUser != null) {

					// renderer.drawPlayerHead(player.getSkinTextures().texture(), 5.5F, offsetY,
					// 12, 12, 2.5F);
					Skia.drawText(name, getX() + 20, getY() + offsetY + 2.5F, this.getDesign().getTextColor(), Fonts.getRegular(9));

					Skia.drawCenteredText(hypixelUser.getBedwarsLevel(), getX() + 120, getY() + offsetY + 2.5F,
							this.getDesign().getTextColor(), Fonts.getRegular(9));
					Skia.drawCenteredText(hypixelUser.getWinLoseRatio(), getX() + 170, getY() + offsetY + 2.5F,
							this.getDesign().getTextColor(), Fonts.getRegular(9));
					Skia.drawCenteredText(hypixelUser.getFinalKillDeathRatio(), getX() + 220, getY() + offsetY + 2.5F,
							this.getDesign().getTextColor(), Fonts.getRegular(9));
					Skia.drawCenteredText(hypixelUser.getBedsBrokeLostRatio(), getX() + 270, getY() + offsetY + 2.5F,
							this.getDesign().getTextColor(), Fonts.getRegular(9));

					if (prevIndex > maxSetting.getValue()) {
						prevIndex++;
						index = prevIndex;
						break;
					}

					prevIndex++;
					offsetY += 15;
				}

				index = prevIndex;
			}
		}

		position.setSize(294, (index * 15) + 36);
	};

	@Override
	public float getRadius() {
		return 6;
	}
}
