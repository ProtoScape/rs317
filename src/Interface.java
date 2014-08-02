import java.io.DataOutputStream;
import java.io.IOException;

public final class Interface {// TODO: Finish refactoring this shit.

	public static void writeInteraces() {
		try {
			Stream buffer = new Stream(new byte[999999]);
			buffer.writeShort(cachedInterfaces.length);
			for (Interface component : cachedInterfaces) {
				if (component == null) {
					continue;
				}
				int parent = component.parentId;
				if (component.parentId != -1) {
					buffer.writeShort(65535);
					buffer.writeShort(parent);
					buffer.writeShort(component.id);
				} else {
					buffer.writeShort(component.id);
				}
				buffer.writeByte(component.interfaceType);
				buffer.writeByte(component.actionType);
				buffer.writeShort(component.contentType);
				buffer.writeShort(component.width);
				buffer.writeShort(component.height);
				buffer.writeByte(component.alpha);
				if (component.hoverType != -1) {
					buffer.writeSizeByte(component.hoverType);
				} else {
					buffer.writeByte(0);
				}
				int array245Count = 0;
				if (component.valueCompareType != null) {
					array245Count = component.valueCompareType.length;
				}
				buffer.writeByte(array245Count);
				if (array245Count > 0) {
					for (int i = 0; i < array245Count; i++) {
						buffer.writeByte(component.valueCompareType[i]);
						buffer.writeShort(component.requiredValues[i]);
					}
				}
				int valueLength = 0;
				if (component.valueIndices != null) {
					valueLength = component.valueIndices.length;
				}
				buffer.writeByte(valueLength);
				if (valueLength > 0) {
					for (int l1 = 0; l1 < valueLength; l1++) {
						int i3 = component.valueIndices[l1].length;
						buffer.writeShort(i3);
						for (int l4 = 0; l4 < i3; l4++) {
							buffer.writeShort(component.valueIndices[l1][l4]);
						}
					}
				}
				if (component.interfaceType == 0) {
					buffer.writeShort(component.scrollMax);
					buffer.writeByte(component.aBoolean266 ? 1 : 0);
					buffer.writeShort(component.children.length);
					for (int i = 0; i < component.children.length; i++) {
						buffer.writeShort(component.children[i]);
						buffer.writeShort(component.childX[i]);
						buffer.writeShort(component.childY[i]);
					}
				} else if (component.interfaceType == 1) {
					buffer.writeShort(0);
					buffer.writeByte(0);
				} else if (component.interfaceType == 2) {
					buffer.writeByte(component.aBoolean259 ? 1 : 0);
					buffer.writeByte(component.isInventoryInterface ? 1 : 0);
					buffer.writeByte(component.usableItemInterface ? 1 : 0);
					buffer.writeByte(component.aBoolean235 ? 1 : 0);
					buffer.writeByte(component.invSpritePadX);
					buffer.writeByte(component.invSpritePadY);
					for (int i = 0; i < 20; i++) {
						buffer.writeByte(component.sprites[i] == null ? 0 : 1);
						if (component.sprites[i] != null) {
							buffer.writeShort(component.spritesX[i]);
							buffer.writeShort(component.spritesY[i]);
							// buffer.writeString(component.sprites[i].myDirectory);
							buffer.writeString(Signlink.findcachedir());
						}
					}
					for (int i = 0; i < 5; i++) {
						if (component.actions[i] != null) {
							buffer.writeString(component.actions[i]);
						} else {
							buffer.writeString("");
						}
					}
				} else if (component.interfaceType == 3) {
					buffer.writeByte(component.aBoolean227 ? 1 : 0);
				}
				if (component.interfaceType == 4
					|| component.interfaceType == 1) {
					buffer.writeByte(component.aBoolean223 ? 1 : 0);
					buffer.writeByte(component.fontId);
					buffer.writeByte(component.aBoolean268 ? 1 : 0);
				}
				if (component.interfaceType == 4) {
					buffer.writeString(component.message);
					buffer.writeString(component.aString228);
				}
				if (component.interfaceType == 1
					|| component.interfaceType == 3
					|| component.interfaceType == 4)
					buffer.writeInt(component.textColor);
				if (component.interfaceType == 3
					|| component.interfaceType == 4) {
					buffer.writeInt(component.anInt219);
					buffer.writeInt(component.anInt216);
					buffer.writeInt(component.anInt239);
				}
				if (component.interfaceType == 5) {
					if (component.sprite1 != null) {
						// if (component.sprite1.myDirectory != null) {
						// buffer.writeString(component.sprite1.myDirectory);
						if (Signlink.findcachedir() != null) {
							buffer.writeString(Signlink.findcachedir());
						} else {
							buffer.writeString("");
						}
					} else {
						buffer.writeString("");
					}
					if (component.sprite2 != null) {
						// if (component.sprite2.myDirectory != null) {
						// buffer.writeString(component.sprite2.myDirectory);
						if (Signlink.findcachedir() != null) {
							buffer.writeString(Signlink.findcachedir());
						} else {
							buffer.writeString("");
						}
					} else {
						buffer.writeString("");
					}
				} else if (component.interfaceType == 6) {
					if (component.anInt233 != -1 && component.mediaId > 0) {
						buffer.writeSizeByte(component.mediaId);
					} else {
						buffer.writeByte(0);
					}
					if (component.anInt256 > 0) {
						buffer.writeSizeByte(component.anInt256);
					} else {
						buffer.writeByte(0);
					}
					if (component.anInt257 > 0) {
						buffer.writeSizeByte(component.anInt257);
					} else {
						buffer.writeByte(0);
					}
					if (component.anInt258 > 0) {
						buffer.writeSizeByte(component.anInt258);
					} else {
						buffer.writeByte(0);
					}
					buffer.writeShort(component.anInt269);
					buffer.writeShort(component.anInt270);
					buffer.writeShort(component.anInt271);
				} else if (component.interfaceType == 7) {
					buffer.writeByte(component.aBoolean223 ? 1 : 0);
					buffer.writeByte(component.fontId);
					buffer.writeByte(component.aBoolean268 ? 1 : 0);
					buffer.writeInt(component.textColor);
					buffer.writeShort(component.invSpritePadX);
					buffer.writeShort(component.invSpritePadY);
					buffer.writeByte(component.isInventoryInterface ? 1 : 0);
					for (int i = 0; i < 5; i++) {
						if (component.actions[i] != null) {
							buffer.writeString(component.actions[i]);
						} else {
							buffer.writeString("");
						}
					}
				}
				if (component.actionType == 2 || component.interfaceType == 2) {
					buffer.writeString(component.selectedActionName);
					buffer.writeString(component.spellName);
					buffer.writeShort(component.spellUsableOn);
				}
				if (component.interfaceType == 8)
					buffer.writeString(component.message);
				if (component.actionType == 1 || component.actionType == 4
					|| component.actionType == 5 || component.actionType == 6) {
					buffer.writeString(component.tooltip);
				}
			}
			DataOutputStream dos = new DataOutputStream(
				new java.io.FileOutputStream("./interfaces.dat"));
			dos.write(buffer.payload, 0, buffer.offset);
			dos.close();
		} catch (IOException e) {
		}
	}

