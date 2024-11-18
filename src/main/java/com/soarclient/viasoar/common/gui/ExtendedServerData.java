package com.soarclient.viasoar.common.gui;

import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;

public interface ExtendedServerData {

    ProtocolVersion getVersion();

    void setVersion(final ProtocolVersion version);

}
