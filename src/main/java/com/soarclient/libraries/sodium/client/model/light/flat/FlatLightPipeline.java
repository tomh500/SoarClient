package com.soarclient.libraries.sodium.client.model.light.flat;

import java.util.Arrays;

import com.soarclient.libraries.sodium.client.model.light.LightPipeline;
import com.soarclient.libraries.sodium.client.model.light.data.LightDataAccess;
import com.soarclient.libraries.sodium.client.model.light.data.QuadLightData;
import com.soarclient.libraries.sodium.client.model.quad.ModelQuadView;

import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class FlatLightPipeline implements LightPipeline {
	private final LightDataAccess lightCache;

	public FlatLightPipeline(LightDataAccess lightCache) {
		this.lightCache = lightCache;
	}

	@Override
	public void calculate(ModelQuadView quad, BlockPos pos, QuadLightData out, EnumFacing cullFace, EnumFacing face, boolean shade) {
		int lightmap;
		if (cullFace != null) {
			lightmap = this.getOffsetLightmap(pos, cullFace);
		} else {
			int flags = quad.getFlags();
			if ((flags & 4) == 0 && ((flags & 2) == 0 || !LightDataAccess.unpackFC(this.lightCache.get(pos)))) {
				lightmap = LightDataAccess.unpackLM(this.lightCache.get(pos));
			} else {
				lightmap = this.getOffsetLightmap(pos, face);
			}
		}

		Arrays.fill(out.lm, lightmap);
		Arrays.fill(out.br, this.lightCache.getWorld().getBrightness(face, shade));
	}

	private int getOffsetLightmap(BlockPos pos, EnumFacing face) {
		int lightmap = LightDataAccess.unpackLM(this.lightCache.get(pos, face));
		if ((lightmap & 240) != 240) {
			int originLightmap = LightDataAccess.unpackLM(this.lightCache.get(pos));
			lightmap = lightmap & -256 | Math.max(lightmap & 0xFF, originLightmap & 0xFF);
		}

		return lightmap;
	}
}
