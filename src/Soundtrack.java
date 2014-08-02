final class Soundtrack {
	public static void method166() {
		Soundtrack.noise = new int[32768];
		for (int i = 0; i < 32768; i++) {
			if (Math.random() > 0.5D) {
				Soundtrack.noise[i] = 1;
			} else {
				Soundtrack.noise[i] = -1;
			}
		}
		Soundtrack.sineTable = new int[32768];
		for (int j = 0; j < 32768; j++) {
			Soundtrack.sineTable[j] = (int) (Math.sin(j / 5215.1903000000002D) * 16384D);
		}
		Soundtrack.sampleBuffer = new int[0x35d54];
	}

	public int[] method167(int arg0, int arg1) {
		for (int k = 0; k < arg0; k++) {
			Soundtrack.sampleBuffer[k] = 0;
		}
		if (arg1 < 10) {
			return Soundtrack.sampleBuffer;
		}
		double d = arg0 / (arg1 + 0.0D);
		aClass29_98.resetValues();
		aClass29_99.resetValues();
		int l = 0;
		int i1 = 0;
		int j1 = 0;
		if (aClass29_100 != null) {
			aClass29_100.resetValues();
			aClass29_101.resetValues();
			l = (int) ((aClass29_100.anInt539 - aClass29_100.anInt538) * 32.768000000000001D / d);
			i1 = (int) (aClass29_100.anInt538 * 32.768000000000001D / d);
		}
		int k1 = 0;
		int l1 = 0;
		int i2 = 0;
		if (aClass29_102 != null) {
			aClass29_102.resetValues();
			aClass29_103.resetValues();
			k1 = (int) ((aClass29_102.anInt539 - aClass29_102.anInt538) * 32.768000000000001D / d);
			l1 = (int) (aClass29_102.anInt538 * 32.768000000000001D / d);
		}
		for (int j2 = 0; j2 < 5; j2++) {
			if (anIntArray106[j2] != 0) {
				Soundtrack.phase[j2] = 0;
				Soundtrack.anIntArray119[j2] = (int) (anIntArray108[j2] * d);
				Soundtrack.anIntArray120[j2] = (anIntArray106[j2] << 14) / 100;
				Soundtrack.anIntArray121[j2] = (int) ((aClass29_98.anInt539 - aClass29_98.anInt538) * 32.768000000000001D * Math.pow(1.0057929410678534D, anIntArray107[j2]) / d);
				Soundtrack.anIntArray122[j2] = (int) (aClass29_98.anInt538 * 32.768000000000001D / d);
			}
		}
		for (int k2 = 0; k2 < arg0; k2++) {
			int l2 = aClass29_98.method328(arg0);
			int j4 = aClass29_99.method328(arg0);
			if (aClass29_100 != null) {
				int j5 = aClass29_100.method328(arg0);
				int j6 = aClass29_101.method328(arg0);
				l2 += method168(j6, j1, aClass29_100.anInt540) >> 1;
				j1 += (j5 * l >> 16) + i1;
			}
			if (aClass29_102 != null) {
				int k5 = aClass29_102.method328(arg0);
				int k6 = aClass29_103.method328(arg0);
				j4 = j4 * ((method168(k6, i2, aClass29_102.anInt540) >> 1) + 32768) >> 15;
				i2 += (k5 * k1 >> 16) + l1;
			}
			for (int l5 = 0; l5 < 5; l5++) {
				if (anIntArray106[l5] != 0) {
					int l6 = k2 + Soundtrack.anIntArray119[l5];
					if (l6 < arg0) {
						Soundtrack.sampleBuffer[l6] += method168(j4 * Soundtrack.anIntArray120[l5] >> 15, Soundtrack.phase[l5], aClass29_98.anInt540);
						Soundtrack.phase[l5] += (l2 * Soundtrack.anIntArray121[l5] >> 16) + Soundtrack.anIntArray122[l5];
					}
				}
			}
		}
		if (aClass29_104 != null) {
			aClass29_104.resetValues();
			aClass29_105.resetValues();
			int i3 = 0;
			boolean flag1 = true;
			for (int i7 = 0; i7 < arg0; i7++) {
				int k7 = aClass29_104.method328(arg0);
				int i8 = aClass29_105.method328(arg0);
				int k4;
				if (flag1) {
					k4 = aClass29_104.anInt538 + ((aClass29_104.anInt539 - aClass29_104.anInt538) * k7 >> 8);
				} else {
					k4 = aClass29_104.anInt538 + ((aClass29_104.anInt539 - aClass29_104.anInt538) * i8 >> 8);
				}
				if ((i3 += 256) >= k4) {
					i3 = 0;
					flag1 = !flag1;
				}
				if (flag1) {
					Soundtrack.sampleBuffer[i7] = 0;
				}
			}
		}
		if (anInt109 > 0 && gain > 0) {
			int j3 = (int) (anInt109 * d);
			for (int l4 = j3; l4 < arg0; l4++) {
				Soundtrack.sampleBuffer[l4] += Soundtrack.sampleBuffer[l4 - j3] * gain / 100;
			}
		}
		if (aClass39_111.anIntArray665[0] > 0 || aClass39_111.anIntArray665[1] > 0) {
			aClass29_112.resetValues();
			int k3 = aClass29_112.method328(arg0 + 1);
			int i5 = aClass39_111.method544(0, k3 / 65536F);
			int i6 = aClass39_111.method544(1, k3 / 65536F);
			if (arg0 >= i5 + i6) {
				int j7 = 0;
				int l7 = i6;
				if (l7 > arg0 - i5) {
					l7 = arg0 - i5;
				}
				for (; j7 < l7; j7++) {
					int j8 = (int) ((long) Soundtrack.sampleBuffer[j7 + i5] * (long) Class39.anInt672 >> 16);
					for (int k8 = 0; k8 < i5; k8++) {
						j8 += (int) ((long) Soundtrack.sampleBuffer[j7 + i5 - 1 - k8] * (long) Class39.anIntArrayArray670[0][k8] >> 16);
					}
					for (int j9 = 0; j9 < j7; j9++) {
						j8 -= (int) ((long) Soundtrack.sampleBuffer[j7 - 1 - j9] * (long) Class39.anIntArrayArray670[1][j9] >> 16);
					}
					Soundtrack.sampleBuffer[j7] = j8;
					k3 = aClass29_112.method328(arg0 + 1);
				}
				char c = '\200';
				l7 = c;
				do {
					if (l7 > arg0 - i5) {
						l7 = arg0 - i5;
					}
					for (; j7 < l7; j7++) {
						int l8 = (int) ((long) Soundtrack.sampleBuffer[j7 + i5] * (long) Class39.anInt672 >> 16);
						for (int k9 = 0; k9 < i5; k9++) {
							l8 += (int) ((long) Soundtrack.sampleBuffer[j7 + i5 - 1 - k9] * (long) Class39.anIntArrayArray670[0][k9] >> 16);
						}
						for (int i10 = 0; i10 < i6; i10++) {
							l8 -= (int) ((long) Soundtrack.sampleBuffer[j7 - 1 - i10] * (long) Class39.anIntArrayArray670[1][i10] >> 16);
						}
						Soundtrack.sampleBuffer[j7] = l8;
						k3 = aClass29_112.method328(arg0 + 1);
					}
					if (j7 >= arg0 - i5) {
						break;
					}
					i5 = aClass39_111.method544(0, k3 / 65536F);
					i6 = aClass39_111.method544(1, k3 / 65536F);
					l7 += c;
				} while (true);
				for (; j7 < arg0; j7++) {
					int i9 = 0;
					for (int l9 = j7 + i5 - arg0; l9 < i5; l9++) {
						i9 += (int) ((long) Soundtrack.sampleBuffer[j7 + i5 - 1 - l9] * (long) Class39.anIntArrayArray670[0][l9] >> 16);
					}
					for (int j10 = 0; j10 < i6; j10++) {
						i9 -= (int) ((long) Soundtrack.sampleBuffer[j7 - 1 - j10] * (long) Class39.anIntArrayArray670[1][j10] >> 16);
					}
					Soundtrack.sampleBuffer[j7] = i9;
					aClass29_112.method328(arg0 + 1);
				}
			}
		}
		for (int i4 = 0; i4 < arg0; i4++) {
			if (Soundtrack.sampleBuffer[i4] < -32768) {
				Soundtrack.sampleBuffer[i4] = -32768;
			}
			if (Soundtrack.sampleBuffer[i4] > 32767) {
				Soundtrack.sampleBuffer[i4] = 32767;
			}
		}
		return Soundtrack.sampleBuffer;
	}

	private int method168(int arg0, int arg1, int arg2) {
		if (arg2 == 1) {
			if ((arg1 & 0x7fff) < 16384) {
				return arg0;
			} else {
				return -arg0;
			}
		}
		if (arg2 == 2) {
			return Soundtrack.sineTable[arg1 & 0x7fff] * arg0 >> 14;
		}
		if (arg2 == 3) {
			return ((arg1 & 0x7fff) * arg0 >> 14) - arg0;
		}
		if (arg2 == 4) {
			return Soundtrack.noise[arg1 / 2607 & 0x7fff] * arg0;
		} else {
			return 0;
		}
	}

	public void method169(Stream buffer) {
		aClass29_98 = new Class29();
		aClass29_98.method325(buffer);
		aClass29_99 = new Class29();
		aClass29_99.method325(buffer);
		int i = buffer.getUnsignedByte();
		if (i != 0) {
			buffer.offset--;
			aClass29_100 = new Class29();
			aClass29_100.method325(buffer);
			aClass29_101 = new Class29();
			aClass29_101.method325(buffer);
		}
		i = buffer.getUnsignedByte();
		if (i != 0) {
			buffer.offset--;
			aClass29_102 = new Class29();
			aClass29_102.method325(buffer);
			aClass29_103 = new Class29();
			aClass29_103.method325(buffer);
		}
		i = buffer.getUnsignedByte();
		if (i != 0) {
			buffer.offset--;
			aClass29_104 = new Class29();
			aClass29_104.method325(buffer);
			aClass29_105 = new Class29();
			aClass29_105.method325(buffer);
		}
		for (int j = 0; j < 10; j++) {
			int k = buffer.getSmart();
			if (k == 0) {
				break;
			}
			anIntArray106[j] = k;
			anIntArray107[j] = buffer.getUnsignedSmart();
			anIntArray108[j] = buffer.getSmart();
		}
		anInt109 = buffer.getSmart();
		gain = buffer.getSmart();
		length = buffer.getUnsignedShort();
		anInt114 = buffer.getUnsignedShort();
		aClass39_111 = new Class39();
		aClass29_112 = new Class29();
		aClass39_111.method545(buffer, aClass29_112);
	}

	public Soundtrack() {
		anIntArray106 = new int[5];
		anIntArray107 = new int[5];
		anIntArray108 = new int[5];
		gain = 100;
		length = 500;
	}
	private Class29 aClass29_98;
	private Class29 aClass29_99;
	private Class29 aClass29_100;
	private Class29 aClass29_101;
	private Class29 aClass29_102;
	private Class29 aClass29_103;
	private Class29 aClass29_104;
	private Class29 aClass29_105;
	private final int[] anIntArray106;
	private final int[] anIntArray107;
	private final int[] anIntArray108;
	private int anInt109;
	private int gain;
	private Class39 aClass39_111;
	private Class29 aClass29_112;
	int length;
	int anInt114;
	private static int[] sampleBuffer;
	private static int[] noise;
	private static int[] sineTable;
	private static final int[] phase = new int[5];
	private static final int[] anIntArray119 = new int[5];
	private static final int[] anIntArray120 = new int[5];
	private static final int[] anIntArray121 = new int[5];
	private static final int[] anIntArray122 = new int[5];
}
