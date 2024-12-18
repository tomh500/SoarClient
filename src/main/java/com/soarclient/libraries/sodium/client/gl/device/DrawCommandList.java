package com.soarclient.libraries.sodium.client.gl.device;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public interface DrawCommandList extends AutoCloseable {
	void multiDrawArrays(IntBuffer intBuffer1, IntBuffer intBuffer2);

	void multiDrawArraysIndirect(ByteBuffer byteBuffer, int integer2, int integer3);

	void multiDrawArraysIndirect(long long1, int integer2, int integer3);

	void endTessellating();

	void flush();

	default void close() {
		this.flush();
	}
}
