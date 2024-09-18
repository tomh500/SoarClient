package com.soarclient.libraries.sodium.client.gl.util;

public class BufferSlice {
	public final int start;
	public final int len;

	public BufferSlice(int start, int len) {
		this.start = start;
		this.len = len;
	}

	public static long pack(int start, int len) {
		return (long)start & 4294967295L | ((long)len & 4294967295L) << 32;
	}

	public static int unpackStart(long slice) {
		return (int)(slice & 4294967295L);
	}

	public static int unpackLength(long slice) {
		return (int)(slice >>> 32 & 4294967295L);
	}
}
