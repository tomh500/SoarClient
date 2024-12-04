package com.soarclient.viasoar.common.protocoltranslator;

import com.soarclient.viasoar.common.platform.VSPlatform;
import com.soarclient.viasoar.common.protocoltranslator.provider.ViaSoarClassicMPPassProvider;
import com.soarclient.viasoar.common.protocoltranslator.provider.ViaSoarEncryptionProvider;
import com.soarclient.viasoar.common.protocoltranslator.provider.ViaSoarMovementTransmitterProvider;
import com.soarclient.viasoar.common.protocoltranslator.provider.ViaSoarOldAuthProvider;
import com.soarclient.viasoar.common.protocoltranslator.provider.ViaSoarVersionProvider;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.platform.providers.ViaProviders;
import com.viaversion.viaversion.api.protocol.version.VersionProvider;
import com.viaversion.viaversion.protocols.v1_8to1_9.provider.MovementTransmitterProvider;

import net.raphimc.vialegacy.protocol.classic.c0_28_30toa1_0_15.provider.ClassicMPPassProvider;
import net.raphimc.vialegacy.protocol.release.r1_2_4_5tor1_3_1_2.provider.OldAuthProvider;
import net.raphimc.vialegacy.protocol.release.r1_6_4tor1_7_2_5.provider.EncryptionProvider;
import net.raphimc.vialegacy.protocol.release.r1_7_6_10tor1_8.provider.GameProfileFetcher;
import net.raphimc.vialoader.impl.viaversion.VLLoader;

public class ViaSoarVLLoader extends VLLoader {

    private final VSPlatform platform;

    public ViaSoarVLLoader(VSPlatform platform) {
        this.platform = platform;
    }

    @Override
    public void load() {
        super.load();

        final ViaProviders providers = Via.getManager().getProviders();

        providers.use(VersionProvider.class, new ViaSoarVersionProvider());
        providers.use(MovementTransmitterProvider.class, new ViaSoarMovementTransmitterProvider());
        providers.use(OldAuthProvider.class, new ViaSoarOldAuthProvider());
        providers.use(GameProfileFetcher.class, platform.getGameProfileFetcher());
        providers.use(EncryptionProvider.class, new ViaSoarEncryptionProvider());
        providers.use(ClassicMPPassProvider.class, new ViaSoarClassicMPPassProvider());
    }
}
