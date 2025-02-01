package com.soarclient.libraries.soarium.config;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import com.soarclient.libraries.soarium.render.chunk.translucent_sorting.SortBehavior;
import com.soarclient.utils.file.FileLocation;
import com.soarclient.utils.file.FileUtils;

public class SoariumConfig {

	private static final Gson GSON = new GsonBuilder()
			.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).setPrettyPrinting()
			.excludeFieldsWithModifiers(Modifier.PRIVATE).create();

	public final QualitySettings quality = new QualitySettings();
	public final AdvancedSettings advanced = new AdvancedSettings();
	public final PerformanceSettings performance = new PerformanceSettings();
	public final CullingSettings culling = new CullingSettings();
	public final AnimationSettings animationSettings = new AnimationSettings();
	public final DetailSettings detailSettings = new DetailSettings();

	public static class PerformanceSettings {

		@SerializedName("chunkBuilderThreads")
		public int chunkBuilderThreads = 0;

		@SerializedName("alwaysDeferChunkUpdates")
		public boolean alwaysDeferChunkUpdates = true;

		@SerializedName("animateOnlyVisibleTextures")
		public boolean animateOnlyVisibleTextures = true;

		@SerializedName("useEntityCulling")
		public boolean useEntityCulling = true;

		@SerializedName("useFogOcclusion")
		public boolean useFogOcclusion = true;

		@SerializedName("useBlockFaceCulling")
		public boolean useBlockFaceCulling = true;

		@SerializedName("smartCull")
		public boolean smartCull = false;

		@SerializedName("sortingEnabled")
		public boolean sortingEnabled = true;

		public SortBehavior getSortBehavior() {
			return this.sortingEnabled ? SortBehavior.DYNAMIC_DEFER_NEARBY_ZERO_FRAMES : SortBehavior.OFF;
		}
	}

	public static class AdvancedSettings {

		@SerializedName("enableMemoryTracing")
		public boolean enableMemoryTracing = false;

		@SerializedName("useAdvancedStagingBuffers")
		public boolean useAdvancedStagingBuffers = true;

		@SerializedName("cpuRenderAhead")
		public boolean cpuRenderAhead = true;

		@SerializedName("cpuRenderAheadLimit")
		public int cpuRenderAheadLimit = 3;
	}

	public static class QualitySettings {

		public transient String[] lightningQualities = { "soarium.off", "soarium.min", "soarium.max" };
		public transient String[] qualities = { "soarium.default", "soarium.fancy", "soarium.fast" };

		@SerializedName("enableClouds")
		public boolean enableClouds = false;

		@SerializedName("cloudHeight")
		public int cloudHeight = 160;

		@SerializedName("cloudQuality")
		public String cloudQuality = qualities[0];

		@SerializedName("weatherQuality")
		public String weatherQuality = qualities[0];

		@SerializedName("leavesQuality")
		public String leavesQuality = qualities[0];

		@SerializedName("smoothLighting")
		public String smoothLighting = lightningQualities[2];

		@SerializedName("biomeBlendRadius")
		public int biomeBlendRadius = 2;

		@SerializedName("enableVignette")
		public boolean enableVignette = true;

	}

	public static class CullingSettings {

		public transient Set<String> blockEntityWhitelist = new HashSet<>(Arrays.asList("tile.beacon"));

		@SerializedName("tracingDistance")
		public int tracingDistance = 128;

		@SerializedName("sleepDelay")
		public int sleepDelay = 10;

		@SerializedName("hitboxLimit")
		public int hitboxLimit = 50;

		@SerializedName("skipMarkerArmorStands")
		public boolean skipMarkerArmorStands = true;

		@SerializedName("renderNametagsThroughWalls")
		public boolean renderNametagsThroughWalls = true;
	}

	public static class AnimationSettings {

		@SerializedName("animation")
		public boolean animation = true;
		
		@SerializedName("water")
		public boolean water = true;

		@SerializedName("lava")
		public boolean lava = true;

		@SerializedName("fire")
		public boolean fire = true;

		@SerializedName("portal")
		public boolean portal = true;

		@SerializedName("blockAnimations")
		public boolean blockAnimations = true;

		@SerializedName("particles")
		public boolean particles = true;

		@SerializedName("rainSplash")
		public boolean rainSplash = true;

		@SerializedName("blockBreak")
		public boolean blockBreak = true;

		@SerializedName("blockBreaking")
		public boolean blockBreaking = true;
		
	}

	public static class DetailSettings {

		@SerializedName("sky")
		public boolean sky = true;

		@SerializedName("sun")
		public boolean sun = true;

		@SerializedName("moon")
		public boolean moon = true;

		@SerializedName("stars")
		public boolean stars = true;

		@SerializedName("rainSnow")
		public boolean rainSnow = true;

		@SerializedName("biomeColors")
		public boolean biomeColors = true;

		@SerializedName("skyColors")
		public boolean skyColors = true;
		
		@SerializedName("itemFrame")
		public boolean itemFrame = true;

		@SerializedName("armorStand")
		public boolean armorStand = true;

		@SerializedName("painting")
		public boolean painting = true;

		@SerializedName("piston")
		public boolean piston = true;

		@SerializedName("beaconBeam")
		public boolean beaconBeam = true;

		@SerializedName("enchantingTableBook")
		public boolean enchantingTableBook = true;

		@SerializedName("itemFrameNameTag")
		public boolean itemFrameNameTag = true;

		@SerializedName("playerNameTag")
		public boolean playerNameTag = true;
	}

	public boolean isFancy(String input, boolean fancy) {

		if (input.contains("default")) {
			return fancy;
		}

		return (input.contains("fancy")) || fancy;
	}

	public static SoariumConfig load() {

		SoariumConfig config;

		if (getSaveFile().exists()) {
			try (FileReader reader = new FileReader(getSaveFile())) {
				config = GSON.fromJson(reader, SoariumConfig.class);
			} catch (IOException e) {
				config = new SoariumConfig();
			}
		} else {
			config = new SoariumConfig();
		}

		return config;
	}

	public static void save(SoariumConfig config) {

		File f = getSaveFile();

		if (!f.exists()) {
			FileUtils.createFile(f);
		}

		try (FileWriter writer = new FileWriter(f)) {
			GSON.toJson(config, writer);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static File getSaveFile() {
		return new File(FileLocation.CONFIG_DIR, "soarium.json");
	}
}
