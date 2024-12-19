package com.soarclient.utils;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class AdaptiveGaussianBlur {

	public static BufferedImage applyGaussianBlur(BufferedImage source, double sigma) {

		int originalWidth = source.getWidth();
		int originalHeight = source.getHeight();

		double scale = calculateOptimalScale(sigma);

		int scaledWidth = Math.max(1, (int) (originalWidth / scale));
		int scaledHeight = Math.max(1, (int) (originalHeight / scale));
		BufferedImage scaledImage = scaleImage(source, scaledWidth, scaledHeight);

		double scaledSigma = sigma / scale;
		BufferedImage blurredScaled = applyGaussianBlurInternal(scaledImage, scaledSigma);

		return scaleImage(blurredScaled, originalWidth, originalHeight);
	}

	private static double calculateOptimalScale(double sigma) {
		double scale = sigma / 2.0;
		return Math.max(1.0, scale);
	}

	private static BufferedImage scaleImage(BufferedImage source, int width, int height) {

		BufferedImage scaled = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = scaled.createGraphics();

		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g2d.drawImage(source, 0, 0, width, height, null);
		g2d.dispose();

		return scaled;
	}

	private static BufferedImage applyGaussianBlurInternal(BufferedImage source, double sigma) {

		int width = source.getWidth();
		int height = source.getHeight();

		int kernelSize = (int) Math.ceil(sigma * 3) * 2 + 1;
		double[] kernel = createGaussianKernel(sigma, kernelSize);

		BufferedImage horizontalBlur = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		applyHorizontalBlur(source, horizontalBlur, kernel);

		BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		applyVerticalBlur(horizontalBlur, result, kernel);

		return result;
	}

	private static double[] createGaussianKernel(double sigma, int size) {

		double[] kernel = new double[size];
		double sum = 0.0;
		int center = size / 2;

		for (int i = 0; i < size; i++) {
			double x = i - center;
			kernel[i] = Math.exp(-(x * x) / (2 * sigma * sigma));
			sum += kernel[i];
		}

		for (int i = 0; i < size; i++) {
			kernel[i] /= sum;
		}

		return kernel;
	}

	private static void applyHorizontalBlur(BufferedImage source, BufferedImage target, double[] kernel) {

		int width = source.getWidth();
		int height = source.getHeight();
		int[] sourcePixels = ((DataBufferInt) source.getRaster().getDataBuffer()).getData();
		int[] targetPixels = ((DataBufferInt) target.getRaster().getDataBuffer()).getData();
		int kernelRadius = kernel.length / 2;

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				double sumR = 0, sumG = 0, sumB = 0, sumA = 0;

				for (int i = -kernelRadius; i <= kernelRadius; i++) {
					int px = Math.min(Math.max(x + i, 0), width - 1);
					int pixel = sourcePixels[y * width + px];

					double weight = kernel[i + kernelRadius];
					sumA += ((pixel >> 24) & 0xff) * weight;
					sumR += ((pixel >> 16) & 0xff) * weight;
					sumG += ((pixel >> 8) & 0xff) * weight;
					sumB += (pixel & 0xff) * weight;
				}

				targetPixels[y * width + x] = (((int) sumA & 0xff) << 24) | (((int) sumR & 0xff) << 16)
						| (((int) sumG & 0xff) << 8) | ((int) sumB & 0xff);
			}
		}
	}

	private static void applyVerticalBlur(BufferedImage source, BufferedImage target, double[] kernel) {
		int width = source.getWidth();
		int height = source.getHeight();
		int[] sourcePixels = ((DataBufferInt) source.getRaster().getDataBuffer()).getData();
		int[] targetPixels = ((DataBufferInt) target.getRaster().getDataBuffer()).getData();
		int kernelRadius = kernel.length / 2;

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				double sumR = 0, sumG = 0, sumB = 0, sumA = 0;

				for (int i = -kernelRadius; i <= kernelRadius; i++) {
					int py = Math.min(Math.max(y + i, 0), height - 1);
					int pixel = sourcePixels[py * width + x];

					double weight = kernel[i + kernelRadius];
					sumA += ((pixel >> 24) & 0xff) * weight;
					sumR += ((pixel >> 16) & 0xff) * weight;
					sumG += ((pixel >> 8) & 0xff) * weight;
					sumB += (pixel & 0xff) * weight;
				}

				targetPixels[y * width + x] = (((int) sumA & 0xff) << 24) | (((int) sumR & 0xff) << 16)
						| (((int) sumG & 0xff) << 8) | ((int) sumB & 0xff);
			}
		}
	}
}
