package com.soarclient.libs.sodium.client.render.pipeline;

import com.soarclient.libs.sodium.client.model.light.LightMode;
import com.soarclient.libs.sodium.client.model.light.LightPipeline;
import com.soarclient.libs.sodium.client.model.light.LightPipelineProvider;
import com.soarclient.libs.sodium.client.model.light.data.QuadLightData;
import com.soarclient.libs.sodium.client.model.quad.ModelQuad;
import com.soarclient.libs.sodium.client.model.quad.ModelQuadView;
import com.soarclient.libs.sodium.client.model.quad.ModelQuadViewMutable;
import com.soarclient.libs.sodium.client.model.quad.blender.BiomeColorBlender;
import com.soarclient.libs.sodium.client.model.quad.properties.ModelQuadFacing;
import com.soarclient.libs.sodium.client.render.chunk.compile.buffers.ChunkModelBuffers;
import com.soarclient.libs.sodium.client.render.chunk.format.ModelVertexSink;
import com.soarclient.libs.sodium.client.util.MathUtil;
import com.soarclient.libs.sodium.client.util.Norm3b;
import com.soarclient.libs.sodium.client.util.color.ColorABGR;
import com.soarclient.libs.sodium.common.util.DirectionUtil;
import com.soarclient.libs.sodium.common.util.WorldUtil;
import com.soarclient.libs.sodium.fluid.EmbeddiumFluidSpriteCache;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.util.BlockPos.MutableBlockPos;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.world.IBlockAccess;

public class FluidRenderer {
	private static final float EPSILON = 0.001F;
	private static final IBlockColor FLUID_COLOR_PROVIDER = (state, world, pos, tintIndex) -> -1;
	private final MutableBlockPos scratchPos = new MutableBlockPos();
	private final ModelQuadViewMutable quad = new ModelQuad();
	private final LightPipelineProvider lighters;
	private final BiomeColorBlender biomeColorBlender;
	private final QuadLightData quadLightData = new QuadLightData();
	private final int[] quadColors = new int[4];
	private final EmbeddiumFluidSpriteCache fluidSpriteCache = new EmbeddiumFluidSpriteCache();
	private final BlockColors vanillaBlockColors;

	public FluidRenderer(Minecraft client, LightPipelineProvider lighters, BiomeColorBlender biomeColorBlender) {
		int normal = Norm3b.pack(0.0F, 1.0F, 0.0F);

		for (int i = 0; i < 4; i++) {
			this.quad.setNormal(i, normal);
		}

		this.lighters = lighters;
		this.biomeColorBlender = biomeColorBlender;
		this.vanillaBlockColors = BlockColors.INSTANCE;
	}

	private boolean isFluidOccluded(IBlockAccess world, int x, int y, int z, EnumFacing dir, BlockLiquid fluid) {
		BlockPos pos = this.scratchPos.set(x, y, z);
		IBlockState blockState = world.getBlockState(pos);
		BlockPos adjPos = this.scratchPos.set(x + dir.getFrontOffsetX(), y + dir.getFrontOffsetY(), z + dir.getFrontOffsetZ());
		BlockLiquid adjFluid = WorldUtil.getFluid(world.getBlockState(adjPos));
		boolean temp = fluid == adjFluid;
		return !blockState.getBlock().getMaterial().isOpaque() ? temp : temp || blockState.getBlock().shouldSideBeRendered(world, pos, dir);
	}

	private boolean isSideExposed(IBlockAccess world, int x, int y, int z, EnumFacing dir) {
		BlockPos pos = this.scratchPos.set(x + dir.getFrontOffsetX(), y + dir.getFrontOffsetY(), z + dir.getFrontOffsetZ());
		IBlockState blockState = world.getBlockState(pos);
		Block block = blockState.getBlock();
		if (block.getMaterial().isOpaque()) {
			boolean renderAsFullCube = block.isFullCube();
			return renderAsFullCube ? dir == EnumFacing.UP : true;
		} else {
			return true;
		}
	}

