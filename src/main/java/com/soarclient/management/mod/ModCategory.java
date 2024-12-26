package com.soarclient.management.mod;

import java.util.ArrayList;
import java.util.List;

public enum ModCategory {
	ALL("category.all"), HUD("category.hud"), RENDER("category.render"), PLAYER("category.player"),
	MISC("category.misc");

	private String title;

	private ModCategory(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public static List<String> getTitles() {

		List<String> output = new ArrayList<>();

		for (ModCategory c : ModCategory.values()) {
			output.add(c.getTitle());
		}

		return output;
	}
}