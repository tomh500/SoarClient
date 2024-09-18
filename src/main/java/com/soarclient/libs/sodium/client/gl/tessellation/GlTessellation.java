package com.soarclient.libs.sodium.client.gl.tessellation;

import com.soarclient.libs.sodium.client.gl.device.CommandList;

public interface GlTessellation {
	void delete(CommandList commandList);

	void bind(CommandList commandList);

	void unbind(CommandList commandList);

	GlPrimitiveType getPrimitiveType();
}
