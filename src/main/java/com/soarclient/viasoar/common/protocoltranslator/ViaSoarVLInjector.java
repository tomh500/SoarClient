package com.soarclient.viasoar.common.protocoltranslator;

import net.raphimc.vialoader.impl.viaversion.VLInjector;
import net.raphimc.vialoader.netty.VLLegacyPipeline;

public class ViaSoarVLInjector extends VLInjector {

	@Override
	public String getDecoderName() {
		return VLLegacyPipeline.VIA_DECODER_NAME;
	}

	@Override
	public String getEncoderName() {
		return VLLegacyPipeline.VIA_ENCODER_NAME;
	}

}
