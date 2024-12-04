package com.soarclient.management.mods.impl.player;

import java.util.concurrent.TimeUnit;

import com.soarclient.event.EventHandler;
import com.soarclient.event.impl.ReceivePacketEvent;
import com.soarclient.management.mods.Mod;
import com.soarclient.management.mods.ModCategory;
import com.soarclient.management.mods.settings.impl.BooleanSetting;
import com.soarclient.management.mods.settings.impl.NumberSetting;
import com.soarclient.management.mods.settings.impl.StringSetting;
import com.soarclient.nanovg.font.Icon;
import com.soarclient.utils.Multithreading;
import com.soarclient.utils.network.ServerUtils;

import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S45PacketTitle;

public class AutoGGMod extends Mod {

	private NumberSetting delaySetting = new NumberSetting("setting.delay", "setting.delay.description", Icon.TIMER,
			this, 0, 0, 5, 1);
	private StringSetting messageSetting = new StringSetting("setting.message", "setting.message.description",
			Icon.TEXT_FIELDS, this, "gg");
	private BooleanSetting hypixelSetting = new BooleanSetting("setting.hypixel", "setting.hypixel.description",
			Icon.TOGGLE_OFF, this, true);

	public AutoGGMod() {
		super("mod.autogg.name", "mod.autogg.description", Icon.TROPHY, ModCategory.PLAYER);
	}

	@EventHandler
	public void onReceivePacket(ReceivePacketEvent event) {

		Packet<?> packet = event.getPacket();

		if (packet instanceof S45PacketTitle) {

			S45PacketTitle titlePacket = (S45PacketTitle) packet;

			if (titlePacket.getMessage() != null) {

				String title = titlePacket.getMessage().getFormattedText();

				if (hypixelSetting.isEnabled() && ServerUtils.isHypixel() && title.startsWith("\2476\247l")
						&& title.endsWith("\247r")) {
					sendMessage(true);
				}
			}
		}
	}

	private void sendMessage(boolean hypixel) {
		Multithreading.schedule(() -> {
			mc.thePlayer.sendChatMessage((hypixel ? "/achat " : "") + messageSetting.getString());
		}, (long) delaySetting.getValue(), TimeUnit.SECONDS);
	}
}
