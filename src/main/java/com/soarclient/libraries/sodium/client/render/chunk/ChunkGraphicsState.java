package com.soarclient.libraries.sodium.client.render.chunk;

import java.nio.ByteBuffer;

import com.soarclient.libraries.sodium.client.gl.device.CommandList;

public abstract class ChunkGraphicsState {
	private final int x;
	private final int y;
	private final int z;
	private ByteBuffer translucencyData;

	protected ChunkGraphicsState(ChunkRenderContainer<?> container) {
		this.x = container.getRenderX();
		this.y = container.getRenderY();
		this.z = container.getRenderZ();
	}

	public abstract void delete(CommandList commandList);

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	public int getZ() {
		return this.z;
	}

	public ByteBuffer getTranslucencyData() {
		return this.translucencyData;
	}

	public void setTranslucencyData(ByteBuffer data) {
		this.translucencyData = data;
	}
}
