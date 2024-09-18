package com.soarclient.libraries.sodium.client.render.chunk;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.soarclient.libraries.sodium.client.gl.device.CommandList;
import com.soarclient.libraries.sodium.client.gl.device.RenderDevice;
import com.soarclient.libraries.sodium.client.model.vertex.type.ChunkVertexType;
import com.soarclient.libraries.sodium.client.render.chunk.compile.ChunkBuildResult;
import com.soarclient.libraries.sodium.client.render.chunk.lists.ChunkRenderListIterator;

public interface ChunkRenderBackend<T extends ChunkGraphicsState> {
	void upload(CommandList commandList, Iterator<ChunkBuildResult<T>> iterator);

	void render(CommandList commandList, ChunkRenderListIterator<T> chunkRenderListIterator, ChunkCameraContext chunkCameraContext);

	void createShaders(RenderDevice renderDevice);

	void begin();

	void end();

	void delete();

	ChunkVertexType getVertexType();

	Class<T> getGraphicsStateType();

	default String getRendererName() {
		return this.getClass().getSimpleName();
	}

	default List<String> getDebugStrings() {
		return Collections.emptyList();
	}
}
