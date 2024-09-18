package com.soarclient.libs.sodium.client.gl.array;

import com.soarclient.libs.sodium.client.gl.GlObject;
import com.soarclient.libs.sodium.client.gl.device.RenderDevice;
import com.soarclient.libs.sodium.client.gl.func.GlFunctions;

public class GlVertexArray extends GlObject {
	public static final int NULL_ARRAY_ID = 0;

	public GlVertexArray(RenderDevice owner) {
		super(owner);
		if (!GlFunctions.isVertexArraySupported()) {
			throw new UnsupportedOperationException("Vertex arrays are unsupported on this platform");
		} else {
			this.setHandle(GlFunctions.VERTEX_ARRAY.glGenVertexArrays());
		}
	}
}
