public final class Player extends Mobile {
	@Override
	public Model getRotatedModel() {
		if (!visible) {
			return null;
		}
		Model model = method452();
		if (model == null) {
			return null;
		}
		super.height = model.height;
		model.aBoolean1659 = true;
		if (aBoolean1699) {
			return model;
		}
		if (super.currentGraphic != -1 && super.anInt1521 != -1) {
			SpotAnimation spotAnimation = SpotAnimation.spotAnimationCache[super.currentGraphic];
			Model graphicModel = spotAnimation.getModel();
			if (graphicModel != null) {
				Model transformedGraphic = new Model(true, Animation.isNullFrame(super.anInt1521), false, graphicModel);
				transformedGraphic.translate(0, -super.anInt1524, 0);
				transformedGraphic.skin();
				transformedGraphic.transform(spotAnimation.sequence.frames[super.anInt1521]);
				transformedGraphic.triangleSkin = null;
				transformedGraphic.vertexSkins = null;
				if (spotAnimation.scaleXY != 128 || spotAnimation.scaleZ != 128) {
					transformedGraphic.scale(spotAnimation.scaleXY, spotAnimation.scaleXY, spotAnimation.scaleZ);
				}
				transformedGraphic.processLighting(64 + spotAnimation.lightness, 850 + spotAnimation.shading, -30, -50, -30, true);
				Model models[] = { model, transformedGraphic };
				model = new Model(models);
			}
		}
		if (aModel_1714 != null) {
			if (Client.loopCycle >= anInt1708) {
				aModel_1714 = null;
			}
			if (Client.loopCycle >= anInt1707 && Client.loopCycle < anInt1708) {
				Model transformedPlayer = aModel_1714;
				transformedPlayer.translate(anInt1711 - super.x, anInt1712 - anInt1709, anInt1713 - super.y);
				if (super.turnDirection == 512) {
					transformedPlayer.method473();
					transformedPlayer.method473();
					transformedPlayer.method473();
				} else if (super.turnDirection == 1024) {
					transformedPlayer.method473();
					transformedPlayer.method473();
				} else if (super.turnDirection == 1536) {
					transformedPlayer.method473();
				}
				Model models[] = { model, transformedPlayer };
				model = new Model(models);
				if (super.turnDirection == 512) {
					transformedPlayer.method473();
				} else if (super.turnDirection == 1024) {
					transformedPlayer.method473();
					transformedPlayer.method473();
				} else if (super.turnDirection == 1536) {
					transformedPlayer.method473();
					transformedPlayer.method473();
					transformedPlayer.method473();
				}
				transformedPlayer.translate(super.x - anInt1711, anInt1709 - anInt1712, super.y - anInt1713);
			}
		}
		model.aBoolean1659 = true;
		return model;
	}

