package com.soarclient.libraries.sodium.client.render;

import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;

import java.util.Map;
import java.util.Set;

import com.soarclient.libraries.sodium.SodiumClientMod;
import com.soarclient.libraries.sodium.client.gl.compat.FogHelper;
import com.soarclient.libraries.sodium.client.gl.device.RenderDevice;
import com.soarclient.libraries.sodium.client.gui.SodiumGameOptions;
import com.soarclient.libraries.sodium.client.model.vertex.type.ChunkVertexType;
import com.soarclient.libraries.sodium.client.render.chunk.ChunkRenderBackend;
import com.soarclient.libraries.sodium.client.render.chunk.ChunkRenderManager;
import com.soarclient.libraries.sodium.client.render.chunk.backends.multidraw.MultidrawChunkRenderBackend;
import com.soarclient.libraries.sodium.client.render.chunk.backends.oneshot.ChunkRenderBackendOneshot;
import com.soarclient.libraries.sodium.client.render.chunk.data.ChunkRenderData;
import com.soarclient.libraries.sodium.client.render.chunk.format.DefaultModelVertexFormats;
import com.soarclient.libraries.sodium.client.render.chunk.passes.BlockRenderPass;
import com.soarclient.libraries.sodium.client.render.chunk.passes.BlockRenderPassManager;
import com.soarclient.libraries.sodium.client.render.pipeline.context.ChunkRenderCacheShared;
import com.soarclient.libraries.sodium.client.util.math.FrustumExtended;
import com.soarclient.libraries.sodium.client.world.ChunkStatusListener;
import com.soarclient.libraries.sodium.client.world.ChunkStatusListenerManager;
import com.soarclient.libraries.sodium.common.util.ListUtil;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.DestroyBlockProgress;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.entity.Entity;
import net.minecraft.profiler.Profiler;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkCoordIntPair;

public class SodiumWorldRenderer implements ChunkStatusListener {
	private static SodiumWorldRenderer instance;
	private final Minecraft client;
	private WorldClient world;
	private int renderDistance;
	private double lastCameraX;
	private double lastCameraY;
	private double lastCameraZ;
	private double lastCameraPitch;
	private double lastCameraYaw;
	private float lastFogDistance;
	private boolean useEntityCulling;
	private final LongSet loadedChunkPositions = new LongOpenHashSet();
	private final Set<TileEntity> globalBlockEntities = new ObjectOpenHashSet<>();
	private Frustum frustum;
	private ChunkRenderManager<?> chunkRenderManager;
	private BlockRenderPassManager renderPassManager;
	private ChunkRenderBackend<?> chunkRenderBackend;

	public static SodiumWorldRenderer create() {
		if (instance == null) {
			instance = new SodiumWorldRenderer(Minecraft.getMinecraft());
		}

		return instance;
	}

	public static SodiumWorldRenderer getInstance() {
		if (instance == null) {
			throw new IllegalStateException("Renderer not initialized");
		} else {
			return instance;
		}
	}

	public static SodiumWorldRenderer getInstanceNullable() {
		return instance;
	}

	private SodiumWorldRenderer(Minecraft client) {
		this.client = client;
	}

	public void setWorld(WorldClient world) {
		if (this.world != world) {
			if (this.world != null) {
				this.unloadWorld();
			}

			if (world != null) {
				this.loadWorld(world);
			}
		}
	}

	private void loadWorld(WorldClient world) {
		this.world = world;
		ChunkRenderCacheShared.createRenderContext(this.world);
		this.initRenderer();
		((ChunkStatusListenerManager)world.getChunkProvider()).setListener(this);
	}

	private void unloadWorld() {
		ChunkRenderCacheShared.destroyRenderContext(this.world);
		if (this.chunkRenderManager != null) {
			this.chunkRenderManager.destroy();
			this.chunkRenderManager = null;
		}

		if (this.chunkRenderBackend != null) {
			this.chunkRenderBackend.delete();
			this.chunkRenderBackend = null;
		}

		this.loadedChunkPositions.clear();
		this.globalBlockEntities.clear();
		this.world = null;
	}

