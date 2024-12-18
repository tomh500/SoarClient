package com.soarclient.viasoar.common;

import java.io.File;

import com.soarclient.utils.file.FileLocation;
import com.soarclient.viasoar.common.gui.AsyncVersionSlider;
import com.soarclient.viasoar.common.platform.VSPlatform;
import com.soarclient.viasoar.common.platform.ViaSoarConfig;
import com.soarclient.viasoar.common.protocoltranslator.ViaSoarVLInjector;
import com.soarclient.viasoar.common.protocoltranslator.ViaSoarVLLoader;
import com.soarclient.viasoar.common.protocoltranslator.netty.VSNetworkManager;
import com.soarclient.viasoar.common.protocoltranslator.netty.ViaSoarVLLegacyPipeline;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import com.viaversion.viaversion.connection.UserConnectionImpl;
import com.viaversion.viaversion.protocol.ProtocolPipelineImpl;

import io.netty.channel.Channel;
import io.netty.util.AttributeKey;
import net.raphimc.vialoader.ViaLoader;
import net.raphimc.vialoader.impl.platform.ViaAprilFoolsPlatformImpl;
import net.raphimc.vialoader.impl.platform.ViaBackwardsPlatformImpl;
import net.raphimc.vialoader.impl.platform.ViaLegacyPlatformImpl;
import net.raphimc.vialoader.impl.platform.ViaRewindPlatformImpl;
import net.raphimc.vialoader.impl.platform.ViaVersionPlatformImpl;
import net.raphimc.vialoader.netty.CompressionReorderEvent;

public class ViaSoarCommon {

	public static final AttributeKey<UserConnection> VF_VIA_USER = AttributeKey.valueOf("local_via_user");
	public static final AttributeKey<VSNetworkManager> VF_NETWORK_MANAGER = AttributeKey.valueOf("encryption_setup");

	private static ViaSoarCommon manager;

	private final VSPlatform platform;
	private ProtocolVersion targetVersion;
	private ProtocolVersion previousVersion;

	private ViaSoarConfig config;

	private static AsyncVersionSlider asyncVersionSlider;

	public ViaSoarCommon(VSPlatform platform) {
		this.platform = platform;
	}

	public static void init(final VSPlatform platform) {

		if (manager != null) {
			return;
		}

		final ProtocolVersion version = ProtocolVersion.getProtocol(platform.getGameVersion());

		if (version == ProtocolVersion.unknown) {
			throw new IllegalArgumentException("Unknown version " + platform.getGameVersion());
		}

		manager = new ViaSoarCommon(platform);

		final File mainFolder = new File(FileLocation.SOAR_DIR, "ViaSoar");

		ViaLoader.init(new ViaVersionPlatformImpl(mainFolder), new ViaSoarVLLoader(platform), new ViaSoarVLInjector(),
				null, ViaBackwardsPlatformImpl::new, ViaRewindPlatformImpl::new, ViaLegacyPlatformImpl::new,
				ViaAprilFoolsPlatformImpl::new);
		manager.config = new ViaSoarConfig(new File(mainFolder, "viaforgemcp.yml"), Via.getPlatform().getLogger());

		final ProtocolVersion configVersion = ProtocolVersion.getClosest(manager.config.getClientSideVersion());
		if (configVersion != null) {
			manager.setTargetVersion(configVersion);
		} else {
			manager.setTargetVersion(version);
		}

		asyncVersionSlider = new AsyncVersionSlider(-1, 5, 5, 110, 20);
		asyncVersionSlider.setVersion(ViaSoarCommon.getManager().getTargetVersion());
	}

	public void inject(final Channel channel, final VSNetworkManager networkManager) {

		if (networkManager.getTrackedVersion().equals(getNativeVersion())) {
			return;
		}

		channel.attr(VF_NETWORK_MANAGER).set(networkManager);

		final UserConnection user = new UserConnectionImpl(channel, true);
		new ProtocolPipelineImpl(user);

		channel.attr(VF_VIA_USER).set(user);

		channel.pipeline().addLast(new ViaSoarVLLegacyPipeline(user, targetVersion));
		channel.closeFuture().addListener(future -> {
			if (previousVersion != null) {
				restoreVersion();
			}
		});
	}

	public void reorderCompression(final Channel channel) {
		channel.pipeline().fireUserEventTriggered(CompressionReorderEvent.INSTANCE);
	}

	public ProtocolVersion getNativeVersion() {
		return ProtocolVersion.getProtocol(platform.getGameVersion());
	}

	public ProtocolVersion getTargetVersion() {
		return targetVersion;
	}

	public void restoreVersion() {
		this.targetVersion = ProtocolVersion.getClosest(config.getClientSideVersion());
	}

	public void setTargetVersionSilent(final ProtocolVersion targetVersion) {
		final ProtocolVersion oldVersion = this.targetVersion;
		this.targetVersion = targetVersion;
		if (oldVersion != targetVersion) {
			previousVersion = oldVersion;
		}
	}

	public void setTargetVersion(final ProtocolVersion targetVersion) {
		this.targetVersion = targetVersion;
		config.setClientSideVersion(targetVersion.getName());
	}

	public VSPlatform getPlatform() {
		return platform;
	}

	public ViaSoarConfig getConfig() {
		return config;
	}

	public static ViaSoarCommon getManager() {
		return manager;
	}

	public static AsyncVersionSlider getAsyncVersionSlider() {
		return asyncVersionSlider;
	}
}
