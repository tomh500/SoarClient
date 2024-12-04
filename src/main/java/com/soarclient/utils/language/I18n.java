package com.soarclient.utils.language;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class I18n {

	private static Map<String, String> translateMap = new HashMap<>();
	private static Language currentLanguage;

	public static void setLanguage(Language language) {
		currentLanguage = language;
		load(language);
	}

	private static void load(Language language) {

		try (BufferedReader reader = new BufferedReader(
				new InputStreamReader(
						I18n.class.getClassLoader()
								.getResourceAsStream("assets/minecraft/soar/languages/" + language.getId() + ".lang"),
						StandardCharsets.UTF_8))) {

			String s;

			while ((s = reader.readLine()) != null) {
				if (!s.equals("") && !s.startsWith("#")) {

					String[] args = s.split("=");

					translateMap.put(args[0], args[1]);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String get(String key) {

		if (translateMap.containsKey(key)) {
			return translateMap.get(key);
		}

		return "null";
	}

	public static Language getCurrentLanguage() {
		return currentLanguage;
	}
}