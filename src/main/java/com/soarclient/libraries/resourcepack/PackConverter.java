package com.soarclient.libraries.resourcepack;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.soarclient.libraries.resourcepack.impl.AnimationConverter;
import com.soarclient.libraries.resourcepack.impl.ArmorModelConverter;
import com.soarclient.libraries.resourcepack.impl.BlockStateConverter;
import com.soarclient.libraries.resourcepack.impl.MapIconConverter;
import com.soarclient.libraries.resourcepack.impl.ModelConverter;
import com.soarclient.libraries.resourcepack.impl.NameConverter;
import com.soarclient.libraries.resourcepack.impl.PackMetaConverter;
import com.soarclient.libraries.resourcepack.impl.PaintingConverter;
import com.soarclient.libraries.resourcepack.impl.ParticleSeparatorConverter;
import com.soarclient.libraries.resourcepack.impl.ParticleSizeChangeConverter;
import com.soarclient.libraries.resourcepack.impl.SoundsConverter;
import com.soarclient.libraries.resourcepack.impl.SpacesConverter;
import com.soarclient.libraries.resourcepack.impl.UnicodeFontConverter;
import com.soarclient.libraries.resourcepack.pack.Pack;
import com.soarclient.logger.SoarLogger;

public class PackConverter {

	protected final Gson gson;
	private final File dir;
	protected final MinecraftVersion version;

	protected final Map<Class<? extends Converter>, Converter> converters = new LinkedHashMap<>();

	public PackConverter(File dir, MinecraftVersion inVersion) {

		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.setPrettyPrinting();
		this.dir = dir;
		this.gson = gsonBuilder.create();
		this.version = inVersion;
		if (this.version == null) {
			SoarLogger.error("RPC", "Invalid version provided!");
			return;
		}

		// this needs to be run first, other converters might reference new directory
		// names
		this.registerConverter(new NameConverter(this));

		for (MinecraftVersion version : MinecraftVersion.values()) {
			this.registerConverter(new PackMetaConverter(this, version));
		}

		this.registerConverter(new ModelConverter(this));
		this.registerConverter(new SpacesConverter(this));
		this.registerConverter(new SoundsConverter(this));
		this.registerConverter(new ParticleSizeChangeConverter(this));
		this.registerConverter(new ParticleSeparatorConverter(this));
		this.registerConverter(new BlockStateConverter(this));
		this.registerConverter(new AnimationConverter(this));
		this.registerConverter(new MapIconConverter(this));
		this.registerConverter(new PaintingConverter(this));
		this.registerConverter(new UnicodeFontConverter(this));
		this.registerConverter(new ArmorModelConverter(this));
	}

	public void registerConverter(Converter converter) {
		converters.put(converter.getClass(), converter);
	}

	@SuppressWarnings("unchecked")
	public <T extends Converter> T getConverter(Class<T> clazz) {
		// noinspection unchecked
		return (T) converters.get(clazz);
	}

	public void run() throws IOException {
		Files.list(dir.toPath()).map(Pack::parse).filter(Objects::nonNull).forEach(pack -> {
			try {
				SoarLogger.info("RPC", "Converting " + pack);

				pack.getHandler().setup();

				SoarLogger.info("RPC", "Running Converters");
				for (Converter converter : converters.values()) {
					if (version.ordinal() < converter.getVersion().ordinal()) {
						continue;
					}
					// TODO: Obfuscate support
					SoarLogger.info("RPC", "Running " + converter.getClass().getSimpleName());
					converter.convert(pack);
				}

				pack.getHandler().finish();
			} catch (Exception e) {
				SoarLogger.error("RPC", "Failed to convert!", e);
			}
		});
	}

	public Gson getGson() {
		return gson;
	}

	@Override
	public String toString() {
		return "PackConverter{" + ", converters=" + converters + '}';
	}
}