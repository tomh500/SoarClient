package com.soarclient.gui.mainmenu.pages;

import java.util.ArrayList;
import java.util.List;

import com.soarclient.gui.Page;
import com.soarclient.gui.component.handler.impl.ButtonHandler;
import com.soarclient.gui.mainmenu.component.MainMenuButton;
import com.soarclient.management.account.AccountAuth;
import com.soarclient.nanovg.font.Icon;
import com.soarclient.utils.Multithreading;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.client.gui.ScaledResolution;

public class MainPage extends Page {

	private List<MainMenuButton> buttons = new ArrayList<>();

	public MainPage() {
		super("", "", 0, 0, 0, 0);
	}

	@Override
	public void init() {

		buttons.clear();

		ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
		int y = sr.getScaledHeight() / 4 + 56;
		int space = 24;

		MainMenuButton singleplayer = new MainMenuButton("text.singleplayer", Icon.PERSON,
				(sr.getScaledWidth() / 2) - 100, y, 200);
		MainMenuButton multiplayer = new MainMenuButton("text.multiplayer", Icon.GROUPS,
				(sr.getScaledWidth() / 2) - 100, y + space, 200);
		MainMenuButton accountManager = new MainMenuButton("text.accountmanager", Icon.ACCOUNT_CIRCLE,
				(sr.getScaledWidth() / 2) - 100, y + (space * 2), 200);
		MainMenuButton settings = new MainMenuButton("text.settings", Icon.SETTINGS,
				(sr.getScaledWidth() / 2) - 100, y + (space * 3), 98);
		MainMenuButton exit = new MainMenuButton("text.exit", Icon.EXIT_TO_APP, (sr.getScaledWidth() / 2) + 2,
				y + (space * 3), 98);

		singleplayer.setHandler(new ButtonHandler() {

			@Override
			public void onClicked() {
				mc.displayGuiScreen(new GuiSelectWorld(mc.currentScreen));
			}
		});

		multiplayer.setHandler(new ButtonHandler() {

			@Override
			public void onClicked() {
				mc.displayGuiScreen(new GuiMultiplayer(mc.currentScreen));
			}
		});

		accountManager.setHandler(new ButtonHandler() {

			@Override
			public void onClicked() {
				Multithreading.runAsync(() -> {
					AccountAuth.handleMicrosoftLogin();
				});
			}
		});

		settings.setHandler(new ButtonHandler() {

			@Override
			public void onClicked() {
				mc.displayGuiScreen(new GuiOptions(mc.currentScreen, mc.gameSettings));
			}
		});

		exit.setHandler(new ButtonHandler() {

			@Override
			public void onClicked() {
				mc.shutdown();
			}
		});

		buttons.add(singleplayer);
		buttons.add(multiplayer);
		buttons.add(accountManager);
		buttons.add(settings);
		buttons.add(exit);
	}

	@Override
	public void draw(int mouseX, int mouseY) {
		for (MainMenuButton b : buttons) {
			b.draw(mouseX, mouseY);
		}
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		for (MainMenuButton b : buttons) {
			b.mouseClicked(mouseX, mouseY, mouseButton);
		}
	}
}
