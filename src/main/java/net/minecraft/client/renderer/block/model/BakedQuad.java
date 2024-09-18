package net.minecraft.client.renderer.block.model;

import com.soarclient.libs.sodium.client.model.quad.ModelQuadView;
import com.soarclient.libs.sodium.client.model.quad.properties.ModelQuadFlags;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;

public class BakedQuad implements ModelQuadView
{
    /**
     * Joined 4 vertex records, each has 7 fields (x, y, z, shadeColor, u, v, <unused>), see
     * FaceBakery.storeVertexData()
     */
    protected final int[] vertexData;
    protected final int tintIndex;
    protected final EnumFacing face;

	protected int cachedFlags;
	
    public BakedQuad(int[] vertexDataIn, int tintIndexIn, EnumFacing faceIn)
    {
        this.vertexData = vertexDataIn;
        this.tintIndex = tintIndexIn;
        this.face = faceIn;
        this.cachedFlags = ModelQuadFlags.getQuadFlags(this, this.face);
    }

    public int[] getVertexData()
    {
        return this.vertexData;
    }

    public boolean hasTintIndex()
    {
        return this.tintIndex != -1;
    }

    public int getTintIndex()
    {
        return this.tintIndex;
    }

    public EnumFacing getFace()
    {
        return this.face;
    }

	public int vertexOffset(int vertexIndex) {
		return vertexIndex * 7;
	}

	@Override
	public float getX(int idx) {
		return Float.intBitsToFloat(this.vertexData[this.vertexOffset(idx) + 0]);
	}

	@Override
	public float getY(int idx) {
		return Float.intBitsToFloat(this.vertexData[this.vertexOffset(idx) + 0 + 1]);
	}

	@Override
	public float getZ(int idx) {
		return Float.intBitsToFloat(this.vertexData[this.vertexOffset(idx) + 0 + 2]);
	}

	@Override
	public TextureAtlasSprite rubidium$getSprite() {
		return null;
	}

	@Override
	public int getColor(int idx) {
		return this.vertexOffset(idx) + 3 < this.vertexData.length ? this.vertexData[this.vertexOffset(idx) + 3] : -1;
	}

	@Override
	public float getTexU(int idx) {
		return Float.intBitsToFloat(this.vertexData[this.vertexOffset(idx) + 4]);
	}

	@Override
	public float getTexV(int idx) {
		return Float.intBitsToFloat(this.vertexData[this.vertexOffset(idx) + 4 + 1]);
	}

	@Override
	public int getFlags() {
		return this.cachedFlags;
	}

	@Override
	public int getNormal(int idx) {
		return this.vertexData[this.vertexOffset(idx) + 6];
	}

	@Override
	public int getColorIndex() {
		return this.tintIndex;
	}
}
