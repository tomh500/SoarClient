package com.soarclient.skia.context;

import org.lwjgl.opengl.GL40;

public class State {

    private final Properties props = new Properties();

    public void push() {
    	
        GL40.glGetIntegerv(GL40.GL_ACTIVE_TEXTURE, props.lastActiveTexture);

        for (int i = GL40.GL_TEXTURE0; i <= GL40.GL_TEXTURE31; i++) {
            GL40.glActiveTexture(i);
            props.lastTextures[i - GL40.GL_TEXTURE0] = GL40.glGetInteger(GL40.GL_TEXTURE_BINDING_2D);
            if (VERSION >= 330) {
                props.lastSamplers[i - GL40.GL_TEXTURE0] = GL40.glGetInteger(GL40.GL_SAMPLER_BINDING);
            }
        }

        GL40.glGetIntegerv(GL40.GL_CURRENT_PROGRAM, props.lastProgram);

        GL40.glGetIntegerv(GL40.GL_ARRAY_BUFFER_BINDING, props.lastArrayBuffer);
        GL40.glGetIntegerv(GL40.GL_VERTEX_ARRAY_BINDING, props.lastVertexArrayObject);

        if (VERSION >= 200) {
            GL40.glGetIntegerv(GL40.GL_POLYGON_MODE, props.lastPolygonMode);
        }

        GL40.glGetIntegerv(GL40.GL_VIEWPORT, props.lastViewport);
        GL40.glGetIntegerv(GL40.GL_SCISSOR_BOX, props.lastScissorBox);

        GL40.glGetIntegerv(GL40.GL_BLEND_SRC_RGB, props.lastBlendSrcRgb);
        GL40.glGetIntegerv(GL40.GL_BLEND_DST_RGB, props.lastBlendDstRgb);
        GL40.glGetIntegerv(GL40.GL_BLEND_SRC_ALPHA, props.lastBlendSrcAlpha);
        GL40.glGetIntegerv(GL40.GL_BLEND_DST_ALPHA, props.lastBlendDstAlpha);
        GL40.glGetIntegerv(GL40.GL_BLEND_EQUATION_RGB, props.lastBlendEquationRgb);
        GL40.glGetIntegerv(GL40.GL_BLEND_EQUATION_ALPHA, props.lastBlendEquationAlpha);

        saveEnableStates();

        GL40.glGetIntegerv(GL40.GL_FRAMEBUFFER_BINDING, props.lastFramebuffer);
        GL40.glGetIntegerv(GL40.GL_DRAW_BUFFER, props.lastDrawBuffer);
        GL40.glGetIntegerv(GL40.GL_READ_BUFFER, props.lastReadBuffer);

        saveDepthStencilStates();

        saveDepthRangeAndOtherStates();

        savePixelStoreStates();

        saveColorWriteMask();

        saveNewerOpenGLStates();

        saveAdditionalStates();
    }

    private void saveEnableStates() {
        props.lastEnableBlend = GL40.glIsEnabled(GL40.GL_BLEND);
        props.lastEnableCullFace = GL40.glIsEnabled(GL40.GL_CULL_FACE);
        props.lastEnableDepthTest = GL40.glIsEnabled(GL40.GL_DEPTH_TEST);
        props.lastEnableStencilTest = GL40.glIsEnabled(GL40.GL_STENCIL_TEST);
        props.lastEnableScissorTest = GL40.glIsEnabled(GL40.GL_SCISSOR_TEST);
        if (VERSION >= 310) {
            props.lastEnablePrimitiveRestart = GL40.glIsEnabled(GL40.GL_PRIMITIVE_RESTART);
        }
    }

    private void saveDepthStencilStates() {
        GL40.glGetIntegerv(GL40.GL_DEPTH_FUNC, props.lastDepthFunc);
        GL40.glGetIntegerv(GL40.GL_STENCIL_FUNC, props.lastStencilFunc);
        GL40.glGetIntegerv(GL40.GL_STENCIL_FAIL, props.lastStencilFail);
        GL40.glGetIntegerv(GL40.GL_STENCIL_PASS_DEPTH_FAIL, props.lastStencilPassDepthFail);
        GL40.glGetIntegerv(GL40.GL_STENCIL_PASS_DEPTH_PASS, props.lastStencilPassDepthPass);
        props.lastDepthMask = GL40.glGetBoolean(GL40.GL_DEPTH_WRITEMASK);
        GL40.glGetIntegerv(GL40.GL_STENCIL_WRITEMASK, props.lastStencilMask);
        GL40.glGetIntegerv(GL40.GL_CULL_FACE_MODE, props.lastCullFaceMode);
        GL40.glGetIntegerv(GL40.GL_FRONT_FACE, props.lastFrontFace);
    }