	public boolean render(IBlockAccess world, IBlockState fluidState, BlockPos pos, ChunkModelBuffers buffers) {
		int posX = pos.getX();
		int posY = pos.getY();
		int posZ = pos.getZ();
		BlockLiquid fluid = (BlockLiquid)fluidState.getBlock();
		boolean sfUp = this.isFluidOccluded(world, posX, posY, posZ, EnumFacing.UP, fluid);
		boolean sfDown = this.isFluidOccluded(world, posX, posY, posZ, EnumFacing.DOWN, fluid) || !this.isSideExposed(world, posX, posY, posZ, EnumFacing.DOWN);
		boolean sfNorth = this.isFluidOccluded(world, posX, posY, posZ, EnumFacing.NORTH, fluid);
		boolean sfSouth = this.isFluidOccluded(world, posX, posY, posZ, EnumFacing.SOUTH, fluid);
		boolean sfWest = this.isFluidOccluded(world, posX, posY, posZ, EnumFacing.WEST, fluid);
		boolean sfEast = this.isFluidOccluded(world, posX, posY, posZ, EnumFacing.EAST, fluid);
		if (sfUp && sfDown && sfEast && sfWest && sfNorth && sfSouth) {
			return false;
		} else {
			TextureAtlasSprite[] sprites = this.fluidSpriteCache.getSprites(fluid);
			boolean hc = fluid.getBlockColor() != -1;
			boolean rendered = false;
			float h1 = this.getCornerHeight(world, posX, posY, posZ, fluid);
			float h2 = this.getCornerHeight(world, posX, posY, posZ + 1, fluid);
			float h3 = this.getCornerHeight(world, posX + 1, posY, posZ + 1, fluid);
			float h4 = this.getCornerHeight(world, posX + 1, posY, posZ, fluid);
			float yOffset = sfDown ? 0.0F : 0.001F;
			ModelQuadViewMutable quad = this.quad;
			LightMode mode = hc && Minecraft.isAmbientOcclusionEnabled() ? LightMode.SMOOTH : LightMode.FLAT;
			LightPipeline lighter = this.lighters.getLighter(mode);
			quad.setFlags(0);
			if (!sfUp && this.isSideExposed(world, posX, posY, posZ, EnumFacing.UP)) {
				h1 -= 0.001F;
				h2 -= 0.001F;
				h3 -= 0.001F;
				h4 -= 0.001F;
				Vec3 velocity = BlockLiquid.getFlowingBlock(fluid.getMaterial()).getFlowVector(world, pos);
				TextureAtlasSprite sprite;
				ModelQuadFacing facing;
				float u1;
				float u2;
				float u3;
				float u4;
				float v1;
				float v2;
				float v3;
				float v4;
				if (velocity.xCoord == 0.0 && velocity.zCoord == 0.0) {
					sprite = sprites[0];
					facing = ModelQuadFacing.UP;
					u1 = sprite.getInterpolatedU(0.0);
					v1 = sprite.getInterpolatedV(0.0);
					u2 = u1;
					v2 = sprite.getInterpolatedV(16.0);
					u3 = sprite.getInterpolatedU(16.0);
					v3 = v2;
					u4 = u3;
					v4 = v1;
				} else {
					sprite = sprites[1];
					facing = ModelQuadFacing.UNASSIGNED;
					float dir = (float)MathHelper.atan2(velocity.zCoord, velocity.xCoord) - (float) (Math.PI / 2);
					float sin = MathHelper.sin(dir) * 0.25F;
					float cos = MathHelper.cos(dir) * 0.25F;
					u1 = sprite.getInterpolatedU((double)(8.0F + (-cos - sin) * 16.0F));
					v1 = sprite.getInterpolatedV((double)(8.0F + (-cos + sin) * 16.0F));
					u2 = sprite.getInterpolatedU((double)(8.0F + (-cos + sin) * 16.0F));
					v2 = sprite.getInterpolatedV((double)(8.0F + (cos + sin) * 16.0F));
					u3 = sprite.getInterpolatedU((double)(8.0F + (cos + sin) * 16.0F));
					v3 = sprite.getInterpolatedV((double)(8.0F + (cos - sin) * 16.0F));
					u4 = sprite.getInterpolatedU((double)(8.0F + (cos - sin) * 16.0F));
					v4 = sprite.getInterpolatedV((double)(8.0F + (-cos - sin) * 16.0F));
				}

				float uAvg = (u1 + u2 + u3 + u4) / 4.0F;
				float vAvg = (v1 + v2 + v3 + v4) / 4.0F;
				float s1 = (float)sprites[0].getIconWidth() / (sprites[0].getMaxU() - sprites[0].getMinU());
				float s2 = (float)sprites[0].getIconHeight() / (sprites[0].getMaxV() - sprites[0].getMinV());
				float s3 = 4.0F / Math.max(s2, s1);
				u1 = (float)MathUtil.lerp((double)s3, (double)u1, (double)uAvg);
				u2 = (float)MathUtil.lerp((double)s3, (double)u2, (double)uAvg);
				u3 = (float)MathUtil.lerp((double)s3, (double)u3, (double)uAvg);
				u4 = (float)MathUtil.lerp((double)s3, (double)u4, (double)uAvg);
				v1 = (float)MathUtil.lerp((double)s3, (double)v1, (double)vAvg);
				v2 = (float)MathUtil.lerp((double)s3, (double)v2, (double)vAvg);
				v3 = (float)MathUtil.lerp((double)s3, (double)v3, (double)vAvg);
				v4 = (float)MathUtil.lerp((double)s3, (double)v4, (double)vAvg);
				quad.setSprite(sprite);
				this.setVertex(quad, 0, 0.0F, h1, 0.0F, u1, v1);
				this.setVertex(quad, 1, 0.0F, h2, 1.0F, u2, v2);
				this.setVertex(quad, 2, 1.0F, h3, 1.0F, u3, v3);
				this.setVertex(quad, 3, 1.0F, h4, 0.0F, u4, v4);
				this.calculateQuadColors(quad, world, pos, lighter, EnumFacing.UP, 1.0F, hc);
				this.flushQuad(buffers, quad, facing, false);
				if (WorldUtil.method_15756(world, this.scratchPos.set(posX, posY + 1, posZ), fluid)) {
					this.setVertex(quad, 3, 0.0F, h1, 0.0F, u1, v1);
					this.setVertex(quad, 2, 0.0F, h2, 1.0F, u2, v2);
					this.setVertex(quad, 1, 1.0F, h3, 1.0F, u3, v3);
					this.setVertex(quad, 0, 1.0F, h4, 0.0F, u4, v4);
					this.flushQuad(buffers, quad, ModelQuadFacing.DOWN, true);
				}

				rendered = true;
			}

			if (!sfDown) {
				TextureAtlasSprite spritex = sprites[0];
				float minU = spritex.getMinU();
				float maxU = spritex.getMaxU();
				float minV = spritex.getMinV();
				float maxV = spritex.getMaxV();
				quad.setSprite(spritex);
				this.setVertex(quad, 0, 0.0F, yOffset, 1.0F, minU, maxV);
				this.setVertex(quad, 1, 0.0F, yOffset, 0.0F, minU, minV);
				this.setVertex(quad, 2, 1.0F, yOffset, 0.0F, maxU, minV);
				this.setVertex(quad, 3, 1.0F, yOffset, 1.0F, maxU, maxV);
				this.calculateQuadColors(quad, world, pos, lighter, EnumFacing.DOWN, 1.0F, hc);
				this.flushQuad(buffers, quad, ModelQuadFacing.DOWN, false);
				rendered = true;
			}

			quad.setFlags(4);

			for (EnumFacing dir : DirectionUtil.HORIZONTAL_DIRECTIONS) {
				float c1;
				float c2;
				float x1;
				float z1;
				float x2;
				float z2;
				switch (dir) {
					case NORTH:
						if (sfNorth) {
							continue;
						}

						c1 = h1;
						c2 = h4;
						x1 = 0.0F;
						x2 = 1.0F;
						z1 = 0.001F;
						z2 = z1;
						break;
					case SOUTH:
						if (sfSouth) {
							continue;
						}

						c1 = h3;
						c2 = h2;
						x1 = 1.0F;
						x2 = 0.0F;
						z1 = 0.999F;
						z2 = z1;
						break;
					case WEST:
						if (sfWest) {
							continue;
						}

						c1 = h2;
						c2 = h1;
						x1 = 0.001F;
						x2 = x1;
						z1 = 1.0F;
						z2 = 0.0F;
						break;
					case EAST:
						if (!sfEast) {
							c1 = h4;
							c2 = h3;
							x1 = 0.999F;
							x2 = x1;
							z1 = 0.0F;
							z2 = 1.0F;
							break;
						}
					default:
						continue;
				}

				if (this.isSideExposed(world, posX, posY, posZ, dir)) {
					TextureAtlasSprite spritex = sprites[1];
					float u1x = spritex.getInterpolatedU(0.0);
					float u2x = spritex.getInterpolatedU(8.0);
					float v1x = spritex.getInterpolatedV((double)((1.0F - c1) * 16.0F * 0.5F));
					float v2x = spritex.getInterpolatedV((double)((1.0F - c2) * 16.0F * 0.5F));
					float v3x = spritex.getInterpolatedV(8.0);
					quad.setSprite(spritex);
					this.setVertex(quad, 0, x2, c2, z2, u2x, v2x);
					this.setVertex(quad, 1, x2, yOffset, z2, u2x, v3x);
					this.setVertex(quad, 2, x1, yOffset, z1, u1x, v3x);
					this.setVertex(quad, 3, x1, c1, z1, u1x, v1x);
					float br = dir.getAxis() == Axis.Z ? 0.8F : 0.6F;
					ModelQuadFacing facingx = ModelQuadFacing.fromDirection(dir);
					this.calculateQuadColors(quad, world, pos, lighter, dir, br, hc);
					this.flushQuad(buffers, quad, facingx, false);
					this.setVertex(quad, 0, x1, c1, z1, u1x, v1x);
					this.setVertex(quad, 1, x1, yOffset, z1, u1x, v3x);
					this.setVertex(quad, 2, x2, yOffset, z2, u2x, v3x);
					this.setVertex(quad, 3, x2, c2, z2, u2x, v2x);
					this.flushQuad(buffers, quad, facingx.getOpposite(), true);
					rendered = true;
				}
			}

			return rendered;
		}
	}

