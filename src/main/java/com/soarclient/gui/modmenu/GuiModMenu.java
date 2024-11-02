package com.soarclient.gui.modmenu;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;

import com.soarclient.Soar;
import com.soarclient.animation.Animation;
import com.soarclient.animation.Duration;
import com.soarclient.animation.cubicbezier.impl.EaseEmphasizedDecelerate;
import com.soarclient.gui.SoarGui;
import com.soarclient.gui.component.Component;
import com.soarclient.gui.component.impl.NavigationRail;
import com.soarclient.gui.edithud.GuiEditHUD;
import com.soarclient.gui.modmenu.pages.HomePage;
import com.soarclient.gui.modmenu.pages.ModsPage;
import com.soarclient.gui.modmenu.pages.MusicPage;
import com.soarclient.gui.modmenu.pages.ProfilePage;
import com.soarclient.gui.modmenu.pages.SettingsPage;
import com.soarclient.management.color.api.ColorPalette;
import com.soarclient.nanovg.NanoVGHelper;
import com.soarclient.shaders.blur.GaussianBlur;
import com.soarclient.shaders.screen.ScreenWrapper;

import net.minecraft.client.gui.ScaledResolution;

public class GuiModMenu extends SoarGui {

	private List<Component> components = new ArrayList<>();
	private NavigationRail navigationRail;
	private float x, y, width, height;

	private ScreenWrapper screenWrapper = new ScreenWrapper();
	private GaussianBlur gaussianBlur = new GaussianBlur(false);

	private Animation animation;

	@Override
	public void init() {

		components.clear();

		width = 938;
		height = 580;
		x = (mc.displayWidth / 2) - (width / 2);
		y = (mc.displayHeight / 2) - (height / 2);

		navigationRail = new NavigationRail(x, y, 80, height);
		navigationRail.add(new HomePage(x + 80, y, width - 80, height));
		navigationRail.add(new ModsPage(x + 80, y, width - 80, height));
		navigationRail.add(new MusicPage(x + 80, y, width - 80, height));
		navigationRail.add(new ProfilePage(x + 80, y, width - 80, height));
		navigationRail.add(new SettingsPage(x + 80, y, width - 80, height));
		navigationRail.initPage();

		components.add(navigationRail);
		animation = new EaseEmphasizedDecelerate(Duration.EXTRA_LONG_1, 0, 1);
	}

	@Override
	public void draw(int mouseX, int mouseY) {

		ScaledResolution sr = new ScaledResolution(mc);
		ColorPalette palette = Soar.getInstance().getColorManager().getPalette();
		NanoVGHelper nvg = NanoVGHelper.getInstance();

		int factor = sr.getScaleFactor();

		gaussianBlur.draw(1 + (24 * animation.getValue()));

		screenWrapper.wrap(() -> {
			nvg.setupAndDraw(() -> {

				nvg.drawRoundedRect(x, y, width, height, 45, palette.getSurfaceContainer());

				for (Component c : components) {
					c.draw(mouseX * factor, mouseY * factor);
				}
				
				navigationRail.getCurrentPage().draw(mouseX * factor, mouseY * factor);
			}, false);
		}, x, y, width, height, 45, 2.0F - animation.getValue(), animation.getValue());

		if(navigationRail.isMoveEdit() && animation.getEnd() == 1) {
			animation = new EaseEmphasizedDecelerate(Duration.EXTRA_LONG_1, 1, 0);
		}
		
		if (animation.isFinished() && animation.getEnd() == 0) {
			mc.displayGuiScreen(navigationRail.isMoveEdit() ? new GuiEditHUD(mc.currentScreen).create() : null);
		}
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {

		ScaledResolution sr = new ScaledResolution(mc);
		int factor = sr.getScaleFactor();

		for (Component c : components) {
			c.mouseClicked(mouseX * factor, mouseY * factor, mouseButton);
		}
		
		navigationRail.getCurrentPage().mouseClicked(mouseX * factor, mouseY * factor, mouseButton);
	}

	@Override
	public void keyTyped(char typedCHar, int keyCode) {
		if (keyCode == Keyboard.KEY_ESCAPE) {
			animation = new EaseEmphasizedDecelerate(Duration.EXTRA_LONG_1, 1, 0);
		}
	}
}
