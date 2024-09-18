package com.soarclient.libraries.sodium.client.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;
import sun.misc.Unsafe;

public class CompatMemoryUtil {
	static final Unsafe UNSAFE = getUnsafeInstance();

	public static ByteBuffer memReallocDirect(ByteBuffer old, int capacity) {
		ByteBuffer newBuf = BufferUtils.createByteBuffer(capacity);
		int oldPos = old.position();
		old.rewind();
		newBuf.put(old);
		newBuf.position(Math.min(capacity, oldPos));
		return newBuf;
	}

	public static IntBuffer memReallocDirect(IntBuffer old, int capacity) {
		IntBuffer newBuf = BufferUtils.createIntBuffer(capacity);
		int oldPos = old.position();
		old.rewind();
		newBuf.put(old);
		newBuf.position(Math.min(capacity, oldPos));
		return newBuf;
	}

	public static FloatBuffer memReallocDirect(FloatBuffer old, int capacity) {
		FloatBuffer newBuf = BufferUtils.createFloatBuffer(capacity);
		int oldPos = old.position();
		old.rewind();
		newBuf.put(old);
		newBuf.position(Math.min(capacity, oldPos));
		return newBuf;
	}

	private static Unsafe getUnsafeInstance() {
		Field[] fields = Unsafe.class.getDeclaredFields();

		for (Field field : fields) {
			if (field.getType().equals(Unsafe.class)) {
				int modifiers = field.getModifiers();
				if (Modifier.isStatic(modifiers) && Modifier.isFinal(modifiers)) {
					try {
						field.setAccessible(true);
						return (Unsafe)field.get(null);
					} catch (Exception var7) {
						break;
					}
				}
			}
		}

		throw new UnsupportedOperationException("LWJGL requires sun.misc.Unsafe to be available.");
	}

	public static void memPutByte(long ptr, byte value) {
		UNSAFE.putByte(null, ptr, value);
	}

	public static void memPutShort(long ptr, short value) {
		UNSAFE.putShort(null, ptr, value);
	}

	public static void memPutInt(long ptr, int value) {
		UNSAFE.putInt(null, ptr, value);
	}

	public static void memPutLong(long ptr, long value) {
		UNSAFE.putLong(null, ptr, value);
	}

	public static void memPutFloat(long ptr, float value) {
		UNSAFE.putFloat(null, ptr, value);
	}

	public static void memPutDouble(long ptr, double value) {
		UNSAFE.putDouble(null, ptr, value);
	}

	public static boolean memGetBoolean(long ptr) {
		return UNSAFE.getByte(null, ptr) != 0;
	}

	public static byte memGetByte(long ptr) {
		return UNSAFE.getByte(null, ptr);
	}

	public static short memGetShort(long ptr) {
		return UNSAFE.getShort(null, ptr);
	}

	public static int memGetInt(long ptr) {
		return UNSAFE.getInt(null, ptr);
	}

	public static long memGetLong(long ptr) {
		return UNSAFE.getLong(null, ptr);
	}

	public static float memGetFloat(long ptr) {
		return UNSAFE.getFloat(null, ptr);
	}

	public static double memGetDouble(long ptr) {
		return UNSAFE.getDouble(null, ptr);
	}
}
