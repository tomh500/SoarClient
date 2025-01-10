package com.soarclient.skia;

import java.awt.Color;
import java.io.File;

import com.soarclient.logger.SoarLogger;
import com.soarclient.skia.context.SkiaContext;
import com.soarclient.skia.image.ImageHelper;

import io.github.humbleui.skija.Canvas;
import io.github.humbleui.skija.ClipMode;
import io.github.humbleui.skija.Font;
import io.github.humbleui.skija.FontMetrics;
import io.github.humbleui.skija.Image;
import io.github.humbleui.skija.Paint;
import io.github.humbleui.skija.Path;
import io.github.humbleui.skija.SurfaceOrigin;
import io.github.humbleui.types.RRect;
import io.github.humbleui.types.Rect;

public class Skia {

	private static final ImageHelper imageHelper = new ImageHelper();

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
	
	private static void drawShadowOutline(float x, float y, float width, float height, float radius, float strokeWidth,
			Paint paint) {

		Path path = new Path();
		path.addRRect(RRect.makeXYWH(x, y, width, height, radius));

		paint.setStrokeWidth(strokeWidth);

		getCanvas().drawPath(path, paint);
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

	public static void drawImage(int textureId, float x, float y, float width, float height, SurfaceOrigin origin) {

		if (imageHelper.load(textureId, width, height, origin)) {
			getCanvas().drawImageRect(imageHelper.get(textureId), Rect.makeXYWH(x, y, width, height));
		}
	}

	public static void drawImage(int textureId, float x, float y, float width, float height) {
		drawImage(textureId, x, y, width, height, SurfaceOrigin.TOP_LEFT);
	}

	public static void drawImageAsync(String filePath, float x, float y, float width, float height,
			Color fallbackColor) {

		Image image = imageHelper.get(filePath);

		if (image != null) {
			getCanvas().drawImageRect(image, Rect.makeXYWH(x, y, width, height));
		} else {
			if (!imageHelper.getLoadingFutures().containsKey(filePath)) {
				imageHelper.loadAsync(filePath).thenAccept(success -> {
					if (!success) {
						SoarLogger.error("Failed to load image " + filePath);
					}
				});
			}

			getCanvas().drawRect(Rect.makeXYWH(x, y, width, height), getPaint(fallbackColor));
		}
	}

	public static void drawImageAsync(File file, float x, float y, float width, float height, Color fallbackColor) {

		String filePath = file.getAbsolutePath();
		Image image = imageHelper.get(filePath);

		if (image != null) {
			getCanvas().drawImageRect(image, Rect.makeXYWH(x, y, width, height));
		} else {
			if (!imageHelper.getLoadingFutures().containsKey(filePath)) {
				imageHelper.loadAsync(file).thenAccept(success -> {
					if (!success) {
						SoarLogger.error("Failed to load image " + filePath);
					}
				});
			}

			getCanvas().drawRect(Rect.makeXYWH(x, y, width, height), getPaint(fallbackColor));
		}
	}

	public static void drawRoundedImage(int textureId, float x, float y, float width, float height, float radius) {

		Path path = new Path();
		path.addRRect(RRect.makeXYWH(x, y, width, height, radius));

		save();
		getCanvas().clipPath(path, ClipMode.INTERSECT, true);
		drawImage(textureId, x, y, width, height);
		restore();
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

	public static void drawRoundedImageAsync(String filePath, float x, float y, float width, float height, float radius,
			Color fallbackColor) {

		Path path = new Path();
		path.addRRect(RRect.makeXYWH(x, y, width, height, radius));

		save();
		getCanvas().clipPath(path, ClipMode.INTERSECT, true);
		drawImageAsync(filePath, x, y, width, height, fallbackColor);
		restore();
	}

	public static void drawRoundedImageAsync(File file, float x, float y, float width, float height, float radius,
			Color fallbackColor) {

		Path path = new Path();
		path.addRRect(RRect.makeXYWH(x, y, width, height, radius));

		save();
		getCanvas().clipPath(path, ClipMode.INTERSECT, true);
		drawImageAsync(file, x, y, width, height, fallbackColor);
		restore();
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

	public static void clip(float x, float y, float width, float height, float topLeft, float topRight,
			float bottomRight, float bottomLeft) {

		float[] corners = new float[] { topLeft, topLeft, topRight, topRight, bottomRight, bottomRight, bottomLeft,
				bottomLeft };

		Path path = new Path();

		path.addRRect(RRect.makeComplexXYWH(x, y, width, height, corners));
		clipPath(path, ClipMode.INTERSECT, true);
	}

	public static void clip(float x, float y, float width, float height, float radius) {
		clip(x, y, width, height, radius, ClipMode.INTERSECT);
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
	
	public static void drawHeightCenteredText(String text, float x, float y, Color color, Font font) {
		
	    FontMetrics metrics = font.getMetrics();
	    Rect bounds = font.measureText(text);
	    
	    float textCenterY = y + (metrics.getAscent() - metrics.getDescent()) / 2 - metrics.getAscent();

	    getCanvas().drawString(text, x - bounds.getLeft(), textCenterY, font, getPaint(color));
	}

	public static void drawFullCenteredText(String text, float x, float y, Color color, Font font) {
		
	    Rect bounds = font.measureText(text);
	    
	    FontMetrics metrics = font.getMetrics();

	    float textCenterX = x - bounds.getLeft() - (bounds.getWidth() / 2);
	    float textCenterY = y + (metrics.getAscent() - metrics.getDescent()) / 2 - metrics.getAscent();

	    getCanvas().drawString(text, textCenterX, textCenterY, font, getPaint(color));
	}
	
	public static Rect getTextBounds(String text, Font font) {
		return font.measureText(text);
	}

	public static String getLimitText(String text, Font font, float width) {

		boolean isInRange = false;
		boolean isRemoved = false;

		while (!isInRange) {

			if (getTextBounds(text, font).getWidth() > width - getTextBounds("...", font).getWidth()) {
				text = text.substring(0, text.length() - 1);
				isRemoved = true;
			} else {
				isInRange = true;
			}
		}

		return text + (isRemoved ? "..." : "");
	}

	private static Paint getPaint(Color color) {
		Paint paint = new Paint();
		paint.setARGB(color.getAlpha(), color.getRed(), color.getGreen(), color.getBlue());
		return paint;
	}

	public static void save() {
		getCanvas().save();
	}

	public static void restore() {
		getCanvas().restore();
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
	
	public static void setAlpha(int alpha) {

		Paint paint = new Paint();
		paint.setAlpha(alpha);

		getCanvas().saveLayer(null, paint);
	}
	
	public static Canvas getCanvas() {
		return SkiaContext.getCanvas();
	}

	public static ImageHelper getImageHelper() {
		return imageHelper;
	}
}
