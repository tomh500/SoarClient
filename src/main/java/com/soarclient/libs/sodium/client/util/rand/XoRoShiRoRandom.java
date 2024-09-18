package com.soarclient.libs.sodium.client.util.rand;

import java.util.Random;

public class XoRoShiRoRandom extends Random {
	private static final long serialVersionUID = 1L;
	private SplitMixRandom mixer;
	private long seed = Long.MIN_VALUE;
	private long p0;
	private long p1;
	private long s0;
	private long s1;
	private boolean hasSavedState;
	private static final SplitMixRandom seedUniquifier = new SplitMixRandom(System.nanoTime());

	public static long randomSeed() {
		long x;
		synchronized (seedUniquifier) {
			x = seedUniquifier.nextLong();
		}

		return x ^ System.nanoTime();
	}

	public XoRoShiRoRandom() {
		this(randomSeed());
	}

	public XoRoShiRoRandom(long seed) {
		this.setSeed(seed);
	}

	public long nextLong() {
		long s0 = this.s0;
		long s1 = this.s1;
		long result = s0 + s1;
		s1 ^= s0;
		this.s0 = Long.rotateLeft(s0, 24) ^ s1 ^ s1 << 16;
		this.s1 = Long.rotateLeft(s1, 37);
		return result;
	}

	public int nextInt() {
		return (int)this.nextLong();
	}

	public int nextInt(int n) {
		return (int)this.nextLong((long)n);
	}

	public long nextLong(long n) {
		if (n <= 0L) {
			throw new IllegalArgumentException("illegal bound " + n + " (must be positive)");
		} else {
			long t = this.nextLong();
			long nMinus1 = n - 1L;
			if ((n & nMinus1) == 0L) {
				return t >>> Long.numberOfLeadingZeros(nMinus1) & nMinus1;
			} else {
				long u = t >>> 1;

				while (u + nMinus1 - (t = u % n) < 0L) {
					u = this.nextLong() >>> 1;
				}

				return t;
			}
		}
	}

	public double nextDouble() {
		return Double.longBitsToDouble(4607182418800017408L | this.nextLong() >>> 12) - 1.0;
	}

	public float nextFloat() {
		return (float)(this.nextLong() >>> 40) * 5.9604645E-8F;
	}

	public boolean nextBoolean() {
		return this.nextLong() < 0L;
	}

	public void nextBytes(byte[] bytes) {
		int i = bytes.length;

		while (i != 0) {
			int n = Math.min(i, 8);

			for (long bits = this.nextLong(); n-- != 0; bits >>= 8) {
				i--;
				bytes[i] = (byte)((int)bits);
			}
		}
	}

	public void setSeed(long seed) {
		if (this.hasSavedState && this.seed == seed) {
			this.s0 = this.p0;
			this.s1 = this.p1;
		} else {
			SplitMixRandom mixer = this.mixer;
			if (mixer == null) {
				mixer = this.mixer = new SplitMixRandom(seed);
			} else {
				mixer.setSeed(seed);
			}

			this.s0 = mixer.nextLong();
			this.s1 = mixer.nextLong();
			this.p0 = this.s0;
			this.p1 = this.s1;
			this.seed = seed;
			this.hasSavedState = true;
		}
	}

	public XoRoShiRoRandom setSeedAndReturn(long seed) {
		this.setSeed(seed);
		return this;
	}
}
