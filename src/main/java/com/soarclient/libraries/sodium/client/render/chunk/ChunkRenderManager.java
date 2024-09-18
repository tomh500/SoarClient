package com.soarclient.libraries.sodium.client.render.chunk;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.concurrent.CompletableFuture;

import com.soarclient.libraries.sodium.SodiumClientMod;
import com.soarclient.libraries.sodium.client.gl.compat.FogHelper;
import com.soarclient.libraries.sodium.client.gl.device.CommandList;
import com.soarclient.libraries.sodium.client.gl.device.RenderDevice;
import com.soarclient.libraries.sodium.client.render.SodiumWorldRenderer;
import com.soarclient.libraries.sodium.client.render.chunk.backends.multidraw.MultidrawChunkRenderBackend;
import com.soarclient.libraries.sodium.client.render.chunk.compile.ChunkBuildResult;
import com.soarclient.libraries.sodium.client.render.chunk.compile.ChunkBuilder;
import com.soarclient.libraries.sodium.client.render.chunk.cull.ChunkCuller;
import com.soarclient.libraries.sodium.client.render.chunk.cull.ChunkFaceFlags;
import com.soarclient.libraries.sodium.client.render.chunk.cull.graph.ChunkGraphCuller;
import com.soarclient.libraries.sodium.client.render.chunk.data.ChunkRenderBounds;
import com.soarclient.libraries.sodium.client.render.chunk.data.ChunkRenderData;
import com.soarclient.libraries.sodium.client.render.chunk.lists.ChunkRenderList;
import com.soarclient.libraries.sodium.client.render.chunk.lists.ChunkRenderListIterator;
import com.soarclient.libraries.sodium.client.render.chunk.passes.BlockRenderPass;
import com.soarclient.libraries.sodium.client.render.chunk.passes.BlockRenderPassManager;
import com.soarclient.libraries.sodium.client.util.math.FrustumExtended;
import com.soarclient.libraries.sodium.client.util.math.MathChunkPos;
import com.soarclient.libraries.sodium.client.world.ChunkStatusListener;
import com.soarclient.libraries.sodium.common.util.DirectionUtil;
import com.soarclient.libraries.sodium.common.util.IdTable;
import com.soarclient.libraries.sodium.common.util.collections.FutureDequeDrain;

import it.unimi.dsi.fastutil.ints.IntIterator;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongCollection;
import it.unimi.dsi.fastutil.longs.LongIterator;
import it.unimi.dsi.fastutil.objects.ObjectArrayFIFOQueue;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;

public class ChunkRenderManager<T extends ChunkGraphicsState> implements ChunkStatusListener {
	private static final double NEARBY_CHUNK_DISTANCE = Math.pow(48.0, 2.0);
	private static final float FOG_PLANE_MIN_DISTANCE = (float)Math.pow(8.0, 2.0);
	private static final float FOG_PLANE_OFFSET = 12.0F;
	private final ChunkBuilder<T> builder;
	private final ChunkRenderBackend<T> backend;
	private final Thread renderThread = Thread.currentThread();
	private final Long2ObjectOpenHashMap<ChunkRenderColumn<T>> columns = new Long2ObjectOpenHashMap<>();
	private final IdTable<ChunkRenderContainer<T>> renders = new IdTable<>(16384);
	private final ObjectArrayFIFOQueue<ChunkRenderContainer<T>> importantRebuildQueue = new ObjectArrayFIFOQueue<>();
	private final ObjectArrayFIFOQueue<ChunkRenderContainer<T>> rebuildQueue = new ObjectArrayFIFOQueue<>();
	private final ObjectArrayFIFOQueue<ChunkRenderContainer<T>> sortQueue = new ObjectArrayFIFOQueue<>();
	private final ObjectArrayFIFOQueue<ChunkRenderContainer<T>> unloadQueue = new ObjectArrayFIFOQueue<>();
	private final ChunkRenderList<T>[] chunkRenderLists = new ChunkRenderList[BlockRenderPass.COUNT];
	private final ObjectList<ChunkRenderContainer<T>> tickableChunks = new ObjectArrayList<>();
	private final ObjectList<TileEntity> visibleBlockEntities = new ObjectArrayList<>();
	private final SodiumWorldRenderer renderer;
	private final WorldClient world;
	private final ChunkCuller culler;
	private final boolean useBlockFaceCulling;
	private float cameraX;
	private float cameraY;
	private float cameraZ;
	private boolean dirty;
	private final boolean translucencySorting;
	private int visibleChunkCount;
	private boolean useFogCulling;
	private double fogRenderCutoff;
	private final int translucencyBlockRenderDistance;
	private boolean alwaysDeferChunkUpdates;
	private float lastCameraTranslucentX;
	private float lastCameraTranslucentY;
	private float lastCameraTranslucentZ;
	private boolean hasCameraMovedTranslucent;

