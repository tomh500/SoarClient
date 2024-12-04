package com.soarclient.libraries.sodium.client.model.quad;

import java.nio.ByteBuffer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public interface ModelQuadView {
	float getX(int integer);

	float getY(int integer);

	float getZ(int integer);

	int getColor(int integer);

	float getTexU(int integer);

	float getTexV(int integer);

	int getFlags();

	int getNormal(int integer);

	int getColorIndex();

	default void copyInto(ByteBuffer buf, int position) {
		for (int i = 0; i < 4; i++) {
			buf.putFloat(position, this.getX(i));
			buf.putFloat(position + 4, this.getY(i));
			buf.putFloat(position + 8, this.getZ(i));
			buf.putInt(position + 12, this.getColor(i));
			buf.putFloat(position + 16, this.getTexU(i));
			buf.putFloat(position + 20, this.getTexV(i));
			position += 28;
		}
	}

	TextureAtlasSprite rubidium$getSprite();
}