	public int getVisibleChunkCount() {
		return this.chunkRenderManager.getVisibleChunkCount();
	}

	public void scheduleTerrainUpdate() {
		if (this.chunkRenderManager != null) {
			this.chunkRenderManager.markDirty();
		}
	}

	public boolean isTerrainRenderComplete() {
		return this.chunkRenderManager.isBuildComplete();
	}

	public void updateChunks(Frustum frustum, float ticks, boolean hasForcedFrustum, int frame, boolean spectator) {
		this.frustum = frustum;
		this.useEntityCulling = SodiumClientMod.options().advanced.useEntityCulling;
		if (this.client.gameSettings.renderDistanceChunks != this.renderDistance) {
			this.reload();
		}

		Profiler profiler = this.client.mcProfiler;
		profiler.startSection("camera_setup");
		Entity viewEntity = this.client.getRenderViewEntity();
		if (viewEntity == null) {
			throw new IllegalStateException("Client instance has no active render entity");
		} else {
			double x = viewEntity.lastTickPosX + (viewEntity.posX - viewEntity.lastTickPosX) * (double)ticks;
			double y = viewEntity.lastTickPosY + (viewEntity.posY - viewEntity.lastTickPosY) * (double)ticks + (double)viewEntity.getEyeHeight();
			double z = viewEntity.lastTickPosZ + (viewEntity.posZ - viewEntity.lastTickPosZ) * (double)ticks;
			this.chunkRenderManager.setCameraPosition(x, y, z);
			float fogDistance = FogHelper.getFogCutoff();
			boolean dirty = x != this.lastCameraX
				|| y != this.lastCameraY
				|| z != this.lastCameraZ
				|| (double)viewEntity.rotationPitch != this.lastCameraPitch | (double)viewEntity.rotationYaw != this.lastCameraYaw;
			if (dirty) {
				this.chunkRenderManager.markDirty();
			}

			this.lastCameraX = x;
			this.lastCameraY = y;
			this.lastCameraZ = z;
			this.lastCameraPitch = (double)viewEntity.rotationPitch;
			this.lastCameraYaw = (double)viewEntity.rotationYaw;
			this.lastFogDistance = fogDistance;
			profiler.endStartSection("chunk_update");
			this.chunkRenderManager.updateChunks();
			if (!hasForcedFrustum && this.chunkRenderManager.isDirty()) {
				profiler.endStartSection("chunk_graph_rebuild");
				this.chunkRenderManager.update(ticks, (FrustumExtended)frustum, frame, spectator);
			}

			profiler.endStartSection("visible_chunk_tick");
			this.chunkRenderManager.tickVisibleRenders();
			profiler.endSection();
		}
	}

	public void drawChunkLayer(EnumWorldBlockLayer renderLayer, double x, double y, double z) {
		BlockRenderPass pass = this.renderPassManager.getRenderPassForLayer(renderLayer);
		this.chunkRenderManager.renderLayer(pass, x, y, z);
		GlStateManager.resetColor();
	}

	public void reload() {
		if (this.world != null) {
			this.initRenderer();
		}
	}

	private void initRenderer() {
		if (this.chunkRenderManager != null) {
			this.chunkRenderManager.destroy();
			this.chunkRenderManager = null;
		}

		if (this.chunkRenderBackend != null) {
			this.chunkRenderBackend.delete();
			this.chunkRenderBackend = null;
		}

		this.globalBlockEntities.clear();
		RenderDevice device = RenderDevice.INSTANCE;
		this.renderDistance = this.client.gameSettings.renderDistanceChunks;
		SodiumGameOptions opts = SodiumClientMod.options();
		this.renderPassManager = BlockRenderPassManager.createDefaultMappings();
		ChunkVertexType vertexFormat;
		if (opts.advanced.useCompactVertexFormat) {
			vertexFormat = DefaultModelVertexFormats.MODEL_VERTEX_HFP;
		} else {
			vertexFormat = DefaultModelVertexFormats.MODEL_VERTEX_SFP;
		}

		this.chunkRenderBackend = createChunkRenderBackend(device, opts, vertexFormat);
		this.chunkRenderBackend.createShaders(device);
		this.chunkRenderManager = new ChunkRenderManager<>(this, this.chunkRenderBackend, this.renderPassManager, this.world, this.renderDistance);
		this.chunkRenderManager.restoreChunks(this.loadedChunkPositions);
	}

