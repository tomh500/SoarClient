package com.soarclient.management.profile;

import java.io.File;
import java.util.List;

import com.google.gson.JsonObject;
import com.soarclient.management.config.ConfigType;
import com.soarclient.utils.tuples.Pair;

public class Profile {

	private final String name, author;
	private final List<Pair<ConfigType, JsonObject>> configs;
	private final File icon;
	
	public Profile(String name, String author, List<Pair<ConfigType, JsonObject>> configs, File icon) {
		this.name = name;
		this.author = author;
		this.configs = configs;
		this.icon = icon;
	}

	public String getName() {
		return name;
	}

	public String getAuthor() {
		return author;
	}

	public List<Pair<ConfigType, JsonObject>> getConfigs() {
		return configs;
	}

	public File getIcon() {
		return icon;
	}
}
