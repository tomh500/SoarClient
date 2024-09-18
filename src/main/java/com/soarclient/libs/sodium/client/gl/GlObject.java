package com.soarclient.libs.sodium.client.gl;

import com.soarclient.libs.sodium.client.gl.device.RenderDevice;

public class GlObject {
	private static final int INVALID_HANDLE = Integer.MIN_VALUE;
	protected final RenderDevice device;
	private int handle = Integer.MIN_VALUE;

	public GlObject(RenderDevice owner) {
		this.device = owner;
	}

	protected final void setHandle(int handle) {
		this.handle = handle;
	}

	public final int handle() {
		this.checkHandle();
		return this.handle;
	}

	protected final void checkHandle() {
		if (!this.isHandleValid()) {
			throw new IllegalStateException("Handle is not valid");
		}
	}

	protected final boolean isHandleValid() {
		return this.handle != Integer.MIN_VALUE;
	}

	public final void invalidateHandle() {
		this.handle = Integer.MIN_VALUE;
	}

	public RenderDevice getDevice() {
		return this.device;
	}
}
