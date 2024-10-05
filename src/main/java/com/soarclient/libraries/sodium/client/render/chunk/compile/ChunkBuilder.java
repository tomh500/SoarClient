package com.soarclient.libraries.sodium.client.render.chunk.compile;

import it.unimi.dsi.fastutil.objects.Reference2ReferenceLinkedOpenHashMap;

import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicBoolean;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ReportedException;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.soarclient.libraries.sodium.SodiumClientMod;
import com.soarclient.libraries.sodium.client.gl.device.RenderDevice;
import com.soarclient.libraries.sodium.client.model.vertex.type.ChunkVertexType;
import com.soarclient.libraries.sodium.client.render.chunk.ChunkGraphicsState;
import com.soarclient.libraries.sodium.client.render.chunk.ChunkRenderBackend;
import com.soarclient.libraries.sodium.client.render.chunk.ChunkRenderContainer;
import com.soarclient.libraries.sodium.client.render.chunk.passes.BlockRenderPassManager;
import com.soarclient.libraries.sodium.client.render.chunk.tasks.ChunkRenderBuildTask;
import com.soarclient.libraries.sodium.client.render.chunk.tasks.ChunkRenderEmptyBuildTask;
import com.soarclient.libraries.sodium.client.render.chunk.tasks.ChunkRenderRebuildTask;
import com.soarclient.libraries.sodium.client.render.chunk.tasks.ChunkRenderTranslucencySortTask;
import com.soarclient.libraries.sodium.client.render.pipeline.context.ChunkRenderCacheLocal;
import com.soarclient.libraries.sodium.client.util.task.CancellationSource;
import com.soarclient.libraries.sodium.client.world.WorldSlice;
import com.soarclient.libraries.sodium.client.world.cloned.ChunkRenderContext;
import com.soarclient.libraries.sodium.client.world.cloned.ClonedChunkSectionCache;
import com.soarclient.libraries.sodium.common.util.collections.DequeDrain;

public class ChunkBuilder<T extends ChunkGraphicsState> {
	private static final int TASK_QUEUE_LIMIT_PER_WORKER = 2;
	private static final Logger LOGGER = LogManager.getLogger("ChunkBuilder");
	private final Deque<ChunkBuilder.WrappedTask<T>> buildQueue = new ConcurrentLinkedDeque();
	private final Deque<ChunkBuildResult<T>> uploadQueue = new ConcurrentLinkedDeque();
	private final Deque<Throwable> failureQueue = new ConcurrentLinkedDeque();
	private final Object jobNotifier = new Object();
	private final AtomicBoolean running = new AtomicBoolean(false);
	private final List<Thread> threads = new ArrayList();
	private ClonedChunkSectionCache sectionCache;
	private World world;
	private Vec3 cameraPosition = new Vec3(0.0, 0.0, 0.0);
	private BlockRenderPassManager renderPassManager;
	private final int limitThreads;
	private final ChunkVertexType vertexType;
	private final ChunkRenderBackend<T> backend;

	public ChunkBuilder(ChunkVertexType vertexType, ChunkRenderBackend<T> backend) {
		this.vertexType = vertexType;
		this.backend = backend;
		this.limitThreads = getThreadCount();
	}

	public int getSchedulingBudget() {
		return Math.max(0, this.limitThreads * 2 - this.buildQueue.size());
	}

	public void startWorkers() {
		if (!this.running.getAndSet(true)) {
			if (!this.threads.isEmpty()) {
				throw new IllegalStateException("Threads are still alive while in the STOPPED state");
			} else {
				Minecraft client = Minecraft.getMinecraft();

				for (int i = 0; i < this.limitThreads; i++) {
					ChunkBuildBuffers buffers = new ChunkBuildBuffers(this.vertexType, this.renderPassManager);
					ChunkRenderCacheLocal pipeline = new ChunkRenderCacheLocal(client, this.world);
					ChunkBuilder<T>.WorkerRunnable worker = new ChunkBuilder.WorkerRunnable(buffers, pipeline);
					Thread thread = new Thread(worker, "Chunk Render Task Executor #" + i);
					thread.setPriority(Math.max(0, 3));
					thread.start();
					this.threads.add(thread);
				}

				LOGGER.info("Started {} worker threads", new Object[]{this.threads.size()});
			}
		}
	}

