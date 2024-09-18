package com.soarclient.libs.sodium.client.model.vertex.formats.glyph;

import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import org.joml.Matrix4f;

import com.soarclient.libs.sodium.client.model.vertex.VertexSink;
import com.soarclient.libs.sodium.client.util.math.MatrixUtil;

public interface GlyphVertexSink extends VertexSink {
	VertexFormat VERTEX_FORMAT = DefaultVertexFormats.POSITION_TEX_LMAP_COLOR;

	default void writeGlyph(Matrix4f matrix, float x, float y, float z, int color, float u, float v, int light) {
		float x2 = MatrixUtil.transformVecX(matrix, x, y, z);
		float y2 = MatrixUtil.transformVecY(matrix, x, y, z);
		float z2 = MatrixUtil.transformVecZ(matrix, x, y, z);
		this.writeGlyph(x2, y2, z2, color, u, v, light);
	}

	void writeGlyph(float float1, float float2, float float3, int integer4, float float5, float float6, int integer7);
}
