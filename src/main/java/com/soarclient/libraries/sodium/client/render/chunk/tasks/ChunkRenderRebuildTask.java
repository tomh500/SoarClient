package com.soarclient.libraries.sodium.client.render.chunk.tasks;

import com.soarclient.libraries.sodium.SodiumClientMod;
import com.soarclient.libraries.sodium.client.render.chunk.ChunkGraphicsState;
import com.soarclient.libraries.sodium.client.render.chunk.ChunkRenderContainer;
import com.soarclient.libraries.sodium.client.render.chunk.compile.ChunkBuildBuffers;
import com.soarclient.libraries.sodium.client.render.chunk.compile.ChunkBuildResult;
import com.soarclient.libraries.sodium.client.render.chunk.data.ChunkMeshData;
import com.soarclient.libraries.sodium.client.render.chunk.data.ChunkRenderData.Builder;
import com.soarclient.libraries.sodium.client.render.chunk.passes.BlockRenderPass;
import com.soarclient.libraries.sodium.client.render.pipeline.context.ChunkRenderCacheLocal;
import com.soarclient.libraries.sodium.client.util.task.CancellationSource;
import com.soarclient.libraries.sodium.client.world.WorldSlice;
import com.soarclient.libraries.sodium.client.world.cloned.ChunkRenderContext;
import com.soarclient.libraries.sodium.common.util.WorldUtil;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.chunk.VisGraph;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.ReportedException;
import net.minecraft.util.Vec3;
import net.minecraft.util.BlockPos.MutableBlockPos;
import net.minecraft.world.WorldType;

public class ChunkRenderRebuildTask<T extends ChunkGraphicsState> extends ChunkRenderBuildTask<T> {
	private static final EnumWorldBlockLayer[] LAYERS = EnumWorldBlockLayer.values();
	private final ChunkRenderContainer<T> render;
	private final BlockPos offset;
	private final ChunkRenderContext context;
	private Vec3 camera;
	private final boolean translucencySorting;

	public ChunkRenderRebuildTask(ChunkRenderContainer<T> render, ChunkRenderContext context, BlockPos offset) {
		this.render = render;
		this.offset = offset;
		this.context = context;
		this.camera = new Vec3(0.0, 0.0, 0.0);
		this.translucencySorting = SodiumClientMod.options().advanced.translucencySorting;
	}

	public ChunkRenderRebuildTask<T> withCameraPosition(Vec3 camera) {
		this.camera = camera;
		return this;
	}

	@Override
	public ChunkBuildResult<T> performBuild(ChunkRenderCacheLocal cache, ChunkBuildBuffers buffers,
			CancellationSource cancellationSource) {
		Builder renderData = new Builder();
		VisGraph occluder = new VisGraph();
		com.soarclient.libraries.sodium.client.render.chunk.data.ChunkRenderBounds.Builder bounds = new com.soarclient.libraries.sodium.client.render.chunk.data.ChunkRenderBounds.Builder(

		);
		buffers.init(renderData);
		cache.init(this.context);
		WorldSlice slice = cache.getWorldSlice();
		int baseX = this.render.getOriginX();
		int baseY = this.render.getOriginY();
		int baseZ = this.render.getOriginZ();
		MutableBlockPos pos = new MutableBlockPos();
		BlockPos renderOffset = this.offset;

		try {
			for (int relY = 0; relY < 16; relY++) {
				if (cancellationSource.isCancelled()) {
					return null;
				}

				for (int relZ = 0; relZ < 16; relZ++) {
					for (int relX = 0; relX < 16; relX++) {
						IBlockState blockState = slice.getBlockStateRelative(relX + 16, relY + 16, relZ + 16);
						Block block = blockState.getBlock();
						if (block.getMaterial() != Material.air) {
							pos.set(baseX + relX, baseY + relY, baseZ + relZ);
							buffers.setRenderOffset((float) (pos.getX() - renderOffset.getX()),
									(float) (pos.getY() - renderOffset.getY()),
									(float) (pos.getZ() - renderOffset.getZ()));
							int renderType = block.getRenderType();
							if (renderType != -1) {
								if (slice.getWorldType() != WorldType.DEBUG_WORLD) {
									blockState = block.getActualState(blockState, slice, pos);
								}

								for (EnumWorldBlockLayer layer : LAYERS) {
									if (block.getBlockLayer() == layer) {
										if (renderType == 3 && WorldUtil.toFluidBlock(block) == null) {
											IBakedModel model = cache.getBlockModels().getModelForState(blockState);
											if (cache.getBlockRenderer().renderModel(cache.getLocalSlice(), blockState,
													pos, model, buffers.get(layer), true)) {
												bounds.addBlock(relX, relY, relZ);
											}
										} else if (WorldUtil.toFluidBlock(block) != null && cache.getFluidRenderer()
												.render(cache.getLocalSlice(), blockState, pos, buffers.get(layer))) {
											bounds.addBlock(relX, relY, relZ);
										}
									}
								}
							}

							if (block.hasTileEntity()) {
								TileEntity entity = slice.getTileEntity(pos);
								if (entity != null) {
									TileEntitySpecialRenderer<TileEntity> renderer = TileEntityRendererDispatcher.instance
											.getSpecialRenderer(entity);
									if (renderer != null) {
										renderData.addBlockEntity(entity, !renderer.forceTileEntityRender());
										bounds.addBlock(relX, relY, relZ);
									}
								}
							}

							if (block.isOpaqueCube()) {
								occluder.func_178606_a(pos);
							}
						}
					}
				}
			}
		} catch (ReportedException var24) {
			throw this.fillCrashInfo(var24.getCrashReport(), slice, pos);
		} catch (Throwable var25) {
			throw this.fillCrashInfo(
					CrashReport.makeCrashReport(var25, "Encountered exception while building chunk meshes"), slice,
					pos);
		}

		this.render.setRebuildForTranslucents(false);

		for (BlockRenderPass pass : BlockRenderPass.VALUES) {
			ChunkMeshData mesh = buffers.createMesh(pass, (float) this.camera.xCoord - (float) this.offset.getX(),
					(float) this.camera.yCoord - (float) this.offset.getY(),
					(float) this.camera.zCoord - (float) this.offset.getZ(), this.translucencySorting);
			if (mesh != null) {
				renderData.setMesh(pass, mesh);
				if (this.translucencySorting && pass.isTranslucent()) {
					this.render.setRebuildForTranslucents(true);
				}
			}
		}

		renderData.setOcclusionData(occluder.computeVisibility());
		renderData.setBounds(bounds.build(this.render.getChunkPos()));
		return new ChunkBuildResult<>(this.render, renderData.build());
	}

	private ReportedException fillCrashInfo(CrashReport report, WorldSlice slice, BlockPos pos) {
		CrashReportCategory crashReportSection = report.makeCategoryDepth("Block being rendered", 1);
		IBlockState state = null;

		try {
			state = slice.getBlockState(pos);
		} catch (Exception var7) {
		}

		CrashReportCategory.addBlockInfo(crashReportSection, pos, state);
		crashReportSection.addCrashSection("Chunk section", this.render);
		if (this.context != null) {
			crashReportSection.addCrashSection("Render context volume", this.context.volume());
		}

		return new ReportedException(report);
	}

	@Override
	public void releaseResources() {
		this.context.releaseResources();
	}
}