	private void calculateQuadColors(
		ModelQuadView quad, IBlockAccess world, BlockPos pos, LightPipeline lighter, EnumFacing dir, float brightness, boolean colorized
	) {
		QuadLightData light = this.quadLightData;
		lighter.calculate(quad, pos, light, null, dir, false);
		int[] biomeColors = null;
		if (colorized) {
			IBlockState state = world.getBlockState(pos);
			IBlockColor colorProvider = this.vanillaBlockColors.getColorProvider(state);
			boolean containsColoredQuad = false;
			if (colorProvider != null) {
				biomeColors = this.biomeColorBlender.getColors(colorProvider, world, state, pos, quad);

				for (int color : biomeColors) {
					if (color != 16777215) {
						containsColoredQuad = true;
						break;
					}
				}
			}

			if (!containsColoredQuad) {
				biomeColors = this.biomeColorBlender.getColors(FLUID_COLOR_PROVIDER, world, state, pos, quad);
			}
		}

		for (int i = 0; i < 4; i++) {
			this.quadColors[i] = ColorABGR.mul(biomeColors != null ? biomeColors[i] : -1, light.br[i] * brightness);
		}
	}

	private void flushQuad(ChunkModelBuffers buffers, ModelQuadView quad, ModelQuadFacing facing, boolean flip) {
		int vertexIdx;
		int lightOrder;
		if (flip) {
			vertexIdx = 3;
			lightOrder = -1;
		} else {
			vertexIdx = 0;
			lightOrder = 1;
		}

		ModelVertexSink sink = buffers.getSink(facing);
		sink.ensureCapacity(4);

		for (int i = 0; i < 4; i++) {
			float x = quad.getX(i);
			float y = quad.getY(i);
			float z = quad.getZ(i);
			int color = this.quadColors[vertexIdx];
			float u = quad.getTexU(i);
			float v = quad.getTexV(i);
			int light = this.quadLightData.lm[vertexIdx];
			sink.writeQuad(x, y, z, color, u, v, light);
			vertexIdx += lightOrder;
		}

		sink.flush();
	}

