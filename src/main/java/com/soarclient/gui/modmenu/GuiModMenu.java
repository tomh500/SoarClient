package com.soarclient.gui.modmenu;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;

import com.soarclient.Soar;
import com.soarclient.animation.Animation;
import com.soarclient.animation.Duration;
import com.soarclient.animation.cubicbezier.impl.EaseEmphasizedDecelerate;
import com.soarclient.animation.other.DummyAnimation;
import com.soarclient.gui.api.page.Page;
import com.soarclient.gui.api.page.PageGui;
import com.soarclient.gui.modmenu.pages.HomePage;
import com.soarclient.gui.modmenu.pages.ModsPage;
import com.soarclient.gui.modmenu.pages.MusicPage;
import com.soarclient.gui.modmenu.pages.ProfilePage;
import com.soarclient.gui.modmenu.pages.SettingsPage;
import com.soarclient.management.color.api.ColorPalette;
import com.soarclient.shaders.impl.GaussianBlur;
import com.soarclient.skia.Skia;
import com.soarclient.ui.component.Component;

import net.minecraft.client.gui.GuiScreen;

public class GuiModMenu extends PageGui {

	private GaussianBlur blur = new GaussianBlur(false);
	
	private Animation animation;
	private NavigationRail navigationRail;
	private GuiScreen nextScreen;
	
	public GuiModMenu() {
		animation = new DummyAnimation(1, 1);
	}
	
	@Override
	public void init() {

		components.clear();
		
		navigationRail = new NavigationRail(this, getX(), getY(), 90, getHeight());
		components.add(navigationRail);
		
		for (Page p : pages) {
			p.setX(getX() + navigationRail.getWidth());
			p.setY(getY());
			p.setWidth(getWidth() - navigationRail.getHeight());
			p.setHeight(getHeight());
		}
		
		animation = new EaseEmphasizedDecelerate(Duration.EXTRA_LONG_1, 0, 1);
		currentPage.init();
	}

	@Override
	public void drawOpenGL(int mouseX, int mouseY) {
		blur.draw(1 + (20 * animation.getValue()));
	}
	
	@Override
	public void draw(int mouseX, int mouseY) {

		ColorPalette palette = Soar.getInstance().getColorManager().getPalette();

		Skia.setAlpha((int) (animation.getValue() * 255));
		Skia.scale(0, 0, mc.displayWidth, mc.displayHeight, 2 - animation.getValue());
		
		Skia.drawRoundedRect(getX(), getY(), getWidth(), getHeight(), 35, palette.getSurfaceContainer());
		Skia.save();
		
		Skia.clip(getX(), getY(), getWidth(), getHeight(), 35);
		
		if (currentPage != null) {
			currentPage.draw(mouseX, mouseY);
		}
		
		for (Component c : components) {
			c.draw(mouseX, mouseY);
		}
		
		Skia.restore();
		
		if (animation.getEnd() == 0 && animation.isFinished()) {
			mc.displayGuiScreen(nextScreen);
			nextScreen = null;
		}
	}
	
	@Override
	public void keyTyped(char typedChar, int keyCode) {
		
		if (keyCode == Keyboard.KEY_ESCAPE && animation.getEnd() == 1) {
			animation = new EaseEmphasizedDecelerate(Duration.EXTRA_LONG_1, 1, 0);
		}
		
		super.keyTyped(typedChar, keyCode);
	}

	public void setNextScreen(GuiScreen nextScreen) {
		if(animation.getEnd() == 1) {
			this.nextScreen = nextScreen;
			animation = new EaseEmphasizedDecelerate(Duration.EXTRA_LONG_1, 1, 0);
		}
	}
	
	@Override
	public List<Page> createPages() {

		List<Page> pages = new ArrayList<>();

		pages.add(new HomePage(this));
		pages.add(new ModsPage(this));
		pages.add(new MusicPage(this));
		pages.add(new ProfilePage(this));
		pages.add(new SettingsPage(this));

		return pages;
	}

	@Override
	public float getX() {
		return (mc.displayWidth / 2) - (getWidth() / 2);
	}

	@Override
	public float getY() {
		return (mc.displayHeight / 2) - (getHeight() / 2);
	}

	@Override
	public float getWidth() {
		return 938;
	}

	@Override
	public float getHeight() {
		return 580;
	}
}
