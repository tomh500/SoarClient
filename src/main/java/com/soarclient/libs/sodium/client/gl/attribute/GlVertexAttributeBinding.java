package com.soarclient.libs.sodium.client.gl.attribute;

import com.soarclient.libs.sodium.client.gl.shader.ShaderBindingPoint;

public class GlVertexAttributeBinding extends GlVertexAttribute {
	private final int index;

	public GlVertexAttributeBinding(ShaderBindingPoint bindingPoint, GlVertexAttribute attribute) {
		super(attribute.getFormat(), attribute.getSize(), attribute.getCount(), attribute.isNormalized(), attribute.getPointer(), attribute.getStride());
		this.index = bindingPoint.getGenericAttributeIndex();
	}

	public int getIndex() {
		return this.index;
	}
}
