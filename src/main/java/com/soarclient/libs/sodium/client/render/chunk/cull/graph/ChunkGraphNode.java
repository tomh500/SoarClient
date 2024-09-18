package com.soarclient.libs.sodium.client.render.chunk.cull.graph;

import com.soarclient.libs.sodium.client.render.chunk.data.ChunkRenderData;
import com.soarclient.libs.sodium.client.util.math.FrustumExtended;
import com.soarclient.libs.sodium.common.util.DirectionUtil;

import net.minecraft.client.renderer.chunk.SetVisibility;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class ChunkGraphNode {
	private static final long DEFAULT_VISIBILITY_DATA = calculateVisibilityData(ChunkRenderData.EMPTY.getOcclusionData());
	private static final float FRUSTUM_EPSILON = 1.125F;
	private final ChunkGraphNode[] nodes = new ChunkGraphNode[DirectionUtil.ALL_DIRECTIONS.length];
	private final int chunkX;
	private final int chunkY;
	private final int chunkZ;
	private final int id;
	private int lastVisibleFrame = -1;
	private long visibilityData;
	private short cullingState;

	public ChunkGraphNode(int chunkX, int chunkY, int chunkZ, int id) {
		this.chunkX = chunkX;
		this.chunkY = chunkY;
		this.chunkZ = chunkZ;
		this.id = id;
		this.visibilityData = DEFAULT_VISIBILITY_DATA;
	}

	public ChunkGraphNode getConnectedNode(EnumFacing dir) {
		return this.nodes[dir.ordinal()];
	}

	public void setAdjacentNode(EnumFacing dir, ChunkGraphNode node) {
		this.nodes[dir.ordinal()] = node;
	}

	public void setOcclusionData(SetVisibility occlusionData) {
		this.visibilityData = calculateVisibilityData(occlusionData);
	}

	private static long calculateVisibilityData(SetVisibility occlusionData) {
		long visibilityData = 0L;

		for (EnumFacing from : DirectionUtil.ALL_DIRECTIONS) {
			for (EnumFacing to : DirectionUtil.ALL_DIRECTIONS) {
				if (occlusionData == null || occlusionData.isVisible(from, to)) {
					visibilityData |= 1L << (from.ordinal() << 3) + to.ordinal();
				}
			}
		}

		return visibilityData;
	}

	public short computeQueuePop() {
		short retVal = (short)(this.cullingState & (this.cullingState >> 8 & 0xFF | 0xFF00));
		this.cullingState = 0;
		return retVal;
	}

	public void updateCullingState(EnumFacing flow, short parent) {
		int inbound = flow.ordinal();
		this.cullingState = (short)(this.cullingState | (short)((int)(this.visibilityData >> (inbound << 3) & 255L)));
		this.cullingState &= (short)(~(1 << inbound + 8));
		this.cullingState &= (short)(parent | 255);
	}

	public void setCullingState(short parent) {
		this.cullingState = (short)(parent & '\uff00');
	}

	public void resetCullingState() {
		this.cullingState = -1;
	}

	public boolean isCulledByFrustum(FrustumExtended frustum) {
		float x = (float)this.getOriginX();
		float y = (float)this.getOriginY();
		float z = (float)this.getOriginZ();
		return !frustum.fastAabbTest(x - 1.125F, y - 1.125F, z - 1.125F, x + 16.0F + 1.125F, y + 16.0F + 1.125F, z + 16.0F + 1.125F);
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

	public double getSquaredDistance(BlockPos pos) {
		return this.getSquaredDistance((double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5);
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

	public double getSquaredDistance(double x, double y, double z) {
		double xDist = x - this.getCenterX();
		double yDist = y - this.getCenterY();
		double zDist = z - this.getCenterZ();
		return xDist * xDist + yDist * yDist + zDist * zDist;
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

	public int getId() {
		return this.id;
	}

	public int getLastVisibleFrame() {
		return this.lastVisibleFrame;
	}

	public void setLastVisibleFrame(int lastVisibleFrame) {
		this.lastVisibleFrame = lastVisibleFrame;
	}
}
