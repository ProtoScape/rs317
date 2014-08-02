/**
 * SpotAnimation [SpotAnim]
 * Represents a graphic that is not part of the scene.
 */
public final class SpotAnimation {

	public static void unpackConfig(Archive container) {
		Stream buffer = new Stream(container.get("spotanim.dat"));
		int len = buffer.getUnsignedShort();
		if (SpotAnimation.spotAnimationCache == null) {
			SpotAnimation.spotAnimationCache = new SpotAnimation[len];
		}
		for (int i = 0; i < len; i++) {
			if (SpotAnimation.spotAnimationCache[i] == null) {
				SpotAnimation.spotAnimationCache[i] = new SpotAnimation();
			}
			SpotAnimation.spotAnimationCache[i].id = i;
			SpotAnimation.spotAnimationCache[i].readValues(buffer);
		}
	}

	private void readValues(Stream buffer) {
		do {
			int i = buffer.getUnsignedByte();
			if (i == 0) {
				return;
			}
			if (i == 1) {
				modelId = buffer.getUnsignedShort();
			} else if (i == 2) {
				sequenceId = buffer.getUnsignedShort();
				if (Sequence.sequenceCache != null) {
					sequence = Sequence.sequenceCache[sequenceId];
				}
			} else if (i == 4) {
				scaleXY = buffer.getUnsignedShort();
			} else if (i == 5) {
				scaleZ = buffer.getUnsignedShort();
			} else if (i == 6) {
				rotation = buffer.getUnsignedShort();
			} else if (i == 7) {
				lightness = buffer.getUnsignedByte();
			} else if (i == 8) {
				shading = buffer.getUnsignedByte();
			} else if (i >= 40 && i < 50) {
				srcColors[i - 40] = buffer.getUnsignedShort();
			} else if (i >= 50 && i < 60) {
				destColors[i - 50] = buffer.getUnsignedShort();
			} else {
				System.out.println("Error unrecognised spotanim config code: " + i);
			}
		} while (true);
	}

	public Model getModel() {
		Model model = (Model) SpotAnimation.cache.get(id);
		if (model != null) {
			return model;
		}
		model = Model.getModel(modelId);
		if (model == null) {
			return null;
		}
		for (int i = 0; i < 6; i++) {
			if (srcColors[0] != 0) {
				model.swapColors(srcColors[i], destColors[i]);
			}
		}
		SpotAnimation.cache.put(model, id);
		return model;
	}

	private SpotAnimation() {
		sequenceId = -1;
		srcColors = new int[6];
		destColors = new int[6];
		scaleXY = 128;
		scaleZ = 128;
	}
	public static SpotAnimation spotAnimationCache[];
	private int id; // anInt404
	private int modelId; // anInt405
	private int sequenceId; // anInt406
	public Sequence sequence;
	private final int[] srcColors; // anIntArray408
	private final int[] destColors; // anIntArray409
	public int scaleXY; // anInt410
	public int scaleZ; // anInt411
	public int rotation; // anInt410
	public int lightness; // anInt410
	public int shading; // anInt410
	public static Cache cache = new Cache(30);
}
