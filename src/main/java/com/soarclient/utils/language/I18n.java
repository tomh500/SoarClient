package com.soarclient.utils.language;

import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public final class I18n {

	private static final Object2ObjectMap<String, String> translateMap = new Object2ObjectOpenHashMap<>();
	private static Language currentLanguage;

	private I18n() {
	}

	public static void setLanguage(Language language) {
		Objects.requireNonNull(language, "Language cannot be null");
		currentLanguage = language;
		load(language);
	}

	private static void load(Language language) {

		String resourcePath = String.format("assets/soar/languages/%s.lang", language.getId());
		translateMap.clear();

		try (BufferedReader reader = new BufferedReader(new InputStreamReader(
				I18n.class.getClassLoader().getResourceAsStream(resourcePath), StandardCharsets.UTF_8))) {

			reader.lines().filter(line -> !line.isEmpty() && !line.startsWith("#")).map(line -> line.split("=", 2))
					.filter(parts -> parts.length == 2)
					.forEach(parts -> translateMap.put(parts[0].trim(), parts[1].trim()));
		} catch (IOException e) {
			throw new RuntimeException("Failed to load language file: " + resourcePath, e);
		} catch (NullPointerException e) {
			throw new RuntimeException("Language file not found: " + resourcePath, e);
		}
	}

	public static String get(String key) {
		Objects.requireNonNull(key, "Translation key cannot be null");
		return translateMap.getOrDefault(key, "null");
	}

	public static Language getCurrentLanguage() {
		return currentLanguage;
	}
}