package com.soarclient.gui.modmenu;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;

import com.soarclient.Soar;
import com.soarclient.animation.Animation;
import com.soarclient.animation.Duration;
import com.soarclient.animation.cubicbezier.impl.EaseEmphasizedDecelerate;
import com.soarclient.animation.other.DummyAnimation;
import com.soarclient.gui.Page;
import com.soarclient.gui.PageDirection;
import com.soarclient.gui.SoarGui;
import com.soarclient.gui.modmenu.pages.impl.HomePage;
import com.soarclient.gui.modmenu.pages.impl.ModsPage;
import com.soarclient.gui.modmenu.pages.impl.MusicPage;
import com.soarclient.gui.modmenu.pages.impl.ProfilePage;
import com.soarclient.gui.modmenu.pages.impl.SettingsPage;
import com.soarclient.management.color.api.ColorPalette;
import com.soarclient.nanovg.NanoVGHelper;
import com.soarclient.shaders.blur.GaussianBlur;
import com.soarclient.shaders.screen.ScreenWrapper;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;

public class GuiModMenu extends SoarGui {

	private List<Page> pages = new ArrayList<>();
	private Animation pageAnimation;

	private NavigationRail navigationRail;
	private float x, y, width, height;

	private ScreenWrapper screenWrapper = new ScreenWrapper();
	private GaussianBlur gaussianBlur = new GaussianBlur(false);

	private Animation animation;

	private Page currentPage;
	private Page prevPage;

	private GuiScreen closeScreen;
	private boolean closeable;

	@Override
	public void init() {

		pages.clear();

		width = 938;
		height = 580;
		x = (mc.displayWidth / 2) - (width / 2);
		y = (mc.displayHeight / 2) - (height / 2);

		pages.add(new HomePage(x + 80, y, width - 80, height));
		pages.add(new ModsPage(this, x + 80, y, width - 80, height));
		pages.add(new MusicPage(x + 80, y, width - 80, height));
		pages.add(new ProfilePage(x + 80, y, width - 80, height));
		pages.add(new SettingsPage(x + 80, y, width - 80, height));

		if (currentPage == null) {
			setPage(HomePage.class);
		} else {
			setPage(currentPage.getClass());
		}

		navigationRail = new NavigationRail(this, x, y, 80, height);
		animation = new EaseEmphasizedDecelerate(Duration.EXTRA_LONG_1, 0, 1);
		pageAnimation = new DummyAnimation(0, 1);
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

				if (currentPage != null && prevPage == null) {
					currentPage.draw(mouseX * factor, mouseY * factor);
				}

				if (prevPage != null) {

					PageDirection d = prevPage.getDirection();
					float moveValue = pageAnimation.getEnd();
					float lastValueX = 0;
					float currentValueX = 0;

					if (d.equals(PageDirection.LEFT)) {
						lastValueX = -pageAnimation.getValue();
						currentValueX = moveValue - pageAnimation.getValue();
					}

					if (d.equals(PageDirection.RIGHT)) {
						lastValueX = pageAnimation.getValue();
						currentValueX = -moveValue + pageAnimation.getValue();
					}

					nvg.save();
					nvg.translate(lastValueX, 0);
					prevPage.draw(mouseX * factor, mouseY * factor);
					nvg.restore();

					nvg.save();
					nvg.translate(currentValueX, 0);
					currentPage.draw(mouseX * factor, mouseY * factor);
					nvg.restore();

					if (pageAnimation.isFinished()) {
						prevPage = null;
						pageAnimation = new DummyAnimation(0, 0);
					}
				}

				navigationRail.draw(mouseX * factor, mouseY * factor);
			}, false);
		}, x, y, width, height, 45, 2.0F - animation.getValue(), animation.getValue(), false);

		if (animation.isFinished() && animation.getEnd() == 0) {
			mc.displayGuiScreen(closeScreen);
		}
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {

		ScaledResolution sr = new ScaledResolution(mc);
		int factor = sr.getScaleFactor();

		navigationRail.mouseClicked(mouseX * factor, mouseY * factor, mouseButton);
		currentPage.mouseClicked(mouseX * factor, mouseY * factor, mouseButton);
	}

	@Override
	public void mouseReleased(int mouseX, int mouseY, int mouseButton) {

		ScaledResolution sr = new ScaledResolution(mc);
		int factor = sr.getScaleFactor();

		currentPage.mouseReleased(mouseX * factor, mouseY * factor, mouseButton);
	}

	@Override
	public void keyTyped(char typedCHar, int keyCode) {
		
		if (keyCode == Keyboard.KEY_ESCAPE && animation.getEnd() != 0 && pageAnimation.isFinished()) {
			close(null);
		}
		
		currentPage.keyTyped(typedCHar, keyCode);
	}

	public List<Page> getPages() {
		return pages;
	}

	public Page getCurrentPage() {
		return currentPage;
	}

	public void setPage(Class<? extends Page> clazz) {
		setPage(getPageByClass(clazz));
	}

	public void setPage(Page page) {

		if (currentPage != null) {
			prevPage = currentPage;
			currentPage.onClosed();
		}

		currentPage = page;
		pageAnimation = new EaseEmphasizedDecelerate(Duration.MEDIUM_4, 0, width);

		if (currentPage != null) {
			currentPage.init();
		}
	}

	private Page getPageByClass(Class<? extends Page> clazz) {

		for (Page p : pages) {
			if (p.getClass().equals(clazz)) {
				return p;
			}
		}

		return pages.get(0);
	}

	public void close(GuiScreen screen) {
		if(closeable) {
			closeScreen = screen;
			animation = new EaseEmphasizedDecelerate(Duration.EXTRA_LONG_1, 1, 0);
		}
	}

	public boolean isCloseable() {
		return closeable;
	}

	public void setCloseable(boolean closeable) {
		this.closeable = closeable;
	}
}
