package com.soarclient.libs.sodium.client.model.light;

import com.soarclient.libs.sodium.client.model.light.data.QuadLightData;
import com.soarclient.libs.sodium.client.model.quad.ModelQuadView;

import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public interface LightPipeline {
	void calculate(ModelQuadView modelQuadView, BlockPos blockPos, QuadLightData quadLightData, EnumFacing enumFacing4, EnumFacing enumFacing5, boolean boolean6);
}
