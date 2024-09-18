package com.soarclient.libs.sodium.client.model.vertex.formats.particle;

import com.soarclient.libs.sodium.client.model.vertex.VertexSink;

import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;

public interface ParticleVertexSink extends VertexSink {
	VertexFormat VERTEX_FORMAT = DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP;

	void writeParticle(float float1, float float2, float float3, float float4, float float5, int integer6, int integer7);
}
