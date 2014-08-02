
public final class ObjectDefinition {

    public static ObjectDefinition forId(int id) {
        for (int i = 0; i < 20; i++) {
            if (ObjectDefinition.cache[i].type == id) {
                return ObjectDefinition.cache[i];
            }
        }
        ObjectDefinition.cacheIndex = (ObjectDefinition.cacheIndex + 1) % 20;
        ObjectDefinition object = ObjectDefinition.cache[ObjectDefinition.cacheIndex];
        ObjectDefinition.buffer.offset = ObjectDefinition.objectIndices[id];
        object.type = id;
        object.setDefaults();
        object.readValues(ObjectDefinition.buffer);
        return object;
    }

    private void setDefaults() {
        modelIds = null;
        objectTypes = null;
        name = null;
        description = null;
        destColors = null;
        srcColors = null;
        ySize = 1;
        xSize = 1;
        unwalkable = true;
        solid = true;
        hasActions = false;
        flatTerrain = false;
        flatShading = false;
        aBoolean764 = false;
        animationId = -1;
        anInt775 = 16;
        aByte737 = 0;
        aByte742 = 0;
        actions = null;
        mapFunctionId = -1;
        mapSceneId = -1;
        aBoolean751 = false;
        aBoolean779 = true;
        scaleX = 128;
        scaleY = 128;
        scaleZ = 128;
        anInt768 = 0;
        offsetX = 0;
        offsetY = 0;
        offsetZ = 0;
        aBoolean736 = false;
        aBoolean766 = false;
        anInt760 = -1;
        varbitFileId = -1;
        anInt749 = -1;
        childIds = null;
    }

    public void method574(OnDemandFetcher onDemandFetcher) {
        if (modelIds == null) {
            return;
        }
        for (int element : modelIds) {
            onDemandFetcher.method560(element & 0xffff, 0);
        }
    }

    public static void dispose() {
        ObjectDefinition.cache1 = null;
        ObjectDefinition.cache2 = null;
        ObjectDefinition.objectIndices = null;
        ObjectDefinition.cache = null;
        ObjectDefinition.buffer = null;
    }

    public static void unpackConfig(Archive container) {
        ObjectDefinition.buffer = new Stream(container.get("loc.dat"));
        Stream stream = new Stream(container.get("loc.idx"));
        int totalObjects = stream.getUnsignedShort();
        ObjectDefinition.objectIndices = new int[totalObjects];
        int i = 2;
        for (int j = 0; j < totalObjects; j++) {
            ObjectDefinition.objectIndices[j] = i;
            i += stream.getUnsignedShort();
        }
        ObjectDefinition.cache = new ObjectDefinition[20];
        for (int j = 0; j < 20; j++) {
            ObjectDefinition.cache[j] = new ObjectDefinition();
        }
    }

    public boolean method577(int arg0) {
        if (objectTypes == null) {
            if (modelIds == null) {
                return true;
            }
            if (arg0 != 10) {
                return true;
            }
            boolean flag1 = true;
            for (int element : modelIds) {
                flag1 &= Model.isCached(element & 0xffff);
            }
            return flag1;
        }
        for (int j = 0; j < objectTypes.length; j++) {
            if (objectTypes[j] == arg0) {
                return Model.isCached(modelIds[j] & 0xffff);
            }
        }
        return true;
    }

    public Model method578(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6) {
        Model model = method581(arg0, arg6, arg1);
        if (model == null) {
            return null;
        }
        if (flatTerrain || flatShading) {
            model = new Model(flatTerrain, flatShading, model);
        }
        if (flatTerrain) {
            int i = (arg2 + arg3 + arg4 + arg5) / 4;
            for (int j = 0; j < model.vertexCount; j++) {
                int j2 = model.xVertex[j];
                int k2 = model.zVertex[j];
                int l2 = arg2 + (arg3 - arg2) * (j2 + 64) / 128;
                int i3 = arg5 + (arg4 - arg5) * (j2 + 64) / 128;
                int j3 = l2 + (i3 - l2) * (k2 + 64) / 128;
                model.yVertex[j] += j3 - i;
            }
            model.method467();
        }
        return model;
    }

    public boolean method579() {
        if (modelIds == null) {
            return true;
        }
        boolean flag1 = true;
        for (int element : modelIds) {
            flag1 &= Model.isCached(element & 0xffff);
        }
        return flag1;
    }

    public ObjectDefinition method580() {
        int i = -1;
        if (varbitFileId != -1) {
            VarBit varBit = VarBit.cache[varbitFileId];
            int j = varBit.configId;
            int k = varBit.anInt649;
            int l = varBit.anInt650;
            int i1 = Client.anIntArray1232[l - k];
            i = ObjectDefinition.client.configStates[j] >> k & i1;
        } else if (anInt749 != -1) {
            i = ObjectDefinition.client.configStates[anInt749];
        }
        if (i < 0 || i >= childIds.length || childIds[i] == -1) {
            return null;
        } else {
            return ObjectDefinition.forId(childIds[i]);
        }
    }

