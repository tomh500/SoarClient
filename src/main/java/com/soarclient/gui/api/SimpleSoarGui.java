package com.soarclient.gui.api;

import com.soarclient.skia.Skia;
import com.soarclient.skia.context.SkiaContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class SimpleSoarGui {

	protected Minecraft client = Minecraft.getInstance();
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

	public void mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
	}

	public void charTyped(char chr, int modifiers) {
	}

	public void keyPressed(int keyCode, int scanCode, int modifiers) {
	}

	public Screen build() {
		return new Screen(Component.empty()) {

			@Override
			public void init() {
				SimpleSoarGui.this.init();
			}

			@Override
			public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {

				SimpleSoarGui.this.drawOpenGL(mcScale ? mouseX : minecraft.mouseHandler.xpos(),
						mcScale ? mouseY : minecraft.mouseHandler.ypos());

				SkiaContext.draw((skiaContext) -> {

					Skia.save();

					if (mcScale) {
						Skia.scale((float) minecraft.getWindow().getGuiScale());
					}

					SimpleSoarGui.this.draw(mcScale ? mouseX : minecraft.mouseHandler.xpos(),
							mcScale ? mouseY : minecraft.mouseHandler.ypos());
					Skia.restore();
				});
			}

			@Override
			public boolean mouseClicked(double mouseX, double mouseY, int button) {
				SimpleSoarGui.this.mousePressed(mcScale ? mouseX : minecraft.mouseHandler.xpos(),
						mcScale ? mouseY : minecraft.mouseHandler.ypos(), button);
				return true;
			}

			@Override
			public boolean mouseReleased(double mouseX, double mouseY, int button) {
				SimpleSoarGui.this.mouseReleased(mcScale ? mouseX : minecraft.mouseHandler.xpos(),
						mcScale ? mouseY : (int) minecraft.mouseHandler.ypos(), button);
				return true;
			}

			@Override
			public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
				SimpleSoarGui.this.mouseScrolled(mcScale ? mouseX : minecraft.mouseHandler.xpos(),
						mcScale ? mouseY : (int) minecraft.mouseHandler.ypos(), horizontalAmount, verticalAmount);
				return true;
			}

			@Override
			public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
				SimpleSoarGui.this.keyPressed(keyCode, scanCode, modifiers);
				return true;
			}

			@Override
			public boolean charTyped(char chr, int modifiers) {
				SimpleSoarGui.this.charTyped(chr, modifiers);
				return true;
			}

			@Override
			public boolean isPauseScreen() {
				return false;
			}
		};
	}
}