	public ChunkRenderManager(
		SodiumWorldRenderer renderer, ChunkRenderBackend<T> backend, BlockRenderPassManager renderPassManager, WorldClient world, int renderDistance
	) {
		this.backend = backend;
		this.renderer = renderer;
		this.world = world;
		this.builder = new ChunkBuilder<>(backend.getVertexType(), this.backend);
		this.builder.init(world, renderPassManager);
		this.dirty = true;

		for (int i = 0; i < this.chunkRenderLists.length; i++) {
			this.chunkRenderLists[i] = new ChunkRenderList<>();
		}

		this.culler = new ChunkGraphCuller(world, renderDistance);
		this.translucencySorting = SodiumClientMod.options().advanced.translucencySorting;
		this.translucencyBlockRenderDistance = Math.min(9216, (renderDistance << 4) * (renderDistance << 4));
		this.useBlockFaceCulling = SodiumClientMod.options().advanced.useBlockFaceCulling;
	}

	public void update(float ticks, FrustumExtended frustum, int frame, boolean spectator) {
		this.reset();
		this.unloadPending();
		this.setup(ticks);
		this.iterateChunks(frustum, frame, spectator);
		this.dirty = false;
	}

	private void setup(float ticks) {
		Vec3 pos = this.builder.getCameraPosition();
		this.cameraX = (float)pos.xCoord;
		this.cameraY = (float)pos.yCoord;
		this.cameraZ = (float)pos.zCoord;
		this.useFogCulling = false;
		this.alwaysDeferChunkUpdates = SodiumClientMod.options().performance.alwaysDeferChunkUpdates;
		if (SodiumClientMod.options().advanced.useFogOcclusion) {
			float dist = FogHelper.getFogCutoff() + 12.0F;
			if (dist != 0.0F) {
				this.useFogCulling = true;
				this.fogRenderCutoff = (double)Math.max(FOG_PLANE_MIN_DISTANCE, dist * dist);
			}
		}
	}

	private void iterateChunks(FrustumExtended frustum, int frame, boolean spectator) {
		if (this.translucencySorting) {
			this.checkTranslucencyCameraMoved();
			if (this.hasCameraMovedTranslucent) {
				for (Object o : this.renders.getElements()) {
					if (o != null) {
						ChunkRenderContainer<T> render = (ChunkRenderContainer<T>)o;
						if (!render.getData().isEmpty()
							&& !render.needsRebuild()
							&& render.canRebuild()
							&& render.shouldRebuildForTranslucents()
							&& render.getSquaredDistance((double)this.cameraX, (double)this.cameraY, (double)this.cameraZ) < (double)this.translucencyBlockRenderDistance) {
							render.scheduleSort(false);
						}
					}
				}
			}
		}

		IntList list = this.culler.computeVisible(new Vec3((double)this.cameraX, (double)this.cameraY, (double)this.cameraZ), frustum, frame, spectator);
		IntIterator it = list.iterator();

		while (it.hasNext()) {
			ChunkRenderContainer<T> render = this.renders.get(it.nextInt());
			this.addChunk(render);
		}
	}

	private void checkTranslucencyCameraMoved() {
		float dx = this.cameraX - this.lastCameraTranslucentX;
		float dy = this.cameraY - this.lastCameraTranslucentY;
		float dz = this.cameraZ - this.lastCameraTranslucentZ;
		if ((double)(dx * dx + dy * dy + dz * dz) > 1.0) {
			this.lastCameraTranslucentX = this.cameraX;
			this.lastCameraTranslucentY = this.cameraY;
			this.lastCameraTranslucentZ = this.cameraZ;
			this.hasCameraMovedTranslucent = true;
		} else {
			this.hasCameraMovedTranslucent = false;
		}
	}