    private Model method581(int arg0, int arg1, int arg2) {
        Model model1 = null;
        long l;
        if (objectTypes == null) {
            if (arg0 != 10) {
                return null;
            }
            l = (type << 6) + arg2 + ((long) (arg1 + 1) << 32);
            Model model2 = (Model) ObjectDefinition.cache2.get(l);
            if (model2 != null) {
                return model2;
            }
            if (modelIds == null) {
                return null;
            }
            boolean flag = aBoolean751 ^ arg2 > 3;
            int modelCount = modelIds.length;
            for (int i = 0; i < modelCount; i++) {
                int id = modelIds[i];
                if (flag) {
                    id += 0x10000;
                }
                model1 = (Model) ObjectDefinition.cache1.get(id);
                if (model1 == null) {
                    model1 = Model.getModel(id & 0xffff);
                    if (model1 == null) {
                        return null;
                    }
                    if (flag) {
                        model1.method477();
                    }
                    ObjectDefinition.cache1.put(model1, id);
                }
                if (modelCount > 1) {
                    ObjectDefinition.models[i] = model1;
                }
            }
            if (modelCount > 1) {
                model1 = new Model(modelCount, ObjectDefinition.models);
            }
        } else {
            int i = -1;
            for (int id = 0; id < objectTypes.length; id++) {
                if (objectTypes[id] != arg0) {
                    continue;
                }
                i = id;
                break;
            }
            if (i == -1) {
                return null;
            }
            l = (type << 6) + (i << 3) + arg2 + ((long) (arg1 + 1) << 32);
            Model model2 = (Model) ObjectDefinition.cache2.get(l);
            if (model2 != null) {
                return model2;
            }
            int models = modelIds[i];
            boolean flag0 = aBoolean751 ^ arg2 > 3;
            if (flag0) {
                models += 0x10000;
            }
            model1 = (Model) ObjectDefinition.cache1.get(models);
            if (model1 == null) {
                model1 = Model.getModel(models & 0xffff);
                if (model1 == null) {
                    return null;
                }
                if (flag0) {
                    model1.method477();
                }
                ObjectDefinition.cache1.put(model1, models);
            }
        }
        boolean flag1;
        flag1 = scaleX != 128 || scaleY != 128 || scaleZ != 128;
        boolean flag2;
        flag2 = offsetX != 0 || offsetY != 0 || offsetZ != 0;
        Model model3 = new Model(destColors == null, Animation.isNullFrame(arg1), arg2 == 0 && arg1 == -1 && !flag1 && !flag2, model1);
        if (arg1 != -1) {
            model3.skin();
            model3.transform(arg1);
            model3.triangleSkin = null;
            model3.vertexSkins = null;
        }
        while (arg2-- > 0) {
            model3.method473();
        }
        if (destColors != null) {
            for (int k2 = 0; k2 < destColors.length; k2++) {
                model3.swapColors(destColors[k2], srcColors[k2]);
            }
        }
        if (flag1) {
            model3.scale(scaleX, scaleZ, scaleY);
        }
        if (flag2) {
            model3.translate(offsetX, offsetY, offsetZ);
        }
        // model3.processLighting(64 + aByte737, 768 + aByte742 * 5, -50, -10, -50, !aBoolean769);
        model3.processLighting(64 + aByte737, 1768 + aByte742 * 5, -50, -10, -50, !flatShading);
        if (anInt760 == 1) {
            model3.anInt1654 = model3.height;
        }
        ObjectDefinition.cache2.put(model3, l);
        return model3;
    }

