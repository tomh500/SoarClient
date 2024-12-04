package com.soarclient.nanovg.asset;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.nanovg.NanoVG;
import org.lwjgl.nanovg.NanoVGGL3;
import org.lwjgl.stb.STBImage;

import com.soarclient.utils.IOUtils;

import net.minecraft.util.ResourceLocation;

public class AssetHelper {

	private static AssetHelper instance = new AssetHelper();

	private Map<String, Asset> imageCache = new HashMap<>();
	private Map<Integer, Integer> glTextureCache = new HashMap<>();

	public boolean loadImage(long nvg, int texture, float width, float height) {

		if (!glTextureCache.containsKey(texture)) {

			glTextureCache.put(texture,
					NanoVGGL3.nvglCreateImageFromHandle(nvg, texture, (int) width, -(int) height, 0));

			return true;
		}

		return true;
	}

	public boolean loadImage(long nvg, ResourceLocation location, AssetFlag assetFlag) {

		if (!imageCache.containsKey(location.getResourcePath())) {

			int[] width = { 0 };
			int[] height = { 0 };
			int[] channels = { 0 };

			ByteBuffer image = IOUtils.resourceToByteBuffer(location);

			if (image == null) {
				return false;
			}

			ByteBuffer buffer = STBImage.stbi_load_from_memory(image, width, height, channels, 4);

			if (buffer == null) {
				return false;
			}

			imageCache.put(location.getResourcePath(),
					new Asset(NanoVG.nvgCreateImageRGBA(nvg, width[0], height[0], assetFlag.getFlags(), buffer),
							width[0], height[0]));

			return true;
		}

		return true;
	}

	public boolean loadImage(long nvg, File file, AssetFlag assetFlag) {

		if (!imageCache.containsKey(file.getName())) {

			int[] width = { 0 };
			int[] height = { 0 };
			int[] channels = { 0 };

			ByteBuffer image = IOUtils.resourceToByteBuffer(file);

			if (image == null) {
				return false;
			}

			ByteBuffer buffer = STBImage.stbi_load_from_memory(image, width, height, channels, 4);

			if (buffer == null) {
				return false;
			}

			imageCache.put(file.getName(),
					new Asset(NanoVG.nvgCreateImageRGBA(nvg, width[0], height[0], assetFlag.getFlags(), buffer),
							width[0], height[0]));

			return true;
		}

		return true;
	}

	public int getImage(File file) {

		String fileName = file.getName();

		if (imageCache.containsKey(fileName)) {
			return imageCache.get(fileName).getImage();
		}

		return 0;
	}

	public int getImage(ResourceLocation location) {

		String path = location.getResourcePath();

		if (imageCache.containsKey(path)) {
			return imageCache.get(path).getImage();
		}

		return 0;
	}

	public int getImage(int texture) {

		if (glTextureCache.containsKey(texture)) {
			return glTextureCache.get(texture);
		}

		return 0;
	}

	public void removeImage(long nvg, File file) {

		String fileName = file.getName();

		if (imageCache.containsKey(fileName)) {
			NanoVG.nvgDeleteImage(nvg, imageCache.get(fileName).getImage());
			imageCache.remove(fileName);
		}
	}

	public void removeImage(long nvg, ResourceLocation location) {

		String path = location.getResourcePath();

		if (imageCache.containsKey(path)) {
			NanoVG.nvgDeleteImage(nvg, imageCache.get(path).getImage());
			imageCache.remove(path);
		}
	}

	public static AssetHelper getInstance() {
		return instance;
	}
}