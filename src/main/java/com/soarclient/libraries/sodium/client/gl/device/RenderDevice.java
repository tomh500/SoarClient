package com.soarclient.libraries.sodium.client.gl.device;

public interface RenderDevice {
	RenderDevice INSTANCE = new GLRenderDevice();

	CommandList createCommandList();

	static void enterManagedCode() {
		INSTANCE.makeActive();
	}

	static void exitManagedCode() {
		INSTANCE.makeInactive();
	}

	void makeActive();

	void makeInactive();
}
