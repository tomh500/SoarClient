package com.soarclient.libraries.sodium.client.gl.buffer;

public enum GlBufferTarget {
	ARRAY_BUFFER(34962, 34964),
	COPY_READ_BUFFER(36662, 36662),
	COPY_WRITE_BUFFER(36663, 36663),
	DRAW_INDIRECT_BUFFER(36671, 36675);

	public static final GlBufferTarget[] VALUES = values();
	public static final int COUNT = VALUES.length;
	private final int target;
	private final int binding;

	private GlBufferTarget(int target, int binding) {
		this.target = target;
		this.binding = binding;
	}

	public int getTargetParameter() {
		return this.target;
	}

	public int getBindingParameter() {
		return this.binding;
	}
}
