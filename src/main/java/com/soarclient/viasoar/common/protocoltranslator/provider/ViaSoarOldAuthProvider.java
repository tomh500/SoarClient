package com.soarclient.viasoar.common.protocoltranslator.provider;

import com.soarclient.viasoar.common.ViaSoarCommon;
import com.viaversion.viaversion.api.connection.UserConnection;

import net.raphimc.vialegacy.protocol.release.r1_2_4_5tor1_3_1_2.provider.OldAuthProvider;

public class ViaSoarOldAuthProvider extends OldAuthProvider {

	@Override
	public void sendAuthRequest(UserConnection user, String serverId) throws Throwable {
		final ViaSoarCommon common = ViaSoarCommon.getManager();
		if (!common.getConfig().isVerifySessionInOldVersions()) {
			return;
		}
		common.getPlatform().joinServer(serverId);
	}

}
