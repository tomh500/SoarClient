package com.soarclient.viasoar.common.protocoltranslator.netty;

import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;

public interface VSNetworkManager {

	void setupPreNettyDecryption();

	ProtocolVersion getTrackedVersion();

	void setTrackedVersion(final ProtocolVersion version);

}
