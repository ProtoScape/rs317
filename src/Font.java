import java.util.Random;

/**
 * Font [TextDrawingArea]
 * Handles and defines the drawing of fonts in the game
 */
public final class Font extends Graphics2D {
	public Font(boolean flag, String name, Archive container) {
		pixels = new byte[256][];
		width = new int[256];
		height = new int[256];
		horizontalKerning = new int[256];
		verticalKerning = new int[256];
		charWidths = new int[256];
		random = new Random();
		strikethrough = false;
		Stream data = new Stream(container.get(name + ".dat"));
		Stream index = new Stream(container.get("index.dat"));
		index.offset = data.getUnsignedShort() + 4;
		int v = index.getUnsignedByte();
		if (v > 0) {
			index.offset += 3 * (v - 1);
		}
		for (int i = 0; i < 256; i++) {
			horizontalKerning[i] = index.getUnsignedByte();
			verticalKerning[i] = index.getUnsignedByte();
			int width = this.width[i] = index.getUnsignedShort();
			int height = this.height[i] = index.getUnsignedShort();
			int type = index.getUnsignedByte();
			int pixelCount = width * height;
			pixels[i] = new byte[pixelCount];
			if (type == 0) {
				for (int i2 = 0; i2 < pixelCount; i2++) {
					pixels[i][i2] = data.getByte();
				}
			} else if (type == 1) {
				for (int j2 = 0; j2 < width; j2++) {
					for (int l2 = 0; l2 < height; l2++) {
						pixels[i][j2 + l2 * width] = data.getByte();
					}
				}
			}
			if (height > trimHeight && i < 128) {
				trimHeight = height;
			}
			horizontalKerning[i] = 1;
			charWidths[i] = width + 2;
			int off = 0;
			for (int i3 = height / 7; i3 < height; i3++) {
				off += pixels[i][i3 * width];
			}
			if (off <= height / 7) {
				charWidths[i]--;
				horizontalKerning[i] = 0;
			}
			off = 0;
			for (int j3 = height / 7; j3 < height; j3++) {
				off += pixels[i][width - 1 + j3 * width];
			}
			if (off <= height / 7) {
				charWidths[i]--;
			}
		}
		if (flag) {
			charWidths[32] = charWidths[73];
		} else {
			charWidths[32] = charWidths[105];
		}
	}

	public void method380(String text, int x, int color, int y) {
		drawText(color, text, y, x - getANTextWidth(text));
	}

	public void drawANCText(int color, String text, int y, int x) {
		drawText(color, text, y, x - getANTextWidth(text) / 2);
	}

	public void drawCenteredText(int color, int x, String text, int y, boolean shadow) {
		drawText(shadow, x - getWidth(text) / 2, color, text, y);
	}

	public int getWidth(String text) {
		if (text == null) {
			return 0;
		}
		int width = 0;
		for (int i = 0; i < text.length(); i++) {
			if (text.charAt(i) == '@' && i + 4 < text.length() && text.charAt(i + 4) == '@') {
				i += 4;
			} else {
				width += charWidths[text.charAt(i)];
			}
		}
		return width;
	}

	public int getANTextWidth(String text) { // method384
		if (text == null) {
			return 0;
		}
		int width = 0;
		for (int i = 0; i < text.length(); i++) {
			width += charWidths[text.charAt(i)];
		}
		return width;
	}

