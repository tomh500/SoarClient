package com.soarclient.skia.image;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.soarclient.skia.utils.SkiaUtils;

import io.github.humbleui.skija.Image;

public class ImageHelper {

	private Map<String, Image> images = new HashMap<>();

	public boolean load(String filePath) {
	    if (!images.containsKey(filePath)) {
	        Optional<byte[]> encodedBytes = SkiaUtils.convertToBytes(filePath);
	        if (encodedBytes.isPresent()) {
	            images.put(filePath, Image.makeDeferredFromEncodedBytes(encodedBytes.get()));
	            return true;
	        } else {
	            return false;
	        }
	    }
	    return true;
	}

	public boolean load(File file) {

		if (!images.containsKey(file.getName())) {

			try {
				byte[] encoded = org.apache.commons.io.IOUtils.toByteArray(new FileInputStream(file));
				images.put(file.getName(), Image.makeDeferredFromEncodedBytes(encoded));
				return true;
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}

		return true;
	}

	public Image get(String path) {

		if (images.containsKey(path)) {
			return images.get(path);
		}

		return null;
	}
}
