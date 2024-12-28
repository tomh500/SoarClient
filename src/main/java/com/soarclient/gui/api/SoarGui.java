package com.soarclient.gui.api;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import com.soarclient.skia.Skia;
import com.soarclient.skia.context.SkiaContext;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;

public abstract class SoarGui {

	protected Minecraft mc = Minecraft.getMinecraft();

	private boolean mcScale;

	public SoarGui(boolean mcScale) {
		this.mcScale = mcScale;
	}

	public abstract void init();

	public void drawOpenGL(int mouseX, int mouseY) {}
	
	public abstract void draw(int mouseX, int mouseY);

	public abstract void mousePressed(int mouseX, int mouseY, int mouseButton);

	public abstract void mouseReleased(int mouseX, int mouseY, int mouseButton);

	public abstract void keyTyped(char typedChar, int keyCode);

	public abstract void onClosed();

	public GuiScreen build() {
		return new GuiScreen() {

			@Override
			public void initGui() {
				SoarGui.this.init();
			}

			@Override
			public void drawScreen(int mouseX, int mouseY, float partialTicks) {

				ScaledResolution sr = new ScaledResolution(mc);
				
				SoarGui.this.drawOpenGL(mcScale ? mouseX : (int) Mouse.getX(),
						mcScale ? mouseY : (int) Display.getHeight() - Mouse.getY());
				
				SkiaContext.draw((context) -> {
					
					Skia.save();
					
					if(mcScale) {
						Skia.scale(sr.getScaleFactor());
					}
					
					SoarGui.this.draw(mcScale ? mouseX : (int) Mouse.getX(),
							mcScale ? mouseY : (int) Display.getHeight() - Mouse.getY());
					Skia.restore();
				});
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