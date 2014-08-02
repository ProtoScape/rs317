/**
 * Rasterizer [Texture]
 * Renders points on the 3d screen
 */
final class Rasterizer extends Graphics2D {

	public static void dispose() {
		Rasterizer.gradientFactors = null;
		Rasterizer.gradientFactors = null;
		Rasterizer.sineTable = null;
		Rasterizer.cosineTable = null;
		Rasterizer.lineOffsets = null;
		Rasterizer.textures = null;
		Rasterizer.transparentTextures = null;
		Rasterizer.averageTextureColors = null;
		Rasterizer.textureStore = null;
		Rasterizer.textureCache = null;
		Rasterizer.texturePriorities = null;
		Rasterizer.palette = null;
		Rasterizer.texturePixels = null;
	}

	public static void setDefaultBounds() {
		Rasterizer.lineOffsets = new int[Graphics2D.height];
		for (int i = 0; i < Graphics2D.height; i++) {
			Rasterizer.lineOffsets[i] = Graphics2D.width * i;
		}
		Rasterizer.centerX = Graphics2D.width / 2;
		Rasterizer.centerY = Graphics2D.height / 2;
	}

	public static void init3dCanvas(int x, int y) {
		Rasterizer.lineOffsets = new int[y];
		for (int i = 0; i < y; i++) {
			Rasterizer.lineOffsets[i] = x * i;
		}
		Rasterizer.centerX = x / 2;
		Rasterizer.centerY = y / 2;
	}

	public static void clearTextures() {
		Rasterizer.textureStore = null;
		for (int i = 0; i < 50; i++) {
			Rasterizer.textureCache[i] = null;
		}
	}

	public static void resetTextures() {
		if (Rasterizer.textureStore == null) {
			Rasterizer.textureStoreCount = 20;
			if (Rasterizer.lowMem) {
				Rasterizer.textureStore = new int[Rasterizer.textureStoreCount][16384];
			} else {
				Rasterizer.textureStore = new int[Rasterizer.textureStoreCount][0x10000];
			}
			for (int i = 0; i < 50; i++) {
				Rasterizer.textureCache[i] = null;
			}
		}
	}

	public static void unpackConfig(Archive container) {
		Rasterizer.textureCount = 0;
		for (int i = 0; i < 50; i++) {
			try {
				Rasterizer.textures[i] = new IndexedSprite(container, String.valueOf(i), 0);
				if (Rasterizer.lowMem && Rasterizer.textures[i].trimWidth == 128) {
					Rasterizer.textures[i].shrink();
				} else {
					Rasterizer.textures[i].resize();
				}
				Rasterizer.textureCount++;
			} catch (Exception exception) {
			}
		}
	}

	public static int getMinimapTextureColor(int texture) {
		if (Rasterizer.averageTextureColors[texture] != 0) {
			return Rasterizer.averageTextureColors[texture];
		}
		int red = 0;
		int green = 0;
		int blue = 0;
		int count = Rasterizer.texturePixels[texture].length;
		for (int i = 0; i < count; i++) {
			red += Rasterizer.texturePixels[texture][i] >> 16 & 0xff;
			green += Rasterizer.texturePixels[texture][i] >> 8 & 0xff;
			blue += Rasterizer.texturePixels[texture][i] & 0xff;
		}
		int average = (red / count << 16) + (green / count << 8) + blue / count;
		average = Rasterizer.adjustBrightness(average, 1.3999999999999999D);
		if (average == 0) {
			average = 1;
		}
		Rasterizer.averageTextureColors[texture] = average;
		return average;
	}

	public static void resetTexture(int texture) {
		if (Rasterizer.textureCache[texture] == null) {
			return;
		}
		Rasterizer.textureStore[Rasterizer.textureStoreCount++] = Rasterizer.textureCache[texture];
		Rasterizer.textureCache[texture] = null;
	}

	private static int[] getTexture(int texture) {
		Rasterizer.texturePriorities[texture] = Rasterizer.texturePriority++;
		if (Rasterizer.textureCache[texture] != null) {
			return Rasterizer.textureCache[texture];
		}
		int dest[];
		if (Rasterizer.textureStoreCount > 0) {
			dest = Rasterizer.textureStore[--Rasterizer.textureStoreCount];
			Rasterizer.textureStore[Rasterizer.textureStoreCount] = null;
		} else {
			int high = 0;
			int target = -1;
			for (int i = 0; i < Rasterizer.textureCount; i++) {
				if (Rasterizer.textureCache[i] != null && (Rasterizer.texturePriorities[i] < high || target == -1)) {
					high = Rasterizer.texturePriorities[i];
					target = i;
				}
			}
			dest = Rasterizer.textureCache[target];
			Rasterizer.textureCache[target] = null;
		}
		Rasterizer.textureCache[texture] = dest;
		IndexedSprite indexedSprite = Rasterizer.textures[texture];
		int pixels[] = Rasterizer.texturePixels[texture];
		if (Rasterizer.lowMem) {
			Rasterizer.transparentTextures[texture] = false;
			for (int i = 0; i < 4096; i++) {
				int alpha = dest[i] = pixels[indexedSprite.pixels[i]] & 0xf8f8ff;
				if (alpha == 0) {
					Rasterizer.transparentTextures[texture] = true;
				}
				dest[4096 + i] = alpha - (alpha >>> 3) & 0xf8f8ff;
				dest[8192 + i] = alpha - (alpha >>> 2) & 0xf8f8ff;
				dest[12288 + i] = alpha - (alpha >>> 2) - (alpha >>> 3) & 0xf8f8ff;
			}
		} else {
			if (indexedSprite.width == 64) {
				for (int i = 0; i < 128; i++) {
					for (int j = 0; j < 128; j++) {
						dest[j + (i << 7)] = pixels[indexedSprite.pixels[(j >> 1) + (i >> 1 << 6)]];
					}
				}
			} else {
				for (int i = 0; i < 16384; i++) {
					dest[i] = pixels[indexedSprite.pixels[i]];
				}
			}
			Rasterizer.transparentTextures[texture] = false;
			for (int i = 0; i < 16384; i++) {
				dest[i] &= 0xf8f8ff;
				int k2 = dest[i];
				if (k2 == 0) {
					Rasterizer.transparentTextures[texture] = true;
				}
				dest[16384 + i] = k2 - (k2 >>> 3) & 0xf8f8ff;
				dest[32768 + i] = k2 - (k2 >>> 2) & 0xf8f8ff;
				dest[49152 + i] = k2 - (k2 >>> 2) - (k2 >>> 3) & 0xf8f8ff;
			}
		}
		return dest;
	}

	public static void generatePalette(double brightness) {
		brightness += Math.random() * 0.029999999999999999D - 0.014999999999999999D;
		int i = 0;
		for (int j = 0; j < 512; j++) {
			double d1 = j / 8 / 64D + 0.0078125D;
			double d2 = (j & 7) / 8D + 0.0625D;
			for (int k1 = 0; k1 < 128; k1++) {
				double d3 = k1 / 128D;
				double srcRed = d3;
				double srcGreen = d3;
				double srcBlue = d3;
				if (d2 != 0.0D) {
					double d7;
					if (d3 < 0.5D) {
						d7 = d3 * (1.0D + d2);
					} else {
						d7 = d3 + d2 - d3 * d2;
					}
					double d8 = 2D * d3 - d7;
					double d9 = d1 + 0.33333333333333331D;
					if (d9 > 1.0D) {
						d9--;
					}
					double d10 = d1;
					double d11 = d1 - 0.33333333333333331D;
					if (d11 < 0.0D) {
						d11++;
					}
					if (6D * d9 < 1.0D) {
						srcRed = d8 + (d7 - d8) * 6D * d9;
					} else if (2D * d9 < 1.0D) {
						srcRed = d7;
					} else if (3D * d9 < 2D) {
						srcRed = d8 + (d7 - d8) * (0.66666666666666663D - d9) * 6D;
					} else {
						srcRed = d8;
					}
					if (6D * d10 < 1.0D) {
						srcGreen = d8 + (d7 - d8) * 6D * d10;
					} else if (2D * d10 < 1.0D) {
						srcGreen = d7;
					} else if (3D * d10 < 2D) {
						srcGreen = d8 + (d7 - d8) * (0.66666666666666663D - d10) * 6D;
					} else {
						srcGreen = d8;
					}
					if (6D * d11 < 1.0D) {
						srcBlue = d8 + (d7 - d8) * 6D * d11;
					} else if (2D * d11 < 1.0D) {
						srcBlue = d7;
					} else if (3D * d11 < 2D) {
						srcBlue = d8 + (d7 - d8) * (0.66666666666666663D - d11) * 6D;
					} else {
						srcBlue = d8;
					}
				}
				int destRed = (int) (srcRed * 256D);
				int destGreen = (int) (srcGreen * 256D);
				int destBlue = (int) (srcBlue * 256D);
				int dest = (destRed << 16) + (destGreen << 8) + destBlue;
				dest = Rasterizer.adjustBrightness(dest, brightness);
				if (dest == 0) {
					dest = 1;
				}
				Rasterizer.palette[i++] = dest;
			}
		}
		for (int l = 0; l < 50; l++) {
			if (Rasterizer.textures[l] != null) {
				int ai[] = Rasterizer.textures[l].pallete;
				Rasterizer.texturePixels[l] = new int[ai.length];
				for (int j1 = 0; j1 < ai.length; j1++) {
					Rasterizer.texturePixels[l][j1] = Rasterizer.adjustBrightness(ai[j1], brightness);
					if ((Rasterizer.texturePixels[l][j1] & 0xf8f8ff) == 0 && j1 != 0) {
						Rasterizer.texturePixels[l][j1] = 1;
					}
				}
			}
		}
		for (int i1 = 0; i1 < 50; i1++) {
			Rasterizer.resetTexture(i1);
		}
	}

