/**
 * IdentityKit [IDK]
 * Contains the config for a character's clothes and appearance
 */
public final class IdentityKit {
	public static void unpackConfig(Archive archive) {
		Stream stream = new Stream(archive.get("idk.dat"));
		IdentityKit.length = stream.getUnsignedShort();
		if (IdentityKit.designCache == null) {
			IdentityKit.designCache = new IdentityKit[IdentityKit.length];
		}
		for (int j = 0; j < IdentityKit.length; j++) {
			if (IdentityKit.designCache[j] == null) {
				IdentityKit.designCache[j] = new IdentityKit();
			}
			IdentityKit.designCache[j].readValues(stream);
		}
	}

	private void readValues(Stream buffer) {
		do {
			int i = buffer.getUnsignedByte();
			if (i == 0) {
				return;
			}
			if (i == 1) {
				anInt657 = buffer.getUnsignedByte();
			} else if (i == 2) {
				int j = buffer.getUnsignedByte();
				anIntArray658 = new int[j];
				for (int k = 0; k < j; k++) {
					anIntArray658[k] = buffer.getUnsignedShort();
				}
			} else if (i == 3) {
				aBoolean662 = true;
			} else if (i >= 40 && i < 50) {
				anIntArray659[i - 40] = buffer.getUnsignedShort();
			} else if (i >= 50 && i < 60) {
				anIntArray660[i - 50] = buffer.getUnsignedShort();
			} else if (i >= 60 && i < 70) {
				anIntArray661[i - 60] = buffer.getUnsignedShort();
			} else {
				System.out.println("Error unrecognised config code: " + i);
			}
		} while (true);
	}

	public boolean method537() {
		if (anIntArray658 == null) {
			return true;
		}
		boolean flag = true;
		for (int j = 0; j < anIntArray658.length; j++) {
			if (!Model.isCached(anIntArray658[j])) {
				flag = false;
			}
		}
		return flag;
	}

	public Model method538() {
		if (anIntArray658 == null) {
			return null;
		}
		Model models[] = new Model[anIntArray658.length];
		for (int i = 0; i < anIntArray658.length; i++) {
			models[i] = Model.getModel(anIntArray658[i]);
		}
		Model model;
		if (models.length == 1) {
			model = models[0];
		} else {
			model = new Model(models.length, models);
		}
		for (int j = 0; j < 6; j++) {
			if (anIntArray659[j] == 0) {
				break;
			}
			model.swapColors(anIntArray659[j], anIntArray660[j]);
		}
		return model;
	}

	public boolean method539() {
		boolean flag1 = true;
		for (int i = 0; i < 5; i++) {
			if (anIntArray661[i] != -1 && !Model.isCached(anIntArray661[i])) {
				flag1 = false;
			}
		}
		return flag1;
	}

	public Model method540() {
		Model models[] = new Model[5];
		int j = 0;
		for (int k = 0; k < 5; k++) {
			if (anIntArray661[k] != -1) {
				models[j++] = Model.getModel(anIntArray661[k]);
			}
		}
		Model model = new Model(j, models);
		for (int l = 0; l < 6; l++) {
			if (anIntArray659[l] == 0) {
				break;
			}
			model.swapColors(anIntArray659[l], anIntArray660[l]);
		}
		return model;
	}

	private IdentityKit() {
		anInt657 = -1;
		anIntArray659 = new int[6];
		anIntArray660 = new int[6];
		aBoolean662 = false;
	}
	public static int length;
	public static IdentityKit designCache[];
	public int anInt657;
	private int[] anIntArray658;
	private final int[] anIntArray659;
	private final int[] anIntArray660;
	private final int[] anIntArray661 = { -1, -1, -1, -1, -1 };
	public boolean aBoolean662;
}
