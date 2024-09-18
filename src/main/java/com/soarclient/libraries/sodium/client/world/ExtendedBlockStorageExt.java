package com.soarclient.libraries.sodium.client.world;

import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.NibbleArray;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;

public class ExtendedBlockStorageExt extends ExtendedBlockStorage {
	public boolean hasSky;

	public ExtendedBlockStorageExt(int y, boolean storeSkylight) {
		super(y, storeSkylight);
		this.hasSky = storeSkylight;
	}

	public ExtendedBlockStorageExt(Chunk chunk, ExtendedBlockStorage storage) {
		super(storage.getyBase(), storage.getSkylightArray() != null);
		char[] blockLSBArray = this.getData();
		System.arraycopy(storage.getData(), 0, blockLSBArray, 0, blockLSBArray.length);
		int arrayLen = blockLSBArray.length;
		copyNibbleArray((ExtendedNibbleArray)storage.getBlocklightArray(), (ExtendedNibbleArray)this.getBlocklightArray());
		if (storage.getSkylightArray() != null) {
			this.hasSky = true;
			if (this.getSkylightArray() == null) {
				this.setSkylightArray(new NibbleArray(new byte[arrayLen]));
			}

			copyNibbleArray((ExtendedNibbleArray)storage.getSkylightArray(), (ExtendedNibbleArray)this.getSkylightArray());
		}

		storage.setBlockRefCount(storage.getBlockRefCount());
	}

	private static void copyNibbleArray(ExtendedNibbleArray srcArray, ExtendedNibbleArray dstArray) {
		if (srcArray != null && dstArray != null) {
			byte[] data = srcArray.getData();
			System.arraycopy(data, 0, dstArray.getData(), 0, data.length);
		} else {
			throw new RuntimeException("NibbleArray is null src: " + (srcArray == null) + " dst: " + (dstArray == null));
		}
	}
}
