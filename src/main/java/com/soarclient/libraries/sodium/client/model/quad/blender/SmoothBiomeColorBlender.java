package com.soarclient.libraries.sodium.client.model.quad.blender;

import com.soarclient.libraries.sodium.client.model.quad.ModelQuadView;
import com.soarclient.libraries.sodium.client.model.quad.properties.ModelQuadFlags;
import com.soarclient.libraries.sodium.client.util.color.ColorABGR;
import com.soarclient.libraries.sodium.client.util.color.ColorARGB;
import com.soarclient.libraries.sodium.client.util.color.ColorU8;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.util.BlockPos;
import net.minecraft.util.BlockPos.MutableBlockPos;
import net.minecraft.world.IBlockAccess;

public class SmoothBiomeColorBlender implements BiomeColorBlender {
	private final int[] cachedRet = new int[4];
	private final MutableBlockPos mpos = new MutableBlockPos();

	@Override
	public int[] getColors(IBlockColor colorizer, IBlockAccess world, IBlockState state, BlockPos origin,
			ModelQuadView quad) {
		int[] colors = this.cachedRet;
		boolean aligned = ModelQuadFlags.contains(quad.getFlags(), 4);

		for (int i = 0; i < 4; i++) {
			if (aligned) {
				colors[i] = this.getVertexColor(colorizer, world, state, origin, quad, i);
			} else {
				colors[i] = this.getInterpolatedVertexColor(colorizer, world, state, origin, quad, i);
			}
		}

		return colors;
	}

	private int getVertexColor(IBlockColor colorizer, IBlockAccess world, IBlockState state, BlockPos origin,
			ModelQuadView quad, int vertexIdx) {
		int x = origin.getX() + (int) quad.getX(vertexIdx);
		int z = origin.getZ() + (int) quad.getZ(vertexIdx);
		int color = this.getBlockColor(colorizer, world, state, origin, x, z, quad.getColorIndex());
		return ColorARGB.toABGR(color);
	}

	private int getBlockColor(IBlockColor colorizer, IBlockAccess world, IBlockState state, BlockPos origin, int x,
			int z, int colorIdx) {
		return colorizer.colorMultiplier(state, world, this.mpos.set(x, origin.getY(), z), colorIdx);
	}

	private int getInterpolatedVertexColor(IBlockColor colorizer, IBlockAccess world, IBlockState state,
			BlockPos origin, ModelQuadView quad, int vertexIdx) {
		float x = quad.getX(vertexIdx);
		float z = quad.getZ(vertexIdx);
		int intX = (int) x;
		int intZ = (int) z;
		int originX = origin.getX() + intX;
		int originZ = origin.getZ() + intZ;
		float fracX = x - (float) intX;
		float fracZ = z - (float) intZ;
		int c1 = this.getBlockColor(colorizer, world, state, origin, originX, originZ, quad.getColorIndex());
		int c2 = this.getBlockColor(colorizer, world, state, origin, originX, originZ + 1, quad.getColorIndex());
		int c3 = this.getBlockColor(colorizer, world, state, origin, originX + 1, originZ, quad.getColorIndex());
		int c4 = this.getBlockColor(colorizer, world, state, origin, originX + 1, originZ + 1, quad.getColorIndex());
		float fr;
		float fg;
		float fb;
		if (c1 == c2 && c2 == c3 && c3 == c4) {
			fr = (float) ColorARGB.unpackRed(c1);
			fg = (float) ColorARGB.unpackGreen(c1);
			fb = (float) ColorARGB.unpackBlue(c1);
		} else {
			float c1r = (float) ColorARGB.unpackRed(c1);
			float c1g = (float) ColorARGB.unpackGreen(c1);
			float c1b = (float) ColorARGB.unpackBlue(c1);
			float c2r = (float) ColorARGB.unpackRed(c2);
			float c2g = (float) ColorARGB.unpackGreen(c2);
			float c2b = (float) ColorARGB.unpackBlue(c2);
			float c3r = (float) ColorARGB.unpackRed(c3);
			float c3g = (float) ColorARGB.unpackGreen(c3);
			float c3b = (float) ColorARGB.unpackBlue(c3);
			float c4r = (float) ColorARGB.unpackRed(c4);
			float c4g = (float) ColorARGB.unpackGreen(c4);
			float c4b = (float) ColorARGB.unpackBlue(c4);
			float r1r = c1r + (c2r - c1r) * fracZ;
			float r1g = c1g + (c2g - c1g) * fracZ;
			float r1b = c1b + (c2b - c1b) * fracZ;
			float r2r = c3r + (c4r - c3r) * fracZ;
			float r2g = c3g + (c4g - c3g) * fracZ;
			float r2b = c3b + (c4b - c3b) * fracZ;
			fr = r1r + (r2r - r1r) * fracX;
			fg = r1g + (r2g - r1g) * fracX;
			fb = r1b + (r2b - r1b) * fracX;
		}

		return ColorABGR.pack(ColorU8.normalize(fr), ColorU8.normalize(fg), ColorU8.normalize(fb));
	}
}
