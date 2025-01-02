package com.soarclient.skia;

import java.awt.Color;
import java.io.File;

import com.soarclient.skia.context.SkiaContext;
import com.soarclient.skia.image.ImageHelper;

import io.github.humbleui.skija.Canvas;
import io.github.humbleui.skija.ClipMode;
import io.github.humbleui.skija.Font;
import io.github.humbleui.skija.Paint;
import io.github.humbleui.skija.Path;
import io.github.humbleui.skija.SurfaceOrigin;
import io.github.humbleui.types.RRect;
import io.github.humbleui.types.Rect;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;

public class Skia {

	private static ImageHelper imageHelper = new ImageHelper();

	public static void drawRect(float x, float y, float width, float height, Color color) {
		getCanvas().drawRect(Rect.makeXYWH(x, y, width, height), getPaint(color));
	}

	public static void drawCircle(float x, float y, float radius, Color color) {
		Paint paint = getPaint(color);
		getCanvas().drawCircle(x, y, radius, paint);
	}

	public static void drawRoundedRect(float x, float y, float width, float height, float radius, Color color) {
		getCanvas().drawRRect(RRect.makeXYWH(x, y, width, height, radius), getPaint(color));
	}

	public static void drawRoundedRectVarying(float x, float y, float width, float height, float topLeft,
			float topRight, float bottomRight, float bottomLeft, Color color) {

		float[] corners = new float[] { topLeft, topLeft, topRight, topRight, bottomRight, bottomRight, bottomLeft,
				bottomLeft };

		getCanvas().drawRRect(RRect.makeComplexXYWH(x, y, width, height, corners), getPaint(color));
	}

	public static void drawShadow(float x, float y, float width, float height, float radius) {
		
		int strength = 8;
		Paint paint = getPaint(Color.BLACK);
		int alpha = 1;
		
		save();
		clip(x, y, width, height, radius, ClipMode.DIFFERENCE);
		for (float f = strength; f > 0; f--) {
			paint.setAlphaf(alpha / 255f);
			drawShadowOutline(x - (f / 2), y - (f / 2), width + f, height + f, radius + 2, f, paint);

			alpha += 2;
		}
		restore();
	}

	private static void drawShadowOutline(float x, float y, float width, float height, float radius,
			float strokeWidth, Paint paint) {
		
		Path path = new Path();
		path.addRRect(RRect.makeXYWH(x, y, width, height, radius));

		paint.setStrokeWidth(strokeWidth);

		getCanvas().drawPath(path, paint);
	}

	public static void drawRoundedImage(int textureId, float x, float y, float width, float height, float radius, float alpha,
			SurfaceOrigin origin) {
		Path path = new Path();
		path.addRRect(RRect.makeXYWH(x, y, width, height, radius));

		save();
		getCanvas().clipPath(path, ClipMode.INTERSECT, true);
		drawImage(textureId, x, y, width, height, alpha, origin);
		restore();
	}
	
	public static void drawRoundedImage(int textureId, float x, float y, float width, float height, float radius, float alpha) {
		drawRoundedImage(textureId, x, y, width, height, radius, alpha, SurfaceOrigin.TOP_LEFT);
	}
	
	public static void drawImage(int textureId, float x, float y, float width, float height, float alpha,
			SurfaceOrigin origin) {

		if (imageHelper.load(textureId, width, height, origin)) {
			Paint paint = new Paint();
			paint.setAlpha((int) (255 * alpha));
			getCanvas().drawImageRect(imageHelper.get(textureId), Rect.makeXYWH(x, y, width, height), paint);
		}
	}

