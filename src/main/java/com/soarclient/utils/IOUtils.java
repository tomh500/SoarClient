package com.soarclient.utils;

import java.io.File;
import java.io.FileInputStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class IOUtils {

	private static Minecraft mc = Minecraft.getMinecraft();

	public static ByteBuffer resourceToByteBuffer(ResourceLocation location) {

		try {
			byte[] bytes = org.apache.commons.io.IOUtils
					.toByteArray(mc.getResourceManager().getResource(location).getInputStream());

			ByteBuffer data = ByteBuffer.allocateDirect(bytes.length).order(ByteOrder.nativeOrder()).put(bytes);
			((Buffer) data).flip();

			return data;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static ByteBuffer resourceToByteBuffer(File file) {

		try {
			byte[] bytes = org.apache.commons.io.IOUtils.toByteArray(new FileInputStream(file));

			ByteBuffer data = ByteBuffer.allocateDirect(bytes.length).order(ByteOrder.nativeOrder()).put(bytes);
			((Buffer) data).flip();

			return data;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
}