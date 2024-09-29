package com.soarclient.utils.file;

import java.io.File;
import java.io.IOException;

public class FileUtils {

	public static void createDir(File file) {
		if (!file.exists()) {
			file.mkdir();
		}
	}

	public static void createFile(File file) {
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
			}
		}
	}
}
