package com.soarclient.libs.sodium.client.gui.options.named;

public enum ParticleMode implements NamedState {
	ALL("options.particles.all"),
	DECREASED("options.particles.decreased"),
	MINIMAL("options.particles.minimal");

	private static final ParticleMode[] VALUES = values();
	private final String name;

	private ParticleMode(String name) {
		this.name = name;
	}

	@Override
	public String getKey() {
		return this.name;
	}

	public static ParticleMode fromOrdinal(int ordinal) {
		return VALUES[ordinal];
	}
}
