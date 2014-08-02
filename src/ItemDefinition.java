public final class ItemDefinition {

	public static void dispose() {
		ItemDefinition.modelCache = null;
		ItemDefinition.spriteCache = null;
		ItemDefinition.itemIndices = null;
		ItemDefinition.itemCache = null;
		ItemDefinition.dataBuffer = null;
	}

	public boolean dialogueModelsCached(int gender) {
		int head = maleDialogue;
		int hat = maleHat;
		if (gender == 1) {
			head = femaleDialogue;
			hat = femaleHat;
		}
		if (head == -1) {
			return true;
		}
		boolean flag = true;
		if (!Model.isCached(head)) {
			flag = false;
		}
		if (hat != -1 && !Model.isCached(hat)) {
			flag = false;
		}
		return flag;
	}

	public static void unpackConfig(Archive container) {
		ItemDefinition.dataBuffer = new Stream(container.get("obj.dat"));
		Stream indexBuffer = new Stream(container.get("obj.idx"));
		ItemDefinition.itemCount = indexBuffer.getUnsignedShort();
		ItemDefinition.itemIndices = new int[ItemDefinition.itemCount + 10000];
		int offset = 2;
		for (int j = 0; j < ItemDefinition.itemCount; j++) {
			ItemDefinition.itemIndices[j] = offset;
			offset += indexBuffer.getUnsignedShort();
		}
		ItemDefinition.itemCache = new ItemDefinition[10];
		for (int i = 0; i < 10; i++) {
			ItemDefinition.itemCache[i] = new ItemDefinition();
		}
	}

	public Model getDialogueModels(int gender) {
		int headId = maleDialogue;
		int hatId = maleHat;
		if (gender == 1) {
			headId = femaleDialogue;
			hatId = femaleHat;
		}
		if (headId == -1) {
			return null;
		}
		Model head = Model.getModel(headId);
		if (hatId != -1) {
			Model hat = Model.getModel(hatId);
			Model models[] = { head, hat };
			head = new Model(2, models);
		}
		if (destColors != null) {
			for (int i = 0; i < destColors.length; i++) {
				head.swapColors(destColors[i], srcColors[i]);
			}
		}
		return head;
	}

	public boolean itemModelsCached(int gender) {
		int primaryModel = maleEquip1;
		int secondaryModel = maleEquip2;
		int emblem = maleEmblem;
		if (gender == 1) {
			primaryModel = maleEquips1;
			secondaryModel = femaleEquip2;
			emblem = femaleEmblem;
		}
		if (primaryModel == -1) {
			return true;
		}
		boolean equipped = true;
		if (!Model.isCached(primaryModel)) {
			equipped = false;
		}
		if (secondaryModel != -1 && !Model.isCached(secondaryModel)) {
			equipped = false;
		}
		if (emblem != -1 && !Model.isCached(emblem)) {
			equipped = false;
		}
		return equipped;
	}

	public Model getEquipModels(int gender) {
		int model1Id = maleEquip1;
		int model2Id = maleEquip2;
		int emblemId = maleEmblem;
		if (gender == 1) {
			model1Id = maleEquips1;
			model2Id = femaleEquip2;
			emblemId = femaleEmblem;
		}
		if (model1Id == -1) {
			return null;
		}
		Model primary = Model.getModel(model1Id);
		if (model2Id != -1) {
			if (emblemId != -1) {
				Model secondary = Model.getModel(model2Id);
				Model emblem = Model.getModel(emblemId);
				Model models[] = { primary, secondary, emblem };
				primary = new Model(3, models);
			} else {
				Model secondary = Model.getModel(model2Id);
				Model models[] = { primary, secondary };
				primary = new Model(2, models);
			}
		}
		if (gender == 0 && maleOffset != 0) {
			primary.translate(0, maleOffset, 0);
		}
		if (gender == 1 && femaleOffset != 0) {
			primary.translate(0, femaleOffset, 0);
		}
		if (destColors != null) {
			for (int i = 0; i < destColors.length; i++) {
				primary.swapColors(destColors[i], srcColors[i]);
			}
		}
		return primary;
	}

	private void setDefaults() {
		groundModel = 0;
		name = null;
		description = null;
		destColors = null;
		srcColors = null;
		modelZoom = 2000;
		rotationY = 0;
		rotationX = 0;
		scaleInventory = 0;
		offsetX = 0;
		offsetY = 0;
		stackable = false;
		value = 1;
		membersObject = false;
		groundActions = null;
		inventoryActions = null;
		maleEquip1 = -1;
		maleEquip2 = -1;
		maleOffset = 0;
		maleEquips1 = -1;
		femaleEquip2 = -1;
		femaleOffset = 0;
		maleEmblem = -1;
		femaleEmblem = -1;
		maleDialogue = -1;
		maleHat = -1;
		femaleDialogue = -1;
		femaleHat = -1;
		stackIds = null;
		stackAmounts = null;
		certId = -1;
		certTemplateId = -1;
		scaleX = 128;
		scaleZ = 128;
		scaleY = 128;
		lightness = 0;
		shading = 0;
		team = 0;
	}

	public static ItemDefinition forId(int id) {
		for (int i = 0; i < 10; i++) {
			if (ItemDefinition.itemCache[i].id == id) {
				return ItemDefinition.itemCache[i];
			}
		}
		ItemDefinition.cacheIndex = (ItemDefinition.cacheIndex + 1) % 10;
		ItemDefinition itemDefinition = ItemDefinition.itemCache[ItemDefinition.cacheIndex];
		ItemDefinition.dataBuffer.offset = ItemDefinition.itemIndices[id];
		itemDefinition.id = id;
		itemDefinition.setDefaults();
		itemDefinition.readValues(ItemDefinition.dataBuffer);
		if (itemDefinition.certTemplateId != -1) {
			itemDefinition.toNote();
		}
		if (!ItemDefinition.isMembers && itemDefinition.membersObject) {
			itemDefinition.name = "Members Object";
			itemDefinition.description = "Login to a members' server to use this object.".getBytes();
			itemDefinition.groundActions = null;
			itemDefinition.inventoryActions = null;
			itemDefinition.team = 0;
		}
		return itemDefinition;
	}

	private void toNote() {
		ItemDefinition item = ItemDefinition.forId(certTemplateId);
		groundModel = item.groundModel;
		modelZoom = item.modelZoom;
		rotationY = item.rotationY;
		rotationX = item.rotationX;
		scaleInventory = item.scaleInventory;
		offsetX = item.offsetX;
		offsetY = item.offsetY;
		destColors = item.destColors;
		srcColors = item.srcColors;
		ItemDefinition note = ItemDefinition.forId(certId);
		name = note.name;
		membersObject = note.membersObject;
		value = note.value;
		String s = "a";
		char c = note.name.charAt(0);
		if (c == 'A' || c == 'E' || c == 'I' || c == 'O' || c == 'U') {
			s = "an";
		}
		description = ("Swap this note at any bank for " + s + " " + note.name + ".").getBytes();
		stackable = true;
	}

	public static Sprite getSprite(int id, int amount, int arg2) {
		if (arg2 == 0) {
			Sprite sprite = (Sprite) ItemDefinition.spriteCache.get(id);
			if (sprite != null && sprite.trimHeight != amount && sprite.trimHeight != -1) {
				sprite.unlink();
				sprite = null;
			}
			if (sprite != null) {
				return sprite;
			}
		}
		ItemDefinition itemDefinition = ItemDefinition.forId(id);
		if (itemDefinition.stackIds == null) {
			amount = -1;
		}
		if (amount > 1) {
			int i = -1;
			for (int j1 = 0; j1 < 10; j1++) {
				if (amount >= itemDefinition.stackAmounts[j1] && itemDefinition.stackAmounts[j1] != 0) {
					i = itemDefinition.stackIds[j1];
				}
			}
			if (i != -1) {
				itemDefinition = ItemDefinition.forId(i);
			}
		}
		Model model = itemDefinition.preProcess(1);
		if (model == null) {
			return null;
		}
		Sprite sprite = null;
		if (itemDefinition.certTemplateId != -1) {
			sprite = ItemDefinition.getSprite(itemDefinition.certId, 10, -1);
			if (sprite == null) {
				return null;
			}
		}
		Sprite sprite2 = new Sprite(32, 32);
		int k1 = Rasterizer.centerX;
		int l1 = Rasterizer.centerY;
		int lineOffsets[] = Rasterizer.lineOffsets;
		int pixels[] = Graphics2D.pixels;
		int width = Graphics2D.width;
		int height = Graphics2D.height;
		int topX = Graphics2D.topX;
		int bottomX = Graphics2D.bottomX;
		int topY = Graphics2D.topY;
		int bottomY = Graphics2D.bottomY;
		Rasterizer.colorRestricted = false;
		Graphics2D.init2dCanvas(32, 32, sprite2.pixels);
		Graphics2D.fillRect(32, 0, 0, 0, 32);
		Rasterizer.setDefaultBounds();
		int zoom = itemDefinition.modelZoom;
		if (arg2 == -1) {
			zoom = (int) (zoom * 1.5D);
		}
		if (arg2 > 0) {
			zoom = (int) (zoom * 1.04D);
		}
		int l3 = Rasterizer.sineTable[itemDefinition.rotationY] * zoom >> 16;
		int i4 = Rasterizer.cosineTable[itemDefinition.rotationY] * zoom >> 16;
		model.method482(itemDefinition.rotationX, itemDefinition.scaleInventory, itemDefinition.rotationY, itemDefinition.offsetX, l3 + model.height / 2 + itemDefinition.offsetY, i4 + itemDefinition.offsetY);
		for (int i5 = 31; i5 >= 0; i5--) {
			for (int j4 = 31; j4 >= 0; j4--) {
				if (sprite2.pixels[i5 + j4 * 32] == 0) {
					if (i5 > 0 && sprite2.pixels[i5 - 1 + j4 * 32] > 1) {
						sprite2.pixels[i5 + j4 * 32] = 1;
					} else if (j4 > 0 && sprite2.pixels[i5 + (j4 - 1) * 32] > 1) {
						sprite2.pixels[i5 + j4 * 32] = 1;
					} else if (i5 < 31 && sprite2.pixels[i5 + 1 + j4 * 32] > 1) {
						sprite2.pixels[i5 + j4 * 32] = 1;
					} else if (j4 < 31 && sprite2.pixels[i5 + (j4 + 1) * 32] > 1) {
						sprite2.pixels[i5 + j4 * 32] = 1;
					}
				}
			}
		}
		if (arg2 > 0) {
			for (int j5 = 31; j5 >= 0; j5--) {
				for (int k4 = 31; k4 >= 0; k4--) {
					if (sprite2.pixels[j5 + k4 * 32] == 0) {
						if (j5 > 0 && sprite2.pixels[j5 - 1 + k4 * 32] == 1) {
							sprite2.pixels[j5 + k4 * 32] = arg2;
						} else if (k4 > 0 && sprite2.pixels[j5 + (k4 - 1) * 32] == 1) {
							sprite2.pixels[j5 + k4 * 32] = arg2;
						} else if (j5 < 31 && sprite2.pixels[j5 + 1 + k4 * 32] == 1) {
							sprite2.pixels[j5 + k4 * 32] = arg2;
						} else if (k4 < 31 && sprite2.pixels[j5 + (k4 + 1) * 32] == 1) {
							sprite2.pixels[j5 + k4 * 32] = arg2;
						}
					}
				}
			}
		} else if (arg2 == 0) {
			for (int k5 = 31; k5 >= 0; k5--) {
				for (int l4 = 31; l4 >= 0; l4--) {
					if (sprite2.pixels[k5 + l4 * 32] == 0 && k5 > 0 && l4 > 0 && sprite2.pixels[k5 - 1 + (l4 - 1) * 32] > 0) {
						sprite2.pixels[k5 + l4 * 32] = 0x302020;
					}
				}
			}
		}
		if (itemDefinition.certTemplateId != -1) {
			int l5 = sprite.trimWidth;
			int j6 = sprite.trimHeight;
			sprite.trimWidth = 32;
			sprite.trimHeight = 32;
			sprite.drawSprite(0, 0);
			sprite.trimWidth = l5;
			sprite.trimHeight = j6;
		}
		if (arg2 == 0) {
			ItemDefinition.spriteCache.put(sprite2, id);
		}
		Graphics2D.init2dCanvas(height, width, pixels);
		Graphics2D.setBounds(bottomY, topX, bottomX, topY);
		Rasterizer.centerX = k1;
		Rasterizer.centerY = l1;
		Rasterizer.lineOffsets = lineOffsets;
		Rasterizer.colorRestricted = true;
		if (itemDefinition.stackable) {
			sprite2.trimWidth = 33;
		} else {
			sprite2.trimWidth = 32;
		}
		sprite2.trimHeight = amount;
		return sprite2;
	}

	public Model preProcess(int arg0) {
		if (stackIds != null && arg0 > 1) {
			int id = -1;
			for (int i = 0; i < 10; i++) {
				if (arg0 >= stackAmounts[i] && stackAmounts[i] != 0) {
					id = stackIds[i];
				}
			}
			if (id != -1) {
				return ItemDefinition.forId(id).preProcess(1);
			}
		}
		Model model = (Model) ItemDefinition.modelCache.get(id);
		if (model != null) {
			return model;
		}
		model = Model.getModel(groundModel);
		if (model == null) {
			return null;
		}
		if (scaleX != 128 || scaleZ != 128 || scaleY != 128) {
			model.scale(scaleX, scaleY, scaleZ);
		}
		if (destColors != null) {
			for (int l = 0; l < destColors.length; l++) {
				model.swapColors(destColors[l], srcColors[l]);
			}
		}
		model.processLighting(64 + lightness, 768 + shading, -50, -10, -50, true);
		model.aBoolean1659 = true;
		ItemDefinition.modelCache.put(model, id);
		return model;
	}

	public Model getRecoloredModel(int arg0) {
		if (stackIds != null && arg0 > 1) {
			int id = -1;
			for (int i = 0; i < 10; i++) {
				if (arg0 >= stackAmounts[i] && stackAmounts[i] != 0) {
					id = stackIds[i];
				}
			}
			if (id != -1) {
				return ItemDefinition.forId(id).getRecoloredModel(1);
			}
		}
		Model model = Model.getModel(groundModel);
		if (model == null) {
			return null;
		}
		if (destColors != null) {
			for (int l = 0; l < destColors.length; l++) {
				model.swapColors(destColors[l], srcColors[l]);
			}
		}
		return model;
	}

	private void readValues(Stream buffer) {
		do {
			int config = buffer.getUnsignedByte();
			if (config == 0) {
				return;
			}
			if (config == 1) {
				groundModel = buffer.getUnsignedShort();
			} else if (config == 2) {
				name = buffer.getString();
			} else if (config == 3) {
				description = buffer.getStringBytes();
			} else if (config == 4) {
				modelZoom = buffer.getUnsignedShort();
			} else if (config == 5) {
				rotationY = buffer.getUnsignedShort();
			} else if (config == 6) {
				rotationX = buffer.getUnsignedShort();
			} else if (config == 7) {
				offsetX = buffer.getUnsignedShort();
				if (offsetX > 32767) {
					offsetX -= 0x10000;
				}
			} else if (config == 8) {
				offsetY = buffer.getUnsignedShort();
				if (offsetY > 32767) {
					offsetY -= 0x10000;
				}
			} else if (config == 10) {
				buffer.getUnsignedShort();
			} else if (config == 11) {
				stackable = true;
			} else if (config == 12) {
				value = buffer.getInt();
			} else if (config == 16) {
				membersObject = true;
			} else if (config == 23) {
				maleEquip1 = buffer.getUnsignedShort();
				maleOffset = buffer.getByte();
			} else if (config == 24) {
				maleEquip2 = buffer.getUnsignedShort();
			} else if (config == 25) {
				maleEquips1 = buffer.getUnsignedShort();
				femaleOffset = buffer.getByte();
			} else if (config == 26) {
				femaleEquip2 = buffer.getUnsignedShort();
			} else if (config >= 30 && config < 35) {
				if (groundActions == null) {
					groundActions = new String[5];
				}
				groundActions[config - 30] = buffer.getString();
				if (groundActions[config - 30].equalsIgnoreCase("hidden")) {
					groundActions[config - 30] = null;
				}
			} else if (config >= 35 && config < 40) {
				if (inventoryActions == null) {
					inventoryActions = new String[5];
				}
				inventoryActions[config - 35] = buffer.getString();
			} else if (config == 40) {
				int len = buffer.getUnsignedByte();
				destColors = new int[len];
				srcColors = new int[len];
				for (int i = 0; i < len; i++) {
					destColors[i] = buffer.getUnsignedShort();
					srcColors[i] = buffer.getUnsignedShort();
				}
			} else if (config == 78) {
				maleEmblem = buffer.getUnsignedShort();
			} else if (config == 79) {
				femaleEmblem = buffer.getUnsignedShort();
			} else if (config == 90) {
				maleDialogue = buffer.getUnsignedShort();
			} else if (config == 91) {
				femaleDialogue = buffer.getUnsignedShort();
			} else if (config == 92) {
				maleHat = buffer.getUnsignedShort();
			} else if (config == 93) {
				femaleHat = buffer.getUnsignedShort();
			} else if (config == 95) {
				scaleInventory = buffer.getUnsignedShort();
			} else if (config == 97) {
				certId = buffer.getUnsignedShort();
			} else if (config == 98) {
				certTemplateId = buffer.getUnsignedShort();
			} else if (config >= 100 && config < 110) {
				if (stackIds == null) {
					stackIds = new int[10];
					stackAmounts = new int[10];
				}
				stackIds[config - 100] = buffer.getUnsignedShort();
				stackAmounts[config - 100] = buffer.getUnsignedShort();
			} else if (config == 110) {
				scaleX = buffer.getUnsignedShort();
			} else if (config == 111) {
				scaleZ = buffer.getUnsignedShort();
			} else if (config == 112) {
				scaleY = buffer.getUnsignedShort();
			} else if (config == 113) {
				lightness = buffer.getByte();
			} else if (config == 114) {
				shading = buffer.getByte() * 5;
			} else if (config == 115) {
				team = buffer.getUnsignedByte();
			}
		} while (true);
	}

	private ItemDefinition() {
		id = -1;
	}
	private byte femaleOffset; // aByte154
	public int value;
	private int[] destColors;
	public int id;
	static Cache spriteCache = new Cache(100);
	public static Cache modelCache = new Cache(50);
	private int[] srcColors;
	public boolean membersObject;
	private int femaleEmblem; // anInt162
	private int certTemplateId;
	private int femaleEquip2; // anInt164
	private int maleEquip1; // anInt165
	private int maleHat; // anInt166
	private int scaleX; // anInt167
	public String groundActions[];
	private int offsetX; // modelOffset1
	public String name;
	private static ItemDefinition[] itemCache;
	private int femaleHat; // anInt173
	private int groundModel;
	private int maleDialogue; // anInt175
	public boolean stackable;
	public byte description[];
	private int certId;
	private static int cacheIndex;
	public int modelZoom;
	public static boolean isMembers = true;
	private static Stream dataBuffer;
	private int shading; // anInt184
	private int maleEmblem; // anInt185
	private int maleEquip2; // anInt188
	public String inventoryActions[];
	public int rotationY; // modelRotation1
	private int scaleY; // anInt191
	private int scaleZ; // anInt192
	private int[] stackIds;
	private int offsetY; // modelOffset2
	private static int[] itemIndices;
	private int lightness; // anInt196
	private int femaleDialogue; // anInt197
	public int rotationX; // modelRotation2
	private int maleEquips1; // anInt200
	private int[] stackAmounts;
	public int team;
	public static int itemCount;
	private int scaleInventory; // anInt204
	private byte maleOffset; // aByte205
}
