package com.soarclient.shaders;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;

public class Shader {

	private final int fragId, vertId, program;
	private final Map<String, Integer> uniformLocationMap = new HashMap<>();

	public Shader(ResourceLocation location) {

		vertId = createShader(readResourceLocation(new ResourceLocation("soar/shader/vertex.vert")),
				GL20.GL_VERTEX_SHADER);
		fragId = createShader(readResourceLocation(location), GL20.GL_FRAGMENT_SHADER);

		if (vertId != 0 && fragId != 0) {
			program = ARBShaderObjects.glCreateProgramObjectARB();

			if (program != 0) {
				ARBShaderObjects.glAttachObjectARB(program, vertId);
				ARBShaderObjects.glAttachObjectARB(program, fragId);
				ARBShaderObjects.glLinkProgramARB(program);
				ARBShaderObjects.glValidateProgramARB(program);
			}
		} else {
			program = -1;
		}
	}

	public void init() {
		GL20.glUseProgram(program);
	}

	public void bind() {

		ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());

		double width = sr.getScaledWidth_double();
		double height = sr.getScaledHeight_double();

		GL11.glBegin(GL11.GL_QUADS);
		GL11.glTexCoord2f(0, 1);
		GL11.glVertex2f(0, 0);
		GL11.glTexCoord2f(0, 0);
		GL11.glVertex2d(0, height);
		GL11.glTexCoord2f(1, 0);
		GL11.glVertex2d(width, height);
		GL11.glTexCoord2f(1, 1);
		GL11.glVertex2d(width, 0);
		GL11.glEnd();
	}

	public void bind(float x, float y, float width, float height) {

		GL11.glBegin(GL11.GL_QUADS);

		GL11.glTexCoord2f(0f, 0f);
		GL11.glVertex2f(x, y);
		GL11.glTexCoord2f(0f, 1f);
		GL11.glVertex2f(x, y + height);
		GL11.glTexCoord2f(1f, 1f);
		GL11.glVertex2f(x + width, y + height);
		GL11.glTexCoord2f(1f, 0f);
		GL11.glVertex2f(x + width, y);

		GL11.glEnd();
	}

	public void finish() {
		GL20.glUseProgram(0);
	}
	
    public void delete() {
        if (program > 0) {
            GL20.glDeleteProgram(program);
        }
    }
    
	public void setupUniform(final String uniform) {
		this.uniformLocationMap.put(uniform, GL20.glGetUniformLocation(this.program, uniform));
	}

	public int getUniform(final String uniform) {
		return this.uniformLocationMap.get(uniform);
	}

	private String readResourceLocation(ResourceLocation loc) {
		StringBuilder stringBuilder = new StringBuilder();

		try {
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
					Minecraft.getMinecraft().getResourceManager().getResource(loc).getInputStream()));
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				stringBuilder.append(line).append('\n');
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return stringBuilder.toString();
	}

	private int createShader(String source, int type) {

		int shader = 0;

		try {
			shader = ARBShaderObjects.glCreateShaderObjectARB(type);

			if (shader != 0) {
				ARBShaderObjects.glShaderSourceARB(shader, source);
				ARBShaderObjects.glCompileShaderARB(shader);

				if (ARBShaderObjects.glGetObjectParameteriARB(shader, 35713) == 0) {
					throw new RuntimeException("Failed to create shader: " + ARBShaderObjects.glGetInfoLogARB(shader,
							ARBShaderObjects.glGetObjectParameteriARB(shader, 35716)));
				}

				return shader;
			} else {
				return 0;
			}
		} catch (Exception e) {
			ARBShaderObjects.glDeleteObjectARB(shader);
			e.printStackTrace();
			throw e;
		}
	}
}