	private static int adjustBrightness(int rgb, double intensity) {
		double srcRed = (rgb >> 16) / 256D;
		double srcGreen = (rgb >> 8 & 0xff) / 256D;
		double srcBlue = (rgb & 0xff) / 256D;
		srcRed = Math.pow(srcRed, intensity);
		srcGreen = Math.pow(srcGreen, intensity);
		srcBlue = Math.pow(srcBlue, intensity);
		int destRed = (int) (srcRed * 256D);
		int destGreen = (int) (srcGreen * 256D);
		int destBlue = (int) (srcBlue * 256D);
		return (destRed << 16) + (destGreen << 8) + destBlue;
	}

	public static void drawShadedTriangle(int y1, int y2, int y3, int x1, int x2, int x3, int grad1, int grad2, int grad3) {
		int j2 = 0;
		int k2 = 0;
		if (y2 != y1) {
			j2 = (x2 - x1 << 16) / (y2 - y1);
			k2 = (grad2 - grad1 << 15) / (y2 - y1);
		}
		int l2 = 0;
		int i3 = 0;
		if (y3 != y2) {
			l2 = (x3 - x2 << 16) / (y3 - y2);
			i3 = (grad3 - grad2 << 15) / (y3 - y2);
		}
		int j3 = 0;
		int k3 = 0;
		if (y3 != y1) {
			j3 = (x1 - x3 << 16) / (y1 - y3);
			k3 = (grad1 - grad3 << 15) / (y1 - y3);
		}
		if (y1 <= y2 && y1 <= y3) {
			if (y1 >= Graphics2D.bottomY) {
				return;
			}
			if (y2 > Graphics2D.bottomY) {
				y2 = Graphics2D.bottomY;
			}
			if (y3 > Graphics2D.bottomY) {
				y3 = Graphics2D.bottomY;
			}
			if (y2 < y3) {
				x3 = x1 <<= 16;
				grad3 = grad1 <<= 15;
				if (y1 < 0) {
					x3 -= j3 * y1;
					x1 -= j2 * y1;
					grad3 -= k3 * y1;
					grad1 -= k2 * y1;
					y1 = 0;
				}
				x2 <<= 16;
				grad2 <<= 15;
				if (y2 < 0) {
					x2 -= l2 * y2;
					grad2 -= i3 * y2;
					y2 = 0;
				}
				if (y1 != y2 && j3 < j2 || y1 == y2 && j3 > l2) {
					y3 -= y2;
					y2 -= y1;
					for (y1 = Rasterizer.lineOffsets[y1]; --y2 >= 0; y1 += Graphics2D.width) {
						Rasterizer.drawShadedLine(Graphics2D.pixels, y1, x3 >> 16, x1 >> 16, grad3 >> 7, grad1 >> 7);
						x3 += j3;
						x1 += j2;
						grad3 += k3;
						grad1 += k2;
					}
					while (--y3 >= 0) {
						Rasterizer.drawShadedLine(Graphics2D.pixels, y1, x3 >> 16, x2 >> 16, grad3 >> 7, grad2 >> 7);
						x3 += j3;
						x2 += l2;
						grad3 += k3;
						grad2 += i3;
						y1 += Graphics2D.width;
					}
					return;
				}
				y3 -= y2;
				y2 -= y1;
				for (y1 = Rasterizer.lineOffsets[y1]; --y2 >= 0; y1 += Graphics2D.width) {
					Rasterizer.drawShadedLine(Graphics2D.pixels, y1, x1 >> 16, x3 >> 16, grad1 >> 7, grad3 >> 7);
					x3 += j3;
					x1 += j2;
					grad3 += k3;
					grad1 += k2;
				}
				while (--y3 >= 0) {
					Rasterizer.drawShadedLine(Graphics2D.pixels, y1, x2 >> 16, x3 >> 16, grad2 >> 7, grad3 >> 7);
					x3 += j3;
					x2 += l2;
					grad3 += k3;
					grad2 += i3;
					y1 += Graphics2D.width;
				}
				return;
			}
			x2 = x1 <<= 16;
			grad2 = grad1 <<= 15;
			if (y1 < 0) {
				x2 -= j3 * y1;
				x1 -= j2 * y1;
				grad2 -= k3 * y1;
				grad1 -= k2 * y1;
				y1 = 0;
			}
			x3 <<= 16;
			grad3 <<= 15;
			if (y3 < 0) {
				x3 -= l2 * y3;
				grad3 -= i3 * y3;
				y3 = 0;
			}
			if (y1 != y3 && j3 < j2 || y1 == y3 && l2 > j2) {
				y2 -= y3;
				y3 -= y1;
				for (y1 = Rasterizer.lineOffsets[y1]; --y3 >= 0; y1 += Graphics2D.width) {
					Rasterizer.drawShadedLine(Graphics2D.pixels, y1, x2 >> 16, x1 >> 16, grad2 >> 7, grad1 >> 7);
					x2 += j3;
					x1 += j2;
					grad2 += k3;
					grad1 += k2;
				}
				while (--y2 >= 0) {
					Rasterizer.drawShadedLine(Graphics2D.pixels, y1, x3 >> 16, x1 >> 16, grad3 >> 7, grad1 >> 7);
					x3 += l2;
					x1 += j2;
					grad3 += i3;
					grad1 += k2;
					y1 += Graphics2D.width;
				}
				return;
			}
			y2 -= y3;
			y3 -= y1;
			for (y1 = Rasterizer.lineOffsets[y1]; --y3 >= 0; y1 += Graphics2D.width) {
				Rasterizer.drawShadedLine(Graphics2D.pixels, y1, x1 >> 16, x2 >> 16, grad1 >> 7, grad2 >> 7);
				x2 += j3;
				x1 += j2;
				grad2 += k3;
				grad1 += k2;
			}
			while (--y2 >= 0) {
				Rasterizer.drawShadedLine(Graphics2D.pixels, y1, x1 >> 16, x3 >> 16, grad1 >> 7, grad3 >> 7);
				x3 += l2;
				x1 += j2;
				grad3 += i3;
				grad1 += k2;
				y1 += Graphics2D.width;
			}
			return;
		}
		if (y2 <= y3) {
			if (y2 >= Graphics2D.bottomY) {
				return;
			}
			if (y3 > Graphics2D.bottomY) {
				y3 = Graphics2D.bottomY;
			}
			if (y1 > Graphics2D.bottomY) {
				y1 = Graphics2D.bottomY;
			}
			if (y3 < y1) {
				x1 = x2 <<= 16;
				grad1 = grad2 <<= 15;
				if (y2 < 0) {
					x1 -= j2 * y2;
					x2 -= l2 * y2;
					grad1 -= k2 * y2;
					grad2 -= i3 * y2;
					y2 = 0;
				}
				x3 <<= 16;
				grad3 <<= 15;
				if (y3 < 0) {
					x3 -= j3 * y3;
					grad3 -= k3 * y3;
					y3 = 0;
				}
				if (y2 != y3 && j2 < l2 || y2 == y3 && j2 > j3) {
					y1 -= y3;
					y3 -= y2;
					for (y2 = Rasterizer.lineOffsets[y2]; --y3 >= 0; y2 += Graphics2D.width) {
						Rasterizer.drawShadedLine(Graphics2D.pixels, y2, x1 >> 16, x2 >> 16, grad1 >> 7, grad2 >> 7);
						x1 += j2;
						x2 += l2;
						grad1 += k2;
						grad2 += i3;
					}
					while (--y1 >= 0) {
						Rasterizer.drawShadedLine(Graphics2D.pixels, y2, x1 >> 16, x3 >> 16, grad1 >> 7, grad3 >> 7);
						x1 += j2;
						x3 += j3;
						grad1 += k2;
						grad3 += k3;
						y2 += Graphics2D.width;
					}
					return;
				}
				y1 -= y3;
				y3 -= y2;
				for (y2 = Rasterizer.lineOffsets[y2]; --y3 >= 0; y2 += Graphics2D.width) {
					Rasterizer.drawShadedLine(Graphics2D.pixels, y2, x2 >> 16, x1 >> 16, grad2 >> 7, grad1 >> 7);
					x1 += j2;
					x2 += l2;
					grad1 += k2;
					grad2 += i3;
				}
				while (--y1 >= 0) {
					Rasterizer.drawShadedLine(Graphics2D.pixels, y2, x3 >> 16, x1 >> 16, grad3 >> 7, grad1 >> 7);
					x1 += j2;
					x3 += j3;
					grad1 += k2;
					grad3 += k3;
					y2 += Graphics2D.width;
				}
				return;
			}
			x3 = x2 <<= 16;
			grad3 = grad2 <<= 15;
			if (y2 < 0) {
				x3 -= j2 * y2;
				x2 -= l2 * y2;
				grad3 -= k2 * y2;
				grad2 -= i3 * y2;
				y2 = 0;
			}
			x1 <<= 16;
			grad1 <<= 15;
			if (y1 < 0) {
				x1 -= j3 * y1;
				grad1 -= k3 * y1;
				y1 = 0;
			}
			if (j2 < l2) {
				y3 -= y1;
				y1 -= y2;
				for (y2 = Rasterizer.lineOffsets[y2]; --y1 >= 0; y2 += Graphics2D.width) {
					Rasterizer.drawShadedLine(Graphics2D.pixels, y2, x3 >> 16, x2 >> 16, grad3 >> 7, grad2 >> 7);
					x3 += j2;
					x2 += l2;
					grad3 += k2;
					grad2 += i3;
				}
				while (--y3 >= 0) {
					Rasterizer.drawShadedLine(Graphics2D.pixels, y2, x1 >> 16, x2 >> 16, grad1 >> 7, grad2 >> 7);
					x1 += j3;
					x2 += l2;
					grad1 += k3;
					grad2 += i3;
					y2 += Graphics2D.width;
				}
				return;
			}
			y3 -= y1;
			y1 -= y2;
			for (y2 = Rasterizer.lineOffsets[y2]; --y1 >= 0; y2 += Graphics2D.width) {
				Rasterizer.drawShadedLine(Graphics2D.pixels, y2, x2 >> 16, x3 >> 16, grad2 >> 7, grad3 >> 7);
				x3 += j2;
				x2 += l2;
				grad3 += k2;
				grad2 += i3;
			}
			while (--y3 >= 0) {
				Rasterizer.drawShadedLine(Graphics2D.pixels, y2, x2 >> 16, x1 >> 16, grad2 >> 7, grad1 >> 7);
				x1 += j3;
				x2 += l2;
				grad1 += k3;
				grad2 += i3;
				y2 += Graphics2D.width;
			}
			return;
		}
		if (y3 >= Graphics2D.bottomY) {
			return;
		}
		if (y1 > Graphics2D.bottomY) {
			y1 = Graphics2D.bottomY;
		}
		if (y2 > Graphics2D.bottomY) {
			y2 = Graphics2D.bottomY;
		}
		if (y1 < y2) {
			x2 = x3 <<= 16;
			grad2 = grad3 <<= 15;
			if (y3 < 0) {
				x2 -= l2 * y3;
				x3 -= j3 * y3;
				grad2 -= i3 * y3;
				grad3 -= k3 * y3;
				y3 = 0;
			}
			x1 <<= 16;
			grad1 <<= 15;
			if (y1 < 0) {
				x1 -= j2 * y1;
				grad1 -= k2 * y1;
				y1 = 0;
			}
			if (l2 < j3) {
				y2 -= y1;
				y1 -= y3;
				for (y3 = Rasterizer.lineOffsets[y3]; --y1 >= 0; y3 += Graphics2D.width) {
					Rasterizer.drawShadedLine(Graphics2D.pixels, y3, x2 >> 16, x3 >> 16, grad2 >> 7, grad3 >> 7);
					x2 += l2;
					x3 += j3;
					grad2 += i3;
					grad3 += k3;
				}
				while (--y2 >= 0) {
					Rasterizer.drawShadedLine(Graphics2D.pixels, y3, x2 >> 16, x1 >> 16, grad2 >> 7, grad1 >> 7);
					x2 += l2;
					x1 += j2;
					grad2 += i3;
					grad1 += k2;
					y3 += Graphics2D.width;
				}
				return;
			}
			y2 -= y1;
			y1 -= y3;
			for (y3 = Rasterizer.lineOffsets[y3]; --y1 >= 0; y3 += Graphics2D.width) {
				Rasterizer.drawShadedLine(Graphics2D.pixels, y3, x3 >> 16, x2 >> 16, grad3 >> 7, grad2 >> 7);
				x2 += l2;
				x3 += j3;
				grad2 += i3;
				grad3 += k3;
			}
			while (--y2 >= 0) {
				Rasterizer.drawShadedLine(Graphics2D.pixels, y3, x1 >> 16, x2 >> 16, grad1 >> 7, grad2 >> 7);
				x2 += l2;
				x1 += j2;
				grad2 += i3;
				grad1 += k2;
				y3 += Graphics2D.width;
			}
			return;
		}
		x1 = x3 <<= 16;
		grad1 = grad3 <<= 15;
		if (y3 < 0) {
			x1 -= l2 * y3;
			x3 -= j3 * y3;
			grad1 -= i3 * y3;
			grad3 -= k3 * y3;
			y3 = 0;
		}
		x2 <<= 16;
		grad2 <<= 15;
		if (y2 < 0) {
			x2 -= j2 * y2;
			grad2 -= k2 * y2;
			y2 = 0;
		}
		if (l2 < j3) {
			y1 -= y2;
			y2 -= y3;
			for (y3 = Rasterizer.lineOffsets[y3]; --y2 >= 0; y3 += Graphics2D.width) {
				Rasterizer.drawShadedLine(Graphics2D.pixels, y3, x1 >> 16, x3 >> 16, grad1 >> 7, grad3 >> 7);
				x1 += l2;
				x3 += j3;
				grad1 += i3;
				grad3 += k3;
			}
			while (--y1 >= 0) {
				Rasterizer.drawShadedLine(Graphics2D.pixels, y3, x2 >> 16, x3 >> 16, grad2 >> 7, grad3 >> 7);
				x2 += j2;
				x3 += j3;
				grad2 += k2;
				grad3 += k3;
				y3 += Graphics2D.width;
			}
			return;
		}
		y1 -= y2;
		y2 -= y3;
		for (y3 = Rasterizer.lineOffsets[y3]; --y2 >= 0; y3 += Graphics2D.width) {
			Rasterizer.drawShadedLine(Graphics2D.pixels, y3, x3 >> 16, x1 >> 16, grad3 >> 7, grad1 >> 7);
			x1 += l2;
			x3 += j3;
			grad1 += i3;
			grad3 += k3;
		}
		while (--y1 >= 0) {
			Rasterizer.drawShadedLine(Graphics2D.pixels, y3, x3 >> 16, x2 >> 16, grad3 >> 7, grad2 >> 7);
			x2 += j2;
			x3 += j3;
			grad2 += k2;
			grad3 += k3;
			y3 += Graphics2D.width;
		}
	}

