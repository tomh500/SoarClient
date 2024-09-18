package com.soarclient.libraries.sodium.client.gui;

import java.io.IOException;

import com.soarclient.libraries.sodium.SodiumClientMod;

import net.minecraft.util.Util;

public class URLUtils {
	private static String[] getURLOpenCommand(String url) {
		switch (Util.getOSType()) {
			case WINDOWS:
				return new String[]{"rundll32", "url.dll,FileProtocolHandler", url};
			case OSX:
				return new String[]{"open", url};
			case UNKNOWN:
			case LINUX:
			case SOLARIS:
				return new String[]{"xdg-open", url};
			default:
				throw new IllegalArgumentException("Unexpected OS Type");
		}
	}

	public static void open(String url) {
		try {
			Runtime.getRuntime().exec(getURLOpenCommand(url));
		} catch (IOException var2) {
			SodiumClientMod.logger().error("Couldn't open url '{}'", new Object[]{url, var2});
		}
	}
}
