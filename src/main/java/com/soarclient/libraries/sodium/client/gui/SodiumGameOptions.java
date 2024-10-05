package com.soarclient.libraries.sodium.client.gui;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.soarclient.libraries.sodium.SodiumClientMod;
import com.soarclient.libraries.sodium.client.gui.options.FormattedTextProvider;

import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;

import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;

public class SodiumGameOptions {
	public final SodiumGameOptions.QualitySettings quality = new SodiumGameOptions.QualitySettings();
	public final SodiumGameOptions.AdvancedSettings advanced = new SodiumGameOptions.AdvancedSettings();
	public final SodiumGameOptions.PerformanceSettings performance = new SodiumGameOptions.PerformanceSettings();
	private Path configPath;
	private static final Gson GSON = new GsonBuilder()
			.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).setPrettyPrinting()
			.excludeFieldsWithModifiers(new int[] { 2 }).create();

	public static SodiumGameOptions load(Path path) {
		boolean resaveConfig = true;
		SodiumGameOptions config;
		if (Files.exists(path, new LinkOption[0])) {
			try {
				FileReader reader = new FileReader(path.toFile());

				try {
					config = (SodiumGameOptions) GSON.fromJson(reader, SodiumGameOptions.class);
				} catch (Throwable var8) {
					try {
						reader.close();
					} catch (Throwable var7) {
						var8.addSuppressed(var7);
					}

					throw var8;
				}

				reader.close();
			} catch (IOException var9) {
				throw new RuntimeException("Could not parse config", var9);
			} catch (JsonSyntaxException var10) {
				SodiumClientMod.logger().error("Could not parse config, will fallback to default settings", var10);
				config = new SodiumGameOptions();
				resaveConfig = false;
			}
		} else {
			config = new SodiumGameOptions();
		}

		config.configPath = path;

		try {
			if (resaveConfig) {
				config.writeChanges();
			}

			return config;
		} catch (IOException var6) {
			throw new RuntimeException("Couldn't update config file", var6);
		}
	}

	public void writeChanges() throws IOException {
		Path dir = this.configPath.getParent();
		if (!Files.exists(dir, new LinkOption[0])) {
			Files.createDirectories(dir);
		} else if (!Files.isDirectory(dir, new LinkOption[0])) {
			throw new IOException("Not a directory: " + dir);
		}

		Files.write(this.configPath, GSON.toJson(this).getBytes(StandardCharsets.UTF_8), new OpenOption[0]);
	}

	public static class AdvancedSettings {
		public boolean useVertexArrayObjects = true;
		public boolean useChunkMultidraw = true;
		public boolean animateOnlyVisibleTextures = true;
		public boolean useEntityCulling = true;
		public boolean useParticleCulling = true;
		public boolean useFogOcclusion = true;
		public boolean useCompactVertexFormat = true;
		public boolean useBlockFaceCulling = true;
		public boolean allowDirectMemoryAccess = true;
		public boolean ignoreDriverBlacklist = false;
		public boolean translucencySorting = false;
	}

	public static enum GraphicsQuality implements FormattedTextProvider {
		DEFAULT(new ChatComponentTranslation("generator.default", new Object[0])),
		FANCY(new ChatComponentTranslation("options.graphics.fancy", new Object[0])),
		FAST(new ChatComponentTranslation("options.graphics.fast", new Object[0]));

		private final IChatComponent name;

		private GraphicsQuality(IChatComponent name) {
			this.name = name;
		}

		@Override
		public IChatComponent getLocalizedName() {
			return this.name;
		}

		public boolean isFancy(boolean fancy) {
			return this == FANCY || this == DEFAULT && fancy;
		}
	}

	public static enum LightingQuality implements FormattedTextProvider {
		OFF(new ChatComponentTranslation("options.ao.off", new Object[0])),
		LOW(new ChatComponentTranslation("options.ao.min", new Object[0])),
		HIGH(new ChatComponentTranslation("options.ao.max", new Object[0]));

		private final IChatComponent name;

		private LightingQuality(IChatComponent name) {
			this.name = name;
		}

		@Override
		public IChatComponent getLocalizedName() {
			return this.name;
		}
	}

	public static class PerformanceSettings {
		public int chunkBuilderThreads = 0;
		public boolean alwaysDeferChunkUpdates = false;
	}

	public static class QualitySettings {
		public SodiumGameOptions.GraphicsQuality cloudQuality = SodiumGameOptions.GraphicsQuality.DEFAULT;
		public SodiumGameOptions.GraphicsQuality weatherQuality = SodiumGameOptions.GraphicsQuality.DEFAULT;
		public SodiumGameOptions.GraphicsQuality leavesQuality = SodiumGameOptions.GraphicsQuality.DEFAULT;
		public int biomeBlendRadius = 2;
		public float entityDistanceScaling = 1.0F;
		public boolean enableVignette = true;
		public boolean enableClouds = true;
		public int cloudHeight = 128;
		public boolean enableFog = true;
		public SodiumGameOptions.LightingQuality smoothLighting = SodiumGameOptions.LightingQuality.HIGH;
	}
}
