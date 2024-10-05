package com.soarclient.libraries.sodium.client.util.rand;

import it.unimi.dsi.fastutil.HashCommon;

public class SplitMixRandom {
	private static final long PHI = -7046029254386353131L;
	private long x;

	public SplitMixRandom() {
		this(XoRoShiRoRandom.randomSeed());
	}

	public SplitMixRandom(long seed) {
		this.setSeed(seed);
	}

	private static long staffordMix13(long z) {
		z = (z ^ z >>> 30) * -4658895280553007687L;
		z = (z ^ z >>> 27) * -7723592293110705685L;
		return z ^ z >>> 31;
	}

	private static int staffordMix4Upper32(long z) {
		z = (z ^ z >>> 33) * 7109453100751455733L;
		return (int) ((z ^ z >>> 28) * -3808689974395783757L >>> 32);
	}

	public long nextLong() {
		return staffordMix13(this.x += -7046029254386353131L);
	}

	public int nextInt() {
		return staffordMix4Upper32(this.x += -7046029254386353131L);
	}

	public int nextInt(int n) {
		return (int) this.nextLong((long) n);
	}

	public long nextLong(long n) {
		if (n <= 0L) {
			throw new IllegalArgumentException("illegal bound " + n + " (must be positive)");
		} else {
			long t = staffordMix13(this.x += -7046029254386353131L);
			long nMinus1 = n - 1L;
			if ((n & nMinus1) == 0L) {
				return t & nMinus1;
			} else {
				long u = t >>> 1;

				while (u + nMinus1 - (t = u % n) < 0L) {
					u = staffordMix13(this.x += -7046029254386353131L) >>> 1;
				}

				return t;
			}
		}
	}

	public double nextDouble() {
		return (double) (staffordMix13(this.x += -7046029254386353131L) >>> 11) * 1.110223E-16F;
	}

	public float nextFloat() {
		return (float) (staffordMix4Upper32(this.x += -7046029254386353131L) >>> 8) * 5.9604645E-8F;
	}

	public boolean nextBoolean() {
		return staffordMix4Upper32(this.x += -7046029254386353131L) < 0;
	}

	public void nextBytes(byte[] bytes) {
		int i = bytes.length;

		while (i != 0) {
			int n = Math.min(i, 8);

			for (long bits = staffordMix13(this.x += -7046029254386353131L); n-- != 0; bits >>= 8) {
				i--;
				bytes[i] = (byte) ((int) bits);
			}
		}
	}

	public void setSeed(long seed) {
		this.x = HashCommon.murmurHash3(seed);
	}

	public void setState(long state) {
		this.x = state;
	}
}
