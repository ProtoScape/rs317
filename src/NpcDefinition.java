/**
 * NpcDefinition [EntityDef]
 * Handles the config of npcs
 */
public final class NpcDefinition {

	public static NpcDefinition forId(int id) {
		for (int i = 0; i < 20; i++) {
			if (NpcDefinition.cache[i].type == id) {
				return NpcDefinition.cache[i];
			}
		}
		NpcDefinition.totalNpcs = (NpcDefinition.totalNpcs + 1) % 20;
		NpcDefinition npc = NpcDefinition.cache[NpcDefinition.totalNpcs] = new NpcDefinition();
		NpcDefinition.buffer.offset = NpcDefinition.npcIndices[id];
		npc.type = id;
		npc.readValues(NpcDefinition.buffer);
		return npc;
	}

	public Model method160() {
		if (childIds != null) {
			NpcDefinition npcDefinition = method161();
			if (npcDefinition == null) {
				return null;
			} else {
				return npcDefinition.method160();
			}
		}
		if (dialogueModels == null) {
			return null;
		}
		boolean flag1 = false;
		for (int i = 0; i < dialogueModels.length; i++) {
			if (!Model.isCached(dialogueModels[i])) {
				flag1 = true;
			}
		}
		if (flag1) {
			return null;
		}
		Model models[] = new Model[dialogueModels.length];
		for (int id = 0; id < dialogueModels.length; id++) {
			models[id] = Model.getModel(dialogueModels[id]);
		}
		Model model;
		if (models.length == 1) {
			model = models[0];
		} else {
			model = new Model(models.length, models);
		}
		if (srcColors != null) {
			for (int k = 0; k < srcColors.length; k++) {
				model.swapColors(srcColors[k], destColors[k]);
			}
		}
		return model;
	}

	public NpcDefinition method161() {
		int j = -1;
		if (varbitId != -1) {
			VarBit varBit = VarBit.cache[varbitId];
			int configId = varBit.configId;
			int shift = varBit.anInt649;
			int amountBits = varBit.anInt650;
			int j1 = Client.anIntArray1232[amountBits - shift];
			j = client.configStates[configId] >> shift & j1;
		} else if (actionId != -1) {
			j = client.configStates[actionId];
		}
		if (j < 0 || j >= childIds.length || childIds[j] == -1) {
			return null;
		} else {
			return NpcDefinition.forId(childIds[j]);
		}
	}

	public static void unpackConfig(Archive container) {
		NpcDefinition.buffer = new Stream(container.get("npc.dat"));
		Stream stream2 = new Stream(container.get("npc.idx"));
		int totalNPCs = stream2.getUnsignedShort();
		NpcDefinition.npcIndices = new int[totalNPCs];
		int i = 2;
		for (int j = 0; j < totalNPCs; j++) {
			NpcDefinition.npcIndices[j] = i;
			i += stream2.getUnsignedShort();
		}
		NpcDefinition.cache = new NpcDefinition[20];
		for (int k = 0; k < 20; k++) {
			NpcDefinition.cache[k] = new NpcDefinition();
		}
	}

	public static void dispose() {
		NpcDefinition.mruNodes = null;
		NpcDefinition.npcIndices = null;
		NpcDefinition.cache = null;
		NpcDefinition.buffer = null;
	}

	public Model method164(int arg0, int arg1, int arg2[]) {
		if (childIds != null) {
			NpcDefinition npcDefinition = method161();
			if (npcDefinition == null) {
				return null;
			} else {
				return npcDefinition.method164(arg0, arg1, arg2);
			}
		}
		Model model = (Model) NpcDefinition.mruNodes.get(type);
		if (model == null) {
			boolean flag = false;
			for (int i1 = 0; i1 < models.length; i1++) {
				if (!Model.isCached(models[i1])) {
					flag = true;
				}
			}
			if (flag) {
				return null;
			}
			Model npcModels[] = new Model[models.length];
			for (int id = 0; id < models.length; id++) {
				npcModels[id] = Model.getModel(models[id]);
			}
			if (npcModels.length == 1) {
				model = npcModels[0];
			} else {
				model = new Model(npcModels.length, npcModels);
			}
			if (srcColors != null) {
				for (int k1 = 0; k1 < srcColors.length; k1++) {
					model.swapColors(srcColors[k1], destColors[k1]);
				}
			}
			model.skin();
			model.processLighting(64 + lightness, 850 + shading, -30, -50, -30, true);
			NpcDefinition.mruNodes.put(model, type);
		}
		Model transformedModel = Model.aModel_1621;
		transformedModel.setTransparency(model, Animation.isNullFrame(arg1) & Animation.isNullFrame(arg0));
		if (arg1 != -1 && arg0 != -1) {
			transformedModel.pushFrames(arg2, arg0, arg1);
		} else if (arg1 != -1) {
			transformedModel.transform(arg1);
		}
		if (scaleXY != 128 || scaleZ != 128) {
			transformedModel.scale(scaleXY, scaleXY, scaleZ);
		}
		transformedModel.getDefaultDiagonals();
		transformedModel.triangleSkin = null;
		transformedModel.vertexSkins = null;
		if (size == 1) {
			transformedModel.aBoolean1659 = true;
		}
		return transformedModel;
	}