	public static void drawShadedLine(int[] dest, int destOff, int startX, int endX, int colorIndex, int gradient) {
		int color;
		int loops;
		int off = 0;
		if (Rasterizer.edgeRestricted) {
			if (endX > Graphics2D.endX) {
				endX = Graphics2D.endX;
			}
			if (startX < 0) {
				colorIndex -= startX * off;
				startX = 0;
			}
		}
		if (startX < endX) {
			destOff += startX;
			colorIndex += off * startX;
			if (Rasterizer.colorRestricted) {
				loops = endX - startX >> 2;
				if (loops > 0) {
					off = (gradient - colorIndex) * Rasterizer.gradientFactors[loops] >> 15;
				} else {
					off = 0;
				}
				if (Rasterizer.alpha == 0) {
					if (loops > 0) {
						do {
							color = Rasterizer.palette[colorIndex >> 8];
							colorIndex += off;
							dest[destOff++] = color;
							dest[destOff++] = color;
							dest[destOff++] = color;
							dest[destOff++] = color;
						} while (--loops > 0);
					}
					loops = endX - startX & 0x3;
					if (loops > 0) {
						color = Rasterizer.palette[colorIndex >> 8];
						do {
							dest[destOff++] = color;
						} while (--loops > 0);
					}
				} else {
					int destAlpha = Rasterizer.alpha;
					int srcAlpha = 256 - Rasterizer.alpha;
					if (loops > 0) {
						do {
							color = Rasterizer.palette[colorIndex >> 8];
							colorIndex += off;
							color = ((color & 0xff00ff) * srcAlpha >> 8 & 0xff00ff) + ((color & 0xff00) * srcAlpha >> 8 & 0xff00);
							int i = dest[destOff];
							dest[destOff++] = color + ((i & 0xff00ff) * destAlpha >> 8 & 0xff00ff) + ((i & 0xff00) * destAlpha >> 8 & 0xff00);
							i = dest[destOff];
							dest[destOff++] = color + ((i & 0xff00ff) * destAlpha >> 8 & 0xff00ff) + ((i & 0xff00) * destAlpha >> 8 & 0xff00);
							i = dest[destOff];
							dest[destOff++] = color + ((i & 0xff00ff) * destAlpha >> 8 & 0xff00ff) + ((i & 0xff00) * destAlpha >> 8 & 0xff00);
							i = dest[destOff];
							dest[destOff++] = color + ((i & 0xff00ff) * destAlpha >> 8 & 0xff00ff) + ((i & 0xff00) * destAlpha >> 8 & 0xff00);
						} while (--loops > 0);
					}
					loops = endX - startX & 0x3;
					if (loops > 0) {
						color = Rasterizer.palette[colorIndex >> 8];
						color = ((color & 0xff00ff) * srcAlpha >> 8 & 0xff00ff) + ((color & 0xff00) * srcAlpha >> 8 & 0xff00);
						do {
							int i = dest[destOff];
							dest[destOff++] = color + ((i & 0xff00ff) * destAlpha >> 8 & 0xff00ff) + ((i & 0xff00) * destAlpha >> 8 & 0xff00);
						} while (--loops > 0);
					}
				}
			} else {
				int step = (gradient - colorIndex) / (endX - startX);
				loops = endX - startX;
				if (Rasterizer.alpha == 0) {
					do {
						dest[destOff++] = Rasterizer.palette[colorIndex >> 8];
						colorIndex += step;
					} while (--loops > 0);
				} else {
					int destAlpha = Rasterizer.alpha;
					int srcAlpha = 256 - Rasterizer.alpha;
					do {
						color = Rasterizer.palette[colorIndex >> 8];
						colorIndex += step;
						color = ((color & 0xff00ff) * srcAlpha >> 8 & 0xff00ff) + ((color & 0xff00) * srcAlpha >> 8 & 0xff00);
						int i = dest[destOff];
						dest[destOff++] = color + ((i & 0xff00ff) * destAlpha >> 8 & 0xff00ff) + ((i & 0xff00) * destAlpha >> 8 & 0xff00);
					} while (--loops > 0);
				}
			}
		}
	}

