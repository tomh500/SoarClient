package com.soarclient.management.mod.settings.impl;

import java.io.File;

import com.soarclient.management.mod.Mod;
import com.soarclient.management.mod.settings.Setting;

public class FileSetting extends Setting {

	private final File defaultValue;
	private File file;
	
	public FileSetting(String name, String description, String icon, Mod parent, File file) {
		super(name, description, icon, parent);
		this.defaultValue = file;
		this.file = file;
	}

	@Override
	public void reset() {
		this.file = this.defaultValue;
	}

	public File getDefaultValue() {
		return defaultValue;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}
}