	private static ChunkRenderBackend<?> createChunkRenderBackend(RenderDevice device, SodiumGameOptions options, ChunkVertexType vertexFormat) {
		boolean disableBlacklist = SodiumClientMod.options().advanced.ignoreDriverBlacklist;
		return (ChunkRenderBackend<?>)(options.advanced.useChunkMultidraw && MultidrawChunkRenderBackend.isSupported(disableBlacklist)
			? new MultidrawChunkRenderBackend(device, vertexFormat)
			: new ChunkRenderBackendOneshot(vertexFormat));
	}

	private boolean checkBEVisibility(TileEntity entity) {
		BlockPos pos = entity.getPos();
		IBlockState state = this.world.getBlockState(pos);
		AxisAlignedBB box = entity.getBlockType().getCollisionBoundingBox(this.world, entity.getPos(), state);
		return box != null && this.frustum.isBoundingBoxInFrustum(box);
	}

	private void renderTE(TileEntity tileEntity, int pass, float partialTicks, int damageProgress) {
		if (this.checkBEVisibility(tileEntity)) {
			try {
				TileEntityRendererDispatcher.instance.renderTileEntity(tileEntity, partialTicks, damageProgress);
			} catch (RuntimeException var6) {
				if (!tileEntity.isInvalid()) {
					throw var6;
				}

				SodiumClientMod.logger().error("Suppressing crash from invalid tile entity", var6);
			}
		}
	}