	public static void drawFlatTriangle(int y1, int y2, int y3, int x1, int x2, int x3, int color) {
		int l1 = 0;
		if (y2 != y1) {
			l1 = (x2 - x1 << 16) / (y2 - y1);
		}
		int i2 = 0;
		if (y3 != y2) {
			i2 = (x3 - x2 << 16) / (y3 - y2);
		}
		int j2 = 0;
		if (y3 != y1) {
			j2 = (x1 - x3 << 16) / (y1 - y3);
		}
		if (y1 <= y2 && y1 <= y3) {
			if (y1 >= Graphics2D.bottomY) {
				return;
			}
			if (y2 > Graphics2D.bottomY) {
				y2 = Graphics2D.bottomY;
			}
			if (y3 > Graphics2D.bottomY) {
				y3 = Graphics2D.bottomY;
			}
			if (y2 < y3) {
				x3 = x1 <<= 16;
				if (y1 < 0) {
					x3 -= j2 * y1;
					x1 -= l1 * y1;
					y1 = 0;
				}
				x2 <<= 16;
				if (y2 < 0) {
					x2 -= i2 * y2;
					y2 = 0;
				}
				if (y1 != y2 && j2 < l1 || y1 == y2 && j2 > i2) {
					y3 -= y2;
					y2 -= y1;
					for (y1 = Rasterizer.lineOffsets[y1]; --y2 >= 0; y1 += Graphics2D.width) {
						Rasterizer.drawScanLine(Graphics2D.pixels, y1, color, x3 >> 16, x1 >> 16);
						x3 += j2;
						x1 += l1;
					}
					while (--y3 >= 0) {
						Rasterizer.drawScanLine(Graphics2D.pixels, y1, color, x3 >> 16, x2 >> 16);
						x3 += j2;
						x2 += i2;
						y1 += Graphics2D.width;
					}
					return;
				}
				y3 -= y2;
				y2 -= y1;
				for (y1 = Rasterizer.lineOffsets[y1]; --y2 >= 0; y1 += Graphics2D.width) {
					Rasterizer.drawScanLine(Graphics2D.pixels, y1, color, x1 >> 16, x3 >> 16);
					x3 += j2;
					x1 += l1;
				}
				while (--y3 >= 0) {
					Rasterizer.drawScanLine(Graphics2D.pixels, y1, color, x2 >> 16, x3 >> 16);
					x3 += j2;
					x2 += i2;
					y1 += Graphics2D.width;
				}
				return;
			}
			x2 = x1 <<= 16;
			if (y1 < 0) {
				x2 -= j2 * y1;
				x1 -= l1 * y1;
				y1 = 0;
			}
			x3 <<= 16;
			if (y3 < 0) {
				x3 -= i2 * y3;
				y3 = 0;
			}
			if (y1 != y3 && j2 < l1 || y1 == y3 && i2 > l1) {
				y2 -= y3;
				y3 -= y1;
				for (y1 = Rasterizer.lineOffsets[y1]; --y3 >= 0; y1 += Graphics2D.width) {
					Rasterizer.drawScanLine(Graphics2D.pixels, y1, color, x2 >> 16, x1 >> 16);
					x2 += j2;
					x1 += l1;
				}
				while (--y2 >= 0) {
					Rasterizer.drawScanLine(Graphics2D.pixels, y1, color, x3 >> 16, x1 >> 16);
					x3 += i2;
					x1 += l1;
					y1 += Graphics2D.width;
				}
				return;
			}
			y2 -= y3;
			y3 -= y1;
			for (y1 = Rasterizer.lineOffsets[y1]; --y3 >= 0; y1 += Graphics2D.width) {
				Rasterizer.drawScanLine(Graphics2D.pixels, y1, color, x1 >> 16, x2 >> 16);
				x2 += j2;
				x1 += l1;
			}
			while (--y2 >= 0) {
				Rasterizer.drawScanLine(Graphics2D.pixels, y1, color, x1 >> 16, x3 >> 16);
				x3 += i2;
				x1 += l1;
				y1 += Graphics2D.width;
			}
			return;
		}
		if (y2 <= y3) {
			if (y2 >= Graphics2D.bottomY) {
				return;
			}
			if (y3 > Graphics2D.bottomY) {
				y3 = Graphics2D.bottomY;
			}
			if (y1 > Graphics2D.bottomY) {
				y1 = Graphics2D.bottomY;
			}
			if (y3 < y1) {
				x1 = x2 <<= 16;
				if (y2 < 0) {
					x1 -= l1 * y2;
					x2 -= i2 * y2;
					y2 = 0;
				}
				x3 <<= 16;
				if (y3 < 0) {
					x3 -= j2 * y3;
					y3 = 0;
				}
				if (y2 != y3 && l1 < i2 || y2 == y3 && l1 > j2) {
					y1 -= y3;
					y3 -= y2;
					for (y2 = Rasterizer.lineOffsets[y2]; --y3 >= 0; y2 += Graphics2D.width) {
						Rasterizer.drawScanLine(Graphics2D.pixels, y2, color, x1 >> 16, x2 >> 16);
						x1 += l1;
						x2 += i2;
					}
					while (--y1 >= 0) {
						Rasterizer.drawScanLine(Graphics2D.pixels, y2, color, x1 >> 16, x3 >> 16);
						x1 += l1;
						x3 += j2;
						y2 += Graphics2D.width;
					}
					return;
				}
				y1 -= y3;
				y3 -= y2;
				for (y2 = Rasterizer.lineOffsets[y2]; --y3 >= 0; y2 += Graphics2D.width) {
					Rasterizer.drawScanLine(Graphics2D.pixels, y2, color, x2 >> 16, x1 >> 16);
					x1 += l1;
					x2 += i2;
				}
				while (--y1 >= 0) {
					Rasterizer.drawScanLine(Graphics2D.pixels, y2, color, x3 >> 16, x1 >> 16);
					x1 += l1;
					x3 += j2;
					y2 += Graphics2D.width;
				}
				return;
			}
			x3 = x2 <<= 16;
			if (y2 < 0) {
				x3 -= l1 * y2;
				x2 -= i2 * y2;
				y2 = 0;
			}
			x1 <<= 16;
			if (y1 < 0) {
				x1 -= j2 * y1;
				y1 = 0;
			}
			if (l1 < i2) {
				y3 -= y1;
				y1 -= y2;
				for (y2 = Rasterizer.lineOffsets[y2]; --y1 >= 0; y2 += Graphics2D.width) {
					Rasterizer.drawScanLine(Graphics2D.pixels, y2, color, x3 >> 16, x2 >> 16);
					x3 += l1;
					x2 += i2;
				}
				while (--y3 >= 0) {
					Rasterizer.drawScanLine(Graphics2D.pixels, y2, color, x1 >> 16, x2 >> 16);
					x1 += j2;
					x2 += i2;
					y2 += Graphics2D.width;
				}
				return;
			}
			y3 -= y1;
			y1 -= y2;
			for (y2 = Rasterizer.lineOffsets[y2]; --y1 >= 0; y2 += Graphics2D.width) {
				Rasterizer.drawScanLine(Graphics2D.pixels, y2, color, x2 >> 16, x3 >> 16);
				x3 += l1;
				x2 += i2;
			}
			while (--y3 >= 0) {
				Rasterizer.drawScanLine(Graphics2D.pixels, y2, color, x2 >> 16, x1 >> 16);
				x1 += j2;
				x2 += i2;
				y2 += Graphics2D.width;
			}
			return;
		}
		if (y3 >= Graphics2D.bottomY) {
			return;
		}
		if (y1 > Graphics2D.bottomY) {
			y1 = Graphics2D.bottomY;
		}
		if (y2 > Graphics2D.bottomY) {
			y2 = Graphics2D.bottomY;
		}
		if (y1 < y2) {
			x2 = x3 <<= 16;
			if (y3 < 0) {
				x2 -= i2 * y3;
				x3 -= j2 * y3;
				y3 = 0;
			}
			x1 <<= 16;
			if (y1 < 0) {
				x1 -= l1 * y1;
				y1 = 0;
			}
			if (i2 < j2) {
				y2 -= y1;
				y1 -= y3;
				for (y3 = Rasterizer.lineOffsets[y3]; --y1 >= 0; y3 += Graphics2D.width) {
					Rasterizer.drawScanLine(Graphics2D.pixels, y3, color, x2 >> 16, x3 >> 16);
					x2 += i2;
					x3 += j2;
				}
				while (--y2 >= 0) {
					Rasterizer.drawScanLine(Graphics2D.pixels, y3, color, x2 >> 16, x1 >> 16);
					x2 += i2;
					x1 += l1;
					y3 += Graphics2D.width;
				}
				return;
			}
			y2 -= y1;
			y1 -= y3;
			for (y3 = Rasterizer.lineOffsets[y3]; --y1 >= 0; y3 += Graphics2D.width) {
				Rasterizer.drawScanLine(Graphics2D.pixels, y3, color, x3 >> 16, x2 >> 16);
				x2 += i2;
				x3 += j2;
			}
			while (--y2 >= 0) {
				Rasterizer.drawScanLine(Graphics2D.pixels, y3, color, x1 >> 16, x2 >> 16);
				x2 += i2;
				x1 += l1;
				y3 += Graphics2D.width;
			}
			return;
		}
		x1 = x3 <<= 16;
		if (y3 < 0) {
			x1 -= i2 * y3;
			x3 -= j2 * y3;
			y3 = 0;
		}
		x2 <<= 16;
		if (y2 < 0) {
			x2 -= l1 * y2;
			y2 = 0;
		}
		if (i2 < j2) {
			y1 -= y2;
			y2 -= y3;
			for (y3 = Rasterizer.lineOffsets[y3]; --y2 >= 0; y3 += Graphics2D.width) {
				Rasterizer.drawScanLine(Graphics2D.pixels, y3, color, x1 >> 16, x3 >> 16);
				x1 += i2;
				x3 += j2;
			}
			while (--y1 >= 0) {
				Rasterizer.drawScanLine(Graphics2D.pixels, y3, color, x2 >> 16, x3 >> 16);
				x2 += l1;
				x3 += j2;
				y3 += Graphics2D.width;
			}
			return;
		}
		y1 -= y2;
		y2 -= y3;
		for (y3 = Rasterizer.lineOffsets[y3]; --y2 >= 0; y3 += Graphics2D.width) {
			Rasterizer.drawScanLine(Graphics2D.pixels, y3, color, x3 >> 16, x1 >> 16);
			x1 += i2;
			x3 += j2;
		}
		while (--y1 >= 0) {
			Rasterizer.drawScanLine(Graphics2D.pixels, y3, color, x3 >> 16, x2 >> 16);
			x2 += l1;
			x3 += j2;
			y3 += Graphics2D.width;
		}
	}

