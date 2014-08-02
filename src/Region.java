/**
 * Region [ObjectManager]
 * Handles the operations for a single region on the scene
 */
final class Region {

	public Region(byte arg0[][][], int arg1[][][]) {
		Region.anInt145 = 99;
		anInt146 = 104;
		anInt147 = 104;
		anIntArrayArrayArray129 = arg1;
		aByteArrayArrayArray149 = arg0;
		aByteArrayArrayArray142 = new byte[4][anInt146][anInt147];
		aByteArrayArrayArray130 = new byte[4][anInt146][anInt147];
		aByteArrayArrayArray136 = new byte[4][anInt146][anInt147];
		aByteArrayArrayArray148 = new byte[4][anInt146][anInt147];
		anIntArrayArrayArray135 = new int[4][anInt146 + 1][anInt147 + 1];
		aByteArrayArrayArray134 = new byte[4][anInt146 + 1][anInt147 + 1];
		anIntArrayArray139 = new int[anInt146 + 1][anInt147 + 1];
		anIntArray124 = new int[anInt147];
		anIntArray125 = new int[anInt147];
		anIntArray126 = new int[anInt147];
		anIntArray127 = new int[anInt147];
		anIntArray128 = new int[anInt147];
	}

	private static int method170(int arg0, int arg1) {
		int k = arg0 + arg1 * 57;
		k = k << 13 ^ k;
		int l = k * (k * k * 15731 + 789221) + 1376312589 & 0x7fffffff;
		return l >> 19 & 0xff;
	}

