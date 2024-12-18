package com.soarclient.viasoar.common.platform;

import com.soarclient.viasoar.common.ViaSoarCommon;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

public class VersionTracker {

	public static final Map<InetAddress, ProtocolVersion> SERVER_PROTOCOL_VERSIONS = new HashMap<>();

	public static void storeServerProtocolVersion(InetAddress address, ProtocolVersion version) {
		SERVER_PROTOCOL_VERSIONS.put(address, version);
		ViaSoarCommon.getManager().setTargetVersionSilent(version);
	}

	public static ProtocolVersion getServerProtocolVersion(InetAddress address) {
		return SERVER_PROTOCOL_VERSIONS.remove(address);
	}

}
