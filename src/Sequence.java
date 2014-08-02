/**
 * Sequence [Animation]
 * Represents the sequence of an animation file.
 * The sequence consists of two arrays: frames and delays.
 * Also, the properties of the sequence are defined,
 * I.e. is the sequence walkable? Does it display a different weapon?
 */
public final class Sequence {

	public static void unpackConfig(Archive archive) {
		Stream stream = new Stream(archive.get("seq.dat"));
		int length = stream.getUnsignedShort();
		if (Sequence.sequenceCache == null) {
			Sequence.sequenceCache = new Sequence[length];
		}
		for (int i = 0; i < length; i++) {
			if (Sequence.sequenceCache[i] == null) {
				Sequence.sequenceCache[i] = new Sequence();
			}
			Sequence.sequenceCache[i].readValues(stream);
		}
	}

	public int getFrameLength(int arg0) {
		int i = delays[arg0];
		if (i == 0) {
			Animation animation = Animation.forId(frames[arg0]);
			if (animation != null) {
				i = delays[arg0] = animation.anInt636;
			}
		}
		if (i == 0) {
			i = 1;
		}
		return i;
	}

	private void readValues(Stream buffer) {
		do {
			int configId = buffer.getUnsignedByte();
			if (configId == 0) {
				break;
			}
			if (configId == 1) {
				length = buffer.getUnsignedByte();
				frames = new int[length];
				anIntArray354 = new int[length];
				delays = new int[length];
				for (int i = 0; i < length; i++) {
					frames[i] = buffer.getUnsignedShort();
					anIntArray354[i] = buffer.getUnsignedShort();
					if (anIntArray354[i] == 65535) {
						anIntArray354[i] = -1;
					}
					delays[i] = buffer.getUnsignedShort();
				}
			} else if (configId == 2) {
				frameStep = buffer.getUnsignedShort();
			} else if (configId == 3) {
				int len = buffer.getUnsignedByte();
				anIntArray357 = new int[len + 1];
				for (int i = 0; i < len; i++) {
					anIntArray357[i] = buffer.getUnsignedByte();
				}
				anIntArray357[len] = 0x98967f;
			} else if (configId == 4) {
				aBoolean358 = true;
			} else if (configId == 5) {
				anInt359 = buffer.getUnsignedByte();
			} else if (configId == 6) {
				shield = buffer.getUnsignedShort();
			} else if (configId == 7) {
				weapon = buffer.getUnsignedShort();
			} else if (configId == 8) {
				anInt362 = buffer.getUnsignedByte();
			} else if (configId == 9) {
				anInt363 = buffer.getUnsignedByte();
			} else if (configId == 10) {
				walkProperties = buffer.getUnsignedByte();
			} else if (configId == 11) {
				anInt365 = buffer.getUnsignedByte();
			} else if (configId == 12) {
				buffer.getInt();
			} else {
				System.out.println("Error unrecognised seq config code: " + configId);
			}
		} while (true);
		if (length == 0) {
			length = 1;
			frames = new int[1];
			frames[0] = -1;
			anIntArray354 = new int[1];
			anIntArray354[0] = -1;
			delays = new int[1];
			delays[0] = -1;
		}
		if (anInt363 == -1) {
			if (anIntArray357 != null) {
				anInt363 = 2;
			} else {
				anInt363 = 0;
			}
		}
		if (walkProperties == -1) {
			if (anIntArray357 != null) {
				walkProperties = 2;
				return;
			}
			walkProperties = 0;
		}
	}

	private Sequence() {
		frameStep = -1;
		aBoolean358 = false;
		anInt359 = 5;
		shield = -1;
		weapon = -1;
		anInt362 = 99;
		anInt363 = -1;
		walkProperties = -1;
		anInt365 = 2;
	}
	public static Sequence sequenceCache[];
	public int length; // anInt352
	public int frames[]; // anIntArray353
	public int anIntArray354[];
	private int[] delays; // anIntArray355
	public int frameStep; // anInt356
	public int anIntArray357[];
	public boolean aBoolean358;
	public int anInt359;
	public int shield; // anInt360
	public int weapon; // anInt361
	public int anInt362;
	public int anInt363;
	public int walkProperties; // anInt364
	public int anInt365;
	public static int anInt367;
}
