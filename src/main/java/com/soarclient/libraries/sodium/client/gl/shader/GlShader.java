package com.soarclient.libraries.sodium.client.gl.shader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL20;

import com.soarclient.libraries.sodium.client.gl.GlObject;
import com.soarclient.libraries.sodium.client.gl.device.RenderDevice;

public class GlShader extends GlObject {
	private static final Logger LOGGER = LogManager.getLogger(GlShader.class);
	private final ResourceLocation name;

	public GlShader(RenderDevice owner, ShaderType type, ResourceLocation name, String src, ShaderConstants constants) {
		super(owner);
		this.name = name;
		src = processShader(src, constants);
		int handle = GL20.glCreateShader(type.id);
		ShaderWorkarounds.safeShaderSource(handle, src);
		GL20.glCompileShader(handle);
		String log = GL20.glGetShaderInfoLog(handle, 32767);
		if (!log.isEmpty()) {
			LOGGER.warn("Shader compilation log for " + this.name + ": " + log);
		}

		int result = GL20.glGetShaderi(handle, 35713);
		if (result != 1) {
			throw new RuntimeException("Shader compilation failed, see log for details");
		} else {
			this.setHandle(handle);
		}
	}

	private static String processShader(String src, ShaderConstants constants) {
		StringBuilder builder = new StringBuilder(src.length());
		boolean patched = false;

		try {
			BufferedReader reader = new BufferedReader(new StringReader(src));

			String line;
			try {
				while ((line = reader.readLine()) != null) {
					builder.append(line).append("\n");
					if (!patched && line.startsWith("#version")) {
						for (String macro : constants.getDefineStrings()) {
							builder.append(macro).append('\n');
						}

						patched = true;
					}
				}
			} catch (Throwable var9) {
				try {
					reader.close();
				} catch (Throwable var8) {
					var9.addSuppressed(var8);
				}

				throw var9;
			}

			reader.close();
		} catch (IOException var10) {
			throw new RuntimeException("Could not process shader source", var10);
		}

		return builder.toString();
	}

	public ResourceLocation getName() {
		return this.name;
	}

	public void delete() {
		GL20.glDeleteShader(this.handle());
		this.invalidateHandle();
	}
}