	public static void drawImage(int textureId, float x, float y, float width, float height, float alpha) {
		drawImage(textureId, x, y, width, height, alpha, SurfaceOrigin.TOP_LEFT);
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

	public static void drawRoundedImage(String filePath, float x, float y, float width, float height, float radius) {

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

	public static void drawBlur(float x, float y, float width, float height) {

		ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());

		Path path = new Path();
		path.addRect(Rect.makeXYWH(x, y, width, height));

		save();
		getCanvas().clipPath(path, ClipMode.INTERSECT, true);
		drawImage(GuiIngame.INGAME_BLUR.getTexture(), 0, 0, sr.getScaledWidth(), sr.getScaledHeight(), 1F,
				SurfaceOrigin.BOTTOM_LEFT);
		restore();
	}

	public static void drawRoundedBlur(float x, float y, float width, float height, float radius) {

		ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());

		Path path = new Path();
		path.addRRect(RRect.makeXYWH(x, y, width, height, radius));

		save();
		getCanvas().clipPath(path, ClipMode.INTERSECT, true);
		drawImage(GuiIngame.INGAME_BLUR.getTexture(), 0, 0, sr.getScaledWidth(), sr.getScaledHeight(), 1F,
				SurfaceOrigin.BOTTOM_LEFT);
		restore();
	}

	public static void drawLine(float x, float y, float endX, float endY, float width, Color color) {

		Paint paint = getPaint(color);

		paint.setStroke(true);
		paint.setStrokeWidth(width);
		paint.setAntiAlias(true);

		getCanvas().drawLine(x, y, endX, endY, paint);
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

	public static void rotate(float x, float y, float width, float height, float rotate) {
		
	    float centerX = x + width / 2;
	    float centerY = y + height / 2;

	    getCanvas().translate(centerX, centerY);
	    getCanvas().rotate(rotate);
	    getCanvas().translate(-centerX, -centerY);
	}
	
	public static void save() {
		getCanvas().save();
	}

	public static void setAlpha(int alpha) {

		Paint paint = new Paint();
		paint.setAlpha(alpha);

		getCanvas().saveLayer(null, paint);
	}

	public static void restore() {
		getCanvas().restore();
	}

	public static void clipPath(Path path, ClipMode mode, boolean arg) {
		getCanvas().clipPath(path, mode, arg);
	}

	public static void clipPath(Path path) {
		getCanvas().clipPath(path, ClipMode.INTERSECT, true);
	}

	public static void clip(float x, float y, float width, float height, float radius, ClipMode mode) {

		Path path = new Path();

		path.addRRect(RRect.makeXYWH(x, y, width, height, radius));
		clipPath(path, mode, true);
	}
	
	public static void clip(float x, float y, float width, float height, float topLeft,
			float topRight, float bottomRight, float bottomLeft) {
		
		float[] corners = new float[] { topLeft, topLeft, topRight, topRight, bottomRight, bottomRight, bottomLeft,
				bottomLeft };
		
		Path path = new Path();
		
		path.addRRect(RRect.makeComplexXYWH(x, y, width, height, corners));
		clipPath(path, ClipMode.INTERSECT, true);
	}

	public static void clip(float x, float y, float width, float height, float radius) {
		clip(x, y, width, height, radius, ClipMode.INTERSECT);
	}

	public static void drawPath(Path path, Paint paint) {
		getCanvas().drawPath(path, paint);
	}

	public static void drawText(String text, float x, float y, Color color, Font font) {
		Rect bounds = font.measureText(text);
		getCanvas().drawString(text, x - bounds.getLeft(), y - bounds.getTop(), font, getPaint(color));
	}

	public static void drawCenteredText(String text, float x, float y, Color color, Font font) {
		Rect bounds = font.measureText(text);
		getCanvas().drawString(text, x - bounds.getLeft() - (bounds.getWidth() / 2), y - bounds.getTop(), font,
				getPaint(color));
	}
	
	public static void drawFullCenteredText(String text, float x, float y, Color color, Font font) {
		Rect bounds = font.measureText(text);
		getCanvas().drawString(text, x - bounds.getLeft() - (bounds.getWidth() / 2), y - bounds.getTop() - (bounds.getHeight() / 2), font,
				getPaint(color));
	}

	public static float getTextWidth(String text, Font font) {
		Rect bounds = font.measureText(text);
		return bounds.getWidth();
	}

	public static float getTextHeight(String text, Font font) {
		Rect bounds = font.measureText(text);
		return bounds.getHeight();
	}

	private static Paint getPaint(Color color) {
		Paint paint = new Paint();
		paint.setARGB(color.getAlpha(), color.getRed(), color.getGreen(), color.getBlue());
		return paint;
	}

	public static Canvas getCanvas() {
		return SkiaContext.getCanvas();
	}

	public static ImageHelper getImageHelper() {
		return imageHelper;
	}
}
