final class SceneGraph {

	public SceneGraph(int heightmap[][][]) {
		int baseY = 104;
		int baseX = 104;
		int baseZ = 4;
		interactiveObjectCache = new InteractiveObject[5000];
		anIntArray486 = new int[10000];
		anIntArray487 = new int[10000];
		mapSizeZ = baseZ;
		mapSizeX = baseX;
		mapSizeY = baseY;
		groundTiles = new GroundTile[baseZ][baseX][baseY];
		anIntArrayArrayArray445 = new int[baseZ][baseX + 1][baseY + 1];
		heightMap = heightmap;
		reset();
	}

	public static void dispose() {
		SceneGraph.interactiveObjects = null;
		SceneGraph.cullingClusterPointer = null;
		SceneGraph.cullingClusters = null;
		SceneGraph.deque = null;
		SceneGraph.tileVisibilityMaps = null;
		SceneGraph.currentVisibilityMap = null;
	}

	public void reset() {
		for (int z = 0; z < mapSizeZ; z++) {
			for (int x = 0; x < mapSizeX; x++) {
				for (int y = 0; y < mapSizeY; y++) {
					groundTiles[z][x][y] = null;
				}
			}
		}
		for (int i = 0; i < SceneGraph.anInt472; i++) {
			for (int j = 0; j < SceneGraph.cullingClusterPointer[i]; j++) {
				SceneGraph.cullingClusters[i][j] = null;
			}
			SceneGraph.cullingClusterPointer[i] = 0;
		}
		for (int i = 0; i < interactiveObjectCachePos; i++) {
			interactiveObjectCache[i] = null;
		}
		interactiveObjectCachePos = 0;
		for (int i = 0; i < SceneGraph.interactiveObjects.length; i++) {
			SceneGraph.interactiveObjects[i] = null;
		}
	}

	public void method275(int z) {
		anInt442 = z;
		for (int x = 0; x < mapSizeX; x++) {
			for (int y = 0; y < mapSizeY; y++) {
				if (groundTiles[z][x][y] == null) {
					groundTiles[z][x][y] = new GroundTile(z, x, y);
				}
			}
		}
	}

	public void method276(int y, int x) {
		GroundTile groundTile = groundTiles[0][x][y];
		for (int z = 0; z < 3; z++) {
			GroundTile heightGroundTile = groundTiles[z][x][y] = groundTiles[z + 1][x][y];
			if (heightGroundTile != null) {
				heightGroundTile.anInt1307--;
				for (int j1 = 0; j1 < heightGroundTile.anInt1317; j1++) {
					InteractiveObject interactiveObject = heightGroundTile.interactiveObjects[j1];
					if ((interactiveObject.uid >> 29 & 3) == 2 && interactiveObject.x == x && interactiveObject.y == y) {
						interactiveObject.z--;
					}
				}
			}
		}
		if (groundTiles[0][x][y] == null) {
			groundTiles[0][x][y] = new GroundTile(0, x, y);
		}
		groundTiles[0][x][y].aGroundTile_1329 = groundTile;
		groundTiles[3][x][y] = null;
	}

	public static void method277(int pointer, int startX, int endZ, int endX, int endY, int startZ, int startY, int mask) {
		CullingCluster cullingCluster = new CullingCluster();
		cullingCluster.tileStartX = startX / 128;
		cullingCluster.tileEndX = endX / 128;
		cullingCluster.tileStartY = startY / 128;
		cullingCluster.tileEndY = endY / 128;
		cullingCluster.searchMask = mask;
		cullingCluster.worldStartX = startX;
		cullingCluster.worldEndX = endX;
		cullingCluster.worldStartY = startY;
		cullingCluster.worldEndY = endY;
		cullingCluster.worldStartZ = startZ;
		cullingCluster.worldEndZ = endZ;
		SceneGraph.cullingClusters[pointer][SceneGraph.cullingClusterPointer[pointer]++] = cullingCluster;
	}

	public void method278(int z, int x, int y, int arg3) {
		GroundTile groundTile = groundTiles[z][x][y];
		if (groundTile != null) {
			groundTiles[z][x][y].anInt1321 = arg3;
		}
	}

	public void method279(int z, int x, int y, int arg3, int arg4, int arg5, int arg6, int arg7, int arg8, int arg9, int arg10, int arg11, int arg12, int arg13, int arg14, int arg15, int arg16, int arg17, int arg18, int arg19) {
		if (arg3 == 0) {
			ShapedTile shapedTile = new ShapedTile(arg10, arg11, arg12, arg13, -1, arg18, false);
			for (int zIdx = z; zIdx >= 0; zIdx--) {
				if (groundTiles[zIdx][x][y] == null) {
					groundTiles[zIdx][x][y] = new GroundTile(zIdx, x, y);
				}
			}
			groundTiles[z][x][y].aShapedTile_1311 = shapedTile;
			return;
		}
		if (arg3 == 1) {
			ShapedTile shapedTile = new ShapedTile(arg14, arg15, arg16, arg17, arg5, arg19, arg6 == arg7 && arg6 == arg8 && arg6 == arg9);
			for (int zIdx = z; zIdx >= 0; zIdx--) {
				if (groundTiles[zIdx][x][y] == null) {
					groundTiles[zIdx][x][y] = new GroundTile(zIdx, x, y);
				}
			}
			groundTiles[z][x][y].aShapedTile_1311 = shapedTile;
			return;
		}
		Tile tile = new Tile(y, arg14, arg13, arg8, arg5, arg16, arg4, arg10, arg18, arg12, arg9, arg7, arg6, arg3, arg17, arg15, arg11, x, arg19);
		for (int k5 = z; k5 >= 0; k5--) {
			if (groundTiles[k5][x][y] == null) {
				groundTiles[k5][x][y] = new GroundTile(k5, x, y);
			}
		}
		groundTiles[z][x][y].aTile_1312 = tile;
	}

	public void method280(int tileZ, int z, int y, Entity entity, byte objectConfig, int uid, int x) {
		if (entity == null) {
			return;
		}
		GroundDecoration groundDecoration = new GroundDecoration();
		groundDecoration.entity = entity;
		groundDecoration.x = x * 128 + 64;
		groundDecoration.y = y * 128 + 64;
		groundDecoration.z = z;
		groundDecoration.uid = uid;
		groundDecoration.objectConfig = objectConfig;
		if (groundTiles[tileZ][x][y] == null) {
			groundTiles[tileZ][x][y] = new GroundTile(tileZ, x, y);
		}
		groundTiles[tileZ][x][y].groundDecoration = groundDecoration;
	}

	public void method281(int x, int uid, Entity entity0, int z, Entity entity1, Entity entity2, int tileZ, int y) {
		ItemPile itemPile = new ItemPile();
		itemPile.anItemPile_48 = entity2;
		itemPile.anInt46 = x * 128 + 64;
		itemPile.anInt47 = y * 128 + 64;
		itemPile.anInt45 = z;
		itemPile.uid = uid;
		itemPile.aEntity_49 = entity0;
		itemPile.aEntity_50 = entity1;
		int j1 = 0;
		GroundTile groundTile = groundTiles[tileZ][x][y];
		if (groundTile != null) {
			for (int i = 0; i < groundTile.anInt1317; i++) {
				if (groundTile.interactiveObjects[i].entity instanceof Model) {
					int l1 = ((Model) groundTile.interactiveObjects[i].entity).anInt1654;
					if (l1 > j1) {
						j1 = l1;
					}
				}
			}
		}
		itemPile.anInt52 = j1;
		if (groundTiles[tileZ][x][y] == null) {
			groundTiles[tileZ][x][y] = new GroundTile(tileZ, x, y);
		}
		groundTiles[tileZ][x][y].itemPile = itemPile;
	}

	public void method282(int orientation, Entity entity0, int uid, int y, byte objectConfig, int x, Entity entity1, int z, int orientation1, int off) {
		if (entity0 == null && entity1 == null) {
			return;
		}
		WallObject wallObject = new WallObject();
		wallObject.uid = uid;
		wallObject.objectConfig = objectConfig;
		wallObject.x = x * 128 + 64;
		wallObject.y = y * 128 + 64;
		wallObject.z = z;
		wallObject.aEntity_278 = entity0;
		wallObject.aEntity_279 = entity1;
		wallObject.orientation1 = orientation;
		wallObject.orientation2 = orientation1;
		for (int tileZ = off; tileZ >= 0; tileZ--) {
			if (groundTiles[tileZ][x][y] == null) {
				groundTiles[tileZ][x][y] = new GroundTile(tileZ, x, y);
			}
		}
		groundTiles[off][x][y].wallObject = wallObject;
	}

	public void method283(int uid, int y, int orientation2, int off, int arg4, int z, Entity entity, int x, byte objectConfig, int arg9, int orientation1) {
		if (entity == null) {
			return;
		}
		WallDecoration wallDecoration = new WallDecoration();
		wallDecoration.uid = uid;
		wallDecoration.objectConfig = objectConfig;
		wallDecoration.x = x * 128 + 64 + arg4;
		wallDecoration.y = y * 128 + 64 + arg9;
		wallDecoration.z = z;
		wallDecoration.entity = entity;
		wallDecoration.orientation1 = orientation1;
		wallDecoration.orientation2 = orientation2;
		for (int tileZ = off; tileZ >= 0; tileZ--) {
			if (groundTiles[tileZ][x][y] == null) {
				groundTiles[tileZ][x][y] = new GroundTile(tileZ, x, y);
			}
		}
		groundTiles[off][x][y].wallDecoration = wallDecoration;
	}

	public boolean method284(int arg0, byte arg1, int arg2, int arg3, Entity entity, int arg5, int arg6, int arg7, int arg8, int arg9) {
		if (entity == null) {
			return true;
		} else {
			int i2 = arg9 * 128 + 64 * arg5;
			int j2 = arg8 * 128 + 64 * arg3;
			return method287(arg6, arg9, arg8, arg5, arg3, i2, j2, arg2, entity, arg7, false, arg0, arg1);
		}
	}

	public boolean method285(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, Entity entity, boolean flag) {
		if (entity == null) {
			return true;
		}
		int l1 = arg6 - arg5;
		int i2 = arg4 - arg5;
		int j2 = arg6 + arg5;
		int k2 = arg4 + arg5;
		if (flag) {
			if (arg1 > 640 && arg1 < 1408) {
				k2 += 128;
			}
			if (arg1 > 1152 && arg1 < 1920) {
				j2 += 128;
			}
			if (arg1 > 1664 || arg1 < 384) {
				i2 -= 128;
			}
			if (arg1 > 128 && arg1 < 896) {
				l1 -= 128;
			}
		}
		l1 /= 128;
		i2 /= 128;
		j2 /= 128;
		k2 /= 128;
		return method287(arg0, l1, i2, j2 - l1 + 1, k2 - i2 + 1, arg6, arg4, arg2, entity, arg1, true, arg3, (byte) 0);
	}

	public boolean method286(int arg0, int arg1, Entity entity, int arg3, int arg4, int arg5, int arg6, int arg7, int arg8, int arg9, int arg10) {
		return entity == null || method287(arg0, arg7, arg10, arg8 - arg7 + 1, arg4 - arg10 + 1, arg5, arg1, arg6, entity, arg3, true, arg9, (byte) 0);
	}

