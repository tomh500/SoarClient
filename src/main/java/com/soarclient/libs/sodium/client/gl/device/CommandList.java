package com.soarclient.libs.sodium.client.gl.device;

import java.nio.ByteBuffer;

import com.soarclient.libs.sodium.client.gl.array.GlVertexArray;
import com.soarclient.libs.sodium.client.gl.buffer.GlBuffer;
import com.soarclient.libs.sodium.client.gl.buffer.GlBufferTarget;
import com.soarclient.libs.sodium.client.gl.buffer.GlBufferUsage;
import com.soarclient.libs.sodium.client.gl.buffer.GlMutableBuffer;
import com.soarclient.libs.sodium.client.gl.buffer.VertexData;
import com.soarclient.libs.sodium.client.gl.tessellation.GlPrimitiveType;
import com.soarclient.libs.sodium.client.gl.tessellation.GlTessellation;
import com.soarclient.libs.sodium.client.gl.tessellation.TessellationBinding;

public interface CommandList extends AutoCloseable {
	GlVertexArray createVertexArray();

	GlMutableBuffer createMutableBuffer(GlBufferUsage glBufferUsage);

	GlTessellation createTessellation(GlPrimitiveType glPrimitiveType, TessellationBinding[] arr);

	void bindVertexArray(GlVertexArray glVertexArray);

	default void uploadData(GlMutableBuffer glBuffer, VertexData data) {
		this.uploadData(glBuffer, data.buffer);
	}

	void uploadData(GlMutableBuffer glMutableBuffer, ByteBuffer byteBuffer);

	void copyBufferSubData(GlBuffer glBuffer, GlMutableBuffer glMutableBuffer, long long3, long long4, long long5);

	void bindBuffer(GlBufferTarget glBufferTarget, GlBuffer glBuffer);

	void unbindBuffer(GlBufferTarget glBufferTarget);

	void unbindVertexArray();

	void invalidateBuffer(GlMutableBuffer glMutableBuffer);

	void allocateBuffer(GlBufferTarget glBufferTarget, GlMutableBuffer glMutableBuffer, long long3);

	void deleteBuffer(GlBuffer glBuffer);

	void deleteVertexArray(GlVertexArray glVertexArray);

	void flush();

	DrawCommandList beginTessellating(GlTessellation glTessellation);

	void deleteTessellation(GlTessellation glTessellation);

	default void close() {
		this.flush();
	}
}