	public void stopWorkers() {
		if (this.running.getAndSet(false)) {
			if (this.threads.isEmpty()) {
				throw new IllegalStateException("No threads are alive but the executor is in the RUNNING state");
			} else {
				LOGGER.info("Stopping worker threads");
				synchronized (this.jobNotifier) {
					this.jobNotifier.notifyAll();
				}

				for (Thread thread : this.threads) {
					try {
						thread.join();
					} catch (InterruptedException var4) {
					}
				}

				this.threads.clear();
				this.uploadQueue.clear();
				this.failureQueue.clear();

				for (ChunkBuilder.WrappedTask<?> job : this.buildQueue) {
					job.future.cancel(true);
				}

				this.buildQueue.clear();
				this.world = null;
				this.sectionCache = null;
			}
		}
	}

	public void cleanupSectionCache() {
		this.sectionCache.cleanup();
	}

	public Iterator<ChunkBuildResult<T>> filterChunkBuilds(Iterator<ChunkBuildResult<T>> uploadIterator) {
		Reference2ReferenceLinkedOpenHashMap<ChunkRenderContainer<T>, ChunkBuildResult<T>> map = new Reference2ReferenceLinkedOpenHashMap<>();

		while (uploadIterator.hasNext()) {
			ChunkBuildResult<T> result = (ChunkBuildResult<T>)uploadIterator.next();
			ChunkRenderContainer<T> section = result.render;
			ChunkBuildResult<T> oldResult = map.get(section);
			if (oldResult == null || result.passesToUpload.length >= oldResult.passesToUpload.length) {
				map.put(section, result);
			}
		}

		return map.values().iterator();
	}

	public boolean performPendingUploads() {
		if (this.uploadQueue.isEmpty()) {
			return false;
		} else {
			this.backend.upload(RenderDevice.INSTANCE.createCommandList(), this.filterChunkBuilds(new DequeDrain<>(this.uploadQueue)));
			return true;
		}
	}

	public void handleFailures() {
		Iterator<Throwable> errorIterator = new DequeDrain<>(this.failureQueue);
		if (errorIterator.hasNext()) {
			Throwable ex = (Throwable)errorIterator.next();
			if (ex instanceof ReportedException) {
				throw (ReportedException)ex;
			} else {
				throw new RuntimeException("Chunk build failed", ex);
			}
		}
	}

	public CompletableFuture<ChunkBuildResult<T>> schedule(ChunkRenderBuildTask<T> task) {
		if (!this.running.get()) {
			throw new IllegalStateException("Executor is stopped");
		} else {
			ChunkBuilder.WrappedTask<T> job = new ChunkBuilder.WrappedTask<>(task);
			this.buildQueue.add(job);
			synchronized (this.jobNotifier) {
				this.jobNotifier.notify();
			}

			return job.future;
		}
	}

	public void setCameraPosition(double x, double y, double z) {
		this.cameraPosition = new Vec3(x, y, z);
	}

	public Vec3 getCameraPosition() {
		return this.cameraPosition;
	}

	public boolean isBuildQueueEmpty() {
		return this.buildQueue.isEmpty();
	}

	public void init(WorldClient world, BlockRenderPassManager renderPassManager) {
		if (world == null) {
			throw new NullPointerException("World is null");
		} else {
			this.stopWorkers();
			this.world = world;
			this.renderPassManager = renderPassManager;
			this.sectionCache = new ClonedChunkSectionCache(this.world);
			this.startWorkers();
		}
	}

	private static int getOptimalThreadCount() {
		return MathHelper.clamp_int(Math.max(getMaxThreadCount() / 3, getMaxThreadCount() - 6), 1, getMaxThreadCount());
	}

	private static int getThreadCount() {
		int requested = SodiumClientMod.options().performance.chunkBuilderThreads;
		return requested == 0 ? getOptimalThreadCount() : Math.min(requested, getMaxThreadCount());
	}

