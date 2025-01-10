package com.soarclient.utils.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FileUtils {

	public static String getMd5Checksum(File file) throws IOException, NoSuchAlgorithmException {

		MessageDigest md = MessageDigest.getInstance("MD5");
		FileInputStream fis = new FileInputStream(file);
		DigestInputStream dis = new DigestInputStream(fis, md);

		byte[] buffer = new byte[8192];
		int bytesRead;

		while ((bytesRead = dis.read(buffer)) != -1) {
			md.update(buffer, 0, bytesRead);
		}

		byte[] digest = md.digest();

		StringBuilder result = new StringBuilder();
		for (byte b : digest) {
			result.append(String.format("%02x", b));
		}

		dis.close();
		fis.close();

		return result.toString();
	}

	public static String getExtension(String fileName) {

		if (fileName == null) {
			return null;
		}

		int lastIndexOf = fileName.lastIndexOf(".");

		if (lastIndexOf == -1) {
			return "null";
		}

		return fileName.substring(lastIndexOf + 1);
	}

	public static String getExtension(File file) {
		return getExtension(file.getName());
	}

	public static void createFile(File file) {
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void createDir(File file) {
		file.mkdir();
	}
}