	public final void method171(CollisionMap collisionFlags[], SceneGraph sceneGraph) {
		for (int j = 0; j < 4; j++) {
			for (int k = 0; k < 104; k++) {
				for (int i1 = 0; i1 < 104; i1++) {
					if ((aByteArrayArrayArray149[j][k][i1] & 1) == 1) {
						int k1 = j;
						if ((aByteArrayArrayArray149[1][k][i1] & 2) == 2) {
							k1--;
						}
						if (k1 >= 0) {
							collisionFlags[k1].method213(i1, k);
						}
					}
				}
			}
		}
		Region.anInt123 += (int) (Math.random() * 5D) - 2;
		if (Region.anInt123 < -8) {
			Region.anInt123 = -8;
		}
		if (Region.anInt123 > 8) {
			Region.anInt123 = 8;
		}
		Region.anInt133 += (int) (Math.random() * 5D) - 2;
		if (Region.anInt133 < -16) {
			Region.anInt133 = -16;
		}
		if (Region.anInt133 > 16) {
			Region.anInt133 = 16;
		}
		for (int l = 0; l < 4; l++) {
			byte abyte0[][] = aByteArrayArrayArray134[l];
			byte byte0 = 96;
			char c = '\u0300';
			byte byte1 = -50;
			byte byte2 = -10;
			byte byte3 = -50;
			int j3 = (int) Math.sqrt(byte1 * byte1 + byte2 * byte2 + byte3 * byte3);
			int l3 = c * j3 >> 8;
			for (int j4 = 1; j4 < anInt147 - 1; j4++) {
				for (int j5 = 1; j5 < anInt146 - 1; j5++) {
					int k6 = anIntArrayArrayArray129[l][j5 + 1][j4] - anIntArrayArrayArray129[l][j5 - 1][j4];
					int l7 = anIntArrayArrayArray129[l][j5][j4 + 1] - anIntArrayArrayArray129[l][j5][j4 - 1];
					int j9 = (int) Math.sqrt(k6 * k6 + 0x10000 + l7 * l7);
					int k12 = (k6 << 8) / j9;
					int l13 = 0x10000 / j9;
					int j15 = (l7 << 8) / j9;
					int j16 = byte0 + (byte1 * k12 + byte2 * l13 + byte3 * j15) / l3;
					int j17 = (abyte0[j5 - 1][j4] >> 2) + (abyte0[j5 + 1][j4] >> 3) + (abyte0[j5][j4 - 1] >> 2) + (abyte0[j5][j4 + 1] >> 3) + (abyte0[j5][j4] >> 1);
					anIntArrayArray139[j5][j4] = j16 - j17;
				}
			}
			for (int k5 = 0; k5 < anInt147; k5++) {
				anIntArray124[k5] = 0;
				anIntArray125[k5] = 0;
				anIntArray126[k5] = 0;
				anIntArray127[k5] = 0;
				anIntArray128[k5] = 0;
			}
			for (int l6 = -5; l6 < anInt146 + 5; l6++) {
				for (int i8 = 0; i8 < anInt147; i8++) {
					int k9 = l6 + 5;
					if (k9 >= 0 && k9 < anInt146) {
						int l12 = aByteArrayArrayArray142[l][k9][i8] & 0xff;
						if (l12 > 0) {
							Floor floor = Floor.cachedFloors[l12 - 1];
							anIntArray124[i8] += floor.anInt397;
							anIntArray125[i8] += floor.anInt395;
							anIntArray126[i8] += floor.anInt396;
							anIntArray127[i8] += floor.anInt398;
							anIntArray128[i8]++;
						}
					}
					int i13 = l6 - 5;
					if (i13 >= 0 && i13 < anInt146) {
						int i14 = aByteArrayArrayArray142[l][i13][i8] & 0xff;
						if (i14 > 0) {
							Floor floor_1 = Floor.cachedFloors[i14 - 1];
							anIntArray124[i8] -= floor_1.anInt397;
							anIntArray125[i8] -= floor_1.anInt395;
							anIntArray126[i8] -= floor_1.anInt396;
							anIntArray127[i8] -= floor_1.anInt398;
							anIntArray128[i8]--;
						}
					}
				}
				if (l6 >= 1 && l6 < anInt146 - 1) {
					int l9 = 0;
					int j13 = 0;
					int j14 = 0;
					int k15 = 0;
					int k16 = 0;
					for (int k17 = -5; k17 < anInt147 + 5; k17++) {
						int j18 = k17 + 5;
						if (j18 >= 0 && j18 < anInt147) {
							l9 += anIntArray124[j18];
							j13 += anIntArray125[j18];
							j14 += anIntArray126[j18];
							k15 += anIntArray127[j18];
							k16 += anIntArray128[j18];
						}
						int k18 = k17 - 5;
						if (k18 >= 0 && k18 < anInt147) {
							l9 -= anIntArray124[k18];
							j13 -= anIntArray125[k18];
							j14 -= anIntArray126[k18];
							k15 -= anIntArray127[k18];
							k16 -= anIntArray128[k18];
						}
						if (k17 >= 1 && k17 < anInt147 - 1 && (!Region.lowMem || (aByteArrayArrayArray149[0][l6][k17] & 2) != 0 || (aByteArrayArrayArray149[l][l6][k17] & 0x10) == 0 && method182(k17, l, l6) == Region.anInt131)) {
							if (l < Region.anInt145) {
								Region.anInt145 = l;
							}
							int l18 = aByteArrayArrayArray142[l][l6][k17] & 0xff;
							int floorId = aByteArrayArrayArray130[l][l6][k17] & 0xff;
							if (l18 > 0 || floorId > 0) {
								int j19 = anIntArrayArrayArray129[l][l6][k17];
								int k19 = anIntArrayArrayArray129[l][l6 + 1][k17];
								int l19 = anIntArrayArrayArray129[l][l6 + 1][k17 + 1];
								int i20 = anIntArrayArrayArray129[l][l6][k17 + 1];
								int j20 = anIntArrayArray139[l6][k17];
								int k20 = anIntArrayArray139[l6 + 1][k17];
								int l20 = anIntArrayArray139[l6 + 1][k17 + 1];
								int i21 = anIntArrayArray139[l6][k17 + 1];
								int j21 = -1;
								int k21 = -1;
								if (l18 > 0) {
									int l21 = l9 * 256 / k15;
									int j22 = j13 / k16;
									int l22 = j14 / k16;
									j21 = method177(l21, j22, l22);
									l21 = l21 + Region.anInt123 & 0xff;
									l22 += Region.anInt133;
									if (l22 < 0) {
										l22 = 0;
									} else if (l22 > 255) {
										l22 = 255;
									}
									k21 = method177(l21, j22, l22);
								}
								if (l > 0) {
									boolean flag = true;
									if (l18 == 0 && aByteArrayArrayArray136[l][l6][k17] != 0) {
										flag = false;
									}
									if (floorId > 0 && !Floor.cachedFloors[floorId - 1].occlude) {
										flag = false;
									}
									if (flag && j19 == k19 && j19 == l19 && j19 == i20) {
										anIntArrayArrayArray135[l][l6][k17] |= 0x924;
									}
								}
								int i22 = 0;
								if (j21 != -1) {
									i22 = Rasterizer.palette[Region.method187(k21, 96)];
								}
								if (floorId == 0) {
									sceneGraph.method279(l, l6, k17, 0, 0, -1, j19, k19, l19, i20, Region.method187(j21, j20), Region.method187(j21, k20), Region.method187(j21, l20), Region.method187(j21, i21), 0, 0, 0, 0, i22, 0);
								} else {
									int k22 = aByteArrayArrayArray136[l][l6][k17] + 1;
									byte byte4 = aByteArrayArrayArray148[l][l6][k17];
									Floor floor = Floor.cachedFloors[floorId - 1];
									int textureId = floor.anInt391;
									int hsl;
									int minimapColor;
									if (textureId >= 0) {
										minimapColor = Rasterizer.getMinimapTextureColor(textureId);
										hsl = -1;
									} else if (floor.anInt390 == 0xff00ff) {
										// minimapColor = 0;
										minimapColor = Rasterizer.palette[randomizeMinimap(floor.anInt399, 96)];
										hsl = -2;
										textureId = -1;
									} else {
										hsl = method177(floor.anInt394, floor.anInt395, floor.anInt396);
										minimapColor = Rasterizer.palette[randomizeMinimap(floor.anInt399, 96)];
									}
									sceneGraph.method279(l, l6, k17, k22, byte4, textureId, j19, k19, l19, i20, Region.method187(j21, j20), Region.method187(j21, k20), Region.method187(j21, l20), Region.method187(j21, i21), randomizeMinimap(hsl, j20), randomizeMinimap(hsl, k20), randomizeMinimap(hsl, l20), randomizeMinimap(hsl, i21), i22, minimapColor);
								}
							}
						}
					}
				}
			}
			for (int j8 = 1; j8 < anInt147 - 1; j8++) {
				for (int i10 = 1; i10 < anInt146 - 1; i10++) {
					sceneGraph.method278(l, i10, j8, method182(j8, l, i10));
				}
			}
		}
		sceneGraph.method305(-10, -50, -50);
		for (int j1 = 0; j1 < anInt146; j1++) {
			for (int l1 = 0; l1 < anInt147; l1++) {
				if ((aByteArrayArrayArray149[1][j1][l1] & 2) == 2) {
					sceneGraph.method276(l1, j1);
				}
			}
		}
		int i2 = 1;
		int j2 = 2;
		int k2 = 4;
		for (int l2 = 0; l2 < 4; l2++) {
			if (l2 > 0) {
				i2 <<= 3;
				j2 <<= 3;
				k2 <<= 3;
			}
			for (int i3 = 0; i3 <= l2; i3++) {
				for (int k3 = 0; k3 <= anInt147; k3++) {
					for (int i4 = 0; i4 <= anInt146; i4++) {
						if ((anIntArrayArrayArray135[i3][i4][k3] & i2) != 0) {
							int k4 = k3;
							int l5 = k3;
							int i7 = i3;
							int k8 = i3;
							for (; k4 > 0 && (anIntArrayArrayArray135[i3][i4][k4 - 1] & i2) != 0; k4--) {
								;
							}
							for (; l5 < anInt147 && (anIntArrayArrayArray135[i3][i4][l5 + 1] & i2) != 0; l5++) {
								;
							}
							label0: for (; i7 > 0; i7--) {
								for (int j10 = k4; j10 <= l5; j10++) {
									if ((anIntArrayArrayArray135[i7 - 1][i4][j10] & i2) == 0) {
										break label0;
									}
								}
							}
							label1: for (; k8 < l2; k8++) {
								for (int k10 = k4; k10 <= l5; k10++) {
									if ((anIntArrayArrayArray135[k8 + 1][i4][k10] & i2) == 0) {
										break label1;
									}
								}
							}
							int l10 = (k8 + 1 - i7) * (l5 - k4 + 1);
							if (l10 >= 8) {
								char c1 = '\360';
								int k14 = anIntArrayArrayArray129[k8][i4][k4] - c1;
								int l15 = anIntArrayArrayArray129[i7][i4][k4];
								SceneGraph.method277(l2, i4 * 128, l15, i4 * 128, l5 * 128 + 128, k14, k4 * 128, 1);
								for (int l16 = i7; l16 <= k8; l16++) {
									for (int l17 = k4; l17 <= l5; l17++) {
										anIntArrayArrayArray135[l16][i4][l17] &= ~i2;
									}
								}
							}
						}
						if ((anIntArrayArrayArray135[i3][i4][k3] & j2) != 0) {
							int l4 = i4;
							int i6 = i4;
							int j7 = i3;
							int l8 = i3;
							for (; l4 > 0 && (anIntArrayArrayArray135[i3][l4 - 1][k3] & j2) != 0; l4--) {
								;
							}
							for (; i6 < anInt146 && (anIntArrayArrayArray135[i3][i6 + 1][k3] & j2) != 0; i6++) {
								;
							}
							label2: for (; j7 > 0; j7--) {
								for (int i11 = l4; i11 <= i6; i11++) {
									if ((anIntArrayArrayArray135[j7 - 1][i11][k3] & j2) == 0) {
										break label2;
									}
								}
							}
							label3: for (; l8 < l2; l8++) {
								for (int j11 = l4; j11 <= i6; j11++) {
									if ((anIntArrayArrayArray135[l8 + 1][j11][k3] & j2) == 0) {
										break label3;
									}
								}
							}
							int k11 = (l8 + 1 - j7) * (i6 - l4 + 1);
							if (k11 >= 8) {
								char c2 = '\360';
								int l14 = anIntArrayArrayArray129[l8][l4][k3] - c2;
								int i16 = anIntArrayArrayArray129[j7][l4][k3];
								SceneGraph.method277(l2, l4 * 128, i16, i6 * 128 + 128, k3 * 128, l14, k3 * 128, 2);
								for (int i17 = j7; i17 <= l8; i17++) {
									for (int i18 = l4; i18 <= i6; i18++) {
										anIntArrayArrayArray135[i17][i18][k3] &= ~j2;
									}
								}
							}
						}
						if ((anIntArrayArrayArray135[i3][i4][k3] & k2) != 0) {
							int i5 = i4;
							int j6 = i4;
							int k7 = k3;
							int i9 = k3;
							for (; k7 > 0 && (anIntArrayArrayArray135[i3][i4][k7 - 1] & k2) != 0; k7--) {
								;
							}
							for (; i9 < anInt147 && (anIntArrayArrayArray135[i3][i4][i9 + 1] & k2) != 0; i9++) {
								;
							}
							label4: for (; i5 > 0; i5--) {
								for (int l11 = k7; l11 <= i9; l11++) {
									if ((anIntArrayArrayArray135[i3][i5 - 1][l11] & k2) == 0) {
										break label4;
									}
								}
							}
							label5: for (; j6 < anInt146; j6++) {
								for (int i12 = k7; i12 <= i9; i12++) {
									if ((anIntArrayArrayArray135[i3][j6 + 1][i12] & k2) == 0) {
										break label5;
									}
								}
							}
							if ((j6 - i5 + 1) * (i9 - k7 + 1) >= 4) {
								int j12 = anIntArrayArrayArray129[i3][i5][k7];
								SceneGraph.method277(l2, i5 * 128, j12, j6 * 128 + 128, i9 * 128 + 128, j12, k7 * 128, 4);
								for (int k13 = i5; k13 <= j6; k13++) {
									for (int i15 = k7; i15 <= i9; i15++) {
										anIntArrayArrayArray135[i3][k13][i15] &= ~k2;
									}
								}
							}
						}
					}
				}
			}
		}
	}