    private void saveDepthRangeAndOtherStates() {
        GL40.glGetDoublev(GL40.GL_DEPTH_RANGE, props.lastDepthRange);
        GL40.glGetFloatv(GL40.GL_LINE_WIDTH, props.lastLineWidth);
        GL40.glGetFloatv(GL40.GL_POINT_SIZE, props.lastPointSize);
        GL40.glGetFloatv(GL40.GL_POLYGON_OFFSET_FACTOR, props.lastPolygonOffsetFactor);
        GL40.glGetFloatv(GL40.GL_POLYGON_OFFSET_UNITS, props.lastPolygonOffsetUnits);
    }

    private void savePixelStoreStates() {
        for (PixelStore parameter : PixelStore.values()) {
            props.pixelStores.put(parameter, GL40.glGetInteger(parameter.getValue()));
        }
    }

    private void saveColorWriteMask() {
        int[] colorWriteMask = new int[4];
        GL40.glGetIntegerv(GL40.GL_COLOR_WRITEMASK, colorWriteMask);
        props.lastColorWriteMask = colorWriteMask;
    }

    private void saveNewerOpenGLStates() {
    	
        if (VERSION >= 300) {
            props.lastEnableDepthClamp = GL40.glIsEnabled(GL40.GL_DEPTH_CLAMP);
        }

        if (VERSION >= 130) {
            props.lastEnableSampleCoverage = GL40.glIsEnabled(GL40.GL_SAMPLE_COVERAGE);
            props.lastEnableSampleAlphaToCoverage = GL40.glIsEnabled(GL40.GL_SAMPLE_ALPHA_TO_COVERAGE);
        }
    }

    private void saveAdditionalStates() {
        if (VERSION >= 300) {
            props.lastEnableMultisample = GL40.glIsEnabled(GL40.GL_MULTISAMPLE);
            props.lastEnableSampleMask = GL40.glIsEnabled(GL40.GL_SAMPLE_MASK);
            props.lastEnableTextureCubeMapSeamless = GL40.glIsEnabled(GL40.GL_TEXTURE_CUBE_MAP_SEAMLESS);
            props.lastEnableFramebufferSRGB = GL40.glIsEnabled(GL40.GL_FRAMEBUFFER_SRGB);
        }
    }

    public void pop() {
    	
        GL40.glUseProgram(props.lastProgram[0]);

        restoreTextures();

        GL40.glActiveTexture(props.lastActiveTexture[0]);

        GL40.glBindVertexArray(props.lastVertexArrayObject[0]);
        GL40.glBindBuffer(GL40.GL_ARRAY_BUFFER, props.lastArrayBuffer[0]);

        restoreBlending();

        restoreEnableStates();

        if (VERSION >= 200) {
            GL40.glPolygonMode(GL40.GL_FRONT_AND_BACK, props.lastPolygonMode[0]);
        }

        GL40.glViewport(props.lastViewport[0], props.lastViewport[1], props.lastViewport[2], props.lastViewport[3]);
        GL40.glScissor(props.lastScissorBox[0], props.lastScissorBox[1], props.lastScissorBox[2], props.lastScissorBox[3]);

        GL40.glBindFramebuffer(GL40.GL_FRAMEBUFFER, props.lastFramebuffer[0]);
        GL40.glDrawBuffer(props.lastDrawBuffer[0]);
        GL40.glReadBuffer(props.lastReadBuffer[0]);

        restoreDepthStencilStates();

        GL40.glDepthMask(props.lastDepthMask);
        GL40.glStencilMask(props.lastStencilMask[0]);
        GL40.glCullFace(props.lastCullFaceMode[0]);
        GL40.glFrontFace(props.lastFrontFace[0]);

        GL40.glDepthRange(props.lastDepthRange[0], props.lastDepthRange[1]);
        GL40.glLineWidth(props.lastLineWidth[0]);
        GL40.glPointSize(props.lastPointSize[0]);
        GL40.glPolygonOffset(props.lastPolygonOffsetFactor[0], props.lastPolygonOffsetUnits[0]);

        restorePixelStoreStates();

        GL40.glColorMask(
            props.lastColorWriteMask[0] != 0,
            props.lastColorWriteMask[1] != 0,
            props.lastColorWriteMask[2] != 0,
            props.lastColorWriteMask[3] != 0
        );

        restoreNewerOpenGLStates();

        restoreAdditionalStates();
    }