	public void swapInventoryItems(int src, int dest) {
		int id = inv[src];
		inv[src] = inv[dest];
		inv[dest] = id;
		id = invStackSizes[src];
		invStackSizes[src] = invStackSizes[dest];
		invStackSizes[dest] = id;
	}

	public static void unpack(Archive data, Font fonts[], Archive index) {
		Interface.spriteCache = new Cache(50000);
		Stream buffer = new Stream(data.get("data"));
		int childId = -1;
		int size = buffer.getUnsignedShort();
		Interface.cachedInterfaces = new Interface[size];
		while (buffer.offset < buffer.payload.length) {
			int parentId = buffer.getUnsignedShort();
			if (parentId == 65535) {
				childId = buffer.getUnsignedShort();
				parentId = buffer.getUnsignedShort();
			}
			Interface component = Interface.cachedInterfaces[parentId] = new Interface();
			component.id = parentId;
			component.parentId = childId;
			component.interfaceType = buffer.getUnsignedByte();
			component.actionType = buffer.getUnsignedByte();
			component.contentType = buffer.getUnsignedShort();
			component.width = buffer.getUnsignedShort();
			component.height = buffer.getUnsignedShort();
			component.alpha = (byte) buffer.getUnsignedByte();
			component.hoverType = buffer.getUnsignedByte();
			if (component.hoverType != 0) {
				component.hoverType = (component.hoverType - 1 << 8)
					+ buffer.getUnsignedByte();
			} else {
				component.hoverType = -1;
			}
			int requirementIndex = buffer.getUnsignedByte();
			if (requirementIndex > 0) {
				component.valueCompareType = new int[requirementIndex];
				component.requiredValues = new int[requirementIndex];
				for (int i = 0; i < requirementIndex; i++) {
					component.valueCompareType[i] = buffer.getUnsignedByte();
					component.requiredValues[i] = buffer.getUnsignedShort();
				}
			}
			int valueType = buffer.getUnsignedByte();
			if (valueType > 0) {
				component.valueIndices = new int[valueType][];
				for (int l1 = 0; l1 < valueType; l1++) {
					int i3 = buffer.getUnsignedShort();
					component.valueIndices[l1] = new int[i3];
					for (int l4 = 0; l4 < i3; l4++) {
						component.valueIndices[l1][l4] = buffer
							.getUnsignedShort();
					}
				}
			}
			if (component.interfaceType == 0) {
				component.scrollMax = buffer.getUnsignedShort();
				component.aBoolean266 = buffer.getUnsignedByte() == 1;
				int i2 = buffer.getUnsignedShort();
				component.children = new int[i2];
				component.childX = new int[i2];
				component.childY = new int[i2];
				for (int j3 = 0; j3 < i2; j3++) {
					component.children[j3] = buffer.getUnsignedShort();
					component.childX[j3] = buffer.getShort();
					component.childY[j3] = buffer.getShort();
				}
			}
			if (component.interfaceType == 1) {
				buffer.getUnsignedShort();
				buffer.getUnsignedByte();
			}
			if (component.interfaceType == 2) {
				component.inv = new int[component.width * component.height];
				component.invStackSizes = new int[component.width
					* component.height];
				component.aBoolean259 = buffer.getUnsignedByte() == 1;
				component.isInventoryInterface = buffer.getUnsignedByte() == 1;
				component.usableItemInterface = buffer.getUnsignedByte() == 1;
				component.aBoolean235 = buffer.getUnsignedByte() == 1;
				component.invSpritePadX = buffer.getUnsignedByte();
				component.invSpritePadY = buffer.getUnsignedByte();
				component.spritesX = new int[20];
				component.spritesY = new int[20];
				component.sprites = new Sprite[20];
				for (int j2 = 0; j2 < 20; j2++) {
					int k3 = buffer.getUnsignedByte();
					if (k3 == 1) {
						component.spritesX[j2] = buffer.getShort();
						component.spritesY[j2] = buffer.getShort();
						String s1 = buffer.getString();
						if (index != null && s1.length() > 0) {
							int i5 = s1.lastIndexOf(",");
							component.sprites[j2] = Interface.getSprite(Integer
								.parseInt(s1.substring(i5 + 1)), index, s1
								.substring(0, i5));
						}
					}
				}
				component.actions = new String[5];
				for (int l3 = 0; l3 < 5; l3++) {
					component.actions[l3] = buffer.getString();
					if (component.actions[l3].length() == 0) {
						component.actions[l3] = null;
					}
				}
			}
			if (component.interfaceType == 3) {
				component.aBoolean227 = buffer.getUnsignedByte() == 1;
			}
			if (component.interfaceType == 4 || component.interfaceType == 1) {
				component.aBoolean223 = buffer.getUnsignedByte() == 1;
				int k2 = buffer.getUnsignedByte();
				if (fonts != null) {
					component.fonts = fonts[k2];
				}
				component.aBoolean268 = buffer.getUnsignedByte() == 1;
			}
			if (component.interfaceType == 4) {
				component.message = buffer.getString();
				component.aString228 = buffer.getString();
			}
			if (component.interfaceType == 1 || component.interfaceType == 3
				|| component.interfaceType == 4) {
				component.textColor = buffer.getInt();
			}
			if (component.interfaceType == 3 || component.interfaceType == 4) {
				component.anInt219 = buffer.getInt();
				component.anInt216 = buffer.getInt();
				component.anInt239 = buffer.getInt();
			}
			if (component.interfaceType == 5) {
				String s = buffer.getString();
				if (index != null && s.length() > 0) {
					int i4 = s.lastIndexOf(",");
					component.sprite1 = Interface.getSprite(Integer.parseInt(s
						.substring(i4 + 1)), index, s.substring(0, i4));
				}
				s = buffer.getString();
				if (index != null && s.length() > 0) {
					int j4 = s.lastIndexOf(",");
					component.sprite2 = Interface.getSprite(Integer.parseInt(s
						.substring(j4 + 1)), index, s.substring(0, j4));
				}
			}
			if (component.interfaceType == 6) {
				int l = buffer.getUnsignedByte();
				if (l != 0) {
					component.anInt233 = 1;
					component.mediaId = (l - 1 << 8) + buffer.getUnsignedByte();
				}
				l = buffer.getUnsignedByte();
				if (l != 0) {
					component.anInt255 = 1;
					component.anInt256 = (l - 1 << 8)
						+ buffer.getUnsignedByte();
				}
				l = buffer.getUnsignedByte();
				if (l != 0) {
					component.anInt257 = (l - 1 << 8)
						+ buffer.getUnsignedByte();
				} else {
					component.anInt257 = -1;
				}
				l = buffer.getUnsignedByte();
				if (l != 0) {
					component.anInt258 = (l - 1 << 8)
						+ buffer.getUnsignedByte();
				} else {
					component.anInt258 = -1;
				}
				component.anInt269 = buffer.getUnsignedShort();
				component.anInt270 = buffer.getUnsignedShort();
				component.anInt271 = buffer.getUnsignedShort();
			}
			if (component.interfaceType == 7) {
				component.inv = new int[component.width * component.height];
				component.invStackSizes = new int[component.width
					* component.height];
				component.aBoolean223 = buffer.getUnsignedByte() == 1;
				component.fontId = buffer.getUnsignedByte();
				if (fonts != null) {
					component.fonts = fonts[component.fontId];
				}
				component.aBoolean268 = buffer.getUnsignedByte() == 1;
				component.textColor = buffer.getInt();
				component.invSpritePadX = buffer.getShort();
				component.invSpritePadY = buffer.getShort();
				component.isInventoryInterface = buffer.getUnsignedByte() == 1;
				component.actions = new String[5];
				for (int k4 = 0; k4 < 5; k4++) {
					component.actions[k4] = buffer.getString();
					if (component.actions[k4].length() == 0) {
						component.actions[k4] = null;
					}
				}
			}
			if (component.actionType == 2 || component.interfaceType == 2) {
				component.selectedActionName = buffer.getString();
				component.spellName = buffer.getString();
				component.spellUsableOn = buffer.getUnsignedShort();
			}
			if (component.interfaceType == 8) {
				component.message = buffer.getString();
			}
			if (component.actionType == 1 || component.actionType == 4
				|| component.actionType == 5 || component.actionType == 6) {
				component.tooltip = buffer.getString();
				if (component.tooltip.length() == 0) {
					if (component.actionType == 1) {
						component.tooltip = "Ok";
					}
					if (component.actionType == 4) {
						component.tooltip = "Select";
					}
					if (component.actionType == 5) {
						component.tooltip = "Select";
					}
					if (component.actionType == 6) {
						component.tooltip = "Continue";
					}
				}
			}
		}
		Interface.spriteCache = null;
	}

