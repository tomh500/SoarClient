package com.soarclient.libraries.sodium.client.render.chunk.shader;

import java.util.function.Function;

import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL20;

import com.soarclient.libraries.sodium.client.gl.device.RenderDevice;
import com.soarclient.libraries.sodium.client.gl.shader.GlProgram;
import com.soarclient.libraries.sodium.client.render.GameRendererContext;

public class ChunkProgram extends GlProgram {
	private final int uModelViewProjectionMatrix = this.getUniformLocation("u_ModelViewProjectionMatrix");
	private final int uModelScale;
	private final int uTextureScale;
	private final int uBlockTex = this.getUniformLocation("u_BlockTex");
	private final int uLightTex = this.getUniformLocation("u_LightTex");
	private final ChunkShaderFogComponent fogShader;

	protected ChunkProgram(RenderDevice owner, ResourceLocation name, int handle,
			Function<ChunkProgram, ChunkShaderFogComponent> fogShaderFunction) {
		super(owner, name, handle);
		this.uModelScale = this.getUniformLocation("u_ModelScale");
		this.uTextureScale = this.getUniformLocation("u_TextureScale");
		this.fogShader = (ChunkShaderFogComponent) fogShaderFunction.apply(this);
	}

	public void setup(float modelScale, float textureScale) {
		GL20.glUniform1i(this.uBlockTex, 0);
		GL20.glUniform1i(this.uLightTex, 1);
		GL20.glUniform3f(this.uModelScale, modelScale, modelScale, modelScale);
		GL20.glUniform2f(this.uTextureScale, textureScale, textureScale);
		this.fogShader.setup();
		GL20.glUniformMatrix4(this.uModelViewProjectionMatrix, false,
				GameRendererContext.getModelViewProjectionMatrix());
	}
}
