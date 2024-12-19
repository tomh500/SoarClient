package com.soarclient.libraries.sodium.client.model.vertex.formats.screen_quad;

import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import org.joml.Matrix4f;

import com.soarclient.libraries.sodium.client.model.vertex.VertexSink;
import com.soarclient.libraries.sodium.client.util.math.MatrixUtil;

public interface BasicScreenQuadVertexSink extends VertexSink {
	VertexFormat VERTEX_FORMAT = DefaultVertexFormats.POSITION_COLOR;

	void writeQuad(float float1, float float2, float float3, int integer);

	default void writeQuad(Matrix4f matrix, float x, float y, float z, int color) {
		float x2 = MatrixUtil.transformVecX(matrix, x, y, z);
		float y2 = MatrixUtil.transformVecY(matrix, x, y, z);
		float z2 = MatrixUtil.transformVecZ(matrix, x, y, z);
		this.writeQuad(x2, y2, z2, color);
	}
}
