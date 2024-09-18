package com.soarclient.libs.sodium.client.world.cloned;

import it.unimi.dsi.fastutil.shorts.Short2ObjectMap;
import it.unimi.dsi.fastutil.shorts.Short2ObjectOpenHashMap;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;

import com.soarclient.libs.sodium.client.util.math.ChunkSectionPos;
import com.soarclient.libs.sodium.client.world.ExtendedBlockStorageExt;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.BlockPos.MutableBlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.NibbleArray;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraft.world.gen.structure.StructureBoundingBox;

public class ClonedChunkSection {
	private static final ExtendedBlockStorage EMPTY_SECTION = new ExtendedBlockStorage(0, false);
	private final AtomicInteger referenceCount = new AtomicInteger(0);
	private final Short2ObjectMap<TileEntity> blockEntities = new Short2ObjectOpenHashMap<>();
	private final ClonedChunkSectionCache backingCache;
	private final World world;
	private ChunkSectionPos pos;
	private ExtendedBlockStorageExt data;
	private BiomeGenBase[] biomeData;
	private long lastUsedTimestamp = Long.MAX_VALUE;

	public void init(ChunkSectionPos context) {
		this.pos = context;
		Chunk chunk = this.world.getChunkFromChunkCoords(this.pos.getX(), this.pos.getZ());
		if (chunk == null) {
			throw new RuntimeException("Couldn't retrieve chunk at " + this.pos.toChunkPos());
		} else {
			ExtendedBlockStorage section = getChunkSection(chunk, this.pos);
			if (section == null) {
				section = EMPTY_SECTION;
			}

			this.data = new ExtendedBlockStorageExt(chunk, section);
			this.biomeData = new BiomeGenBase[chunk.getBiomeArray().length];
			StructureBoundingBox box = new StructureBoundingBox(
				this.pos.getMinX(), this.pos.getMinY(), this.pos.getMinZ(), this.pos.getMaxX(), this.pos.getMaxY(), this.pos.getMaxZ()
			);
			this.blockEntities.clear();

			for (Entry<BlockPos, TileEntity> entry : chunk.getTileEntityMap().entrySet()) {
				BlockPos entityPos = (BlockPos)entry.getKey();
				if (box.isVecInside(entityPos)) {
					this.blockEntities.put(ChunkSectionPos.packLocal(entityPos), (TileEntity)entry.getValue());
				}
			}

			MutableBlockPos biomePos = new MutableBlockPos();

			for (int z = this.pos.getMinZ(); z <= this.pos.getMaxZ(); z++) {
				for (int x = this.pos.getMinX(); x <= this.pos.getMaxX(); x++) {
					biomePos.set(x, 100, z);
					this.biomeData[(z & 15) << 4 | x & 15] = this.world.getBiomeGenForCoords(biomePos);
				}
			}
		}
	}

	public IBlockState getBlockState(int x, int y, int z) {
		return this.data.get(x, y, z);
	}

	public BiomeGenBase getBiomeForNoiseGen(int x, int z) {
		return this.biomeData[x | z << 4];
	}

	public TileEntity getBlockEntity(int x, int y, int z) {
		return this.blockEntities.get(packLocal(x, y, z));
	}

	public ChunkSectionPos getPosition() {
		return this.pos;
	}

	public NibbleArray getLightArray(EnumSkyBlock type) {
		if (type != EnumSkyBlock.SKY) {
			return this.data.getBlocklightArray();
		} else {
			return !this.world.provider.getHasNoSky() && this.data.hasSky ? this.data.getSkylightArray() : null;
		}
	}

	public int getLightLevel(int x, int y, int z, EnumSkyBlock type) {
		NibbleArray lightArray = type == EnumSkyBlock.BLOCK ? this.data.getBlocklightArray() : this.data.getSkylightArray();
		return lightArray != null ? lightArray.get(x, y, z) : type.defaultLightValue;
	}

	private static ExtendedBlockStorage getChunkSection(Chunk chunk, ChunkSectionPos pos) {
		ExtendedBlockStorage section = null;
		if (!isOutsideBuildHeight(ChunkSectionPos.getBlockCoord(pos.getY()))) {
			section = chunk.getBlockStorageArray()[pos.getY()];
		}

		return section;
	}

	private static boolean isOutsideBuildHeight(int y) {
		return y < 0 || y >= 256;
	}

	public void acquireReference() {
		this.referenceCount.incrementAndGet();
	}

	public boolean releaseReference() {
		return this.referenceCount.decrementAndGet() <= 0;
	}

	private static short packLocal(int x, int y, int z) {
		return (short)(x << 8 | z << 4 | y);
	}

	public ClonedChunkSection(ClonedChunkSectionCache backingCache, World world) {
		this.backingCache = backingCache;
		this.world = world;
	}

	public ClonedChunkSectionCache getBackingCache() {
		return this.backingCache;
	}

	public void setLastUsedTimestamp(long lastUsedTimestamp) {
		this.lastUsedTimestamp = lastUsedTimestamp;
	}

	public long getLastUsedTimestamp() {
		return this.lastUsedTimestamp;
	}
}
