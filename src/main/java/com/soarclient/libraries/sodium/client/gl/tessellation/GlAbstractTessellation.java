package com.soarclient.libraries.sodium.client.gl.tessellation;

import org.lwjgl.opengl.GL20;

import com.soarclient.libraries.sodium.client.gl.attribute.GlVertexAttributeBinding;
import com.soarclient.libraries.sodium.client.gl.buffer.GlBufferTarget;
import com.soarclient.libraries.sodium.client.gl.device.CommandList;
import com.soarclient.libraries.sodium.client.gl.func.GlFunctions;

public abstract class GlAbstractTessellation implements GlTessellation {
	protected final GlPrimitiveType primitiveType;
	protected final TessellationBinding[] bindings;

	protected GlAbstractTessellation(GlPrimitiveType primitiveType, TessellationBinding[] bindings) {
		this.primitiveType = primitiveType;
		this.bindings = bindings;
	}

	@Override
	public GlPrimitiveType getPrimitiveType() {
		return this.primitiveType;
	}

	protected void bindAttributes(CommandList commandList) {
		for (TessellationBinding binding : this.bindings) {
			commandList.bindBuffer(GlBufferTarget.ARRAY_BUFFER, binding.getBuffer());

			for (GlVertexAttributeBinding attrib : binding.getAttributeBindings()) {
				GL20.glVertexAttribPointer(attrib.getIndex(), attrib.getCount(), attrib.getFormat(), attrib.isNormalized(), attrib.getStride(), (long)attrib.getPointer());
				GL20.glEnableVertexAttribArray(attrib.getIndex());
				if (binding.isInstanced()) {
					GlFunctions.INSTANCED_ARRAY.glVertexAttribDivisor(attrib.getIndex(), 1);
				}
			}
		}
	}
}