	private void addChunk(ChunkRenderContainer<T> render) {
		if (render.needsRebuild() && render.canRebuild()) {
			if (!this.alwaysDeferChunkUpdates && render.needsImportantRebuild()) {
				this.importantRebuildQueue.enqueue(render);
			} else {
				this.rebuildQueue.enqueue(render);
			}
		} else if (render.canRebuild() && !render.getData().isEmpty() && render.needsSort()) {
			this.sortQueue.enqueue(render);
		}

		if (!this.useFogCulling || !(render.getSquaredDistanceXZ((double)this.cameraX, (double)this.cameraZ) >= this.fogRenderCutoff)) {
			if (!render.isEmpty()) {
				this.addChunkToRenderLists(render);
				this.addEntitiesToRenderLists(render);
			}
		}
	}

	private void addChunkToRenderLists(ChunkRenderContainer<T> render) {
		int visibleFaces = this.computeVisibleFaces(render) & render.getFacesWithData();
		if (visibleFaces != 0) {
			boolean added = false;
			T[] states = render.getGraphicsStates();

			for (int i = 0; i < states.length; i++) {
				T state = states[i];
				if (state != null) {
					ChunkRenderList<T> list = this.chunkRenderLists[i];
					list.add(state, this.translucencySorting && BlockRenderPass.VALUES[i].isTranslucent() ? ChunkFaceFlags.ALL & render.getFacesWithData() : visibleFaces);
					added = true;
				}
			}

			if (added) {
				if (render.isTickable()) {
					this.tickableChunks.add(render);
				}

				this.visibleChunkCount++;
			}
		}
	}

	private int computeVisibleFaces(ChunkRenderContainer<T> render) {
		if (!this.useBlockFaceCulling) {
			return ChunkFaceFlags.ALL;
		} else {
			ChunkRenderBounds bounds = render.getBounds();
			int visibleFaces = ChunkFaceFlags.UNASSIGNED;
			if (this.cameraY > bounds.y1 - 3.0F) {
				visibleFaces |= ChunkFaceFlags.UP;
			}

			if (this.cameraY < bounds.y2 + 3.0F) {
				visibleFaces |= ChunkFaceFlags.DOWN;
			}

			if (this.cameraX > bounds.x1 - 3.0F) {
				visibleFaces |= ChunkFaceFlags.EAST;
			}

			if (this.cameraX < bounds.x2 + 3.0F) {
				visibleFaces |= ChunkFaceFlags.WEST;
			}

			if (this.cameraZ > bounds.z1 - 3.0F) {
				visibleFaces |= ChunkFaceFlags.SOUTH;
			}

			if (this.cameraZ < bounds.z2 + 3.0F) {
				visibleFaces |= ChunkFaceFlags.NORTH;
			}

			return visibleFaces;
		}
	}

	private void addEntitiesToRenderLists(ChunkRenderContainer<T> render) {
		Collection<TileEntity> blockEntities = render.getData().getBlockEntities();
		if (!blockEntities.isEmpty()) {
			this.visibleBlockEntities.addAll(blockEntities);
		}
	}

	public ChunkRenderContainer<T> getRender(int x, int y, int z) {
		ChunkRenderColumn<T> column = this.columns.get(ChunkCoordIntPair.chunkXZ2Int(x, z));
		return column == null ? null : column.getRender(y);
	}

	private void reset() {
		this.rebuildQueue.clear();
		this.importantRebuildQueue.clear();
		this.sortQueue.clear();
		this.visibleBlockEntities.clear();

		for (ChunkRenderList<T> list : this.chunkRenderLists) {
			list.reset();
		}

		this.tickableChunks.clear();
		this.visibleChunkCount = 0;
	}

	private void unloadPending() {
		while (!this.unloadQueue.isEmpty()) {
			this.unloadQueue.dequeue().delete();
		}
	}

	public Collection<TileEntity> getVisibleBlockEntities() {
		return this.visibleBlockEntities;
	}

	@Override
	public void onChunkAdded(int x, int z) {
		this.loadChunk(x, z);
	}

	@Override
	public void onChunkRemoved(int x, int z) {
		this.unloadChunk(x, z);
	}