	private void preRenderDamagedBlocks() {
		GlStateManager.tryBlendFuncSeparate(768, 774, 1, 0);
		GlStateManager.enableBlend();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 0.5F);
		GlStateManager.doPolygonOffset(-1.0F, -10.0F);
		GlStateManager.enablePolygonOffset();
		GlStateManager.alphaFunc(516, 0.1F);
		GlStateManager.enableAlpha();
		GlStateManager.pushMatrix();
	}

	private void postRenderDamagedBlocks() {
		GlStateManager.disableAlpha();
		GlStateManager.doPolygonOffset(0.0F, 0.0F);
		GlStateManager.disablePolygonOffset();
		GlStateManager.enableAlpha();
		GlStateManager.depthMask(true);
		GlStateManager.popMatrix();
	}

	public void renderTileEntities(float partialTicks, Map<Integer, DestroyBlockProgress> damagedBlocks) {
		int pass = 0;

		for (TileEntity tileEntity : this.chunkRenderManager.getVisibleBlockEntities()) {
			this.renderTE(tileEntity, pass, partialTicks, -1);
		}

		for (TileEntity tileEntity : this.globalBlockEntities) {
			this.renderTE(tileEntity, pass, partialTicks, -1);
		}

		this.preRenderDamagedBlocks();

		for (DestroyBlockProgress destroyProgress : damagedBlocks.values()) {
			BlockPos pos = destroyProgress.getPosition();
			if (this.world.getBlockState(pos).getBlock().hasTileEntity()) {
				TileEntity tileEntity = this.world.getTileEntity(pos);
				if (tileEntity instanceof TileEntityChest) {
					TileEntityChest chest = (TileEntityChest)tileEntity;
					if (chest.adjacentChestXNeg != null) {
						pos = pos.offset(EnumFacing.WEST);
						tileEntity = this.world.getTileEntity(pos);
					} else if (chest.adjacentChestZNeg != null) {
						pos = pos.offset(EnumFacing.NORTH);
						tileEntity = this.world.getTileEntity(pos);
					}
				}

				if (tileEntity != null) {
					this.renderTE(tileEntity, pass, partialTicks, destroyProgress.getPartialBlockDamage());
				}
			}
		}

		this.postRenderDamagedBlocks();
	}

	@Override
	public void onChunkAdded(int x, int z) {
		this.loadedChunkPositions.add(ChunkCoordIntPair.chunkXZ2Int(x, z));
		this.chunkRenderManager.onChunkAdded(x, z);
	}

	@Override
	public void onChunkRemoved(int x, int z) {
		this.loadedChunkPositions.remove(ChunkCoordIntPair.chunkXZ2Int(x, z));
		this.chunkRenderManager.onChunkRemoved(x, z);
	}

	public void onChunkRenderUpdated(int x, int y, int z, ChunkRenderData meshBefore, ChunkRenderData meshAfter) {
		ListUtil.updateList(this.globalBlockEntities, meshBefore.getGlobalBlockEntities(), meshAfter.getGlobalBlockEntities());
		this.chunkRenderManager.onChunkRenderUpdates(x, y, z, meshAfter);
	}

	private static boolean isInfiniteExtentsBox(AxisAlignedBB box) {
		return Double.isInfinite(box.minX)
			|| Double.isInfinite(box.minY)
			|| Double.isInfinite(box.minZ)
			|| Double.isInfinite(box.maxX)
			|| Double.isInfinite(box.maxY)
			|| Double.isInfinite(box.maxZ);
	}

	public boolean isEntityVisible(Entity entity) {
		if (!this.useEntityCulling) {
			return true;
		} else {
			AxisAlignedBB box = entity.getEntityBoundingBox();
			if (box.maxY < 0.5 || box.minY > 255.5) {
				return true;
			} else if (isInfiniteExtentsBox(box)) {
				return true;
			} else if (!this.isGlowing(entity) && !entity.getAlwaysRenderNameTagForRender()) {
				int minX = MathHelper.floor_double(box.minX - 0.5) >> 4;
				int minY = MathHelper.floor_double(box.minY - 0.5) >> 4;
				int minZ = MathHelper.floor_double(box.minZ - 0.5) >> 4;
				int maxX = MathHelper.floor_double(box.maxX + 0.5) >> 4;
				int maxY = MathHelper.floor_double(box.maxY + 0.5) >> 4;
				int maxZ = MathHelper.floor_double(box.maxZ + 0.5) >> 4;

				for (int x = minX; x <= maxX; x++) {
					for (int z = minZ; z <= maxZ; z++) {
						for (int y = minY; y <= maxY; y++) {
							if (this.chunkRenderManager.isChunkVisible(x, y, z)) {
								return true;
							}
						}
					}
				}

				return false;
			} else {
				return true;
			}
		}
	}

	private boolean isGlowing(Entity entity) {
		return Minecraft.getMinecraft().theWorld.isRemote && (entity.getDataWatcher().getWatchableObjectByte(0) & 64) != 0;
	}

	public int getTotalSections() {
		return this.chunkRenderManager.getTotalSections();
	}

	public int getRebuildQueueSize() {
		return this.chunkRenderManager.getRebuildQueueSize();
	}

	public int getImportantRebuildQueueSize() {
		return this.chunkRenderManager.getImportantRebuildQueueSize();
	}

	public void scheduleRebuildForBlockArea(int minX, int minY, int minZ, int maxX, int maxY, int maxZ, boolean important) {
		this.scheduleRebuildForChunks(minX >> 4, minY >> 4, minZ >> 4, maxX >> 4, maxY >> 4, maxZ >> 4, important);
	}

	public void scheduleRebuildForChunks(int minX, int minY, int minZ, int maxX, int maxY, int maxZ, boolean important) {
		for (int chunkX = minX; chunkX <= maxX; chunkX++) {
			for (int chunkY = minY; chunkY <= maxY; chunkY++) {
				for (int chunkZ = minZ; chunkZ <= maxZ; chunkZ++) {
					this.scheduleRebuildForChunk(chunkX, chunkY, chunkZ, important);
				}
			}
		}
	}

	public void scheduleRebuildForChunk(int x, int y, int z, boolean important) {
		this.chunkRenderManager.scheduleRebuild(x, y, z, important);
	}

	public ChunkRenderBackend<?> getChunkRenderer() {
		return this.chunkRenderBackend;
	}

	public Frustum getFrustum() {
		return this.frustum;
	}
}
