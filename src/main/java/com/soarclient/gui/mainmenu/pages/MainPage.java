package com.soarclient.gui.mainmenu.pages;

import java.util.ArrayList;
import java.util.List;

import com.soarclient.event.impl.RenderBlurEvent;
import com.soarclient.gui.Page;
import com.soarclient.nanovg.NanoVGHelper;
import com.soarclient.nanovg.font.Fonts;
import com.soarclient.nanovg.font.Icon;
import com.soarclient.ui.UIRenderer;
import com.soarclient.ui.color.Colors;
import com.soarclient.utils.language.I18n;
import com.soarclient.utils.mouse.MouseUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;

public class MainPage extends Page {

	private Minecraft mc = Minecraft.getMinecraft();
	private List<Button> buttons = new ArrayList<>();

	public MainPage() {

		ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
		int y = sr.getScaledHeight() / 4 + 56;
		int space = 24;

		Button singleplayer = new Button("text.singleplayer", Icon.PERSON, (sr.getScaledWidth() / 2) - 100, y, 200,
				() -> {
					mc.displayGuiScreen(new GuiSelectWorld(mc.currentScreen));
				});
		Button multiplayer = new Button("text.multiplayer", Icon.GROUPS, (sr.getScaledWidth() / 2) - 100, y + space,
				200, () -> {
					mc.displayGuiScreen(new GuiMultiplayer(mc.currentScreen));
				});
		Button accountManager = new Button("text.accountmanager", Icon.ACCOUNT_CIRCLE, (sr.getScaledWidth() / 2) - 100,
				y + (space * 2), 200, () -> {

				});
		Button settings = new Button("text.settings", Icon.SETTINGS, (sr.getScaledWidth() / 2) - 100, y + (space * 3),
				98, () -> {
					mc.displayGuiScreen(new GuiOptions(mc.currentScreen, mc.gameSettings));
				});
		Button exit = new Button("text.exit", Icon.EXIT_TO_APP, (sr.getScaledWidth() / 2) + 2, y + (space * 3), 98,
				() -> {
					mc.shutdown();
				});

		buttons.add(singleplayer);
		buttons.add(multiplayer);
		buttons.add(accountManager);
		buttons.add(settings);
		buttons.add(exit);
	}

	@Override
	public void draw(int mouseX, int mouseY) {

		ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
		NanoVGHelper nvg = NanoVGHelper.getInstance();

		float size = 76;

		nvg.drawImage(new ResourceLocation("soar/logo.png"), (sr.getScaledWidth() / 2) - (size / 2),
				sr.getScaledHeight() / 4 + 56 - size - 4, size, size);

		for (Button b : buttons) {
			b.draw();
		}
	}

	@Override
	public void drawBlur(RenderBlurEvent event) {

		for (Button b : buttons) {
			b.drawBlur(event);
		}
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		for (Button b : buttons) {
			b.mouseClicked(mouseX, mouseY, mouseButton);
		}
	}

	private class Button {

		private Runnable runnable;
		private String text, icon;
		private float x, y, width, height;

		private Button(String text, String icon, float x, float y, float width, Runnable runnable) {
			this.text = text;
			this.icon = icon;
			this.runnable = runnable;
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = 20;
		}

		private void draw() {

			NanoVGHelper nvg = NanoVGHelper.getInstance();

			UIRenderer.drawWindow(x, y, width, height, 8);
			nvg.drawCenteredText(I18n.get(text), x + (width / 2), y + 6, Colors.TEXT_PRIMARY, 9, Fonts.REGULAR);
			nvg.drawText(icon, x + 6, y + 5.5F, Colors.TEXT_PRIMARY, 10.5F, Fonts.ICON);
		}

		private void drawBlur(RenderBlurEvent event) {
			event.drawRoundedRect(x, y, width, height, 8, 1F);
		}

		private void mouseClicked(int mouseX, int mouseY, int mouseButton) {

			if (MouseUtils.isInside(mouseX, mouseY, x, y, width, height) && mouseButton == 0) {
				runnable.run();
			}
		}
	}
}
