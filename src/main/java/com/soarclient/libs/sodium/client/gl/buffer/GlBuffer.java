package com.soarclient.libs.sodium.client.gl.buffer;

import org.lwjgl.opengl.GL15;

import com.soarclient.libs.sodium.client.gl.GlObject;
import com.soarclient.libs.sodium.client.gl.device.RenderDevice;

public abstract class GlBuffer extends GlObject {
	public static final int NULL_BUFFER_ID = 0;
	protected final GlBufferUsage usage;

	protected GlBuffer(RenderDevice owner, GlBufferUsage usage) {
		super(owner);
		this.setHandle(GL15.glGenBuffers());
		this.usage = usage;
	}

	public GlBufferUsage getUsageHint() {
		return this.usage;
	}
}