	private static int method172(int arg0, int arg1) {
		int k = Region.method176(arg0 + 45365, arg1 + 0x16713, 4) - 128 + (Region.method176(arg0 + 10294, arg1 + 37821, 2) - 128 >> 1) + (Region.method176(arg0, arg1, 1) - 128 >> 2);
		k = (int) (k * 0.29999999999999999D) + 35;
		if (k < 10) {
			k = 10;
		} else if (k > 60) {
			k = 60;
		}
		return k;
	}

	public static void method173(Stream stream, OnDemandFetcher onDemandFetcher) {
		label0: {
			int i = -1;
			do {
				int j = stream.getSmart();
				if (j == 0) {
					break label0;
				}
				i += j;
				ObjectDefinition objectDefinition = ObjectDefinition.forId(i);
				objectDefinition.method574(onDemandFetcher);
				do {
					int k = stream.getSmart();
					if (k == 0) {
						break;
					}
					stream.getUnsignedByte();
				} while (true);
			} while (true);
		}
	}

	public final void method174(int arg0, int arg1, int arg2, int arg3) {
		for (int j1 = arg0; j1 <= arg0 + arg1; j1++) {
			for (int k1 = arg3; k1 <= arg3 + arg2; k1++) {
				if (k1 >= 0 && k1 < anInt146 && j1 >= 0 && j1 < anInt147) {
					aByteArrayArrayArray134[0][k1][j1] = 127;
					if (k1 == arg3 && k1 > 0) {
						anIntArrayArrayArray129[0][k1][j1] = anIntArrayArrayArray129[0][k1 - 1][j1];
					}
					if (k1 == arg3 + arg2 && k1 < anInt146 - 1) {
						anIntArrayArrayArray129[0][k1][j1] = anIntArrayArrayArray129[0][k1 + 1][j1];
					}
					if (j1 == arg0 && j1 > 0) {
						anIntArrayArrayArray129[0][k1][j1] = anIntArrayArrayArray129[0][k1][j1 - 1];
					}
					if (j1 == arg0 + arg1 && j1 < anInt147 - 1) {
						anIntArrayArrayArray129[0][k1][j1] = anIntArrayArrayArray129[0][k1][j1 + 1];
					}
				}
			}
		}
	}

