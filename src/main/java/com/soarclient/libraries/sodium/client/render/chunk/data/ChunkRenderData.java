package com.soarclient.libraries.sodium.client.render.chunk.data;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.Predicate;

import com.soarclient.libraries.sodium.client.gl.util.BufferSlice;
import com.soarclient.libraries.sodium.client.model.quad.properties.ModelQuadFacing;
import com.soarclient.libraries.sodium.client.render.chunk.passes.BlockRenderPass;

import net.minecraft.client.renderer.chunk.SetVisibility;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

public class ChunkRenderData {
	public static final ChunkRenderData ABSENT = new ChunkRenderData.Builder().build();
	public static final ChunkRenderData EMPTY = createEmptyData();
	private Set<TileEntity> globalBlockEntities;
	private List<TileEntity> blockEntities;
	private EnumMap<BlockRenderPass, ChunkMeshData> meshes;
	private SetVisibility occlusionData;
	private ChunkRenderBounds bounds;
	private List<TextureAtlasSprite> animatedSprites;
	private boolean isEmpty;
	private int meshByteSize;
	private int facesWithData;

	public boolean isEmpty() {
		return this.isEmpty;
	}

	public Collection<TileEntity> getBlockEntities() {
		return this.blockEntities;
	}

	public Collection<TileEntity> getGlobalBlockEntities() {
		return this.globalBlockEntities;
	}

	public ChunkMeshData getMesh(BlockRenderPass pass) {
		return (ChunkMeshData) this.meshes.get(pass);
	}

	public void setMesh(BlockRenderPass pass, ChunkMeshData data) {
		if (this.meshes.get(pass) == null) {
			throw new IllegalStateException("No mesh found");
		} else {
			this.meshes.put(pass, data);
		}
	}

	public int getMeshSize() {
		return this.meshByteSize;
	}

	public ChunkRenderData copyAndReplaceMesh(Map<BlockRenderPass, ChunkMeshData> replacements) {
		ChunkRenderData data = new ChunkRenderData();
		data.globalBlockEntities = this.globalBlockEntities;
		data.blockEntities = this.blockEntities;
		data.occlusionData = this.occlusionData;
		data.meshes = new EnumMap(this.meshes);
		data.bounds = this.bounds;
		data.animatedSprites = new ObjectArrayList<>(this.animatedSprites);
		data.meshes.putAll(replacements);
		int facesWithData = 0;
		int size = 0;

		for (ChunkMeshData meshData : this.meshes.values()) {
			size += meshData.getVertexDataSize();

			for (Entry<ModelQuadFacing, BufferSlice> entry : meshData.getSlices()) {
				facesWithData |= 1 << ((ModelQuadFacing) entry.getKey()).ordinal();
			}
		}

		data.isEmpty = this.globalBlockEntities.isEmpty() && this.blockEntities.isEmpty() && facesWithData == 0;
		data.meshByteSize = size;
		data.facesWithData = facesWithData;
		return data;
	}

	private static ChunkRenderData createEmptyData() {
		SetVisibility occlusionData = new SetVisibility();
		occlusionData.setManyVisible(EnumSet.allOf(EnumFacing.class));
		ChunkRenderData.Builder meshInfo = new ChunkRenderData.Builder();
		meshInfo.setOcclusionData(occlusionData);
		return meshInfo.build();
	}

	public SetVisibility getOcclusionData() {
		return this.occlusionData;
	}

	public ChunkRenderBounds getBounds() {
		return this.bounds;
	}

	public List<TextureAtlasSprite> getAnimatedSprites() {
		return this.animatedSprites;
	}

	public int getFacesWithData() {
		return this.facesWithData;
	}

	public static class Builder {
		private final List<TileEntity> globalBlockEntities = new ArrayList();
		private final List<TileEntity> blockEntities = new ArrayList();
		private final Set<TextureAtlasSprite> animatedSprites = new ObjectOpenHashSet<>();
		private final EnumMap<BlockRenderPass, ChunkMeshData> meshes = new EnumMap(BlockRenderPass.class);
		private SetVisibility occlusionData;
		private ChunkRenderBounds bounds = ChunkRenderBounds.ALWAYS_FALSE;

		public Builder() {
			for (BlockRenderPass pass : BlockRenderPass.VALUES) {
				this.setMesh(pass, ChunkMeshData.EMPTY);
			}
		}

		public void addSprite(TextureAtlasSprite sprite) {
			if (sprite.hasAnimationMetadata()) {
				this.animatedSprites.add(sprite);
			}
		}

		public void setMesh(BlockRenderPass pass, ChunkMeshData data) {
			this.meshes.put(pass, data);
		}

		public void addBlockEntity(TileEntity entity, boolean cull) {
			(cull ? this.blockEntities : this.globalBlockEntities).add(entity);
		}

		public void removeBlockEntitiesIf(Predicate<TileEntity> removePredicate) {
			this.blockEntities.removeIf(removePredicate);
			this.globalBlockEntities.removeIf(removePredicate);
		}

		public ChunkRenderData build() {
			ChunkRenderData data = new ChunkRenderData();
			data.globalBlockEntities = new ObjectOpenHashSet<>(this.globalBlockEntities);
			data.blockEntities = this.blockEntities;
			data.occlusionData = this.occlusionData;
			data.meshes = this.meshes;
			data.bounds = this.bounds;
			data.animatedSprites = new ObjectArrayList<>(this.animatedSprites);
			int facesWithData = 0;
			int size = 0;

			for (ChunkMeshData meshData : this.meshes.values()) {
				size += meshData.getVertexDataSize();

				for (Entry<ModelQuadFacing, BufferSlice> entry : meshData.getSlices()) {
					facesWithData |= 1 << ((ModelQuadFacing) entry.getKey()).ordinal();
				}
			}

			data.isEmpty = this.globalBlockEntities.isEmpty() && this.blockEntities.isEmpty() && facesWithData == 0;
			data.meshByteSize = size;
			data.facesWithData = facesWithData;
			return data;
		}

		public void setOcclusionData(SetVisibility occlusionData) {
			this.occlusionData = occlusionData;
		}

		public void setBounds(ChunkRenderBounds bounds) {
			this.bounds = bounds;
		}
	}
}
