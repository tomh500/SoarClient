package com.soarclient.libraries.sodium.client.gl.tessellation;

import com.soarclient.libraries.sodium.client.gl.device.CommandList;

public interface GlTessellation {
	void delete(CommandList commandList);

	void bind(CommandList commandList);

	void unbind(CommandList commandList);

	GlPrimitiveType getPrimitiveType();
}