	private void readValues(Stream buffer) {
		do {
			int configId = buffer.getUnsignedByte();
			if (configId == 0) {
				return;
			}
			if (configId == 1) {
				int total = buffer.getUnsignedByte();
				models = new int[total];
				for (int i = 0; i < total; i++) {
					models[i] = buffer.getUnsignedShort();
				}
			} else if (configId == 2) {
				name = buffer.getString();
			} else if (configId == 3) {
				description = buffer.getStringBytes();
			} else if (configId == 12) {
				size = buffer.getByte();
			} else if (configId == 13) {
				idleAnim = buffer.getUnsignedShort();
			} else if (configId == 14) {
				walkAnim = buffer.getUnsignedShort();
			} else if (configId == 17) {
				walkAnim = buffer.getUnsignedShort();
				retreatAnim = buffer.getUnsignedShort();
				turnRightAnim = buffer.getUnsignedShort();
				turnLeftAnim = buffer.getUnsignedShort();
			} else if (configId >= 30 && configId < 40) {
				if (actions == null) {
					actions = new String[5];
				}
				actions[configId - 30] = buffer.getString();
				if (actions[configId - 30].equalsIgnoreCase("hidden")) {
					actions[configId - 30] = null;
				}
			} else if (configId == 40) {
				int total = buffer.getUnsignedByte();
				srcColors = new int[total];
				destColors = new int[total];
				for (int i = 0; i < total; i++) {
					srcColors[i] = buffer.getUnsignedShort();
					destColors[i] = buffer.getUnsignedShort();
				}
			} else if (configId == 60) {
				int total = buffer.getUnsignedByte();
				dialogueModels = new int[total];
				for (int i = 0; i < total; i++) {
					dialogueModels[i] = buffer.getUnsignedShort();
				}
			} else if (configId == 90) {
				buffer.getUnsignedShort();
			} else if (configId == 91) {
				buffer.getUnsignedShort();
			} else if (configId == 92) {
				buffer.getUnsignedShort();
			} else if (configId == 93) {
				visibleOnMinimap = false;
			} else if (configId == 95) {
				combatLevel = buffer.getUnsignedShort();
			} else if (configId == 97) {
				scaleXY = buffer.getUnsignedShort();
			} else if (configId == 98) {
				scaleZ = buffer.getUnsignedShort();
			} else if (configId == 99) {
				members = true;
			} else if (configId == 100) {
				lightness = buffer.getByte();
			} else if (configId == 101) {
				shading = buffer.getByte() * 5;
			} else if (configId == 102) {
				headIcon = buffer.getUnsignedShort();
			} else if (configId == 103) {
				turnAmount = buffer.getUnsignedShort();
			} else if (configId == 106) {
				varbitId = buffer.getUnsignedShort();
				if (varbitId == 65535) {
					varbitId = -1;
				}
				actionId = buffer.getUnsignedShort();
				if (actionId == 65535) {
					actionId = -1;
				}
				int id = buffer.getUnsignedByte();
				childIds = new int[id + 1];
				for (int i = 0; i <= id; i++) {
					childIds[i] = buffer.getUnsignedShort();
					if (childIds[i] == 65535) {
						childIds[i] = -1;
					}
				}
			} else if (configId == 107) {
				clickable = false;
			}
		} while (true);
	}

	private NpcDefinition() {
		turnLeftAnim = -1;
		varbitId = -1;
		retreatAnim = -1;
		actionId = -1;
		combatLevel = -1;
		walkAnim = -1;
		size = 1;
		headIcon = -1;
		idleAnim = -1;
		type = -1L;
		turnAmount = 32;
		turnRightAnim = -1;
		clickable = true;
		scaleZ = 128;
		visibleOnMinimap = true;
		scaleXY = 128;
		members = false;
	}
	public int turnLeftAnim; // anInt55
	private static int totalNpcs; // anInt56
	private int varbitId; // anInt57
	public int retreatAnim; // anInt58
	private int actionId; // anInt59
	private static Stream buffer;
	public int combatLevel;
	public String name;
	public String actions[];
	public int walkAnim; // anInt67
	public byte size; // aByte68
	private int[] destColors; // anIntArray70
	private static int[] npcIndices;
	private int[] dialogueModels; // anIntArray73
	public int headIcon; // anInt75
	private int[] srcColors; // anIntArray76
	public int idleAnim; // anInt77
	public long type;
	public int turnAmount; // anInt79
	private static NpcDefinition[] cache;
	public static Client client;
	public int turnRightAnim; // anInt83
	public boolean clickable; // aBoolean84
	private int lightness; // anInt86
	private int scaleZ; // anInt86
	public boolean visibleOnMinimap; // aBoolean87
	public int childIds[];
	public byte description[];
	private int scaleXY; // anInt92
	private int shading; // anInt92
	public boolean members; // aBoolean93
	private int[] models; // anIntArray94
	public static Cache mruNodes = new Cache(30);
}
