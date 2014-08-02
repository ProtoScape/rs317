/**
 * AnimatedObject [Animable_Sub5]
 * Represents an object that has its own animation sequence
 */
final class AnimatedObject extends Entity {
	@Override
	public Model getRotatedModel() {
		int j = -1;
		if (sequence != null) {
			int k = Client.loopCycle - anInt1608;
			if (k > 100 && sequence.frameStep > 0) {
				k = 100;
			}
			while (k > sequence.getFrameLength(anInt1599)) {
				k -= sequence.getFrameLength(anInt1599);
				anInt1599++;
				if (anInt1599 < sequence.length) {
					continue;
				}
				anInt1599 -= sequence.frameStep;
				if (anInt1599 >= 0 && anInt1599 < sequence.length) {
					continue;
				}
				sequence = null;
				break;
			}
			anInt1608 = Client.loopCycle - k;
			if (sequence != null) {
				j = sequence.frames[anInt1599];
			}
		}
		ObjectDefinition objectDefinition;
		if (childIds != null) {
			objectDefinition = method457();
		} else {
			objectDefinition = ObjectDefinition.forId(id);
		}
		if (objectDefinition == null) {
			return null;
		} else {
			return objectDefinition.method578(type, rotation, pos, posxplus1, posxplus1yplus1, posyplus1, j);
		}
	}

	private ObjectDefinition method457() {
		int i = -1;
		if (varbitFileId != -1) {
			VarBit varBit = VarBit.cache[varbitFileId];
			int configId = varBit.configId;
			int shift = varBit.anInt649;
			int amountBits = varBit.anInt650;
			int j1 = Client.anIntArray1232[amountBits - shift];
			i = client.configStates[configId] >> shift & j1;
		} else if (anInt1602 != -1) {
			i = client.configStates[anInt1602];
		}
		if (i < 0 || i >= childIds.length || childIds[i] == -1) {
			return null;
		} else {
			return ObjectDefinition.forId(childIds[i]);
		}
	}

	public AnimatedObject(int id, int rotation, int type, int posxplus1, int posxplus1yplus1, int pos, int posyplus1, int animationId, boolean flag) {
		this.id = id;
		this.type = type;
		this.rotation = rotation;
		this.pos = pos;
		this.posxplus1 = posxplus1;
		this.posxplus1yplus1 = posxplus1yplus1;
		this.posyplus1 = posyplus1;
		if (animationId != -1) {
			sequence = Sequence.sequenceCache[animationId];
			anInt1599 = 0;
			anInt1608 = Client.loopCycle;
			if (flag && sequence.frameStep != -1) {
				anInt1599 = (int) (Math.random() * sequence.length);
				anInt1608 -= (int) (Math.random() * sequence.getFrameLength(anInt1599));
			}
		}
		ObjectDefinition objectDefinition = ObjectDefinition.forId(id);
		varbitFileId = objectDefinition.varbitFileId;
		anInt1602 = objectDefinition.anInt749;
		childIds = objectDefinition.childIds;
	}
	private int anInt1599;
	private final int[] childIds;
	private final int varbitFileId;
	private final int anInt1602;
	private final int pos;
	private final int posxplus1;
	private final int posxplus1yplus1;
	private final int posyplus1;
	private Sequence sequence;
	private int anInt1608;
	public static Client client;
	private final int id;
	private final int type;
	private final int rotation;
}
