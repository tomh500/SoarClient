package com.soarclient.libraries.resourcepack.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.soarclient.libraries.resourcepack.Converter;
import com.soarclient.libraries.resourcepack.MinecraftVersion;
import com.soarclient.libraries.resourcepack.PackConverter;
import com.soarclient.libraries.resourcepack.Util;
import com.soarclient.libraries.resourcepack.pack.Pack;
import com.soarclient.logger.SoarLogger;

public class SpacesConverter extends Converter {

    public SpacesConverter(PackConverter packConverter) {
        super(packConverter);
    }

    @Override
    public MinecraftVersion getVersion() {
        return MinecraftVersion.v1_13;
    }

    @Override
    public void convert(Pack pack) throws IOException {
        Path assets = pack.getWorkingPath().resolve("assets");
        if (!assets.toFile().exists()) return;

        Files.walk(assets).forEach(path -> {
            if (!path.getFileName().toString().contains(" ")) return;

            String noSpaces = path.getFileName().toString().replaceAll(" ", "_");
            Boolean ret = Util.renameFile(path, noSpaces);
            if (ret == null) return;
            if (ret) {
            	SoarLogger.info("RPC", "Renamed: " + path.getFileName().toString() + "->" + noSpaces);
            } else if (!ret) {
            	SoarLogger.error("RPC", "Failed to rename: " + path.getFileName().toString() + "->" + noSpaces);
            }
        });
    }

}
