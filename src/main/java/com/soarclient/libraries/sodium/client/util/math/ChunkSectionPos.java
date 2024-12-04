package com.soarclient.libraries.sodium.client.util.math;

import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3i;
import net.minecraft.world.ChunkCoordIntPair;

public class ChunkSectionPos extends Vec3i {
	private ChunkSectionPos(int i, int j, int k) {
		super(i, j, k);
	}

	public static ChunkSectionPos from(int x, int y, int z) {
		return new ChunkSectionPos(x, y, z);
	}

	public static ChunkSectionPos from(BlockPos pos) {
		int x = getSectionCoord(pos.getX());
		int y = getSectionCoord(pos.getY());
		int z = getSectionCoord(pos.getZ());
		return new ChunkSectionPos(x, y, z);
	}

	public static ChunkSectionPos from(ChunkCoordIntPair chunkPos, int y) {
		return new ChunkSectionPos(chunkPos.chunkXPos, y, chunkPos.chunkZPos);
	}

	public static ChunkSectionPos from(Entity entity) {
		BlockPos pos = entity.getPosition();
		int x = getSectionCoord(pos.getX());
		int y = getSectionCoord(pos.getY());
		int z = getSectionCoord(pos.getZ());
		return new ChunkSectionPos(x, y, z);
	}

	public static ChunkSectionPos from(long packed) {
		return new ChunkSectionPos(unpackX(packed), unpackY(packed), unpackZ(packed));
	}

	public static long offset(long packed, EnumFacing direction) {
		return offset(packed, direction.getFrontOffsetX(), direction.getFrontOffsetY(), direction.getFrontOffsetZ());
	}

	public static long offset(long packed, int x, int y, int z) {
		return asLong(unpackX(packed) + x, unpackY(packed) + y, unpackZ(packed) + z);
	}

	public static int getSectionCoord(int coord) {
		return coord >> 4;
	}

	public static int getLocalCoord(int coord) {
		return coord & 15;
	}

	public static short packLocal(BlockPos pos) {
		int x = getLocalCoord(pos.getX());
		int y = getLocalCoord(pos.getY());
		int z = getLocalCoord(pos.getZ());
		return (short) (x << 8 | z << 4 | y);
	}

	public static int unpackLocalX(short packedLocalPos) {
		return packedLocalPos >>> 8 & 15;
	}

	public static int unpackLocalY(short packedLocalPos) {
		return packedLocalPos & 15;
	}

	public static int unpackLocalZ(short packedLocalPos) {
		return packedLocalPos >>> 4 & 15;
	}

	public static int getBlockCoord(int sectionCoord) {
		return sectionCoord << 4;
	}

	public static int unpackX(long packed) {
		return (int) (packed >> 42);
	}

	public static int unpackY(long packed) {
		return (int) (packed << 44 >> 44);
	}

	public static int unpackZ(long packed) {
		return (int) (packed << 22 >> 42);
	}

	public static long fromBlockPos(long blockPos) {
		BlockPos pos = BlockPos.fromLong(blockPos);
		int x = getSectionCoord(pos.getX());
		int y = getSectionCoord(pos.getY());
		int z = getSectionCoord(pos.getZ());
		return asLong(x, y, z);
	}

	public static long withZeroY(long pos) {
		return pos & -1048576L;
	}

	public static long asLong(int x, int y, int z) {
		long l = 0L;
		l |= ((long) x & 4194303L) << 42;
		l |= (long) y & 1048575L;
		return l | ((long) z & 4194303L) << 20;
	}

	public int unpackBlockX(short packedLocalPos) {
		return this.getMinX() + unpackLocalX(packedLocalPos);
	}

	public int unpackBlockY(short packedLocalPos) {
		return this.getMinY() + unpackLocalY(packedLocalPos);
	}

	public int unpackBlockZ(short packedLocalPos) {
		return this.getMinZ() + unpackLocalZ(packedLocalPos);
	}

	public BlockPos unpackBlockPos(short packedLocalPos) {
		int x = this.unpackBlockX(packedLocalPos);
		int y = this.unpackBlockY(packedLocalPos);
		int z = this.unpackBlockZ(packedLocalPos);
		return new BlockPos(x, y, z);
	}

	public int getSectionX() {
		return this.getX();
	}

	public int getSectionY() {
		return this.getY();
	}

	public int getSectionZ() {
		return this.getZ();
	}

	public int getMinX() {
		return this.getSectionX() << 4;
	}

	public int getMinY() {
		return this.getSectionY() << 4;
	}

	public int getMinZ() {
		return this.getSectionZ() << 4;
	}

	public int getMaxX() {
		return (this.getSectionX() << 4) + 15;
	}

	public int getMaxY() {
		return (this.getSectionY() << 4) + 15;
	}

	public int getMaxZ() {
		return (this.getSectionZ() << 4) + 15;
	}

	public BlockPos getMinPos() {
		int x = getBlockCoord(this.getSectionX());
		int y = getBlockCoord(this.getSectionY());
		int z = getBlockCoord(this.getSectionZ());
		return new BlockPos(x, y, z);
	}

	public BlockPos getCenterPos() {
		return this.getMinPos().add(8, 8, 8);
	}

	public ChunkCoordIntPair toChunkPos() {
		return new ChunkCoordIntPair(this.getSectionX(), this.getSectionZ());
	}

	public long asLong() {
		return asLong(this.getSectionX(), this.getSectionY(), this.getSectionZ());
	}

	public Iterable<BlockPos> streamBlocks() {
		return BlockPos.getAllInBox(new BlockPos(this.getMinX(), this.getMinY(), this.getMinZ()),
				new BlockPos(this.getMaxX(), this.getMaxY(), this.getMaxZ()));
	}
}