	private boolean method287(int z, int x, int y, int arg3, int arg4, int arg5, int arg6, int arg7, Entity entity, int arg9, boolean flag, int uid, byte arg12) {
		for (int tileX = x; tileX < x + arg3; tileX++) {
			for (int tileY = y; tileY < y + arg4; tileY++) {
				if (tileX < 0 || tileY < 0 || tileX >= mapSizeX || tileY >= mapSizeY) {
					return false;
				}
				GroundTile groundTile = groundTiles[z][tileX][tileY];
				if (groundTile != null && groundTile.anInt1317 >= 5) {
					return false;
				}
			}
		}
		InteractiveObject interactiveObject = new InteractiveObject();
		interactiveObject.uid = uid;
		interactiveObject.objectConfig = arg12;
		interactiveObject.z = z;
		interactiveObject.anInt519 = arg5;
		interactiveObject.anInt520 = arg6;
		interactiveObject.anInt518 = arg7;
		interactiveObject.entity = entity;
		interactiveObject.anInt522 = arg9;
		interactiveObject.x = x;
		interactiveObject.y = y;
		interactiveObject.anInt524 = x + arg3 - 1;
		interactiveObject.anInt526 = y + arg4 - 1;
		for (int tileX = x; tileX < x + arg3; tileX++) {
			for (int tileY = y; tileY < y + arg4; tileY++) {
				int k3 = 0;
				if (tileX > x) {
					k3++;
				}
				if (tileX < x + arg3 - 1) {
					k3 += 4;
				}
				if (tileY > y) {
					k3 += 8;
				}
				if (tileY < y + arg4 - 1) {
					k3 += 2;
				}
				for (int tileZ = z; tileZ >= 0; tileZ--) {
					if (groundTiles[tileZ][tileX][tileY] == null) {
						groundTiles[tileZ][tileX][tileY] = new GroundTile(tileZ, tileX, tileY);
					}
				}
				GroundTile groundTile = groundTiles[z][tileX][tileY];
				groundTile.interactiveObjects[groundTile.anInt1317] = interactiveObject;
				groundTile.anIntArray1319[groundTile.anInt1317] = k3;
				groundTile.anInt1320 |= k3;
				groundTile.anInt1317++;
			}
		}
		if (flag) {
			interactiveObjectCache[interactiveObjectCachePos++] = interactiveObject;
		}
		return true;
	}

	public void resetInteractiveObject() {
		for (int i = 0; i < interactiveObjectCachePos; i++) {
			InteractiveObject interactiveObject = interactiveObjectCache[i];
			method289(interactiveObject);
			interactiveObjectCache[i] = null;
		}
		interactiveObjectCachePos = 0;
	}

	private void method289(InteractiveObject interactiveObject) {
		for (int x = interactiveObject.x; x <= interactiveObject.anInt524; x++) {
			for (int y = interactiveObject.y; y <= interactiveObject.anInt526; y++) {
				GroundTile groundTile = groundTiles[interactiveObject.z][x][y];
				if (groundTile != null) {
					for (int i = 0; i < groundTile.anInt1317; i++) {
						if (groundTile.interactiveObjects[i] != interactiveObject) {
							continue;
						}
						groundTile.anInt1317--;
						for (int j = i; j < groundTile.anInt1317; j++) {
							groundTile.interactiveObjects[j] = groundTile.interactiveObjects[j + 1];
							groundTile.anIntArray1319[j] = groundTile.anIntArray1319[j + 1];
						}
						groundTile.interactiveObjects[groundTile.anInt1317] = null;
						break;
					}
					groundTile.anInt1320 = 0;
					for (int i = 0; i < groundTile.anInt1317; i++) {
						groundTile.anInt1320 |= groundTile.anIntArray1319[i];
					}
				}
			}
		}
	}

	public void method290(int y, int arg1, int x, int z) {
		GroundTile groundTile = groundTiles[z][x][y];
		if (groundTile == null) {
			return;
		}
		WallDecoration wallDecoration = groundTile.wallDecoration;
		if (wallDecoration != null) {
			int offX = x * 128 + 64;
			int offY = y * 128 + 64;
			wallDecoration.x = offX + (wallDecoration.x - offX) * arg1 / 16;
			wallDecoration.y = offY + (wallDecoration.y - offY) * arg1 / 16;
		}
	}

	public void resetWallObject(int x, int z, int y) {
		GroundTile grondGroundTile = groundTiles[z][x][y];
		if (grondGroundTile != null) {
			grondGroundTile.wallObject = null;
		}
	}

	public void resetWallDecoration(int y, int z, int x) {
		GroundTile groundTile = groundTiles[z][x][y];
		if (groundTile != null) {
			groundTile.wallDecoration = null;
		}
	}

	public void method293(int z, int x, int y) {
		GroundTile groundTile = groundTiles[z][x][y];
		if (groundTile == null) {
			return;
		}
		for (int j1 = 0; j1 < groundTile.anInt1317; j1++) {
			InteractiveObject interactiveObject = groundTile.interactiveObjects[j1];
			if ((interactiveObject.uid >> 29 & 3) == 2 && interactiveObject.x == x && interactiveObject.y == y) {
				method289(interactiveObject);
				return;
			}
		}
	}

	public void resetGroundDecoration(int z, int y, int x) {
		GroundTile groundTile = groundTiles[z][x][y];
		if (groundTile == null) {
			return;
		}
		groundTile.groundDecoration = null;
	}

	public void resetItemPile(int z, int x, int y) {
		GroundTile groundTile = groundTiles[z][x][y];
		if (groundTile != null) {
			groundTile.itemPile = null;
		}
	}

	public WallObject getWallObject(int z, int x, int y) {
		GroundTile groundTile = groundTiles[z][x][y];
		if (groundTile == null) {
			return null;
		} else {
			return groundTile.wallObject;
		}
	}

	public WallDecoration getWallDecoration(int i, int k, int l) {
		GroundTile groundTile = groundTiles[l][i][k];
		if (groundTile == null) {
			return null;
		} else {
			return groundTile.wallDecoration;
		}
	}

	public InteractiveObject getInteractiveObject(int x, int y, int z) {
		GroundTile groundTile = groundTiles[z][x][y];
		if (groundTile == null) {
			return null;
		}
		for (int l = 0; l < groundTile.anInt1317; l++) {
			InteractiveObject interactiveObject = groundTile.interactiveObjects[l];
			if ((interactiveObject.uid >> 29 & 3) == 2 && interactiveObject.x == x && interactiveObject.y == y) {
				return interactiveObject;
			}
		}
		return null;
	}

	public GroundDecoration getGroundDecoration(int y, int x, int z) {
		GroundTile groundTile = groundTiles[z][x][y];
		if (groundTile == null || groundTile.groundDecoration == null) {
			return null;
		} else {
			return groundTile.groundDecoration;
		}
	}

	public int getWallObjectUid(int z, int x, int y) {
		GroundTile groundTile = groundTiles[z][x][y];
		if (groundTile == null || groundTile.wallObject == null) {
			return 0;
		} else {
			return groundTile.wallObject.uid;
		}
	}

	public int getWallDecorationUid(int z, int x, int y) {
		GroundTile groundTile = groundTiles[z][x][y];
		if (groundTile == null || groundTile.wallDecoration == null) {
			return 0;
		} else {
			return groundTile.wallDecoration.uid;
		}
	}

	public int getInteractiveObjectUid(int z, int x, int y) {
		GroundTile groundTile = groundTiles[z][x][y];
		if (groundTile == null) {
			return 0;
		}
		for (int l = 0; l < groundTile.anInt1317; l++) {
			InteractiveObject interactiveObject = groundTile.interactiveObjects[l];
			if ((interactiveObject.uid >> 29 & 3) == 2 && interactiveObject.x == x && interactiveObject.y == y) {
				return interactiveObject.uid;
			}
		}
		return 0;
	}

	public int getGroundDecorationUid(int z, int x, int y) {
		GroundTile groundTile = groundTiles[z][x][y];
		if (groundTile == null || groundTile.groundDecoration == null) {
			return 0;
		} else {
			return groundTile.groundDecoration.uid;
		}
	}

	public int method304(int z, int x, int y, int uid) {
		GroundTile groundTile = groundTiles[z][x][y];
		if (groundTile == null) {
			return -1;
		}
		if (groundTile.wallObject != null && groundTile.wallObject.uid == uid) {
			return groundTile.wallObject.objectConfig & 0xff;
		}
		if (groundTile.wallDecoration != null && groundTile.wallDecoration.uid == uid) {
			return groundTile.wallDecoration.objectConfig & 0xff;
		}
		if (groundTile.groundDecoration != null && groundTile.groundDecoration.uid == uid) {
			return groundTile.groundDecoration.objectConfig & 0xff;
		}
		for (int i1 = 0; i1 < groundTile.anInt1317; i1++) {
			if (groundTile.interactiveObjects[i1].uid == uid) {
				return groundTile.interactiveObjects[i1].objectConfig & 0xff;
			}
		}
		return -1;
	}

	public void method305(int lightnessY, int lightnessX, int lightnessZ) {
		int lightness = 64;
		// int shading = 768;
		int shading = 1768;
		int src_shad = (int) Math.sqrt(lightnessX * lightnessX + lightnessY * lightnessY + lightnessZ * lightnessZ);
		int dest_shad = shading * src_shad >> 8;
		for (int z = 0; z < mapSizeZ; z++) {
			for (int x = 0; x < mapSizeX; x++) {
				for (int y = 0; y < mapSizeY; y++) {
					GroundTile groundTile = groundTiles[z][x][y];
					if (groundTile != null) {
						WallObject wallObject = groundTile.wallObject;
						if (wallObject != null && wallObject.aEntity_278 != null && wallObject.aEntity_278.vertices != null) {
							method307(z, 1, 1, x, y, (Model) wallObject.aEntity_278);
							if (wallObject.aEntity_279 != null && wallObject.aEntity_279.vertices != null) {
								method307(z, 1, 1, x, y, (Model) wallObject.aEntity_279);
								method308((Model) wallObject.aEntity_278, (Model) wallObject.aEntity_279, 0, 0, 0, false);
								((Model) wallObject.aEntity_279).shade(lightness, dest_shad, lightnessX, lightnessY, lightnessZ);
							}
							((Model) wallObject.aEntity_278).shade(lightness, dest_shad, lightnessX, lightnessY, lightnessZ);
						}
						for (int i = 0; i < groundTile.anInt1317; i++) {
							InteractiveObject interactiveObject = groundTile.interactiveObjects[i];
							if (interactiveObject != null && interactiveObject.entity != null && interactiveObject.entity.vertices != null) {
								method307(z, interactiveObject.anInt524 - interactiveObject.x + 1, interactiveObject.anInt526 - interactiveObject.y + 1, x, y, (Model) interactiveObject.entity);
								((Model) interactiveObject.entity).shade(lightness, dest_shad, lightnessX, lightnessY, lightnessZ);
							}
						}
						GroundDecoration groundDecoration = groundTile.groundDecoration;
						if (groundDecoration != null && groundDecoration.entity.vertices != null) {
							method306(x, z, (Model) groundDecoration.entity, y);
							((Model) groundDecoration.entity).shade(lightness, dest_shad, lightnessX, lightnessY, lightnessZ);
						}
					}
				}
			}
		}
	}

	private void method306(int x, int z, Model model, int y) {
		if (x < mapSizeX) {
			GroundTile groundTile = groundTiles[z][x + 1][y];
			if (groundTile != null && groundTile.groundDecoration != null && groundTile.groundDecoration.entity.vertices != null) {
				method308(model, (Model) groundTile.groundDecoration.entity, 128, 0, 0, true);
			}
		}
		if (y < mapSizeX) {
			GroundTile groundTile = groundTiles[z][x][y + 1];
			if (groundTile != null && groundTile.groundDecoration != null && groundTile.groundDecoration.entity.vertices != null) {
				method308(model, (Model) groundTile.groundDecoration.entity, 0, 0, 128, true);
			}
		}
		if (x < mapSizeX && y < mapSizeY) {
			GroundTile groundTile = groundTiles[z][x + 1][y + 1];
			if (groundTile != null && groundTile.groundDecoration != null && groundTile.groundDecoration.entity.vertices != null) {
				method308(model, (Model) groundTile.groundDecoration.entity, 128, 0, 128, true);
			}
		}
		if (x < mapSizeX && y > 0) {
			GroundTile groundTile = groundTiles[z][x + 1][y - 1];
			if (groundTile != null && groundTile.groundDecoration != null && groundTile.groundDecoration.entity.vertices != null) {
				method308(model, (Model) groundTile.groundDecoration.entity, 128, 0, -128, true);
			}
		}
	}

