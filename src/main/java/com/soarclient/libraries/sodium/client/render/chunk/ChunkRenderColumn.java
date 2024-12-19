package com.soarclient.libraries.sodium.client.render.chunk;

import com.soarclient.libraries.sodium.common.util.DirectionUtil;

import net.minecraft.util.EnumFacing;

public class ChunkRenderColumn<T extends ChunkGraphicsState> {
	private final ChunkRenderContainer<T>[] renders = new ChunkRenderContainer[16];
	private final ChunkRenderColumn<T>[] adjacent = new ChunkRenderColumn[6];
	private final int x;
	private final int z;

	public ChunkRenderColumn(int x, int z) {
		this.x = x;
		this.z = z;
		this.setAdjacentColumn(EnumFacing.UP, this);
		this.setAdjacentColumn(EnumFacing.DOWN, this);
	}

	public void setAdjacentColumn(EnumFacing dir, ChunkRenderColumn<T> column) {
		this.adjacent[dir.ordinal()] = column;
	}

	public ChunkRenderColumn<T> getAdjacentColumn(EnumFacing dir) {
		return this.adjacent[dir.ordinal()];
	}

	public void setRender(int y, ChunkRenderContainer<T> render) {
		this.renders[y] = render;
	}

	public ChunkRenderContainer<T> getRender(int y) {
		return y >= 0 && y < this.renders.length ? this.renders[y] : null;
	}

	public int getX() {
		return this.x;
	}

	public int getZ() {
		return this.z;
	}

	public boolean areNeighborsPresent() {
		for (EnumFacing dir : DirectionUtil.HORIZONTAL_DIRECTIONS) {
			ChunkRenderColumn<T> adj = this.adjacent[dir.ordinal()];
			if (adj == null) {
				return false;
			}

			EnumFacing corner;
			if (dir == EnumFacing.NORTH) {
				corner = EnumFacing.EAST;
			} else if (dir == EnumFacing.SOUTH) {
				corner = EnumFacing.WEST;
			} else if (dir == EnumFacing.WEST) {
				corner = EnumFacing.NORTH;
			} else {
				if (dir != EnumFacing.EAST) {
					continue;
				}

				corner = EnumFacing.SOUTH;
			}

			if (adj.getAdjacentColumn(corner) == null) {
				return false;
			}
		}

		return true;
	}
}
