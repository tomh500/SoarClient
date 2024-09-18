package com.soarclient.libs.sodium.client.gui.options.storage;

import java.io.IOException;

import com.soarclient.libs.sodium.SodiumClientMod;
import com.soarclient.libs.sodium.client.gui.SodiumGameOptions;

public class SodiumOptionsStorage implements OptionStorage<SodiumGameOptions> {
	private final SodiumGameOptions options = SodiumClientMod.options();

	public SodiumGameOptions getData() {
		return this.options;
	}

	@Override
	public void save() {
		try {
			this.options.writeChanges();
		} catch (IOException var2) {
			throw new RuntimeException("Couldn't save configuration changes", var2);
		}

		SodiumClientMod.logger().info("Flushed changes to Oldium configuration");
	}
}
