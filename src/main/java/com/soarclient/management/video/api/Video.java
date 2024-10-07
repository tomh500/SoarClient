package com.soarclient.management.video.api;

import java.io.File;

public class Video {

	private File file;
	private int hct;
	
	public Video(File file, int hct) {
		this.file = file;
		this.hct = hct;
	}

	public File getFile() {
		return file;
	}

	public int getHct() {
		return hct;
	}
}
