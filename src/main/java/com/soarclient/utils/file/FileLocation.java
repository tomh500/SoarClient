package com.soarclient.utils.file;

import java.io.File;

import net.minecraft.client.Minecraft;

public class FileLocation {

	public static File SOAR_DIR = new File(Minecraft.getMinecraft().mcDataDir, "soar");
	public static File MUSIC_DIR = new File(SOAR_DIR, "music");
	public static File CACHE_DIR = new File(SOAR_DIR, "cache");
	public static File EXTERNAL_DIR = new File(SOAR_DIR, "external");

	public static void init() {
		FileUtils.createDir(SOAR_DIR);
		FileUtils.createDir(MUSIC_DIR);
		FileUtils.createDir(CACHE_DIR);
		FileUtils.createDir(EXTERNAL_DIR);
	}
}