	private Model getModel(int type, int id) { // method206
		Model model = (Model) Interface.modelCache.get((type << 16) + id);
		if (model != null) {
			return model;
		}
		if (type == 1) {
			model = Model.getModel(id);
		}
		if (type == 2) {
			model = NpcDefinition.forId(id).method160();
		}
		if (type == 3) {
			model = Client.myPlayer.method453();
		}
		if (type == 4) {
			model = ItemDefinition.forId(id).getRecoloredModel(50);
		}
		if (type == 5) {
			model = null;
		}
		if (model != null) {
			Interface.modelCache.put(model, (type << 16) + id);
		}
		return model;
	}

	private static Sprite getSprite(int index, Archive container, String name) { // method207
		long l = (TextUtil.imageToLong(name) << 8) + index;
		Sprite sprite = (Sprite) Interface.spriteCache.get(l);
		if (sprite != null) {
			return sprite;
		}
		try {
			sprite = new Sprite(container, name, index);
			Interface.spriteCache.put(sprite, l);
		} catch (Exception exception) {
			return null;
		}
		return sprite;
	}

	public static void method208(boolean flag, Model model) {
		int i = 0;
		int j = 5;
		if (flag) {
			return;
		}
		Interface.modelCache.clear();
		if (model != null && j != 4) {
			Interface.modelCache.put(model, (j << 16) + i);
		}
	}

