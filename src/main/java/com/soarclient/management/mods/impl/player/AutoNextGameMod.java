package com.soarclient.management.mods.impl.player;

import java.util.concurrent.TimeUnit;

import com.soarclient.event.EventHandler;
import com.soarclient.event.impl.PlayerUpdateEvent;
import com.soarclient.event.impl.ReceivePacketEvent;
import com.soarclient.event.impl.SendChatEvent;
import com.soarclient.event.impl.SendPacketEvent;
import com.soarclient.management.mods.Mod;
import com.soarclient.management.mods.ModCategory;
import com.soarclient.management.mods.api.HypixelGameMode;
import com.soarclient.management.mods.settings.impl.BooleanSetting;
import com.soarclient.management.mods.settings.impl.NumberSetting;
import com.soarclient.nanovg.font.Icon;
import com.soarclient.utils.ColorUtils;
import com.soarclient.utils.Multithreading;
import com.soarclient.utils.network.ServerUtils;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C0EPacketClickWindow;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S2FPacketSetSlot;
import net.minecraft.network.play.server.S45PacketTitle;
import net.minecraft.scoreboard.Scoreboard;

public class AutoNextGameMod extends Mod {

	private NumberSetting delaySetting = new NumberSetting("setting.delay", "setting.delay.description", Icon.TIMER,
			this, 0, 0, 5, 1);
	private BooleanSetting hypixelSetting = new BooleanSetting("setting.hypixel", "setting.hypixel.description",
			Icon.DNS, this, true);
	private BooleanSetting minemenSetting = new BooleanSetting("setting.minemen", "setting.minemen.description",
			Icon.DNS, this, true);

	private HypixelGameMode currentMode;

	public AutoNextGameMod() {
		super("mod.autonextgame.name", "mod.autonextgame.description", Icon.AUTORENEW, ModCategory.PLAYER);

		currentMode = HypixelGameMode.SKYWARS_SOLO_NORMAL;
	}

	@EventHandler
	public void onPlayerUpdate(PlayerUpdateEvent event) {

		if (!ServerUtils.isHypixel() || !hypixelSetting.isEnabled()) {
			return;
		}

		Scoreboard scoreboard = mc.theWorld.getScoreboard();

		if (scoreboard != null && scoreboard.getObjectiveInDisplaySlot(1) != null) {

			String title = ColorUtils.removeColorCode(scoreboard.getObjectiveInDisplaySlot(1).getDisplayName());

			if (title.contains("TNT RUN")) {
				currentMode = HypixelGameMode.TNT_RUN;
			}

			if (title.contains("BOW SPLEEF")) {
				currentMode = HypixelGameMode.BOW_SPLEEF;
			}

			if (title.contains("PVP RUN")) {
				currentMode = HypixelGameMode.PVP_RUN;
			}

			if (title.contains("TNT TAG")) {
				currentMode = HypixelGameMode.TNT_TAG;
			}

			if (title.contains("TNT WIZARDS")) {
				currentMode = HypixelGameMode.TNT_WIZARDS;
			}
		}
	}

	@EventHandler
	public void onSendChat(SendChatEvent event) {

		if (!ServerUtils.isHypixel() || !hypixelSetting.isEnabled()) {
			return;
		}

		String message = event.getMessage();

		if (message.startsWith("/play")) {

			HypixelGameMode mode = HypixelGameMode.getModeByCommand(message);

			if (mode != null) {
				currentMode = mode;
			}
		}
	}