	private void method175(int arg0, SceneGraph sceneGraph, CollisionMap collisionMap, int arg3, int arg4, int arg5, int arg6, int arg7) {
		if (Region.lowMem && (aByteArrayArrayArray149[0][arg5][arg0] & 2) == 0) {
			if ((aByteArrayArrayArray149[arg4][arg5][arg0] & 0x10) != 0) {
				return;
			}
			if (method182(arg0, arg4, arg5) != Region.anInt131) {
				return;
			}
		}
		if (arg4 < Region.anInt145) {
			Region.anInt145 = arg4;
		}
		int k1 = anIntArrayArrayArray129[arg4][arg5][arg0];
		int l1 = anIntArrayArrayArray129[arg4][arg5 + 1][arg0];
		int i2 = anIntArrayArrayArray129[arg4][arg5 + 1][arg0 + 1];
		int j2 = anIntArrayArrayArray129[arg4][arg5][arg0 + 1];
		int k2 = k1 + l1 + i2 + j2 >> 2;
		ObjectDefinition object = ObjectDefinition.forId(arg6);
		int l2 = arg5 + (arg0 << 7) + (arg6 << 14) + 0x40000000;
		if (!object.hasActions) {
			l2 += 0x80000000;
		}
		byte byte0 = (byte) ((arg7 << 6) + arg3);
		if (arg3 == 22) {
			if (Region.lowMem && !object.hasActions && !object.aBoolean736) {
				return;
			}
			Object obj;
			if (object.animationId == -1 && object.childIds == null) {
				obj = object.method578(22, arg7, k1, l1, i2, j2, -1);
			} else {
				obj = new AnimatedObject(arg6, arg7, 22, l1, i2, k1, j2, object.animationId, true);
			}
			sceneGraph.method280(arg4, k2, arg0, ((Entity) obj), byte0, l2, arg5);
			if (object.unwalkable && object.hasActions && collisionMap != null) {
				collisionMap.method213(arg0, arg5);
			}
			return;
		}
		if (arg3 == 10 || arg3 == 11) {
			Object obj1;
			if (object.animationId == -1 && object.childIds == null) {
				obj1 = object.method578(10, arg7, k1, l1, i2, j2, -1);
			} else {
				obj1 = new AnimatedObject(arg6, arg7, 10, l1, i2, k1, j2, object.animationId, true);
			}
			if (obj1 != null) {
				int i5 = 0;
				if (arg3 == 11) {
					i5 += 256;
				}
				int j4;
				int l4;
				if (arg7 == 1 || arg7 == 3) {
					j4 = object.xSize;
					l4 = object.ySize;
				} else {
					j4 = object.ySize;
					l4 = object.xSize;
				}
				if (sceneGraph.method284(l2, byte0, k2, l4, ((Entity) obj1), j4, arg4, i5, arg0, arg5) && object.aBoolean779) {
					Model model;
					if (obj1 instanceof Model) {
						model = (Model) obj1;
					} else {
						model = object.method578(10, arg7, k1, l1, i2, j2, -1);
					}
					if (model != null) {
						for (int j5 = 0; j5 <= j4; j5++) {
							for (int k5 = 0; k5 <= l4; k5++) {
								int l5 = model.anInt1650 / 4;
								if (l5 > 30) {
									l5 = 30;
								}
								if (l5 > aByteArrayArrayArray134[arg4][arg5 + j5][arg0 + k5]) {
									aByteArrayArrayArray134[arg4][arg5 + j5][arg0 + k5] = (byte) l5;
								}
							}
						}
					}
				}
			}
			if (object.unwalkable && collisionMap != null) {
				collisionMap.method212(object.solid, object.ySize, object.xSize, arg5, arg0, arg7);
			}
			return;
		}
		if (arg3 >= 12) {
			Object obj2;
			if (object.animationId == -1 && object.childIds == null) {
				obj2 = object.method578(arg3, arg7, k1, l1, i2, j2, -1);
			} else {
				obj2 = new AnimatedObject(arg6, arg7, arg3, l1, i2, k1, j2, object.animationId, true);
			}
			sceneGraph.method284(l2, byte0, k2, 1, ((Entity) obj2), 1, arg4, 0, arg0, arg5);
			if (arg3 >= 12 && arg3 <= 17 && arg3 != 13 && arg4 > 0) {
				anIntArrayArrayArray135[arg4][arg5][arg0] |= 0x924;
			}
			if (object.unwalkable && collisionMap != null) {
				collisionMap.method212(object.solid, object.ySize, object.xSize, arg5, arg0, arg7);
			}
			return;
		}
		if (arg3 == 0) {
			Object obj3;
			if (object.animationId == -1 && object.childIds == null) {
				obj3 = object.method578(0, arg7, k1, l1, i2, j2, -1);
			} else {
				obj3 = new AnimatedObject(arg6, arg7, 0, l1, i2, k1, j2, object.animationId, true);
			}
			sceneGraph.method282(Region.anIntArray152[arg7], ((Entity) obj3), l2, arg0, byte0, arg5, null, k2, 0, arg4);
			if (arg7 == 0) {
				if (object.aBoolean779) {
					aByteArrayArrayArray134[arg4][arg5][arg0] = 50;
					aByteArrayArrayArray134[arg4][arg5][arg0 + 1] = 50;
				}
				if (object.aBoolean764) {
					anIntArrayArrayArray135[arg4][arg5][arg0] |= 0x249;
				}
			} else if (arg7 == 1) {
				if (object.aBoolean779) {
					aByteArrayArrayArray134[arg4][arg5][arg0 + 1] = 50;
					aByteArrayArrayArray134[arg4][arg5 + 1][arg0 + 1] = 50;
				}
				if (object.aBoolean764) {
					anIntArrayArrayArray135[arg4][arg5][arg0 + 1] |= 0x492;
				}
			} else if (arg7 == 2) {
				if (object.aBoolean779) {
					aByteArrayArrayArray134[arg4][arg5 + 1][arg0] = 50;
					aByteArrayArrayArray134[arg4][arg5 + 1][arg0 + 1] = 50;
				}
				if (object.aBoolean764) {
					anIntArrayArrayArray135[arg4][arg5 + 1][arg0] |= 0x249;
				}
			} else if (arg7 == 3) {
				if (object.aBoolean779) {
					aByteArrayArrayArray134[arg4][arg5][arg0] = 50;
					aByteArrayArrayArray134[arg4][arg5 + 1][arg0] = 50;
				}
				if (object.aBoolean764) {
					anIntArrayArrayArray135[arg4][arg5][arg0] |= 0x492;
				}
			}
			if (object.unwalkable && collisionMap != null) {
				collisionMap.method211(arg0, arg7, arg5, arg3, object.solid);
			}
			if (object.anInt775 != 16) {
				sceneGraph.method290(arg0, object.anInt775, arg5, arg4);
			}
			return;
		}
		if (arg3 == 1) {
			Object obj4;
			if (object.animationId == -1 && object.childIds == null) {
				obj4 = object.method578(1, arg7, k1, l1, i2, j2, -1);
			} else {
				obj4 = new AnimatedObject(arg6, arg7, 1, l1, i2, k1, j2, object.animationId, true);
			}
			sceneGraph.method282(Region.anIntArray140[arg7], ((Entity) obj4), l2, arg0, byte0, arg5, null, k2, 0, arg4);
			if (object.aBoolean779) {
				if (arg7 == 0) {
					aByteArrayArrayArray134[arg4][arg5][arg0 + 1] = 50;
				} else if (arg7 == 1) {
					aByteArrayArrayArray134[arg4][arg5 + 1][arg0 + 1] = 50;
				} else if (arg7 == 2) {
					aByteArrayArrayArray134[arg4][arg5 + 1][arg0] = 50;
				} else if (arg7 == 3) {
					aByteArrayArrayArray134[arg4][arg5][arg0] = 50;
				}
			}
			if (object.unwalkable && collisionMap != null) {
				collisionMap.method211(arg0, arg7, arg5, arg3, object.solid);
			}
			return;
		}
		if (arg3 == 2) {
			int i3 = arg7 + 1 & 3;
			Object obj11;
			Object obj12;
			if (object.animationId == -1 && object.childIds == null) {
				obj11 = object.method578(2, 4 + arg7, k1, l1, i2, j2, -1);
				obj12 = object.method578(2, i3, k1, l1, i2, j2, -1);
			} else {
				obj11 = new AnimatedObject(arg6, 4 + arg7, 2, l1, i2, k1, j2, object.animationId, true);
				obj12 = new AnimatedObject(arg6, i3, 2, l1, i2, k1, j2, object.animationId, true);
			}
			sceneGraph.method282(Region.anIntArray152[arg7], ((Entity) obj11), l2, arg0, byte0, arg5, ((Entity) obj12), k2, Region.anIntArray152[i3], arg4);
			if (object.aBoolean764) {
				if (arg7 == 0) {
					anIntArrayArrayArray135[arg4][arg5][arg0] |= 0x249;
					anIntArrayArrayArray135[arg4][arg5][arg0 + 1] |= 0x492;
				} else if (arg7 == 1) {
					anIntArrayArrayArray135[arg4][arg5][arg0 + 1] |= 0x492;
					anIntArrayArrayArray135[arg4][arg5 + 1][arg0] |= 0x249;
				} else if (arg7 == 2) {
					anIntArrayArrayArray135[arg4][arg5 + 1][arg0] |= 0x249;
					anIntArrayArrayArray135[arg4][arg5][arg0] |= 0x492;
				} else if (arg7 == 3) {
					anIntArrayArrayArray135[arg4][arg5][arg0] |= 0x492;
					anIntArrayArrayArray135[arg4][arg5][arg0] |= 0x249;
				}
			}
			if (object.unwalkable && collisionMap != null) {
				collisionMap.method211(arg0, arg7, arg5, arg3, object.solid);
			}
			if (object.anInt775 != 16) {
				sceneGraph.method290(arg0, object.anInt775, arg5, arg4);
			}
			return;
		}
		if (arg3 == 3) {
			Object obj5;
			if (object.animationId == -1 && object.childIds == null) {
				obj5 = object.method578(3, arg7, k1, l1, i2, j2, -1);
			} else {
				obj5 = new AnimatedObject(arg6, arg7, 3, l1, i2, k1, j2, object.animationId, true);
			}
			sceneGraph.method282(Region.anIntArray140[arg7], ((Entity) obj5), l2, arg0, byte0, arg5, null, k2, 0, arg4);
			if (object.aBoolean779) {
				if (arg7 == 0) {
					aByteArrayArrayArray134[arg4][arg5][arg0 + 1] = 50;
				} else if (arg7 == 1) {
					aByteArrayArrayArray134[arg4][arg5 + 1][arg0 + 1] = 50;
				} else if (arg7 == 2) {
					aByteArrayArrayArray134[arg4][arg5 + 1][arg0] = 50;
				} else if (arg7 == 3) {
					aByteArrayArrayArray134[arg4][arg5][arg0] = 50;
				}
			}
			if (object.unwalkable && collisionMap != null) {
				collisionMap.method211(arg0, arg7, arg5, arg3, object.solid);
			}
			return;
		}
		if (arg3 == 9) {
			Object obj6;
			if (object.animationId == -1 && object.childIds == null) {
				obj6 = object.method578(arg3, arg7, k1, l1, i2, j2, -1);
			} else {
				obj6 = new AnimatedObject(arg6, arg7, arg3, l1, i2, k1, j2, object.animationId, true);
			}
			sceneGraph.method284(l2, byte0, k2, 1, ((Entity) obj6), 1, arg4, 0, arg0, arg5);
			if (object.unwalkable && collisionMap != null) {
				collisionMap.method212(object.solid, object.ySize, object.xSize, arg5, arg0, arg7);
			}
			return;
		}
		if (object.flatTerrain) {
			if (arg7 == 1) {
				int j3 = j2;
				j2 = i2;
				i2 = l1;
				l1 = k1;
				k1 = j3;
			} else if (arg7 == 2) {
				int k3 = j2;
				j2 = l1;
				l1 = k3;
				k3 = i2;
				i2 = k1;
				k1 = k3;
			} else if (arg7 == 3) {
				int l3 = j2;
				j2 = k1;
				k1 = l1;
				l1 = i2;
				i2 = l3;
			}
		}
		if (arg3 == 4) {
			Object obj7;
			if (object.animationId == -1 && object.childIds == null) {
				obj7 = object.method578(4, 0, k1, l1, i2, j2, -1);
			} else {
				obj7 = new AnimatedObject(arg6, 0, 4, l1, i2, k1, j2, object.animationId, true);
			}
			sceneGraph.method283(l2, arg0, arg7 * 512, arg4, 0, k2, ((Entity) obj7), arg5, byte0, 0, Region.anIntArray152[arg7]);
			return;
		}
		if (arg3 == 5) {
			int i4 = 16;
			int k4 = sceneGraph.getWallObjectUid(arg4, arg5, arg0);
			if (k4 > 0) {
				i4 = ObjectDefinition.forId(k4 >> 14 & 0x7fff).anInt775;
			}
			Object obj13;
			if (object.animationId == -1 && object.childIds == null) {
				obj13 = object.method578(4, 0, k1, l1, i2, j2, -1);
			} else {
				obj13 = new AnimatedObject(arg6, 0, 4, l1, i2, k1, j2, object.animationId, true);
			}
			sceneGraph.method283(l2, arg0, arg7 * 512, arg4, Region.anIntArray137[arg7] * i4, k2, ((Entity) obj13), arg5, byte0, Region.anIntArray144[arg7] * i4, Region.anIntArray152[arg7]);
			return;
		}
		if (arg3 == 6) {
			Object obj8;
			if (object.animationId == -1 && object.childIds == null) {
				obj8 = object.method578(4, 0, k1, l1, i2, j2, -1);
			} else {
				obj8 = new AnimatedObject(arg6, 0, 4, l1, i2, k1, j2, object.animationId, true);
			}
			sceneGraph.method283(l2, arg0, arg7, arg4, 0, k2, ((Entity) obj8), arg5, byte0, 0, 256);
			return;
		}
		if (arg3 == 7) {
			Object obj9;
			if (object.animationId == -1 && object.childIds == null) {
				obj9 = object.method578(4, 0, k1, l1, i2, j2, -1);
			} else {
				obj9 = new AnimatedObject(arg6, 0, 4, l1, i2, k1, j2, object.animationId, true);
			}
			sceneGraph.method283(l2, arg0, arg7, arg4, 0, k2, ((Entity) obj9), arg5, byte0, 0, 512);
			return;
		}
		if (arg3 == 8) {
			Object obj10;
			if (object.animationId == -1 && object.childIds == null) {
				obj10 = object.method578(4, 0, k1, l1, i2, j2, -1);
			} else {
				obj10 = new AnimatedObject(arg6, 0, 4, l1, i2, k1, j2, object.animationId, true);
			}
			sceneGraph.method283(l2, arg0, arg7, arg4, 0, k2, ((Entity) obj10), arg5, byte0, 0, 768);
		}
	}

