package com.soarclient.libs.sodium.client.render.pipeline;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.Block.EnumOffsetType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.util.vector.Vector3f;

import com.soarclient.libs.sodium.client.model.light.LightMode;
import com.soarclient.libs.sodium.client.model.light.LightPipeline;
import com.soarclient.libs.sodium.client.model.light.LightPipelineProvider;
import com.soarclient.libs.sodium.client.model.light.data.QuadLightData;
import com.soarclient.libs.sodium.client.model.quad.ModelQuadView;
import com.soarclient.libs.sodium.client.model.quad.blender.BiomeColorBlender;
import com.soarclient.libs.sodium.client.model.quad.properties.ModelQuadFacing;
import com.soarclient.libs.sodium.client.model.quad.properties.ModelQuadOrientation;
import com.soarclient.libs.sodium.client.render.chunk.compile.buffers.ChunkModelBuffers;
import com.soarclient.libs.sodium.client.render.chunk.data.ChunkRenderData.Builder;
import com.soarclient.libs.sodium.client.render.chunk.format.ModelVertexSink;
import com.soarclient.libs.sodium.client.render.occlusion.BlockOcclusionCache;
import com.soarclient.libs.sodium.client.util.color.ColorABGR;
import com.soarclient.libs.sodium.client.world.biome.BlockColorsExtended;
import com.soarclient.libs.sodium.common.util.DirectionUtil;

public class BlockRenderer {
	private final BlockColorsExtended blockColors;
	private final QuadLightData cachedQuadLightData = new QuadLightData();
	private final BiomeColorBlender biomeColorBlender;
	private final LightPipelineProvider lighters;
	private final BlockOcclusionCache occlusionCache;
	private final boolean useAmbientOcclusion;

	public BlockRenderer(Minecraft client, LightPipelineProvider lighters, BiomeColorBlender biomeColorBlender) {
		this.blockColors = BlockColors.INSTANCE;
		this.biomeColorBlender = biomeColorBlender;
		this.lighters = lighters;
		this.useAmbientOcclusion = Minecraft.isAmbientOcclusionEnabled();
		this.occlusionCache = new BlockOcclusionCache();
	}

	public boolean renderModel(IBlockAccess world, IBlockState state, BlockPos pos, IBakedModel model, ChunkModelBuffers buffers, boolean cull) {
		LightMode mode = this.getLightingMode(state, model, world, pos);
		LightPipeline lighter = this.lighters.getLighter(mode);
		Vector3f offset = new Vector3f();
		EnumOffsetType offsetType = state.getBlock().getOffsetType();
		if (offsetType != EnumOffsetType.NONE) {
			int x = pos.getX();
			int z = pos.getZ();
			long i = (long)x * 3129871L ^ (long)z * 116129781L;
			i = i * i * 42317861L + i * 11L;
			offset.x += ((float)(i >> 16 & 15L) / 15.0F - 0.5F) * 0.5F;
			offset.z += ((float)(i >> 24 & 15L) / 15.0F - 0.5F) * 0.5F;
			if (offsetType == EnumOffsetType.XYZ) {
				offset.y += ((float)(i >> 20 & 15L) / 15.0F - 1.0F) * 0.2F;
			}
		}

		boolean rendered = false;

		for (EnumFacing dir : DirectionUtil.ALL_DIRECTIONS) {
			List<BakedQuad> sided = model.getFaceQuads(dir);
			if (!sided.isEmpty() && (!cull || this.occlusionCache.shouldDrawSide(world, pos, dir))) {
				this.renderQuadList(world, state, pos, lighter, offset, buffers, sided, dir);
				rendered = true;
			}
		}

		List<BakedQuad> all = model.getGeneralQuads();
		if (!all.isEmpty()) {
			this.renderQuadList(world, state, pos, lighter, offset, buffers, all, null);
			rendered = true;
		}

		return rendered;
	}

	private void renderQuadList(
		IBlockAccess world,
		IBlockState state,
		BlockPos pos,
		LightPipeline lighter,
		Vector3f offset,
		ChunkModelBuffers buffers,
		List<BakedQuad> quads,
		EnumFacing cullFace
	) {
		ModelQuadFacing facing = cullFace == null ? ModelQuadFacing.UNASSIGNED : ModelQuadFacing.fromDirection(cullFace);
		IBlockColor colorizer = null;
		ModelVertexSink sink = buffers.getSink(facing);
		sink.ensureCapacity(quads.size() * 4);
		Builder renderData = buffers.getRenderData();
		int i = 0;

		for (int quadsSize = quads.size(); i < quadsSize; i++) {
			BakedQuad quad = (BakedQuad)quads.get(i);
			EnumFacing quadFace = quad.getFace();
			QuadLightData light = this.cachedQuadLightData;
			lighter.calculate((ModelQuadView)quad, pos, light, cullFace, quadFace, quad.hasTintIndex());
			if (quad.hasTintIndex() && colorizer == null) {
				colorizer = this.blockColors.getColorProvider(state);
			}

			this.renderQuad(world, state, pos, sink, offset, colorizer, quad, light, renderData);
		}

		sink.flush();
	}

	private void renderQuad(
		IBlockAccess world,
		IBlockState state,
		BlockPos pos,
		ModelVertexSink sink,
		Vector3f offset,
		IBlockColor colorProvider,
		BakedQuad bakedQuad,
		QuadLightData light,
		Builder renderData
	) {
		ModelQuadView src = (ModelQuadView)bakedQuad;
		ModelQuadOrientation order = ModelQuadOrientation.orient(light.br);
		int[] colors = null;
		if (bakedQuad.hasTintIndex()) {
			colors = this.biomeColorBlender.getColors(colorProvider, world, state, pos, src);
		}

		for (int dstIndex = 0; dstIndex < 4; dstIndex++) {
			int srcIndex = order.getVertexIndex(dstIndex);
			float x = src.getX(srcIndex) + offset.x;
			float y = src.getY(srcIndex) + offset.y;
			float z = src.getZ(srcIndex) + offset.z;
			int color;
			if (state.getBlock().isTranslucent()) {
				color = ColorABGR.mul(colors != null ? colors[srcIndex] : src.getColor(srcIndex), 1.0F);
			} else {
				color = ColorABGR.mul(colors != null ? colors[srcIndex] : src.getColor(srcIndex), light.br[srcIndex]);
			}

			float u = src.getTexU(srcIndex);
			float v = src.getTexV(srcIndex);
			int lm = light.lm[srcIndex];
			sink.writeQuad(x, y, z, color, u, v, lm);
		}

		TextureAtlasSprite sprite = src.rubidium$getSprite();
		if (sprite != null) {
			renderData.addSprite(sprite);
		}
	}

	private LightMode getLightingMode(IBlockState state, IBakedModel model, IBlockAccess world, BlockPos pos) {
		Block block = state.getBlock();
		return this.useAmbientOcclusion && model.isAmbientOcclusion() && block.getLightValue() == 0 ? LightMode.SMOOTH : LightMode.FLAT;
	}
}
