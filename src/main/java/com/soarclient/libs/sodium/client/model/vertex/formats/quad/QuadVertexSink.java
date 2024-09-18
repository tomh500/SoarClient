package com.soarclient.libs.sodium.client.model.vertex.formats.quad;

import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import org.joml.Matrix4f;

import com.soarclient.libs.sodium.client.model.vertex.VertexSink;
import com.soarclient.libs.sodium.client.util.math.MatrixUtil;
import com.soarclient.libs.sodium.client.util.math.MatrixStack.Entry;

public interface QuadVertexSink extends VertexSink {
	VertexFormat VERTEX_FORMAT = DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP;

	void writeQuad(float float1, float float2, float float3, int integer4, float float5, float float6, int integer7, int integer8, int integer9);

	default void writeQuad(Entry matrices, float x, float y, float z, int color, float u, float v, int light, int overlay, int normal) {
		Matrix4f matrix = matrices.getModel();
		float x2 = MatrixUtil.transformVecX(matrix, x, y, z);
		float y2 = MatrixUtil.transformVecY(matrix, x, y, z);
		float z2 = MatrixUtil.transformVecZ(matrix, x, y, z);
		int norm = MatrixUtil.transformPackedNormal(normal, matrices.getNormal());
		this.writeQuad(x2, y2, z2, color, u, v, light, overlay, norm);
	}
}