	private static void drawScanLine(int dest[], int destOff, int loops, int startX, int endX) {
		int color;
		if (Rasterizer.edgeRestricted) {
			if (endX > Graphics2D.endX) {
				endX = Graphics2D.endX;
			}
			if (startX < 0) {
				startX = 0;
			}
		}
		if (startX >= endX) {
			return;
		}
		destOff += startX;
		color = endX - startX >> 2;
		if (Rasterizer.alpha == 0) {
			while (--color >= 0) {
				dest[destOff++] = loops;
				dest[destOff++] = loops;
				dest[destOff++] = loops;
				dest[destOff++] = loops;
			}
			for (color = endX - startX & 3; --color >= 0;) {
				dest[destOff++] = loops;
			}
			return;
		}
		int j1 = Rasterizer.alpha;
		int k1 = 256 - Rasterizer.alpha;
		loops = ((loops & 0xff00ff) * k1 >> 8 & 0xff00ff) + ((loops & 0xff00) * k1 >> 8 & 0xff00);
		while (--color >= 0) {
			dest[destOff++] = loops + ((dest[destOff] & 0xff00ff) * j1 >> 8 & 0xff00ff) + ((dest[destOff] & 0xff00) * j1 >> 8 & 0xff00);
			dest[destOff++] = loops + ((dest[destOff] & 0xff00ff) * j1 >> 8 & 0xff00ff) + ((dest[destOff] & 0xff00) * j1 >> 8 & 0xff00);
			dest[destOff++] = loops + ((dest[destOff] & 0xff00ff) * j1 >> 8 & 0xff00ff) + ((dest[destOff] & 0xff00) * j1 >> 8 & 0xff00);
			dest[destOff++] = loops + ((dest[destOff] & 0xff00ff) * j1 >> 8 & 0xff00ff) + ((dest[destOff] & 0xff00) * j1 >> 8 & 0xff00);
		}
		for (color = endX - startX & 3; --color >= 0;) {
			dest[destOff++] = loops + ((dest[destOff] & 0xff00ff) * j1 >> 8 & 0xff00ff) + ((dest[destOff] & 0xff00) * j1 >> 8 & 0xff00);
		}
	}

