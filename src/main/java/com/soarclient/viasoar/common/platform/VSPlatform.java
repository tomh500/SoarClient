package com.soarclient.viasoar.common.platform;

import java.io.File;
import java.util.function.Supplier;

import net.raphimc.vialegacy.protocol.release.r1_7_6_10tor1_8.provider.GameProfileFetcher;

public interface VSPlatform {

	int getGameVersion();

	Supplier<Boolean> isSingleplayer();

	File getLeadingDirectory();

	void joinServer(final String serverId) throws Throwable;

	GameProfileFetcher getGameProfileFetcher();
}
