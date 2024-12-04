package com.soarclient.viasoar.common.protocoltranslator.provider;

import com.soarclient.viasoar.common.ViaSoarCommon;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import com.viaversion.viaversion.protocol.version.BaseVersionProvider;

public class ViaSoarVersionProvider extends BaseVersionProvider {

    @Override
    public ProtocolVersion getClosestServerProtocol(UserConnection connection) throws Exception {
        if (connection.isClientSide() && !ViaSoarCommon.getManager().getPlatform().isSingleplayer().get()) {
            return connection.getChannel().attr(ViaSoarCommon.VF_NETWORK_MANAGER).get().getTrackedVersion();
        }
        return super.getClosestServerProtocol(connection);
    }
    
}
