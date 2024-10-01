package com.soarclient.gui.mainmenu;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Mouse;

import com.soarclient.animation.SimpleAnimation;
import com.soarclient.event.impl.RenderBlurEvent;
import com.soarclient.gui.Page;
import com.soarclient.gui.SoarGui;
import com.soarclient.gui.mainmenu.pages.MainPage;
import com.soarclient.nanovg.NanoVGHelper;
import com.soarclient.shaders.blur.GaussianBlur;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;

public class GuiSoarMainMenu extends SoarGui {

	private GaussianBlur blur;
	private List<Page> pages;
	private Page currentPage;
	private SimpleAnimation[] backgroundAnimations;

	public GuiSoarMainMenu() {
		this.blur = new GaussianBlur(true);
		this.pages = new ArrayList<>();
		this.backgroundAnimations = new SimpleAnimation[2];
		initializeBackgroundAnimations();
	}

	private void initializeBackgroundAnimations() {
		for (int i = 0; i < backgroundAnimations.length; i++) {
			backgroundAnimations[i] = new SimpleAnimation();
		}
	}

	@Override
	public void init() {
		MainPage mainPage = new MainPage();
		pages.add(mainPage);
		currentPage = mainPage;
	}

	@Override
	public void draw(int mouseX, int mouseY) {
		ScaledResolution sr = new ScaledResolution(mc);
		NanoVGHelper nvg = NanoVGHelper.getInstance();

		updateBackgroundAnimations();

		nvg.setupAndDraw(() -> drawBackground(nvg, sr));
		blur.draw(65);

		nvg.setupAndDraw(() -> {
			if (currentPage != null) {
				currentPage.draw(mouseX, mouseY);
			}
		});
	}

	@Override
	public void drawBlur(RenderBlurEvent event) {
		event.setupAndDraw(() -> {
			if (currentPage != null) {
				currentPage.drawBlur(event);
			}
		});
	}
	
	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		if (currentPage != null) {
			currentPage.mouseClicked(mouseX, mouseY, mouseButton);
		}
	}
	
	private void updateBackgroundAnimations() {
		backgroundAnimations[0].onTick(Mouse.getX(), 16);
		backgroundAnimations[1].onTick(Mouse.getY(), 16);
	}

	private void drawBackground(NanoVGHelper nvg, ScaledResolution sr) {
		nvg.drawImage(new ResourceLocation("soar/background.png"), -21 + backgroundAnimations[0].getValue() / 90,
				backgroundAnimations[1].getValue() * -1 / 90, sr.getScaledWidth() + 21, sr.getScaledHeight() + 20);
	}
}
