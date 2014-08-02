public final class VarBit {
	public static void unpackConfig(Archive container) {
		Stream stream = new Stream(container.get("varbit.dat"));
		int cacheSize = stream.getUnsignedShort();
		if (VarBit.cache == null) {
			VarBit.cache = new VarBit[cacheSize];
		}
		for (int i = 0; i < cacheSize; i++) {
			if (VarBit.cache[i] == null) {
				VarBit.cache[i] = new VarBit();
			}
			VarBit.cache[i].readValues(stream);
			if (VarBit.cache[i].aBoolean651) {
				Varp.cache[VarBit.cache[i].configId].aBoolean713 = true;
			}
		}
		if (stream.offset != stream.payload.length) {
			System.out.println("varbit load mismatch");
		}
	}

	private void readValues(Stream buffer) {
		do {
			int i = buffer.getUnsignedByte();
			if (i == 0) {
				return;
			}
			if (i == 1) {
				configId = buffer.getUnsignedShort();
				anInt649 = buffer.getUnsignedByte();
				anInt650 = buffer.getUnsignedByte();
			} else if (i == 10) {
				buffer.getString();
			} else if (i == 2) {
				aBoolean651 = true;
			} else if (i == 3) {
				buffer.getInt();
			} else if (i == 4) {
				buffer.getInt();
			} else {
				System.out.println("Error unrecognised config code: " + i);
			}
		} while (true);
	}

	private VarBit() {
		aBoolean651 = false;
	}
	public static VarBit cache[];
	public int configId; // anInt648
	public int anInt649;
	public int anInt650;
	private boolean aBoolean651;
}
