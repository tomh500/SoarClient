package com.soarclient.gui.modmenu;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;

import com.soarclient.event.impl.RenderBlurEvent;
import com.soarclient.gui.Page;
import com.soarclient.gui.SoarGui;
import com.soarclient.gui.modmenu.pages.HomePage;
import com.soarclient.gui.modmenu.pages.ModsPage;
import com.soarclient.gui.modmenu.pages.MusicPage;
import com.soarclient.gui.modmenu.pages.ProfilePage;
import com.soarclient.gui.modmenu.pages.SettingsPage;
import com.soarclient.gui.modmenu.toolbar.Toolbar;
import com.soarclient.nanovg.NanoVGHelper;
import com.soarclient.shaders.blur.GaussianBlur;
import com.soarclient.ui.UIRenderer;
import com.soarclient.ui.math.Style;

import net.minecraft.client.gui.ScaledResolution;

public class GuiModMenu extends SoarGui {

	private GaussianBlur blur = new GaussianBlur(true);
	private Toolbar toolbar;
	private float x, y, width, height;

	@Override
	public void init() {

		ScaledResolution sr = new ScaledResolution(mc);

		width = Style.px(1280);
		height = Style.px(720);
		x = (sr.getScaledWidth() / 2) - (width / 2);
		y = (sr.getScaledHeight() / 2) - (height / 2);

		float tWidth = Style.px(68);

		List<Page> pages = new ArrayList<>();

		pages.add(new HomePage(x, y, width, height));
		pages.add(new ModsPage(x, y, width, height));
		pages.add(new MusicPage(x, y, width, height));
		pages.add(new ProfilePage(x, y, width, height));
		pages.add(new SettingsPage(x, y, width, height));

		toolbar = new Toolbar(pages, x - tWidth - Style.px(32), y + (height / 2), tWidth);
	}

	@Override
	public void draw(int mouseX, int mouseY) {

		NanoVGHelper nvg = NanoVGHelper.getInstance();

		blur.draw(65);

		nvg.setupAndDraw(() -> {
			UIRenderer.drawWindow(x, y, width, height, Style.px(45));
			toolbar.draw(mouseX, mouseY);
		});
	}

	@Override
	public void drawBlur(RenderBlurEvent event) {
		event.setupAndDraw(() -> {
			event.drawRoundedRect(x, y, width, height, Style.px(45), 1.0F);
			toolbar.drawBlur(event);
		});
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		toolbar.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	public void keyTyped(char typedChar, int keyCode) {

		if (keyCode == Keyboard.KEY_ESCAPE) {
			mc.displayGuiScreen(null);
		}
	}
}
