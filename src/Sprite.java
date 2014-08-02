import java.awt.Component;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.image.PixelGrabber;

public final class Sprite extends Graphics2D {

	public Sprite(int width, int height) {
		pixels = new int[width * height];
		this.width = trimWidth = width;
		this.height = trimHeight = height;
		offsetX = offsetY = 0;
	}

	public Sprite(byte buf[], Component component) {
		try {
			Image image = Toolkit.getDefaultToolkit().createImage(buf);
			MediaTracker mediatracker = new MediaTracker(component);
			mediatracker.addImage(image, 0);
			mediatracker.waitForAll();
			width = image.getWidth(component);
			height = image.getHeight(component);
			trimWidth = width;
			trimHeight = height;
			offsetX = 0;
			offsetY = 0;
			pixels = new int[width * height];
			PixelGrabber pixelgrabber = new PixelGrabber(image, 0, 0, width, height, pixels, 0, width);
			pixelgrabber.grabPixels();
		} catch (Exception _ex) {
			System.out.println("Error converting jpg");
		}
	}

	public Sprite(Archive container, String name, int entry) {
		Stream image = new Stream(container.get(name + ".dat"));
		Stream index = new Stream(container.get("index.dat"));
		index.offset = image.getUnsignedShort();
		trimWidth = index.getUnsignedShort();
		trimHeight = index.getUnsignedShort();
		int len = index.getUnsignedByte();
		int buf[] = new int[len];
		for (int i = 0; i < len - 1; i++) {
			buf[i + 1] = index.get24BitInt();
			if (buf[i + 1] == 0) {
				buf[i + 1] = 1;
			}
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
		pixels = new int[pixelCount];
		if (imageType == 0) {
			for (int k1 = 0; k1 < pixelCount; k1++) {
				pixels[k1] = buf[image.getUnsignedByte()];
			}
			return;
		}
		if (imageType == 1) {
			for (int l1 = 0; l1 < width; l1++) {
				for (int i2 = 0; i2 < height; i2++) {
					pixels[l1 + i2 * width] = buf[image.getUnsignedByte()];
				}
			}
		}
	}

	public void init() {
		Graphics2D.init2dCanvas(height, width, pixels);
	}

	public void adjustColors(int redOff, int greenOff, int blueOff) {
		for (int i = 0; i < pixels.length; i++) {
			int color = pixels[i];
			if (color != 0) {
				int red = color >> 16 & 0xff;
				red += redOff;
				if (red < 1) {
					red = 1;
				} else if (red > 255) {
					red = 255;
				}
				int green = color >> 8 & 0xff;
				green += greenOff;
				if (green < 1) {
					green = 1;
				} else if (green > 255) {
					green = 255;
				}
				int blue = color & 0xff;
				blue += blueOff;
				if (blue < 1) {
					blue = 1;
				} else if (blue > 255) {
					blue = 255;
				}
				pixels[i] = (red << 16) + (green << 8) + blue;
			}
		}
	}

	public void resize() {
		int image[] = new int[trimWidth * trimHeight];
		for (int offY = 0; offY < height; offY++) {
			for (int offX = 0; offX < width; offX++) {
				image[(offY + offsetY) * trimWidth + (offX + offsetX)] = pixels[offY * width + offX];
			}
		}
		pixels = image;
		width = trimWidth;
		height = trimHeight;
		offsetX = 0;
		offsetY = 0;
	}

	public void drawFlippedSprite(int x, int y) {
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
		if (width <= 0 || height <= 0) {
		} else {
			copyPixels(destOff, width, height, srcStep, srcOff, destStep, pixels, Graphics2D.pixels);
		}
	}

	private void copyPixels(int destOff, int width, int height, int srcStep, int srcOff, int destStep, int src[], int dest[]) {
		int quarterX = -(width >> 2);
		width = -(width & 3);
		for (int i = -height; i < 0; i++) {
			for (int j = quarterX; j < 0; j++) {
				dest[destOff++] = src[srcOff++];
				dest[destOff++] = src[srcOff++];
				dest[destOff++] = src[srcOff++];
				dest[destOff++] = src[srcOff++];
			}
			for (int k = width; k < 0; k++) {
				dest[destOff++] = src[srcOff++];
			}
			destOff += destStep;
			srcOff += srcStep;
		}
	}

	public void drawSprite(int x, int y, int alpha) {
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
			pushPixels(srcOff, width, Graphics2D.pixels, pixels, srcStep, height, destStep, alpha, destOff);
		}
	}

