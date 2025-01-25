package com.soarclient.skia.context;

import java.util.HashMap;

public class Properties {
    public final HashMap<PixelStore, Integer> pixelStores = new HashMap<>();

    public final int[] lastActiveTexture = new int[1];

    public final int[] lastTextures = new int[32];

    public final int[] lastProgram = new int[1];
    public final int[] lastArrayBuffer = new int[1];
    public final int[] lastVertexArrayObject = new int[1];
    public final int[] lastPolygonMode = new int[2];
    public final int[] lastViewport = new int[4];
    public final int[] lastScissorBox = new int[4];
    public final int[] lastBlendSrcRgb = new int[1];
    public final int[] lastBlendDstRgb = new int[1];
    public final int[] lastBlendSrcAlpha = new int[1];
    public final int[] lastBlendDstAlpha = new int[1];
    public final int[] lastBlendEquationRgb = new int[1];
    public final int[] lastBlendEquationAlpha = new int[1];
    public boolean lastEnableBlend = false;
    public boolean lastEnableCullFace = false;
    public boolean lastEnableDepthTest = false;
    public boolean lastEnableStencilTest = false;
    public boolean lastEnableScissorTest = false;
    public boolean lastEnablePrimitiveRestart = false;

    public final int[] lastFramebuffer = new int[1];
    public final int[] lastDrawBuffer = new int[1];
    public final int[] lastReadBuffer = new int[1];
    public final int[] lastDepthFunc = new int[1];
    public final int[] lastStencilFunc = new int[1];
    public final int[] lastStencilFail = new int[1];
    public final int[] lastStencilPassDepthFail = new int[1];
    public final int[] lastStencilPassDepthPass = new int[1];

    public boolean lastDepthMask = false;
    public final int[] lastStencilMask = new int[1];
    public final int[] lastCullFaceMode = new int[1];
    public final int[] lastFrontFace = new int[1];

    public final double[] lastDepthRange = new double[2];
    public final float[] lastLineWidth = new float[1];
    public final float[] lastPointSize = new float[1];
    public final float[] lastPolygonOffsetFactor = new float[1];
    public final float[] lastPolygonOffsetUnits = new float[1];

    public int[] lastColorWriteMask = new int[4];
    public boolean lastEnableDepthClamp = false;
    public boolean lastEnableSampleCoverage = false;
    public boolean lastEnableSampleAlphaToCoverage = false;

    public boolean lastEnableMultisample = false;
    public boolean lastEnableSampleMask = false;
    public boolean lastEnableTextureCubeMapSeamless = false;
    public boolean lastEnableFramebufferSRGB = false;

    public final int[] lastSamplers = new int[32];

    public Properties() {
        for (int i = 0; i < lastTextures.length; i++) {
            lastTextures[i] = -1;
        }
        for (int i = 0; i < lastSamplers.length; i++) {
            lastSamplers[i] = -1;
        }
    }
}