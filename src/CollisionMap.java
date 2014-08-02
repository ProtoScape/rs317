/**
 * CollisionMap [Class11]
 * Handles the clipping flags for the client
 */
final class CollisionMap {
	public CollisionMap() {
		offsetX = 0; // anInt290
		offsetY = 0; // anInt291
		baseX = 104; // anInt292
		baseY = 104; // anInt293
		collisionFlags = new int[baseX][baseY];
		reset();
	}

	public void reset() {
		for (int x = 0; x < baseX; x++) {
			for (int y = 0; y < baseY; y++) {
				if (x == 0 || y == 0 || x == baseX - 1 || y == baseY - 1) {
					collisionFlags[x][y] = 0xffffff;
				} else {
					collisionFlags[x][y] = 0x1000000;
				}
			}
		}
	}

	public void method211(int y, int rotation, int x, int objectType, boolean flag) {
		x -= offsetX;
		y -= offsetY;
		if (objectType == 0) {
			if (rotation == 0) {
				setFlagMask(x, y, 128);
				setFlagMask(x - 1, y, 8);
			}
			if (rotation == 1) {
				setFlagMask(x, y, 2);
				setFlagMask(x, y + 1, 32);
			}
			if (rotation == 2) {
				setFlagMask(x, y, 8);
				setFlagMask(x + 1, y, 128);
			}
			if (rotation == 3) {
				setFlagMask(x, y, 32);
				setFlagMask(x, y - 1, 2);
			}
		}
		if (objectType == 1 || objectType == 3) {
			if (rotation == 0) {
				setFlagMask(x, y, 1);
				setFlagMask(x - 1, y + 1, 16);
			}
			if (rotation == 1) {
				setFlagMask(x, y, 4);
				setFlagMask(x + 1, y + 1, 64);
			}
			if (rotation == 2) {
				setFlagMask(x, y, 16);
				setFlagMask(x + 1, y - 1, 1);
			}
			if (rotation == 3) {
				setFlagMask(x, y, 64);
				setFlagMask(x - 1, y - 1, 4);
			}
		}
		if (objectType == 2) {
			if (rotation == 0) {
				setFlagMask(x, y, 130);
				setFlagMask(x - 1, y, 8);
				setFlagMask(x, y + 1, 32);
			}
			if (rotation == 1) {
				setFlagMask(x, y, 10);
				setFlagMask(x, y + 1, 32);
				setFlagMask(x + 1, y, 128);
			}
			if (rotation == 2) {
				setFlagMask(x, y, 40);
				setFlagMask(x + 1, y, 128);
				setFlagMask(x, y - 1, 2);
			}
			if (rotation == 3) {
				setFlagMask(x, y, 160);
				setFlagMask(x, y - 1, 2);
				setFlagMask(x - 1, y, 8);
			}
		}
		if (flag) {
			if (objectType == 0) {
				if (rotation == 0) {
					setFlagMask(x, y, 0x10000);
					setFlagMask(x - 1, y, 4096);
				}
				if (rotation == 1) {
					setFlagMask(x, y, 1024);
					setFlagMask(x, y + 1, 16384);
				}
				if (rotation == 2) {
					setFlagMask(x, y, 4096);
					setFlagMask(x + 1, y, 0x10000);
				}
				if (rotation == 3) {
					setFlagMask(x, y, 16384);
					setFlagMask(x, y - 1, 1024);
				}
			}
			if (objectType == 1 || objectType == 3) {
				if (rotation == 0) {
					setFlagMask(x, y, 512);
					setFlagMask(x - 1, y + 1, 8192);
				}
				if (rotation == 1) {
					setFlagMask(x, y, 2048);
					setFlagMask(x + 1, y + 1, 32768);
				}
				if (rotation == 2) {
					setFlagMask(x, y, 8192);
					setFlagMask(x + 1, y - 1, 512);
				}
				if (rotation == 3) {
					setFlagMask(x, y, 32768);
					setFlagMask(x - 1, y - 1, 2048);
				}
			}
			if (objectType == 2) {
				if (rotation == 0) {
					setFlagMask(x, y, 0x10400);
					setFlagMask(x - 1, y, 4096);
					setFlagMask(x, y + 1, 16384);
				}
				if (rotation == 1) {
					setFlagMask(x, y, 5120);
					setFlagMask(x, y + 1, 16384);
					setFlagMask(x + 1, y, 0x10000);
				}
				if (rotation == 2) {
					setFlagMask(x, y, 20480);
					setFlagMask(x + 1, y, 0x10000);
					setFlagMask(x, y - 1, 1024);
				}
				if (rotation == 3) {
					setFlagMask(x, y, 0x14000);
					setFlagMask(x, y - 1, 1024);
					setFlagMask(x - 1, y, 4096);
				}
			}
		}
	}