	private void loadChunk(int x, int z) {
		ChunkRenderColumn<T> column = new ChunkRenderColumn<>(x, z);
		ChunkRenderColumn<T> prev;
		if ((prev = this.columns.put(ChunkCoordIntPair.chunkXZ2Int(x, z), column)) != null) {
			this.unloadSections(prev);
		}

		this.connectNeighborColumns(column);
		this.loadSections(column);
		this.dirty = true;
	}

	private void unloadChunk(int x, int z) {
		ChunkRenderColumn<T> column = this.columns.remove(ChunkCoordIntPair.chunkXZ2Int(x, z));
		if (column != null) {
			this.disconnectNeighborColumns(column);
			this.unloadSections(column);
			this.dirty = true;
		}
	}

	private void loadSections(ChunkRenderColumn<T> column) {
		int x = column.getX();
		int z = column.getZ();

		for (int y = 0; y < 16; y++) {
			ChunkRenderContainer<T> render = this.createChunkRender(column, x, y, z);
			column.setRender(y, render);
			this.culler.onSectionLoaded(x, y, z, render.getId());
		}
	}

	private void unloadSections(ChunkRenderColumn<T> column) {
		int x = column.getX();
		int z = column.getZ();

		for (int y = 0; y < 16; y++) {
			ChunkRenderContainer<T> render = column.getRender(y);
			if (render != null) {
				this.unloadQueue.enqueue(render);
				this.renders.remove(render.getId());
			}

			this.culler.onSectionUnloaded(x, y, z);
		}
	}

	private void connectNeighborColumns(ChunkRenderColumn<T> column) {
		for (EnumFacing dir : DirectionUtil.ALL_DIRECTIONS) {
			ChunkRenderColumn<T> adj = this.getAdjacentColumn(column, dir);
			if (adj != null) {
				adj.setAdjacentColumn(dir.getOpposite(), column);
			}

			column.setAdjacentColumn(dir, adj);
		}
	}

	private void disconnectNeighborColumns(ChunkRenderColumn<T> column) {
		for (EnumFacing dir : DirectionUtil.ALL_DIRECTIONS) {
			ChunkRenderColumn<T> adj = column.getAdjacentColumn(dir);
			if (adj != null) {
				adj.setAdjacentColumn(dir.getOpposite(), null);
			}

			column.setAdjacentColumn(dir, null);
		}
	}

	private ChunkRenderColumn<T> getAdjacentColumn(ChunkRenderColumn<T> column, EnumFacing dir) {
		return this.getColumn(column.getX() + dir.getFrontOffsetX(), column.getZ() + dir.getFrontOffsetZ());
	}

	private ChunkRenderColumn<T> getColumn(int x, int z) {
		return this.columns.get(ChunkCoordIntPair.chunkXZ2Int(x, z));
	}

	private ChunkRenderContainer<T> createChunkRender(ChunkRenderColumn<T> column, int x, int y, int z) {
		ChunkRenderContainer<T> render = new ChunkRenderContainer<>(this.backend, this.renderer, x, y, z, column);
		ExtendedBlockStorage array = this.world.getChunkFromChunkCoords(x, z).getBlockStorageArray()[y];
		if (array != null && !array.isEmpty()) {
			render.scheduleRebuild(false);
		} else {
			render.setData(ChunkRenderData.EMPTY);
		}

		render.setId(this.renders.add(render));
		return render;
	}

	public void renderLayer(BlockRenderPass pass, double x, double y, double z) {
		ChunkRenderList<T> chunkRenderList = this.chunkRenderLists[pass.ordinal()];
		ChunkRenderListIterator<T> iterator = chunkRenderList.iterator(pass.isTranslucent());
		RenderDevice device = RenderDevice.INSTANCE;
		CommandList commandList = device.createCommandList();
		this.backend.begin();
		if (this.backend instanceof MultidrawChunkRenderBackend) {
			((MultidrawChunkRenderBackend)this.backend).setReverseRegions(pass.isTranslucent());
		}

		this.backend.render(commandList, iterator, new ChunkCameraContext(x, y, z));
		this.backend.end();
		commandList.flush();
	}

	public void tickVisibleRenders() {
		for (ChunkRenderContainer<T> render : this.tickableChunks) {
			render.tick();
		}
	}