	private void setVertex(ModelQuadViewMutable quad, int i, float x, float y, float z, float u, float v) {
		quad.setX(i, x);
		quad.setY(i, y);
		quad.setZ(i, z);
		quad.setTexU(i, u);
		quad.setTexV(i, v);
	}

	private float getCornerHeight(IBlockAccess world, int x, int y, int z, BlockLiquid fluid) {
		int samples = 0;
		float totalHeight = 0.0F;

		for (int i = 0; i < 4; i++) {
			int x2 = x - (i & 1);
			int z2 = z - (i >> 1 & 1);
			Block block = world.getBlockState(this.scratchPos.set(x2, y + 1, z2)).getBlock();
			if (block.getMaterial() == fluid.getMaterial()) {
				return 1.0F;
			}

			BlockPos pos = this.scratchPos.set(x2, y, z2);
			IBlockState blockState = world.getBlockState(pos);
			Material material = blockState.getBlock().getMaterial();
			if (fluid.getMaterial() != material) {
				if (!material.isSolid()) {
					samples++;
					totalHeight++;
				}
			} else {
				int height = (Integer)blockState.getValue(BlockLiquid.LEVEL);
				if (height < 8 && height != 0) {
					totalHeight += BlockLiquid.getLiquidHeightPercent(height);
					samples++;
				} else {
					totalHeight += BlockLiquid.getLiquidHeightPercent(height) * 10.0F;
					samples += 10;
				}
			}
		}

		return 1.0F - totalHeight / (float)samples;
	}
}
