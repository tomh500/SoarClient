package com.soarclient.libraries.patcher.reload;

import com.soarclient.hooks.FontRendererHook;
import com.soarclient.libraries.patcher.font.text.EnhancedFontRenderer;

import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;

public class PatcherReloadListener implements IResourceManagerReloadListener {

	@Override
	public void onResourceManagerReload(IResourceManager resourceManager) {

		for (EnhancedFontRenderer enhancedFontRenderer : EnhancedFontRenderer.getInstances()) {
			enhancedFontRenderer.invalidateAll();
		}

		FontRendererHook.forceRefresh = true;
	}
}