	private static int method176(int arg0, int arg1, int arg2) {
		int l = arg0 / arg2;
		int i1 = arg0 & arg2 - 1;
		int j1 = arg1 / arg2;
		int k1 = arg1 & arg2 - 1;
		int l1 = Region.method186(l, j1);
		int i2 = Region.method186(l + 1, j1);
		int j2 = Region.method186(l, j1 + 1);
		int k2 = Region.method186(l + 1, j1 + 1);
		int l2 = Region.method184(l1, i2, i1, arg2);
		int i3 = Region.method184(j2, k2, i1, arg2);
		return Region.method184(l2, i3, k1, arg2);
	}

	private int method177(int arg0, int arg1, int arg3) {
		if (arg3 > 179) {
			arg1 /= 2;
		}
		if (arg3 > 192) {
			arg1 /= 2;
		}
		if (arg3 > 217) {
			arg1 /= 2;
		}
		if (arg3 > 243) {
			arg1 /= 2;
		}
		return (arg0 / 4 << 10) + (arg1 / 32 << 7) + arg3 / 2;
	}

	public static boolean method178(int objectId, int arg1) {
		ObjectDefinition objectDefinition = ObjectDefinition.forId(objectId);
		if (arg1 == 11) {
			arg1 = 10;
		}
		if (arg1 >= 5 && arg1 <= 8) {
			arg1 = 4;
		}
		return objectDefinition.method577(arg1);
	}

	public final void method179(int arg0, int arg1, CollisionMap collisionFlags[], int arg3, int arg4, byte arg5[], int arg6, int arg7, int arg8) {
		for (int i2 = 0; i2 < 8; i2++) {
			for (int j2 = 0; j2 < 8; j2++) {
				if (arg3 + i2 > 0 && arg3 + i2 < 103 && arg8 + j2 > 0 && arg8 + j2 < 103) {
					collisionFlags[arg7].collisionFlags[arg3 + i2][arg8 + j2] &= 0xfeffffff;
				}
			}
		}
		Stream stream = new Stream(arg5);
		for (int l2 = 0; l2 < 4; l2++) {
			for (int i3 = 0; i3 < 64; i3++) {
				for (int j3 = 0; j3 < 64; j3++) {
					if (l2 == arg0 && i3 >= arg4 && i3 < arg4 + 8 && j3 >= arg6 && j3 < arg6 + 8) {
						method181(arg8 + MapUtil.getRotatedMapChunkY(j3 & 7, arg1, i3 & 7), 0, stream, arg3 + MapUtil.getRotatedMapChunkX(arg1, j3 & 7, i3 & 7), arg7, arg1, 0);
					} else {
						method181(-1, 0, stream, -1, 0, 0, 0);
					}
				}
			}
		}
	}

