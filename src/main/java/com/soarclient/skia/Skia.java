package com.soarclient.skia;

import java.awt.Color;
import java.io.File;

import org.lwjgl.opengl.GL11;

import com.soarclient.skia.context.SkiaContext;
import com.soarclient.skia.image.ImageHelper;

import io.github.humbleui.skija.Canvas;
import io.github.humbleui.skija.ClipMode;
import io.github.humbleui.skija.ColorType;
import io.github.humbleui.skija.Font;
import io.github.humbleui.skija.Image;
import io.github.humbleui.skija.Paint;
import io.github.humbleui.skija.Path;
import io.github.humbleui.skija.SurfaceOrigin;
import io.github.humbleui.types.RRect;
import io.github.humbleui.types.Rect;

public class Skia {

	private static ImageHelper imageHelper = new ImageHelper();

	public static void drawRect(float x, float y, float width, float height, Color color) {
		getCanvas().drawRect(Rect.makeXYWH(x, y, width, height), getPaint(color));
	}

	public static void drawRoundedRect(float x, float y, float width, float height, float radius, Color color) {
		getCanvas().drawRRect(RRect.makeXYWH(x, y, width, height, radius), getPaint(color));
	}

	public static void drawImage(int textureId, float x, float y, float width, float height) {
		getCanvas().drawImageRect(
				Image.adoptTextureFrom(SkiaContext.getContext(), textureId, GL11.GL_TEXTURE_2D, (int) width,
						(int) height, GL11.GL_RGBA8, SurfaceOrigin.TOP_LEFT, ColorType.RGBA_8888),
				Rect.makeXYWH(x, y, width, height));
	}

	public static void drawImage(String path, float x, float y, float width, float height) {
		
		path = "/assets/soar/" + path;
		
		if (imageHelper.load(path)) {
			getCanvas().drawImageRect(imageHelper.get(path), Rect.makeXYWH(x, y, width, height));
		}
	}

	public static void drawImage(File file, float x, float y, float width, float height) {
		if (imageHelper.load(file)) {
			getCanvas().drawImageRect(imageHelper.get(file.getName()), Rect.makeXYWH(x, y, width, height));
		}
	}

	public static void drawRoundedImage(String filePath, float x, float y, float width, float height,
			float radius) {

		Path path = new Path();
		path.addRRect(RRect.makeXYWH(x, y, width, height, radius));

		save();
		getCanvas().clipPath(path, ClipMode.INTERSECT, true);
		drawImage(filePath, x, y, width, height);
		restore();
	}

	public static void drawRoundedImage(File file, float x, float y, float width, float height, float radius) {

		Path path = new Path();
		path.addRRect(RRect.makeXYWH(x, y, width, height, radius));

		save();
		getCanvas().clipPath(path, ClipMode.INTERSECT, true);
		drawImage(file, x, y, width, height);
		restore();
	}

	public static void clip(float x, float y, float width, float height) {
		getCanvas().clipRect(Rect.makeXYWH(x, y, width, height));
	}

	public static void clip(float x, float y, float width, float height, float radius) {
		getCanvas().clipRRect(RRect.makeXYWH(x, y, width, height, radius));
	}

	public static void scale(float scale) {
		getCanvas().scale(scale, scale);
	}

	public static void scale(float x, float y, float scale) {
		getCanvas().translate(x, y);
		getCanvas().scale(scale, scale);
		getCanvas().translate(-x, -y);
	}
	
	public static void scale(float x, float y, float width, float height, float scale) {

		float centerX = x + width / 2;
		float centerY = y + height / 2;

		getCanvas().translate(centerX, centerY);
		getCanvas().scale(scale, scale);
		getCanvas().translate(-centerX, -centerY);
	}

	public static void translate(float x, float y) {
		getCanvas().translate(x, y);
	}

	public static void save() {
		getCanvas().save();
	}

	public static void restore() {
		getCanvas().restore();
	}

	public static void drawText(String text, float x, float y, Color color, Font font) {
		Rect bounds = font.measureText(text);
		getCanvas().drawString(text, x - 1F, (y - 1F) + bounds.getHeight(), font, getPaint(color));
	}
	
	private static Paint getPaint(Color color) {
		Paint paint = new Paint();
		paint.setARGB(color.getAlpha(), color.getRed(), color.getGreen(), color.getBlue());
		return paint;
	}

	private static Canvas getCanvas() {
		return SkiaContext.getCanvas();
	}
}
