final class Class39 {
	private float method541(int arg0, int arg1, float arg3) {
		float f1 = anIntArrayArrayArray667[arg0][0][arg1] + arg3 * (anIntArrayArrayArray667[arg0][1][arg1] - anIntArrayArrayArray667[arg0][0][arg1]);
		f1 *= 0.001525879F;
		return 1.0F - (float) Math.pow(10D, -f1 / 20F);
	}

	private float method542(float arg0) {
		float f1 = 32.7032F * (float) Math.pow(2D, arg0);
		return f1 * 3.141593F / 11025F;
	}

	private float method543(float arg0, int arg1, int arg2) {
		float f1 = anIntArrayArrayArray666[arg2][0][arg1] + arg0 * (anIntArrayArrayArray666[arg2][1][arg1] - anIntArrayArrayArray666[arg2][0][arg1]);
		f1 *= 0.0001220703F;
		return method542(f1);
	}

	public int method544(int arg0, float arg1) {
		if (arg0 == 0) {
			float f1 = anIntArray668[0] + (anIntArray668[1] - anIntArray668[0]) * arg1;
			f1 *= 0.003051758F;
			Class39.aFloat671 = (float) Math.pow(0.10000000000000001D, f1 / 20F);
			Class39.anInt672 = (int) (Class39.aFloat671 * 65536F);
		}
		if (anIntArray665[arg0] == 0) {
			return 0;
		}
		float f2 = method541(arg0, 0, arg1);
		Class39.aFloatArrayArray669[arg0][0] = -2F * f2 * (float) Math.cos(method543(arg1, 0, arg0));
		Class39.aFloatArrayArray669[arg0][1] = f2 * f2;
		for (int k = 1; k < anIntArray665[arg0]; k++) {
			float f3 = method541(arg0, k, arg1);
			float f4 = -2F * f3 * (float) Math.cos(method543(arg1, k, arg0));
			float f5 = f3 * f3;
			Class39.aFloatArrayArray669[arg0][k * 2 + 1] = Class39.aFloatArrayArray669[arg0][k * 2 - 1] * f5;
			Class39.aFloatArrayArray669[arg0][k * 2] = Class39.aFloatArrayArray669[arg0][k * 2 - 1] * f4 + Class39.aFloatArrayArray669[arg0][k * 2 - 2] * f5;
			for (int j1 = k * 2 - 1; j1 >= 2; j1--) {
				Class39.aFloatArrayArray669[arg0][j1] += Class39.aFloatArrayArray669[arg0][j1 - 1] * f4 + Class39.aFloatArrayArray669[arg0][j1 - 2] * f5;
			}
			Class39.aFloatArrayArray669[arg0][1] += Class39.aFloatArrayArray669[arg0][0] * f4 + f5;
			Class39.aFloatArrayArray669[arg0][0] += f4;
		}
		if (arg0 == 0) {
			for (int l = 0; l < anIntArray665[0] * 2; l++) {
				Class39.aFloatArrayArray669[0][l] *= Class39.aFloat671;
			}
		}
		for (int i1 = 0; i1 < anIntArray665[arg0] * 2; i1++) {
			Class39.anIntArrayArray670[arg0][i1] = (int) (Class39.aFloatArrayArray669[arg0][i1] * 65536F);
		}
		return anIntArray665[arg0] * 2;
	}

	public void method545(Stream buffer, Class29 class29) {
		int i = buffer.getUnsignedByte();
		anIntArray665[0] = i >> 4;
		anIntArray665[1] = i & 0xf;
		if (i != 0) {
			anIntArray668[0] = buffer.getUnsignedShort();
			anIntArray668[1] = buffer.getUnsignedShort();
			int j = buffer.getUnsignedByte();
			for (int k = 0; k < 2; k++) {
				for (int l = 0; l < anIntArray665[k]; l++) {
					anIntArrayArrayArray666[k][0][l] = buffer.getUnsignedShort();
					anIntArrayArrayArray667[k][0][l] = buffer.getUnsignedShort();
				}
			}
			for (int i1 = 0; i1 < 2; i1++) {
				for (int j1 = 0; j1 < anIntArray665[i1]; j1++) {
					if ((j & 1 << i1 * 4 << j1) != 0) {
						anIntArrayArrayArray666[i1][1][j1] = buffer.getUnsignedShort();
						anIntArrayArrayArray667[i1][1][j1] = buffer.getUnsignedShort();
					} else {
						anIntArrayArrayArray666[i1][1][j1] = anIntArrayArrayArray666[i1][0][j1];
						anIntArrayArrayArray667[i1][1][j1] = anIntArrayArrayArray667[i1][0][j1];
					}
				}
			}
			if (j != 0 || anIntArray668[1] != anIntArray668[0]) {
				class29.method326(buffer);
			}
		} else {
			anIntArray668[0] = anIntArray668[1] = 0;
		}
	}

	public Class39() {
		anIntArray665 = new int[2];
		anIntArrayArrayArray666 = new int[2][2][4];
		anIntArrayArrayArray667 = new int[2][2][4];
		anIntArray668 = new int[2];
	}
	final int[] anIntArray665;
	private final int[][][] anIntArrayArrayArray666;
	private final int[][][] anIntArrayArrayArray667;
	private final int[] anIntArray668;
	private static final float[][] aFloatArrayArray669 = new float[2][8];
	static final int[][] anIntArrayArray670 = new int[2][8];
	private static float aFloat671;
	static int anInt672;
}