	public final void method180(byte buf[], int arg1, int arg2, int arg3, int arg4, CollisionMap collisionFlags[]) {
		for (int i1 = 0; i1 < 4; i1++) {
			for (int j1 = 0; j1 < 64; j1++) {
				for (int k1 = 0; k1 < 64; k1++) {
					if (arg2 + j1 > 0 && arg2 + j1 < 103 && arg1 + k1 > 0 && arg1 + k1 < 103) {
						collisionFlags[i1].collisionFlags[arg2 + j1][arg1 + k1] &= 0xfeffffff;
					}
				}
			}
		}
		Stream buffer = new Stream(buf);
		for (int l1 = 0; l1 < 4; l1++) {
			for (int i2 = 0; i2 < 64; i2++) {
				for (int j2 = 0; j2 < 64; j2++) {
					method181(j2 + arg1, arg4, buffer, i2 + arg2, l1, 0, arg3);
				}
			}
		}
	}

	private void method181(int arg0, int arg1, Stream buffer, int arg3, int arg4, int arg5, int arg6) {
		if (arg3 >= 0 && arg3 < 104 && arg0 >= 0 && arg0 < 104) {
			aByteArrayArrayArray149[arg4][arg3][arg0] = 0;
			do {
				int l1 = buffer.getUnsignedByte();
				if (l1 == 0) {
					if (arg4 == 0) {
						anIntArrayArrayArray129[0][arg3][arg0] = -Region.method172(0xe3b7b + arg3 + arg6, 0x87cce + arg0 + arg1) * 8;
						return;
					} else {
						anIntArrayArrayArray129[arg4][arg3][arg0] = anIntArrayArrayArray129[arg4 - 1][arg3][arg0] - 240;
						return;
					}
				}
				if (l1 == 1) {
					int j2 = buffer.getUnsignedByte();
					if (j2 == 1) {
						j2 = 0;
					}
					if (arg4 == 0) {
						anIntArrayArrayArray129[0][arg3][arg0] = -j2 * 8;
						return;
					} else {
						anIntArrayArrayArray129[arg4][arg3][arg0] = anIntArrayArrayArray129[arg4 - 1][arg3][arg0] - j2 * 8;
						return;
					}
				}
				if (l1 <= 49) {
					aByteArrayArrayArray130[arg4][arg3][arg0] = buffer.getByte();
					aByteArrayArrayArray136[arg4][arg3][arg0] = (byte) ((l1 - 2) / 4);
					aByteArrayArrayArray148[arg4][arg3][arg0] = (byte) (l1 - 2 + arg5 & 3);
				} else if (l1 <= 81) {
					aByteArrayArrayArray149[arg4][arg3][arg0] = (byte) (l1 - 49);
				} else {
					aByteArrayArrayArray142[arg4][arg3][arg0] = (byte) (l1 - 81);
				}
			} while (true);
		}
		do {
			int i2 = buffer.getUnsignedByte();
			if (i2 == 0) {
				break;
			}
			if (i2 == 1) {
				buffer.getUnsignedByte();
				return;
			}
			if (i2 <= 49) {
				buffer.getUnsignedByte();
			}
		} while (true);
	}

	private int method182(int arg0, int arg1, int arg2) {
		if ((aByteArrayArrayArray149[arg1][arg2][arg0] & 8) != 0) {
			return 0;
		}
		if (arg1 > 0 && (aByteArrayArrayArray149[1][arg2][arg0] & 2) != 0) {
			return arg1 - 1;
		} else {
			return arg1;
		}
	}

	public final void method183(CollisionMap collisionFlags[], SceneGraph sceneGraph, int arg2, int arg3, int arg4, int arg5, byte arg6[], int arg7, int arg8, int arg9) {
		label0: {
			Stream stream = new Stream(arg6);
			int l1 = -1;
			do {
				int i2 = stream.getSmart();
				if (i2 == 0) {
					break label0;
				}
				l1 += i2;
				int j2 = 0;
				do {
					int k2 = stream.getSmart();
					if (k2 == 0) {
						break;
					}
					j2 += k2 - 1;
					int l2 = j2 & 0x3f;
					int i3 = j2 >> 6 & 0x3f;
					int j3 = j2 >> 12;
					int k3 = stream.getUnsignedByte();
					int l3 = k3 >> 2;
					int i4 = k3 & 3;
					if (j3 == arg2 && i3 >= arg7 && i3 < arg7 + 8 && l2 >= arg4 && l2 < arg4 + 8) {
						ObjectDefinition objectDefinition = ObjectDefinition.forId(l1);
						int j4 = arg3 + MapUtil.getRotatedLandscapeChunkX(arg8, objectDefinition.xSize, i3 & 7, l2 & 7, objectDefinition.ySize);
						int k4 = arg9 + MapUtil.getRotatedLandscapeChunkY(l2 & 7, objectDefinition.xSize, arg8, objectDefinition.ySize, i3 & 7);
						if (j4 > 0 && k4 > 0 && j4 < 103 && k4 < 103) {
							int l4 = j3;
							if ((aByteArrayArrayArray149[1][j4][k4] & 2) == 2) {
								l4--;
							}
							CollisionMap collisionMap = null;
							if (l4 >= 0) {
								collisionMap = collisionFlags[l4];
							}
							method175(k4, sceneGraph, collisionMap, l3, arg5, j4, l1, i4 + arg8 & 3);
						}
					}
				} while (true);
			} while (true);
		}
	}

	private static int method184(int arg0, int arg1, int arg2, int arg3) {
		int i1 = 0x10000 - Rasterizer.cosineTable[arg2 * 1024 / arg3] >> 1;
		return (arg0 * (0x10000 - i1) >> 16) + (arg1 * i1 >> 16);
	}

	private int randomizeMinimap(int arg0, int lightness) {
		if (arg0 == -2) {
			return 0xbc614e;
		}
		if (arg0 == -1) {
			if (lightness < 0) {
				lightness = 0;
			} else if (lightness > 127) {
				lightness = 127;
			}
			lightness = 127 - lightness;
			return lightness;
		}
		lightness = lightness * (arg0 & 0x7f) / 128;
		if (lightness < 2) {
			lightness = 2;
		} else if (lightness > 126) {
			lightness = 126;
		}
		return (arg0 & 0xff80) + lightness;
	}

	private static int method186(int arg0, int arg1) {
		int k = Region.method170(arg0 - 1, arg1 - 1) + Region.method170(arg0 + 1, arg1 - 1) + Region.method170(arg0 - 1, arg1 + 1) + Region.method170(arg0 + 1, arg1 + 1);
		int l = Region.method170(arg0 - 1, arg1) + Region.method170(arg0 + 1, arg1) + Region.method170(arg0, arg1 - 1) + Region.method170(arg0, arg1 + 1);
		int i1 = Region.method170(arg0, arg1);
		return k / 16 + l / 8 + i1 / 4;
	}

	private static int method187(int arg0, int arg1) {
		if (arg0 == -1) {
			return 12345678;
		}
		arg1 = arg1 * (arg0 & 0x7f) / 128;
		if (arg1 < 2) {
			arg1 = 2;
		} else if (arg1 > 126) {
			arg1 = 126;
		}
		return (arg0 & 0xff80) + arg1;
	}

