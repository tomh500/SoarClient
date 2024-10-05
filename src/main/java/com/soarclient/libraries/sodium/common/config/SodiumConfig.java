package com.soarclient.libraries.sodium.common.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SodiumConfig {
	private static final Logger LOGGER = LogManager.getLogger("OldiumConfig");
	private static final String JSON_KEY_SODIUM_OPTIONS = "sodium:options";
	private static final Set<String> SYSTEM_OPTIONS = (Set<String>) Stream.of("core", "features.chunk_rendering")
			.map(SodiumConfig::getMixinRuleName).collect(Collectors.toSet());
	private final Map<String, Option> options = new HashMap();

	private SodiumConfig() {
		this.addMixinRule("core", true);
		this.addMixinRule("features.block", true);
		this.addMixinRule("features.buffer_builder", true);
		this.addMixinRule("features.buffer_builder.fast_advance", true);
		this.addMixinRule("features.buffer_builder.fast_sort", true);
		this.addMixinRule("features.buffer_builder.intrinsics", true);
		this.addMixinRule("features.chunk_rendering", true);
		this.addMixinRule("features.debug", true);
		this.addMixinRule("features.entity", true);
		this.addMixinRule("features.entity.fast_render", true);
		this.addMixinRule("features.entity.smooth_lighting", true);
		this.addMixinRule("features.gui", true);
		this.addMixinRule("features.gui.fast_loading_screen", true);
		this.addMixinRule("features.gui.fast_status_bars", true);
		this.addMixinRule("features.gui.fast_fps_pie", true);
		this.addMixinRule("features.gui.font", true);
		this.addMixinRule("features.item", true);
		this.addMixinRule("features.matrix_stack", true);
		this.addMixinRule("features.model", true);
		this.addMixinRule("features.optimized_bamboo", true);
		this.addMixinRule("features.options", true);
		this.addMixinRule("features.particle", true);
		this.addMixinRule("features.particle.cull", true);
		this.addMixinRule("features.particle.fast_render", true);
		this.addMixinRule("features.render_layer", true);
		this.addMixinRule("features.render_layer.leaves", true);
		this.addMixinRule("features.sky", true);
		this.addMixinRule("features.texture_tracking", true);
		this.addMixinRule("features.world_ticking", true);
		this.addMixinRule("features.fast_biome_colors", true);
	}

	private void addMixinRule(String mixin, boolean enabled) {
		String name = getMixinRuleName(mixin);
		if (this.options.putIfAbsent(name, new Option(name, enabled, false)) != null) {
			throw new IllegalStateException("Mixin rule already defined: " + mixin);
		}
	}

	private void readProperties(Properties props) {
		for (Entry<Object, Object> entry : props.entrySet()) {
			String key = (String) entry.getKey();
			String value = (String) entry.getValue();
			Option option = (Option) this.options.get(key);
			if (option == null) {
				LOGGER.warn("No configuration key exists with name '{}', ignoring", new Object[] { key });
			} else {
				boolean enabled;
				if (value.equalsIgnoreCase("true")) {
					enabled = true;
				} else {
					if (!value.equalsIgnoreCase("false")) {
						LOGGER.warn("Invalid value '{}' encountered for configuration key '{}', ignoring",
								new Object[] { value, key });
						continue;
					}

					enabled = false;
				}

				if (!enabled && SYSTEM_OPTIONS.contains(key)) {
					LOGGER.warn("Configuration key '{}' is a required option and cannot be disabled",
							new Object[] { key });
				} else {
					option.setEnabled(enabled, true);
				}
			}
		}
	}

	public Option getEffectiveOptionForMixin(String mixinClassName) {
		int lastSplit = 0;
		Option rule = null;

		int nextSplit;
		while ((nextSplit = mixinClassName.indexOf(46, lastSplit)) != -1) {
			String key = getMixinRuleName(mixinClassName.substring(0, nextSplit));
			Option candidate = (Option) this.options.get(key);
			if (candidate != null) {
				rule = candidate;
				if (!candidate.isEnabled()) {
					return candidate;
				}
			}

			lastSplit = nextSplit + 1;
		}

		return rule;
	}

	public static SodiumConfig load(File file) {
		if (!file.exists()) {
			try {
				writeDefaultConfig(file);
			} catch (IOException var6) {
				LOGGER.warn("Could not write default configuration file", var6);
			}

			return new SodiumConfig();
		} else {
			Properties props = new Properties();

			try {
				FileInputStream fin = new FileInputStream(file);

				try {
					props.load(fin);
				} catch (Throwable var7) {
					try {
						fin.close();
					} catch (Throwable var5) {
						var7.addSuppressed(var5);
					}

					throw var7;
				}

				fin.close();
			} catch (IOException var8) {
				throw new RuntimeException("Could not load config file", var8);
			}

			SodiumConfig config = new SodiumConfig();
			config.readProperties(props);
			return config;
		}
	}

	private static void writeDefaultConfig(File file) throws IOException {
		File dir = file.getParentFile();
		if (!dir.exists()) {
			if (!dir.mkdirs()) {
				throw new IOException("Could not create parent directories");
			}
		} else if (!dir.isDirectory()) {
			throw new IOException("The parent file is not a directory");
		}

		Writer writer = new FileWriter(file);

		try {
			writer.write("# This is the configuration file for Oldium.\n");
			writer.write("#\n");
			writer.write("# You can find information on editing this file and all the available options here:\n");
			writer.write("# https://github.com/jellysquid3/sodium-fabric/wiki/Configuration-File\n");
			writer.write("#\n");
			writer.write("# By default, this file will be empty except for this notice.\n");
		} catch (Throwable var6) {
			try {
				writer.close();
			} catch (Throwable var5) {
				var6.addSuppressed(var5);
			}

			throw var6;
		}

		writer.close();
	}

	private static String getMixinRuleName(String name) {
		return "mixin." + name;
	}

	public int getOptionCount() {
		return this.options.size();
	}

	public int getOptionOverrideCount() {
		return (int) this.options.values().stream().filter(Option::isOverridden).count();
	}
}
