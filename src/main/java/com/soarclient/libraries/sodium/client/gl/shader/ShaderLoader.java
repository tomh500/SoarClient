package com.soarclient.libraries.sodium.client.gl.shader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.IOUtils;

import com.soarclient.libraries.sodium.client.gl.device.RenderDevice;

public class ShaderLoader {
	public static GlShader loadShader(RenderDevice device, ShaderType type, ResourceLocation name, ShaderConstants constants) {
		return new GlShader(device, type, name, getShaderSource(getShaderPath(name)), constants);
	}

	@Deprecated
	public static GlShader loadShader(RenderDevice device, ShaderType type, ResourceLocation name, List<String> constants) {
		return new GlShader(device, type, name, getShaderSource(getShaderPath(name)), ShaderConstants.fromStringList(constants));
	}

	private static String getShaderPath(ResourceLocation name) {
		return String.format("/assets/%s/shaders/%s", name.getResourceDomain(), name.getResourcePath());
	}

	private static String getShaderSource(String path) {
		try {
			InputStream in = ShaderLoader.class.getResourceAsStream(path);

			String var2;
			try {
				if (in == null) {
					throw new RuntimeException("Shader not found: " + path);
				}

				var2 = IOUtils.toString(in, StandardCharsets.UTF_8);
			} catch (Throwable var5) {
				if (in != null) {
					try {
						in.close();
					} catch (Throwable var4) {
						var5.addSuppressed(var4);
					}
				}

				throw var5;
			}

			if (in != null) {
				in.close();
			}

			return var2;
		} catch (IOException var6) {
			throw new RuntimeException("Could not read shader sources", var6);
		}
	}
}
