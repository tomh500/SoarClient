package com.soarclient.libs.sodium.client.gl.tessellation;

import org.lwjgl.opengl.GL20;

import com.soarclient.libs.sodium.client.gl.attribute.GlVertexAttributeBinding;
import com.soarclient.libs.sodium.client.gl.device.CommandList;

public class GlFallbackTessellation extends GlAbstractTessellation {
	public GlFallbackTessellation(GlPrimitiveType primitiveType, TessellationBinding[] bindings) {
		super(primitiveType, bindings);
	}

	@Override
	public void delete(CommandList commandList) {
	}

	@Override
	public void bind(CommandList commandList) {
		this.bindAttributes(commandList);
	}

	@Override
	public void unbind(CommandList commandList) {
		for (TessellationBinding binding : this.bindings) {
			for (GlVertexAttributeBinding attrib : binding.getAttributeBindings()) {
				GL20.glDisableVertexAttribArray(attrib.getIndex());
			}
		}
	}
}
