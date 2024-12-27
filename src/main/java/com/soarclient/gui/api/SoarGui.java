package com.soarclient.gui.api;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import com.soarclient.animation.Animation;
import com.soarclient.animation.Duration;
import com.soarclient.animation.cubicbezier.impl.EaseEmphasizedDecelerate;
import com.soarclient.animation.other.DummyAnimation;
import com.soarclient.shaders.impl.GaussianBlur;
import com.soarclient.skia.Skia;
import com.soarclient.skia.context.SkiaContext;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;

public abstract class SoarGui {

	protected Minecraft mc = Minecraft.getMinecraft();
	private Animation animation;

	private boolean useBlur;
	private boolean mcScale;

	public SoarGui(boolean useBlur, boolean mcScale) {
		this.useBlur = useBlur;
		this.mcScale = mcScale;
		animation = new DummyAnimation(1, 1);
	}

	public SoarGui() {
		this(true, false);
	}

	public abstract void init();

	public abstract void draw(int mouseX, int mouseY);

	public abstract void mousePressed(int mouseX, int mouseY, int mouseButton);

	public abstract void mouseReleased(int mouseX, int mouseY, int mouseButton);

	public abstract void keyTyped(char typedChar, int keyCode);

	public abstract void onClosed();

	public GuiScreen build() {
		return new GuiScreen() {

			private GaussianBlur blur = new GaussianBlur(false);

			@Override
			public void initGui() {
				animation = new EaseEmphasizedDecelerate(Duration.EXTRA_LONG_1, 0, 1);
				SoarGui.this.init();
			}

			@Override
			public void drawScreen(int mouseX, int mouseY, float partialTicks) {

				ScaledResolution sr = new ScaledResolution(mc);
				
				if (useBlur) {
					blur.draw(1 + (20 * animation.getValue()));
				}

				SkiaContext.draw((context) -> {
					Skia.save();
					Skia.setAlpha((int) (animation.getValue() * 255));
					Skia.scale(0, 0, mc.displayWidth, mc.displayHeight, 2 - animation.getValue());
					
					if(mcScale) {
						Skia.scale(sr.getScaleFactor());
					}
					
					SoarGui.this.draw(mcScale ? mouseX : (int) Mouse.getX(),
							mcScale ? mouseY : (int) Display.getHeight() - Mouse.getY());
					Skia.restore();
				});

				if (animation.getEnd() == 0 && animation.isFinished()) {
					mc.displayGuiScreen(null);
				}
			}

			@Override
			public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
				SoarGui.this.mousePressed(mcScale ? mouseX : (int) Mouse.getX(),
						mcScale ? mouseY : (int) Display.getHeight() - Mouse.getY(), mouseButton);
			}

			@Override
			public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
				SoarGui.this.mouseReleased(mcScale ? mouseX : (int) Mouse.getX(),
						mcScale ? mouseY : (int) Display.getHeight() - Mouse.getY(), mouseButton);
			}

			@Override
			public void keyTyped(char typedChar, int keyCode) {

				if (keyCode == Keyboard.KEY_ESCAPE) {
					animation = new EaseEmphasizedDecelerate(Duration.EXTRA_LONG_1, 1, 0);
				}

				SoarGui.this.keyTyped(typedChar, keyCode);
			}

			@Override
			public void onGuiClosed() {
				SoarGui.this.onClosed();
			}

			@Override
			public boolean doesGuiPauseGame() {
				return false;
			}
		};
	}
}