    private void restoreTextures() {
        for (int i = GL40.GL_TEXTURE0; i <= GL40.GL_TEXTURE31; i++) {
            GL40.glActiveTexture(i);
            GL40.glBindTexture(GL40.GL_TEXTURE_2D, props.lastTextures[i - GL40.GL_TEXTURE0]);
            if (VERSION >= 330) {
                GL40.glBindSampler(i - GL40.GL_TEXTURE0, props.lastSamplers[i - GL40.GL_TEXTURE0]);
            }
        }
    }

    private void restoreBlending() {
        GL40.glBlendEquationSeparate(props.lastBlendEquationRgb[0], props.lastBlendEquationAlpha[0]);
        GL40.glBlendFuncSeparate(
            props.lastBlendSrcRgb[0], props.lastBlendDstRgb[0],
            props.lastBlendSrcAlpha[0], props.lastBlendDstAlpha[0]
        );
        if (props.lastEnableBlend) GL40.glEnable(GL40.GL_BLEND);
        else GL40.glDisable(GL40.GL_BLEND);
    }

    private void restoreEnableStates() {
        if (props.lastEnableCullFace) GL40.glEnable(GL40.GL_CULL_FACE);
        else GL40.glDisable(GL40.GL_CULL_FACE);
        if (props.lastEnableDepthTest) GL40.glEnable(GL40.GL_DEPTH_TEST);
        else GL40.glDisable(GL40.GL_DEPTH_TEST);
        if (props.lastEnableStencilTest) GL40.glEnable(GL40.GL_STENCIL_TEST);
        else GL40.glDisable(GL40.GL_STENCIL_TEST);
        if (props.lastEnableScissorTest) GL40.glEnable(GL40.GL_SCISSOR_TEST);
        else GL40.glDisable(GL40.GL_SCISSOR_TEST);
        if (VERSION >= 310) {
            if (props.lastEnablePrimitiveRestart) GL40.glEnable(GL40.GL_PRIMITIVE_RESTART);
            else GL40.glDisable(GL40.GL_PRIMITIVE_RESTART);
        }
    }

    private void restoreDepthStencilStates() {
        GL40.glDepthFunc(props.lastDepthFunc[0]);
        GL40.glStencilFunc(props.lastStencilFunc[0], 0, 0xFF);
        GL40.glStencilOp(
            props.lastStencilFail[0],
            props.lastStencilPassDepthFail[0],
            props.lastStencilPassDepthPass[0]
        );
    }

    private void restorePixelStoreStates() {
        for (PixelStore parameter : PixelStore.values()) {
            GL40.glPixelStorei(parameter.getValue(), props.pixelStores.get(parameter));
        }
    }

    private void restoreNewerOpenGLStates() {
    	
        if (VERSION >= 300) {
            if (props.lastEnableDepthClamp) GL40.glEnable(GL40.GL_DEPTH_CLAMP);
            else GL40.glDisable(GL40.GL_DEPTH_CLAMP);
        }

        if (VERSION >= 130) {
            if (props.lastEnableSampleCoverage) GL40.glEnable(GL40.GL_SAMPLE_COVERAGE);
            else GL40.glDisable(GL40.GL_SAMPLE_COVERAGE);
            if (props.lastEnableSampleAlphaToCoverage) GL40.glEnable(GL40.GL_SAMPLE_ALPHA_TO_COVERAGE);
            else GL40.glDisable(GL40.GL_SAMPLE_ALPHA_TO_COVERAGE);
        }
    }

    private void restoreAdditionalStates() {
        if (VERSION >= 300) {
            if (props.lastEnableMultisample) GL40.glEnable(GL40.GL_MULTISAMPLE);
            else GL40.glDisable(GL40.GL_MULTISAMPLE);
            if (props.lastEnableSampleMask) GL40.glEnable(GL40.GL_SAMPLE_MASK);
            else GL40.glDisable(GL40.GL_SAMPLE_MASK);
            if (props.lastEnableTextureCubeMapSeamless) GL40.glEnable(GL40.GL_TEXTURE_CUBE_MAP_SEAMLESS);
            else GL40.glDisable(GL40.GL_TEXTURE_CUBE_MAP_SEAMLESS);
            if (props.lastEnableFramebufferSRGB) GL40.glEnable(GL40.GL_FRAMEBUFFER_SRGB);
            else GL40.glDisable(GL40.GL_FRAMEBUFFER_SRGB);
        }
    }

    private static final int VERSION;

    static {
        int[] major = new int[1];
        int[] minor = new int[1];
        GL40.glGetIntegerv(GL40.GL_MAJOR_VERSION, major);
        GL40.glGetIntegerv(GL40.GL_MINOR_VERSION, minor);
        VERSION = major[0] * 100 + minor[0] * 10;
    }
}