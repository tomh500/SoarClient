package com.soarclient.libraries.sodium.client.model.quad.blender;

import com.google.common.collect.Sets;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

public class DefaultBlockColorSettings {
	private static final Set<Block> BLENDED_BLOCKS = new ReferenceOpenHashSet<>(
			Sets.newHashSet(new Block[] { Blocks.grass, Blocks.tallgrass, Blocks.double_plant, Blocks.leaves,
					Blocks.leaves2, Blocks.vine, Blocks.water, Blocks.cauldron, Blocks.reeds }));

	public static boolean isSmoothBlendingAvailable(Block block) {
		return BLENDED_BLOCKS.contains(block);
	}
}