	public static void method188(SceneGraph sceneGraph, int arg1, int arg2, int arg3, int arg4, CollisionMap collisionMap, int arg6[][][], int arg7, int arg8, int arg9) {
		int l1 = arg6[arg4][arg7][arg2];
		int i2 = arg6[arg4][arg7 + 1][arg2];
		int j2 = arg6[arg4][arg7 + 1][arg2 + 1];
		int k2 = arg6[arg4][arg7][arg2 + 1];
		int l2 = l1 + i2 + j2 + k2 >> 2;
		ObjectDefinition objectDefinition = ObjectDefinition.forId(arg8);
		int i3 = arg7 + (arg2 << 7) + (arg8 << 14) + 0x40000000;
		if (!objectDefinition.hasActions) {
			i3 += 0x80000000;
		}
		byte byte1 = (byte) ((arg1 << 6) + arg3);
		if (arg3 == 22) {
			Object obj;
			if (objectDefinition.animationId == -1 && objectDefinition.childIds == null) {
				obj = objectDefinition.method578(22, arg1, l1, i2, j2, k2, -1);
			} else {
				obj = new AnimatedObject(arg8, arg1, 22, i2, j2, l1, k2, objectDefinition.animationId, true);
			}
			sceneGraph.method280(arg9, l2, arg2, ((Entity) obj), byte1, i3, arg7);
			if (objectDefinition.unwalkable && objectDefinition.hasActions) {
				collisionMap.method213(arg2, arg7);
			}
			return;
		}
		if (arg3 == 10 || arg3 == 11) {
			Object obj1;
			if (objectDefinition.animationId == -1 && objectDefinition.childIds == null) {
				obj1 = objectDefinition.method578(10, arg1, l1, i2, j2, k2, -1);
			} else {
				obj1 = new AnimatedObject(arg8, arg1, 10, i2, j2, l1, k2, objectDefinition.animationId, true);
			}
			if (obj1 != null) {
				int j5 = 0;
				if (arg3 == 11) {
					j5 += 256;
				}
				int k4;
				int i5;
				if (arg1 == 1 || arg1 == 3) {
					k4 = objectDefinition.xSize;
					i5 = objectDefinition.ySize;
				} else {
					k4 = objectDefinition.ySize;
					i5 = objectDefinition.xSize;
				}
				sceneGraph.method284(i3, byte1, l2, i5, ((Entity) obj1), k4, arg9, j5, arg2, arg7);
			}
			if (objectDefinition.unwalkable) {
				collisionMap.method212(objectDefinition.solid, objectDefinition.ySize, objectDefinition.xSize, arg7, arg2, arg1);
			}
			return;
		}
		if (arg3 >= 12) {
			Object obj2;
			if (objectDefinition.animationId == -1 && objectDefinition.childIds == null) {
				obj2 = objectDefinition.method578(arg3, arg1, l1, i2, j2, k2, -1);
			} else {
				obj2 = new AnimatedObject(arg8, arg1, arg3, i2, j2, l1, k2, objectDefinition.animationId, true);
			}
			sceneGraph.method284(i3, byte1, l2, 1, ((Entity) obj2), 1, arg9, 0, arg2, arg7);
			if (objectDefinition.unwalkable) {
				collisionMap.method212(objectDefinition.solid, objectDefinition.ySize, objectDefinition.xSize, arg7, arg2, arg1);
			}
			return;
		}
		if (arg3 == 0) {
			Object obj3;
			if (objectDefinition.animationId == -1 && objectDefinition.childIds == null) {
				obj3 = objectDefinition.method578(0, arg1, l1, i2, j2, k2, -1);
			} else {
				obj3 = new AnimatedObject(arg8, arg1, 0, i2, j2, l1, k2, objectDefinition.animationId, true);
			}
			sceneGraph.method282(Region.anIntArray152[arg1], ((Entity) obj3), i3, arg2, byte1, arg7, null, l2, 0, arg9);
			if (objectDefinition.unwalkable) {
				collisionMap.method211(arg2, arg1, arg7, arg3, objectDefinition.solid);
			}
			return;
		}
		if (arg3 == 1) {
			Object obj4;
			if (objectDefinition.animationId == -1 && objectDefinition.childIds == null) {
				obj4 = objectDefinition.method578(1, arg1, l1, i2, j2, k2, -1);
			} else {
				obj4 = new AnimatedObject(arg8, arg1, 1, i2, j2, l1, k2, objectDefinition.animationId, true);
			}
			sceneGraph.method282(Region.anIntArray140[arg1], ((Entity) obj4), i3, arg2, byte1, arg7, null, l2, 0, arg9);
			if (objectDefinition.unwalkable) {
				collisionMap.method211(arg2, arg1, arg7, arg3, objectDefinition.solid);
			}
			return;
		}
		if (arg3 == 2) {
			int j3 = arg1 + 1 & 3;
			Object obj11;
			Object obj12;
			if (objectDefinition.animationId == -1 && objectDefinition.childIds == null) {
				obj11 = objectDefinition.method578(2, 4 + arg1, l1, i2, j2, k2, -1);
				obj12 = objectDefinition.method578(2, j3, l1, i2, j2, k2, -1);
			} else {
				obj11 = new AnimatedObject(arg8, 4 + arg1, 2, i2, j2, l1, k2, objectDefinition.animationId, true);
				obj12 = new AnimatedObject(arg8, j3, 2, i2, j2, l1, k2, objectDefinition.animationId, true);
			}
			sceneGraph.method282(Region.anIntArray152[arg1], ((Entity) obj11), i3, arg2, byte1, arg7, ((Entity) obj12), l2, Region.anIntArray152[j3], arg9);
			if (objectDefinition.unwalkable) {
				collisionMap.method211(arg2, arg1, arg7, arg3, objectDefinition.solid);
			}
			return;
		}
		if (arg3 == 3) {
			Object obj5;
			if (objectDefinition.animationId == -1 && objectDefinition.childIds == null) {
				obj5 = objectDefinition.method578(3, arg1, l1, i2, j2, k2, -1);
			} else {
				obj5 = new AnimatedObject(arg8, arg1, 3, i2, j2, l1, k2, objectDefinition.animationId, true);
			}
			sceneGraph.method282(Region.anIntArray140[arg1], ((Entity) obj5), i3, arg2, byte1, arg7, null, l2, 0, arg9);
			if (objectDefinition.unwalkable) {
				collisionMap.method211(arg2, arg1, arg7, arg3, objectDefinition.solid);
			}
			return;
		}
		if (arg3 == 9) {
			Object obj6;
			if (objectDefinition.animationId == -1 && objectDefinition.childIds == null) {
				obj6 = objectDefinition.method578(arg3, arg1, l1, i2, j2, k2, -1);
			} else {
				obj6 = new AnimatedObject(arg8, arg1, arg3, i2, j2, l1, k2, objectDefinition.animationId, true);
			}
			sceneGraph.method284(i3, byte1, l2, 1, ((Entity) obj6), 1, arg9, 0, arg2, arg7);
			if (objectDefinition.unwalkable) {
				collisionMap.method212(objectDefinition.solid, objectDefinition.ySize, objectDefinition.xSize, arg7, arg2, arg1);
			}
			return;
		}
		if (objectDefinition.flatTerrain) {
			if (arg1 == 1) {
				int k3 = k2;
				k2 = j2;
				j2 = i2;
				i2 = l1;
				l1 = k3;
			} else if (arg1 == 2) {
				int l3 = k2;
				k2 = i2;
				i2 = l3;
				l3 = j2;
				j2 = l1;
				l1 = l3;
			} else if (arg1 == 3) {
				int i4 = k2;
				k2 = l1;
				l1 = i2;
				i2 = j2;
				j2 = i4;
			}
		}
		if (arg3 == 4) {
			Object obj7;
			if (objectDefinition.animationId == -1 && objectDefinition.childIds == null) {
				obj7 = objectDefinition.method578(4, 0, l1, i2, j2, k2, -1);
			} else {
				obj7 = new AnimatedObject(arg8, 0, 4, i2, j2, l1, k2, objectDefinition.animationId, true);
			}
			sceneGraph.method283(i3, arg2, arg1 * 512, arg9, 0, l2, ((Entity) obj7), arg7, byte1, 0, Region.anIntArray152[arg1]);
			return;
		}
		if (arg3 == 5) {
			int j4 = 16;
			int l4 = sceneGraph.getWallObjectUid(arg9, arg7, arg2);
			if (l4 > 0) {
				j4 = ObjectDefinition.forId(l4 >> 14 & 0x7fff).anInt775;
			}
			Object obj13;
			if (objectDefinition.animationId == -1 && objectDefinition.childIds == null) {
				obj13 = objectDefinition.method578(4, 0, l1, i2, j2, k2, -1);
			} else {
				obj13 = new AnimatedObject(arg8, 0, 4, i2, j2, l1, k2, objectDefinition.animationId, true);
			}
			sceneGraph.method283(i3, arg2, arg1 * 512, arg9, Region.anIntArray137[arg1] * j4, l2, ((Entity) obj13), arg7, byte1, Region.anIntArray144[arg1] * j4, Region.anIntArray152[arg1]);
			return;
		}
		if (arg3 == 6) {
			Object obj8;
			if (objectDefinition.animationId == -1 && objectDefinition.childIds == null) {
				obj8 = objectDefinition.method578(4, 0, l1, i2, j2, k2, -1);
			} else {
				obj8 = new AnimatedObject(arg8, 0, 4, i2, j2, l1, k2, objectDefinition.animationId, true);
			}
			sceneGraph.method283(i3, arg2, arg1, arg9, 0, l2, ((Entity) obj8), arg7, byte1, 0, 256);
			return;
		}
		if (arg3 == 7) {
			Object obj9;
			if (objectDefinition.animationId == -1 && objectDefinition.childIds == null) {
				obj9 = objectDefinition.method578(4, 0, l1, i2, j2, k2, -1);
			} else {
				obj9 = new AnimatedObject(arg8, 0, 4, i2, j2, l1, k2, objectDefinition.animationId, true);
			}
			sceneGraph.method283(i3, arg2, arg1, arg9, 0, l2, ((Entity) obj9), arg7, byte1, 0, 512);
			return;
		}
		if (arg3 == 8) {
			Object obj10;
			if (objectDefinition.animationId == -1 && objectDefinition.childIds == null) {
				obj10 = objectDefinition.method578(4, 0, l1, i2, j2, k2, -1);
			} else {
				obj10 = new AnimatedObject(arg8, 0, 4, i2, j2, l1, k2, objectDefinition.animationId, true);
			}
			sceneGraph.method283(i3, arg2, arg1, arg9, 0, l2, ((Entity) obj10), arg7, byte1, 0, 768);
		}
	}