	private void method307(int arg0, int arg1, int arg2, int arg3, int arg4, Model model) {
		boolean flag = true;
		int j1 = arg3;
		int k1 = arg3 + arg1;
		int l1 = arg4 - 1;
		int i2 = arg4 + arg2;
		for (int z = arg0; z <= arg0 + 1; z++) {
			if (z != mapSizeZ) {
				for (int x = j1; x <= k1; x++) {
					if (x >= 0 && x < mapSizeX) {
						for (int y = l1; y <= i2; y++) {
							if (y >= 0 && y < mapSizeY && (!flag || x >= k1 || y >= i2 || y < arg4 && x != arg3)) {
								GroundTile groundTile = groundTiles[z][x][y];
								if (groundTile != null) {
									int i3 = (heightMap[z][x][y] + heightMap[z][x + 1][y] + heightMap[z][x][y + 1] + heightMap[z][x + 1][y + 1]) / 4 - (heightMap[arg0][arg3][arg4] + heightMap[arg0][arg3 + 1][arg4] + heightMap[arg0][arg3][arg4 + 1] + heightMap[arg0][arg3 + 1][arg4 + 1]) / 4;
									WallObject wallObject = groundTile.wallObject;
									if (wallObject != null && wallObject.aEntity_278 != null && wallObject.aEntity_278.vertices != null) {
										method308(model, (Model) wallObject.aEntity_278, (x - arg3) * 128 + (1 - arg1) * 64, i3, (y - arg4) * 128 + (1 - arg2) * 64, flag);
									}
									if (wallObject != null && wallObject.aEntity_279 != null && wallObject.aEntity_279.vertices != null) {
										method308(model, (Model) wallObject.aEntity_279, (x - arg3) * 128 + (1 - arg1) * 64, i3, (y - arg4) * 128 + (1 - arg2) * 64, flag);
									}
									for (int i = 0; i < groundTile.anInt1317; i++) {
										InteractiveObject interactiveObject = groundTile.interactiveObjects[i];
										if (interactiveObject != null && interactiveObject.entity != null && interactiveObject.entity.vertices != null) {
											int k3 = interactiveObject.anInt524 - interactiveObject.x + 1;
											int l3 = interactiveObject.anInt526 - interactiveObject.y + 1;
											method308(model, (Model) interactiveObject.entity, (interactiveObject.x - arg3) * 128 + (k3 - arg1) * 64, i3, (interactiveObject.y - arg4) * 128 + (l3 - arg2) * 64, flag);
										}
									}
								}
							}
						}
					}
				}
				j1--;
				flag = false;
			}
		}
	}

	private void method308(Model model0, Model model1, int i, int j, int k, boolean flag) {
		anInt488++;
		int l = 0;
		int vertices[] = model1.xVertex;
		int i1 = model1.vertexCount;
		for (int j1 = 0; j1 < model0.vertexCount; j1++) {
			VertexNormal vertexNormal0 = model0.vertices[j1];
			VertexNormal vertexNormal1 = model0.vertices[j1];
			if (vertexNormal1.magnitude != 0) {
				int i2 = model0.yVertex[j1] - j;
				if (i2 <= model1.trimHeight) {
					int j2 = model0.xVertex[j1] - i;
					if (j2 >= model1.anInt1646 && j2 <= model1.anInt1647) {
						int k2 = model0.zVertex[j1] - k;
						if (k2 >= model1.anInt1649 && k2 <= model1.anInt1648) {
							for (int l2 = 0; l2 < i1; l2++) {
								VertexNormal vertexNormal2 = model1.vertices[l2];
								VertexNormal vertexNormal3 = model1.vertices[l2];
								if (j2 == vertices[l2] && k2 == model1.zVertex[l2] && i2 == model1.yVertex[l2] && vertexNormal3.magnitude != 0) {
									vertexNormal0.x += vertexNormal3.x;
									vertexNormal0.y += vertexNormal3.y;
									vertexNormal0.z += vertexNormal3.z;
									vertexNormal0.magnitude += vertexNormal3.magnitude;
									vertexNormal2.x += vertexNormal1.x;
									vertexNormal2.y += vertexNormal1.y;
									vertexNormal2.z += vertexNormal1.z;
									vertexNormal2.magnitude += vertexNormal1.magnitude;
									l++;
									anIntArray486[j1] = anInt488;
									anIntArray487[l2] = anInt488;
								}
							}
						}
					}
				}
			}
		}
		if (l < 3 || !flag) {
			return;
		}
		for (int k1 = 0; k1 < model0.triangleCount; k1++) {
			if (anIntArray486[model0.facePointA[k1]] == anInt488 && anIntArray486[model0.facePointB[k1]] == anInt488 && anIntArray486[model0.facePointC[k1]] == anInt488) {
				model0.faceTypes[k1] = -1;
			}
		}
		for (int l1 = 0; l1 < model1.triangleCount; l1++) {
			if (anIntArray487[model1.facePointA[l1]] == anInt488 && anIntArray487[model1.facePointB[l1]] == anInt488 && anIntArray487[model1.facePointC[l1]] == anInt488) {
				model1.faceTypes[l1] = -1;
			}
		}
	}

	public void method309(int arg0[], int arg1, int arg2, int arg3, int arg4) {
		int j = 512;
		GroundTile groundTile = groundTiles[arg2][arg3][arg4];
		if (groundTile == null) {
			return;
		}
		ShapedTile shapedTile = groundTile.aShapedTile_1311;
		if (shapedTile != null) {
			int j1 = shapedTile.anInt722;
			if (j1 == 0) {
				return;
			}
			for (int k1 = 0; k1 < 4; k1++) {
				arg0[arg1] = j1;
				arg0[arg1 + 1] = j1;
				arg0[arg1 + 2] = j1;
				arg0[arg1 + 3] = j1;
				arg1 += j;
			}
			return;
		}
		Tile tile = groundTile.aTile_1312;
		if (tile == null) {
			return;
		}
		int l1 = tile.anInt684;
		int i2 = tile.anInt685;
		int j2 = tile.anInt686;
		int k2 = tile.anInt687;
		int ai1[] = anIntArrayArray489[l1];
		int ai2[] = anIntArrayArray490[i2];
		int l2 = 0;
		if (j2 != 0) {
			for (int i3 = 0; i3 < 4; i3++) {
				arg0[arg1] = ai1[ai2[l2++]] != 0 ? k2 : j2;
				arg0[arg1 + 1] = ai1[ai2[l2++]] != 0 ? k2 : j2;
				arg0[arg1 + 2] = ai1[ai2[l2++]] != 0 ? k2 : j2;
				arg0[arg1 + 3] = ai1[ai2[l2++]] != 0 ? k2 : j2;
				arg1 += j;
			}
			return;
		}
		for (int j3 = 0; j3 < 4; j3++) {
			if (ai1[ai2[l2++]] != 0) {
				arg0[arg1] = k2;
			}
			if (ai1[ai2[l2++]] != 0) {
				arg0[arg1 + 1] = k2;
			}
			if (ai1[ai2[l2++]] != 0) {
				arg0[arg1 + 2] = k2;
			}
			if (ai1[ai2[l2++]] != 0) {
				arg0[arg1 + 3] = k2;
			}
			arg1 += j;
		}
	}

	public static void method310(int arg0, int arg1, int arg2, int arg3, int arg4[]) {
		SceneGraph.anInt495 = 0;
		SceneGraph.anInt496 = 0;
		SceneGraph.anInt497 = arg2;
		SceneGraph.anInt498 = arg3;
		SceneGraph.anInt493 = arg2 / 2;
		SceneGraph.anInt494 = arg3 / 2;
		boolean aflag[][][][] = new boolean[9][32][53][53];
		for (int i1 = 128; i1 <= 384; i1 += 32) {
			for (int j1 = 0; j1 < 2048; j1 += 64) {
				SceneGraph.anInt458 = Model.modelIntArray1[i1];
				SceneGraph.anInt459 = Model.modelIntArray2[i1];
				SceneGraph.anInt460 = Model.modelIntArray1[j1];
				SceneGraph.anInt461 = Model.modelIntArray2[j1];
				int l1 = (i1 - 128) / 32;
				int j2 = j1 / 64;
				for (int l2 = -26; l2 <= 26; l2++) {
					for (int j3 = -26; j3 <= 26; j3++) {
						int k3 = l2 * 128;
						int i4 = j3 * 128;
						boolean flag2 = false;
						for (int k4 = -arg0; k4 <= arg1; k4 += 128) {
							if (!SceneGraph.method311(arg4[l1] + k4, i4, k3)) {
								continue;
							}
							flag2 = true;
							break;
						}
						aflag[l1][j2][l2 + 25 + 1][j3 + 25 + 1] = flag2;
					}
				}
			}
		}
		for (int k1 = 0; k1 < 8; k1++) {
			for (int i2 = 0; i2 < 32; i2++) {
				for (int k2 = -25; k2 < 25; k2++) {
					for (int i3 = -25; i3 < 25; i3++) {
						boolean flag = false;
						label0: for (int l3 = -1; l3 <= 1; l3++) {
							for (int j4 = -1; j4 <= 1; j4++) {
								if (aflag[k1][i2][k2 + l3 + 25 + 1][i3 + j4 + 25 + 1]) {
									flag = true;
								} else if (aflag[k1][(i2 + 1) % 31][k2 + l3 + 25 + 1][i3 + j4 + 25 + 1]) {
									flag = true;
								} else if (aflag[k1 + 1][i2][k2 + l3 + 25 + 1][i3 + j4 + 25 + 1]) {
									flag = true;
								} else {
									if (!aflag[k1 + 1][(i2 + 1) % 31][k2 + l3 + 25 + 1][i3 + j4 + 25 + 1]) {
										continue;
									}
									flag = true;
								}
								break label0;
							}
						}
						SceneGraph.tileVisibilityMaps[k1][i2][k2 + 25][i3 + 25] = flag;
					}
				}
			}
		}
	}

	private static boolean method311(int arg0, int arg1, int arg2) {
		int l = arg1 * SceneGraph.anInt460 + arg2 * SceneGraph.anInt461 >> 16;
		int i1 = arg1 * SceneGraph.anInt461 - arg2 * SceneGraph.anInt460 >> 16;
		int j1 = arg0 * SceneGraph.anInt458 + i1 * SceneGraph.anInt459 >> 16;
		int k1 = arg0 * SceneGraph.anInt459 - i1 * SceneGraph.anInt458 >> 16;
		if (j1 < 50 || j1 > 3500) {
			return false;
		}
		int l1 = SceneGraph.anInt493 + (l << 9) / j1;
		int i2 = SceneGraph.anInt494 + (k1 << 9) / j1;
		return l1 >= SceneGraph.anInt495 && l1 <= SceneGraph.anInt497 && i2 >= SceneGraph.anInt496 && i2 <= SceneGraph.anInt498;
	}

	public void method312(int arg0, int arg1) {
		SceneGraph.aBoolean467 = true;
		SceneGraph.anInt468 = arg1;
		SceneGraph.anInt469 = arg0;
		SceneGraph.anInt470 = -1;
		SceneGraph.anInt471 = -1;
	}

