package com.soarclient.libraries.resourcepack;

import java.io.IOException;

import com.soarclient.libraries.resourcepack.pack.Pack;

public abstract class Converter {

	private final String name;
	protected PackConverter packConverter;

	public Converter(String name, PackConverter packConverter) {
		this.name = name;
		this.packConverter = packConverter;
	}

	public abstract MinecraftVersion getVersion();

	public abstract void convert(Pack pack) throws IOException;

	public String getName() {
		return name;
	}
}