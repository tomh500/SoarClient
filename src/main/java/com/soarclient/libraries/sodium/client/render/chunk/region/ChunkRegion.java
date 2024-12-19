package com.soarclient.libraries.sodium.client.render.chunk.region;

import com.soarclient.libraries.sodium.client.gl.arena.GlBufferArena;
import com.soarclient.libraries.sodium.client.gl.device.CommandList;
import com.soarclient.libraries.sodium.client.gl.device.RenderDevice;
import com.soarclient.libraries.sodium.client.gl.tessellation.GlTessellation;
import com.soarclient.libraries.sodium.client.model.quad.properties.ModelQuadFacing;
import com.soarclient.libraries.sodium.client.render.chunk.ChunkGraphicsState;
import com.soarclient.libraries.sodium.client.render.chunk.backends.multidraw.ChunkDrawCallBatcher;
import com.soarclient.libraries.sodium.client.render.chunk.compile.ChunkBuildResult;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

public class ChunkRegion<T extends ChunkGraphicsState> {
	private static final int EXPECTED_CHUNK_SIZE = 4096;
	private final GlBufferArena arena;
	private final ChunkDrawCallBatcher batch;
	private final RenderDevice device;
	private final ObjectArrayList<ChunkBuildResult<T>> uploadQueue;
	private GlTessellation tessellation;
	private final int x;
	private final int y;
	private final int z;
	public float camDistance;

	public ChunkRegion(RenderDevice device, int size, int x, int y, int z) {
		int arenaSize = 4096 * size;
		this.device = device;
		this.arena = new GlBufferArena(device, arenaSize, arenaSize);
		this.uploadQueue = new ObjectArrayList<>();
		this.batch = ChunkDrawCallBatcher.create(size * ModelQuadFacing.COUNT);
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public int getCenterBlockX() {
		return this.x * 8 * 16 + 64;
	}

	public int getCenterBlockY() {
		return this.y * 4 * 16 + 32;
	}

	public int getCenterBlockZ() {
		return this.z * 8 * 16 + 64;
	}

	public GlBufferArena getBufferArena() {
		return this.arena;
	}

	public boolean isArenaEmpty() {
		return this.arena.isEmpty();
	}

	public void deleteResources() {
		if (this.tessellation != null) {
			try (CommandList commands = this.device.createCommandList()) {
				commands.deleteTessellation(this.tessellation);
			}

			this.tessellation = null;
		}

		this.arena.delete();
		this.batch.delete();
	}

	public ObjectArrayList<ChunkBuildResult<T>> getUploadQueue() {
		return this.uploadQueue;
	}

	public ChunkDrawCallBatcher getDrawBatcher() {
		return this.batch;
	}

	public GlTessellation getTessellation() {
		return this.tessellation;
	}

	public void setTessellation(GlTessellation tessellation) {
		this.tessellation = tessellation;
	}
}
