/**
 * Floor [Flo]
 * Contains the config for floors
 */
public final class Floor {

	public static void unpackConfig(Archive archive) {
		Stream stream = new Stream(archive.get("flo.dat"));
		int cacheSize = stream.getUnsignedShort();
		if (Floor.cachedFloors == null) {
			Floor.cachedFloors = new Floor[cacheSize];
		}
		for (int i = 0; i < cacheSize; i++) {
			if (Floor.cachedFloors[i] == null) {
				Floor.cachedFloors[i] = new Floor();
			}
			Floor.cachedFloors[i].readValues(stream);
		}
	}

	private void readValues(Stream buffer) {
		do {
			int configId = buffer.getUnsignedByte();
			if (configId == 0) {
				return;
			} else if (configId == 1) {
				anInt390 = buffer.get24BitInt();
				method262(anInt390);
			} else if (configId == 2) {
				anInt391 = buffer.getUnsignedByte();
			} else if (configId == 3) {
			} else if (configId == 5) {
				occlude = false;
			} else if (configId == 6) {
				buffer.getString();
			} else if (configId == 7) {
				int j = anInt394;
				int k = anInt395;
				int l = anInt396;
				int i1 = anInt397;
				int j1 = buffer.get24BitInt();
				method262(j1);
				anInt394 = j;
				anInt395 = k;
				anInt396 = l;
				anInt397 = i1;
				anInt398 = i1;
			} else {
				System.out.println("Error unrecognised config code: " + configId);
			}
		} while (true);
	}

	private void method262(int rgb) {
		double red = (rgb >> 16 & 0xff) / 256D;
		double green = (rgb >> 8 & 0xff) / 256D;
		double blue = (rgb & 0xff) / 256D;
		double d3 = red;
		if (green < d3) {
			d3 = green;
		}
		if (blue < d3) {
			d3 = blue;
		}
		double d4 = red;
		if (green > d4) {
			d4 = green;
		}
		if (blue > d4) {
			d4 = blue;
		}
		double d5 = 0.0D;
		double d6 = 0.0D;
		double d7 = (d3 + d4) / 2D;
		if (d3 != d4) {
			if (d7 < 0.5D) {
				d6 = (d4 - d3) / (d4 + d3);
			}
			if (d7 >= 0.5D) {
				d6 = (d4 - d3) / (2D - d4 - d3);
			}
			if (red == d4) {
				d5 = (green - blue) / (d4 - d3);
			} else if (green == d4) {
				d5 = 2D + (blue - red) / (d4 - d3);
			} else if (blue == d4) {
				d5 = 4D + (red - green) / (d4 - d3);
			}
		}
		d5 /= 6D;
		anInt394 = (int) (d5 * 256D);
		anInt395 = (int) (d6 * 256D);
		anInt396 = (int) (d7 * 256D);
		if (anInt395 < 0) {
			anInt395 = 0;
		} else if (anInt395 > 255) {
			anInt395 = 255;
		}
		if (anInt396 < 0) {
			anInt396 = 0;
		} else if (anInt396 > 255) {
			anInt396 = 255;
		}
		if (d7 > 0.5D) {
			anInt398 = (int) ((1.0D - d7) * d6 * 512D);
		} else {
			anInt398 = (int) (d7 * d6 * 512D);
		}
		if (anInt398 < 1) {
			anInt398 = 1;
		}
		anInt397 = (int) (d5 * anInt398);
		int k = anInt394 + (int) (Math.random() * 16D) - 8;
		if (k < 0) {
			k = 0;
		} else if (k > 255) {
			k = 255;
		}
		int l = anInt395 + (int) (Math.random() * 48D) - 24;
		if (l < 0) {
			l = 0;
		} else if (l > 255) {
			l = 255;
		}
		int i1 = anInt396 + (int) (Math.random() * 48D) - 24;
		if (i1 < 0) {
			i1 = 0;
		} else if (i1 > 255) {
			i1 = 255;
		}
		anInt399 = method263(k, l, i1);
	}

	private int method263(int arg0, int arg1, int arg2) {
		if (arg2 > 179) {
			arg1 /= 2;
		}
		if (arg2 > 192) {
			arg1 /= 2;
		}
		if (arg2 > 217) {
			arg1 /= 2;
		}
		if (arg2 > 243) {
			arg1 /= 2;
		}
		return (arg0 / 4 << 10) + (arg1 / 32 << 7) + arg2 / 2;
	}

	private Floor() {
		anInt391 = -1;
		occlude = true;
	}
	public static Floor cachedFloors[];
	public int anInt390;
	public int anInt391;
	public boolean occlude; // aBoolean393
	public int anInt394;
	public int anInt395;
	public int anInt396;
	public int anInt397;
	public int anInt398;
	public int anInt399;
}
