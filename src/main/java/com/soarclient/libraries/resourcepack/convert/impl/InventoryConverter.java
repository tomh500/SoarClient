package com.soarclient.libraries.resourcepack.convert.impl;

import java.io.File;
import java.nio.file.Path;

import com.soarclient.libraries.resourcepack.convert.Converter;
import com.soarclient.libraries.resourcepack.utils.ImageConverter;

public class InventoryConverter extends Converter {

	@Override
	public void convert(File assetsDir) {
		
		try {
			
	        Path imagePath = new File(assetsDir, "assets/minecraft/textures/gui/container/inventory.png").toPath();
	        
	        if (!imagePath.toFile().exists())
	            return;

	        int defaultW = 256, defaultH = 256;
	        ImageConverter image = new ImageConverter(defaultW, defaultH, imagePath);

	        image.newImage(defaultH, defaultW);
	        image.subImage(0, 0, 256, 256, 0, 0);
	        image.subImage(0, 166, 16, 198, 0, 198);
	        image.subImage(104, 166, 120, 198, 16, 198);

	        image.store();
		} catch(Exception e) {}
	}
}
