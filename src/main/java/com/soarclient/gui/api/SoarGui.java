package com.soarclient.gui.api;

import org.lwjgl.input.Keyboard;

import com.soarclient.Soar;
import com.soarclient.animation.Animation;
import com.soarclient.animation.Duration;
import com.soarclient.animation.cubicbezier.impl.EaseEmphasizedDecelerate;
import com.soarclient.animation.other.DummyAnimation;
import com.soarclient.management.color.api.ColorPalette;
import com.soarclient.shaders.impl.GaussianBlur;
import com.soarclient.skia.Skia;

import net.minecraft.client.gui.GuiScreen;

public abstract class SoarGui extends SimpleSoarGui {

	private final GuiTransition transition;
	private Animation inOutAnimation;
	private GuiScreen nextScreen;
	private final boolean background;
	private GaussianBlur gaussianBlur;

	public SoarGui(GuiTransition transition, boolean background, boolean blur) {
		super(false);
		this.transition = transition;
		this.background = background;

		if (blur) {
			gaussianBlur = new GaussianBlur(false);
		}
	}

	@Override
	public void init() {
		if(transition != null) {
			inOutAnimation = new EaseEmphasizedDecelerate(Duration.EXTRA_LONG_1, 0, 1);
		} else {
			inOutAnimation = new DummyAnimation(1);
		}
	}

	@Override
	public void drawOpenGL(int mouseX, int mouseY) {
		if (gaussianBlur != null) {
			gaussianBlur.draw(transition != null ? (1 + (inOutAnimation.getValue() * 20)) : 20);
		}
	}

	@Override
	public void draw(int mouseX, int mouseY) {

		ColorPalette palette = Soar.getInstance().getColorManager().getPalette();

		Skia.save();
		
		if(transition != null) {
			float[] result = transition.onTransition(inOutAnimation);
			Skia.setAlpha((int) (inOutAnimation.getValue() * 255));
			Skia.translate(result[0] * mc.displayWidth, result[1] * mc.displayHeight);
			Skia.scale(getX(), getY(), getWidth(), getHeight(), result[2]);
		}

		if (background) {
			Skia.drawRoundedRect(getX(), getY(), getWidth(), getHeight(), 35, palette.getBackground());
		}

		drawSkia(mouseX, mouseY);

		Skia.restore();

		if (inOutAnimation.getEnd() == 0 && inOutAnimation.isFinished()) {
			mc.displayGuiScreen(nextScreen);
			nextScreen = null;
		}
	}

	public void drawSkia(int mouseX, int mouseY) {
	}

	@Override
	public void keyTyped(char typedChar, int keyCode) {
		if (keyCode == Keyboard.KEY_ESCAPE && inOutAnimation.getEnd() == 1) {
			close();
		}
	}

	public void close(GuiScreen nextScreen) {
		if (inOutAnimation.getEnd() == 1) {
			this.nextScreen = nextScreen;
			if(transition != null) {
				inOutAnimation = new EaseEmphasizedDecelerate(Duration.EXTRA_LONG_1, 1, 0);
			} else {
				inOutAnimation = new DummyAnimation(0);
			}
		}
	}

	public void close() {
		close(null);
	}

	public abstract float getX();

	public abstract float getY();

	public abstract float getWidth();

	public abstract float getHeight();
}