	public void drawSprite(int x, int y) {
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
			pushPixels(Graphics2D.pixels, pixels, srcOff, destOff, width, height, destStep, srcStep);
		}
	}

	private void pushPixels(int dest[], int src[], int srcOff, int destOff, int width, int height, int destStep, int srcStep) {
		int color;
		int quarterX = -(width >> 2);
		width = -(width & 3);
		for (int i = -height; i < 0; i++) {
			for (int j = quarterX; j < 0; j++) {
				color = src[srcOff++];
				if (color != 0) {
					dest[destOff++] = color;
				} else {
					destOff++;
				}
				color = src[srcOff++];
				if (color != 0) {
					dest[destOff++] = color;
				} else {
					destOff++;
				}
				color = src[srcOff++];
				if (color != 0) {
					dest[destOff++] = color;
				} else {
					destOff++;
				}
				color = src[srcOff++];
				if (color != 0) {
					dest[destOff++] = color;
				} else {
					destOff++;
				}
			}
			for (int k = width; k < 0; k++) {
				color = src[srcOff++];
				if (color != 0) {
					dest[destOff++] = color;
				} else {
					destOff++;
				}
			}
			destOff += destStep;
			srcOff += srcStep;
		}
	}

	private void pushPixels(int srcOff, int width, int destPixels[], int srcPixels[], int srcStep, int height, int destStep, int fadeAlpha,
		int destOff) {
		int color;
		int destAlpha = 256 - fadeAlpha;
		for (int k2 = -height; k2 < 0; k2++) {
			for (int l2 = -width; l2 < 0; l2++) {
				color = srcPixels[srcOff++];
				if (color != 0) {
					int destColor = destPixels[destOff];
					destPixels[destOff++] = ((color & 0xff00ff) * fadeAlpha + (destColor & 0xff00ff) * destAlpha & 0xff00ff00)
						+ ((color & 0xff00) * fadeAlpha + (destColor & 0xff00) * destAlpha & 0xff0000) >> 8;
				} else {
					destOff++;
				}
			}
			destOff += destStep;
			srcOff += srcStep;
		}
	}

	public void drawShapedSprite(int height, int arg1, int dest[], int arg3, int src[], int arg5, int arg6, int arg7, int width, int arg9) {
		try {
			int j2 = -width / 2;
			int k2 = -height / 2;
			int sineStep = (int) (Math.sin(arg1 / 326.11000000000001D) * 65536D);
			int cosStep = (int) (Math.cos(arg1 / 326.11000000000001D) * 65536D);
			sineStep = sineStep * arg3 >> 8;
			cosStep = cosStep * arg3 >> 8;
			int sine = (arg9 << 16) + k2 * sineStep + j2 * cosStep;
			int cos = (arg5 << 16) + k2 * cosStep - j2 * sineStep;
			int l3 = arg7 + arg6 * Graphics2D.width;
			for (arg6 = 0; arg6 < height; arg6++) {
				int i4 = src[arg6];
				int j4 = l3 + i4;
				int k4 = sine + cosStep * i4;
				int l4 = cos - sineStep * i4;
				for (arg7 = -dest[arg6]; arg7 < 0; arg7++) {
					Graphics2D.pixels[j4++] = pixels[(k4 >> 16) + (l4 >> 16) * this.width];
					k4 += cosStep;
					l4 -= sineStep;
				}
				sine += sineStep;
				cos += cosStep;
				l3 += Graphics2D.width;
			}
		} catch (Exception exception) {
		}
	}

	public void drawRotatedSprite(int arg0, double arg1, int arg2) {
		int j = 15;
		int k = 20;
		int l = 15;
		int j1 = 256;
		int k1 = 20;
		try {
			int i2 = -k / 2;
			int j2 = -k1 / 2;
			int k2 = (int) (Math.sin(arg1) * 65536D);
			int l2 = (int) (Math.cos(arg1) * 65536D);
			k2 = k2 * j1 >> 8;
			l2 = l2 * j1 >> 8;
			int i3 = (l << 16) + j2 * k2 + i2 * l2;
			int j3 = (j << 16) + j2 * l2 - i2 * k2;
			int k3 = arg2 + arg0 * Graphics2D.width;
			for (arg0 = 0; arg0 < k1; arg0++) {
				int l3 = k3;
				int i4 = i3;
				int j4 = j3;
				for (arg2 = -k; arg2 < 0; arg2++) {
					int k4 = pixels[(i4 >> 16) + (j4 >> 16) * width];
					if (k4 != 0) {
						Graphics2D.pixels[l3++] = k4;
					} else {
						l3++;
					}
					i4 += l2;
					j4 -= k2;
				}
				i3 += k2;
				j3 += l2;
				k3 += Graphics2D.width;
			}
		} catch (Exception exception) {
		}
	}

	public void method354(IndexedSprite indexedSprite, int y, int x) {
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
			method355(pixels, width, indexedSprite.pixels, height, Graphics2D.pixels, 0, destStep, destOff, srcStep, srcOff);
		}
	}

	private void method355(int src[], int width, byte palette[], int height, int dest[], int color, int destStep, int destOff, int srcStep, int srcOff) {
		int quarterX = -(width >> 2);
		width = -(width & 3);
		for (int i = -height; i < 0; i++) {
			for (int j = quarterX; j < 0; j++) {
				color = src[srcOff++];
				if (color != 0 && palette[destOff] == 0) {
					dest[destOff++] = color;
				} else {
					destOff++;
				}
				color = src[srcOff++];
				if (color != 0 && palette[destOff] == 0) {
					dest[destOff++] = color;
				} else {
					destOff++;
				}
				color = src[srcOff++];
				if (color != 0 && palette[destOff] == 0) {
					dest[destOff++] = color;
				} else {
					destOff++;
				}
				color = src[srcOff++];
				if (color != 0 && palette[destOff] == 0) {
					dest[destOff++] = color;
				} else {
					destOff++;
				}
			}
			for (int k = width; k < 0; k++) {
				color = src[srcOff++];
				if (color != 0 && palette[destOff] == 0) {
					dest[destOff++] = color;
				} else {
					destOff++;
				}
			}
			destOff += destStep;
			srcOff += srcStep;
		}
	}
	public int pixels[];
	public int width;
	public int height;
	private int offsetX;
	private int offsetY;
	public int trimWidth;
	public int trimHeight;
}