    private void readValues(Stream buffer) {
        int i = -1;
        label0:
        do {
            int configId;
            do {
                configId = buffer.getUnsignedByte();
                if (configId == 0) {
                    break label0;
                }
                if (configId == 1) {
                    int k = buffer.getUnsignedByte();
                    if (k > 0) {
                        if (modelIds == null || ObjectDefinition.lowMem) {
                            objectTypes = new int[k];
                            modelIds = new int[k];
                            for (int k1 = 0; k1 < k; k1++) {
                                modelIds[k1] = buffer.getUnsignedShort();
                                objectTypes[k1] = buffer.getUnsignedByte();
                            }
                        } else {
                            buffer.offset += k * 3;
                        }
                    }
                } else if (configId == 2) {
                    name = buffer.getString();
                } else if (configId == 3) {
                    description = buffer.getStringBytes();
                } else if (configId == 5) {
                    int l = buffer.getUnsignedByte();
                    if (l > 0) {
                        if (modelIds == null || ObjectDefinition.lowMem) {
                            objectTypes = null;
                            modelIds = new int[l];
                            for (int l1 = 0; l1 < l; l1++) {
                                modelIds[l1] = buffer.getUnsignedShort();
                            }
                        } else {
                            buffer.offset += l * 2;
                        }
                    }
                } else if (configId == 14) {
                    ySize = buffer.getUnsignedByte();
                } else if (configId == 15) {
                    xSize = buffer.getUnsignedByte();
                } else if (configId == 17) {
                    unwalkable = false;
                } else if (configId == 18) {
                    solid = false;
                } else if (configId == 19) {
                    i = buffer.getUnsignedByte();
                    if (i == 1) {
                        hasActions = true;
                    }
                } else if (configId == 21) {
                    flatTerrain = true;
                } else if (configId == 22) {
                    flatShading = true;
                } else if (configId == 23) {
                    aBoolean764 = true;
                } else if (configId == 24) {
                    animationId = buffer.getUnsignedShort();
                    if (animationId == 65535) {
                        animationId = -1;
                    }
                } else if (configId == 28) {
                    anInt775 = buffer.getUnsignedByte();
                } else if (configId == 29) {
                    aByte737 = buffer.getByte();
                } else if (configId == 39) {
                    aByte742 = buffer.getByte();
                } else if (configId >= 30 && configId < 39) {
                    if (actions == null) {
                        actions = new String[5];
                    }
                    actions[configId - 30] = buffer.getString();
                    if (actions[configId - 30].equalsIgnoreCase("hidden")) {
                        actions[configId - 30] = null;
                    }
                } else if (configId == 40) {
                    int i1 = buffer.getUnsignedByte();
                    destColors = new int[i1];
                    srcColors = new int[i1];
                    for (int i2 = 0; i2 < i1; i2++) {
                        destColors[i2] = buffer.getUnsignedShort();
                        srcColors[i2] = buffer.getUnsignedShort();
                    }
                } else if (configId == 60) {
                    mapFunctionId = buffer.getUnsignedShort();
                } else if (configId == 62) {
                    aBoolean751 = true;
                } else if (configId == 64) {
                    aBoolean779 = false;
                } else if (configId == 65) {
                    scaleX = buffer.getUnsignedShort();
                } else if (configId == 66) {
                    scaleY = buffer.getUnsignedShort();
                } else if (configId == 67) {
                    scaleZ = buffer.getUnsignedShort();
                } else if (configId == 68) {
                    mapSceneId = buffer.getUnsignedShort();
                } else if (configId == 69) {
                    anInt768 = buffer.getUnsignedByte();
                } else if (configId == 70) {
                    offsetX = buffer.getShort();
                } else if (configId == 71) {
                    offsetY = buffer.getShort();
                } else if (configId == 72) {
                    offsetZ = buffer.getShort();
                } else if (configId == 73) {
                    aBoolean736 = true;
                } else if (configId == 74) {
                    aBoolean766 = true;
                } else {
                    if (configId != 75) {
                        continue;
                    }
                    anInt760 = buffer.getUnsignedByte();
                }
                continue label0;
            } while (configId != 77);
            varbitFileId = buffer.getUnsignedShort();
            if (varbitFileId == 65535) {
                varbitFileId = -1;
            }
            anInt749 = buffer.getUnsignedShort();
            if (anInt749 == 65535) {
                anInt749 = -1;
            }
            int j1 = buffer.getUnsignedByte();
            childIds = new int[j1 + 1];
            for (int j2 = 0; j2 <= j1; j2++) {
                childIds[j2] = buffer.getUnsignedShort();
                if (childIds[j2] == 65535) {
                    childIds[j2] = -1;
                }
            }
        } while (true);
        if (i == -1) {
            hasActions = modelIds != null && (objectTypes == null || objectTypes[0] == 10);
            if (actions != null) {
                hasActions = true;
            }
        }
        if (aBoolean766) {
            unwalkable = false;
            solid = false;
        }
        if (anInt760 == -1) {
            anInt760 = unwalkable ? 1 : 0;
        }
    }

    private ObjectDefinition() {
        type = -1;
    }
    public boolean aBoolean736;
    private byte aByte737;
    private int offsetX; // anInt738
    public String name;
    private int scaleZ; // anInt740
    private static final Model[] models = new Model[4];
    private byte aByte742;
    public int ySize;
    private int offsetY; // anInt745
    public int mapFunctionId; // anInt746
    private int[] srcColors;
    private int scaleX; // anInt748
    public int anInt749;
    private boolean aBoolean751;
    public static boolean lowMem;
    private static Stream buffer;
    public int type;
    private static int[] objectIndices;
    public boolean solid;
    public int mapSceneId; // anInt758
    public int childIds[];
    private int anInt760;
    public int xSize;
    public boolean flatTerrain; // aBoolean762
    public boolean aBoolean764;
    public static Client client;
    private boolean aBoolean766;
    public boolean unwalkable; // aBoolean767
    public int anInt768;
    private boolean flatShading; // aBoolean769
    private static int cacheIndex;
    private int scaleY; // anInt772
    private int[] modelIds; // anIntArray773
    public int varbitFileId;
    public int anInt775;
    private int[] objectTypes; // anIntArray776
    public byte description[];
    public boolean hasActions;
    public boolean aBoolean779;
    public static Cache cache2 = new Cache(30);
    public int animationId; // anInt781
    private static ObjectDefinition[] cache;
    private int offsetZ; // anInt783
    private int[] destColors;
    public static Cache cache1 = new Cache(500);
    public String actions[];
}