	public void method313(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5) {
		if (arg0 < 0) {
			arg0 = 0;
		} else if (arg0 >= mapSizeX * 128) {
			arg0 = mapSizeX * 128 - 1;
		}
		if (arg1 < 0) {
			arg1 = 0;
		} else if (arg1 >= mapSizeY * 128) {
			arg1 = mapSizeY * 128 - 1;
		}
		SceneGraph.anInt448++;
		SceneGraph.anInt458 = Model.modelIntArray1[arg5];
		SceneGraph.anInt459 = Model.modelIntArray2[arg5];
		SceneGraph.anInt460 = Model.modelIntArray1[arg2];
		SceneGraph.anInt461 = Model.modelIntArray2[arg2];
		SceneGraph.currentVisibilityMap = SceneGraph.tileVisibilityMaps[(arg5 - 128) / 32][arg2 / 64];
		SceneGraph.anInt455 = arg0;
		SceneGraph.anInt456 = arg3;
		SceneGraph.anInt457 = arg1;
		SceneGraph.anInt453 = arg0 / 128;
		SceneGraph.anInt454 = arg1 / 128;
		SceneGraph.anInt447 = arg4;
		SceneGraph.anInt449 = SceneGraph.anInt453 - 25;
		if (SceneGraph.anInt449 < 0) {
			SceneGraph.anInt449 = 0;
		}
		SceneGraph.anInt451 = SceneGraph.anInt454 - 25;
		if (SceneGraph.anInt451 < 0) {
			SceneGraph.anInt451 = 0;
		}
		SceneGraph.anInt450 = SceneGraph.anInt453 + 25;
		if (SceneGraph.anInt450 > mapSizeX) {
			SceneGraph.anInt450 = mapSizeX;
		}
		SceneGraph.anInt452 = SceneGraph.anInt454 + 25;
		if (SceneGraph.anInt452 > mapSizeY) {
			SceneGraph.anInt452 = mapSizeY;
		}
		method319();
		SceneGraph.anInt446 = 0;
		for (int k1 = anInt442; k1 < mapSizeZ; k1++) {
			GroundTile aclass30_sub3[][] = groundTiles[k1];
			for (int i2 = SceneGraph.anInt449; i2 < SceneGraph.anInt450; i2++) {
				for (int k2 = SceneGraph.anInt451; k2 < SceneGraph.anInt452; k2++) {
					GroundTile groundTile = aclass30_sub3[i2][k2];
					if (groundTile != null) {
						if (groundTile.anInt1321 > arg4 || !SceneGraph.currentVisibilityMap[i2 - SceneGraph.anInt453 + 25][k2 - SceneGraph.anInt454 + 25] && heightMap[k1][i2][k2] - arg3 < 2000) {
							groundTile.aBoolean1322 = false;
							groundTile.aBoolean1323 = false;
							groundTile.anInt1325 = 0;
						} else {
							groundTile.aBoolean1322 = true;
							groundTile.aBoolean1323 = true;
							groundTile.aBoolean1324 = groundTile.anInt1317 > 0;
							SceneGraph.anInt446++;
						}
					}
				}
			}
		}
		for (int l1 = anInt442; l1 < mapSizeZ; l1++) {
			GroundTile groundTiles[][] = this.groundTiles[l1];
			for (int l2 = -25; l2 <= 0; l2++) {
				int i3 = SceneGraph.anInt453 + l2;
				int k3 = SceneGraph.anInt453 - l2;
				if (i3 >= SceneGraph.anInt449 || k3 < SceneGraph.anInt450) {
					for (int i4 = -25; i4 <= 0; i4++) {
						int k4 = SceneGraph.anInt454 + i4;
						int i5 = SceneGraph.anInt454 - i4;
						if (i3 >= SceneGraph.anInt449) {
							if (k4 >= SceneGraph.anInt451) {
								GroundTile groundTile = groundTiles[i3][k4];
								if (groundTile != null && groundTile.aBoolean1322) {
									method314(groundTile, true);
								}
							}
							if (i5 < SceneGraph.anInt452) {
								GroundTile groundTile = groundTiles[i3][i5];
								if (groundTile != null && groundTile.aBoolean1322) {
									method314(groundTile, true);
								}
							}
						}
						if (k3 < SceneGraph.anInt450) {
							if (k4 >= SceneGraph.anInt451) {
								GroundTile groundTile = groundTiles[k3][k4];
								if (groundTile != null && groundTile.aBoolean1322) {
									method314(groundTile, true);
								}
							}
							if (i5 < SceneGraph.anInt452) {
								GroundTile groundTile = groundTiles[k3][i5];
								if (groundTile != null && groundTile.aBoolean1322) {
									method314(groundTile, true);
								}
							}
						}
						if (SceneGraph.anInt446 == 0) {
							SceneGraph.aBoolean467 = false;
							return;
						}
					}
				}
			}
		}
		for (int j2 = anInt442; j2 < mapSizeZ; j2++) {
			GroundTile aclass30_sub3_2[][] = groundTiles[j2];
			for (int j3 = -25; j3 <= 0; j3++) {
				int l3 = SceneGraph.anInt453 + j3;
				int j4 = SceneGraph.anInt453 - j3;
				if (l3 >= SceneGraph.anInt449 || j4 < SceneGraph.anInt450) {
					for (int l4 = -25; l4 <= 0; l4++) {
						int j5 = SceneGraph.anInt454 + l4;
						int k5 = SceneGraph.anInt454 - l4;
						if (l3 >= SceneGraph.anInt449) {
							if (j5 >= SceneGraph.anInt451) {
								GroundTile groundTile = aclass30_sub3_2[l3][j5];
								if (groundTile != null && groundTile.aBoolean1322) {
									method314(groundTile, false);
								}
							}
							if (k5 < SceneGraph.anInt452) {
								GroundTile groundTile = aclass30_sub3_2[l3][k5];
								if (groundTile != null && groundTile.aBoolean1322) {
									method314(groundTile, false);
								}
							}
						}
						if (j4 < SceneGraph.anInt450) {
							if (j5 >= SceneGraph.anInt451) {
								GroundTile groundTile = aclass30_sub3_2[j4][j5];
								if (groundTile != null && groundTile.aBoolean1322) {
									method314(groundTile, false);
								}
							}
							if (k5 < SceneGraph.anInt452) {
								GroundTile groundTile = aclass30_sub3_2[j4][k5];
								if (groundTile != null && groundTile.aBoolean1322) {
									method314(groundTile, false);
								}
							}
						}
						if (SceneGraph.anInt446 == 0) {
							SceneGraph.aBoolean467 = false;
							return;
						}
					}
				}
			}
		}
		SceneGraph.aBoolean467 = false;
	}

