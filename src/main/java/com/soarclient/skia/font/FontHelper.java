package com.soarclient.skia.font;

import java.io.Closeable;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.soarclient.skia.utils.SkiaUtils;

import io.github.humbleui.skija.Data;
import io.github.humbleui.skija.Font;
import io.github.humbleui.skija.Typeface;

public class FontHelper {

	private static final Map<String, Font> fontCache = new HashMap<>();
	private static final Map<String, Typeface> typefaceCache = new HashMap<>();

	private static Typeface getTypeface(String font, FontType type) {
		return typefaceCache.computeIfAbsent(font, k -> loadTypeface(k, type));
	}

	private static Typeface loadTypeface(String font, FontType type) {
		Optional<Data> fontDataOptional = SkiaUtils.convertToData("/assets/soar/fonts/" + font);
		return fontDataOptional.map(Typeface::makeFromData)
				.orElseThrow(() -> new IllegalArgumentException("Font not found: " + font));
	}

	public static Font load(String font, float size, FontType fontType) {
		String key = font + "-" + size;
		return fontCache.computeIfAbsent(key, k -> {
			Font f = new Font(getTypeface(font, fontType), size);
			if (f instanceof Closeable) {
				try {
					((Closeable) f).close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return f;
		});
	}

	public static Font load(String font, float size) {
		return load(font, size, getFontType(font));
	}

	private static FontType getFontType(String font) {
		String fileExtension = font.substring(font.lastIndexOf('.') + 1).toLowerCase();
		return FontType.fromString(fileExtension);
	}
}
