package com.soarclient.skia.context;

import java.util.function.Consumer;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.systems.RenderSystem;

import io.github.humbleui.skija.BackendRenderTarget;
import io.github.humbleui.skija.Canvas;
import io.github.humbleui.skija.ColorSpace;
import io.github.humbleui.skija.DirectContext;
import io.github.humbleui.skija.Surface;
import io.github.humbleui.skija.SurfaceColorFormat;
import io.github.humbleui.skija.SurfaceOrigin;
import net.minecraft.client.MinecraftClient;

public class SkiaContext {

	private static DirectContext context = null;
	private static Surface surface;
	private static BackendRenderTarget renderTarget;

	public static Canvas getCanvas() {
		return surface.getCanvas();
	}

	public static void createSurface(int width, int height) {

		if (context == null) {
			context = DirectContext.makeGL();
		}

		if (surface != null) {
			surface.close();
		}

		if (renderTarget != null) {
			renderTarget.close();
		}

		renderTarget = BackendRenderTarget.makeGL(width, height, 0, 8, MinecraftClient.getInstance().getFramebuffer().fbo, GL11.GL_RGBA8);
		surface = Surface.wrapBackendRenderTarget(context, renderTarget, SurfaceOrigin.BOTTOM_LEFT,
				SurfaceColorFormat.RGBA_8888, ColorSpace.getSRGB());
	}

	public static void draw(Consumer<Canvas> drawingLogic) {

		States.getInstance().push();
		RenderSystem.clearColor(0f, 0f, 0f, 0f);
		context.resetGLAll();

		Canvas canvas = getCanvas();
		drawingLogic.accept(canvas);

		surface.flushAndSubmit();
		States.getInstance().pop();
	}

	public static DirectContext getContext() {
		return context;
	}
}
