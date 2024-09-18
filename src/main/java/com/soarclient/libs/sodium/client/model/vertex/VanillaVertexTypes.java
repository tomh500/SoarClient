package com.soarclient.libs.sodium.client.model.vertex;

import com.soarclient.libs.sodium.client.model.vertex.formats.glyph.GlyphVertexSink;
import com.soarclient.libs.sodium.client.model.vertex.formats.glyph.GlyphVertexType;
import com.soarclient.libs.sodium.client.model.vertex.formats.line.LineVertexSink;
import com.soarclient.libs.sodium.client.model.vertex.formats.line.LineVertexType;
import com.soarclient.libs.sodium.client.model.vertex.formats.particle.ParticleVertexSink;
import com.soarclient.libs.sodium.client.model.vertex.formats.particle.ParticleVertexType;
import com.soarclient.libs.sodium.client.model.vertex.formats.quad.QuadVertexSink;
import com.soarclient.libs.sodium.client.model.vertex.formats.quad.QuadVertexType;
import com.soarclient.libs.sodium.client.model.vertex.formats.screen_quad.BasicScreenQuadVertexSink;
import com.soarclient.libs.sodium.client.model.vertex.formats.screen_quad.BasicScreenQuadVertexType;
import com.soarclient.libs.sodium.client.model.vertex.type.VanillaVertexType;

public class VanillaVertexTypes {
	public static final VanillaVertexType<QuadVertexSink> QUADS = new QuadVertexType();
	public static final VanillaVertexType<LineVertexSink> LINES = new LineVertexType();
	public static final VanillaVertexType<GlyphVertexSink> GLYPHS = new GlyphVertexType();
	public static final VanillaVertexType<ParticleVertexSink> PARTICLES = new ParticleVertexType();
	public static final VanillaVertexType<BasicScreenQuadVertexSink> BASIC_SCREEN_QUADS = new BasicScreenQuadVertexType();
}
