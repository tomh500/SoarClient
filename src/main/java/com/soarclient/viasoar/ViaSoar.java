package com.soarclient.viasoar;

import java.io.File;
import java.util.function.Supplier;

import com.soarclient.viasoar.common.platform.VSPlatform;
import com.soarclient.viasoar.provider.ViaSoarGameProfileFetcher;

import net.minecraft.client.Minecraft;
import net.minecraft.realms.RealmsSharedConstants;
import net.minecraft.util.Session;
import net.raphimc.vialegacy.protocol.release.r1_7_6_10tor1_8.provider.GameProfileFetcher;

public class ViaSoar implements VSPlatform {
    
    public static final ViaSoar PLATFORM = new ViaSoar();

    @Override
    public int getGameVersion() {
        return RealmsSharedConstants.NETWORK_PROTOCOL_VERSION;
    }

    @Override
    public Supplier<Boolean> isSingleplayer() {
        return () -> Minecraft.getMinecraft().isSingleplayer();
    }

    @Override
    public File getLeadingDirectory() {
        return Minecraft.getMinecraft().mcDataDir;
    }

    @Override
    public void joinServer(String serverId) throws Throwable {
        final Session session = Minecraft.getMinecraft().getSession();

        Minecraft.getMinecraft().getSessionService().joinServer(session.getProfile(), session.getToken(), serverId);
    }

    @Override
    public GameProfileFetcher getGameProfileFetcher() {
        return new ViaSoarGameProfileFetcher();
    }
    
}
