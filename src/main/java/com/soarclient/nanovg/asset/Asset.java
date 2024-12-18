package com.soarclient.nanovg.asset;

public class Asset {

	private int image;
	private int width;
	private int height;

	public Asset(int image, int width, int height) {
		this.image = image;
		this.width = width;
		this.height = height;
	}

	public int getImage() {
		return image;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
}