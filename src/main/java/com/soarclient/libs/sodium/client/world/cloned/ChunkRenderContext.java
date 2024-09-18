package com.soarclient.libs.sodium.client.world.cloned;

import com.soarclient.libs.sodium.client.util.math.ChunkSectionPos;

import net.minecraft.world.gen.structure.StructureBoundingBox;

public record ChunkRenderContext(ChunkSectionPos origin, ClonedChunkSection[] sections, StructureBoundingBox volume) {
	public void releaseResources() {
		for (ClonedChunkSection section : this.sections) {
			if (section != null) {
				section.getBackingCache().release(section);
			}
		}
	}
}
