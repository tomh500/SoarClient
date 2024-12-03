package com.soarclient.nanovg.font;

import java.nio.ByteBuffer;

public class Font {

	private String name;
	private String filePath;
	private boolean loaded;
	private ByteBuffer buffer;

	public Font(String name, String filePath) {
		this.name = name;
		this.filePath = filePath;
		this.loaded = false;
		this.buffer = null;
	}

	public boolean isLoaded() {
		return loaded;
	}

	public void setLoaded(boolean loaded) {
		this.loaded = loaded;
	}

	public ByteBuffer getBuffer() {
		return buffer;
	}

	public void setBuffer(ByteBuffer buffer) {
		this.buffer = buffer;
	}

	public String getFilePath() {
		return filePath;
	}

	public String getName() {
		return name;
	}
}