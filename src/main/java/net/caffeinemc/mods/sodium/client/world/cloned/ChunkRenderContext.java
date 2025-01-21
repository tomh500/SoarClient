package net.caffeinemc.mods.sodium.client.world.cloned;

import dev.vexor.radium.compat.mojang.minecraft.math.SectionPos;
import net.minecraft.world.gen.structure.StructureBoundingBox;

public record ChunkRenderContext(SectionPos origin, ClonedChunkSection[] sections, StructureBoundingBox volume) {
}