	public void method212(boolean flag, int xSize, int ySize, int x, int y, int rotation) {
		int k1 = 256;
		if (flag) {
			k1 += 0x20000;
		}
		x -= offsetX;
		y -= offsetY;
		if (rotation == 1 || rotation == 3) {
			int srcX = xSize;
			xSize = ySize;
			ySize = srcX;
		}
		for (int i2 = x; i2 < x + xSize; i2++) {
			if (i2 >= 0 && i2 < baseX) {
				for (int j2 = y; j2 < y + ySize; j2++) {
					if (j2 >= 0 && j2 < baseY) {
						setFlagMask(i2, j2, k1);
					}
				}
			}
		}
	}

	public void method213(int y, int x) {
		x -= offsetX;
		y -= offsetY;
		collisionFlags[x][y] |= 0x200000;
	}

	private void setFlagMask(int x, int y, int mask) { // method214
		collisionFlags[x][y] |= mask;
	}

	public void method215(int rotation, int objectType, boolean flag, int x, int y) {
		x -= offsetX;
		y -= offsetY;
		if (objectType == 0) {
			if (rotation == 0) {
				method217(128, x, y);
				method217(8, x - 1, y);
			}
			if (rotation == 1) {
				method217(2, x, y);
				method217(32, x, y + 1);
			}
			if (rotation == 2) {
				method217(8, x, y);
				method217(128, x + 1, y);
			}
			if (rotation == 3) {
				method217(32, x, y);
				method217(2, x, y - 1);
			}
		}
		if (objectType == 1 || objectType == 3) {
			if (rotation == 0) {
				method217(1, x, y);
				method217(16, x - 1, y + 1);
			}
			if (rotation == 1) {
				method217(4, x, y);
				method217(64, x + 1, y + 1);
			}
			if (rotation == 2) {
				method217(16, x, y);
				method217(1, x + 1, y - 1);
			}
			if (rotation == 3) {
				method217(64, x, y);
				method217(4, x - 1, y - 1);
			}
		}
		if (objectType == 2) {
			if (rotation == 0) {
				method217(130, x, y);
				method217(8, x - 1, y);
				method217(32, x, y + 1);
			}
			if (rotation == 1) {
				method217(10, x, y);
				method217(32, x, y + 1);
				method217(128, x + 1, y);
			}
			if (rotation == 2) {
				method217(40, x, y);
				method217(128, x + 1, y);
				method217(2, x, y - 1);
			}
			if (rotation == 3) {
				method217(160, x, y);
				method217(2, x, y - 1);
				method217(8, x - 1, y);
			}
		}
		if (flag) {
			if (objectType == 0) {
				if (rotation == 0) {
					method217(0x10000, x, y);
					method217(4096, x - 1, y);
				}
				if (rotation == 1) {
					method217(1024, x, y);
					method217(16384, x, y + 1);
				}
				if (rotation == 2) {
					method217(4096, x, y);
					method217(0x10000, x + 1, y);
				}
				if (rotation == 3) {
					method217(16384, x, y);
					method217(1024, x, y - 1);
				}
			}
			if (objectType == 1 || objectType == 3) {
				if (rotation == 0) {
					method217(512, x, y);
					method217(8192, x - 1, y + 1);
				}
				if (rotation == 1) {
					method217(2048, x, y);
					method217(32768, x + 1, y + 1);
				}
				if (rotation == 2) {
					method217(8192, x, y);
					method217(512, x + 1, y - 1);
				}
				if (rotation == 3) {
					method217(32768, x, y);
					method217(2048, x - 1, y - 1);
				}
			}
			if (objectType == 2) {
				if (rotation == 0) {
					method217(0x10400, x, y);
					method217(4096, x - 1, y);
					method217(16384, x, y + 1);
				}
				if (rotation == 1) {
					method217(5120, x, y);
					method217(16384, x, y + 1);
					method217(0x10000, x + 1, y);
				}
				if (rotation == 2) {
					method217(20480, x, y);
					method217(0x10000, x + 1, y);
					method217(1024, x, y - 1);
				}
				if (rotation == 3) {
					method217(0x14000, x, y);
					method217(1024, x, y - 1);
					method217(4096, x - 1, y);
				}
			}
		}
	}

