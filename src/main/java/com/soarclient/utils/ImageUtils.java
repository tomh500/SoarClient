package com.soarclient.utils;

import java.awt.image.BufferedImage;

public class ImageUtils {

	public static int[] imageToPixels(BufferedImage image) {

		int width = image.getWidth();
		int height = image.getHeight();

		return image.getRGB(0, 0, width, height, null, 0, width);
	}
}