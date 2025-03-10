package com.soarclient.libraries.resourcepack;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.soarclient.libraries.resourcepack.convert.Converter;
import com.soarclient.libraries.resourcepack.convert.impl.ArmorModelConverter;
import com.soarclient.libraries.resourcepack.convert.impl.ChestModelConverter;
import com.soarclient.libraries.resourcepack.convert.impl.CompassConverter;
import com.soarclient.libraries.resourcepack.convert.impl.CreativeTabsConverter;
import com.soarclient.libraries.resourcepack.convert.impl.DirectoryConverter;
import com.soarclient.libraries.resourcepack.convert.impl.EnchantConverter;
import com.soarclient.libraries.resourcepack.convert.impl.ImageFormatConverter;
import com.soarclient.libraries.resourcepack.convert.impl.InventoryConverter;
import com.soarclient.libraries.resourcepack.convert.impl.NameConverter;
import com.soarclient.libraries.resourcepack.convert.impl.OffHandConverter;
import com.soarclient.libraries.resourcepack.convert.impl.PackMetaConverter;
import com.soarclient.libraries.resourcepack.convert.impl.ParticleSizeChangeConverter;
import com.soarclient.libraries.resourcepack.convert.impl.SlidersConverter;
import com.soarclient.libraries.resourcepack.convert.impl.WaterConverter;
import com.soarclient.libraries.resourcepack.convert.impl.slicer.impl.V1_14_SliceConverter;
import com.soarclient.libraries.resourcepack.convert.impl.slicer.impl.V1_20_2_SliceConverter;
import com.soarclient.libraries.resourcepack.convert.impl.slicer.impl.V1_20_5_SliceConverter;
import com.soarclient.utils.file.FileUtils;

public class ResourcePackUpdater {

	public static final int VERSION = 46;
	
	private List<Converter> converters = new ArrayList<>();
	
	private final File inputFile;
	private final File outputFile;
	private final File tempDir;
	
	public ResourcePackUpdater(File inputFile, File outputFile, File tempDir) {
		this.inputFile = inputFile;
		this.outputFile = outputFile;
		this.tempDir = tempDir;
		
		this.converters.add(new ImageFormatConverter());
		this.converters.add(new DirectoryConverter());
		this.converters.add(new NameConverter());
		this.converters.add(new ArmorModelConverter());
		this.converters.add(new ParticleSizeChangeConverter());
		this.converters.add(new ChestModelConverter());
		this.converters.add(new OffHandConverter());
		this.converters.add(new CompassConverter());
		this.converters.add(new CreativeTabsConverter());
		this.converters.add(new InventoryConverter());
		this.converters.add(new WaterConverter());
		this.converters.add(new SlidersConverter());
		this.converters.add(new EnchantConverter());
		this.converters.add(new V1_14_SliceConverter());
		this.converters.add(new V1_20_2_SliceConverter());
		this.converters.add(new V1_20_5_SliceConverter());
		this.converters.add(new PackMetaConverter());
	}
	
	public ResourcePackUpdater(File inputFile, File outputFile) {
		this(inputFile, outputFile, new File("./"));
	}
	
	public void run() throws Exception {
		
		if(tempDir.exists()) {
			FileUtils.deleteDirectory(tempDir.toPath());
		}
		
		if(inputFile.exists()) {
			FileUtils.unzip(inputFile, tempDir);
		}
		
		if(tempDir.exists()) {
			for(Converter converter : converters) {
				converter.convert(tempDir);
			}
		}
		
		FileUtils.zip(tempDir, outputFile);
	}
}