	public static void drawTexturedTriangle(int y1, int y2, int y3, int x1, int x2, int x3, int grad1, int grad2, int grad3, int tx1, int tx2, int tx3, int ty1, int ty2, int ty3, int tz1, int tz2, int tz3, int textureId) {
		int texture[] = Rasterizer.getTexture(textureId);
		Rasterizer.transparent = !Rasterizer.transparentTextures[textureId];
		tx2 = tx1 - tx2;
		ty2 = ty1 - ty2;
		tz2 = tz1 - tz2;
		tx3 -= tx1;
		ty3 -= ty1;
		tz3 -= tz1;
		int l4 = tx3 * ty1 - ty3 * tx1 << 14;
		int i5 = ty3 * tz1 - tz3 * ty1 << 8;
		int j5 = tz3 * tx1 - tx3 * tz1 << 5;
		int k5 = tx2 * ty1 - ty2 * tx1 << 14;
		int l5 = ty2 * tz1 - tz2 * ty1 << 8;
		int i6 = tz2 * tx1 - tx2 * tz1 << 5;
		int j6 = ty2 * tx3 - tx2 * ty3 << 14;
		int k6 = tz2 * ty3 - ty2 * tz3 << 8;
		int l6 = tx2 * tz3 - tz2 * tx3 << 5;
		int i7 = 0;
		int j7 = 0;
		if (y2 != y1) {
			i7 = (x2 - x1 << 16) / (y2 - y1);
			j7 = (grad2 - grad1 << 16) / (y2 - y1);
		}
		int k7 = 0;
		int l7 = 0;
		if (y3 != y2) {
			k7 = (x3 - x2 << 16) / (y3 - y2);
			l7 = (grad3 - grad2 << 16) / (y3 - y2);
		}
		int i8 = 0;
		int j8 = 0;
		if (y3 != y1) {
			i8 = (x1 - x3 << 16) / (y1 - y3);
			j8 = (grad1 - grad3 << 16) / (y1 - y3);
		}
		if (y1 <= y2 && y1 <= y3) {
			if (y1 >= Graphics2D.bottomY) {
				return;
			}
			if (y2 > Graphics2D.bottomY) {
				y2 = Graphics2D.bottomY;
			}
			if (y3 > Graphics2D.bottomY) {
				y3 = Graphics2D.bottomY;
			}
			if (y2 < y3) {
				x3 = x1 <<= 16;
				grad3 = grad1 <<= 16;
				if (y1 < 0) {
					x3 -= i8 * y1;
					x1 -= i7 * y1;
					grad3 -= j8 * y1;
					grad1 -= j7 * y1;
					y1 = 0;
				}
				x2 <<= 16;
				grad2 <<= 16;
				if (y2 < 0) {
					x2 -= k7 * y2;
					grad2 -= l7 * y2;
					y2 = 0;
				}
				int k8 = y1 - Rasterizer.centerY;
				l4 += j5 * k8;
				k5 += i6 * k8;
				j6 += l6 * k8;
				if (y1 != y2 && i8 < i7 || y1 == y2 && i8 > k7) {
					y3 -= y2;
					y2 -= y1;
					y1 = Rasterizer.lineOffsets[y1];
					while (--y2 >= 0) {
						Rasterizer.drawTexturedLine(Graphics2D.pixels, texture, y1, x3 >> 16, x1 >> 16, grad3 >> 8, grad1 >> 8, l4, k5, j6, i5, l5, k6);
						x3 += i8;
						x1 += i7;
						grad3 += j8;
						grad1 += j7;
						y1 += Graphics2D.width;
						l4 += j5;
						k5 += i6;
						j6 += l6;
					}
					while (--y3 >= 0) {
						Rasterizer.drawTexturedLine(Graphics2D.pixels, texture, y1, x3 >> 16, x2 >> 16, grad3 >> 8, grad2 >> 8, l4, k5, j6, i5, l5, k6);
						x3 += i8;
						x2 += k7;
						grad3 += j8;
						grad2 += l7;
						y1 += Graphics2D.width;
						l4 += j5;
						k5 += i6;
						j6 += l6;
					}
					return;
				}
				y3 -= y2;
				y2 -= y1;
				y1 = Rasterizer.lineOffsets[y1];
				while (--y2 >= 0) {
					Rasterizer.drawTexturedLine(Graphics2D.pixels, texture, y1, x1 >> 16, x3 >> 16, grad1 >> 8, grad3 >> 8, l4, k5, j6, i5, l5, k6);
					x3 += i8;
					x1 += i7;
					grad3 += j8;
					grad1 += j7;
					y1 += Graphics2D.width;
					l4 += j5;
					k5 += i6;
					j6 += l6;
				}
				while (--y3 >= 0) {
					Rasterizer.drawTexturedLine(Graphics2D.pixels, texture, y1, x2 >> 16, x3 >> 16, grad2 >> 8, grad3 >> 8, l4, k5, j6, i5, l5, k6);
					x3 += i8;
					x2 += k7;
					grad3 += j8;
					grad2 += l7;
					y1 += Graphics2D.width;
					l4 += j5;
					k5 += i6;
					j6 += l6;
				}
				return;
			}
			x2 = x1 <<= 16;
			grad2 = grad1 <<= 16;
			if (y1 < 0) {
				x2 -= i8 * y1;
				x1 -= i7 * y1;
				grad2 -= j8 * y1;
				grad1 -= j7 * y1;
				y1 = 0;
			}
			x3 <<= 16;
			grad3 <<= 16;
			if (y3 < 0) {
				x3 -= k7 * y3;
				grad3 -= l7 * y3;
				y3 = 0;
			}
			int l8 = y1 - Rasterizer.centerY;
			l4 += j5 * l8;
			k5 += i6 * l8;
			j6 += l6 * l8;
			if (y1 != y3 && i8 < i7 || y1 == y3 && k7 > i7) {
				y2 -= y3;
				y3 -= y1;
				y1 = Rasterizer.lineOffsets[y1];
				while (--y3 >= 0) {
					Rasterizer.drawTexturedLine(Graphics2D.pixels, texture, y1, x2 >> 16, x1 >> 16, grad2 >> 8, grad1 >> 8, l4, k5, j6, i5, l5, k6);
					x2 += i8;
					x1 += i7;
					grad2 += j8;
					grad1 += j7;
					y1 += Graphics2D.width;
					l4 += j5;
					k5 += i6;
					j6 += l6;
				}
				while (--y2 >= 0) {
					Rasterizer.drawTexturedLine(Graphics2D.pixels, texture, y1, x3 >> 16, x1 >> 16, grad3 >> 8, grad1 >> 8, l4, k5, j6, i5, l5, k6);
					x3 += k7;
					x1 += i7;
					grad3 += l7;
					grad1 += j7;
					y1 += Graphics2D.width;
					l4 += j5;
					k5 += i6;
					j6 += l6;
				}
				return;
			}
			y2 -= y3;
			y3 -= y1;
			y1 = Rasterizer.lineOffsets[y1];
			while (--y3 >= 0) {
				Rasterizer.drawTexturedLine(Graphics2D.pixels, texture, y1, x1 >> 16, x2 >> 16, grad1 >> 8, grad2 >> 8, l4, k5, j6, i5, l5, k6);
				x2 += i8;
				x1 += i7;
				grad2 += j8;
				grad1 += j7;
				y1 += Graphics2D.width;
				l4 += j5;
				k5 += i6;
				j6 += l6;
			}
			while (--y2 >= 0) {
				Rasterizer.drawTexturedLine(Graphics2D.pixels, texture, y1, x1 >> 16, x3 >> 16, grad1 >> 8, grad3 >> 8, l4, k5, j6, i5, l5, k6);
				x3 += k7;
				x1 += i7;
				grad3 += l7;
				grad1 += j7;
				y1 += Graphics2D.width;
				l4 += j5;
				k5 += i6;
				j6 += l6;
			}
			return;
		}
		if (y2 <= y3) {
			if (y2 >= Graphics2D.bottomY) {
				return;
			}
			if (y3 > Graphics2D.bottomY) {
				y3 = Graphics2D.bottomY;
			}
			if (y1 > Graphics2D.bottomY) {
				y1 = Graphics2D.bottomY;
			}
			if (y3 < y1) {
				x1 = x2 <<= 16;
				grad1 = grad2 <<= 16;
				if (y2 < 0) {
					x1 -= i7 * y2;
					x2 -= k7 * y2;
					grad1 -= j7 * y2;
					grad2 -= l7 * y2;
					y2 = 0;
				}
				x3 <<= 16;
				grad3 <<= 16;
				if (y3 < 0) {
					x3 -= i8 * y3;
					grad3 -= j8 * y3;
					y3 = 0;
				}
				int i9 = y2 - Rasterizer.centerY;
				l4 += j5 * i9;
				k5 += i6 * i9;
				j6 += l6 * i9;
				if (y2 != y3 && i7 < k7 || y2 == y3 && i7 > i8) {
					y1 -= y3;
					y3 -= y2;
					y2 = Rasterizer.lineOffsets[y2];
					while (--y3 >= 0) {
						Rasterizer.drawTexturedLine(Graphics2D.pixels, texture, y2, x1 >> 16, x2 >> 16, grad1 >> 8, grad2 >> 8, l4, k5, j6, i5, l5, k6);
						x1 += i7;
						x2 += k7;
						grad1 += j7;
						grad2 += l7;
						y2 += Graphics2D.width;
						l4 += j5;
						k5 += i6;
						j6 += l6;
					}
					while (--y1 >= 0) {
						Rasterizer.drawTexturedLine(Graphics2D.pixels, texture, y2, x1 >> 16, x3 >> 16, grad1 >> 8, grad3 >> 8, l4, k5, j6, i5, l5, k6);
						x1 += i7;
						x3 += i8;
						grad1 += j7;
						grad3 += j8;
						y2 += Graphics2D.width;
						l4 += j5;
						k5 += i6;
						j6 += l6;
					}
					return;
				}
				y1 -= y3;
				y3 -= y2;
				y2 = Rasterizer.lineOffsets[y2];
				while (--y3 >= 0) {
					Rasterizer.drawTexturedLine(Graphics2D.pixels, texture, y2, x2 >> 16, x1 >> 16, grad2 >> 8, grad1 >> 8, l4, k5, j6, i5, l5, k6);
					x1 += i7;
					x2 += k7;
					grad1 += j7;
					grad2 += l7;
					y2 += Graphics2D.width;
					l4 += j5;
					k5 += i6;
					j6 += l6;
				}
				while (--y1 >= 0) {
					Rasterizer.drawTexturedLine(Graphics2D.pixels, texture, y2, x3 >> 16, x1 >> 16, grad3 >> 8, grad1 >> 8, l4, k5, j6, i5, l5, k6);
					x1 += i7;
					x3 += i8;
					grad1 += j7;
					grad3 += j8;
					y2 += Graphics2D.width;
					l4 += j5;
					k5 += i6;
					j6 += l6;
				}
				return;
			}
			x3 = x2 <<= 16;
			grad3 = grad2 <<= 16;
			if (y2 < 0) {
				x3 -= i7 * y2;
				x2 -= k7 * y2;
				grad3 -= j7 * y2;
				grad2 -= l7 * y2;
				y2 = 0;
			}
			x1 <<= 16;
			grad1 <<= 16;
			if (y1 < 0) {
				x1 -= i8 * y1;
				grad1 -= j8 * y1;
				y1 = 0;
			}
			int j9 = y2 - Rasterizer.centerY;
			l4 += j5 * j9;
			k5 += i6 * j9;
			j6 += l6 * j9;
			if (i7 < k7) {
				y3 -= y1;
				y1 -= y2;
				y2 = Rasterizer.lineOffsets[y2];
				while (--y1 >= 0) {
					Rasterizer.drawTexturedLine(Graphics2D.pixels, texture, y2, x3 >> 16, x2 >> 16, grad3 >> 8, grad2 >> 8, l4, k5, j6, i5, l5, k6);
					x3 += i7;
					x2 += k7;
					grad3 += j7;
					grad2 += l7;
					y2 += Graphics2D.width;
					l4 += j5;
					k5 += i6;
					j6 += l6;
				}
				while (--y3 >= 0) {
					Rasterizer.drawTexturedLine(Graphics2D.pixels, texture, y2, x1 >> 16, x2 >> 16, grad1 >> 8, grad2 >> 8, l4, k5, j6, i5, l5, k6);
					x1 += i8;
					x2 += k7;
					grad1 += j8;
					grad2 += l7;
					y2 += Graphics2D.width;
					l4 += j5;
					k5 += i6;
					j6 += l6;
				}
				return;
			}
			y3 -= y1;
			y1 -= y2;
			y2 = Rasterizer.lineOffsets[y2];
			while (--y1 >= 0) {
				Rasterizer.drawTexturedLine(Graphics2D.pixels, texture, y2, x2 >> 16, x3 >> 16, grad2 >> 8, grad3 >> 8, l4, k5, j6, i5, l5, k6);
				x3 += i7;
				x2 += k7;
				grad3 += j7;
				grad2 += l7;
				y2 += Graphics2D.width;
				l4 += j5;
				k5 += i6;
				j6 += l6;
			}
			while (--y3 >= 0) {
				Rasterizer.drawTexturedLine(Graphics2D.pixels, texture, y2, x2 >> 16, x1 >> 16, grad2 >> 8, grad1 >> 8, l4, k5, j6, i5, l5, k6);
				x1 += i8;
				x2 += k7;
				grad1 += j8;
				grad2 += l7;
				y2 += Graphics2D.width;
				l4 += j5;
				k5 += i6;
				j6 += l6;
			}
			return;
		}
		if (y3 >= Graphics2D.bottomY) {
			return;
		}
		if (y1 > Graphics2D.bottomY) {
			y1 = Graphics2D.bottomY;
		}
		if (y2 > Graphics2D.bottomY) {
			y2 = Graphics2D.bottomY;
		}
		if (y1 < y2) {
			x2 = x3 <<= 16;
			grad2 = grad3 <<= 16;
			if (y3 < 0) {
				x2 -= k7 * y3;
				x3 -= i8 * y3;
				grad2 -= l7 * y3;
				grad3 -= j8 * y3;
				y3 = 0;
			}
			x1 <<= 16;
			grad1 <<= 16;
			if (y1 < 0) {
				x1 -= i7 * y1;
				grad1 -= j7 * y1;
				y1 = 0;
			}
			int k9 = y3 - Rasterizer.centerY;
			l4 += j5 * k9;
			k5 += i6 * k9;
			j6 += l6 * k9;
			if (k7 < i8) {
				y2 -= y1;
				y1 -= y3;
				y3 = Rasterizer.lineOffsets[y3];
				while (--y1 >= 0) {
					Rasterizer.drawTexturedLine(Graphics2D.pixels, texture, y3, x2 >> 16, x3 >> 16, grad2 >> 8, grad3 >> 8, l4, k5, j6, i5, l5, k6);
					x2 += k7;
					x3 += i8;
					grad2 += l7;
					grad3 += j8;
					y3 += Graphics2D.width;
					l4 += j5;
					k5 += i6;
					j6 += l6;
				}
				while (--y2 >= 0) {
					Rasterizer.drawTexturedLine(Graphics2D.pixels, texture, y3, x2 >> 16, x1 >> 16, grad2 >> 8, grad1 >> 8, l4, k5, j6, i5, l5, k6);
					x2 += k7;
					x1 += i7;
					grad2 += l7;
					grad1 += j7;
					y3 += Graphics2D.width;
					l4 += j5;
					k5 += i6;
					j6 += l6;
				}
				return;
			}
			y2 -= y1;
			y1 -= y3;
			y3 = Rasterizer.lineOffsets[y3];
			while (--y1 >= 0) {
				Rasterizer.drawTexturedLine(Graphics2D.pixels, texture, y3, x3 >> 16, x2 >> 16, grad3 >> 8, grad2 >> 8, l4, k5, j6, i5, l5, k6);
				x2 += k7;
				x3 += i8;
				grad2 += l7;
				grad3 += j8;
				y3 += Graphics2D.width;
				l4 += j5;
				k5 += i6;
				j6 += l6;
			}
			while (--y2 >= 0) {
				Rasterizer.drawTexturedLine(Graphics2D.pixels, texture, y3, x1 >> 16, x2 >> 16, grad1 >> 8, grad2 >> 8, l4, k5, j6, i5, l5, k6);
				x2 += k7;
				x1 += i7;
				grad2 += l7;
				grad1 += j7;
				y3 += Graphics2D.width;
				l4 += j5;
				k5 += i6;
				j6 += l6;
			}
			return;
		}
		x1 = x3 <<= 16;
		grad1 = grad3 <<= 16;
		if (y3 < 0) {
			x1 -= k7 * y3;
			x3 -= i8 * y3;
			grad1 -= l7 * y3;
			grad3 -= j8 * y3;
			y3 = 0;
		}
		x2 <<= 16;
		grad2 <<= 16;
		if (y2 < 0) {
			x2 -= i7 * y2;
			grad2 -= j7 * y2;
			y2 = 0;
		}
		int l9 = y3 - Rasterizer.centerY;
		l4 += j5 * l9;
		k5 += i6 * l9;
		j6 += l6 * l9;
		if (k7 < i8) {
			y1 -= y2;
			y2 -= y3;
			y3 = Rasterizer.lineOffsets[y3];
			while (--y2 >= 0) {
				Rasterizer.drawTexturedLine(Graphics2D.pixels, texture, y3, x1 >> 16, x3 >> 16, grad1 >> 8, grad3 >> 8, l4, k5, j6, i5, l5, k6);
				x1 += k7;
				x3 += i8;
				grad1 += l7;
				grad3 += j8;
				y3 += Graphics2D.width;
				l4 += j5;
				k5 += i6;
				j6 += l6;
			}
			while (--y1 >= 0) {
				Rasterizer.drawTexturedLine(Graphics2D.pixels, texture, y3, x2 >> 16, x3 >> 16, grad2 >> 8, grad3 >> 8, l4, k5, j6, i5, l5, k6);
				x2 += i7;
				x3 += i8;
				grad2 += j7;
				grad3 += j8;
				y3 += Graphics2D.width;
				l4 += j5;
				k5 += i6;
				j6 += l6;
			}
			return;
		}
		y1 -= y2;
		y2 -= y3;
		y3 = Rasterizer.lineOffsets[y3];
		while (--y2 >= 0) {
			Rasterizer.drawTexturedLine(Graphics2D.pixels, texture, y3, x3 >> 16, x1 >> 16, grad3 >> 8, grad1 >> 8, l4, k5, j6, i5, l5, k6);
			x1 += k7;
			x3 += i8;
			grad1 += l7;
			grad3 += j8;
			y3 += Graphics2D.width;
			l4 += j5;
			k5 += i6;
			j6 += l6;
		}
		while (--y1 >= 0) {
			Rasterizer.drawTexturedLine(Graphics2D.pixels, texture, y3, x3 >> 16, x2 >> 16, grad3 >> 8, grad2 >> 8, l4, k5, j6, i5, l5, k6);
			x2 += i7;
			x3 += i8;
			grad2 += j7;
			grad3 += j8;
			y3 += Graphics2D.width;
			l4 += j5;
			k5 += i6;
			j6 += l6;
		}
	}

