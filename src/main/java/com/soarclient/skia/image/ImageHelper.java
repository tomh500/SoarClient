package com.soarclient.skia.image;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.lwjgl.opengl.GL11;

import com.soarclient.skia.context.SkiaContext;
import com.soarclient.skia.utils.SkiaUtils;

import io.github.humbleui.skija.ColorType;
import io.github.humbleui.skija.Image;
import io.github.humbleui.skija.SurfaceOrigin;

public class ImageHelper {

	private Map<String, Image> images = new HashMap<>();
	private Map<Integer, Image> textures = new HashMap<>();

    public boolean load(int texture, float width, float height, SurfaceOrigin origin) {
        if (!textures.containsKey(texture)) {
            textures.put(texture, Image.adoptTextureFrom(SkiaContext.getContext(), texture, GL11.GL_TEXTURE_2D, (int) width,
                    (int) height, GL11.GL_RGBA8, origin, ColorType.RGBA_8888));
        }
        return true;
    }

	
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
	
	public Image get(int texture) {

		if (textures.containsKey(texture)) {
			return textures.get(texture);
		}

		return null;
	}
}
