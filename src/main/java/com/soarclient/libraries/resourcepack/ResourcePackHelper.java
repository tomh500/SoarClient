package com.soarclient.libraries.resourcepack;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.soarclient.utils.JsonUtils;
import com.soarclient.utils.Multithreading;
import com.soarclient.utils.file.FileLocation;
import com.soarclient.utils.file.FileUtils;

import net.minecraft.client.MinecraftClient;

public class ResourcePackHelper {

	private static MinecraftClient client = MinecraftClient.getInstance();

	public static void convertAll() {
		Multithreading.runAsync(() -> {

			List<File> packs = getOldResourcePacks();
			List<File> convertPacks = new ArrayList<>();

			for (File f : packs) {

				try (FileInputStream fis = new FileInputStream(f); ZipInputStream zipIn = new ZipInputStream(fis)) {

					ZipEntry entry;
					while ((entry = zipIn.getNextEntry()) != null) {
						if (!entry.isDirectory()) {
							if (entry.getName().equals("pack.mcmeta")) {

								JsonObject jsonObject = readJsonFromZip(zipIn);
								JsonObject packJsonObject = JsonUtils.getObjectProperty(jsonObject, "pack");

								if (packJsonObject != null) {

									int version = JsonUtils.getIntProperty(packJsonObject, "pack_format", -1);
									boolean convert = JsonUtils.getBooleanProperty(packJsonObject, "convert",
											false);

									if (version == 1 || (version != ResourcePackUpdater.VERSION && convert)) {
										convertPacks.add(f);
									}
								}
							}
						}
						zipIn.closeEntry();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			for(File f : convertPacks) {
				convert(f);
			}
		});
	}

	private static void convert(File f) {
		
		File cacheDir = new File(FileLocation.CACHE_DIR, "resourcepack");
		File tempDir = new File(cacheDir, "temp");
		
		try {
			
			File targetFile = new File(cacheDir, FileUtils.getMd5Checksum(f));
			File packDir = new File(client.runDirectory, "resourcepacks");
			File outputFile = new File(packDir, f.getName());
			
			Files.createDirectories(tempDir.toPath());
			Files.move(f.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
			
			ResourcePackUpdater updater = new ResourcePackUpdater(targetFile, outputFile,
					tempDir);
			
			updater.run();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static List<File> getOldResourcePacks() {

		List<File> files = new ArrayList<>();
		File packDir = new File(client.runDirectory, "resourcepacks");

		for (File f : packDir.listFiles()) {
			if (f.getName().endsWith(".zip")) {
				files.add(f);
			}
		}

		return files;
	}

	private static JsonObject readJsonFromZip(ZipInputStream zipIn) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len;
		while ((len = zipIn.read(buffer)) > 0) {
			baos.write(buffer, 0, len);
		}
		String jsonString = baos.toString("UTF-8");
		return JsonParser.parseString(jsonString).getAsJsonObject();
	}
}
