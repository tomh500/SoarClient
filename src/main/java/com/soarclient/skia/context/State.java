package com.soarclient.skia.context;

import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL45;

public class State {
    private final Properties props = new Properties();
    private final int glVersion;

    public State(int glVersion) {
        this.glVersion = glVersion;
    }

    public void push() {
        GL45.glGetIntegerv(GL45.GL_ACTIVE_TEXTURE, props.lastActiveTexture);
        GL45.glActiveTexture(GL45.GL_TEXTURE0);
        GL45.glGetIntegerv(GL45.GL_CURRENT_PROGRAM, props.lastProgram);
        GL45.glGetIntegerv(GL45.GL_TEXTURE_BINDING_2D, props.lastTexture);
        if (glVersion >= 330 || GL.getCapabilities().GL_ARB_sampler_objects) {
            GL45.glGetIntegerv(GL45.GL_SAMPLER_BINDING, props.lastSampler);
        }
        GL45.glGetIntegerv(GL45.GL_ARRAY_BUFFER_BINDING, props.lastArrayBuffer);
        GL45.glGetIntegerv(GL45.GL_VERTEX_ARRAY_BINDING, props.lastVertexArrayObject);
        if (glVersion >= 200) {
            GL45.glGetIntegerv(GL45.GL_POLYGON_MODE, props.lastPolygonMode);
        }
        GL45.glGetIntegerv(GL45.GL_VIEWPORT, props.lastViewport);
        GL45.glGetIntegerv(GL45.GL_SCISSOR_BOX, props.lastScissorBox);
        GL45.glGetIntegerv(GL45.GL_BLEND_SRC_RGB, props.lastBlendSrcRgb);
        GL45.glGetIntegerv(GL45.GL_BLEND_DST_RGB, props.lastBlendDstRgb);
        GL45.glGetIntegerv(GL45.GL_BLEND_SRC_ALPHA, props.lastBlendSrcAlpha);
        GL45.glGetIntegerv(GL45.GL_BLEND_DST_ALPHA, props.lastBlendDstAlpha);
        GL45.glGetIntegerv(GL45.GL_BLEND_EQUATION_RGB, props.lastBlendEquationRgb);
        GL45.glGetIntegerv(GL45.GL_BLEND_EQUATION_ALPHA, props.lastBlendEquationAlpha);
        props.lastEnableBlend = GL45.glIsEnabled(GL45.GL_BLEND);
        props.lastEnableCullFace = GL45.glIsEnabled(GL45.GL_CULL_FACE);
        props.lastEnableDepthTest = GL45.glIsEnabled(GL45.GL_DEPTH_TEST);
        props.lastEnableStencilTest = GL45.glIsEnabled(GL45.GL_STENCIL_TEST);
        props.lastEnableScissorTest = GL45.glIsEnabled(GL45.GL_SCISSOR_TEST);
        if (glVersion >= 310) {
            props.lastEnablePrimitiveRestart = GL45.glIsEnabled(GL45.GL_PRIMITIVE_RESTART);
        }
        props.lastDepthMask = GL45.glGetBoolean(GL45.GL_DEPTH_WRITEMASK);
    }

    public void pop() {
        GL45.glUseProgram(props.lastProgram[0]);
        GL45.glBindTexture(GL45.GL_TEXTURE_2D, props.lastTexture[0]);
        if (glVersion >= 330 || GL.getCapabilities().GL_ARB_sampler_objects) {
            GL45.glBindSampler(0, props.lastSampler[0]);
        }
        GL45.glActiveTexture(props.lastActiveTexture[0]);
        GL45.glBindVertexArray(props.lastVertexArrayObject[0]);
        GL45.glBindBuffer(GL45.GL_ARRAY_BUFFER, props.lastArrayBuffer[0]);
        GL45.glBlendEquationSeparate(props.lastBlendEquationRgb[0], props.lastBlendEquationAlpha[0]);
        GL45.glBlendFuncSeparate(
            props.lastBlendSrcRgb[0], props.lastBlendDstRgb[0], props.lastBlendSrcAlpha[0], props.lastBlendDstAlpha[0]
        );
        if (props.lastEnableBlend) GL45.glEnable(GL45.GL_BLEND);
        else GL45.glDisable(GL45.GL_BLEND);
        if (props.lastEnableCullFace) GL45.glEnable(GL45.GL_CULL_FACE);
        else GL45.glDisable(GL45.GL_CULL_FACE);
        if (props.lastEnableDepthTest) GL45.glEnable(GL45.GL_DEPTH_TEST);
        else GL45.glDisable(GL45.GL_DEPTH_TEST);
        if (props.lastEnableStencilTest) GL45.glEnable(GL45.GL_STENCIL_TEST);
        else GL45.glDisable(GL45.GL_STENCIL_TEST);
        if (props.lastEnableScissorTest) GL45.glEnable(GL45.GL_SCISSOR_TEST);
        else GL45.glDisable(GL45.GL_SCISSOR_TEST);
        if (glVersion >= 310) {
            if (props.lastEnablePrimitiveRestart) GL45.glEnable(GL45.GL_PRIMITIVE_RESTART);
            else GL45.glDisable(GL45.GL_PRIMITIVE_RESTART);
        }
        if (glVersion >= 200) {
            GL45.glPolygonMode(GL45.GL_FRONT_AND_BACK, props.lastPolygonMode[0]);
        }
        GL45.glViewport(props.lastViewport[0], props.lastViewport[1], props.lastViewport[2], props.lastViewport[3]);
        GL45.glScissor(props.lastScissorBox[0], props.lastScissorBox[1], props.lastScissorBox[2], props.lastScissorBox[3]);
        GL45.glDepthMask(props.lastDepthMask);
    }
}