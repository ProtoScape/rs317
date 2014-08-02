public class Model extends Entity {

	public static void dispose() {
		Model.modelHeaderCache = null;
		Model.edgeRestricted = null;
		Model.aBooleanArray1664 = null;
		Model.triangleX = null;
		Model.anIntArray1667 = null;
		Model.zoom = null;
		Model.anIntArray1669 = null;
		Model.anIntArray1670 = null;
		Model.anIntArray1671 = null;
		Model.anIntArrayArray1672 = null;
		Model.faceLists = null;
		Model.anIntArrayArray1674 = null;
		Model.anIntArray1675 = null;
		Model.anIntArray1676 = null;
		Model.anIntArray1677 = null;
		Model.modelIntArray1 = null;
		Model.modelIntArray2 = null;
		Model.modelIntArray3 = null;
		Model.modelIntArray4 = null;
	}

	public void read525Model(byte buf[], int id) {
		Stream offsets1 = new Stream(buf);
		Stream offsets2 = new Stream(buf);
		Stream offsets3 = new Stream(buf);
		Stream offsets4 = new Stream(buf);
		Stream offsets5 = new Stream(buf);
		Stream offsets6 = new Stream(buf);
		Stream offsets7 = new Stream(buf);
		offsets1.offset = buf.length - 23;
		int numVertices = offsets1.getUnsignedShort();
		int numTriangles = offsets1.getUnsignedShort();
		int numTexTriangles = offsets1.getUnsignedByte();
		ModelHeader header = Model.modelHeaderCache[id] = new ModelHeader();
		header.buffer = buf;
		header.vertexCount = numVertices;
		header.triangleCount = numTriangles;
		header.texturedTriangleCount = numTexTriangles;
		int i = offsets1.getUnsignedByte();
		boolean flag = (1 & i) == 1;
		int i2 = offsets1.getUnsignedByte();
		int j2 = offsets1.getUnsignedByte();
		int k2 = offsets1.getUnsignedByte();
		int l2 = offsets1.getUnsignedByte();
		int i3 = offsets1.getUnsignedByte();
		int j3 = offsets1.getUnsignedShort();
		int k3 = offsets1.getUnsignedShort();
		int l3 = offsets1.getUnsignedShort();
		int i4 = offsets1.getUnsignedShort();
		int j4 = offsets1.getUnsignedShort();
		int k4 = 0;
		int l4 = 0;
		int i5 = 0;
		byte[] x = null;
		byte[] O = null;
		byte[] J = null;
		byte[] F = null;
		byte[] cb = null;
		byte[] gb = null;
		byte[] lb = null;
		int[] kb = null;
		int[] y = null;
		int[] N = null;
		short[] D = null;
		int[] triangleColours2 = new int[numTriangles];
		if (numTexTriangles > 0) {
			O = new byte[numTexTriangles];
			offsets1.offset = 0;
			for (int j5 = 0; j5 < numTexTriangles; j5++) {
				byte byte0 = O[j5] = offsets1.getByte();
				if (byte0 == 0) {
					k4++;
				}
				if (byte0 >= 1 && byte0 <= 3) {
					l4++;
				}
				if (byte0 == 2) {
					i5++;
				}
			}
		}
		int k5 = numTexTriangles;
		int l5 = k5;
		k5 += numVertices;
		int i6 = k5;
		if (i == 1) {
			k5 += numTriangles;
		}
		int j6 = k5;
		k5 += numTriangles;
		int k6 = k5;
		if (i2 == 255) {
			k5 += numTriangles;
		}
		int l6 = k5;
		if (k2 == 1) {
			k5 += numTriangles;
		}
		int i7 = k5;
		if (i3 == 1) {
			k5 += numVertices;
		}
		int j7 = k5;
		if (j2 == 1) {
			k5 += numTriangles;
		}
		int k7 = k5;
		k5 += i4;
		int l7 = k5;
		if (l2 == 1) {
			k5 += numTriangles * 2;
		}
		int i8 = k5;
		k5 += j4;
		int j8 = k5;
		k5 += numTriangles * 2;
		int k8 = k5;
		k5 += j3;
		int l8 = k5;
		k5 += k3;
		int i9 = k5;
		k5 += l3;
		int j9 = k5;
		k5 += k4 * 6;
		int k9 = k5;
		k5 += l4 * 6;
		int l9 = k5;
		k5 += l4 * 6;
		int i10 = k5;
		k5 += l4;
		int j10 = k5;
		k5 += l4;
		int k10 = k5;
		k5 += l4 + i5 * 2;
		int[] vertexX = new int[numVertices];
		int[] vertexY = new int[numVertices];
		int[] vertexZ = new int[numVertices];
		int[] facePoint1 = new int[numTriangles];
		int[] facePoint2 = new int[numTriangles];
		int[] facePoint3 = new int[numTriangles];
		vertexVSkins = new int[numVertices];
		faceTypes = new int[numTriangles];
		facePriority = new int[numTriangles];
		transparency = new int[numTriangles];
		tSkin = new int[numTriangles];
		if (i3 == 1) {
			vertexVSkins = new int[numVertices];
		}
		if (flag) {
			faceTypes = new int[numTriangles];
		}
		if (i2 == 255) {
			facePriority = new int[numTriangles];
		} else {
		}
		if (j2 == 1) {
			transparency = new int[numTriangles];
		}
		if (k2 == 1) {
			tSkin = new int[numTriangles];
		}
		if (l2 == 1) {
			D = new short[numTriangles];
		}
		if (l2 == 1 && numTexTriangles > 0) {
			x = new byte[numTriangles];
		}
		triangleColours2 = new int[numTriangles];
		int[] texTrianglesPoint1 = null;
		int[] texTrianglesPoint2 = null;
		int[] texTrianglesPoint3 = null;
		if (numTexTriangles > 0) {
			texTrianglesPoint1 = new int[numTexTriangles];
			texTrianglesPoint2 = new int[numTexTriangles];
			texTrianglesPoint3 = new int[numTexTriangles];
			if (l4 > 0) {
				kb = new int[l4];
				N = new int[l4];
				y = new int[l4];
				gb = new byte[l4];
				lb = new byte[l4];
				F = new byte[l4];
			}
			if (i5 > 0) {
				cb = new byte[i5];
				J = new byte[i5];
			}
		}
		offsets1.offset = l5;
		offsets2.offset = k8;
		offsets3.offset = l8;
		offsets4.offset = i9;
		offsets5.offset = i7;
		int l10 = 0;
		int i11 = 0;
		int j11 = 0;
		for (int k11 = 0; k11 < numVertices; k11++) {
			int l11 = offsets1.getUnsignedByte();
			int j12 = 0;
			if ((l11 & 1) != 0) {
				j12 = offsets2.getUnsignedSmart();
			}
			int l12 = 0;
			if ((l11 & 2) != 0) {
				l12 = offsets3.getUnsignedSmart();
			}
			int j13 = 0;
			if ((l11 & 4) != 0) {
				j13 = offsets4.getUnsignedSmart();
			}
			vertexX[k11] = l10 + j12;
			vertexY[k11] = i11 + l12;
			vertexZ[k11] = j11 + j13;
			l10 = vertexX[k11];
			i11 = vertexY[k11];
			j11 = vertexZ[k11];
			if (vertexVSkins != null) {
				vertexVSkins[k11] = offsets5.getUnsignedByte();
			}
		}
		offsets1.offset = j8;
		offsets2.offset = i6;
		offsets3.offset = k6;
		offsets4.offset = j7;
		offsets5.offset = l6;
		offsets6.offset = l7;
		offsets7.offset = i8;
		for (int i12 = 0; i12 < numTriangles; i12++) {
			triangleColours2[i12] = offsets1.getUnsignedShort();
			if (i == 1) {
				faceTypes[i12] = offsets2.getByte();
				if (faceTypes[i12] == 2) {
					triangleColours2[i12] = 65535;
				}
				faceTypes[i12] = 0;
			}
			if (i2 == 255) {
				facePriority[i12] = offsets3.getByte();
			}
			if (j2 == 1) {
				transparency[i12] = offsets4.getByte();
				if (transparency[i12] < 0) {
					transparency[i12] = 256 + transparency[i12];
				}
			}
			if (k2 == 1) {
				tSkin[i12] = offsets5.getUnsignedByte();
			}
			if (l2 == 1) {
				D[i12] = (short) (offsets6.getUnsignedShort() - 1);
			}
			if (x != null) {
				if (D[i12] != -1) {
					x[i12] = (byte) (offsets7.getUnsignedByte() - 1);
				} else {
					x[i12] = -1;
				}
			}
		}
		offsets1.offset = k7;
		offsets2.offset = j6;
		int k12 = 0;
		int i13 = 0;
		int k13 = 0;
		int l13 = 0;
		for (int i14 = 0; i14 < numTriangles; i14++) {
			int j14 = offsets2.getUnsignedByte();
			if (j14 == 1) {
				k12 = offsets1.getUnsignedSmart() + l13;
				l13 = k12;
				i13 = offsets1.getUnsignedSmart() + l13;
				l13 = i13;
				k13 = offsets1.getUnsignedSmart() + l13;
				l13 = k13;
				facePoint1[i14] = k12;
				facePoint2[i14] = i13;
				facePoint3[i14] = k13;
			}
			if (j14 == 2) {
				i13 = k13;
				k13 = offsets1.getUnsignedSmart() + l13;
				l13 = k13;
				facePoint1[i14] = k12;
				facePoint2[i14] = i13;
				facePoint3[i14] = k13;
			}
			if (j14 == 3) {
				k12 = k13;
				k13 = offsets1.getUnsignedSmart() + l13;
				l13 = k13;
				facePoint1[i14] = k12;
				facePoint2[i14] = i13;
				facePoint3[i14] = k13;
			}
			if (j14 == 4) {
				int l14 = k12;
				k12 = i13;
				i13 = l14;
				k13 = offsets1.getUnsignedSmart() + l13;
				l13 = k13;
				facePoint1[i14] = k12;
				facePoint2[i14] = i13;
				facePoint3[i14] = k13;
			}
		}
		offsets1.offset = j9;
		offsets2.offset = k9;
		offsets3.offset = l9;
		offsets4.offset = i10;
		offsets5.offset = j10;
		offsets6.offset = k10;
		for (int k14 = 0; k14 < numTexTriangles; k14++) {
			int i15 = O[k14] & 0xff;
			if (i15 == 0) {
				texTrianglesPoint1[k14] = offsets1.getUnsignedShort();
				texTrianglesPoint2[k14] = offsets1.getUnsignedShort();
				texTrianglesPoint3[k14] = offsets1.getUnsignedShort();
			}
			if (i15 == 1) {
				texTrianglesPoint1[k14] = offsets2.getUnsignedShort();
				texTrianglesPoint2[k14] = offsets2.getUnsignedShort();
				texTrianglesPoint3[k14] = offsets2.getUnsignedShort();
				kb[k14] = offsets3.getUnsignedShort();
				N[k14] = offsets3.getUnsignedShort();
				y[k14] = offsets3.getUnsignedShort();
				gb[k14] = offsets4.getByte();
				lb[k14] = offsets5.getByte();
				F[k14] = offsets6.getByte();
			}
			if (i15 == 2) {
				texTrianglesPoint1[k14] = offsets2.getUnsignedShort();
				texTrianglesPoint2[k14] = offsets2.getUnsignedShort();
				texTrianglesPoint3[k14] = offsets2.getUnsignedShort();
				kb[k14] = offsets3.getUnsignedShort();
				N[k14] = offsets3.getUnsignedShort();
				y[k14] = offsets3.getUnsignedShort();
				gb[k14] = offsets4.getByte();
				lb[k14] = offsets5.getByte();
				F[k14] = offsets6.getByte();
				cb[k14] = offsets6.getByte();
				J[k14] = offsets6.getByte();
			}
			if (i15 == 3) {
				texTrianglesPoint1[k14] = offsets2.getUnsignedShort();
				texTrianglesPoint2[k14] = offsets2.getUnsignedShort();
				texTrianglesPoint3[k14] = offsets2.getUnsignedShort();
				kb[k14] = offsets3.getUnsignedShort();
				N[k14] = offsets3.getUnsignedShort();
				y[k14] = offsets3.getUnsignedShort();
				gb[k14] = offsets4.getByte();
				lb[k14] = offsets5.getByte();
				F[k14] = offsets6.getByte();
			}
		}
		if (i2 != 255) {
			for (int i12 = 0; i12 < numTriangles; i12++) {
				facePriority[i12] = i2;
			}
		}
		triangleColors = triangleColours2;
		vertexCount = numVertices;
		triangleCount = numTriangles;
		xVertex = vertexX;
		yVertex = vertexY;
		zVertex = vertexZ;
		facePointA = facePoint1;
		facePointB = facePoint2;
		facePointC = facePoint3;
	}

	public Model(int modelId) {
		byte[] is = Model.modelHeaderCache[modelId].buffer;
		if (is[is.length - 1] == -1 && is[is.length - 2] == -1) {
			read622Model(is, modelId);
		} else {
			readOldModel(modelId);
		}
	}

	public void read622Model(byte abyte0[], int modelID) {
		Stream nc1 = new Stream(abyte0);
		Stream nc2 = new Stream(abyte0);
		Stream nc3 = new Stream(abyte0);
		Stream nc4 = new Stream(abyte0);
		Stream nc5 = new Stream(abyte0);
		Stream nc6 = new Stream(abyte0);
		Stream nc7 = new Stream(abyte0);
		nc1.offset = abyte0.length - 23;
		int numVertices = nc1.getUnsignedShort();
		int numTriangles = nc1.getUnsignedShort();
		int numTexTriangles = nc1.getUnsignedByte();
		ModelHeader ModelDef_1 = Model.modelHeaderCache[modelID] = new ModelHeader();
		ModelDef_1.buffer = abyte0;
		ModelDef_1.vertexCount = numVertices;
		ModelDef_1.triangleCount = numTriangles;
		ModelDef_1.texturedTriangleCount = numTexTriangles;
		int l1 = nc1.getUnsignedByte();
		boolean bool = (0x1 & l1 ^ 0xffffffff) == -2;
		boolean bool_26_ = (0x8 & l1) == 8;
		boolean scalingRequired = false;
		if (!bool_26_) {
			read525Model(abyte0, modelID);
			return;
		}
		int newformat = 0;
		if (bool_26_) {
			nc1.offset -= 7;
			newformat = nc1.getUnsignedByte();
			nc1.offset += 6;
		}
		if (newformat == 15) {
			scalingRequired = true;
		}
		int i2 = nc1.getUnsignedByte();
		int j2 = nc1.getUnsignedByte();
		int k2 = nc1.getUnsignedByte();
		int l2 = nc1.getUnsignedByte();
		int i3 = nc1.getUnsignedByte();
		int j3 = nc1.getUnsignedShort();
		int k3 = nc1.getUnsignedShort();
		int l3 = nc1.getUnsignedShort();
		int i4 = nc1.getUnsignedShort();
		int j4 = nc1.getUnsignedShort();
		int k4 = 0;
		int l4 = 0;
		int i5 = 0;
		byte[] x = null;
		byte[] O = null;
		byte[] J = null;
		byte[] F = null;
		byte[] cb = null;
		byte[] gb = null;
		byte[] lb = null;
		int[] kb = null;
		int[] y = null;
		int[] N = null;
		short[] D = null;
		int[] triangleColours2 = new int[numTriangles];
		if (numTexTriangles > 0) {
			O = new byte[numTexTriangles];
			nc1.offset = 0;
			for (int j5 = 0; j5 < numTexTriangles; j5++) {
				byte byte0 = O[j5] = nc1.getByte();
				if (byte0 == 0) {
					k4++;
				}
				if (byte0 >= 1 && byte0 <= 3) {
					l4++;
				}
				if (byte0 == 2) {
					i5++;
				}
			}
		}
		int k5 = numTexTriangles;
		int l5 = k5;
		k5 += numVertices;
		int i6 = k5;
		if (bool) {
			k5 += numTriangles;
		}
		if (l1 == 1) {
			k5 += numTriangles;
		}
		int j6 = k5;
		k5 += numTriangles;
		int k6 = k5;
		if (i2 == 255) {
			k5 += numTriangles;
		}
		int l6 = k5;
		if (k2 == 1) {
			k5 += numTriangles;
		}
		int i7 = k5;
		if (i3 == 1) {
			k5 += numVertices;
		}
		int j7 = k5;
		if (j2 == 1) {
			k5 += numTriangles;
		}
		int k7 = k5;
		k5 += i4;
		int l7 = k5;
		if (l2 == 1) {
			k5 += numTriangles * 2;
		}
		int i8 = k5;
		k5 += j4;
		int j8 = k5;
		k5 += numTriangles * 2;
		int k8 = k5;
		k5 += j3;
		int l8 = k5;
		k5 += k3;
		int i9 = k5;
		k5 += l3;
		int j9 = k5;
		k5 += k4 * 6;
		int k9 = k5;
		k5 += l4 * 6;
		int i_59_ = 6;
		if (newformat != 14) {
			if (newformat >= 15) {
				i_59_ = 9;
			}
		} else {
			i_59_ = 7;
		}
		int l9 = k5;
		k5 += i_59_ * l4;
		int i10 = k5;
		k5 += l4;
		int j10 = k5;
		k5 += l4;
		int k10 = k5;
		k5 += l4 + i5 * 2;
		int[] vertexX = new int[numVertices];
		int[] vertexY = new int[numVertices];
		int[] vertexZ = new int[numVertices];
		int[] facePoint1 = new int[numTriangles];
		int[] facePoint2 = new int[numTriangles];
		int[] facePoint3 = new int[numTriangles];
		vertexVSkins = new int[numVertices];
		faceTypes = new int[numTriangles];
		facePriority = new int[numTriangles];
		transparency = new int[numTriangles];
		tSkin = new int[numTriangles];
		if (i3 == 1) {
			vertexVSkins = new int[numVertices];
		}
		if (bool) {
			faceTypes = new int[numTriangles];
		}
		if (i2 == 255) {
			facePriority = new int[numTriangles];
		} else {
		}
		if (j2 == 1) {
			transparency = new int[numTriangles];
		}
		if (k2 == 1) {
			tSkin = new int[numTriangles];
		}
		if (l2 == 1) {
			D = new short[numTriangles];
		}
		if (l2 == 1 && numTexTriangles > 0) {
			x = new byte[numTriangles];
		}
		triangleColours2 = new int[numTriangles];
		int[] texTrianglesPoint1 = null;
		int[] texTrianglesPoint2 = null;
		int[] texTrianglesPoint3 = null;
		if (numTexTriangles > 0) {
			texTrianglesPoint1 = new int[numTexTriangles];
			texTrianglesPoint2 = new int[numTexTriangles];
			texTrianglesPoint3 = new int[numTexTriangles];
			if (l4 > 0) {
				kb = new int[l4];
				N = new int[l4];
				y = new int[l4];
				gb = new byte[l4];
				lb = new byte[l4];
				F = new byte[l4];
			}
			if (i5 > 0) {
				cb = new byte[i5];
				J = new byte[i5];
			}
		}
		nc1.offset = l5;
		nc2.offset = k8;
		nc3.offset = l8;
		nc4.offset = i9;
		nc5.offset = i7;
		int l10 = 0;
		int i11 = 0;
		int j11 = 0;
		for (int k11 = 0; k11 < numVertices; k11++) {
			int l11 = nc1.getUnsignedByte();
			int j12 = 0;
			if ((l11 & 1) != 0) {
				j12 = nc2.getUnsignedSmart();
			}
			int l12 = 0;
			if ((l11 & 2) != 0) {
				l12 = nc3.getUnsignedSmart();
			}
			int j13 = 0;
			if ((l11 & 4) != 0) {
				j13 = nc4.getUnsignedSmart();
			}
			vertexX[k11] = l10 + j12;
			vertexY[k11] = i11 + l12;
			vertexZ[k11] = j11 + j13;
			l10 = vertexX[k11];
			i11 = vertexY[k11];
			j11 = vertexZ[k11];
			if (vertexVSkins != null) {
				vertexVSkins[k11] = nc5.getUnsignedByte();
			}
		}
		nc1.offset = j8;
		nc2.offset = i6;
		nc3.offset = k6;
		nc4.offset = j7;
		nc5.offset = l6;
		nc6.offset = l7;
		nc7.offset = i8;
		for (int i12 = 0; i12 < numTriangles; i12++) {
			triangleColours2[i12] = nc1.getUnsignedShort();
			if (l1 == 1) {
				faceTypes[i12] = nc2.getByte();
				if (faceTypes[i12] == 2) {
					triangleColours2[i12] = 65535;
				}
				faceTypes[i12] = 0;
			}
			if (i2 == 255) {
				facePriority[i12] = nc3.getByte();
			}
			if (j2 == 1) {
				transparency[i12] = nc4.getByte();
				if (transparency[i12] < 0) {
					transparency[i12] = 256 + transparency[i12];
				}
			}
			if (k2 == 1) {
				tSkin[i12] = nc5.getUnsignedByte();
			}
			if (l2 == 1) {
				D[i12] = (short) (nc6.getUnsignedShort() - 1);
			}
			if (x != null) {
				if (D[i12] != -1) {
					x[i12] = (byte) (nc7.getUnsignedByte() - 1);
				} else {
					x[i12] = -1;
				}
			}
		}
		nc1.offset = k7;
		nc2.offset = j6;
		int k12 = 0;
		int i13 = 0;
		int k13 = 0;
		int l13 = 0;
		for (int i14 = 0; i14 < numTriangles; i14++) {
			int j14 = nc2.getUnsignedByte();
			if (j14 == 1) {
				k12 = nc1.getUnsignedSmart() + l13;
				l13 = k12;
				i13 = nc1.getUnsignedSmart() + l13;
				l13 = i13;
				k13 = nc1.getUnsignedSmart() + l13;
				l13 = k13;
				facePoint1[i14] = k12;
				facePoint2[i14] = i13;
				facePoint3[i14] = k13;
			}
			if (j14 == 2) {
				i13 = k13;
				k13 = nc1.getUnsignedSmart() + l13;
				l13 = k13;
				facePoint1[i14] = k12;
				facePoint2[i14] = i13;
				facePoint3[i14] = k13;
			}
			if (j14 == 3) {
				k12 = k13;
				k13 = nc1.getUnsignedSmart() + l13;
				l13 = k13;
				facePoint1[i14] = k12;
				facePoint2[i14] = i13;
				facePoint3[i14] = k13;
			}
			if (j14 == 4) {
				int l14 = k12;
				k12 = i13;
				i13 = l14;
				k13 = nc1.getUnsignedSmart() + l13;
				l13 = k13;
				facePoint1[i14] = k12;
				facePoint2[i14] = i13;
				facePoint3[i14] = k13;
			}
		}
		nc1.offset = j9;
		nc2.offset = k9;
		nc3.offset = l9;
		nc4.offset = i10;
		nc5.offset = j10;
		nc6.offset = k10;
		for (int k14 = 0; k14 < numTexTriangles; k14++) {
			int i15 = O[k14] & 0xff;
			if (i15 == 0) {
				texTrianglesPoint1[k14] = nc1.getUnsignedShort();
				texTrianglesPoint2[k14] = nc1.getUnsignedShort();
				texTrianglesPoint3[k14] = nc1.getUnsignedShort();
			}
			if (i15 == 1) {
				texTrianglesPoint1[k14] = nc2.getUnsignedShort();
				texTrianglesPoint2[k14] = nc2.getUnsignedShort();
				texTrianglesPoint3[k14] = nc2.getUnsignedShort();
				if (newformat < 15) {
					kb[k14] = nc3.getUnsignedShort();
					if (newformat >= 14) {
						N[k14] = nc3.get24BitInt();
					} else {
						N[k14] = nc3.getUnsignedShort();
					}
					y[k14] = nc3.getUnsignedShort();
				} else {
					kb[k14] = nc3.get24BitInt();
					N[k14] = nc3.get24BitInt();
					y[k14] = nc3.get24BitInt();
				}
				gb[k14] = nc4.getByte();
				lb[k14] = nc5.getByte();
				F[k14] = nc6.getByte();
			}
			if (i15 == 2) {
				texTrianglesPoint1[k14] = nc2.getUnsignedShort();
				texTrianglesPoint2[k14] = nc2.getUnsignedShort();
				texTrianglesPoint3[k14] = nc2.getUnsignedShort();
				if (newformat >= 15) {
					kb[k14] = nc3.get24BitInt();
					N[k14] = nc3.get24BitInt();
					y[k14] = nc3.get24BitInt();
				} else {
					kb[k14] = nc3.getUnsignedShort();
					if (newformat < 14) {
						N[k14] = nc3.getUnsignedShort();
					} else {
						N[k14] = nc3.get24BitInt();
					}
					y[k14] = nc3.getUnsignedShort();
				}
				gb[k14] = nc4.getByte();
				lb[k14] = nc5.getByte();
				F[k14] = nc6.getByte();
				cb[k14] = nc6.getByte();
				J[k14] = nc6.getByte();
			}
			if (i15 == 3) {
				texTrianglesPoint1[k14] = nc2.getUnsignedShort();
				texTrianglesPoint2[k14] = nc2.getUnsignedShort();
				texTrianglesPoint3[k14] = nc2.getUnsignedShort();
				if (newformat < 15) {
					kb[k14] = nc3.getUnsignedShort();
					if (newformat < 14) {
						N[k14] = nc3.getUnsignedShort();
					} else {
						N[k14] = nc3.get24BitInt();
					}
					y[k14] = nc3.getUnsignedShort();
				} else {
					kb[k14] = nc3.get24BitInt();
					N[k14] = nc3.get24BitInt();
					y[k14] = nc3.get24BitInt();
				}
				gb[k14] = nc4.getByte();
				lb[k14] = nc5.getByte();
				F[k14] = nc6.getByte();
			}
		}
		if (i2 != 255) {
			for (int i12 = 0; i12 < numTriangles; i12++) {
				facePriority[i12] = i2;
			}
		}
		triangleColors = triangleColours2;
		vertexCount = numVertices;
		triangleCount = numTriangles;
		xVertex = vertexX;
		yVertex = vertexY;
		zVertex = vertexZ;
		facePointA = facePoint1;
		facePointB = facePoint2;
		facePointC = facePoint3;
		if (scalingRequired) {
			for (int i1 = 0; i1 < vertexCount; i1++) {
				xVertex[i1] = xVertex[i1] / 4;
				yVertex[i1] = yVertex[i1] / 4;
				zVertex[i1] = zVertex[i1] / 4;
			}
		}
	}

	private void readOldModel(int id) {
		// aBoolean1618 = true;
		aBoolean1659 = false;
		// Model.dummyCounter++;
		ModelHeader modelHeader = Model.modelHeaderCache[id];
		vertexCount = modelHeader.vertexCount;
		triangleCount = modelHeader.triangleCount;
		texturedTriangleCount = modelHeader.texturedTriangleCount;
		xVertex = new int[vertexCount];
		yVertex = new int[vertexCount];
		zVertex = new int[vertexCount];
		facePointA = new int[triangleCount];
		facePointB = new int[triangleCount];
		facePointC = new int[triangleCount];
		trianglePIndex = new int[texturedTriangleCount];
		triangleMIndex = new int[texturedTriangleCount];
		triangleNIndex = new int[texturedTriangleCount];
		if (modelHeader.vertexSkinOffset >= 0) {
			vertexVSkins = new int[vertexCount];
		}
		if (modelHeader.triangleFaceTypeOffset >= 0) {
			faceTypes = new int[triangleCount];
		}
		if (modelHeader.trianglePriorityOffset >= 0) {
			facePriority = new int[triangleCount];
		} else {
			priority = -modelHeader.trianglePriorityOffset - 1;
		}
		if (modelHeader.triangleTransparencyOffset >= 0) {
			transparency = new int[triangleCount];
		}
		if (modelHeader.triangleSkinningOffset >= 0) {
			tSkin = new int[triangleCount];
		}
		triangleColors = new int[triangleCount];
		Stream offsets1 = new Stream(modelHeader.buffer);
		offsets1.offset = modelHeader.vertexModOffset;
		Stream offsets2 = new Stream(modelHeader.buffer);
		offsets2.offset = modelHeader.vertexOffsetX;
		Stream offsets3 = new Stream(modelHeader.buffer);
		offsets3.offset = modelHeader.vertexOffsetY;
		Stream offsets4 = new Stream(modelHeader.buffer);
		offsets4.offset = modelHeader.vertexOffsetZ;
		Stream offsets5 = new Stream(modelHeader.buffer);
		offsets5.offset = modelHeader.vertexSkinOffset;
		int vertXOff = 0;
		int vertYOff = 0;
		int vertZOff = 0;
		for (int i = 0; i < vertexCount; i++) {
			int v = offsets1.getUnsignedByte();
			int xModOff = 0;
			if ((v & 1) != 0) {
				xModOff = offsets2.getUnsignedSmart();
			}
			int yModOff = 0;
			if ((v & 2) != 0) {
				yModOff = offsets3.getUnsignedSmart();
			}
			int zModOff = 0;
			if ((v & 4) != 0) {
				zModOff = offsets4.getUnsignedSmart();
			}
			xVertex[i] = vertXOff + xModOff;
			yVertex[i] = vertYOff + yModOff;
			zVertex[i] = vertZOff + zModOff;
			vertXOff = xVertex[i];
			vertYOff = yVertex[i];
			vertZOff = zVertex[i];
			if (vertexVSkins != null) {
				vertexVSkins[i] = offsets5.getUnsignedByte();
			}
		}
		offsets1.offset = modelHeader.triangleColorOffset;
		offsets2.offset = modelHeader.triangleFaceTypeOffset;
		offsets3.offset = modelHeader.trianglePriorityOffset;
		offsets4.offset = modelHeader.triangleTransparencyOffset;
		offsets5.offset = modelHeader.triangleSkinningOffset;
		for (int i = 0; i < triangleCount; i++) {
			triangleColors[i] = offsets1.getUnsignedShort();
			if (faceTypes != null) {
				faceTypes[i] = offsets2.getUnsignedByte();
			}
			if (facePriority != null) {
				facePriority[i] = offsets3.getUnsignedByte();
			}
			if (transparency != null) {
				transparency[i] = offsets4.getUnsignedByte();
			}
			if (tSkin != null) {
				tSkin[i] = offsets5.getUnsignedByte();
			}
		}
		offsets1.offset = modelHeader.triangleVertexPointOffset;
		offsets2.offset = modelHeader.triangleMeshLinkOffset;
		int tri_a_off = 0;
		int tri_b_off = 0;
		int tri_c_off = 0;
		int off = 0;
		for (int i = 0; i < triangleCount; i++) {
			int xOff = offsets2.getUnsignedByte();
			if (xOff == 1) {
				tri_a_off = offsets1.getUnsignedSmart() + off;
				off = tri_a_off;
				tri_b_off = offsets1.getUnsignedSmart() + off;
				off = tri_b_off;
				tri_c_off = offsets1.getUnsignedSmart() + off;
				off = tri_c_off;
				facePointA[i] = tri_a_off;
				facePointB[i] = tri_b_off;
				facePointC[i] = tri_c_off;
			}
			if (xOff == 2) {
				tri_b_off = tri_c_off;
				tri_c_off = offsets1.getUnsignedSmart() + off;
				off = tri_c_off;
				facePointA[i] = tri_a_off;
				facePointB[i] = tri_b_off;
				facePointC[i] = tri_c_off;
			}
			if (xOff == 3) {
				tri_a_off = tri_c_off;
				tri_c_off = offsets1.getUnsignedSmart() + off;
				off = tri_c_off;
				facePointA[i] = tri_a_off;
				facePointB[i] = tri_b_off;
				facePointC[i] = tri_c_off;
			}
			if (xOff == 4) {
				int a_off = tri_a_off;
				tri_a_off = tri_b_off;
				tri_b_off = a_off;
				tri_c_off = offsets1.getUnsignedSmart() + off;
				off = tri_c_off;
				facePointA[i] = tri_a_off;
				facePointB[i] = tri_b_off;
				facePointC[i] = tri_c_off;
			}
		}
		offsets1.offset = modelHeader.triangleTextureOffset;
		for (int i = 0; i < texturedTriangleCount; i++) {
			trianglePIndex[i] = offsets1.getUnsignedShort();
			triangleMIndex[i] = offsets1.getUnsignedShort();
			triangleNIndex[i] = offsets1.getUnsignedShort();
		}
	}

	public static void formatTriangles(byte data[], int id) {
		if (data == null) {
			ModelHeader modelHeader = Model.modelHeaderCache[id] = new ModelHeader();
			modelHeader.vertexCount = 0;
			modelHeader.triangleCount = 0;
			modelHeader.texturedTriangleCount = 0;
			return;
		}
		Stream buffer = new Stream(data);
		buffer.offset = data.length - 18;
		ModelHeader header = Model.modelHeaderCache[id] = new ModelHeader();
		header.buffer = data;
		header.vertexCount = buffer.getUnsignedShort();
		header.triangleCount = buffer.getUnsignedShort();
		header.texturedTriangleCount = buffer.getUnsignedByte();
		int faceOff = buffer.getUnsignedByte();
		int priOff = buffer.getUnsignedByte();
		int transOff = buffer.getUnsignedByte();
		int skinOff = buffer.getUnsignedByte();
		int vSkinOff = buffer.getUnsignedByte();
		int vertXOff = buffer.getUnsignedShort();
		int vertYOff = buffer.getUnsignedShort();
		int vertZOff = buffer.getUnsignedShort();
		int colorOff = buffer.getUnsignedShort();
		int off = 0;
		header.vertexModOffset = off;
		off += header.vertexCount;
		header.triangleMeshLinkOffset = off;
		off += header.triangleCount;
		header.trianglePriorityOffset = off;
		if (priOff == 255) {
			off += header.triangleCount;
		} else {
			header.trianglePriorityOffset = -priOff - 1;
		}
		header.triangleSkinningOffset = off;
		if (skinOff == 1) {
			off += header.triangleCount;
		} else {
			header.triangleSkinningOffset = -1;
		}
		header.triangleFaceTypeOffset = off;
		if (faceOff == 1) {
			off += header.triangleCount;
		} else {
			header.triangleFaceTypeOffset = -1;
		}
		header.vertexSkinOffset = off;
		if (vSkinOff == 1) {
			off += header.vertexCount;
		} else {
			header.vertexSkinOffset = -1;
		}
		header.triangleTransparencyOffset = off;
		if (transOff == 1) {
			off += header.triangleCount;
		} else {
			header.triangleTransparencyOffset = -1;
		}
		header.triangleVertexPointOffset = off;
		off += colorOff;
		header.triangleColorOffset = off;
		off += header.triangleCount * 2;
		header.triangleTextureOffset = off;
		off += header.texturedTriangleCount * 6;
		header.vertexOffsetX = off;
		off += vertXOff;
		header.vertexOffsetY = off;
		off += vertYOff;
		header.vertexOffsetZ = off;
		off += vertZOff;
	}

	public static void allocate(int size, FileRequester fileRequester) {
		try {
			Model.modelHeaderCache = new ModelHeader[size];
			Model.fileRequester = fileRequester;
		} catch (ArrayIndexOutOfBoundsException arrayoutofbounds) {
			Model.modelHeaderCache = new ModelHeader[size + Short.MAX_VALUE];
			Model.fileRequester = fileRequester;
		}
	}

	public static void resetModel(int id) {
		Model.modelHeaderCache[id] = null;
	}

	public static Model getModel(int id) {
		if (Model.modelHeaderCache == null) {
			return null;
		}
		ModelHeader modelHeader = Model.modelHeaderCache[id];
		if (modelHeader == null) {
			Model.fileRequester.get(id);
			return null;
		} else {
			return new Model(id);
		}
	}

	public static boolean isCached(int id) {
		if (Model.modelHeaderCache == null) {
			return false;
		}
		ModelHeader modelHeader = Model.modelHeaderCache[id];
		if (modelHeader == null) {
			Model.fileRequester.get(id);
			return false;
		} else {
			return true;
		}
	}

	private Model(boolean flag) {
		aBoolean1618 = true;
		aBoolean1659 = false;
		if (!flag) {
			aBoolean1618 = !aBoolean1618;
		}
	}

	public Model(int amount, Model models[]) {
		aBoolean1618 = true;
		aBoolean1659 = false;
		Model.dummyCounter++;
		boolean flag = false;
		boolean flag1 = false;
		boolean flag2 = false;
		boolean flag3 = false;
		vertexCount = 0;
		triangleCount = 0;
		texturedTriangleCount = 0;
		priority = -1;
		for (int k = 0; k < amount; k++) {
			Model model = models[k];
			if (model != null) {
				vertexCount += model.vertexCount;
				triangleCount += model.triangleCount;
				texturedTriangleCount += model.texturedTriangleCount;
				flag |= model.faceTypes != null;
				if (model.facePriority != null) {
					flag1 = true;
				} else {
					if (priority == -1) {
						priority = model.priority;
					}
					if (priority != model.priority) {
						flag1 = true;
					}
				}
				flag2 |= model.transparency != null;
				flag3 |= model.tSkin != null;
			}
		}
		xVertex = new int[vertexCount];
		yVertex = new int[vertexCount];
		zVertex = new int[vertexCount];
		vertexVSkins = new int[vertexCount];
		facePointA = new int[triangleCount];
		facePointB = new int[triangleCount];
		facePointC = new int[triangleCount];
		trianglePIndex = new int[texturedTriangleCount];
		triangleMIndex = new int[texturedTriangleCount];
		triangleNIndex = new int[texturedTriangleCount];
		if (flag) {
			faceTypes = new int[triangleCount];
		}
		if (flag1) {
			facePriority = new int[triangleCount];
		}
		if (flag2) {
			transparency = new int[triangleCount];
		}
		if (flag3) {
			tSkin = new int[triangleCount];
		}
		triangleColors = new int[triangleCount];
		vertexCount = 0;
		triangleCount = 0;
		texturedTriangleCount = 0;
		int l = 0;
		for (int i1 = 0; i1 < amount; i1++) {
			Model model = models[i1];
			if (model != null) {
				for (int j1 = 0; j1 < model.triangleCount; j1++) {
					if (flag) {
						if (model.faceTypes == null) {
							faceTypes[triangleCount] = 0;
						} else {
							int k1 = model.faceTypes[j1];
							if ((k1 & 2) == 2) {
								k1 += l << 2;
							}
							faceTypes[triangleCount] = k1;
						}
					}
					if (flag1) {
						if (model.facePriority == null) {
							facePriority[triangleCount] = model.priority;
						} else {
							facePriority[triangleCount] = model.facePriority[j1];
						}
					}
					if (flag2) {
						if (model.transparency == null) {
							transparency[triangleCount] = 0;
						} else {
							transparency[triangleCount] = model.transparency[j1];
						}
					}
					if (flag3 && model.tSkin != null) {
						tSkin[triangleCount] = model.tSkin[j1];
					}
					triangleColors[triangleCount] = model.triangleColors[j1];
					facePointA[triangleCount] = method465(model, model.facePointA[j1]);
					facePointB[triangleCount] = method465(model, model.facePointB[j1]);
					facePointC[triangleCount] = method465(model, model.facePointC[j1]);
					triangleCount++;
				}
				for (int l1 = 0; l1 < model.texturedTriangleCount; l1++) {
					trianglePIndex[texturedTriangleCount] = method465(model, model.trianglePIndex[l1]);
					triangleMIndex[texturedTriangleCount] = method465(model, model.triangleMIndex[l1]);
					triangleNIndex[texturedTriangleCount] = method465(model, model.triangleNIndex[l1]);
					texturedTriangleCount++;
				}
				l += model.texturedTriangleCount;
			}
		}
	}

	public Model(Model amodel[]) {
		int i = 2;
		aBoolean1618 = true;
		aBoolean1659 = false;
		Model.dummyCounter++;
		boolean flag1 = false;
		boolean flag2 = false;
		boolean flag3 = false;
		boolean flag4 = false;
		vertexCount = 0;
		triangleCount = 0;
		texturedTriangleCount = 0;
		priority = -1;
		for (int k = 0; k < i; k++) {
			Model model = amodel[k];
			if (model != null) {
				vertexCount += model.vertexCount;
				triangleCount += model.triangleCount;
				texturedTriangleCount += model.texturedTriangleCount;
				flag1 |= model.faceTypes != null;
				if (model.facePriority != null) {
					flag2 = true;
				} else {
					if (priority == -1) {
						priority = model.priority;
					}
					if (priority != model.priority) {
						flag2 = true;
					}
				}
				flag3 |= model.transparency != null;
				flag4 |= model.triangleColors != null;
			}
		}
		xVertex = new int[vertexCount];
		yVertex = new int[vertexCount];
		zVertex = new int[vertexCount];
		facePointA = new int[triangleCount];
		facePointB = new int[triangleCount];
		facePointC = new int[triangleCount];
		triangleColorsA = new int[triangleCount];
		triangleColorsB = new int[triangleCount];
		triangleColorsC = new int[triangleCount];
		trianglePIndex = new int[texturedTriangleCount];
		triangleMIndex = new int[texturedTriangleCount];
		triangleNIndex = new int[texturedTriangleCount];
		if (flag1) {
			faceTypes = new int[triangleCount];
		}
		if (flag2) {
			facePriority = new int[triangleCount];
		}
		if (flag3) {
			transparency = new int[triangleCount];
		}
		if (flag4) {
			triangleColors = new int[triangleCount];
		}
		vertexCount = 0;
		triangleCount = 0;
		texturedTriangleCount = 0;
		int i1 = 0;
		for (int j1 = 0; j1 < i; j1++) {
			Model model_1 = amodel[j1];
			if (model_1 != null) {
				int k1 = vertexCount;
				for (int l1 = 0; l1 < model_1.vertexCount; l1++) {
					xVertex[vertexCount] = model_1.xVertex[l1];
					yVertex[vertexCount] = model_1.yVertex[l1];
					zVertex[vertexCount] = model_1.zVertex[l1];
					vertexCount++;
				}
				for (int i2 = 0; i2 < model_1.triangleCount; i2++) {
					facePointA[triangleCount] = model_1.facePointA[i2] + k1;
					facePointB[triangleCount] = model_1.facePointB[i2] + k1;
					facePointC[triangleCount] = model_1.facePointC[i2] + k1;
					triangleColorsA[triangleCount] = model_1.triangleColorsA[i2];
					triangleColorsB[triangleCount] = model_1.triangleColorsB[i2];
					triangleColorsC[triangleCount] = model_1.triangleColorsC[i2];
					if (flag1) {
						if (model_1.faceTypes == null) {
							faceTypes[triangleCount] = 0;
						} else {
							int j2 = model_1.faceTypes[i2];
							if ((j2 & 2) == 2) {
								j2 += i1 << 2;
							}
							faceTypes[triangleCount] = j2;
						}
					}
					if (flag2) {
						if (model_1.facePriority == null) {
							facePriority[triangleCount] = model_1.priority;
						} else {
							facePriority[triangleCount] = model_1.facePriority[i2];
						}
					}
					if (flag3) {
						if (model_1.transparency == null) {
							transparency[triangleCount] = 0;
						} else {
							transparency[triangleCount] = model_1.transparency[i2];
						}
					}
					if (flag4 && model_1.triangleColors != null) {
						triangleColors[triangleCount] = model_1.triangleColors[i2];
					}
					triangleCount++;
				}
				for (int k2 = 0; k2 < model_1.texturedTriangleCount; k2++) {
					trianglePIndex[texturedTriangleCount] = model_1.trianglePIndex[k2] + k1;
					triangleMIndex[texturedTriangleCount] = model_1.triangleMIndex[k2] + k1;
					triangleNIndex[texturedTriangleCount] = model_1.triangleNIndex[k2] + k1;
					texturedTriangleCount++;
				}
				i1 += model_1.texturedTriangleCount;
			}
		}
		getDefaultDiagonals();
	}

	public Model(boolean flag, boolean flag1, boolean flag2, Model model) {
		aBoolean1618 = true;
		aBoolean1659 = false;
		Model.dummyCounter++;
		vertexCount = model.vertexCount;
		triangleCount = model.triangleCount;
		texturedTriangleCount = model.texturedTriangleCount;
		if (flag2) {
			xVertex = model.xVertex;
			yVertex = model.yVertex;
			zVertex = model.zVertex;
		} else {
			xVertex = new int[vertexCount];
			yVertex = new int[vertexCount];
			zVertex = new int[vertexCount];
			for (int j = 0; j < vertexCount; j++) {
				xVertex[j] = model.xVertex[j];
				yVertex[j] = model.yVertex[j];
				zVertex[j] = model.zVertex[j];
			}
		}
		if (flag) {
			triangleColors = model.triangleColors;
		} else {
			triangleColors = new int[triangleCount];
			for (int k = 0; k < triangleCount; k++) {
				triangleColors[k] = model.triangleColors[k];
			}
		}
		if (flag1) {
			transparency = model.transparency;
		} else {
			transparency = new int[triangleCount];
			if (model.transparency == null) {
				for (int l = 0; l < triangleCount; l++) {
					transparency[l] = 0;
				}
			} else {
				for (int i1 = 0; i1 < triangleCount; i1++) {
					transparency[i1] = model.transparency[i1];
				}
			}
		}
		vertexVSkins = model.vertexVSkins;
		tSkin = model.tSkin;
		faceTypes = model.faceTypes;
		facePointA = model.facePointA;
		facePointB = model.facePointB;
		facePointC = model.facePointC;
		facePriority = model.facePriority;
		priority = model.priority;
		trianglePIndex = model.trianglePIndex;
		triangleMIndex = model.triangleMIndex;
		triangleNIndex = model.triangleNIndex;
	}

	public Model(boolean flag, boolean flag1, Model model) {
		aBoolean1618 = true;
		aBoolean1659 = false;
		Model.dummyCounter++;
		vertexCount = model.vertexCount;
		triangleCount = model.triangleCount;
		texturedTriangleCount = model.texturedTriangleCount;
		if (flag) {
			yVertex = new int[vertexCount];
			for (int j = 0; j < vertexCount; j++) {
				yVertex[j] = model.yVertex[j];
			}
		} else {
			yVertex = model.yVertex;
		}
		if (flag1) {
			triangleColorsA = new int[triangleCount];
			triangleColorsB = new int[triangleCount];
			triangleColorsC = new int[triangleCount];
			for (int k = 0; k < triangleCount; k++) {
				triangleColorsA[k] = model.triangleColorsA[k];
				triangleColorsB[k] = model.triangleColorsB[k];
				triangleColorsC[k] = model.triangleColorsC[k];
			}
			faceTypes = new int[triangleCount];
			if (model.faceTypes == null) {
				for (int l = 0; l < triangleCount; l++) {
					faceTypes[l] = 0;
				}
			} else {
				for (int i1 = 0; i1 < triangleCount; i1++) {
					faceTypes[i1] = model.faceTypes[i1];
				}
			}
			super.vertices = new VertexNormal[vertexCount];
			for (int j1 = 0; j1 < vertexCount; j1++) {
				VertexNormal vertexNormal = super.vertices[j1] = new VertexNormal();
				VertexNormal vertexNormal1 = model.vertices[j1];
				vertexNormal.x = vertexNormal1.x;
				vertexNormal.y = vertexNormal1.y;
				vertexNormal.z = vertexNormal1.z;
				vertexNormal.magnitude = vertexNormal1.magnitude;
			}
			vertices = model.vertices;
		} else {
			triangleColorsA = model.triangleColorsA;
			triangleColorsB = model.triangleColorsB;
			triangleColorsC = model.triangleColorsC;
			faceTypes = model.faceTypes;
		}
		xVertex = model.xVertex;
		zVertex = model.zVertex;
		triangleColors = model.triangleColors;
		transparency = model.transparency;
		facePriority = model.facePriority;
		priority = model.priority;
		facePointA = model.facePointA;
		facePointB = model.facePointB;
		facePointC = model.facePointC;
		trianglePIndex = model.trianglePIndex;
		triangleMIndex = model.triangleMIndex;
		triangleNIndex = model.triangleNIndex;
		super.height = model.height;
		anInt1650 = model.anInt1650;
		anInt1653 = model.anInt1653;
		anInt1652 = model.anInt1652;
		anInt1646 = model.anInt1646;
		anInt1648 = model.anInt1648;
		anInt1649 = model.anInt1649;
		anInt1647 = model.anInt1647;
	}

	public void setTransparency(Model model, boolean transparent) {
		vertexCount = model.vertexCount;
		triangleCount = model.triangleCount;
		texturedTriangleCount = model.texturedTriangleCount;
		if (Model.anIntArray1622.length < vertexCount) {
			Model.anIntArray1622 = new int[vertexCount + 10000];
			Model.anIntArray1623 = new int[vertexCount + 10000];
			Model.anIntArray1624 = new int[vertexCount + 10000];
		}
		xVertex = Model.anIntArray1622;
		yVertex = Model.anIntArray1623;
		zVertex = Model.anIntArray1624;
		for (int k = 0; k < vertexCount; k++) {
			xVertex[k] = model.xVertex[k];
			yVertex[k] = model.yVertex[k];
			zVertex[k] = model.zVertex[k];
		}
		if (transparent) {
			transparency = model.transparency;
		} else {
			if (Model.anIntArray1625.length < triangleCount) {
				Model.anIntArray1625 = new int[triangleCount + 100];
			}
			transparency = Model.anIntArray1625;
			if (model.transparency == null) {
				for (int i = 0; i < triangleCount; i++) {
					transparency[i] = 0;
				}
			} else {
				for (int i = 0; i < triangleCount; i++) {
					transparency[i] = model.transparency[i];
				}
			}
		}
		faceTypes = model.faceTypes;
		triangleColors = model.triangleColors;
		facePriority = model.facePriority;
		priority = model.priority;
		triangleSkin = model.triangleSkin;
		vertexSkins = model.vertexSkins;
		facePointA = model.facePointA;
		facePointB = model.facePointB;
		facePointC = model.facePointC;
		triangleColorsA = model.triangleColorsA;
		triangleColorsB = model.triangleColorsB;
		triangleColorsC = model.triangleColorsC;
		trianglePIndex = model.trianglePIndex;
		triangleMIndex = model.triangleMIndex;
		triangleNIndex = model.triangleNIndex;
	}

	private final int method465(Model model, int id) {
		int dest = -1;
		int x = model.xVertex[id];
		int y = model.yVertex[id];
		int z = model.zVertex[id];
		for (int i = 0; i < vertexCount; i++) {
			if (x != xVertex[i] || y != yVertex[i] || z != zVertex[i]) {
				continue;
			}
			dest = i;
			break;
		}
		if (dest == -1) {
			xVertex[vertexCount] = x;
			yVertex[vertexCount] = y;
			zVertex[vertexCount] = z;
			if (model.vertexVSkins != null) {
				vertexVSkins[vertexCount] = model.vertexVSkins[id];
			}
			dest = vertexCount++;
		}
		return dest;
	}

	public void getDefaultDiagonals() {
		super.height = 0;
		anInt1650 = 0;
		trimHeight = 0;
		for (int i = 0; i < vertexCount; i++) {
			int x = xVertex[i];
			int y = yVertex[i];
			int z = zVertex[i];
			if (-y > super.height) {
				super.height = -y;
			}
			if (y > trimHeight) {
				trimHeight = y;
			}
			int i1 = x * x + z * z;
			if (i1 > anInt1650) {
				anInt1650 = i1;
			}
		}
		anInt1650 = (int) (Math.sqrt(anInt1650) + 0.98999999999999999D);
		anInt1653 = (int) (Math.sqrt(anInt1650 * anInt1650 + super.height * super.height) + 0.98999999999999999D);
		anInt1652 = anInt1653 + (int) (Math.sqrt(anInt1650 * anInt1650 + trimHeight * trimHeight) + 0.98999999999999999D);
	}

	public void method467() {
		super.height = 0;
		trimHeight = 0;
		for (int i = 0; i < vertexCount; i++) {
			int j = yVertex[i];
			if (-j > super.height) {
				super.height = -j;
			}
			if (j > trimHeight) {
				trimHeight = j;
			}
		}
		anInt1653 = (int) (Math.sqrt(anInt1650 * anInt1650 + super.height * super.height) + 0.98999999999999999D);
		anInt1652 = anInt1653 + (int) (Math.sqrt(anInt1650 * anInt1650 + trimHeight * trimHeight) + 0.98999999999999999D);
	}

	public void getDiagonals() {
		super.height = 0;
		anInt1650 = 0;
		trimHeight = 0;
		anInt1646 = 0xf423f;
		anInt1647 = 0xfff0bdc1;
		anInt1648 = 0xfffe7961;
		anInt1649 = 0x1869f;
		for (int j = 0; j < vertexCount; j++) {
			int k = xVertex[j];
			int l = yVertex[j];
			int i1 = zVertex[j];
			if (k < anInt1646) {
				anInt1646 = k;
			}
			if (k > anInt1647) {
				anInt1647 = k;
			}
			if (i1 < anInt1649) {
				anInt1649 = i1;
			}
			if (i1 > anInt1648) {
				anInt1648 = i1;
			}
			if (-l > super.height) {
				super.height = -l;
			}
			if (l > trimHeight) {
				trimHeight = l;
			}
			int j1 = k * k + i1 * i1;
			if (j1 > anInt1650) {
				anInt1650 = j1;
			}
		}
		anInt1650 = (int) Math.sqrt(anInt1650);
		anInt1653 = (int) Math.sqrt(anInt1650 * anInt1650 + super.height * super.height);
		anInt1652 = anInt1653 + (int) Math.sqrt(anInt1650 * anInt1650 + trimHeight * trimHeight);
		return;
	}

	public void skin() {
		if (vertexVSkins != null) {
			int ai[] = new int[256];
			int j = 0;
			for (int l = 0; l < vertexCount; l++) {
				int j1 = vertexVSkins[l];
				ai[j1]++;
				if (j1 > j) {
					j = j1;
				}
			}
			vertexSkins = new int[j + 1][];
			for (int k1 = 0; k1 <= j; k1++) {
				vertexSkins[k1] = new int[ai[k1]];
				ai[k1] = 0;
			}
			for (int j2 = 0; j2 < vertexCount; j2++) {
				int l2 = vertexVSkins[j2];
				vertexSkins[l2][ai[l2]++] = j2;
			}
			vertexVSkins = null;
		}
		if (tSkin != null) {
			int ai1[] = new int[256];
			int k = 0;
			for (int i1 = 0; i1 < triangleCount; i1++) {
				int l1 = tSkin[i1];
				ai1[l1]++;
				if (l1 > k) {
					k = l1;
				}
			}
			triangleSkin = new int[k + 1][];
			for (int i2 = 0; i2 <= k; i2++) {
				triangleSkin[i2] = new int[ai1[i2]];
				ai1[i2] = 0;
			}
			for (int k2 = 0; k2 < triangleCount; k2++) {
				int i3 = tSkin[k2];
				triangleSkin[i3][ai1[i3]++] = k2;
			}
			tSkin = null;
		}
	}

	public void transform(int id) {
		if (vertexSkins == null) {
			return;
		}
		if (id == -1) {
			return;
		}
		Animation animation = Animation.forId(id);
		if (animation == null) {
			return;
		}
		SkinList skinList = animation.aSkinList_637;
		Model.vertexModX = 0;
		Model.vertexModY = 0;
		Model.vertexModZ = 0;
		for (int k = 0; k < animation.anInt638; k++) {
			int opcode = animation.opcodes[k];
			transformStep(skinList.opcodes[opcode], skinList.anIntArrayArray343[opcode], animation.mod1[k], animation.mod2[k], animation.mod3[k]);
		}
	}

	public void pushFrames(int arg0[], int arg1, int arg2) {
		if (arg2 == -1) {
			return;
		}
		if (arg0 == null || arg1 == -1) {
			transform(arg2);
			return;
		}
		Animation animation = Animation.forId(arg2);
		if (animation == null) {
			return;
		}
		Animation animation_1 = Animation.forId(arg1);
		if (animation_1 == null) {
			transform(arg2);
			return;
		}
		SkinList skinList = animation.aSkinList_637;
		Model.vertexModX = 0;
		Model.vertexModY = 0;
		Model.vertexModZ = 0;
		int l = 0;
		int i1 = arg0[l++];
		for (int j1 = 0; j1 < animation.anInt638; j1++) {
			int k1;
			for (k1 = animation.opcodes[j1]; k1 > i1; i1 = arg0[l++]) {
				;
			}
			if (k1 != i1 || skinList.opcodes[k1] == 0) {
				transformStep(skinList.opcodes[k1], skinList.anIntArrayArray343[k1], animation.mod1[j1], animation.mod2[j1], animation.mod3[j1]);
			}
		}
		Model.vertexModX = 0;
		Model.vertexModY = 0;
		Model.vertexModZ = 0;
		l = 0;
		i1 = arg0[l++];
		for (int l1 = 0; l1 < animation_1.anInt638; l1++) {
			int i2;
			for (i2 = animation_1.opcodes[l1]; i2 > i1; i1 = arg0[l++]) {
				;
			}
			if (i2 == i1 || skinList.opcodes[i2] == 0) {
				transformStep(skinList.opcodes[i2], skinList.anIntArrayArray343[i2], animation_1.mod1[l1], animation_1.mod2[l1], animation_1.mod3[l1]);
			}
		}
	}

	private void transformStep(int opcode, int skins[], int vert_x_off, int vert_y_off, int vert_z_off) {
		int skinCount = skins.length;
		if (opcode == 0) {
			int modOff = 0;
			Model.vertexModX = 0;
			Model.vertexModY = 0;
			Model.vertexModZ = 0;
			for (int k2 = 0; k2 < skinCount; k2++) {
				int vSkin = skins[k2];
				if (vSkin < vertexSkins.length) {
					int ai5[] = vertexSkins[vSkin];
					for (int j6 : ai5) {
						Model.vertexModX += xVertex[j6];
						Model.vertexModY += yVertex[j6];
						Model.vertexModZ += zVertex[j6];
						modOff++;
					}
				}
			}
			if (modOff > 0) {
				Model.vertexModX = Model.vertexModX / modOff + vert_x_off;
				Model.vertexModY = Model.vertexModY / modOff + vert_y_off;
				Model.vertexModZ = Model.vertexModZ / modOff + vert_z_off;
				return;
			} else {
				Model.vertexModX = vert_x_off;
				Model.vertexModY = vert_y_off;
				Model.vertexModZ = vert_z_off;
				return;
			}
		}
		if (opcode == 1) {
			for (int k1 = 0; k1 < skinCount; k1++) {
				int l2 = skins[k1];
				if (l2 < vertexSkins.length) {
					int ai1[] = vertexSkins[l2];
					for (int element : ai1) {
						int j5 = element;
						xVertex[j5] += vert_x_off;
						yVertex[j5] += vert_y_off;
						zVertex[j5] += vert_z_off;
					}
				}
			}
			return;
		}
		if (opcode == 2) {
			for (int l1 = 0; l1 < skinCount; l1++) {
				int i3 = skins[l1];
				if (i3 < vertexSkins.length) {
					int ai2[] = vertexSkins[i3];
					for (int element : ai2) {
						int k5 = element;
						xVertex[k5] -= Model.vertexModX;
						yVertex[k5] -= Model.vertexModY;
						zVertex[k5] -= Model.vertexModZ;
						int k6 = (vert_x_off & 0xff) * 8;
						int l6 = (vert_y_off & 0xff) * 8;
						int i7 = (vert_z_off & 0xff) * 8;
						if (i7 != 0) {
							int j7 = Model.modelIntArray1[i7];
							int i8 = Model.modelIntArray2[i7];
							int l8 = yVertex[k5] * j7 + xVertex[k5] * i8 >> 16;
							yVertex[k5] = yVertex[k5] * i8 - xVertex[k5] * j7 >> 16;
							xVertex[k5] = l8;
						}
						if (k6 != 0) {
							int k7 = Model.modelIntArray1[k6];
							int j8 = Model.modelIntArray2[k6];
							int i9 = yVertex[k5] * j8 - zVertex[k5] * k7 >> 16;
							zVertex[k5] = yVertex[k5] * k7 + zVertex[k5] * j8 >> 16;
							yVertex[k5] = i9;
						}
						if (l6 != 0) {
							int l7 = Model.modelIntArray1[l6];
							int k8 = Model.modelIntArray2[l6];
							int j9 = zVertex[k5] * l7 + xVertex[k5] * k8 >> 16;
							zVertex[k5] = zVertex[k5] * k8 - xVertex[k5] * l7 >> 16;
							xVertex[k5] = j9;
						}
						xVertex[k5] += Model.vertexModX;
						yVertex[k5] += Model.vertexModY;
						zVertex[k5] += Model.vertexModZ;
					}
				}
			}
			return;
		}
		if (opcode == 3) {
			for (int i2 = 0; i2 < skinCount; i2++) {
				int j3 = skins[i2];
				if (j3 < vertexSkins.length) {
					int ai3[] = vertexSkins[j3];
					for (int element : ai3) {
						int l5 = element;
						xVertex[l5] -= Model.vertexModX;
						yVertex[l5] -= Model.vertexModY;
						zVertex[l5] -= Model.vertexModZ;
						xVertex[l5] = xVertex[l5] * vert_x_off / 128;
						yVertex[l5] = yVertex[l5] * vert_y_off / 128;
						zVertex[l5] = zVertex[l5] * vert_z_off / 128;
						xVertex[l5] += Model.vertexModX;
						yVertex[l5] += Model.vertexModY;
						zVertex[l5] += Model.vertexModZ;
					}
				}
			}
			return;
		}
		if (opcode == 5 && triangleSkin != null && transparency != null) {
			for (int j2 = 0; j2 < skinCount; j2++) {
				int k3 = skins[j2];
				if (k3 < triangleSkin.length) {
					int ai4[] = triangleSkin[k3];
					for (int element : ai4) {
						int i6 = element;
						transparency[i6] += vert_x_off * 8;
						if (transparency[i6] < 0) {
							transparency[i6] = 0;
						}
						if (transparency[i6] > 255) {
							transparency[i6] = 255;
						}
					}
				}
			}
		}
	}

	public void method473() {
		for (int j = 0; j < vertexCount; j++) {
			int k = xVertex[j];
			xVertex[j] = zVertex[j];
			zVertex[j] = -k;
		}
	}

	public void method474(int i) {
		int k = Model.modelIntArray1[i];
		int l = Model.modelIntArray2[i];
		for (int i1 = 0; i1 < vertexCount; i1++) {
			int j1 = yVertex[i1] * l - zVertex[i1] * k >> 16;
			zVertex[i1] = yVertex[i1] * k + zVertex[i1] * l >> 16;
			yVertex[i1] = j1;
		}
	}

	public void translate(int x, int y, int z) {
		for (int i = 0; i < vertexCount; i++) {
			xVertex[i] += x;
			yVertex[i] += y;
			zVertex[i] += z;
		}
	}

	public void swapColors(int src, int dest) {
		for (int i = 0; i < triangleCount; i++) {
			if (triangleColors[i] == src) {
				triangleColors[i] = dest;
			}
		}
	}

	public void method477() {
		for (int j = 0; j < vertexCount; j++) {
			zVertex[j] = -zVertex[j];
		}
		for (int k = 0; k < triangleCount; k++) {
			int l = facePointA[k];
			facePointA[k] = facePointC[k];
			facePointC[k] = l;
		}
	}

	public void scale(int x, int y, int z) {
		for (int i = 0; i < vertexCount; i++) {
			xVertex[i] = xVertex[i] * x / 128;
			yVertex[i] = yVertex[i] * z / 128;
			zVertex[i] = zVertex[i] * y / 128;
		}
	}

	public final void processLighting(int lightness, int shading, int x_lightness, int y_lightness, int z_lightness, boolean flag) {
		int src_shad = (int) Math.sqrt(x_lightness * x_lightness + y_lightness * y_lightness + z_lightness * z_lightness);
		int dest_shad = shading * src_shad >> 8;
		if (triangleColorsA == null) {
			triangleColorsA = new int[triangleCount];
			triangleColorsB = new int[triangleCount];
			triangleColorsC = new int[triangleCount];
		}
		if (super.vertices == null) {
			super.vertices = new VertexNormal[vertexCount];
			for (int i = 0; i < vertexCount; i++) {
				super.vertices[i] = new VertexNormal();
			}
		}
		for (int i = 0; i < triangleCount; i++) {
			if (triangleColors != null) {
				if (triangleColors[i] == 65535) {
					transparency[i] = 255;
				}
			}
			int j2 = facePointA[i];
			int l2 = facePointB[i];
			int i3 = facePointC[i];
			int j3 = xVertex[l2] - xVertex[j2];
			int k3 = yVertex[l2] - yVertex[j2];
			int l3 = zVertex[l2] - zVertex[j2];
			int i4 = xVertex[i3] - xVertex[j2];
			int j4 = yVertex[i3] - yVertex[j2];
			int k4 = zVertex[i3] - zVertex[j2];
			int l4 = k3 * k4 - j4 * l3;
			int i5 = l3 * i4 - k4 * j3;
			int j5;
			for (j5 = j3 * j4 - i4 * k3; l4 > 8192 || i5 > 8192 || j5 > 8192 || l4 < -8192 || i5 < -8192 || j5 < -8192; j5 >>= 1) {
				l4 >>= 1;
				i5 >>= 1;
			}
			int k5 = (int) Math.sqrt(l4 * l4 + i5 * i5 + j5 * j5);
			if (k5 <= 0) {
				k5 = 1;
			}
			l4 = l4 * 256 / k5;
			i5 = i5 * 256 / k5;
			j5 = j5 * 256 / k5;
			if (faceTypes == null || (faceTypes[i] & 1) == 0) {
				VertexNormal vertexNormal1 = super.vertices[j2];
				vertexNormal1.x += l4;
				vertexNormal1.y += i5;
				vertexNormal1.z += j5;
				vertexNormal1.magnitude++;
				vertexNormal1 = super.vertices[l2];
				vertexNormal1.x += l4;
				vertexNormal1.y += i5;
				vertexNormal1.z += j5;
				vertexNormal1.magnitude++;
				vertexNormal1 = super.vertices[i3];
				vertexNormal1.x += l4;
				vertexNormal1.y += i5;
				vertexNormal1.z += j5;
				vertexNormal1.magnitude++;
			} else {
				int l5 = lightness + (x_lightness * l4 + y_lightness * i5 + z_lightness * j5) / (dest_shad + dest_shad / 2);
				triangleColorsA[i] = Model.getShadedColor(triangleColors[i], l5, faceTypes[i]);
			}
		}
		if (flag) {
			shade(lightness, dest_shad, x_lightness, y_lightness, z_lightness);
			getDefaultDiagonals();
			return;
		} else {
			vertices = new VertexNormal[vertexCount];
			for (int k2 = 0; k2 < vertexCount; k2++) {
				VertexNormal src = super.vertices[k2];
				VertexNormal dest = vertices[k2] = new VertexNormal();
				dest.x = src.x;
				dest.y = src.y;
				dest.z = src.z;
				dest.magnitude = src.magnitude;
			}
			getDiagonals();
			return;
		}
	}

	public final void shade(int intensity, int fallOffset, int x_lightness, int y_lightness, int z_lightness) {
		for (int i = 0; i < triangleCount; i++) {
			int k1 = facePointA[i];
			int i2 = facePointB[i];
			int j2 = facePointC[i];
			if (faceTypes == null) {
				int i3 = triangleColors[i];
				VertexNormal vertexNormal = super.vertices[k1];
				int k2 = intensity + (x_lightness * vertexNormal.x + y_lightness * vertexNormal.y + z_lightness * vertexNormal.z) / (fallOffset * vertexNormal.magnitude);
				triangleColorsA[i] = Model.getShadedColor(i3, k2, 0);
				vertexNormal = super.vertices[i2];
				k2 = intensity + (x_lightness * vertexNormal.x + y_lightness * vertexNormal.y + z_lightness * vertexNormal.z) / (fallOffset * vertexNormal.magnitude);
				triangleColorsB[i] = Model.getShadedColor(i3, k2, 0);
				vertexNormal = super.vertices[j2];
				k2 = intensity + (x_lightness * vertexNormal.x + y_lightness * vertexNormal.y + z_lightness * vertexNormal.z) / (fallOffset * vertexNormal.magnitude);
				triangleColorsC[i] = Model.getShadedColor(i3, k2, 0);
			} else if ((faceTypes[i] & 1) == 0) {
				int j3 = triangleColors[i];
				int k3 = faceTypes[i];
				VertexNormal vertexNormal1 = super.vertices[k1];
				int l2 = intensity + (x_lightness * vertexNormal1.x + y_lightness * vertexNormal1.y + z_lightness * vertexNormal1.z) / (fallOffset * vertexNormal1.magnitude);
				triangleColorsA[i] = Model.getShadedColor(j3, l2, k3);
				vertexNormal1 = super.vertices[i2];
				l2 = intensity + (x_lightness * vertexNormal1.x + y_lightness * vertexNormal1.y + z_lightness * vertexNormal1.z) / (fallOffset * vertexNormal1.magnitude);
				triangleColorsB[i] = Model.getShadedColor(j3, l2, k3);
				vertexNormal1 = super.vertices[j2];
				l2 = intensity + (x_lightness * vertexNormal1.x + y_lightness * vertexNormal1.y + z_lightness * vertexNormal1.z) / (fallOffset * vertexNormal1.magnitude);
				triangleColorsC[i] = Model.getShadedColor(j3, l2, k3);
			}
		}
		super.vertices = null;
		vertices = null;
		vertexVSkins = null;
		tSkin = null;
		if (faceTypes != null) {
			for (int l1 = 0; l1 < triangleCount; l1++) {
				if ((faceTypes[l1] & 2) == 2) {
					return;
				}
			}
		}
		triangleColors = null;
	}

	public static final int getShadedColor(int color, int lightness, int flag) {
		if (color == 65535) {
			return 0;
		}
		if ((flag & 2) == 2) {
			if (lightness < 0) {
				lightness = 0;
			} else if (lightness > 127) {
				lightness = 127;
			}
			lightness = 127 - lightness;
			return lightness;
		}
		lightness = lightness * (color & 0x7f) >> 7;
		if (lightness < 2) {
			lightness = 2;
		} else if (lightness > 126) {
			lightness = 126;
		}
		return (color & 0xff80) + lightness;
	}

	public final void method482(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5) {
		int i = 0;
		int l1 = Rasterizer.centerX;
		int i2 = Rasterizer.centerY;
		int j2 = Model.modelIntArray1[i];
		int k2 = Model.modelIntArray2[i];
		int l2 = Model.modelIntArray1[arg0];
		int i3 = Model.modelIntArray2[arg0];
		int j3 = Model.modelIntArray1[arg1];
		int k3 = Model.modelIntArray2[arg1];
		int l3 = Model.modelIntArray1[arg2];
		int i4 = Model.modelIntArray2[arg2];
		int j4 = arg4 * l3 + arg5 * i4 >> 16;
		for (int k4 = 0; k4 < vertexCount; k4++) {
			int l4 = xVertex[k4];
			int i5 = yVertex[k4];
			int j5 = zVertex[k4];
			if (arg1 != 0) {
				int k5 = i5 * j3 + l4 * k3 >> 16;
				i5 = i5 * k3 - l4 * j3 >> 16;
				l4 = k5;
			}
			if (i != 0) {
				int l5 = i5 * k2 - j5 * j2 >> 16;
				j5 = i5 * j2 + j5 * k2 >> 16;
				i5 = l5;
			}
			if (arg0 != 0) {
				int i6 = j5 * l2 + l4 * i3 >> 16;
				j5 = j5 * i3 - l4 * l2 >> 16;
				l4 = i6;
			}
			l4 += arg3;
			i5 += arg4;
			j5 += arg5;
			int j6 = i5 * i4 - j5 * l3 >> 16;
			j5 = i5 * l3 + j5 * i4 >> 16;
			i5 = j6;
			Model.anIntArray1667[k4] = j5 - j4;
			Model.anIntArray1665[k4] = l1 + (l4 << 9) / j5;
			Model.triangleX[k4] = i2 + (i5 << 9) / j5;
			if (texturedTriangleCount > 0) {
				Model.zoom[k4] = l4;
				Model.anIntArray1669[k4] = i5;
				Model.anIntArray1670[k4] = j5;
			}
		}
		try {
			method483(false, false, 0);
			return;
		} catch (Exception exception) {
			return;
		}
	}

	@Override
	public final void render(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, int arg8) {
		int j2 = arg7 * arg4 - arg5 * arg3 >> 16;
		int k2 = arg6 * arg1 + j2 * arg2 >> 16;
		int l2 = anInt1650 * arg2 >> 16;
		int i3 = k2 + l2;
		if (i3 <= 50 || k2 >= 3500) {
			return;
		}
		int j3 = arg7 * arg3 + arg5 * arg4 >> 16;
		int k3 = j3 - anInt1650 << 9;
		if (k3 / i3 >= Graphics2D.middleX) {
			return;
		}
		int l3 = j3 + anInt1650 << 9;
		if (l3 / i3 <= -Graphics2D.middleX) {
			return;
		}
		int i4 = arg6 * arg2 - j2 * arg1 >> 16;
		int j4 = anInt1650 * arg1 >> 16;
		int k4 = i4 + j4 << 9;
		if (k4 / i3 <= -Graphics2D.middleY) {
			return;
		}
		int l4 = j4 + (super.height * arg2 >> 16);
		int i5 = i4 - l4 << 9;
		if (i5 / i3 >= Graphics2D.middleY) {
			return;
		}
		int j5 = l2 + (super.height * arg1 >> 16);
		boolean flag = false;
		if (k2 - j5 <= 50) {
			flag = true;
		}
		boolean flag1 = false;
		if (arg8 > 0 && Model.aBoolean1684) {
			int k5 = k2 - l2;
			if (k5 <= 50) {
				k5 = 50;
			}
			if (j3 > 0) {
				k3 /= i3;
				l3 /= k5;
			} else {
				l3 /= i3;
				k3 /= k5;
			}
			if (i4 > 0) {
				i5 /= i3;
				k4 /= k5;
			} else {
				k4 /= i3;
				i5 /= k5;
			}
			int i6 = Model.anInt1685 - Rasterizer.centerX;
			int k6 = Model.anInt1686 - Rasterizer.centerY;
			if (i6 > k3 && i6 < l3 && k6 > i5 && k6 < k4) {
				if (aBoolean1659) {
					Model.anIntArray1688[Model.anInt1687++] = arg8;
				} else {
					flag1 = true;
				}
			}
		}
		int l5 = Rasterizer.centerX;
		int j6 = Rasterizer.centerY;
		int l6 = 0;
		int i7 = 0;
		if (arg0 != 0) {
			l6 = Model.modelIntArray1[arg0];
			i7 = Model.modelIntArray2[arg0];
		}
		for (int j7 = 0; j7 < vertexCount; j7++) {
			int k7 = xVertex[j7];
			int l7 = yVertex[j7];
			int i8 = zVertex[j7];
			if (arg0 != 0) {
				int j8 = i8 * l6 + k7 * i7 >> 16;
				i8 = i8 * i7 - k7 * l6 >> 16;
				k7 = j8;
			}
			k7 += arg5;
			l7 += arg6;
			i8 += arg7;
			int k8 = i8 * arg3 + k7 * arg4 >> 16;
			i8 = i8 * arg4 - k7 * arg3 >> 16;
			k7 = k8;
			k8 = l7 * arg2 - i8 * arg1 >> 16;
			i8 = l7 * arg1 + i8 * arg2 >> 16;
			l7 = k8;
			Model.anIntArray1667[j7] = i8 - k2;
			if (i8 >= 50) {
				Model.anIntArray1665[j7] = l5 + (k7 << 9) / i8;
				Model.triangleX[j7] = j6 + (l7 << 9) / i8;
			} else {
				Model.anIntArray1665[j7] = -5000;
				flag = true;
			}
			if (flag || texturedTriangleCount > 0) {
				Model.zoom[j7] = k7;
				Model.anIntArray1669[j7] = l7;
				Model.anIntArray1670[j7] = i8;
			}
		}
		try {
			method483(flag, flag1, arg8);
			return;
		} catch (Exception exception) {
			return;
		}
	}

	private final void method483(boolean flag0, boolean flag1, int arg3) {
		for (int j = 0; j < anInt1652; j++) {
			Model.anIntArray1671[j] = 0;
		}
		for (int k = 0; k < triangleCount; k++) {
			if (faceTypes == null || faceTypes[k] != -1) {
				int l = facePointA[k];
				int k1 = facePointB[k];
				int j2 = facePointC[k];
				int i3 = Model.anIntArray1665[l];
				int l3 = Model.anIntArray1665[k1];
				int k4 = Model.anIntArray1665[j2];
				if (flag0 && (i3 == -5000 || l3 == -5000 || k4 == -5000)) {
					Model.aBooleanArray1664[k] = true;
					int j5 = (Model.anIntArray1667[l] + Model.anIntArray1667[k1] + Model.anIntArray1667[j2]) / 3 + anInt1653;
					Model.anIntArrayArray1672[j5][Model.anIntArray1671[j5]++] = k;
				} else {
					if (flag1 && method486(Model.anInt1685, Model.anInt1686, Model.triangleX[l], Model.triangleX[k1], Model.triangleX[j2], i3, l3, k4)) {
						Model.anIntArray1688[Model.anInt1687++] = arg3;
						flag1 = false;
					}
					if ((i3 - l3) * (Model.triangleX[j2] - Model.triangleX[k1]) - (Model.triangleX[l] - Model.triangleX[k1]) * (k4 - l3) > 0) {
						Model.aBooleanArray1664[k] = false;
						if (i3 < 0 || l3 < 0 || k4 < 0 || i3 > Graphics2D.endX || l3 > Graphics2D.endX || k4 > Graphics2D.endX) {
							Model.edgeRestricted[k] = true;
						} else {
							Model.edgeRestricted[k] = false;
						}
						int k5 = (Model.anIntArray1667[l] + Model.anIntArray1667[k1] + Model.anIntArray1667[j2]) / 3 + anInt1653;
						Model.anIntArrayArray1672[k5][Model.anIntArray1671[k5]++] = k;
					}
				}
			}
		}
		if (facePriority == null) {
			for (int i1 = anInt1652 - 1; i1 >= 0; i1--) {
				int l1 = Model.anIntArray1671[i1];
				if (l1 > 0) {
					int ai[] = Model.anIntArrayArray1672[i1];
					for (int j3 = 0; j3 < l1; j3++) {
						rasterize(ai[j3]);
					}
				}
			}
			return;
		}
		for (int j1 = 0; j1 < 12; j1++) {
			Model.faceLists[j1] = 0;
			Model.anIntArray1677[j1] = 0;
		}
		for (int i2 = anInt1652 - 1; i2 >= 0; i2--) {
			int k2 = Model.anIntArray1671[i2];
			if (k2 > 0) {
				int ai1[] = Model.anIntArrayArray1672[i2];
				for (int i4 = 0; i4 < k2; i4++) {
					int l4 = ai1[i4];
					int l5 = facePriority[l4];
					int j6 = Model.faceLists[l5]++;
					Model.anIntArrayArray1674[l5][j6] = l4;
					if (l5 < 10) {
						Model.anIntArray1677[l5] += i2;
					} else if (l5 == 10) {
						Model.anIntArray1675[j6] = i2;
					} else {
						Model.anIntArray1676[j6] = i2;
					}
				}
			}
		}
		int l2 = 0;
		if (Model.faceLists[1] > 0 || Model.faceLists[2] > 0) {
			l2 = (Model.anIntArray1677[1] + Model.anIntArray1677[2]) / (Model.faceLists[1] + Model.faceLists[2]);
		}
		int k3 = 0;
		if (Model.faceLists[3] > 0 || Model.faceLists[4] > 0) {
			k3 = (Model.anIntArray1677[3] + Model.anIntArray1677[4]) / (Model.faceLists[3] + Model.faceLists[4]);
		}
		int j4 = 0;
		if (Model.faceLists[6] > 0 || Model.faceLists[8] > 0) {
			j4 = (Model.anIntArray1677[6] + Model.anIntArray1677[8]) / (Model.faceLists[6] + Model.faceLists[8]);
		}
		int i6 = 0;
		int k6 = Model.faceLists[10];
		int ai2[] = Model.anIntArrayArray1674[10];
		int ai3[] = Model.anIntArray1675;
		if (i6 == k6) {
			i6 = 0;
			k6 = Model.faceLists[11];
			ai2 = Model.anIntArrayArray1674[11];
			ai3 = Model.anIntArray1676;
		}
		int i5;
		if (i6 < k6) {
			i5 = ai3[i6];
		} else {
			i5 = -1000;
		}
		for (int l6 = 0; l6 < 10; l6++) {
			while (l6 == 0 && i5 > l2) {
				rasterize(ai2[i6++]);
				if (i6 == k6 && ai2 != Model.anIntArrayArray1674[11]) {
					i6 = 0;
					k6 = Model.faceLists[11];
					ai2 = Model.anIntArrayArray1674[11];
					ai3 = Model.anIntArray1676;
				}
				if (i6 < k6) {
					i5 = ai3[i6];
				} else {
					i5 = -1000;
				}
			}
			while (l6 == 3 && i5 > k3) {
				rasterize(ai2[i6++]);
				if (i6 == k6 && ai2 != Model.anIntArrayArray1674[11]) {
					i6 = 0;
					k6 = Model.faceLists[11];
					ai2 = Model.anIntArrayArray1674[11];
					ai3 = Model.anIntArray1676;
				}
				if (i6 < k6) {
					i5 = ai3[i6];
				} else {
					i5 = -1000;
				}
			}
			while (l6 == 5 && i5 > j4) {
				rasterize(ai2[i6++]);
				if (i6 == k6 && ai2 != Model.anIntArrayArray1674[11]) {
					i6 = 0;
					k6 = Model.faceLists[11];
					ai2 = Model.anIntArrayArray1674[11];
					ai3 = Model.anIntArray1676;
				}
				if (i6 < k6) {
					i5 = ai3[i6];
				} else {
					i5 = -1000;
				}
			}
			int i7 = Model.faceLists[l6];
			int ai4[] = Model.anIntArrayArray1674[l6];
			for (int j7 = 0; j7 < i7; j7++) {
				rasterize(ai4[j7]);
			}
		}
		while (i5 != -1000) {
			rasterize(ai2[i6++]);
			if (i6 == k6 && ai2 != Model.anIntArrayArray1674[11]) {
				i6 = 0;
				ai2 = Model.anIntArrayArray1674[11];
				k6 = Model.faceLists[11];
				ai3 = Model.anIntArray1676;
			}
			if (i6 < k6) {
				i5 = ai3[i6];
			} else {
				i5 = -1000;
			}
		}
	}

	private final void rasterize(int arg0) {
		if (Model.aBooleanArray1664[arg0]) {
			method485(arg0);
			return;
		}
		int j = facePointA[arg0];
		int k = facePointB[arg0];
		int l = facePointC[arg0];
		Rasterizer.edgeRestricted = Model.edgeRestricted[arg0];
		if (transparency == null) {
			Rasterizer.alpha = 0;
		} else {
			Rasterizer.alpha = transparency[arg0];
		}
		int i1;
		if (faceTypes == null) {
			i1 = 0;
		} else {
			i1 = faceTypes[arg0] & 3;
		}
		if (i1 == 0) {
			Rasterizer.drawShadedTriangle(Model.triangleX[j], Model.triangleX[k], Model.triangleX[l], Model.anIntArray1665[j], Model.anIntArray1665[k], Model.anIntArray1665[l], triangleColorsA[arg0], triangleColorsB[arg0], triangleColorsC[arg0]);
			return;
		}
		if (i1 == 1) {
			Rasterizer.drawFlatTriangle(Model.triangleX[j], Model.triangleX[k], Model.triangleX[l], Model.anIntArray1665[j], Model.anIntArray1665[k], Model.anIntArray1665[l], Model.modelIntArray3[triangleColorsA[arg0]]);
			return;
		}
		if (i1 == 2) {
			int j1 = faceTypes[arg0] >> 2;
			int l1 = trianglePIndex[j1];
			int j2 = triangleMIndex[j1];
			int l2 = triangleNIndex[j1];
			Rasterizer.drawTexturedTriangle(Model.triangleX[j], Model.triangleX[k], Model.triangleX[l], Model.anIntArray1665[j], Model.anIntArray1665[k], Model.anIntArray1665[l], triangleColorsA[arg0], triangleColorsB[arg0], triangleColorsC[arg0], Model.zoom[l1], Model.zoom[j2], Model.zoom[l2], Model.anIntArray1669[l1], Model.anIntArray1669[j2], Model.anIntArray1669[l2], Model.anIntArray1670[l1], Model.anIntArray1670[j2], Model.anIntArray1670[l2], triangleColors[arg0]);
			return;
		}
		if (i1 == 3) {
			int k1 = faceTypes[arg0] >> 2;
			int i2 = trianglePIndex[k1];
			int k2 = triangleMIndex[k1];
			int i3 = triangleNIndex[k1];
			Rasterizer.drawTexturedTriangle(Model.triangleX[j], Model.triangleX[k], Model.triangleX[l], Model.anIntArray1665[j], Model.anIntArray1665[k], Model.anIntArray1665[l], triangleColorsA[arg0], triangleColorsA[arg0], triangleColorsA[arg0], Model.zoom[i2], Model.zoom[k2], Model.zoom[i3], Model.anIntArray1669[i2], Model.anIntArray1669[k2], Model.anIntArray1669[i3], Model.anIntArray1670[i2], Model.anIntArray1670[k2], Model.anIntArray1670[i3], triangleColors[arg0]);
		}
	}

	private final void method485(int arg0) {
		if (triangleColors != null) {
			if (triangleColors[arg0] == 65535) {
				return;
			}
		}
		int j = Rasterizer.centerX;
		int k = Rasterizer.centerY;
		int l = 0;
		int i1 = facePointA[arg0];
		int j1 = facePointB[arg0];
		int k1 = facePointC[arg0];
		int l1 = Model.anIntArray1670[i1];
		int i2 = Model.anIntArray1670[j1];
		int j2 = Model.anIntArray1670[k1];
		if (l1 >= 50) {
			Model.anIntArray1678[l] = Model.anIntArray1665[i1];
			Model.anIntArray1679[l] = Model.triangleX[i1];
			Model.anIntArray1680[l++] = triangleColorsA[arg0];
		} else {
			int k2 = Model.zoom[i1];
			int k3 = Model.anIntArray1669[i1];
			int k4 = triangleColorsA[arg0];
			if (j2 >= 50) {
				int k5 = (50 - l1) * Model.modelIntArray4[j2 - l1];
				Model.anIntArray1678[l] = j + (k2 + ((Model.zoom[k1] - k2) * k5 >> 16) << 9) / 50;
				Model.anIntArray1679[l] = k + (k3 + ((Model.anIntArray1669[k1] - k3) * k5 >> 16) << 9) / 50;
				Model.anIntArray1680[l++] = k4 + ((triangleColorsC[arg0] - k4) * k5 >> 16);
			}
			if (i2 >= 50) {
				int l5 = (50 - l1) * Model.modelIntArray4[i2 - l1];
				Model.anIntArray1678[l] = j + (k2 + ((Model.zoom[j1] - k2) * l5 >> 16) << 9) / 50;
				Model.anIntArray1679[l] = k + (k3 + ((Model.anIntArray1669[j1] - k3) * l5 >> 16) << 9) / 50;
				Model.anIntArray1680[l++] = k4 + ((triangleColorsB[arg0] - k4) * l5 >> 16);
			}
		}
		if (i2 >= 50) {
			Model.anIntArray1678[l] = Model.anIntArray1665[j1];
			Model.anIntArray1679[l] = Model.triangleX[j1];
			Model.anIntArray1680[l++] = triangleColorsB[arg0];
		} else {
			int l2 = Model.zoom[j1];
			int l3 = Model.anIntArray1669[j1];
			int l4 = triangleColorsB[arg0];
			if (l1 >= 50) {
				int i6 = (50 - i2) * Model.modelIntArray4[l1 - i2];
				Model.anIntArray1678[l] = j + (l2 + ((Model.zoom[i1] - l2) * i6 >> 16) << 9) / 50;
				Model.anIntArray1679[l] = k + (l3 + ((Model.anIntArray1669[i1] - l3) * i6 >> 16) << 9) / 50;
				Model.anIntArray1680[l++] = l4 + ((triangleColorsA[arg0] - l4) * i6 >> 16);
			}
			if (j2 >= 50) {
				int j6 = (50 - i2) * Model.modelIntArray4[j2 - i2];
				Model.anIntArray1678[l] = j + (l2 + ((Model.zoom[k1] - l2) * j6 >> 16) << 9) / 50;
				Model.anIntArray1679[l] = k + (l3 + ((Model.anIntArray1669[k1] - l3) * j6 >> 16) << 9) / 50;
				Model.anIntArray1680[l++] = l4 + ((triangleColorsC[arg0] - l4) * j6 >> 16);
			}
		}
		if (j2 >= 50) {
			Model.anIntArray1678[l] = Model.anIntArray1665[k1];
			Model.anIntArray1679[l] = Model.triangleX[k1];
			Model.anIntArray1680[l++] = triangleColorsC[arg0];
		} else {
			int i3 = Model.zoom[k1];
			int i4 = Model.anIntArray1669[k1];
			int i5 = triangleColorsC[arg0];
			if (i2 >= 50) {
				int k6 = (50 - j2) * Model.modelIntArray4[i2 - j2];
				Model.anIntArray1678[l] = j + (i3 + ((Model.zoom[j1] - i3) * k6 >> 16) << 9) / 50;
				Model.anIntArray1679[l] = k + (i4 + ((Model.anIntArray1669[j1] - i4) * k6 >> 16) << 9) / 50;
				Model.anIntArray1680[l++] = i5 + ((triangleColorsB[arg0] - i5) * k6 >> 16);
			}
			if (l1 >= 50) {
				int l6 = (50 - j2) * Model.modelIntArray4[l1 - j2];
				Model.anIntArray1678[l] = j + (i3 + ((Model.zoom[i1] - i3) * l6 >> 16) << 9) / 50;
				Model.anIntArray1679[l] = k + (i4 + ((Model.anIntArray1669[i1] - i4) * l6 >> 16) << 9) / 50;
				Model.anIntArray1680[l++] = i5 + ((triangleColorsA[arg0] - i5) * l6 >> 16);
			}
		}
		int j3 = Model.anIntArray1678[0];
		int j4 = Model.anIntArray1678[1];
		int j5 = Model.anIntArray1678[2];
		int i7 = Model.anIntArray1679[0];
		int j7 = Model.anIntArray1679[1];
		int k7 = Model.anIntArray1679[2];
		if ((j3 - j4) * (k7 - j7) - (i7 - j7) * (j5 - j4) > 0) {
			Rasterizer.edgeRestricted = false;
			if (l == 3) {
				if (j3 < 0 || j4 < 0 || j5 < 0 || j3 > Graphics2D.endX || j4 > Graphics2D.endX || j5 > Graphics2D.endX) {
					Rasterizer.edgeRestricted = true;
				}
				int l7;
				if (faceTypes == null) {
					l7 = 0;
				} else {
					l7 = faceTypes[arg0] & 3;
				}
				if (l7 == 0) {
					Rasterizer.drawShadedTriangle(i7, j7, k7, j3, j4, j5, Model.anIntArray1680[0], Model.anIntArray1680[1], Model.anIntArray1680[2]);
				} else if (l7 == 1) {
					Rasterizer.drawFlatTriangle(i7, j7, k7, j3, j4, j5, Model.modelIntArray3[triangleColorsA[arg0]]);
				} else if (l7 == 2) {
					int j8 = faceTypes[arg0] >> 2;
					int k9 = trianglePIndex[j8];
					int k10 = triangleMIndex[j8];
					int k11 = triangleNIndex[j8];
					Rasterizer.drawTexturedTriangle(i7, j7, k7, j3, j4, j5, Model.anIntArray1680[0], Model.anIntArray1680[1], Model.anIntArray1680[2], Model.zoom[k9], Model.zoom[k10], Model.zoom[k11], Model.anIntArray1669[k9], Model.anIntArray1669[k10], Model.anIntArray1669[k11], Model.anIntArray1670[k9], Model.anIntArray1670[k10], Model.anIntArray1670[k11], triangleColors[arg0]);
				} else if (l7 == 3) {
					int k8 = faceTypes[arg0] >> 2;
					int l9 = trianglePIndex[k8];
					int l10 = triangleMIndex[k8];
					int l11 = triangleNIndex[k8];
					Rasterizer.drawTexturedTriangle(i7, j7, k7, j3, j4, j5, triangleColorsA[arg0], triangleColorsA[arg0], triangleColorsA[arg0], Model.zoom[l9], Model.zoom[l10], Model.zoom[l11], Model.anIntArray1669[l9], Model.anIntArray1669[l10], Model.anIntArray1669[l11], Model.anIntArray1670[l9], Model.anIntArray1670[l10], Model.anIntArray1670[l11], triangleColors[arg0]);
				}
			}
			if (l == 4) {
				if (j3 < 0 || j4 < 0 || j5 < 0 || j3 > Graphics2D.endX || j4 > Graphics2D.endX || j5 > Graphics2D.endX || Model.anIntArray1678[3] < 0 || Model.anIntArray1678[3] > Graphics2D.endX) {
					Rasterizer.edgeRestricted = true;
				}
				int i8;
				if (faceTypes == null) {
					i8 = 0;
				} else {
					i8 = faceTypes[arg0] & 3;
				}
				if (i8 == 0) {
					Rasterizer.drawShadedTriangle(i7, j7, k7, j3, j4, j5, Model.anIntArray1680[0], Model.anIntArray1680[1], Model.anIntArray1680[2]);
					Rasterizer.drawShadedTriangle(i7, k7, Model.anIntArray1679[3], j3, j5, Model.anIntArray1678[3], Model.anIntArray1680[0], Model.anIntArray1680[2], Model.anIntArray1680[3]);
					return;
				}
				if (i8 == 1) {
					int l8 = Model.modelIntArray3[triangleColorsA[arg0]];
					Rasterizer.drawFlatTriangle(i7, j7, k7, j3, j4, j5, l8);
					Rasterizer.drawFlatTriangle(i7, k7, Model.anIntArray1679[3], j3, j5, Model.anIntArray1678[3], l8);
					return;
				}
				if (i8 == 2) {
					int i9 = faceTypes[arg0] >> 2;
					int i10 = trianglePIndex[i9];
					int i11 = triangleMIndex[i9];
					int i12 = triangleNIndex[i9];
					Rasterizer.drawTexturedTriangle(i7, j7, k7, j3, j4, j5, Model.anIntArray1680[0], Model.anIntArray1680[1], Model.anIntArray1680[2], Model.zoom[i10], Model.zoom[i11], Model.zoom[i12], Model.anIntArray1669[i10], Model.anIntArray1669[i11], Model.anIntArray1669[i12], Model.anIntArray1670[i10], Model.anIntArray1670[i11], Model.anIntArray1670[i12], triangleColors[arg0]);
					Rasterizer.drawTexturedTriangle(i7, k7, Model.anIntArray1679[3], j3, j5, Model.anIntArray1678[3], Model.anIntArray1680[0], Model.anIntArray1680[2], Model.anIntArray1680[3], Model.zoom[i10], Model.zoom[i11], Model.zoom[i12], Model.anIntArray1669[i10], Model.anIntArray1669[i11], Model.anIntArray1669[i12], Model.anIntArray1670[i10], Model.anIntArray1670[i11], Model.anIntArray1670[i12], triangleColors[arg0]);
					return;
				}
				if (i8 == 3) {
					int j9 = faceTypes[arg0] >> 2;
					int j10 = trianglePIndex[j9];
					int j11 = triangleMIndex[j9];
					int j12 = triangleNIndex[j9];
					Rasterizer.drawTexturedTriangle(i7, j7, k7, j3, j4, j5, triangleColorsA[arg0], triangleColorsA[arg0], triangleColorsA[arg0], Model.zoom[j10], Model.zoom[j11], Model.zoom[j12], Model.anIntArray1669[j10], Model.anIntArray1669[j11], Model.anIntArray1669[j12], Model.anIntArray1670[j10], Model.anIntArray1670[j11], Model.anIntArray1670[j12], triangleColors[arg0]);
					Rasterizer.drawTexturedTriangle(i7, k7, Model.anIntArray1679[3], j3, j5, Model.anIntArray1678[3], triangleColorsA[arg0], triangleColorsA[arg0], triangleColorsA[arg0], Model.zoom[j10], Model.zoom[j11], Model.zoom[j12], Model.anIntArray1669[j10], Model.anIntArray1669[j11], Model.anIntArray1669[j12], Model.anIntArray1670[j10], Model.anIntArray1670[j11], Model.anIntArray1670[j12], triangleColors[arg0]);
				}
			}
		}
	}

	private final boolean method486(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7) {
		if (arg1 < arg2 && arg1 < arg3 && arg1 < arg4) {
			return false;
		}
		if (arg1 > arg2 && arg1 > arg3 && arg1 > arg4) {
			return false;
		}
		if (arg0 < arg5 && arg0 < arg6 && arg0 < arg7) {
			return false;
		}
		return arg0 <= arg5 || arg0 <= arg6 || arg0 <= arg7;
	}
	private boolean aBoolean1618;
	public static int dummyCounter;
	public static Model aModel_1621 = new Model(true);
	private static int anIntArray1622[] = new int[2000];
	private static int anIntArray1623[] = new int[2000];
	private static int anIntArray1624[] = new int[2000];
	private static int anIntArray1625[] = new int[2000];
	public int vertexCount;
	public int xVertex[];
	public int yVertex[];
	public int zVertex[];
	public int triangleCount;
	public int facePointA[];
	public int facePointB[];
	public int facePointC[];
	public int triangleColorsA[];
	public int triangleColorsB[];
	public int triangleColorsC[];
	public int faceTypes[];
	public int facePriority[];
	public int transparency[];
	public int triangleColors[];
	public int priority;
	public int texturedTriangleCount;
	public int trianglePIndex[];
	public int triangleMIndex[];
	public int triangleNIndex[];
	public int anInt1646;
	public int anInt1647;
	public int anInt1648;
	public int anInt1649;
	public int anInt1650;
	public int trimHeight;
	public int anInt1652;
	public int anInt1653;
	public int anInt1654;
	public int vertexVSkins[];
	public int tSkin[];
	public int vertexSkins[][];
	public int triangleSkin[][];
	public boolean aBoolean1659;
	VertexNormal vertices[];
	static ModelHeader modelHeaderCache[];
	static FileRequester fileRequester;
	static boolean edgeRestricted[] = new boolean[8000];
	static boolean aBooleanArray1664[] = new boolean[8000];
	static int anIntArray1665[] = new int[8000];
	static int triangleX[] = new int[8000];
	static int anIntArray1667[] = new int[8000];
	static int zoom[] = new int[8000];
	static int anIntArray1669[] = new int[8000];
	static int anIntArray1670[] = new int[8000];
	static int anIntArray1671[] = new int[1500];
	static int anIntArrayArray1672[][] = new int[1500][512];
	static int faceLists[] = new int[12];
	static int anIntArrayArray1674[][] = new int[12][2000];
	static int anIntArray1675[] = new int[2000];
	static int anIntArray1676[] = new int[2000];
	static int anIntArray1677[] = new int[12];
	static int anIntArray1678[] = new int[10];
	static int anIntArray1679[] = new int[10];
	static int anIntArray1680[] = new int[10];
	static int vertexModX;
	static int vertexModY;
	static int vertexModZ;
	public static boolean aBoolean1684;
	public static int anInt1685;
	public static int anInt1686;
	public static int anInt1687;
	public static int anIntArray1688[] = new int[1000];
	public static int modelIntArray1[];
	public static int modelIntArray2[];
	static int modelIntArray3[];
	static int modelIntArray4[];
	static {
		Model.modelIntArray1 = Rasterizer.sineTable;
		Model.modelIntArray2 = Rasterizer.cosineTable;
		Model.modelIntArray3 = Rasterizer.palette;
		Model.modelIntArray4 = Rasterizer.anIntArray1469;
	}
}