	public static boolean method189(int arg0, byte[] arg1, int arg2) {
		boolean flag = true;
		Stream stream = new Stream(arg1);
		int i = -1;
		for (;;) {
			int j = stream.getSmart();
			if (j == 0) {
				break;
			}
			i += j;
			int k = 0;
			boolean flag1 = false;
			for (;;) {
				if (flag1) {
					int l = stream.getSmart();
					if (l == 0) {
						break;
					}
					stream.getUnsignedByte();
				} else {
					int i1 = stream.getSmart();
					if (i1 == 0) {
						break;
					}
					k += i1 - 1;
					int j1 = k & 0x3f;
					int k1 = k >> 6 & 0x3f;
					int l1 = stream.getUnsignedByte() >> 2;
					int i2 = k1 + arg0;
					int j2 = j1 + arg2;
					if (i2 > 0 && j2 > 0 && i2 < 103 && j2 < 103) {
						ObjectDefinition objectDefinition = ObjectDefinition.forId(i);
						if (l1 != 22 || !Region.lowMem || objectDefinition.hasActions || objectDefinition.aBoolean736) {
							flag &= objectDefinition.method579();
							flag1 = true;
						}
					}
				}
			}
		}
		return flag;
	}

	public final void method190(int arg0, CollisionMap collisionFlags[], int arg2, SceneGraph sceneGraph, byte buf[]) {
		label0: {
			Stream buffer = new Stream(buf);
			int l = -1;
			do {
				int i1 = buffer.getSmart();
				if (i1 == 0) {
					break label0;
				}
				l += i1;
				int j1 = 0;
				do {
					int k1 = buffer.getSmart();
					if (k1 == 0) {
						break;
					}
					j1 += k1 - 1;
					int l1 = j1 & 0x3f;
					int i2 = j1 >> 6 & 0x3f;
					int j2 = j1 >> 12;
					int k2 = buffer.getUnsignedByte();
					int l2 = k2 >> 2;
					int i3 = k2 & 3;
					int j3 = i2 + arg0;
					int k3 = l1 + arg2;
					if (j3 > 0 && k3 > 0 && j3 < 103 && k3 < 103) {
						int l3 = j2;
						if ((aByteArrayArrayArray149[1][j3][k3] & 2) == 2) {
							l3--;
						}
						CollisionMap collisionMap = null;
						if (l3 >= 0) {
							collisionMap = collisionFlags[l3];
						}
						method175(k3, sceneGraph, collisionMap, l2, j2, j3, l, i3);
					}
				} while (true);
			} while (true);
		}
	}
	private static int anInt123 = (int) (Math.random() * 17D) - 8;
	private final int[] anIntArray124;
	private final int[] anIntArray125;
	private final int[] anIntArray126;
	private final int[] anIntArray127;
	private final int[] anIntArray128;
	private final int[][][] anIntArrayArrayArray129;
	private final byte[][][] aByteArrayArrayArray130;
	static int anInt131;
	private static int anInt133 = (int) (Math.random() * 33D) - 16;
	private final byte[][][] aByteArrayArrayArray134;
	private final int[][][] anIntArrayArrayArray135;
	private final byte[][][] aByteArrayArrayArray136;
	private static final int anIntArray137[] = { 1, 0, -1, 0 };
	private final int[][] anIntArrayArray139;
	private static final int anIntArray140[] = { 16, 32, 64, 128 };
	private final byte[][][] aByteArrayArrayArray142;
	private static final int anIntArray144[] = { 0, -1, 0, 1 };
	static int anInt145 = 99;
	private final int anInt146;
	private final int anInt147;
	private final byte[][][] aByteArrayArrayArray148;
	private final byte[][][] aByteArrayArrayArray149;
	static boolean lowMem = true;
	private static final int anIntArray152[] = { 1, 2, 4, 8 };
}
