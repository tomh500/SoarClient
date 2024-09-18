package com.soarclient.libs.sodium.client.render.chunk;

import java.lang.reflect.Array;
import java.util.concurrent.CompletableFuture;

import com.soarclient.libs.sodium.client.gl.device.RenderDevice;
import com.soarclient.libs.sodium.client.render.SodiumWorldRenderer;
import com.soarclient.libs.sodium.client.render.chunk.data.ChunkRenderBounds;
import com.soarclient.libs.sodium.client.render.chunk.data.ChunkRenderData;
import com.soarclient.libs.sodium.client.render.chunk.passes.BlockRenderPass;
import com.soarclient.libs.sodium.client.util.math.ChunkSectionPos;
import com.soarclient.libs.sodium.client.util.math.FrustumExtended;

import net.minecraft.util.BlockPos;

public class ChunkRenderContainer<T extends ChunkGraphicsState> {
	private final SodiumWorldRenderer worldRenderer;
	private final int chunkX;
	private final int chunkY;
	private final int chunkZ;
	private final T[] graphicsStates;
	private final ChunkRenderColumn<T> column;
	private ChunkRenderData data = ChunkRenderData.ABSENT;
	private CompletableFuture<Void> rebuildTask = null;
	private boolean needsRebuild;
	private boolean needsImportantRebuild;
	private boolean needsSort;
	private boolean tickable;
	private int id;
	private boolean rebuildableForTranslucents;

	public ChunkRenderContainer(ChunkRenderBackend<T> backend, SodiumWorldRenderer worldRenderer, int chunkX, int chunkY, int chunkZ, ChunkRenderColumn<T> column) {
		this.worldRenderer = worldRenderer;
		this.chunkX = chunkX;
		this.chunkY = chunkY;
		this.chunkZ = chunkZ;
		this.graphicsStates = (T[])((ChunkGraphicsState[])Array.newInstance(backend.getGraphicsStateType(), BlockRenderPass.COUNT));
		this.rebuildableForTranslucents = false;
		this.column = column;
	}

	public void cancelRebuildTask() {
		this.needsRebuild = false;
		this.needsImportantRebuild = false;
		this.needsSort = false;
		if (this.rebuildTask != null) {
			this.rebuildTask.cancel(false);
			this.rebuildTask = null;
		}
	}

	public ChunkRenderData getData() {
		return this.data;
	}

	public boolean needsRebuild() {
		return this.needsRebuild;
	}

	public boolean needsImportantRebuild() {
		return this.needsImportantRebuild;
	}

	public boolean needsSort() {
		return this.needsSort;
	}

	public void delete() {
		this.cancelRebuildTask();
		this.setData(ChunkRenderData.ABSENT);
		this.deleteGraphicsState();
	}

	private void deleteGraphicsState() {
		T[] states = this.graphicsStates;

		for (int i = 0; i < states.length; i++) {
			T state = states[i];
			if (state != null) {
				state.delete(RenderDevice.INSTANCE.createCommandList());
				states[i] = null;
			}
		}
	}

	public boolean shouldRebuildForTranslucents() {
		return this.rebuildableForTranslucents;
	}

	public void setRebuildForTranslucents(boolean flag) {
		this.rebuildableForTranslucents = flag;
	}

	public void setData(ChunkRenderData info) {
		if (info == null) {
			throw new NullPointerException("Mesh information must not be null");
		} else {
			this.worldRenderer.onChunkRenderUpdated(this.chunkX, this.chunkY, this.chunkZ, this.data, info);
			this.data = info;
			this.tickable = !info.getAnimatedSprites().isEmpty();
		}
	}

	public boolean scheduleRebuild(boolean important) {
		boolean changed = !this.needsRebuild || !this.needsImportantRebuild && important;
		this.needsImportantRebuild = important;
		this.needsRebuild = true;
		this.needsSort = false;
		return changed;
	}

	public boolean scheduleSort(boolean important) {
		if (this.needsRebuild) {
			return false;
		} else {
			boolean changed = !this.needsSort;
			this.needsSort = true;
			return changed;
		}
	}

	public boolean isEmpty() {
		return this.data.isEmpty();
	}

	public ChunkSectionPos getChunkPos() {
		return ChunkSectionPos.from(this.chunkX, this.chunkY, this.chunkZ);
	}

	public boolean isOutsideFrustum(FrustumExtended frustum) {
		float x = (float)this.getOriginX();
		float y = (float)this.getOriginY();
		float z = (float)this.getOriginZ();
		return !frustum.fastAabbTest(x, y, z, x + 16.0F, y + 16.0F, z + 16.0F);
	}

	public void tick() {
	}

	public int getOriginX() {
		return this.chunkX << 4;
	}

	public int getOriginY() {
		return this.chunkY << 4;
	}

	public int getOriginZ() {
		return this.chunkZ << 4;
	}

	public int getRenderX() {
		return this.getOriginX() - 8;
	}

	public int getRenderY() {
		return this.getOriginY() - 8;
	}

	public int getRenderZ() {
		return this.getOriginZ() - 8;
	}

	public double getSquaredDistance(double x, double y, double z) {
		double xDist = x - this.getCenterX();
		double yDist = y - this.getCenterY();
		double zDist = z - this.getCenterZ();
		return xDist * xDist + yDist * yDist + zDist * zDist;
	}

	private double getCenterX() {
		return (double)this.getOriginX() + 8.0;
	}

	private double getCenterY() {
		return (double)this.getOriginY() + 8.0;
	}

	private double getCenterZ() {
		return (double)this.getOriginZ() + 8.0;
	}

	public BlockPos getRenderOrigin() {
		return new BlockPos(this.getRenderX(), this.getRenderY(), this.getRenderZ());
	}

	public T[] getGraphicsStates() {
		return this.graphicsStates;
	}

	public void setGraphicsState(BlockRenderPass pass, T state) {
		this.graphicsStates[pass.ordinal()] = state;
	}

	public double getSquaredDistanceXZ(double x, double z) {
		double xDist = x - this.getCenterX();
		double zDist = z - this.getCenterZ();
		return xDist * xDist + zDist * zDist;
	}

	public int getChunkX() {
		return this.chunkX;
	}

	public int getChunkY() {
		return this.chunkY;
	}

	public int getChunkZ() {
		return this.chunkZ;
	}

	public ChunkRenderBounds getBounds() {
		return this.data.getBounds();
	}

	public T getGraphicsState(BlockRenderPass pass) {
		return this.graphicsStates[pass.ordinal()];
	}

	public boolean isTickable() {
		return this.tickable;
	}

	public int getFacesWithData() {
		return this.data.getFacesWithData();
	}

	public boolean canRebuild() {
		return this.column.areNeighborsPresent();
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return this.id;
	}

	public String toString() {
		return String.format(
			"ChunkRenderContainer at chunk (%d, %d, %d) from (%d, %d, %d) to (%d, %d, %d)",
			this.chunkX,
			this.chunkY,
			this.chunkZ,
			this.getOriginX(),
			this.getOriginY(),
			this.getOriginZ(),
			this.getOriginX() + 15,
			this.getOriginY() + 15,
			this.getOriginZ() + 15
		);
	}
}
