/**
 * IndexedSprite [Background]
 * Represents an image with color restriction.
 * Jagex's old cache format indexes the images that it contains.
 */
public final class IndexedSprite extends Graphics2D {
	public IndexedSprite(Archive container, String name, int entry) {
		Stream image = new Stream(container.get(name + ".dat"));
		Stream index = new Stream(container.get("index.dat"));
		index.offset = image.getUnsignedShort();
		trimWidth = index.getUnsignedShort();
		trimHeight = index.getUnsignedShort();
		int pallete_size = index.getUnsignedByte();
		pallete = new int[pallete_size];
		for (int i = 0; i < pallete_size - 1; i++) {
			pallete[i + 1] = index.get24BitInt();
		}
		for (int i = 0; i < entry; i++) {
			index.offset += 2;
			image.offset += index.getUnsignedShort() * index.getUnsignedShort();
			index.offset++;
		}
		offsetX = index.getUnsignedByte();
		offsetY = index.getUnsignedByte();
		width = index.getUnsignedShort();
		height = index.getUnsignedShort();
		int imageType = index.getUnsignedByte();
		int pixelCount = width * height;
		pixels = new byte[pixelCount];
		if (imageType == 0) {
			for (int i = 0; i < pixelCount; i++) {
				pixels[i] = image.getByte();
			}
			return;
		}
		if (imageType == 1) {
			for (int offX = 0; offX < width; offX++) {
				for (int offY = 0; offY < height; offY++) {
					pixels[offX + offY * width] = image.getByte();
				}
			}
		}
	}

	public void shrink() {
		trimWidth /= 2;
		trimHeight /= 2;
		byte image[] = new byte[trimWidth * trimHeight];
		int off = 0;
		for (int offY = 0; offY < height; offY++) {
			for (int offX = 0; offX < width; offX++) {
				image[(offX + offsetX >> 1) + (offY + offsetY >> 1) * trimWidth] = pixels[off++];
			}
		}
		pixels = image;
		width = trimWidth;
		height = trimHeight;
		offsetX = 0;
		offsetY = 0;
	}

	public void resize() {
		if (width == trimWidth && height == trimHeight) {
			return;
		}
		byte image[] = new byte[trimWidth * trimHeight];
		int off = 0;
		for (int offY = 0; offY < height; offY++) {
			for (int offX = 0; offX < width; offX++) {
				image[offX + offsetX + (offY + offsetY) * trimWidth] = pixels[off++];
			}
		}
		pixels = image;
		width = trimWidth;
		height = trimHeight;
		offsetX = 0;
		offsetY = 0;
	}

	public void flipHorizontal() {
		byte image[] = new byte[width * height];
		int off = 0;
		for (int offY = 0; offY < height; offY++) {
			for (int offX = width - 1; offX >= 0; offX--) {
				image[off++] = pixels[offX + offY * width];
			}
		}
		pixels = image;
		offsetX = trimWidth - width - offsetX;
	}

	public void flipVertical() {
		byte image[] = new byte[width * height];
		int off = 0;
		for (int j = height - 1; j >= 0; j--) {
			for (int k = 0; k < width; k++) {
				image[off++] = pixels[k + j * width];
			}
		}
		pixels = image;
		offsetY = trimHeight - height - offsetY;
	}

	public void adjustColors(int redOff, int greenOff, int blueOff) {
		for (int i = 0; i < pallete.length; i++) {
			int red = pallete[i] >> 16 & 0xff;
			red += redOff;
			if (red < 0) {
				red = 0;
			} else if (red > 255) {
				red = 255;
			}
			int green = pallete[i] >> 8 & 0xff;
			green += greenOff;
			if (green < 0) {
				green = 0;
			} else if (green > 255) {
				green = 255;
			}
			int blue = pallete[i] & 0xff;
			blue += blueOff;
			if (blue < 0) {
				blue = 0;
			} else if (blue > 255) {
				blue = 255;
			}
			pallete[i] = (red << 16) + (green << 8) + blue;
		}
	}

	public void drawIndexedSprite(int x, int y) {
		x += offsetX;
		y += offsetY;
		int destOff = x + y * Graphics2D.width;
		int srcOff = 0;
		int height = this.height;
		int width = this.width;
		int destStep = Graphics2D.width - width;
		int srcStep = 0;
		if (y < Graphics2D.topY) {
			int topMargin = Graphics2D.topY - y;
			height -= topMargin;
			y = Graphics2D.topY;
			srcOff += topMargin * width;
			destOff += topMargin * Graphics2D.width;
		}
		if (y + height > Graphics2D.bottomY) {
			height -= y + height - Graphics2D.bottomY;
		}
		if (x < Graphics2D.topX) {
			int leftMargin = Graphics2D.topX - x;
			width -= leftMargin;
			x = Graphics2D.topX;
			srcOff += leftMargin;
			destOff += leftMargin;
			srcStep += leftMargin;
			destStep += leftMargin;
		}
		if (x + width > Graphics2D.bottomX) {
			int rightMargin = x + width - Graphics2D.bottomX;
			width -= rightMargin;
			srcStep += rightMargin;
			destStep += rightMargin;
		}
		if (!(width <= 0 || height <= 0)) {
			pushPixels(height, Graphics2D.pixels, pixels, destStep, destOff, width, srcOff,
				pallete, srcStep);
		}
	}

	private void pushPixels(int height, int dest[], byte src[], int destStep, int destOff, int width, int srcOff, int palette[],
		int srcStep) {
		int quarterX = -(width >> 2);
		width = -(width & 3);
		for (int i = -height; i < 0; i++) {
			for (int j = quarterX; j < 0; j++) {
				byte color = src[srcOff++];
				if (color != 0) {
					dest[destOff++] = palette[color & 0xff];
				} else {
					destOff++;
				}
				color = src[srcOff++];
				if (color != 0) {
					dest[destOff++] = palette[color & 0xff];
				} else {
					destOff++;
				}
				color = src[srcOff++];
				if (color != 0) {
					dest[destOff++] = palette[color & 0xff];
				} else {
					destOff++;
				}
				color = src[srcOff++];
				if (color != 0) {
					dest[destOff++] = palette[color & 0xff];
				} else {
					destOff++;
				}
			}
			for (int k = width; k < 0; k++) {
				byte color = src[srcOff++];
				if (color != 0) {
					dest[destOff++] = palette[color & 0xff];
				} else {
					destOff++;
				}
			}
			destOff += destStep;
			srcOff += srcStep;
		}
	}
	public byte pixels[];
	public final int[] pallete;
	public int width;
	public int height;
	public int offsetX;
	public int offsetY;
	public int trimWidth;
	private int trimHeight;
}
