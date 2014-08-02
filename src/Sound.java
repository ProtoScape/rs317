final class Sound {
	private Sound() {
		soundtracks = new Soundtrack[10];
	}

	public static void unpack(Stream buffer) {
		Sound.aByteArray327 = new byte[0x6baa8];
		Sound.buffer = new Stream(Sound.aByteArray327);
		Soundtrack.method166();
		do {
			int j = buffer.getUnsignedShort();
			if (j == 65535) {
				return;
			}
			Sound.sound[j] = new Sound();
			Sound.sound[j].method242(buffer);
			Sound.anIntArray326[j] = Sound.sound[j].method243();
		} while (true);
	}

	public static Stream method241(int arg0, int arg1) {
		if (Sound.sound[arg1] != null) {
			Sound sound = Sound.sound[arg1];
			return sound.method244(arg0);
		} else {
			return null;
		}
	}

	private void method242(Stream buffer) {
		for (int i = 0; i < 10; i++) {
			int j = buffer.getUnsignedByte();
			if (j != 0) {
				buffer.offset--;
				soundtracks[i] = new Soundtrack();
				soundtracks[i].method169(buffer);
			}
		}
		anInt330 = buffer.getUnsignedShort();
		anInt331 = buffer.getUnsignedShort();
	}

	private int method243() {
		int j = 0x98967f;
		for (int k = 0; k < 10; k++) {
			if (soundtracks[k] != null && soundtracks[k].anInt114 / 20 < j) {
				j = soundtracks[k].anInt114 / 20;
			}
		}
		if (anInt330 < anInt331 && anInt330 / 20 < j) {
			j = anInt330 / 20;
		}
		if (j == 0x98967f || j == 0) {
			return 0;
		}
		for (int l = 0; l < 10; l++) {
			if (soundtracks[l] != null) {
				soundtracks[l].anInt114 -= j * 20;
			}
		}
		if (anInt330 < anInt331) {
			anInt330 -= j * 20;
			anInt331 -= j * 20;
		}
		return j;
	}

	private Stream method244(int arg0) {
		int k = method245(arg0);
		Sound.buffer.offset = 0;
		Sound.buffer.writeInt(0x52494646);
		Sound.buffer.writeLEInt(36 + k);
		Sound.buffer.writeInt(0x57415645);
		Sound.buffer.writeInt(0x666d7420);
		Sound.buffer.writeLEInt(16);
		Sound.buffer.writeLEShort(1);
		Sound.buffer.writeLEShort(1);
		Sound.buffer.writeLEInt(22050);
		Sound.buffer.writeLEInt(22050);
		Sound.buffer.writeLEShort(1);
		Sound.buffer.writeLEShort(8);
		Sound.buffer.writeInt(0x64617461);
		Sound.buffer.writeLEInt(k);
		Sound.buffer.offset += k;
		return Sound.buffer;
	}

	private int method245(int arg0) {
		int j = 0;
		for (int k = 0; k < 10; k++) {
			if (soundtracks[k] != null && soundtracks[k].length + soundtracks[k].anInt114 > j) {
				j = soundtracks[k].length + soundtracks[k].anInt114;
			}
		}
		if (j == 0) {
			return 0;
		}
		int l = 22050 * j / 1000;
		int i1 = 22050 * anInt330 / 1000;
		int j1 = 22050 * anInt331 / 1000;
		if (i1 < 0 || i1 > l || j1 < 0 || j1 > l || i1 >= j1) {
			arg0 = 0;
		}
		int k1 = l + (j1 - i1) * (arg0 - 1);
		for (int l1 = 44; l1 < k1 + 44; l1++) {
			Sound.aByteArray327[l1] = -128;
		}
		for (int i2 = 0; i2 < 10; i2++) {
			if (soundtracks[i2] != null) {
				int j2 = soundtracks[i2].length * 22050 / 1000;
				int i3 = soundtracks[i2].anInt114 * 22050 / 1000;
				int ai[] = soundtracks[i2].method167(j2, soundtracks[i2].length);
				for (int l3 = 0; l3 < j2; l3++) {
					Sound.aByteArray327[l3 + i3 + 44] += (byte) (ai[l3] >> 8);
				}
			}
		}
		if (arg0 > 1) {
			i1 += 44;
			j1 += 44;
			l += 44;
			int k2 = (k1 += 44) - l;
			for (int j3 = l - 1; j3 >= j1; j3--) {
				Sound.aByteArray327[j3 + k2] = Sound.aByteArray327[j3];
			}
			for (int k3 = 1; k3 < arg0; k3++) {
				int l2 = (j1 - i1) * k3;
				System.arraycopy(Sound.aByteArray327, i1, Sound.aByteArray327, i1 + l2, j1 - i1);
			}
			k1 -= 44;
		}
		return k1;
	}
	private static final Sound[] sound = new Sound[5000];
	public static final int[] anIntArray326 = new int[5000];
	private static byte[] aByteArray327;
	private static Stream buffer;
	private final Soundtrack[] soundtracks;
	private int anInt330;
	private int anInt331;
}