	public void method216(int rotation, int xSize, int x, int y, int ySize, boolean flag) {
		int j1 = 256;
		if (flag) {
			j1 += 0x20000;
		}
		x -= offsetX;
		y -= offsetY;
		if (rotation == 1 || rotation == 3) {
			int srcXSize = xSize;
			xSize = ySize;
			ySize = srcXSize;
		}
		for (int l1 = x; l1 < x + xSize; l1++) {
			if (l1 >= 0 && l1 < baseX) {
				for (int i2 = y; i2 < y + ySize; i2++) {
					if (i2 >= 0 && i2 < baseY) {
						method217(j1, l1, i2);
					}
				}
			}
		}
	}

	private void method217(int arg0, int x, int y) {
		collisionFlags[x][y] &= 0xffffff - arg0;
	}

	public void method218(int y, int x) {
		x -= offsetX;
		y -= offsetY;
		collisionFlags[x][y] &= 0xdfffff;
	}

	public boolean method219(int destX, int srcX, int srcY, int rotation, int objectType, int destY) {
		if (srcX == destX && srcY == destY) {
			return true;
		}
		srcX -= offsetX;
		srcY -= offsetY;
		destX -= offsetX;
		destY -= offsetY;
		if (objectType == 0) {
			if (rotation == 0) {
				if (srcX == destX - 1 && srcY == destY) {
					return true;
				}
				if (srcX == destX && srcY == destY + 1 && (collisionFlags[srcX][srcY] & 0x1280120) == 0) {
					return true;
				}
				if (srcX == destX && srcY == destY - 1 && (collisionFlags[srcX][srcY] & 0x1280102) == 0) {
					return true;
				}
			} else if (rotation == 1) {
				if (srcX == destX && srcY == destY + 1) {
					return true;
				}
				if (srcX == destX - 1 && srcY == destY && (collisionFlags[srcX][srcY] & 0x1280108) == 0) {
					return true;
				}
				if (srcX == destX + 1 && srcY == destY && (collisionFlags[srcX][srcY] & 0x1280180) == 0) {
					return true;
				}
			} else if (rotation == 2) {
				if (srcX == destX + 1 && srcY == destY) {
					return true;
				}
				if (srcX == destX && srcY == destY + 1 && (collisionFlags[srcX][srcY] & 0x1280120) == 0) {
					return true;
				}
				if (srcX == destX && srcY == destY - 1 && (collisionFlags[srcX][srcY] & 0x1280102) == 0) {
					return true;
				}
			} else if (rotation == 3) {
				if (srcX == destX && srcY == destY - 1) {
					return true;
				}
				if (srcX == destX - 1 && srcY == destY && (collisionFlags[srcX][srcY] & 0x1280108) == 0) {
					return true;
				}
				if (srcX == destX + 1 && srcY == destY && (collisionFlags[srcX][srcY] & 0x1280180) == 0) {
					return true;
				}
			}
		}
		if (objectType == 2) {
			if (rotation == 0) {
				if (srcX == destX - 1 && srcY == destY) {
					return true;
				}
				if (srcX == destX && srcY == destY + 1) {
					return true;
				}
				if (srcX == destX + 1 && srcY == destY && (collisionFlags[srcX][srcY] & 0x1280180) == 0) {
					return true;
				}
				if (srcX == destX && srcY == destY - 1 && (collisionFlags[srcX][srcY] & 0x1280102) == 0) {
					return true;
				}
			} else if (rotation == 1) {
				if (srcX == destX - 1 && srcY == destY && (collisionFlags[srcX][srcY] & 0x1280108) == 0) {
					return true;
				}
				if (srcX == destX && srcY == destY + 1) {
					return true;
				}
				if (srcX == destX + 1 && srcY == destY) {
					return true;
				}
				if (srcX == destX && srcY == destY - 1 && (collisionFlags[srcX][srcY] & 0x1280102) == 0) {
					return true;
				}
			} else if (rotation == 2) {
				if (srcX == destX - 1 && srcY == destY && (collisionFlags[srcX][srcY] & 0x1280108) == 0) {
					return true;
				}
				if (srcX == destX && srcY == destY + 1 && (collisionFlags[srcX][srcY] & 0x1280120) == 0) {
					return true;
				}
				if (srcX == destX + 1 && srcY == destY) {
					return true;
				}
				if (srcX == destX && srcY == destY - 1) {
					return true;
				}
			} else if (rotation == 3) {
				if (srcX == destX - 1 && srcY == destY) {
					return true;
				}
				if (srcX == destX && srcY == destY + 1 && (collisionFlags[srcX][srcY] & 0x1280120) == 0) {
					return true;
				}
				if (srcX == destX + 1 && srcY == destY && (collisionFlags[srcX][srcY] & 0x1280180) == 0) {
					return true;
				}
				if (srcX == destX && srcY == destY - 1) {
					return true;
				}
			}
		}
		if (objectType == 9) {
			if (srcX == destX && srcY == destY + 1 && (collisionFlags[srcX][srcY] & 0x20) == 0) {
				return true;
			}
			if (srcX == destX && srcY == destY - 1 && (collisionFlags[srcX][srcY] & 2) == 0) {
				return true;
			}
			if (srcX == destX - 1 && srcY == destY && (collisionFlags[srcX][srcY] & 8) == 0) {
				return true;
			}
			if (srcX == destX + 1 && srcY == destY && (collisionFlags[srcX][srcY] & 0x80) == 0) {
				return true;
			}
		}
		return false;
	}

