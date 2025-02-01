package com.soarclient.libraries.soarium.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.soarclient.libraries.soarium.Soarium;
import com.soarclient.libraries.soarium.config.SoariumConfig.QualitySettings;
import com.soarclient.libraries.soarium.gui.component.GuiSoariumComboBox;
import com.soarclient.libraries.soarium.gui.component.GuiSoariumSlider;
import com.soarclient.libraries.soarium.gui.component.GuiSoariumSwitch;
import com.soarclient.libraries.soarium.gui.component.handler.SoariumComboBoxHandler;
import com.soarclient.libraries.soarium.gui.component.handler.SoariumSliderHandler;
import com.soarclient.libraries.soarium.gui.component.handler.SoariumSwitchHandler;
import com.soarclient.libraries.soarium.gui.locale.SoariumI18n;
import com.soarclient.libraries.soarium.gui.utils.SoariumGui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiOptionButton;
import net.minecraft.client.gui.GuiOptionSlider;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.settings.GameSettings;

public class GuiQualitySettings extends GuiScreen {

	private static GameSettings.Options[] OPTIONS = new GameSettings.Options[] { GameSettings.Options.GAMMA,
			GameSettings.Options.ENTITY_SHADOWS };

	private GuiScreen prevScreen;

	public GuiQualitySettings(GuiScreen prevScreen) {
		this.prevScreen = prevScreen;
	}

	@Override
	public void initGui() {
		this.buttonList.clear();

		for (int i = 0; i < OPTIONS.length; ++i) {
			GameSettings.Options gamesettings$options = OPTIONS[i];

			if (gamesettings$options != null) {
				int j = this.width / 2 - 155 + i % 2 * 160;
				int k = this.height / 6 + 21 * (i / 2) - 12;

				if (gamesettings$options.getEnumFloat()) {
					this.buttonList.add(
							new GuiOptionSlider(gamesettings$options.returnEnumOrdinal(), j, k, gamesettings$options));
				} else {
					this.buttonList.add(new GuiOptionButton(gamesettings$options.returnEnumOrdinal(), j, k,
							gamesettings$options, mc.gameSettings.getKeyBinding(gamesettings$options)));
				}
			}
		}

		int x = this.width / 2 - 155 + 0;
		int y = this.height / 6 + 21 * (OPTIONS.length / 2) - 12;

		List<GuiButton> options = new ArrayList<>();

		QualitySettings settings = Soarium.getConfig().quality;

		options.add(new GuiSoariumSwitch(0, 0, SoariumI18n.get("soarium.enableClouds"), settings.enableClouds,
				new SoariumSwitchHandler() {

					@Override
					public void onAction(boolean state) {
						settings.enableClouds = state;
					}
				}));

		options.add(new GuiSoariumSlider(0, 0, SoariumI18n.get("soarium.cloudHeight"), settings.cloudHeight, 1, 230,
				new SoariumSliderHandler() {

					@Override
					public void onValueChanged(int value) {
						settings.cloudHeight = value;
					}
				}));

		options.add(new GuiSoariumComboBox(0, 0, SoariumI18n.get("soarium.cloudQuality"), settings.cloudQuality,
				settings.qualities, new SoariumComboBoxHandler() {

					@Override
					public void onAction(String value) {
						settings.cloudQuality = value;
					}
				}));

		options.add(new GuiSoariumComboBox(0, 0, SoariumI18n.get("soarium.weatherQuality"), settings.weatherQuality,
				settings.qualities, new SoariumComboBoxHandler() {

					@Override
					public void onAction(String value) {
						settings.weatherQuality = value;
					}
				}));

		options.add(new GuiSoariumComboBox(0, 0, SoariumI18n.get("soarium.leavesQuality"), settings.leavesQuality,
				settings.qualities, new SoariumComboBoxHandler() {

					@Override
					public void onAction(String value) {
						settings.leavesQuality = value;
					}
				}));

		options.add(new GuiSoariumComboBox(0, 0, SoariumI18n.get("soarium.smoothLighting"), settings.smoothLighting,
				settings.lightningQualities, new SoariumComboBoxHandler() {

					@Override
					public void onAction(String value) {
						settings.smoothLighting = value;
						Minecraft.getMinecraft().renderGlobal.loadRenderers();
					}
				}));

		options.add(new GuiSoariumSlider(0, 0, SoariumI18n.get("soarium.biomeBlendRadius"), settings.biomeBlendRadius,
				1, 7, new SoariumSliderHandler() {

					@Override
					public void onValueChanged(int value) {
						settings.biomeBlendRadius = value;
					}
				}));

		options.add(new GuiSoariumSwitch(0, 0, SoariumI18n.get("options.entityShadows"), mc.gameSettings.entityShadows,
				new SoariumSwitchHandler() {

					@Override
					public void onAction(boolean state) {
						mc.gameSettings.entityShadows = state;
					}
				}));

		options.add(new GuiSoariumSwitch(0, 0, SoariumI18n.get("soarium.enableVignette"), settings.enableVignette,
				new SoariumSwitchHandler() {

					@Override
					public void onAction(boolean state) {
						settings.enableVignette = state;
					}
				}));

		this.buttonList.addAll(SoariumGui.sortButtons(options, x, y, OPTIONS.length));
		this.buttonList
				.add(new GuiButton(200, this.width / 2 - 100, this.height / 6 + 168 + 11, SoariumI18n.get("gui.done")));
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		this.drawCenteredString(this.fontRendererObj, SoariumI18n.get("soarium.quality"), this.width / 2, 15, 16777215);
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	@Override
	public void actionPerformed(GuiButton button) throws IOException {

		if (button.id < 200 && button instanceof GuiOptionButton) {
			mc.gameSettings.setOptionValue(((GuiOptionButton) button).returnEnumOptions(), 1);
			button.displayString = mc.gameSettings.getKeyBinding(GameSettings.Options.getEnumOptions(button.id));
		}

		if (button.id == 200) {
			this.mc.displayGuiScreen(this.prevScreen);
		}
	}
}
