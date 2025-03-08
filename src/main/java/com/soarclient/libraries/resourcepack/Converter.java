package com.soarclient.libraries.resourcepack;

import java.io.IOException;

import com.soarclient.libraries.resourcepack.pack.Pack;

public abstract class Converter {

    protected PackConverter packConverter;

    public Converter(PackConverter packConverter) {
        this.packConverter = packConverter;
    }

    public abstract MinecraftVersion getVersion();

    public abstract void convert(Pack pack) throws IOException;

}