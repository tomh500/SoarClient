package com.soarclient.nanovg.asset;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.nanovg.NanoVG;
import org.lwjgl.nanovg.NanoVGGL2;
import org.lwjgl.stb.STBImage;

import com.soarclient.utils.IOUtils;

public class AssetHelper {

	private static AssetHelper instance = new AssetHelper();

	private Map<String, Asset> imageCache = new HashMap<>();
	private Map<Integer, Integer> glTextureCache = new HashMap<>();

	public boolean loadImage(long nvg, int texture, float width, float height) {

		if (!glTextureCache.containsKey(texture)) {

			glTextureCache.put(texture,
					NanoVGGL2.nvglCreateImageFromHandle(nvg, texture, (int) width, -(int) height, 0));

			return true;
		}

		return true;
	}

	public boolean loadImage(long nvg, String filePath, AssetFlag assetFlag) {

		if (!imageCache.containsKey(filePath)) {

			int[] width = { 0 };
			int[] height = { 0 };
			int[] channels = { 0 };

			ByteBuffer image = IOUtils.resourceToByteBuffer(filePath);

			if (image == null) {
				return false;
			}

			ByteBuffer buffer = STBImage.stbi_load_from_memory(image, width, height, channels, 4);

			if (buffer == null) {
				return false;
			}

			imageCache.put(filePath,
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

	public int getImage(String filePath) {
		
		if (imageCache.containsKey(filePath)) {
			return imageCache.get(filePath).getImage();
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

	public void removeImage(long nvg, String filePath) {
		if (imageCache.containsKey(filePath)) {
			NanoVG.nvgDeleteImage(nvg, imageCache.get(filePath).getImage());
			imageCache.remove(filePath);
		}
	}

	public static AssetHelper getInstance() {
		return instance;
	}
}