	private void method314(GroundTile groundTile, boolean flag) {
		SceneGraph.deque.insertBack(groundTile);
		do {
			GroundTile groundTile1;
			do {
				groundTile1 = (GroundTile) SceneGraph.deque.popFront();
				if (groundTile1 == null) {
					return;
				}
			} while (!groundTile1.aBoolean1323);
			int i = groundTile1.anInt1308;
			int j = groundTile1.anInt1309;
			int k = groundTile1.anInt1307;
			int l = groundTile1.anInt1310;
			GroundTile aclass30_sub3[][] = groundTiles[k];
			if (groundTile1.aBoolean1322) {
				if (flag) {
					if (k > 0) {
						GroundTile groundTile2 = groundTiles[k - 1][i][j];
						if (groundTile2 != null && groundTile2.aBoolean1323) {
							continue;
						}
					}
					if (i <= SceneGraph.anInt453 && i > SceneGraph.anInt449) {
						GroundTile groundTile2 = aclass30_sub3[i - 1][j];
						if (groundTile2 != null && groundTile2.aBoolean1323 && (groundTile2.aBoolean1322 || (groundTile1.anInt1320 & 1) == 0)) {
							continue;
						}
					}
					if (i >= SceneGraph.anInt453 && i < SceneGraph.anInt450 - 1) {
						GroundTile groundTile2 = aclass30_sub3[i + 1][j];
						if (groundTile2 != null && groundTile2.aBoolean1323 && (groundTile2.aBoolean1322 || (groundTile1.anInt1320 & 4) == 0)) {
							continue;
						}
					}
					if (j <= SceneGraph.anInt454 && j > SceneGraph.anInt451) {
						GroundTile groundTile2 = aclass30_sub3[i][j - 1];
						if (groundTile2 != null && groundTile2.aBoolean1323 && (groundTile2.aBoolean1322 || (groundTile1.anInt1320 & 8) == 0)) {
							continue;
						}
					}
					if (j >= SceneGraph.anInt454 && j < SceneGraph.anInt452 - 1) {
						GroundTile groundTile2 = aclass30_sub3[i][j + 1];
						if (groundTile2 != null && groundTile2.aBoolean1323 && (groundTile2.aBoolean1322 || (groundTile1.anInt1320 & 2) == 0)) {
							continue;
						}
					}
				} else {
					flag = true;
				}
				groundTile1.aBoolean1322 = false;
				if (groundTile1.aGroundTile_1329 != null) {
					GroundTile groundTile2 = groundTile1.aGroundTile_1329;
					if (groundTile2.aShapedTile_1311 != null) {
						if (!method320(0, i, j)) {
							method315(groundTile2.aShapedTile_1311, 0, SceneGraph.anInt458, SceneGraph.anInt459, SceneGraph.anInt460, SceneGraph.anInt461, i, j);
						}
					} else if (groundTile2.aTile_1312 != null && !method320(0, i, j)) {
						method316(i, SceneGraph.anInt458, SceneGraph.anInt460, groundTile2.aTile_1312, SceneGraph.anInt459, j, SceneGraph.anInt461);
					}
					WallObject wallObject = groundTile2.wallObject;
					if (wallObject != null) {
						wallObject.aEntity_278.render(0, SceneGraph.anInt458, SceneGraph.anInt459, SceneGraph.anInt460, SceneGraph.anInt461, wallObject.x - SceneGraph.anInt455, wallObject.z - SceneGraph.anInt456, wallObject.y - SceneGraph.anInt457, wallObject.uid);
					}
					for (int i2 = 0; i2 < groundTile2.anInt1317; i2++) {
						InteractiveObject interactiveObject = groundTile2.interactiveObjects[i2];
						if (interactiveObject != null) {
							interactiveObject.entity.render(interactiveObject.anInt522, SceneGraph.anInt458, SceneGraph.anInt459, SceneGraph.anInt460, SceneGraph.anInt461, interactiveObject.anInt519 - SceneGraph.anInt455, interactiveObject.anInt518 - SceneGraph.anInt456, interactiveObject.anInt520 - SceneGraph.anInt457, interactiveObject.uid);
						}
					}
				}
				boolean flag1 = false;
				if (groundTile1.aShapedTile_1311 != null) {
					if (!method320(l, i, j)) {
						flag1 = true;
						method315(groundTile1.aShapedTile_1311, l, SceneGraph.anInt458, SceneGraph.anInt459, SceneGraph.anInt460, SceneGraph.anInt461, i, j);
					}
				} else if (groundTile1.aTile_1312 != null && !method320(l, i, j)) {
					flag1 = true;
					method316(i, SceneGraph.anInt458, SceneGraph.anInt460, groundTile1.aTile_1312, SceneGraph.anInt459, j, SceneGraph.anInt461);
				}
				int j1 = 0;
				int j2 = 0;
				WallObject wallObject = groundTile1.wallObject;
				WallDecoration wallDecoration = groundTile1.wallDecoration;
				if (wallObject != null || wallDecoration != null) {
					if (SceneGraph.anInt453 == i) {
						j1++;
					} else if (SceneGraph.anInt453 < i) {
						j1 += 2;
					}
					if (SceneGraph.anInt454 == j) {
						j1 += 3;
					} else if (SceneGraph.anInt454 > j) {
						j1 += 6;
					}
					j2 = SceneGraph.anIntArray478[j1];
					groundTile1.anInt1328 = SceneGraph.anIntArray480[j1];
				}
				if (wallObject != null) {
					if ((wallObject.orientation1 & SceneGraph.anIntArray479[j1]) != 0) {
						if (wallObject.orientation1 == 16) {
							groundTile1.anInt1325 = 3;
							groundTile1.anInt1326 = SceneGraph.anIntArray481[j1];
							groundTile1.anInt1327 = 3 - groundTile1.anInt1326;
						} else if (wallObject.orientation1 == 32) {
							groundTile1.anInt1325 = 6;
							groundTile1.anInt1326 = SceneGraph.anIntArray482[j1];
							groundTile1.anInt1327 = 6 - groundTile1.anInt1326;
						} else if (wallObject.orientation1 == 64) {
							groundTile1.anInt1325 = 12;
							groundTile1.anInt1326 = SceneGraph.anIntArray483[j1];
							groundTile1.anInt1327 = 12 - groundTile1.anInt1326;
						} else {
							groundTile1.anInt1325 = 9;
							groundTile1.anInt1326 = SceneGraph.anIntArray484[j1];
							groundTile1.anInt1327 = 9 - groundTile1.anInt1326;
						}
					} else {
						groundTile1.anInt1325 = 0;
					}
					if ((wallObject.orientation1 & j2) != 0 && !method321(l, i, j, wallObject.orientation1)) {
						wallObject.aEntity_278.render(0, SceneGraph.anInt458, SceneGraph.anInt459, SceneGraph.anInt460, SceneGraph.anInt461, wallObject.x - SceneGraph.anInt455, wallObject.z - SceneGraph.anInt456, wallObject.y - SceneGraph.anInt457, wallObject.uid);
					}
					if ((wallObject.orientation2 & j2) != 0 && !method321(l, i, j, wallObject.orientation2)) {
						wallObject.aEntity_279.render(0, SceneGraph.anInt458, SceneGraph.anInt459, SceneGraph.anInt460, SceneGraph.anInt461, wallObject.x - SceneGraph.anInt455, wallObject.z - SceneGraph.anInt456, wallObject.y - SceneGraph.anInt457, wallObject.uid);
					}
				}
				if (wallDecoration != null && !method322(l, i, j, wallDecoration.entity.height)) {
					if ((wallDecoration.orientation1 & j2) != 0) {
						wallDecoration.entity.render(wallDecoration.orientation2, SceneGraph.anInt458, SceneGraph.anInt459, SceneGraph.anInt460, SceneGraph.anInt461, wallDecoration.x - SceneGraph.anInt455, wallDecoration.z - SceneGraph.anInt456, wallDecoration.y - SceneGraph.anInt457, wallDecoration.uid);
					} else if ((wallDecoration.orientation1 & 0x300) != 0) {
						int j4 = wallDecoration.x - SceneGraph.anInt455;
						int l5 = wallDecoration.z - SceneGraph.anInt456;
						int k6 = wallDecoration.y - SceneGraph.anInt457;
						int i8 = wallDecoration.orientation2;
						int k9;
						if (i8 == 1 || i8 == 2) {
							k9 = -j4;
						} else {
							k9 = j4;
						}
						int k10;
						if (i8 == 2 || i8 == 3) {
							k10 = -k6;
						} else {
							k10 = k6;
						}
						if ((wallDecoration.orientation1 & 0x100) != 0 && k10 < k9) {
							int i11 = j4 + SceneGraph.anIntArray463[i8];
							int k11 = k6 + SceneGraph.anIntArray464[i8];
							wallDecoration.entity.render(i8 * 512 + 256, SceneGraph.anInt458, SceneGraph.anInt459, SceneGraph.anInt460, SceneGraph.anInt461, i11, l5, k11, wallDecoration.uid);
						}
						if ((wallDecoration.orientation1 & 0x200) != 0 && k10 > k9) {
							int j11 = j4 + SceneGraph.anIntArray465[i8];
							int l11 = k6 + SceneGraph.anIntArray466[i8];
							wallDecoration.entity.render(i8 * 512 + 1280 & 0x7ff, SceneGraph.anInt458, SceneGraph.anInt459, SceneGraph.anInt460, SceneGraph.anInt461, j11, l5, l11, wallDecoration.uid);
						}
					}
				}
				if (flag1) {
					GroundDecoration groundDecoration = groundTile1.groundDecoration;
					if (groundDecoration != null) {
						groundDecoration.entity.render(0, SceneGraph.anInt458, SceneGraph.anInt459, SceneGraph.anInt460, SceneGraph.anInt461, groundDecoration.x - SceneGraph.anInt455, groundDecoration.z - SceneGraph.anInt456, groundDecoration.y - SceneGraph.anInt457, groundDecoration.uid);
					}
					ItemPile itemPile = groundTile1.itemPile;
					if (itemPile != null && itemPile.anInt52 == 0) {
						if (itemPile.aEntity_49 != null) {
							itemPile.aEntity_49.render(0, SceneGraph.anInt458, SceneGraph.anInt459, SceneGraph.anInt460, SceneGraph.anInt461, itemPile.anInt46 - SceneGraph.anInt455, itemPile.anInt45 - SceneGraph.anInt456, itemPile.anInt47 - SceneGraph.anInt457, itemPile.uid);
						}
						if (itemPile.aEntity_50 != null) {
							itemPile.aEntity_50.render(0, SceneGraph.anInt458, SceneGraph.anInt459, SceneGraph.anInt460, SceneGraph.anInt461, itemPile.anInt46 - SceneGraph.anInt455, itemPile.anInt45 - SceneGraph.anInt456, itemPile.anInt47 - SceneGraph.anInt457, itemPile.uid);
						}
						if (itemPile.anItemPile_48 != null) {
							itemPile.anItemPile_48.render(0, SceneGraph.anInt458, SceneGraph.anInt459, SceneGraph.anInt460, SceneGraph.anInt461, itemPile.anInt46 - SceneGraph.anInt455, itemPile.anInt45 - SceneGraph.anInt456, itemPile.anInt47 - SceneGraph.anInt457, itemPile.uid);
						}
					}
				}
				int k4 = groundTile1.anInt1320;
				if (k4 != 0) {
					if (i < SceneGraph.anInt453 && (k4 & 4) != 0) {
						GroundTile groundTile2 = aclass30_sub3[i + 1][j];
						if (groundTile2 != null && groundTile2.aBoolean1323) {
							SceneGraph.deque.insertBack(groundTile2);
						}
					}
					if (j < SceneGraph.anInt454 && (k4 & 2) != 0) {
						GroundTile groundTile2 = aclass30_sub3[i][j + 1];
						if (groundTile2 != null && groundTile2.aBoolean1323) {
							SceneGraph.deque.insertBack(groundTile2);
						}
					}
					if (i > SceneGraph.anInt453 && (k4 & 1) != 0) {
						GroundTile groundTile2 = aclass30_sub3[i - 1][j];
						if (groundTile2 != null && groundTile2.aBoolean1323) {
							SceneGraph.deque.insertBack(groundTile2);
						}
					}
					if (j > SceneGraph.anInt454 && (k4 & 8) != 0) {
						GroundTile groundTile2 = aclass30_sub3[i][j - 1];
						if (groundTile2 != null && groundTile2.aBoolean1323) {
							SceneGraph.deque.insertBack(groundTile2);
						}
					}
				}
			}
			if (groundTile1.anInt1325 != 0) {
				boolean flag2 = true;
				for (int k1 = 0; k1 < groundTile1.anInt1317; k1++) {
					if (groundTile1.interactiveObjects[k1].anInt528 == SceneGraph.anInt448 || (groundTile1.anIntArray1319[k1] & groundTile1.anInt1325) != groundTile1.anInt1326) {
						continue;
					}
					flag2 = false;
					break;
				}
				if (flag2) {
					WallObject wallObject = groundTile1.wallObject;
					if (!method321(l, i, j, wallObject.orientation1)) {
						wallObject.aEntity_278.render(0, SceneGraph.anInt458, SceneGraph.anInt459, SceneGraph.anInt460, SceneGraph.anInt461, wallObject.x - SceneGraph.anInt455, wallObject.z - SceneGraph.anInt456, wallObject.y - SceneGraph.anInt457, wallObject.uid);
					}
					groundTile1.anInt1325 = 0;
				}
			}
			if (groundTile1.aBoolean1324) {
				try {
					int i1 = groundTile1.anInt1317;
					groundTile1.aBoolean1324 = false;
					int l1 = 0;
					label0: for (int k2 = 0; k2 < i1; k2++) {
						InteractiveObject interactiveObject = groundTile1.interactiveObjects[k2];
						if (interactiveObject.anInt528 == SceneGraph.anInt448) {
							continue;
						}
						for (int k3 = interactiveObject.x; k3 <= interactiveObject.anInt524; k3++) {
							for (int l4 = interactiveObject.y; l4 <= interactiveObject.anInt526; l4++) {
								GroundTile groundTile2 = aclass30_sub3[k3][l4];
								if (groundTile2.aBoolean1322) {
									groundTile1.aBoolean1324 = true;
								} else {
									if (groundTile2.anInt1325 == 0) {
										continue;
									}
									int l6 = 0;
									if (k3 > interactiveObject.x) {
										l6++;
									}
									if (k3 < interactiveObject.anInt524) {
										l6 += 4;
									}
									if (l4 > interactiveObject.y) {
										l6 += 8;
									}
									if (l4 < interactiveObject.anInt526) {
										l6 += 2;
									}
									if ((l6 & groundTile2.anInt1325) != groundTile1.anInt1327) {
										continue;
									}
									groundTile1.aBoolean1324 = true;
								}
								continue label0;
							}
						}
						SceneGraph.interactiveObjects[l1++] = interactiveObject;
						int i5 = SceneGraph.anInt453 - interactiveObject.x;
						int i6 = interactiveObject.anInt524 - SceneGraph.anInt453;
						if (i6 > i5) {
							i5 = i6;
						}
						int i7 = SceneGraph.anInt454 - interactiveObject.y;
						int j8 = interactiveObject.anInt526 - SceneGraph.anInt454;
						if (j8 > i7) {
							interactiveObject.anInt527 = i5 + j8;
						} else {
							interactiveObject.anInt527 = i5 + i7;
						}
					}
					while (l1 > 0) {
						int i3 = -50;
						int l3 = -1;
						for (int j5 = 0; j5 < l1; j5++) {
							InteractiveObject interactiveObject = SceneGraph.interactiveObjects[j5];
							if (interactiveObject.anInt528 != SceneGraph.anInt448) {
								if (interactiveObject.anInt527 > i3) {
									i3 = interactiveObject.anInt527;
									l3 = j5;
								} else if (interactiveObject.anInt527 == i3) {
									int j7 = interactiveObject.anInt519 - SceneGraph.anInt455;
									int k8 = interactiveObject.anInt520 - SceneGraph.anInt457;
									int l9 = SceneGraph.interactiveObjects[l3].anInt519 - SceneGraph.anInt455;
									int l10 = SceneGraph.interactiveObjects[l3].anInt520 - SceneGraph.anInt457;
									if (j7 * j7 + k8 * k8 > l9 * l9 + l10 * l10) {
										l3 = j5;
									}
								}
							}
						}
						if (l3 == -1) {
							break;
						}
						InteractiveObject interactiveObject = SceneGraph.interactiveObjects[l3];
						interactiveObject.anInt528 = SceneGraph.anInt448;
						if (!method323(l, interactiveObject.x, interactiveObject.anInt524, interactiveObject.y, interactiveObject.anInt526, interactiveObject.entity.height)) {
							interactiveObject.entity.render(interactiveObject.anInt522, SceneGraph.anInt458, SceneGraph.anInt459, SceneGraph.anInt460, SceneGraph.anInt461, interactiveObject.anInt519 - SceneGraph.anInt455, interactiveObject.anInt518 - SceneGraph.anInt456, interactiveObject.anInt520 - SceneGraph.anInt457, interactiveObject.uid);
						}
						for (int k7 = interactiveObject.x; k7 <= interactiveObject.anInt524; k7++) {
							for (int l8 = interactiveObject.y; l8 <= interactiveObject.anInt526; l8++) {
								GroundTile groundTile_22 = aclass30_sub3[k7][l8];
								if (groundTile_22.anInt1325 != 0) {
									SceneGraph.deque.insertBack(groundTile_22);
								} else if ((k7 != i || l8 != j) && groundTile_22.aBoolean1323) {
									SceneGraph.deque.insertBack(groundTile_22);
								}
							}
						}
					}
					if (groundTile1.aBoolean1324) {
						continue;
					}
				} catch (Exception exception) {
					groundTile1.aBoolean1324 = false;
				}
			}
			if (!groundTile1.aBoolean1323 || groundTile1.anInt1325 != 0) {
				continue;
			}
			if (i <= SceneGraph.anInt453 && i > SceneGraph.anInt449) {
				GroundTile groundTile2 = aclass30_sub3[i - 1][j];
				if (groundTile2 != null && groundTile2.aBoolean1323) {
					continue;
				}
			}
			if (i >= SceneGraph.anInt453 && i < SceneGraph.anInt450 - 1) {
				GroundTile groundTile2 = aclass30_sub3[i + 1][j];
				if (groundTile2 != null && groundTile2.aBoolean1323) {
					continue;
				}
			}
			if (j <= SceneGraph.anInt454 && j > SceneGraph.anInt451) {
				GroundTile groundTile2 = aclass30_sub3[i][j - 1];
				if (groundTile2 != null && groundTile2.aBoolean1323) {
					continue;
				}
			}
			if (j >= SceneGraph.anInt454 && j < SceneGraph.anInt452 - 1) {
				GroundTile groundTile2 = aclass30_sub3[i][j + 1];
				if (groundTile2 != null && groundTile2.aBoolean1323) {
					continue;
				}
			}
			groundTile1.aBoolean1323 = false;
			SceneGraph.anInt446--;
			ItemPile itemPile = groundTile1.itemPile;
			if (itemPile != null && itemPile.anInt52 != 0) {
				if (itemPile.aEntity_49 != null) {
					itemPile.aEntity_49.render(0, SceneGraph.anInt458, SceneGraph.anInt459, SceneGraph.anInt460, SceneGraph.anInt461, itemPile.anInt46 - SceneGraph.anInt455, itemPile.anInt45 - SceneGraph.anInt456 - itemPile.anInt52, itemPile.anInt47 - SceneGraph.anInt457, itemPile.uid);
				}
				if (itemPile.aEntity_50 != null) {
					itemPile.aEntity_50.render(0, SceneGraph.anInt458, SceneGraph.anInt459, SceneGraph.anInt460, SceneGraph.anInt461, itemPile.anInt46 - SceneGraph.anInt455, itemPile.anInt45 - SceneGraph.anInt456 - itemPile.anInt52, itemPile.anInt47 - SceneGraph.anInt457, itemPile.uid);
				}
				if (itemPile.anItemPile_48 != null) {
					itemPile.anItemPile_48.render(0, SceneGraph.anInt458, SceneGraph.anInt459, SceneGraph.anInt460, SceneGraph.anInt461, itemPile.anInt46 - SceneGraph.anInt455, itemPile.anInt45 - SceneGraph.anInt456 - itemPile.anInt52, itemPile.anInt47 - SceneGraph.anInt457, itemPile.uid);
				}
			}
			if (groundTile1.anInt1328 != 0) {
				WallDecoration wallDecoration = groundTile1.wallDecoration;
				if (wallDecoration != null && !method322(l, i, j, wallDecoration.entity.height)) {
					if ((wallDecoration.orientation1 & groundTile1.anInt1328) != 0) {
						wallDecoration.entity.render(wallDecoration.orientation2, SceneGraph.anInt458, SceneGraph.anInt459, SceneGraph.anInt460, SceneGraph.anInt461, wallDecoration.x - SceneGraph.anInt455, wallDecoration.z - SceneGraph.anInt456, wallDecoration.y - SceneGraph.anInt457, wallDecoration.uid);
					} else if ((wallDecoration.orientation1 & 0x300) != 0) {
						int l2 = wallDecoration.x - SceneGraph.anInt455;
						int j3 = wallDecoration.z - SceneGraph.anInt456;
						int i4 = wallDecoration.y - SceneGraph.anInt457;
						int k5 = wallDecoration.orientation2;
						int j6;
						if (k5 == 1 || k5 == 2) {
							j6 = -l2;
						} else {
							j6 = l2;
						}
						int l7;
						if (k5 == 2 || k5 == 3) {
							l7 = -i4;
						} else {
							l7 = i4;
						}
						if ((wallDecoration.orientation1 & 0x100) != 0 && l7 >= j6) {
							int i9 = l2 + SceneGraph.anIntArray463[k5];
							int i10 = i4 + SceneGraph.anIntArray464[k5];
							wallDecoration.entity.render(k5 * 512 + 256, SceneGraph.anInt458, SceneGraph.anInt459, SceneGraph.anInt460, SceneGraph.anInt461, i9, j3, i10, wallDecoration.uid);
						}
						if ((wallDecoration.orientation1 & 0x200) != 0 && l7 <= j6) {
							int j9 = l2 + SceneGraph.anIntArray465[k5];
							int j10 = i4 + SceneGraph.anIntArray466[k5];
							wallDecoration.entity.render(k5 * 512 + 1280 & 0x7ff, SceneGraph.anInt458, SceneGraph.anInt459, SceneGraph.anInt460, SceneGraph.anInt461, j9, j3, j10, wallDecoration.uid);
						}
					}
				}
				WallObject wallObject = groundTile1.wallObject;
				if (wallObject != null) {
					if ((wallObject.orientation2 & groundTile1.anInt1328) != 0 && !method321(l, i, j, wallObject.orientation2)) {
						wallObject.aEntity_279.render(0, SceneGraph.anInt458, SceneGraph.anInt459, SceneGraph.anInt460, SceneGraph.anInt461, wallObject.x - SceneGraph.anInt455, wallObject.z - SceneGraph.anInt456, wallObject.y - SceneGraph.anInt457, wallObject.uid);
					}
					if ((wallObject.orientation1 & groundTile1.anInt1328) != 0 && !method321(l, i, j, wallObject.orientation1)) {
						wallObject.aEntity_278.render(0, SceneGraph.anInt458, SceneGraph.anInt459, SceneGraph.anInt460, SceneGraph.anInt461, wallObject.x - SceneGraph.anInt455, wallObject.z - SceneGraph.anInt456, wallObject.y - SceneGraph.anInt457, wallObject.uid);
					}
				}
			}
			if (k < mapSizeZ - 1) {
				GroundTile groundTile2 = groundTiles[k + 1][i][j];
				if (groundTile2 != null && groundTile2.aBoolean1323) {
					SceneGraph.deque.insertBack(groundTile2);
				}
			}
			if (i < SceneGraph.anInt453) {
				GroundTile groundTile2 = aclass30_sub3[i + 1][j];
				if (groundTile2 != null && groundTile2.aBoolean1323) {
					SceneGraph.deque.insertBack(groundTile2);
				}
			}
			if (j < SceneGraph.anInt454) {
				GroundTile groundTile2 = aclass30_sub3[i][j + 1];
				if (groundTile2 != null && groundTile2.aBoolean1323) {
					SceneGraph.deque.insertBack(groundTile2);
				}
			}
			if (i > SceneGraph.anInt453) {
				GroundTile groundTile2 = aclass30_sub3[i - 1][j];
				if (groundTile2 != null && groundTile2.aBoolean1323) {
					SceneGraph.deque.insertBack(groundTile2);
				}
			}
			if (j > SceneGraph.anInt454) {
				GroundTile groundTile2 = aclass30_sub3[i][j - 1];
				if (groundTile2 != null && groundTile2.aBoolean1323) {
					SceneGraph.deque.insertBack(groundTile2);
				}
			}
		} while (true);
	}