	@EventHandler
	public void onReceivePacket(ReceivePacketEvent event) {

		if (minemenSetting.isEnabled() && event.getPacket() instanceof S02PacketChat) {

			S02PacketChat chatPacket = (S02PacketChat) event.getPacket();
			String raw = chatPacket.getChatComponent().toString();

			if (raw.contains("clickEvent=ClickEvent{action=RUN_COMMAND, value='/requeue")) {
				Multithreading.schedule(() -> {
					mc.thePlayer.sendChatMessage("/requeue");
				}, (long) delaySetting.getValue(), TimeUnit.SECONDS);
			}
		}

		if (hypixelSetting.isEnabled()) {

			if (event.getPacket() instanceof S2FPacketSetSlot) {

				S2FPacketSetSlot slotPacket = (S2FPacketSetSlot) event.getPacket();
				ItemStack stack = slotPacket.func_149174_e();

				if (stack != null && stack.getItem().equals(Items.paper)
						&& (HypixelGameMode.isBedwars(currentMode) || HypixelGameMode.isTntGames(currentMode))) {
					sendNextGame();
					return;
				}
			}

			if (event.getPacket() instanceof S45PacketTitle) {

				S45PacketTitle titlePacket = (S45PacketTitle) event.getPacket();

				if (titlePacket.getMessage() != null) {

					String title = titlePacket.getMessage().getFormattedText();

					if (title.startsWith("\2476\247l") && title.endsWith("\247r")
							|| title.startsWith("\247c\247lY") && title.endsWith("\247r")) {
						sendNextGame();
					}
				}
			}
		}
	}

	@EventHandler
	public void onSendPacket(SendPacketEvent event) {

		if (!ServerUtils.isHypixel() || !hypixelSetting.isEnabled()) {
			return;
		}

		if (event.getPacket() instanceof C0EPacketClickWindow) {

			C0EPacketClickWindow packet = (C0EPacketClickWindow) event.getPacket();
			String itemname;

			if (packet.getClickedItem() == null) {
				return;
			}

			itemname = packet.getClickedItem().getDisplayName();

			if (packet.getClickedItem().getDisplayName().startsWith("\247a")) {

				int itemID = Item.getIdFromItem(packet.getClickedItem().getItem());

				if (itemID == 381 || itemID == 368) {
					if (itemname.contains("SkyWars")) {
						if (itemname.contains("Doubles")) {
							if (itemname.contains("Normal")) {
								currentMode = HypixelGameMode.SKYWARS_DOUBLES_NORMAL;
							} else if (itemname.contains("Insane")) {
								currentMode = HypixelGameMode.SKYWARS_DOUBLES_INSANE;
							}
						} else if (itemname.contains("Solo")) {
							if (itemname.contains("Normal")) {
								currentMode = HypixelGameMode.SKYWARS_SOLO_NORMAL;
							} else if (itemname.contains("Insane")) {
								currentMode = HypixelGameMode.SKYWARS_SOLO_INSANE;
							}
						}
					}
				} else if (itemID == 355) {
					if (itemname.contains("Bed Wars")) {
						if (itemname.contains("4v4")) {
							currentMode = HypixelGameMode.BEDWARS_4V4;
						} else if (itemname.contains("3v3")) {
							currentMode = HypixelGameMode.BEDWARS_3V3;
						} else if (itemname.contains("Doubles")) {
							currentMode = HypixelGameMode.BEDWARS_DOUBLES;
						} else if (itemname.contains("Solo")) {
							currentMode = HypixelGameMode.BEDWARS_SOLO;
						}
					}
				} else if (itemID == 397) {
					if (itemname.contains("UHC Duel")) {
						if (itemname.contains("1v1")) {
							currentMode = HypixelGameMode.UHC_DUEL_1V1;
						} else if (itemname.contains("2v2")) {
							currentMode = HypixelGameMode.UHC_DUEL_2V2;
						} else if (itemname.contains("4v4")) {
							currentMode = HypixelGameMode.UHC_DUEL_4V4;
						} else if (itemname.contains("Player FFA")) {
							currentMode = HypixelGameMode.UHC_DUEL_MEETUP;
						}
					}
				}
			}
		}
	}

	private void sendNextGame() {
		Multithreading.schedule(() -> {
			mc.thePlayer.sendChatMessage(currentMode.getCommand());
		}, (long) delaySetting.getValue(), TimeUnit.SECONDS);
	}
}
