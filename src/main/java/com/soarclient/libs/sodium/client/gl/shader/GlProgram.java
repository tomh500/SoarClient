package com.soarclient.libs.sodium.client.gl.shader;

import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL20;

import com.soarclient.libs.sodium.client.gl.GlObject;
import com.soarclient.libs.sodium.client.gl.device.RenderDevice;

public abstract class GlProgram extends GlObject {
	private static final Logger LOGGER = LogManager.getLogger(GlProgram.class);
	private final ResourceLocation name;

	protected GlProgram(RenderDevice owner, ResourceLocation name, int program) {
		super(owner);
		this.name = name;
		this.setHandle(program);
	}

	public static GlProgram.Builder builder(ResourceLocation identifier) {
		return new GlProgram.Builder(identifier);
	}

	public void bind() {
		GL20.glUseProgram(this.handle());
	}

	public void unbind() {
		GL20.glUseProgram(0);
	}

	public ResourceLocation getName() {
		return this.name;
	}

	public int getUniformLocation(String name) {
		int index = GL20.glGetUniformLocation(this.handle(), name);
		if (index < 0) {
			throw new NullPointerException("No uniform exists with name: " + name);
		} else {
			return index;
		}
	}

	public void delete() {
		GL20.glDeleteProgram(this.handle());
		this.invalidateHandle();
	}

	public static class Builder {
		private final ResourceLocation name;
		private final int program;

		public Builder(ResourceLocation name) {
			this.name = name;
			this.program = GL20.glCreateProgram();
		}

		public GlProgram.Builder attachShader(GlShader shader) {
			GL20.glAttachShader(this.program, shader.handle());
			return this;
		}

		public <P extends GlProgram> P build(GlProgram.ProgramFactory<P> factory) {
			GL20.glLinkProgram(this.program);
			String log = GL20.glGetProgramInfoLog(this.program, 32767);
			if (!log.isEmpty()) {
				GlProgram.LOGGER.warn("Program link log for " + this.name + ": " + log);
			}

			int result = GL20.glGetProgrami(this.program, 35714);
			if (result != 1) {
				throw new RuntimeException("Shader program linking failed, see log for details");
			} else {
				return factory.create(this.name, this.program);
			}
		}

		public GlProgram.Builder bindAttribute(String name, ShaderBindingPoint binding) {
			GL20.glBindAttribLocation(this.program, binding.getGenericAttributeIndex(), name);
			return this;
		}
	}

	public interface ProgramFactory<P extends GlProgram> {
		P create(ResourceLocation resourceLocation, int integer);
	}
}
