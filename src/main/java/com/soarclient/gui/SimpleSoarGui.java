package com.soarclient.gui;

import com.soarclient.skia.Skia;
import com.soarclient.skia.context.SkiaContext;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;

public class SimpleSoarGui {

	private boolean mcScale;

	public SimpleSoarGui(boolean mcScale) {
		this.mcScale = mcScale;
	}

	public void init() {
	}

	public void drawOpenGL(double mouseX, double mouseY) {
	}

	public void draw(double mouseX, double mouseY) {
	}

	public void mousePressed(double mouseX, double mouseY, int button) {
	}

	public void mouseReleased(double mouseX, double mouseY, int button) {
	}

	public void charTyped(char chr, int modifiers) {
	}

	public void keyPressed(int keyCode, int scanCode, int modifiers) {
	}

	public void onClosed() {
	}

	public Screen build() {
		return new Screen(null) {

			@Override
			protected void init() {
				SimpleSoarGui.this.init();
			}

			@Override
			public void render(DrawContext context, int mouseX, int mouseY, float delta) {

				SimpleSoarGui.this.drawOpenGL(mcScale ? mouseX : client.mouse.getX(),
						mcScale ? mouseY : client.getWindow().getHeight() - client.mouse.getY());

				SkiaContext.draw((skiaContext) -> {

					Skia.save();

					if (mcScale) {
						Skia.scale((float) client.getWindow().getScaleFactor());
					}

					SimpleSoarGui.this.draw(mcScale ? mouseX : client.mouse.getX(),
							mcScale ? mouseY : client.getWindow().getHeight() - client.mouse.getY());
					Skia.restore();
				});
			}

			@Override
			public boolean mouseClicked(double mouseX, double mouseY, int button) {

				SimpleSoarGui.this.mousePressed(mcScale ? mouseX : client.mouse.getX(),
						mcScale ? mouseY : client.getWindow().getHeight() - client.mouse.getY(), button);

				return true;
			}

			@Override
			public boolean mouseReleased(double mouseX, double mouseY, int button) {
				SimpleSoarGui.this.mouseReleased(mcScale ? mouseX : client.mouse.getX(),
						mcScale ? mouseY : (int) client.getWindow().getHeight() - client.mouse.getY(), button);
				return true;
			}

			@Override
			public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
				SimpleSoarGui.this.keyPressed(keyCode, scanCode, modifiers);
				return true;
			}

			@Override
			public void close() {
				SimpleSoarGui.this.onClosed();
				super.close();
			}

			@Override
			public boolean shouldPause() {
				return false;
			}
		};
	}
}