	private static void drawTexturedLine(int dest[], int texture[], int destOff, int startX, int endX, int colorIndex, int gradient, int arg7, int arg8, int arg9, int arg10, int arg11, int arg12) {
		int alpha = 0;
		int loops = 0;
		if (startX >= endX) {
			return;
		}
		int j3;
		int k3;
		if (Rasterizer.edgeRestricted) {
			j3 = (gradient - colorIndex) / (endX - startX);
			if (endX > Graphics2D.endX) {
				endX = Graphics2D.endX;
			}
			if (startX < 0) {
				colorIndex -= startX * j3;
				startX = 0;
			}
			if (startX >= endX) {
				return;
			}
			k3 = endX - startX >> 3;
			j3 <<= 12;
			colorIndex <<= 9;
		} else {
			if (endX - startX > 7) {
				k3 = endX - startX >> 3;
				j3 = (gradient - colorIndex) * Rasterizer.gradientFactors[k3] >> 6;
			} else {
				k3 = 0;
				j3 = 0;
			}
			colorIndex <<= 9;
		}
		destOff += startX;
		if (Rasterizer.lowMem) {
			int i4 = 0;
			int k4 = 0;
			int k6 = startX - Rasterizer.centerX;
			arg7 += (arg10 >> 3) * k6;
			arg8 += (arg11 >> 3) * k6;
			arg9 += (arg12 >> 3) * k6;
			int i5 = arg9 >> 12;
			if (i5 != 0) {
				alpha = arg7 / i5;
				loops = arg8 / i5;
				if (alpha < 0) {
					alpha = 0;
				} else if (alpha > 4032) {
					alpha = 4032;
				}
			}
			arg7 += arg10;
			arg8 += arg11;
			arg9 += arg12;
			i5 = arg9 >> 12;
			if (i5 != 0) {
				i4 = arg7 / i5;
				k4 = arg8 / i5;
				if (i4 < 7) {
					i4 = 7;
				} else if (i4 > 4032) {
					i4 = 4032;
				}
			}
			int i7 = i4 - alpha >> 3;
			int k7 = k4 - loops >> 3;
			alpha += (colorIndex & 0x600000) >> 3;
			int i8 = colorIndex >> 23;
			if (Rasterizer.transparent) {
				while (k3-- > 0) {
					dest[destOff++] = texture[(loops & 0xfc0) + (alpha >> 6)] >>> i8;
					alpha += i7;
					loops += k7;
					dest[destOff++] = texture[(loops & 0xfc0) + (alpha >> 6)] >>> i8;
					alpha += i7;
					loops += k7;
					dest[destOff++] = texture[(loops & 0xfc0) + (alpha >> 6)] >>> i8;
					alpha += i7;
					loops += k7;
					dest[destOff++] = texture[(loops & 0xfc0) + (alpha >> 6)] >>> i8;
					alpha += i7;
					loops += k7;
					dest[destOff++] = texture[(loops & 0xfc0) + (alpha >> 6)] >>> i8;
					alpha += i7;
					loops += k7;
					dest[destOff++] = texture[(loops & 0xfc0) + (alpha >> 6)] >>> i8;
					alpha += i7;
					loops += k7;
					dest[destOff++] = texture[(loops & 0xfc0) + (alpha >> 6)] >>> i8;
					alpha += i7;
					loops += k7;
					dest[destOff++] = texture[(loops & 0xfc0) + (alpha >> 6)] >>> i8;
					alpha = i4;
					loops = k4;
					arg7 += arg10;
					arg8 += arg11;
					arg9 += arg12;
					int j5 = arg9 >> 12;
					if (j5 != 0) {
						i4 = arg7 / j5;
						k4 = arg8 / j5;
						if (i4 < 7) {
							i4 = 7;
						} else if (i4 > 4032) {
							i4 = 4032;
						}
					}
					i7 = i4 - alpha >> 3;
					k7 = k4 - loops >> 3;
					colorIndex += j3;
					alpha += (colorIndex & 0x600000) >> 3;
					i8 = colorIndex >> 23;
				}
				for (k3 = endX - startX & 7; k3-- > 0;) {
					dest[destOff++] = texture[(loops & 0xfc0) + (alpha >> 6)] >>> i8;
					alpha += i7;
					loops += k7;
				}
				return;
			}
			while (k3-- > 0) {
				int k8;
				if ((k8 = texture[(loops & 0xfc0) + (alpha >> 6)] >>> i8) != 0) {
					dest[destOff] = k8;
				}
				destOff++;
				alpha += i7;
				loops += k7;
				if ((k8 = texture[(loops & 0xfc0) + (alpha >> 6)] >>> i8) != 0) {
					dest[destOff] = k8;
				}
				destOff++;
				alpha += i7;
				loops += k7;
				if ((k8 = texture[(loops & 0xfc0) + (alpha >> 6)] >>> i8) != 0) {
					dest[destOff] = k8;
				}
				destOff++;
				alpha += i7;
				loops += k7;
				if ((k8 = texture[(loops & 0xfc0) + (alpha >> 6)] >>> i8) != 0) {
					dest[destOff] = k8;
				}
				destOff++;
				alpha += i7;
				loops += k7;
				if ((k8 = texture[(loops & 0xfc0) + (alpha >> 6)] >>> i8) != 0) {
					dest[destOff] = k8;
				}
				destOff++;
				alpha += i7;
				loops += k7;
				if ((k8 = texture[(loops & 0xfc0) + (alpha >> 6)] >>> i8) != 0) {
					dest[destOff] = k8;
				}
				destOff++;
				alpha += i7;
				loops += k7;
				if ((k8 = texture[(loops & 0xfc0) + (alpha >> 6)] >>> i8) != 0) {
					dest[destOff] = k8;
				}
				destOff++;
				alpha += i7;
				loops += k7;
				if ((k8 = texture[(loops & 0xfc0) + (alpha >> 6)] >>> i8) != 0) {
					dest[destOff] = k8;
				}
				destOff++;
				alpha = i4;
				loops = k4;
				arg7 += arg10;
				arg8 += arg11;
				arg9 += arg12;
				int k5 = arg9 >> 12;
				if (k5 != 0) {
					i4 = arg7 / k5;
					k4 = arg8 / k5;
					if (i4 < 7) {
						i4 = 7;
					} else if (i4 > 4032) {
						i4 = 4032;
					}
				}
				i7 = i4 - alpha >> 3;
				k7 = k4 - loops >> 3;
				colorIndex += j3;
				alpha += (colorIndex & 0x600000) >> 3;
				i8 = colorIndex >> 23;
			}
			for (k3 = endX - startX & 7; k3-- > 0;) {
				int l8;
				if ((l8 = texture[(loops & 0xfc0) + (alpha >> 6)] >>> i8) != 0) {
					dest[destOff] = l8;
				}
				destOff++;
				alpha += i7;
				loops += k7;
			}
			return;
		}
		int j4 = 0;
		int l4 = 0;
		int l6 = startX - Rasterizer.centerX;
		arg7 += (arg10 >> 3) * l6;
		arg8 += (arg11 >> 3) * l6;
		arg9 += (arg12 >> 3) * l6;
		int l5 = arg9 >> 14;
		if (l5 != 0) {
			alpha = arg7 / l5;
			loops = arg8 / l5;
			if (alpha < 0) {
				alpha = 0;
			} else if (alpha > 16256) {
				alpha = 16256;
			}
		}
		arg7 += arg10;
		arg8 += arg11;
		arg9 += arg12;
		l5 = arg9 >> 14;
		if (l5 != 0) {
			j4 = arg7 / l5;
			l4 = arg8 / l5;
			if (j4 < 7) {
				j4 = 7;
			} else if (j4 > 16256) {
				j4 = 16256;
			}
		}
		int j7 = j4 - alpha >> 3;
		int l7 = l4 - loops >> 3;
		alpha += colorIndex & 0x600000;
		int j8 = colorIndex >> 23;
		if (Rasterizer.transparent) {
			while (k3-- > 0) {
				dest[destOff++] = texture[(loops & 0x3f80) + (alpha >> 7)] >>> j8;
				alpha += j7;
				loops += l7;
				dest[destOff++] = texture[(loops & 0x3f80) + (alpha >> 7)] >>> j8;
				alpha += j7;
				loops += l7;
				dest[destOff++] = texture[(loops & 0x3f80) + (alpha >> 7)] >>> j8;
				alpha += j7;
				loops += l7;
				dest[destOff++] = texture[(loops & 0x3f80) + (alpha >> 7)] >>> j8;
				alpha += j7;
				loops += l7;
				dest[destOff++] = texture[(loops & 0x3f80) + (alpha >> 7)] >>> j8;
				alpha += j7;
				loops += l7;
				dest[destOff++] = texture[(loops & 0x3f80) + (alpha >> 7)] >>> j8;
				alpha += j7;
				loops += l7;
				dest[destOff++] = texture[(loops & 0x3f80) + (alpha >> 7)] >>> j8;
				alpha += j7;
				loops += l7;
				dest[destOff++] = texture[(loops & 0x3f80) + (alpha >> 7)] >>> j8;
				alpha = j4;
				loops = l4;
				arg7 += arg10;
				arg8 += arg11;
				arg9 += arg12;
				int i6 = arg9 >> 14;
				if (i6 != 0) {
					j4 = arg7 / i6;
					l4 = arg8 / i6;
					if (j4 < 7) {
						j4 = 7;
					} else if (j4 > 16256) {
						j4 = 16256;
					}
				}
				j7 = j4 - alpha >> 3;
				l7 = l4 - loops >> 3;
				colorIndex += j3;
				alpha += colorIndex & 0x600000;
				j8 = colorIndex >> 23;
			}
			for (k3 = endX - startX & 7; k3-- > 0;) {
				dest[destOff++] = texture[(loops & 0x3f80) + (alpha >> 7)] >>> j8;
				alpha += j7;
				loops += l7;
			}
			return;
		}
		while (k3-- > 0) {
			int i9;
			if ((i9 = texture[(loops & 0x3f80) + (alpha >> 7)] >>> j8) != 0) {
				dest[destOff] = i9;
			}
			destOff++;
			alpha += j7;
			loops += l7;
			if ((i9 = texture[(loops & 0x3f80) + (alpha >> 7)] >>> j8) != 0) {
				dest[destOff] = i9;
			}
			destOff++;
			alpha += j7;
			loops += l7;
			if ((i9 = texture[(loops & 0x3f80) + (alpha >> 7)] >>> j8) != 0) {
				dest[destOff] = i9;
			}
			destOff++;
			alpha += j7;
			loops += l7;
			if ((i9 = texture[(loops & 0x3f80) + (alpha >> 7)] >>> j8) != 0) {
				dest[destOff] = i9;
			}
			destOff++;
			alpha += j7;
			loops += l7;
			if ((i9 = texture[(loops & 0x3f80) + (alpha >> 7)] >>> j8) != 0) {
				dest[destOff] = i9;
			}
			destOff++;
			alpha += j7;
			loops += l7;
			if ((i9 = texture[(loops & 0x3f80) + (alpha >> 7)] >>> j8) != 0) {
				dest[destOff] = i9;
			}
			destOff++;
			alpha += j7;
			loops += l7;
			if ((i9 = texture[(loops & 0x3f80) + (alpha >> 7)] >>> j8) != 0) {
				dest[destOff] = i9;
			}
			destOff++;
			alpha += j7;
			loops += l7;
			if ((i9 = texture[(loops & 0x3f80) + (alpha >> 7)] >>> j8) != 0) {
				dest[destOff] = i9;
			}
			destOff++;
			alpha = j4;
			loops = l4;
			arg7 += arg10;
			arg8 += arg11;
			arg9 += arg12;
			int j6 = arg9 >> 14;
			if (j6 != 0) {
				j4 = arg7 / j6;
				l4 = arg8 / j6;
				if (j4 < 7) {
					j4 = 7;
				} else if (j4 > 16256) {
					j4 = 16256;
				}
			}
			j7 = j4 - alpha >> 3;
			l7 = l4 - loops >> 3;
			colorIndex += j3;
			alpha += colorIndex & 0x600000;
			j8 = colorIndex >> 23;
		}
		for (int l3 = endX - startX & 7; l3-- > 0;) {
			int j9;
			if ((j9 = texture[(loops & 0x3f80) + (alpha >> 7)] >>> j8) != 0) {
				dest[destOff] = j9;
			}
			destOff++;
			alpha += j7;
			loops += l7;
		}
	}
	public static boolean lowMem = true;
	static boolean edgeRestricted; // aBoolean1462
	private static boolean transparent; // aBoolean1463
	public static boolean colorRestricted = true;
	public static int alpha; // anInt1465
	public static int centerX; // textureInt1
	public static int centerY; // textureInt2
	private static int[] gradientFactors; // anIntArray1468
	public static final int[] anIntArray1469;
	public static int sineTable[]; // anIntArray1470[]
	public static int cosineTable[]; // anIntArray1471[]
	public static int lineOffsets[];
	private static int textureCount; // anInt1473
	public static IndexedSprite textures[] = new IndexedSprite[50]; // anIndexedSpriteArray1474[]
	private static boolean[] transparentTextures = new boolean[50]; // aBooleanArray1475
	private static int[] averageTextureColors = new int[50]; // anIntArray1476
	private static int textureStoreCount; // anInt1477
	private static int[][] textureStore; // anIntArrayArray1478
	private static int[][] textureCache = new int[50][]; // anIntArrayArray1479
	public static int texturePriorities[] = new int[50]; // anIntArray1480[]
	public static int texturePriority;
	public static int palette[] = new int[0x10000]; // anIntArray1482[]
	private static int[][] texturePixels = new int[50][]; // anIntArrayArray1483
	static {
		Rasterizer.gradientFactors = new int[512];
		anIntArray1469 = new int[2048];
		Rasterizer.sineTable = new int[2048];
		Rasterizer.cosineTable = new int[2048];
		for (int i = 1; i < 512; i++) {
			Rasterizer.gradientFactors[i] = 32768 / i;
		}
		for (int i = 1; i < 2048; i++) {
			Rasterizer.anIntArray1469[i] = 0x10000 / i;
		}
		for (int i = 0; i < 2048; i++) {
			Rasterizer.sineTable[i] = (int) (65536D * Math.sin(i * 0.0030679614999999999D));
			Rasterizer.cosineTable[i] = (int) (65536D * Math.cos(i * 0.0030679614999999999D));
		}
	}
}