	public boolean method220(int destX, int destY, int srcY, int objectType, int rotation, int srcX) {
		if (srcX == destX && srcY == destY) {
			return true;
		}
		srcX -= offsetX;
		srcY -= offsetY;
		destX -= offsetX;
		destY -= offsetY;
		if (objectType == 6 || objectType == 7) {
			if (objectType == 7) {
				rotation = rotation + 2 & 3;
			}
			if (rotation == 0) {
				if (srcX == destX + 1 && srcY == destY && (collisionFlags[srcX][srcY] & 0x80) == 0) {
					return true;
				}
				if (srcX == destX && srcY == destY - 1 && (collisionFlags[srcX][srcY] & 2) == 0) {
					return true;
				}
			} else if (rotation == 1) {
				if (srcX == destX - 1 && srcY == destY && (collisionFlags[srcX][srcY] & 8) == 0) {
					return true;
				}
				if (srcX == destX && srcY == destY - 1 && (collisionFlags[srcX][srcY] & 2) == 0) {
					return true;
				}
			} else if (rotation == 2) {
				if (srcX == destX - 1 && srcY == destY && (collisionFlags[srcX][srcY] & 8) == 0) {
					return true;
				}
				if (srcX == destX && srcY == destY + 1 && (collisionFlags[srcX][srcY] & 0x20) == 0) {
					return true;
				}
			} else if (rotation == 3) {
				if (srcX == destX + 1 && srcY == destY && (collisionFlags[srcX][srcY] & 0x80) == 0) {
					return true;
				}
				if (srcX == destX && srcY == destY + 1 && (collisionFlags[srcX][srcY] & 0x20) == 0) {
					return true;
				}
			}
		}
		if (objectType == 8) {
			if (srcX == destX && srcY == destY + 1 && (collisionFlags[srcX][srcY] & 0x20) == 0) {
				return true;
			}
			if (srcX == destX && srcY == destY - 1 && (collisionFlags[srcX][srcY] & 2) == 0) {
				return true;
			}
			if (srcX == destX - 1 && srcY == destY && (collisionFlags[srcX][srcY] & 8) == 0) {
				return true;
			}
			if (srcX == destX + 1 && srcY == destY && (collisionFlags[srcX][srcY] & 0x80) == 0) {
				return true;
			}
		}
		return false;
	}

	public boolean method221(int destY, int destX, int srcX, int offY, int arg4, int offX, int srcY) {
		int l1 = destX + offX - 1;
		int i2 = destY + offY - 1;
		if (srcX >= destX && srcX <= l1 && srcY >= destY && srcY <= i2) {
			return true;
		}
		if (srcX == destX - 1 && srcY >= destY && srcY <= i2
			&& (collisionFlags[srcX - offsetX][srcY - offsetY] & 8) == 0 && (arg4 & 8) == 0) {
			return true;
		}
		if (srcX == l1 + 1 && srcY >= destY && srcY <= i2
			&& (collisionFlags[srcX - offsetX][srcY - offsetY] & 0x80) == 0 && (arg4 & 2) == 0) {
			return true;
		}
		return srcY == destY - 1 && srcX >= destX && srcX <= l1
			&& (collisionFlags[srcX - offsetX][srcY - offsetY] & 2) == 0 && (arg4 & 4) == 0
			|| srcY == i2 + 1 && srcX >= destX && srcX <= l1
			&& (collisionFlags[srcX - offsetX][srcY - offsetY] & 0x20) == 0 && (arg4 & 1) == 0;
	}
	private final int offsetX;
	private final int offsetY;
	private final int baseX;
	private final int baseY;
	public final int[][] collisionFlags;
}
