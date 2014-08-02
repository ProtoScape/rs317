/**
 * Animation [Class36]
 * Represents the configuration for a single animation sequence
 */
public final class Animation {
	public static void unpackConfig(int id) {
		Animation.animationCache = new Animation[id + 1];
		Animation.aBooleanArray643 = new boolean[id + 1];
		for (int j = 0; j < id + 1; j++) {
			Animation.aBooleanArray643[j] = true;
		}
	}

	public static void method529(byte buf[]) {
		Stream stream = new Stream(buf);
		stream.offset = buf.length - 8;
		int i = stream.getUnsignedShort();
		int j = stream.getUnsignedShort();
		int k = stream.getUnsignedShort();
		int l = stream.getUnsignedShort();
		int i1 = 0;
		Stream stream_1 = new Stream(buf);
		stream_1.offset = i1;
		i1 += i + 2;
		Stream stream_2 = new Stream(buf);
		stream_2.offset = i1;
		i1 += j;
		Stream stream_3 = new Stream(buf);
		stream_3.offset = i1;
		i1 += k;
		Stream stream_4 = new Stream(buf);
		stream_4.offset = i1;
		i1 += l;
		Stream stream_5 = new Stream(buf);
		stream_5.offset = i1;
		SkinList skinList = new SkinList(stream_5);
		int k1 = stream_1.getUnsignedShort();
		int ai[] = new int[500];
		int ai1[] = new int[500];
		int ai2[] = new int[500];
		int ai3[] = new int[500];
		for (int l1 = 0; l1 < k1; l1++) {
			int i2 = stream_1.getUnsignedShort();
			Animation animation = Animation.animationCache[i2] = new Animation();
			animation.anInt636 = stream_4.getUnsignedByte();
			animation.aSkinList_637 = skinList;
			int j2 = stream_1.getUnsignedByte();
			int k2 = -1;
			int l2 = 0;
			for (int i3 = 0; i3 < j2; i3++) {
				int j3 = stream_2.getUnsignedByte();
				if (j3 > 0) {
					if (skinList.opcodes[i3] != 0) {
						for (int l3 = i3 - 1; l3 > k2; l3--) {
							if (skinList.opcodes[l3] != 0) {
								continue;
							}
							ai[l2] = l3;
							ai1[l2] = 0;
							ai2[l2] = 0;
							ai3[l2] = 0;
							l2++;
							break;
						}
					}
					ai[l2] = i3;
					char c = '\0';
					if (skinList.opcodes[i3] == 3) {
						c = '\200';
					}
					if ((j3 & 1) != 0) {
						ai1[l2] = stream_3.getUnsignedSmart();
					} else {
						ai1[l2] = c;
					}
					if ((j3 & 2) != 0) {
						ai2[l2] = stream_3.getUnsignedSmart();
					} else {
						ai2[l2] = c;
					}
					if ((j3 & 4) != 0) {
						ai3[l2] = stream_3.getUnsignedSmart();
					} else {
						ai3[l2] = c;
					}
					k2 = i3;
					l2++;
					if (skinList.opcodes[i3] == 5) {
						Animation.aBooleanArray643[i2] = false;
					}
				}
			}
			animation.anInt638 = l2;
			animation.opcodes = new int[l2];
			animation.mod1 = new int[l2];
			animation.mod2 = new int[l2];
			animation.mod3 = new int[l2];
			for (int k3 = 0; k3 < l2; k3++) {
				animation.opcodes[k3] = ai[k3];
				animation.mod1[k3] = ai1[k3];
				animation.mod2[k3] = ai2[k3];
				animation.mod3[k3] = ai3[k3];
			}
		}
	}

	public static void dispose() {
		Animation.animationCache = null;
	}

	public static Animation forId(int id) {
		if (Animation.animationCache == null) {
			return null;
		} else {
			return Animation.animationCache[id];
		}
	}

	public static boolean isNullFrame(int arg0) {
		return arg0 == -1;
	}

	private Animation() {
	}
	private static Animation[] animationCache;
	public int anInt636;
	public SkinList aSkinList_637;
	public int anInt638;
	public int opcodes[];
	public int mod1[];
	public int mod2[];
	public int mod3[];
	private static boolean[] aBooleanArray643;
}
