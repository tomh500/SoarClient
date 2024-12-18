package com.soarclient.viasoar.common.protocoltranslator.provider;

import com.soarclient.viasoar.common.ViaSoarCommon;
import com.viaversion.viaversion.api.connection.UserConnection;

import net.raphimc.vialegacy.protocol.release.r1_6_4tor1_7_2_5.provider.EncryptionProvider;

public class ViaSoarEncryptionProvider extends EncryptionProvider {

	@Override
	public void enableDecryption(UserConnection user) {
		user.getChannel().attr(ViaSoarCommon.VF_NETWORK_MANAGER).getAndRemove().setupPreNettyDecryption();
	}

}
