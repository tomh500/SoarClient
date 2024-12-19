package com.soarclient.libraries.sodium.client.render.chunk;

public class ChunkCameraContext {
	public final int blockOriginX;
	public final int blockOriginY;
	public final int blockOriginZ;
	public final float originX;
	public final float originY;
	public final float originZ;

	public ChunkCameraContext(double x, double y, double z) {
		this.blockOriginX = (int) x;
		this.blockOriginY = (int) y;
		this.blockOriginZ = (int) z;
		this.originX = (float) (x - (double) this.blockOriginX);
		this.originY = (float) (y - (double) this.blockOriginY);
		this.originZ = (float) (z - (double) this.blockOriginZ);
	}

	public float getChunkModelOffset(int chunkBlockPos, int cameraBlockPos, float cameraPos) {
		int t = chunkBlockPos - cameraBlockPos;
		return (float) t - cameraPos;
	}
}