	public Model method209(int arg0, int arg1, boolean flag) {
		Model model;
		if (flag) {
			model = getModel(anInt255, anInt256);
		} else {
			model = getModel(anInt233, mediaId);
		}
		if (model == null) {
			return null;
		}
		if (arg1 == -1 && arg0 == -1 && model.triangleColors == null) {
			return model;
		}
		Model transformedModel = new Model(true, Animation.isNullFrame(arg1)
			& Animation.isNullFrame(arg0), false, model);
		if (arg1 != -1 || arg0 != -1) {
			transformedModel.skin();
		}
		if (arg1 != -1) {
			transformedModel.transform(arg1);
		}
		if (arg0 != -1) {
			transformedModel.transform(arg0);
		}
		transformedModel.processLighting(64, 768, -50, -10, -50, true);
		return transformedModel;
	}

	public Interface() {
	}
	public Sprite sprite1;
	public int anInt208;
	public Sprite sprites[];
	public static Interface cachedInterfaces[];
	public int requiredValues[]; // anIntArray212
	public int contentType; // anInt214
	public int spritesX[];
	public int anInt216;
	public int actionType;
	public String spellName;
	public int anInt219;
	public int width;
	public String tooltip;
	public String selectedActionName;
	public boolean aBoolean223;
	public int scrollPosition;
	public String actions[];
	public int valueIndices[][];
	public boolean aBoolean227;
	public String aString228;
	public int hoverType; // anInt230
	public int invSpritePadX;
	public int textColor;
	public int anInt233;
	public int mediaId;
	public boolean aBoolean235;
	public int parentId;
	public int spellUsableOn;
	private static Cache spriteCache;
	public int anInt239;
	public int children[];
	public int childX[];
	public boolean usableItemInterface;
	public Font fonts;
	public int invSpritePadY;
	public int valueCompareType[]; // anIntArray245
	public int anInt246;
	public int spritesY[];
	public String message;
	public boolean isInventoryInterface;
	public int id;
	public int invStackSizes[];
	public int inv[];
	public byte alpha; // aByte254
	private int anInt255;
	private int anInt256;
	public int anInt257;
	public int anInt258;
	public boolean aBoolean259;
	public Sprite sprite2;
	public int scrollMax;
	public int interfaceType; // type
	public int anInt263;
	private static final Cache modelCache = new Cache(30);
	public int anInt265;
	public boolean aBoolean266;
	public int height;
	public boolean aBoolean268;
	public int anInt269;
	public int anInt270;
	public int anInt271;
	public int childY[];
	public int fontId;
}