	private void method315(ShapedTile shapedTile, int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6) {
		int l1;
		int i2 = l1 = (arg5 << 7) - SceneGraph.anInt455;
		int j2;
		int k2 = j2 = (arg6 << 7) - SceneGraph.anInt457;
		int l2;
		int i3 = l2 = i2 + 128;
		int j3;
		int k3 = j3 = k2 + 128;
		int l3 = heightMap[arg0][arg5][arg6] - SceneGraph.anInt456;
		int i4 = heightMap[arg0][arg5 + 1][arg6] - SceneGraph.anInt456;
		int j4 = heightMap[arg0][arg5 + 1][arg6 + 1] - SceneGraph.anInt456;
		int k4 = heightMap[arg0][arg5][arg6 + 1] - SceneGraph.anInt456;
		int l4 = k2 * arg3 + i2 * arg4 >> 16;
		k2 = k2 * arg4 - i2 * arg3 >> 16;
		i2 = l4;
		l4 = l3 * arg2 - k2 * arg1 >> 16;
		k2 = l3 * arg1 + k2 * arg2 >> 16;
		l3 = l4;
		if (k2 < 50) {
			return;
		}
		l4 = j2 * arg3 + i3 * arg4 >> 16;
		j2 = j2 * arg4 - i3 * arg3 >> 16;
		i3 = l4;
		l4 = i4 * arg2 - j2 * arg1 >> 16;
		j2 = i4 * arg1 + j2 * arg2 >> 16;
		i4 = l4;
		if (j2 < 50) {
			return;
		}
		l4 = k3 * arg3 + l2 * arg4 >> 16;
		k3 = k3 * arg4 - l2 * arg3 >> 16;
		l2 = l4;
		l4 = j4 * arg2 - k3 * arg1 >> 16;
		k3 = j4 * arg1 + k3 * arg2 >> 16;
		j4 = l4;
		if (k3 < 50) {
			return;
		}
		l4 = j3 * arg3 + l1 * arg4 >> 16;
		j3 = j3 * arg4 - l1 * arg3 >> 16;
		l1 = l4;
		l4 = k4 * arg2 - j3 * arg1 >> 16;
		j3 = k4 * arg1 + j3 * arg2 >> 16;
		k4 = l4;
		if (j3 < 50) {
			return;
		}
		int i5 = Rasterizer.centerX + (i2 << 9) / k2;
		int j5 = Rasterizer.centerY + (l3 << 9) / k2;
		int k5 = Rasterizer.centerX + (i3 << 9) / j2;
		int l5 = Rasterizer.centerY + (i4 << 9) / j2;
		int i6 = Rasterizer.centerX + (l2 << 9) / k3;
		int j6 = Rasterizer.centerY + (j4 << 9) / k3;
		int k6 = Rasterizer.centerX + (l1 << 9) / j3;
		int l6 = Rasterizer.centerY + (k4 << 9) / j3;
		Rasterizer.alpha = 0;
		if ((i6 - k6) * (l5 - l6) - (j6 - l6) * (k5 - k6) > 0) {
			Rasterizer.edgeRestricted = i6 < 0 || k6 < 0 || k5 < 0 || i6 > Graphics2D.endX || k6 > Graphics2D.endX || k5 > Graphics2D.endX;
			if (SceneGraph.aBoolean467 && method318(SceneGraph.anInt468, SceneGraph.anInt469, j6, l6, l5, i6, k6, k5)) {
				SceneGraph.anInt470 = arg5;
				SceneGraph.anInt471 = arg6;
			}
			if (shapedTile.anInt720 == -1) {
				if (shapedTile.anInt718 != 0xbc614e) {
					Rasterizer.drawShadedTriangle(j6, l6, l5, i6, k6, k5, shapedTile.anInt718, shapedTile.anInt719, shapedTile.anInt717);
				}
			} else if (!SceneGraph.lowMem) {
				if (shapedTile.aBoolean721) {
					Rasterizer.drawTexturedTriangle(j6, l6, l5, i6, k6, k5, shapedTile.anInt718, shapedTile.anInt719, shapedTile.anInt717, i2, i3, l1, l3, i4, k4, k2, j2, j3, shapedTile.anInt720);
				} else {
					Rasterizer.drawTexturedTriangle(j6, l6, l5, i6, k6, k5, shapedTile.anInt718, shapedTile.anInt719, shapedTile.anInt717, l2, l1, i3, j4, k4, i4, k3, j3, j2, shapedTile.anInt720);
				}
			} else {
				int i7 = SceneGraph.anIntArray485[shapedTile.anInt720];
				Rasterizer.drawShadedTriangle(j6, l6, l5, i6, k6, k5, method317(i7, shapedTile.anInt718), method317(i7, shapedTile.anInt719), method317(i7, shapedTile.anInt717));
			}
		}
		if ((i5 - k5) * (l6 - l5) - (j5 - l5) * (k6 - k5) > 0) {
			Rasterizer.edgeRestricted = i5 < 0 || k5 < 0 || k6 < 0 || i5 > Graphics2D.endX || k5 > Graphics2D.endX || k6 > Graphics2D.endX;
			if (SceneGraph.aBoolean467 && method318(SceneGraph.anInt468, SceneGraph.anInt469, j5, l5, l6, i5, k5, k6)) {
				SceneGraph.anInt470 = arg5;
				SceneGraph.anInt471 = arg6;
			}
			if (shapedTile.anInt720 == -1) {
				if (shapedTile.anInt716 != 0xbc614e) {
					Rasterizer.drawShadedTriangle(j5, l5, l6, i5, k5, k6, shapedTile.anInt716, shapedTile.anInt717, shapedTile.anInt719);
				}
			} else {
				if (!SceneGraph.lowMem) {
					Rasterizer.drawTexturedTriangle(j5, l5, l6, i5, k5, k6, shapedTile.anInt716, shapedTile.anInt717, shapedTile.anInt719, i2, i3, l1, l3, i4, k4, k2, j2, j3, shapedTile.anInt720);
					return;
				}
				int j7 = SceneGraph.anIntArray485[shapedTile.anInt720];
				Rasterizer.drawShadedTriangle(j5, l5, l6, i5, k5, k6, method317(j7, shapedTile.anInt716), method317(j7, shapedTile.anInt717), method317(j7, shapedTile.anInt719));
			}
		}
	}

