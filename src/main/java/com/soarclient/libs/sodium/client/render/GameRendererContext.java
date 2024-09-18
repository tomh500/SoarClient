package com.soarclient.libs.sodium.client.render;

import java.nio.FloatBuffer;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

import net.minecraft.client.renderer.ActiveRenderInfo;

public class GameRendererContext {
	private static final FloatBuffer bufModelViewProjection = BufferUtils.createFloatBuffer(16);

	public static FloatBuffer getModelViewProjectionMatrix() {
		Matrix4f matrix = new Matrix4f(ActiveRenderInfo.PROJECTION);
		Matrix4f model = new Matrix4f(ActiveRenderInfo.MODELVIEW);
		matrix.mul(model);
		matrix.get(bufModelViewProjection);
		return bufModelViewProjection;
	}
}
