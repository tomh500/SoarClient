package com.soarclient.libs.sodium.client.render.chunk.cull;

import com.soarclient.libs.sodium.client.util.math.FrustumExtended;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import net.minecraft.client.renderer.chunk.SetVisibility;
import net.minecraft.util.Vec3;

public interface ChunkCuller {
	IntArrayList computeVisible(Vec3 vec3, FrustumExtended frustumExtended, int integer, boolean boolean4);

	void onSectionStateChanged(int integer1, int integer2, int integer3, SetVisibility setVisibility);

	void onSectionLoaded(int integer1, int integer2, int integer3, int integer4);

	void onSectionUnloaded(int integer1, int integer2, int integer3);

	boolean isSectionVisible(int integer1, int integer2, int integer3);
}