	private static int getMaxThreadCount() {
		return Runtime.getRuntime().availableProcessors();
	}

	private void handleCompletion(CompletableFuture<ChunkBuildResult<T>> future) {
		future.whenComplete((res, ex) -> {
			if (ex != null) {
				this.failureQueue.add(ex);
			} else if (res != null) {
				this.enqueueUpload(res);
			}
		});
	}

	public void deferRebuild(ChunkRenderContainer<T> render) {
		this.handleCompletion(this.scheduleRebuildTaskAsync(render));
	}

	public void deferSort(ChunkRenderContainer<T> render) {
		this.handleCompletion(this.scheduleSortTaskAsync(render));
	}

	private void enqueueUpload(ChunkBuildResult<T> result) {
		this.uploadQueue.add(result);
	}

	public CompletableFuture<ChunkBuildResult<T>> scheduleRebuildTaskAsync(ChunkRenderContainer<T> render) {
		return this.schedule(this.createRebuildTask(render));
	}

	public CompletableFuture<ChunkBuildResult<T>> scheduleSortTaskAsync(ChunkRenderContainer<T> render) {
		return this.schedule(this.createSortTask(render));
	}

	private ChunkRenderBuildTask<T> createRebuildTask(ChunkRenderContainer<T> render) {
		render.cancelRebuildTask();
		ChunkRenderContext context = WorldSlice.prepare(this.world, render.getChunkPos(), this.sectionCache);
		return (ChunkRenderBuildTask<T>)(context == null
			? new ChunkRenderEmptyBuildTask<>(render)
			: new ChunkRenderRebuildTask<>(render, context, render.getRenderOrigin()).withCameraPosition(this.cameraPosition));
	}

	private ChunkRenderBuildTask<T> createSortTask(ChunkRenderContainer<T> render) {
		render.cancelRebuildTask();
		return new ChunkRenderTranslucencySortTask<>(render, render.getRenderOrigin(), this.cameraPosition);
	}

	public void onChunkDataChanged(int x, int y, int z) {
		this.sectionCache.invalidate(x, y, z);
	}

	private class WorkerRunnable implements Runnable {
		private final AtomicBoolean running;
		private final ChunkBuildBuffers bufferCache;
		private final ChunkRenderCacheLocal cache;

		public WorkerRunnable(ChunkBuildBuffers bufferCache, ChunkRenderCacheLocal cache) {
			this.running = ChunkBuilder.this.running;
			this.bufferCache = bufferCache;
			this.cache = cache;
		}

		public void run() {
			while (this.running.get()) {
				ChunkBuilder.WrappedTask<T> job = this.getNextJob();
				if (job != null && !job.isCancelled()) {
					ChunkBuildResult<T> result;
					try {
						result = job.task.performBuild(this.cache, this.bufferCache, job);
					} catch (Throwable var7) {
						job.future.completeExceptionally(var7);
						SodiumClientMod.logger().error("Chunk build failed", var7);
						continue;
					} finally {
						job.task.releaseResources();
					}

					if (result != null) {
						job.future.complete(result);
					} else if (!job.isCancelled()) {
						job.future.completeExceptionally(new RuntimeException("No result was produced by the task"));
					}
				}
			}
		}

		private ChunkBuilder.WrappedTask<T> getNextJob() {
			ChunkBuilder.WrappedTask<T> job = (ChunkBuilder.WrappedTask<T>)ChunkBuilder.this.buildQueue.poll();
			if (job == null) {
				synchronized (ChunkBuilder.this.jobNotifier) {
					try {
						ChunkBuilder.this.jobNotifier.wait();
					} catch (InterruptedException var5) {
					}
				}
			}

			return job;
		}
	}

	private static class WrappedTask<T extends ChunkGraphicsState> implements CancellationSource {
		private final ChunkRenderBuildTask<T> task;
		private final CompletableFuture<ChunkBuildResult<T>> future;

		private WrappedTask(ChunkRenderBuildTask<T> task) {
			this.task = task;
			this.future = new CompletableFuture();
		}

		@Override
		public boolean isCancelled() {
			return this.future.isCancelled();
		}
	}
}
