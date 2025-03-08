package com.soarclient.libraries.browser;

import java.io.IOException;

import com.soarclient.logger.SoarLogger;
import com.soarclient.utils.Multithreading;

import net.ccbluex.liquidbounce.mcef.MCEF;
import net.ccbluex.liquidbounce.mcef.MCEFDownloadManager;
import net.ccbluex.liquidbounce.mcef.cef.MCEFBrowser;

public class JCefBrowser {

	private static MCEFBrowser browser;
	private static boolean unsupport;
	
	public static void init() {
		
		MCEF.INSTANCE.initialize();
		
		if(browser == null) {
            String url = "https://www.google.com";
            boolean transparent = true;
			browser = MCEF.INSTANCE.createBrowser(url, transparent, 60);
			browser.resize(1280, 720);
		}
	}
	
	public static void close() {
		
		if(browser != null) {
			browser.close();
			browser = null;
		}
		
		if(MCEF.INSTANCE.isInitialized()) {
			MCEF.INSTANCE.shutdown();
		}
	}
	
	public static void download() {
		try {
			MCEFDownloadManager resourceManager = MCEF.INSTANCE.newResourceManager();
			
			if(!resourceManager.isSystemCompatible()) {
				unsupport = true;
				SoarLogger.error("[JCEF] The computer does not support the browser");
				return;
			}
			
			if(resourceManager.requiresDownload()) {
				Multithreading.runAsync(() -> {
					try {
						resourceManager.downloadJcef();
					} catch (IOException e) {
						e.printStackTrace();
					}
				});
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static MCEFBrowser getBrowser() {
		return browser;
	}

	public static boolean isUnsupport() {
		return unsupport;
	}
}