	public void updatePlayer(Stream buffer) {
		buffer.offset = 0;
		anInt1702 = buffer.getUnsignedByte();
		prayerIcon = buffer.getByte();
		skullIcon = buffer.getByte();
		hintIcon = buffer.getUnsignedByte();
		desc = null;
		team = 0;
		for (int i = 0; i < 12; i++) {
			int equipId = buffer.getUnsignedByte();
			if (equipId == 0) {
				equipmentIds[i] = 0;
				continue;
			}
			int off = buffer.getUnsignedByte();
			equipmentIds[i] = (equipId << 8) + off;
			if (i == 0 && equipmentIds[0] == 65535) {
				desc = NpcDefinition.forId(buffer.getUnsignedShort());
				break;
			}
			if (equipmentIds[i] >= 512 && equipmentIds[i] - 512 < ItemDefinition.itemCount) {
				int l1 = ItemDefinition.forId(equipmentIds[i] - 512).team;
				if (l1 != 0) {
					team = l1;
				}
			}
		}
		for (int i = 0; i < 5; i++) {
			int len = buffer.getUnsignedByte();
			if (len < 0 || len >= Client.anIntArrayArray1003[i].length) {
				len = 0;
			}
			anIntArray1700[i] = len;
		}
		super.idleAnimation = buffer.getUnsignedShort();
		if (super.idleAnimation == 65535) {
			super.idleAnimation = -1;
		}
		super.turnAnimation = buffer.getUnsignedShort();
		if (super.turnAnimation == 65535) {
			super.turnAnimation = -1;
		}
		super.walkAnimation = buffer.getUnsignedShort();
		if (super.walkAnimation == 65535) {
			super.walkAnimation = -1;
		}
		super.anInt1555 = buffer.getUnsignedShort();
		if (super.anInt1555 == 65535) {
			super.anInt1555 = -1;
		}
		super.anInt1556 = buffer.getUnsignedShort();
		if (super.anInt1556 == 65535) {
			super.anInt1556 = -1;
		}
		super.anInt1557 = buffer.getUnsignedShort();
		if (super.anInt1557 == 65535) {
			super.anInt1557 = -1;
		}
		super.runAnimation = buffer.getUnsignedShort();
		if (super.runAnimation == 65535) {
			super.runAnimation = -1;
		}
		name = TextUtil.formatName(TextUtil.longToName(buffer.getLong()));
		combatLevel = buffer.getUnsignedByte();
		skillLevel = buffer.getUnsignedShort();
		visible = true;
		equipment = 0L;
		for (int k1 = 0; k1 < 12; k1++) {
			equipment <<= 4;
			if (equipmentIds[k1] >= 256) {
				equipment += equipmentIds[k1] - 256;
			}
		}
		if (equipmentIds[0] >= 256) {
			equipment += equipmentIds[0] - 256 >> 4;
		}
		if (equipmentIds[1] >= 256) {
			equipment += equipmentIds[1] - 256 >> 8;
		}
		for (int i2 = 0; i2 < 5; i2++) {
			equipment <<= 3;
			equipment += anIntArray1700[i2];
		}
		equipment <<= 1;
		equipment += anInt1702;
	}

	private Model method452() {
		if (desc != null) {
			int anim = -1;
			if (super.animationId >= 0 && super.animationDelay == 0) {
				anim = Sequence.sequenceCache[super.animationId].frames[super.anInt1527];
			} else if (super.anInt1517 >= 0) {
				anim = Sequence.sequenceCache[super.anInt1517].frames[super.anInt1518];
			}
			Model model = desc.method164(-1, anim, null);
			return model;
		}
		long id = equipment;
		int k = -1;
		int i1 = -1;
		int j1 = -1;
		int k1 = -1;
		if (super.animationId >= 0 && super.animationDelay == 0) {
			Sequence sequence = Sequence.sequenceCache[super.animationId];
			k = sequence.frames[super.anInt1527];
			if (super.anInt1517 >= 0 && super.anInt1517 != super.idleAnimation) {
				i1 = Sequence.sequenceCache[super.anInt1517].frames[super.anInt1518];
			}
			if (sequence.shield >= 0) {
				j1 = sequence.shield;
				id += j1 - equipmentIds[5] << 40;
			}
			if (sequence.weapon >= 0) {
				k1 = sequence.weapon;
				id += k1 - equipmentIds[3] << 48;
			}
		} else if (super.anInt1517 >= 0) {
			k = Sequence.sequenceCache[super.anInt1517].frames[super.anInt1518];
		}
		Model model = (Model) Player.modelCache.get(id);
		if (model == null) {
			boolean flag = false;
			for (int i2 = 0; i2 < 12; i2++) {
				int k2 = equipmentIds[i2];
				if (k1 >= 0 && i2 == 3) {
					k2 = k1;
				}
				if (j1 >= 0 && i2 == 5) {
					k2 = j1;
				}
				if (k2 >= 256 && k2 < 512 && !IdentityKit.designCache[k2 - 256].method537()) {
					flag = true;
				}
				if (k2 >= 512 && !ItemDefinition.forId(k2 - 512).itemModelsCached(anInt1702)) {
					flag = true;
				}
			}
			if (flag) {
				if (aLong1697 != -1L) {
					model = (Model) Player.modelCache.get(aLong1697);
				}
				if (model == null) {
					return null;
				}
			}
		}
		if (model == null) {
			Model appearance[] = new Model[12];
			int off = 0;
			for (int i = 0; i < 12; i++) {
				int i3 = equipmentIds[i];
				if (k1 >= 0 && i == 3) {
					i3 = k1;
				}
				if (j1 >= 0 && i == 5) {
					i3 = j1;
				}
				if (i3 >= 256 && i3 < 512) {
					Model clothes = IdentityKit.designCache[i3 - 256].method538();
					if (clothes != null) {
						appearance[off++] = clothes;
					}
				}
				if (i3 >= 512) {
					Model model_4 = ItemDefinition.forId(i3 - 512).getEquipModels(anInt1702);
					if (model_4 != null) {
						appearance[off++] = model_4;
					}
				}
			}
			model = new Model(off, appearance);
			for (int j3 = 0; j3 < 5; j3++) {
				if (anIntArray1700[j3] != 0) {
					model.swapColors(Client.anIntArrayArray1003[j3][0], Client.anIntArrayArray1003[j3][anIntArray1700[j3]]);
					if (j3 == 1) {
						model.swapColors(Client.anIntArray1204[0], Client.anIntArray1204[anIntArray1700[j3]]);
					}
				}
			}
			model.skin();
			model.processLighting(64, 850, -30, -50, -30, true);
			Player.modelCache.put(model, id);
			aLong1697 = id;
		}
		if (aBoolean1699) {
			return model;
		}
		Model transformedModel = Model.aModel_1621;
		transformedModel.setTransparency(model, Animation.isNullFrame(k) & Animation.isNullFrame(i1));
		if (k != -1 && i1 != -1) {
			transformedModel.pushFrames(Sequence.sequenceCache[super.animationId].anIntArray357, i1, k);
		} else if (k != -1) {
			transformedModel.transform(k);
		}
		transformedModel.getDefaultDiagonals();
		transformedModel.triangleSkin = null;
		transformedModel.vertexSkins = null;
		return transformedModel;
	}

