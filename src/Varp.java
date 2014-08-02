public final class Varp {
	public static void unpackConfig(Archive container) {
		Stream stream = new Stream(container.get("varp.dat"));
		Varp.anInt702 = 0;
		int cacheSize = stream.getUnsignedShort();
		if (Varp.cache == null) {
			Varp.cache = new Varp[cacheSize];
		}
		if (Varp.anIntArray703 == null) {
			Varp.anIntArray703 = new int[cacheSize];
		}
		for (int i = 0; i < cacheSize; i++) {
			if (Varp.cache[i] == null) {
				Varp.cache[i] = new Varp();
			}
			Varp.cache[i].readValues(stream, i);
		}
		if (stream.offset != stream.payload.length) {
			System.out.println("varptype load mismatch");
		}
	}

	private void readValues(Stream buffer, int i) {
		do {
			int j = buffer.getUnsignedByte();
			if (j == 0) {
				return;
			}
			if (j == 1) {
				buffer.getUnsignedByte();
			} else if (j == 2) {
				buffer.getUnsignedByte();
			} else if (j == 3) {
				Varp.anIntArray703[Varp.anInt702++] = i;
			} else if (j == 4) {
			} else if (j == 5) {
				anInt709 = buffer.getUnsignedShort();
			} else if (j == 6) {
			} else if (j == 7) {
				buffer.getInt();
			} else if (j == 8) {
				aBoolean713 = true;
			} else if (j == 10) {
				buffer.getString();
			} else if (j == 11) {
				aBoolean713 = true;
			} else if (j == 12) {
				buffer.getInt();
			} else if (j == 13) {
			} else {
				System.out.println("Error unrecognised config code: " + j);
			}
		} while (true);
	}

	private Varp() {
		aBoolean713 = false;
	}
	public static Varp cache[];
	private static int anInt702;
	private static int[] anIntArray703;
	public int anInt709;
	public boolean aBoolean713;
}
