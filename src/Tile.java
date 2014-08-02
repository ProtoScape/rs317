/**
 * Tile [Class40]
 * A tile on the scene
 */
final class Tile {
	public Tile(int x, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, int arg8, int arg9, int arg10, int arg11, int arg12, int arg13, int arg14, int arg15, int arg16, int y, int arg18) {
		aBoolean683 = !(arg12 != arg11 || arg12 != arg3 || arg12 != arg10);
		anInt684 = arg13;
		anInt685 = arg6;
		anInt686 = arg8;
		anInt687 = arg18;
		char c = '\200';
		int i5 = c / 2;
		int j5 = c / 4;
		int k5 = c * 3 / 4;
		int ai[] = Tile.anIntArrayArray696[arg13];
		int l5 = ai.length;
		anIntArray673 = new int[l5];
		anIntArray674 = new int[l5];
		anIntArray675 = new int[l5];
		int ai1[] = new int[l5];
		int ai2[] = new int[l5];
		int i6 = y * c;
		int j6 = x * c;
		for (int k6 = 0; k6 < l5; k6++) {
			int l6 = ai[k6];
			if ((l6 & 1) == 0 && l6 <= 8) {
				l6 = (l6 - arg6 - arg6 - 1 & 7) + 1;
			}
			if (l6 > 8 && l6 <= 12) {
				l6 = (l6 - 9 - arg6 & 3) + 9;
			}
			if (l6 > 12 && l6 <= 16) {
				l6 = (l6 - 13 - arg6 & 3) + 13;
			}
			int i7;
			int k7;
			int i8;
			int k8;
			int j9;
			if (l6 == 1) {
				i7 = i6;
				k7 = j6;
				i8 = arg12;
				k8 = arg7;
				j9 = arg1;
			} else if (l6 == 2) {
				i7 = i6 + i5;
				k7 = j6;
				i8 = arg12 + arg11 >> 1;
				k8 = arg7 + arg16 >> 1;
				j9 = arg1 + arg15 >> 1;
			} else if (l6 == 3) {
				i7 = i6 + c;
				k7 = j6;
				i8 = arg11;
				k8 = arg16;
				j9 = arg15;
			} else if (l6 == 4) {
				i7 = i6 + c;
				k7 = j6 + i5;
				i8 = arg11 + arg3 >> 1;
				k8 = arg16 + arg9 >> 1;
				j9 = arg15 + arg5 >> 1;
			} else if (l6 == 5) {
				i7 = i6 + c;
				k7 = j6 + c;
				i8 = arg3;
				k8 = arg9;
				j9 = arg5;
			} else if (l6 == 6) {
				i7 = i6 + i5;
				k7 = j6 + c;
				i8 = arg3 + arg10 >> 1;
				k8 = arg9 + arg2 >> 1;
				j9 = arg5 + arg14 >> 1;
			} else if (l6 == 7) {
				i7 = i6;
				k7 = j6 + c;
				i8 = arg10;
				k8 = arg2;
				j9 = arg14;
			} else if (l6 == 8) {
				i7 = i6;
				k7 = j6 + i5;
				i8 = arg10 + arg12 >> 1;
				k8 = arg2 + arg7 >> 1;
				j9 = arg14 + arg1 >> 1;
			} else if (l6 == 9) {
				i7 = i6 + i5;
				k7 = j6 + j5;
				i8 = arg12 + arg11 >> 1;
				k8 = arg7 + arg16 >> 1;
				j9 = arg1 + arg15 >> 1;
			} else if (l6 == 10) {
				i7 = i6 + k5;
				k7 = j6 + i5;
				i8 = arg11 + arg3 >> 1;
				k8 = arg16 + arg9 >> 1;
				j9 = arg15 + arg5 >> 1;
			} else if (l6 == 11) {
				i7 = i6 + i5;
				k7 = j6 + k5;
				i8 = arg3 + arg10 >> 1;
				k8 = arg9 + arg2 >> 1;
				j9 = arg5 + arg14 >> 1;
			} else if (l6 == 12) {
				i7 = i6 + j5;
				k7 = j6 + i5;
				i8 = arg10 + arg12 >> 1;
				k8 = arg2 + arg7 >> 1;
				j9 = arg14 + arg1 >> 1;
			} else if (l6 == 13) {
				i7 = i6 + j5;
				k7 = j6 + j5;
				i8 = arg12;
				k8 = arg7;
				j9 = arg1;
			} else if (l6 == 14) {
				i7 = i6 + k5;
				k7 = j6 + j5;
				i8 = arg11;
				k8 = arg16;
				j9 = arg15;
			} else if (l6 == 15) {
				i7 = i6 + k5;
				k7 = j6 + k5;
				i8 = arg3;
				k8 = arg9;
				j9 = arg5;
			} else {
				i7 = i6 + j5;
				k7 = j6 + k5;
				i8 = arg10;
				k8 = arg2;
				j9 = arg14;
			}
			anIntArray673[k6] = i7;
			anIntArray674[k6] = i8;
			anIntArray675[k6] = k7;
			ai1[k6] = k8;
			ai2[k6] = j9;
		}
		int ai3[] = Tile.anIntArrayArray697[arg13];
		int j7 = ai3.length / 4;
		anIntArray679 = new int[j7];
		anIntArray680 = new int[j7];
		anIntArray681 = new int[j7];
		anIntArray676 = new int[j7];
		anIntArray677 = new int[j7];
		anIntArray678 = new int[j7];
		if (arg4 != -1) {
			anIntArray682 = new int[j7];
		}
		int l7 = 0;
		for (int j8 = 0; j8 < j7; j8++) {
			int l8 = ai3[l7];
			int k9 = ai3[l7 + 1];
			int i10 = ai3[l7 + 2];
			int k10 = ai3[l7 + 3];
			l7 += 4;
			if (k9 < 4) {
				k9 = k9 - arg6 & 3;
			}
			if (i10 < 4) {
				i10 = i10 - arg6 & 3;
			}
			if (k10 < 4) {
				k10 = k10 - arg6 & 3;
			}
			anIntArray679[j8] = k9;
			anIntArray680[j8] = i10;
			anIntArray681[j8] = k10;
			if (l8 == 0) {
				anIntArray676[j8] = ai1[k9];
				anIntArray677[j8] = ai1[i10];
				anIntArray678[j8] = ai1[k10];
				if (anIntArray682 != null) {
					anIntArray682[j8] = -1;
				}
			} else {
				anIntArray676[j8] = ai2[k9];
				anIntArray677[j8] = ai2[i10];
				anIntArray678[j8] = ai2[k10];
				if (anIntArray682 != null) {
					anIntArray682[j8] = arg4;
				}
			}
		}
		int i9 = arg12;
		int l9 = arg11;
		if (arg11 < i9) {
			i9 = arg11;
		}
		if (arg11 > l9) {
			l9 = arg11;
		}
		if (arg3 < i9) {
			i9 = arg3;
		}
		if (arg3 > l9) {
			l9 = arg3;
		}
		if (arg10 < i9) {
			i9 = arg10;
		}
		if (arg10 > l9) {
			l9 = arg10;
		}
		i9 /= 14;
		l9 /= 14;
	}
	final int[] anIntArray673;
	final int[] anIntArray674;
	final int[] anIntArray675;
	final int[] anIntArray676;
	final int[] anIntArray677;
	final int[] anIntArray678;
	final int[] anIntArray679;
	final int[] anIntArray680;
	final int[] anIntArray681;
	int anIntArray682[];
	final boolean aBoolean683;
	final int anInt684;
	final int anInt685;
	final int anInt686;
	final int anInt687;
	static final int[] anIntArray688 = new int[6];
	static final int[] anIntArray689 = new int[6];
	static final int[] anIntArray690 = new int[6];
	static final int[] anIntArray691 = new int[6];
	static final int[] anIntArray692 = new int[6];
	static final int[] anIntArray693 = { 1, 0 };
	static final int[] anIntArray694 = { 2, 1 };
	static final int[] anIntArray695 = { 3, 3 };
	private static final int[][] anIntArrayArray696 = { { 1, 3, 5, 7 }, { 1, 3, 5, 7 }, { 1, 3, 5, 7 }, { 1, 3, 5, 7, 6 }, { 1, 3, 5, 7, 6 }, { 1, 3, 5, 7, 6 }, { 1, 3, 5, 7, 6 }, { 1, 3, 5, 7, 2, 6 }, { 1, 3, 5, 7, 2, 8 }, { 1, 3, 5, 7, 2, 8 }, { 1, 3, 5, 7, 11, 12 }, { 1, 3, 5, 7, 11, 12 }, { 1, 3, 5, 7, 13, 14 } };
	private static final int[][] anIntArrayArray697 = { { 0, 1, 2, 3, 0, 0, 1, 3 }, { 1, 1, 2, 3, 1, 0, 1, 3 }, { 0, 1, 2, 3, 1, 0, 1, 3 }, { 0, 0, 1, 2, 0, 0, 2, 4, 1, 0, 4, 3 }, { 0, 0, 1, 4, 0, 0, 4, 3, 1, 1, 2, 4 }, { 0, 0, 4, 3, 1, 0, 1, 2, 1, 0, 2, 4 }, { 0, 1, 2, 4, 1, 0, 1, 4, 1, 0, 4, 3 }, { 0, 4, 1, 2, 0, 4, 2, 5, 1, 0, 4, 5, 1, 0, 5, 3 }, { 0, 4, 1, 2, 0, 4, 2, 3, 0, 4, 3, 5, 1, 0, 4, 5 }, { 0, 0, 4, 5, 1, 4, 1, 2, 1, 4, 2, 3, 1, 4, 3, 5 }, { 0, 0, 1, 5, 0, 1, 4, 5, 0, 1, 2, 4, 1, 0, 5, 3, 1, 5, 4, 3, 1, 4, 2, 3 }, { 1, 0, 1, 5, 1, 1, 4, 5, 1, 1, 2, 4, 0, 0, 5, 3, 0, 5, 4, 3, 0, 4, 2, 3 }, { 1, 0, 5, 4, 1, 0, 1, 5, 0, 0, 4, 3, 0, 4, 5, 3, 0, 5, 2, 3, 0, 1, 2, 5 } };
}