	@Override
	public boolean isVisible() {
		return visible;
	}
	public int privelage;

	public Model method453() {
		if (!visible) {
			return null;
		}
		if (desc != null) {
			return desc.method160();
		}
		boolean flag = false;
		for (int i = 0; i < 12; i++) {
			int j = equipmentIds[i];
			if (j >= 256 && j < 512 && !IdentityKit.designCache[j - 256].method539()) {
				flag = true;
			}
			if (j >= 512 && !ItemDefinition.forId(j - 512).dialogueModelsCached(anInt1702)) {
				flag = true;
			}
		}
		if (flag) {
			return null;
		}
		Model appearance[] = new Model[12];
		int k = 0;
		for (int l = 0; l < 12; l++) {
			int i1 = equipmentIds[l];
			if (i1 >= 256 && i1 < 512) {
				Model model = IdentityKit.designCache[i1 - 256].method540();
				if (model != null) {
					appearance[k++] = model;
				}
			}
			if (i1 >= 512) {
				Model model = ItemDefinition.forId(i1 - 512).getDialogueModels(anInt1702);
				if (model != null) {
					appearance[k++] = model;
				}
			}
		}
		Model model = new Model(k, appearance);
		for (int i = 0; i < 5; i++) {
			if (anIntArray1700[i] != 0) {
				model.swapColors(Client.anIntArrayArray1003[i][0], Client.anIntArrayArray1003[i][anIntArray1700[i]]);
				if (i == 1) {
					model.swapColors(Client.anIntArray1204[0], Client.anIntArray1204[anIntArray1700[i]]);
				}
			}
		}
		return model;
	}

	Player() {
		aLong1697 = -1L;
		aBoolean1699 = false;
		anIntArray1700 = new int[5];
		visible = false;
		equipmentIds = new int[12];
	}
	private long aLong1697;
	public NpcDefinition desc;
	boolean aBoolean1699;
	final int[] anIntArray1700;
	public int team;
	private int anInt1702;
	public String name;
	static Cache modelCache = new Cache(260);
	public int combatLevel;
	public int hintIcon;
	public int skullIcon;
	public int prayerIcon;
	public int anInt1707;
	int anInt1708;
	int anInt1709;
	boolean visible;
	int anInt1711;
	int anInt1712;
	int anInt1713;
	Model aModel_1714;
	public final int[] equipmentIds;
	private long equipment;
	int anInt1719;
	int anInt1720;
	int anInt1721;
	int anInt1722;
	int skillLevel;
}
