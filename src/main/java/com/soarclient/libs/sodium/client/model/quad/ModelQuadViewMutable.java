package com.soarclient.libs.sodium.client.model.quad;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public interface ModelQuadViewMutable extends ModelQuadView {
	void setX(int integer, float float2);

	void setY(int integer, float float2);

	void setZ(int integer, float float2);

	void setColor(int integer1, int integer2);

	void setTexU(int integer, float float2);

	void setTexV(int integer, float float2);

	void setNormal(int integer1, int integer2);

	void setFlags(int integer);

	void setSprite(TextureAtlasSprite textureAtlasSprite);

	void setColorIndex(int integer);
}