	private void method316(int arg0, int arg1, int arg2, Tile tile, int arg4, int arg5, int arg6) {
		int k1 = tile.anIntArray673.length;
		for (int l1 = 0; l1 < k1; l1++) {
			int i2 = tile.anIntArray673[l1] - SceneGraph.anInt455;
			int k2 = tile.anIntArray674[l1] - SceneGraph.anInt456;
			int i3 = tile.anIntArray675[l1] - SceneGraph.anInt457;
			int k3 = i3 * arg2 + i2 * arg6 >> 16;
			i3 = i3 * arg6 - i2 * arg2 >> 16;
			i2 = k3;
			k3 = k2 * arg4 - i3 * arg1 >> 16;
			i3 = k2 * arg1 + i3 * arg4 >> 16;
			k2 = k3;
			if (i3 < 50) {
				return;
			}
			if (tile.anIntArray682 != null) {
				Tile.anIntArray690[l1] = i2;
				Tile.anIntArray691[l1] = k2;
				Tile.anIntArray692[l1] = i3;
			}
			Tile.anIntArray688[l1] = Rasterizer.centerX + (i2 << 9) / i3;
			Tile.anIntArray689[l1] = Rasterizer.centerY + (k2 << 9) / i3;
		}
		Rasterizer.alpha = 0;
		k1 = tile.anIntArray679.length;
		for (int j2 = 0; j2 < k1; j2++) {
			int l2 = tile.anIntArray679[j2];
			int j3 = tile.anIntArray680[j2];
			int l3 = tile.anIntArray681[j2];
			int i4 = Tile.anIntArray688[l2];
			int j4 = Tile.anIntArray688[j3];
			int k4 = Tile.anIntArray688[l3];
			int l4 = Tile.anIntArray689[l2];
			int i5 = Tile.anIntArray689[j3];
			int j5 = Tile.anIntArray689[l3];
			if ((i4 - j4) * (j5 - i5) - (l4 - i5) * (k4 - j4) > 0) {
				Rasterizer.edgeRestricted = i4 < 0 || j4 < 0 || k4 < 0 || i4 > Graphics2D.endX || j4 > Graphics2D.endX || k4 > Graphics2D.endX;
				if (SceneGraph.aBoolean467 && method318(SceneGraph.anInt468, SceneGraph.anInt469, l4, i5, j5, i4, j4, k4)) {
					SceneGraph.anInt470 = arg0;
					SceneGraph.anInt471 = arg5;
				}
				if (tile.anIntArray682 == null || tile.anIntArray682[j2] == -1) {
					if (tile.anIntArray676[j2] != 0xbc614e) {
						Rasterizer.drawShadedTriangle(l4, i5, j5, i4, j4, k4, tile.anIntArray676[j2], tile.anIntArray677[j2], tile.anIntArray678[j2]);
					}
				} else if (!SceneGraph.lowMem) {
					if (tile.aBoolean683) {
						Rasterizer.drawTexturedTriangle(l4, i5, j5, i4, j4, k4, tile.anIntArray676[j2], tile.anIntArray677[j2], tile.anIntArray678[j2], Tile.anIntArray690[0], Tile.anIntArray690[1], Tile.anIntArray690[3], Tile.anIntArray691[0], Tile.anIntArray691[1], Tile.anIntArray691[3], Tile.anIntArray692[0], Tile.anIntArray692[1], Tile.anIntArray692[3], tile.anIntArray682[j2]);
					} else {
						Rasterizer.drawTexturedTriangle(l4, i5, j5, i4, j4, k4, tile.anIntArray676[j2], tile.anIntArray677[j2], tile.anIntArray678[j2], Tile.anIntArray690[l2], Tile.anIntArray690[j3], Tile.anIntArray690[l3], Tile.anIntArray691[l2], Tile.anIntArray691[j3], Tile.anIntArray691[l3], Tile.anIntArray692[l2], Tile.anIntArray692[j3], Tile.anIntArray692[l3], tile.anIntArray682[j2]);
					}
				} else {
					int k5 = SceneGraph.anIntArray485[tile.anIntArray682[j2]];
					Rasterizer.drawShadedTriangle(l4, i5, j5, i4, j4, k4, method317(k5, tile.anIntArray676[j2]), method317(k5, tile.anIntArray677[j2]), method317(k5, tile.anIntArray678[j2]));
				}
			}
		}
	}

	private int method317(int arg0, int arg1) {
		arg1 = 127 - arg1;
		arg1 = arg1 * (arg0 & 0x7f) / 160;
		if (arg1 < 2) {
			arg1 = 2;
		} else if (arg1 > 126) {
			arg1 = 126;
		}
		return (arg0 & 0xff80) + arg1;
	}

	private boolean method318(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7) {
		if (arg1 < arg2 && arg1 < arg3 && arg1 < arg4) {
			return false;
		}
		if (arg1 > arg2 && arg1 > arg3 && arg1 > arg4) {
			return false;
		}
		if (arg0 < arg5 && arg0 < arg6 && arg0 < arg7) {
			return false;
		}
		if (arg0 > arg5 && arg0 > arg6 && arg0 > arg7) {
			return false;
		}
		int i2 = (arg1 - arg2) * (arg6 - arg5) - (arg0 - arg5) * (arg3 - arg2);
		int j2 = (arg1 - arg4) * (arg5 - arg7) - (arg0 - arg7) * (arg2 - arg4);
		int k2 = (arg1 - arg3) * (arg7 - arg6) - (arg0 - arg6) * (arg4 - arg3);
		return i2 * k2 > 0 && k2 * j2 > 0;
	}

	private void method319() {
		int ptr = SceneGraph.cullingClusterPointer[SceneGraph.anInt447];
		CullingCluster aclass47[] = SceneGraph.cullingClusters[SceneGraph.anInt447];
		SceneGraph.anInt475 = 0;
		for (int k = 0; k < ptr; k++) {
			CullingCluster cullingCluster = aclass47[k];
			if (cullingCluster.searchMask == 1) {
				int l = cullingCluster.tileStartX - SceneGraph.anInt453 + 25;
				if (l < 0 || l > 50) {
					continue;
				}
				int k1 = cullingCluster.tileStartY - SceneGraph.anInt454 + 25;
				if (k1 < 0) {
					k1 = 0;
				}
				int j2 = cullingCluster.tileEndY - SceneGraph.anInt454 + 25;
				if (j2 > 50) {
					j2 = 50;
				}
				boolean flag = false;
				while (k1 <= j2) {
					if (SceneGraph.currentVisibilityMap[l][k1++]) {
						flag = true;
						break;
					}
				}
				if (!flag) {
					continue;
				}
				int j3 = SceneGraph.anInt455 - cullingCluster.worldStartX;
				if (j3 > 32) {
					cullingCluster.worldDistance = 1;
				} else {
					if (j3 >= -32) {
						continue;
					}
					cullingCluster.worldDistance = 2;
					j3 = -j3;
				}
				cullingCluster.startYToScreen = (cullingCluster.worldStartY - SceneGraph.anInt457 << 8) / j3;
				cullingCluster.endYToScreen = (cullingCluster.worldEndY - SceneGraph.anInt457 << 8) / j3;
				cullingCluster.startZToScreen = (cullingCluster.worldStartZ - SceneGraph.anInt456 << 8) / j3;
				cullingCluster.endZToScreen = (cullingCluster.worldEndZ - SceneGraph.anInt456 << 8) / j3;
				SceneGraph.aCullingClusterArray476[SceneGraph.anInt475++] = cullingCluster;
				continue;
			}
			if (cullingCluster.searchMask == 2) {
				int i1 = cullingCluster.tileStartY - SceneGraph.anInt454 + 25;
				if (i1 < 0 || i1 > 50) {
					continue;
				}
				int l1 = cullingCluster.tileStartX - SceneGraph.anInt453 + 25;
				if (l1 < 0) {
					l1 = 0;
				}
				int k2 = cullingCluster.tileEndX - SceneGraph.anInt453 + 25;
				if (k2 > 50) {
					k2 = 50;
				}
				boolean flag1 = false;
				while (l1 <= k2) {
					if (SceneGraph.currentVisibilityMap[l1++][i1]) {
						flag1 = true;
						break;
					}
				}
				if (!flag1) {
					continue;
				}
				int k3 = SceneGraph.anInt457 - cullingCluster.worldStartY;
				if (k3 > 32) {
					cullingCluster.worldDistance = 3;
				} else {
					if (k3 >= -32) {
						continue;
					}
					cullingCluster.worldDistance = 4;
					k3 = -k3;
				}
				cullingCluster.startXToScreen = (cullingCluster.worldStartX - SceneGraph.anInt455 << 8) / k3;
				cullingCluster.endXToScreen = (cullingCluster.worldEndX - SceneGraph.anInt455 << 8) / k3;
				cullingCluster.startZToScreen = (cullingCluster.worldStartZ - SceneGraph.anInt456 << 8) / k3;
				cullingCluster.endZToScreen = (cullingCluster.worldEndZ - SceneGraph.anInt456 << 8) / k3;
				SceneGraph.aCullingClusterArray476[SceneGraph.anInt475++] = cullingCluster;
			} else if (cullingCluster.searchMask == 4) {
				int j1 = cullingCluster.worldStartZ - SceneGraph.anInt456;
				if (j1 > 128) {
					int i2 = cullingCluster.tileStartY - SceneGraph.anInt454 + 25;
					if (i2 < 0) {
						i2 = 0;
					}
					int l2 = cullingCluster.tileEndY - SceneGraph.anInt454 + 25;
					if (l2 > 50) {
						l2 = 50;
					}
					if (i2 <= l2) {
						int i3 = cullingCluster.tileStartX - SceneGraph.anInt453 + 25;
						if (i3 < 0) {
							i3 = 0;
						}
						int l3 = cullingCluster.tileEndX - SceneGraph.anInt453 + 25;
						if (l3 > 50) {
							l3 = 50;
						}
						boolean flag2 = false;
						label0: for (int i4 = i3; i4 <= l3; i4++) {
							for (int j4 = i2; j4 <= l2; j4++) {
								if (!SceneGraph.currentVisibilityMap[i4][j4]) {
									continue;
								}
								flag2 = true;
								break label0;
							}
						}
						if (flag2) {
							cullingCluster.worldDistance = 5;
							cullingCluster.startXToScreen = (cullingCluster.worldStartX - SceneGraph.anInt455 << 8) / j1;
							cullingCluster.endXToScreen = (cullingCluster.worldEndX - SceneGraph.anInt455 << 8) / j1;
							cullingCluster.startYToScreen = (cullingCluster.worldStartY - SceneGraph.anInt457 << 8) / j1;
							cullingCluster.endYToScreen = (cullingCluster.worldEndY - SceneGraph.anInt457 << 8) / j1;
							SceneGraph.aCullingClusterArray476[SceneGraph.anInt475++] = cullingCluster;
						}
					}
				}
			}
		}
	}

	private boolean method320(int arg0, int arg1, int arg2) {
		int l = anIntArrayArrayArray445[arg0][arg1][arg2];
		if (l == -SceneGraph.anInt448) {
			return false;
		}
		if (l == SceneGraph.anInt448) {
			return true;
		}
		int i1 = arg1 << 7;
		int j1 = arg2 << 7;
		if (method324(i1 + 1, heightMap[arg0][arg1][arg2], j1 + 1) && method324(i1 + 128 - 1, heightMap[arg0][arg1 + 1][arg2], j1 + 1) && method324(i1 + 128 - 1, heightMap[arg0][arg1 + 1][arg2 + 1], j1 + 128 - 1) && method324(i1 + 1, heightMap[arg0][arg1][arg2 + 1], j1 + 128 - 1)) {
			anIntArrayArrayArray445[arg0][arg1][arg2] = SceneGraph.anInt448;
			return true;
		} else {
			anIntArrayArrayArray445[arg0][arg1][arg2] = -SceneGraph.anInt448;
			return false;
		}
	}

