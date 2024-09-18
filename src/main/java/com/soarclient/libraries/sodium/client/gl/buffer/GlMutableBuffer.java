package com.soarclient.libraries.sodium.client.gl.buffer;

import com.soarclient.libraries.sodium.client.gl.device.RenderDevice;

public class GlMutableBuffer extends GlBuffer {
	private long size = 0L;

	public GlMutableBuffer(RenderDevice owner, GlBufferUsage usage) {
		super(owner, usage);
	}

	public void setSize(long size) {
		this.size = size;
	}

	public long getSize() {
		return this.size;
	}
}