	public void drawText(int color, String text, int y, int x) { // method385
		if (text == null) {
			return;
		}
		y -= trimHeight;
		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			if (c != ' ') {
				drawChar(pixels[c], x + horizontalKerning[c], y + verticalKerning[c], width[c], height[c], color);
			}
			x += charWidths[c];
		}
	}

	public void drawWaveText(int color, String text, int x, int effectSpeed, int y) { // method386
		if (text == null) {
			return;
		}
		x -= getANTextWidth(text) / 2;
		y -= trimHeight;
		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			if (c != ' ') {
				drawChar(pixels[c], x + horizontalKerning[c], y + verticalKerning[c] + (int) (Math.sin(i / 2D + effectSpeed / 5D) * 5D), width[c], height[c], color);
			}
			x += charWidths[c];
		}
	}

	public void drawWave2Text(int x, String text, int effectSpeed, int y, int color) { // method387
		if (text == null) {
			return;
		}
		x -= getANTextWidth(text) / 2;
		y -= trimHeight;
		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			if (c != ' ') {
				drawChar(pixels[c], x + horizontalKerning[c] + (int) (Math.sin(i / 5D + effectSpeed / 5D) * 5D), y + verticalKerning[c] + (int) (Math.sin(i / 3D + effectSpeed / 5D) * 5D), width[c], height[c], color);
			}
			x += charWidths[c];
		}
	}

	public void drawShakeText(int startHeight, String text, int effectSpeed, int y, int x, int color) { // method388
		if (text == null) {
			return;
		}
		double bounceHeight = 7D - startHeight / 8D;
		if (bounceHeight < 0.0D) {
			bounceHeight = 0.0D;
		}
		x -= getANTextWidth(text) / 2;
		y -= trimHeight;
		for (int k1 = 0; k1 < text.length(); k1++) {
			char c = text.charAt(k1);
			if (c != ' ') {
				drawChar(pixels[c], x + horizontalKerning[c], y + verticalKerning[c] + (int) (Math.sin(k1 / 1.5D + effectSpeed) * bounceHeight), width[c], height[c], color);
			}
			x += charWidths[c];
		}
	}

	public void drawText(boolean shadow, int x, int color, String text, int y) {
		strikethrough = false;
		int strikethroughLen = x;
		if (text == null) {
			return;
		}
		y -= trimHeight;
		for (int i = 0; i < text.length(); i++) {
			if (text.charAt(i) == '@' && i + 4 < text.length() && text.charAt(i + 4) == '@') {
				int colorCode = getColorByName(text.substring(i + 1, i + 4));
				if (colorCode != -1) {
					color = colorCode;
				}
				i += 4;
			} else {
				char c = text.charAt(i);
				if (c != ' ') {
					if (shadow) {
						drawChar(pixels[c], x + horizontalKerning[c] + 1, y + verticalKerning[c] + 1, width[c], height[c], 0);
					}
					drawChar(pixels[c], x + horizontalKerning[c], y + verticalKerning[c], width[c], height[c], color);
				}
				x += charWidths[c];
			}
		}
		if (strikethrough) {
			Graphics2D.drawHorizontalLine(y + (int) (trimHeight * 0.69999999999999996D), 0x800000, x - strikethroughLen, strikethroughLen);
		}
	}

	public void method390(int x, int alpha, String text, int seed, int y) {
		if (text == null) {
			return;
		}
		random.setSeed(seed);
		int color = 192 + (random.nextInt() & 0x1f);
		y -= trimHeight;
		for (int i = 0; i < text.length(); i++) {
			if (text.charAt(i) == '@' && i + 4 < text.length() && text.charAt(i + 4) == '@') {
				int col = getColorByName(text.substring(i + 1, i + 4));
				if (col != -1) {
					alpha = col;
				}
				i += 4;
			} else {
				char c = text.charAt(i);
				if (c != ' ') {
					drawChar(192, x + horizontalKerning[c] + 1, pixels[c], width[c], y + verticalKerning[c] + 1, height[c], 0);
					drawChar(color, x + horizontalKerning[c], pixels[c], width[c], y + verticalKerning[c], height[c], alpha);
				}
				x += charWidths[c];
				if ((random.nextInt() & 3) == 0) {
					x++;
				}
			}
		}
	}

	private int getColorByName(String colorCode) {
		if (colorCode.equals("red")) {
			return 0xff0000;
		}
		if (colorCode.equals("gre")) {
			return 65280;
		}
		if (colorCode.equals("blu")) {
			return 255;
		}
		if (colorCode.equals("yel")) {
			return 0xffff00;
		}
		if (colorCode.equals("cya")) {
			return 65535;
		}
		if (colorCode.equals("mag")) {
			return 0xff00ff;
		}
		if (colorCode.equals("whi")) {
			return 0xffffff;
		}
		if (colorCode.equals("bla")) {
			return 0;
		}
		if (colorCode.equals("lre")) {
			return 0xff9040;
		}
		if (colorCode.equals("dre")) {
			return 0x800000;
		}
		if (colorCode.equals("dbl")) {
			return 128;
		}
		if (colorCode.equals("or1")) {
			return 0xffb000;
		}
		if (colorCode.equals("or2")) {
			return 0xff7000;
		}
		if (colorCode.equals("or3")) {
			return 0xff3000;
		}
		if (colorCode.equals("gr1")) {
			return 0xc0ff00;
		}
		if (colorCode.equals("gr2")) {
			return 0x80ff00;
		}
		if (colorCode.equals("gr3")) {
			return 0x40ff00;
		}
		if (colorCode.equals("str")) {
			strikethrough = true;
		}
		if (colorCode.equals("end")) {
			strikethrough = false;
		}
		return -1;
	}

	private void drawChar(byte text[], int x, int y, int width, int height, int color) { // method392
		int destOff = x + y * Graphics2D.width;
		int destStep = Graphics2D.width - width;
		int srcStep = 0;
		int srcOff = 0;
		if (y < Graphics2D.topY) {
			int topMargin = Graphics2D.topY - y;
			height -= topMargin;
			y = Graphics2D.topY;
			srcOff += topMargin * width;
			destOff += topMargin * Graphics2D.width;
		}
		if (y + height >= Graphics2D.bottomY) {
			height -= y + height - Graphics2D.bottomY + 1;
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
		if (x + width >= Graphics2D.bottomX) {
			int rightMargin = x + width - Graphics2D.bottomX + 1;
			width -= rightMargin;
			srcStep += rightMargin;
			destStep += rightMargin;
		}
		if (!(width <= 0 || height <= 0)) {
			pushPixels(Graphics2D.pixels, text, color, srcOff, destOff, width, height, destStep, srcStep);
		}
	}

	private void pushPixels(int dest[], byte src[], int color, int srcOff, int destOff, int width, int height, int destStep, int srcStep) { // method393
		int quarterX = -(width >> 2);
		width = -(width & 3);
		for (int i = -height; i < 0; i++) {
			for (int j = quarterX; j < 0; j++) {
				if (src[srcOff++] != 0) {
					dest[destOff++] = color;
				} else {
					destOff++;
				}
				if (src[srcOff++] != 0) {
					dest[destOff++] = color;
				} else {
					destOff++;
				}
				if (src[srcOff++] != 0) {
					dest[destOff++] = color;
				} else {
					destOff++;
				}
				if (src[srcOff++] != 0) {
					dest[destOff++] = color;
				} else {
					destOff++;
				}
			}
			for (int k = width; k < 0; k++) {
				if (src[srcOff++] != 0) {
					dest[destOff++] = color;
				} else {
					destOff++;
				}
			}
			destOff += destStep;
			srcOff += srcStep;
		}
	}

	private void drawChar(int alpha, int x, byte text[], int width, int y, int height, int color) { // method394
		int destOff = x + y * Graphics2D.width;
		int destStep = Graphics2D.width - width;
		int srcStep = 0;
		int srcOff = 0;
		if (y < Graphics2D.topY) {
			int topMargin = Graphics2D.topY - y;
			height -= topMargin;
			y = Graphics2D.topY;
			srcOff += topMargin * width;
			destOff += topMargin * Graphics2D.width;
		}
		if (y + height >= Graphics2D.bottomY) {
			height -= y + height - Graphics2D.bottomY + 1;
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
		if (x + width >= Graphics2D.bottomX) {
			int rightMargin = x + width - Graphics2D.bottomX + 1;
			width -= rightMargin;
			srcStep += rightMargin;
			destStep += rightMargin;
		}
		if (width <= 0 || height <= 0) {
			return;
		}
		pushPixels(text, height, destOff, Graphics2D.pixels, srcOff, width, srcStep, destStep, color, alpha);
	}

	private void pushPixels(byte text[], int height, int destOff, int pixels[], int srcOff, int width, int srcStep, int destStep, int color, int alpha) { // method395
		color = ((color & 0xff00ff) * alpha & 0xff00ff00) + ((color & 0xff00) * alpha & 0xff0000) >> 8;
		alpha = 256 - alpha;
		for (int j2 = -height; j2 < 0; j2++) {
			for (int k2 = -width; k2 < 0; k2++) {
				if (text[srcOff++] != 0) {
					int l2 = pixels[destOff];
					pixels[destOff++] = (((l2 & 0xff00ff) * alpha & 0xff00ff00) + ((l2 & 0xff00) * alpha & 0xff0000) >> 8) + color;
				} else {
					destOff++;
				}
			}
			destOff += destStep;
			srcOff += srcStep;
		}
	}
	private final byte[][] pixels; // aByteArrayArray1491
	private final int[] width; // anIntArray1492
	private final int[] height; // anIntArray1493
	private final int[] horizontalKerning; // anIntArray1494
	private final int[] verticalKerning; // anIntArray1495
	private final int[] charWidths; // anIntArray1496
	public int trimHeight; // anInt1497
	private final Random random;
	private boolean strikethrough;
}
