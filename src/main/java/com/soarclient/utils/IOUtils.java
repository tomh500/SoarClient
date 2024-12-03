package com.soarclient.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;

public class IOUtils {

	public static ByteBuffer resourceToByteBuffer(String path) {
		
		try {
			byte[] bytes;
			path = path.trim();
			InputStream stream;
			File file = new File(path);
			if (file.exists() && file.isFile()) {
				stream = Files.newInputStream(file.toPath());
			} else {
				stream = IOUtils.class.getResourceAsStream(path);
			}
			if (stream == null) {
				throw new FileNotFoundException(path);
			}
			bytes = org.apache.commons.io.IOUtils.toByteArray(stream);
			ByteBuffer data = ByteBuffer.allocateDirect(bytes.length).order(ByteOrder.nativeOrder()).put(bytes);
			((Buffer) data).flip();
			return data;
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