	public boolean isChunkVisible(int x, int y, int z) {
		return this.culler.isSectionVisible(x, y, z);
	}

	public void updateChunks() {
		this.builder.cleanupSectionCache();
		Deque<CompletableFuture<ChunkBuildResult<T>>> futures = new ArrayDeque();
		int budget = this.builder.getSchedulingBudget();
		int submitted = 0;

		while (!this.importantRebuildQueue.isEmpty()) {
			ChunkRenderContainer<T> render = this.importantRebuildQueue.dequeue();
			if (render != null) {
				if (!this.alwaysDeferChunkUpdates && this.isChunkPrioritized(render)) {
					futures.add(this.builder.scheduleRebuildTaskAsync(render));
				} else {
					this.builder.deferRebuild(render);
				}

				this.dirty = true;
				if (this.alwaysDeferChunkUpdates && ++submitted >= budget) {
					break;
				}
			}
		}

		while (submitted < budget && !this.rebuildQueue.isEmpty()) {
			ChunkRenderContainer<T> render = this.rebuildQueue.dequeue();
			this.builder.deferRebuild(render);
			submitted++;
		}

		for (boolean sortedAnything = false; (!sortedAnything || submitted < budget) && !this.sortQueue.isEmpty(); submitted++) {
			ChunkRenderContainer<T> render = this.sortQueue.dequeue();
			this.builder.deferSort(render);
			sortedAnything = true;
		}

		this.dirty |= submitted > 0;
		this.dirty = this.dirty | this.builder.performPendingUploads();
		this.builder.handleFailures();
		if (!futures.isEmpty()) {
			this.dirty = true;
			this.backend.upload(RenderDevice.INSTANCE.createCommandList(), this.builder.filterChunkBuilds(new FutureDequeDrain<>(futures)));
		}
	}

	public void markDirty() {
		this.dirty = true;
	}

	public void restoreChunks(LongCollection chunks) {
		LongIterator it = chunks.iterator();

		while (it.hasNext()) {
			long pos = it.nextLong();
			this.loadChunk(MathChunkPos.getX(pos), MathChunkPos.getZ(pos));
		}
	}

	public boolean isBuildComplete() {
		return this.builder.isBuildQueueEmpty();
	}

	public void setCameraPosition(double x, double y, double z) {
		this.builder.setCameraPosition(x, y, z);
	}

	public void destroy() {
		this.reset();

		for (ChunkRenderColumn<T> column : this.columns.values()) {
			this.unloadSections(column);
		}

		this.columns.clear();
		this.builder.stopWorkers();
	}

	public int getTotalSections() {
		return this.columns.size() * 16;
	}

	public int getRebuildQueueSize() {
		return this.rebuildQueue.size();
	}

	public int getImportantRebuildQueueSize() {
		return this.importantRebuildQueue.size();
	}

	private void scheduleRebuildOffThread(int x, int y, int z, boolean important) {
		Minecraft.getMinecraft().addScheduledTask(() -> this.scheduleRebuild(x, y, z, important));
	}

	public void scheduleRebuild(int x, int y, int z, boolean important) {
		if (Thread.currentThread() != this.renderThread) {
			this.scheduleRebuildOffThread(x, y, z, important);
		} else {
			ChunkRenderContainer<T> render = this.getRender(x, y, z);
			if (render != null) {
				important = important || this.isChunkPrioritized(render);
				if (render.scheduleRebuild(important) && render.canRebuild()) {
					(render.needsImportantRebuild() ? this.importantRebuildQueue : this.rebuildQueue).enqueue(render);
				}

				this.dirty = true;
			}

			this.builder.onChunkDataChanged(x, y, z);
		}
	}

	public boolean isChunkPrioritized(ChunkRenderContainer<T> render) {
		return render != null && render.getSquaredDistance((double)this.cameraX, (double)this.cameraY, (double)this.cameraZ) <= NEARBY_CHUNK_DISTANCE;
	}

	public void onChunkRenderUpdates(int x, int y, int z, ChunkRenderData data) {
		this.culler.onSectionStateChanged(x, y, z, data.getOcclusionData());
	}

	public boolean isDirty() {
		return this.dirty;
	}

	public int getVisibleChunkCount() {
		return this.visibleChunkCount;
	}
}
