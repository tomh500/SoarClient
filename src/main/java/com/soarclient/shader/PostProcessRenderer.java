package com.soarclient.shader;

import net.minecraft.client.util.math.MatrixStack;

public class PostProcessRenderer {
    private static Mesh mesh;

    private static final MatrixStack matrices = new MatrixStack();

    private PostProcessRenderer() {
    }

    public static void init() {
        mesh = new Mesh();
        mesh.begin();

        mesh.quad(
            mesh.vec2(-1, -1).next(),
            mesh.vec2(-1, 1).next(),
            mesh.vec2(1, 1).next(),
            mesh.vec2(1, -1).next()
        );

        mesh.end();
    }

    public static void beginRender() {
        mesh.beginRender(matrices);
    }

    public static void render() {
        mesh.render(matrices);
    }

    public static void endRender() {
        mesh.endRender();
    }
}
