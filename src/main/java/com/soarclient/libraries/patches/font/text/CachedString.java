package com.soarclient.libraries.patches.font.text;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public final class CachedString {

	private static final Queue<CachedString> POOL = new ConcurrentLinkedQueue<>();

	private String text;
	private int listId;
	private float height;
	private float width;
	private float lastRed;
	private float lastBlue;
	private float lastGreen;
	private float lastAlpha;

	private CachedString() {
	}

	public static CachedString create(String text, int listId, float width, float height) {
		CachedString cachedString = POOL.poll();
		if (cachedString == null) {
			cachedString = new CachedString();
		}
		cachedString.text = text;
		cachedString.listId = listId;
		cachedString.width = width;
		cachedString.height = height;
		return cachedString;
	}

	public void recycle() {
		text = null;
		listId = 0;
		width = 0;
		height = 0;
		lastRed = 0;
		lastBlue = 0;
		lastGreen = 0;
		lastAlpha = 0;
		POOL.offer(this);
	}
	
	public String getText() {
		return text;
	}

	public int getListId() {
		return listId;
	}

	public float getLastAlpha() {
		return lastAlpha;
	}

	public void setLastAlpha(float lastAlpha) {
		this.lastAlpha = lastAlpha;
	}

	public float getLastRed() {
		return lastRed;
	}

	public void setLastRed(float lastRed) {
		this.lastRed = lastRed;
	}

	public float getLastBlue() {
		return lastBlue;
	}

	public void setLastBlue(float lastBlue) {
		this.lastBlue = lastBlue;
	}

	public float getLastGreen() {
		return lastGreen;
	}

	public void setLastGreen(float lastGreen) {
		this.lastGreen = lastGreen;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float v) {
		this.width = v;
	}

	public float getHeight() {
		return height;
	}
}