	private boolean method321(int arg0, int arg1, int arg2, int arg3) {
		if (!method320(arg0, arg1, arg2)) {
			return false;
		}
		int i1 = arg1 << 7;
		int j1 = arg2 << 7;
		int k1 = heightMap[arg0][arg1][arg2] - 1;
		int l1 = k1 - 120;
		int i2 = k1 - 230;
		int j2 = k1 - 238;
		if (arg3 < 16) {
			if (arg3 == 1) {
				if (i1 > SceneGraph.anInt455) {
					if (!method324(i1, k1, j1)) {
						return false;
					}
					if (!method324(i1, k1, j1 + 128)) {
						return false;
					}
				}
				if (arg0 > 0) {
					if (!method324(i1, l1, j1)) {
						return false;
					}
					if (!method324(i1, l1, j1 + 128)) {
						return false;
					}
				}
				return method324(i1, i2, j1) && method324(i1, i2, j1 + 128);
			}
			if (arg3 == 2) {
				if (j1 < SceneGraph.anInt457) {
					if (!method324(i1, k1, j1 + 128)) {
						return false;
					}
					if (!method324(i1 + 128, k1, j1 + 128)) {
						return false;
					}
				}
				if (arg0 > 0) {
					if (!method324(i1, l1, j1 + 128)) {
						return false;
					}
					if (!method324(i1 + 128, l1, j1 + 128)) {
						return false;
					}
				}
				return method324(i1, i2, j1 + 128) && method324(i1 + 128, i2, j1 + 128);
			}
			if (arg3 == 4) {
				if (i1 < SceneGraph.anInt455) {
					if (!method324(i1 + 128, k1, j1)) {
						return false;
					}
					if (!method324(i1 + 128, k1, j1 + 128)) {
						return false;
					}
				}
				if (arg0 > 0) {
					if (!method324(i1 + 128, l1, j1)) {
						return false;
					}
					if (!method324(i1 + 128, l1, j1 + 128)) {
						return false;
					}
				}
				return method324(i1 + 128, i2, j1) && method324(i1 + 128, i2, j1 + 128);
			}
			if (arg3 == 8) {
				if (j1 > SceneGraph.anInt457) {
					if (!method324(i1, k1, j1)) {
						return false;
					}
					if (!method324(i1 + 128, k1, j1)) {
						return false;
					}
				}
				if (arg0 > 0) {
					if (!method324(i1, l1, j1)) {
						return false;
					}
					if (!method324(i1 + 128, l1, j1)) {
						return false;
					}
				}
				return method324(i1, i2, j1) && method324(i1 + 128, i2, j1);
			}
		}
		if (!method324(i1 + 64, j2, j1 + 64)) {
			return false;
		}
		if (arg3 == 16) {
			return method324(i1, i2, j1 + 128);
		}
		if (arg3 == 32) {
			return method324(i1 + 128, i2, j1 + 128);
		}
		if (arg3 == 64) {
			return method324(i1 + 128, i2, j1);
		}
		if (arg3 == 128) {
			return method324(i1, i2, j1);
		} else {
			System.out.println("Warning unsupported wall type");
			return true;
		}
	}

	private boolean method322(int arg0, int arg1, int arg2, int arg3) {
		if (!method320(arg0, arg1, arg2)) {
			return false;
		}
		int i1 = arg1 << 7;
		int j1 = arg2 << 7;
		return method324(i1 + 1, heightMap[arg0][arg1][arg2] - arg3, j1 + 1) && method324(i1 + 128 - 1, heightMap[arg0][arg1 + 1][arg2] - arg3, j1 + 1) && method324(i1 + 128 - 1, heightMap[arg0][arg1 + 1][arg2 + 1] - arg3, j1 + 128 - 1) && method324(i1 + 1, heightMap[arg0][arg1][arg2 + 1] - arg3, j1 + 128 - 1);
	}

	private boolean method323(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5) {
		if (arg1 == arg2 && arg3 == arg4) {
			if (!method320(arg0, arg1, arg3)) {
				return false;
			}
			int k1 = arg1 << 7;
			int i2 = arg3 << 7;
			return method324(k1 + 1, heightMap[arg0][arg1][arg3] - arg5, i2 + 1) && method324(k1 + 128 - 1, heightMap[arg0][arg1 + 1][arg3] - arg5, i2 + 1) && method324(k1 + 128 - 1, heightMap[arg0][arg1 + 1][arg3 + 1] - arg5, i2 + 128 - 1) && method324(k1 + 1, heightMap[arg0][arg1][arg3 + 1] - arg5, i2 + 128 - 1);
		}
		for (int l1 = arg1; l1 <= arg2; l1++) {
			for (int j2 = arg3; j2 <= arg4; j2++) {
				if (anIntArrayArrayArray445[arg0][l1][j2] == -SceneGraph.anInt448) {
					return false;
				}
			}
		}
		int k2 = (arg1 << 7) + 1;
		int l2 = (arg3 << 7) + 2;
		int i3 = heightMap[arg0][arg1][arg3] - arg5;
		if (!method324(k2, i3, l2)) {
			return false;
		}
		int j3 = (arg2 << 7) - 1;
		if (!method324(j3, i3, l2)) {
			return false;
		}
		int k3 = (arg4 << 7) - 1;
		return method324(k2, i3, k3) && method324(j3, i3, k3);
	}

	private boolean method324(int arg0, int arg1, int arg2) {
		for (int l = 0; l < SceneGraph.anInt475; l++) {
			CullingCluster cullingCluster = SceneGraph.aCullingClusterArray476[l];
			if (cullingCluster.worldDistance == 1) {
				int i1 = cullingCluster.worldStartX - arg0;
				if (i1 > 0) {
					int j2 = cullingCluster.worldStartY + (cullingCluster.startYToScreen * i1 >> 8);
					int k3 = cullingCluster.worldEndY + (cullingCluster.endYToScreen * i1 >> 8);
					int l4 = cullingCluster.worldStartZ + (cullingCluster.startZToScreen * i1 >> 8);
					int i6 = cullingCluster.worldEndZ + (cullingCluster.endZToScreen * i1 >> 8);
					if (arg2 >= j2 && arg2 <= k3 && arg1 >= l4 && arg1 <= i6) {
						return true;
					}
				}
			} else if (cullingCluster.worldDistance == 2) {
				int j1 = arg0 - cullingCluster.worldStartX;
				if (j1 > 0) {
					int k2 = cullingCluster.worldStartY + (cullingCluster.startYToScreen * j1 >> 8);
					int l3 = cullingCluster.worldEndY + (cullingCluster.endYToScreen * j1 >> 8);
					int i5 = cullingCluster.worldStartZ + (cullingCluster.startZToScreen * j1 >> 8);
					int j6 = cullingCluster.worldEndZ + (cullingCluster.endZToScreen * j1 >> 8);
					if (arg2 >= k2 && arg2 <= l3 && arg1 >= i5 && arg1 <= j6) {
						return true;
					}
				}
			} else if (cullingCluster.worldDistance == 3) {
				int k1 = cullingCluster.worldStartY - arg2;
				if (k1 > 0) {
					int l2 = cullingCluster.worldStartX + (cullingCluster.startXToScreen * k1 >> 8);
					int i4 = cullingCluster.worldEndX + (cullingCluster.endXToScreen * k1 >> 8);
					int j5 = cullingCluster.worldStartZ + (cullingCluster.startZToScreen * k1 >> 8);
					int k6 = cullingCluster.worldEndZ + (cullingCluster.endZToScreen * k1 >> 8);
					if (arg0 >= l2 && arg0 <= i4 && arg1 >= j5 && arg1 <= k6) {
						return true;
					}
				}
			} else if (cullingCluster.worldDistance == 4) {
				int l1 = arg2 - cullingCluster.worldStartY;
				if (l1 > 0) {
					int i3 = cullingCluster.worldStartX + (cullingCluster.startXToScreen * l1 >> 8);
					int j4 = cullingCluster.worldEndX + (cullingCluster.endXToScreen * l1 >> 8);
					int k5 = cullingCluster.worldStartZ + (cullingCluster.startZToScreen * l1 >> 8);
					int l6 = cullingCluster.worldEndZ + (cullingCluster.endZToScreen * l1 >> 8);
					if (arg0 >= i3 && arg0 <= j4 && arg1 >= k5 && arg1 <= l6) {
						return true;
					}
				}
			} else if (cullingCluster.worldDistance == 5) {
				int i2 = arg1 - cullingCluster.worldStartZ;
				if (i2 > 0) {
					int j3 = cullingCluster.worldStartX + (cullingCluster.startXToScreen * i2 >> 8);
					int k4 = cullingCluster.worldEndX + (cullingCluster.endXToScreen * i2 >> 8);
					int l5 = cullingCluster.worldStartY + (cullingCluster.startYToScreen * i2 >> 8);
					int i7 = cullingCluster.worldEndY + (cullingCluster.endYToScreen * i2 >> 8);
					if (arg0 >= j3 && arg0 <= k4 && arg2 >= l5 && arg2 <= i7) {
						return true;
					}
				}
			}
		}
		return false;
	}
	public static boolean lowMem = true;
	private final int mapSizeZ;
	private final int mapSizeX;
	private final int mapSizeY;
	private final int[][][] heightMap;
	private final GroundTile[][][] groundTiles;
	private int anInt442;
	private int interactiveObjectCachePos;
	private final InteractiveObject[] interactiveObjectCache;
	private final int[][][] anIntArrayArrayArray445;
	private static int anInt446;
	private static int anInt447;
	private static int anInt448;
	private static int anInt449;
	private static int anInt450;
	private static int anInt451;
	private static int anInt452;
	private static int anInt453;
	private static int anInt454;
	private static int anInt455;
	private static int anInt456;
	private static int anInt457;
	private static int anInt458;
	private static int anInt459;
	private static int anInt460;
	private static int anInt461;
	private static InteractiveObject[] interactiveObjects = new InteractiveObject[100];
	private static final int[] anIntArray463 = { 53, -53, -53, 53 };
	private static final int[] anIntArray464 = { -53, -53, 53, 53 };
	private static final int[] anIntArray465 = { -45, 45, 45, -45 };
	private static final int[] anIntArray466 = { 45, 45, -45, -45 };
	private static boolean aBoolean467;
	private static int anInt468;
	private static int anInt469;
	public static int anInt470 = -1;
	public static int anInt471 = -1;
	private static final int anInt472;
	private static int[] cullingClusterPointer;
	private static CullingCluster[][] cullingClusters;
	private static int anInt475;
	private static final CullingCluster[] aCullingClusterArray476 = new CullingCluster[500];
	private static Deque deque = new Deque();
	private static final int[] anIntArray478 = { 19, 55, 38, 155, 255, 110, 137, 205, 76 };
	private static final int[] anIntArray479 = { 160, 192, 80, 96, 0, 144, 80, 48, 160 };
	private static final int[] anIntArray480 = { 76, 8, 137, 4, 0, 1, 38, 2, 19 };
	private static final int[] anIntArray481 = { 0, 0, 2, 0, 0, 2, 1, 1, 0 };
	private static final int[] anIntArray482 = { 2, 0, 0, 2, 0, 0, 0, 4, 4 };
	private static final int[] anIntArray483 = { 0, 4, 4, 8, 0, 0, 8, 0, 0 };
	private static final int[] anIntArray484 = { 1, 1, 0, 0, 0, 8, 0, 0, 8 };
	private static final int[] anIntArray485 = { 41, 39248, 41, 4643, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 43086, 41, 41, 41, 41, 41, 41, 41, 8602, 41, 28992, 41, 41, 41, 41, 41, 5056, 41, 41, 41, 7079, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 3131, 41, 41, 41 };
	private final int[] anIntArray486;
	private final int[] anIntArray487;
	private int anInt488;
	private final int[][] anIntArrayArray489 = { new int[16], { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 }, { 1, 0, 0, 0, 1, 1, 0, 0, 1, 1, 1, 0, 1, 1, 1, 1 }, { 1, 1, 0, 0, 1, 1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0 }, { 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 1 }, { 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 }, { 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1 }, { 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 1, 0, 0 }, { 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 0, 1, 1 }, { 1, 1, 1, 1, 1, 1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 1, 1, 1, 1 } };
	private final int[][] anIntArrayArray490 = { { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15 }, { 12, 8, 4, 0, 13, 9, 5, 1, 14, 10, 6, 2, 15, 11, 7, 3 }, { 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0 }, { 3, 7, 11, 15, 2, 6, 10, 14, 1, 5, 9, 13, 0, 4, 8, 12 } };
	private static boolean[][][][] tileVisibilityMaps = new boolean[8][32][51][51];
	private static boolean[][] currentVisibilityMap;
	private static int anInt493;
	private static int anInt494;
	private static int anInt495;
	private static int anInt496;
	private static int anInt497;
	private static int anInt498;
	static {
		anInt472 = 4;
		SceneGraph.cullingClusterPointer = new int[SceneGraph.anInt472];
		SceneGraph.cullingClusters = new CullingCluster[SceneGraph.anInt472][500];
	}
}
