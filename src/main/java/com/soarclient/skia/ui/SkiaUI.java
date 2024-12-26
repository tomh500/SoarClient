package com.soarclient.skia.ui;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import com.soarclient.shaders.impl.GaussianBlur;
import com.soarclient.skia.context.SkiaContext;

import net.minecraft.client.gui.GuiScreen;

public abstract class SkiaUI {
	
	public abstract void draw(float mouseX, float mouseY);
	public abstract void mousePressed(float mouseX, float mouseY, int mouseButton);
	public abstract void mouseReleased(float mouseX, float mouseY, int mouseButton);
	public abstract void keyTyped(char typedChar, int keyCode);

	public GuiScreen build() {
		return new GuiScreen() {

			private GaussianBlur blur = new GaussianBlur(false);
			
			@Override
			public void drawScreen(int mouseX, int mouseY, float partialTicks) {
				blur.draw(20);;
				SkiaContext.draw((context) -> SkiaUI.this.draw((float) Mouse.getX(),
						(float) Display.getHeight() - Mouse.getY()));
			}

			@Override
			public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
				SkiaUI.this.mousePressed((float) Mouse.getX(), (float) Display.getHeight() - Mouse.getY(), mouseButton);
			}
			
			@Override
			public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
				SkiaUI.this.mouseReleased((float) Mouse.getX(), (float) Display.getHeight() - Mouse.getY(), mouseButton);
			}
			
			@Override
			public boolean doesGuiPauseGame() {
				return false;
			}
		};
	}
}
