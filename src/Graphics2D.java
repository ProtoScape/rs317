/**
 * Renderer2d [DrawingArea]
 * Contains the methods for most of the client's 2d rendering
 */
public class Graphics2D extends CacheableNode {
	public static void init2dCanvas(int height, int width, int pixels[]) {
		Graphics2D.pixels = pixels;
		Graphics2D.width = width;
		Graphics2D.height = height;
		Graphics2D.setBounds(height, 0, width, 0);
	}

	public static void resetBounds() {
		Graphics2D.topX = 0;
		Graphics2D.topY = 0;
		Graphics2D.bottomX = Graphics2D.width;
		Graphics2D.bottomY = Graphics2D.height;
		Graphics2D.endX = Graphics2D.bottomX;
		Graphics2D.middleX = Graphics2D.bottomX / 2;
	}

	public static void setBounds(int height, int x, int width, int y) {
		if (x < 0) {
			x = 0;
		}
		if (y < 0) {
			y = 0;
		}
		if (width > Graphics2D.width) {
			width = Graphics2D.width;
		}
		if (height > Graphics2D.height) {
			height = Graphics2D.height;
		}
		Graphics2D.topX = x;
		Graphics2D.topY = y;
		Graphics2D.bottomX = width;
		Graphics2D.bottomY = height;
		Graphics2D.endX = Graphics2D.bottomX;
		Graphics2D.middleX = Graphics2D.bottomX / 2;
		Graphics2D.middleY = Graphics2D.bottomY / 2;
	}

	public static void resetPixels() {
		int total = Graphics2D.width * Graphics2D.height;
		for (int i = 0; i < total; i++) {
			Graphics2D.pixels[i] = 0;
		}
	}

	public static void fillRect(int color, int y, int width, int height, int alpha, int x) {
		if (x < Graphics2D.topX) {
			width -= Graphics2D.topX - x;
			x = Graphics2D.topX;
		}
		if (y < Graphics2D.topY) {
			height -= Graphics2D.topY - y;
			y = Graphics2D.topY;
		}
		if (x + width > Graphics2D.bottomX) {
			width = Graphics2D.bottomX - x;
		}
		if (y + height > Graphics2D.bottomY) {
			height = Graphics2D.bottomY - y;
		}
		int srcAlpha = 256 - alpha;
		int destRed = (color >> 16 & 0xff) * alpha;
		int destGreen = (color >> 8 & 0xff) * alpha;
		int destBlue = (color & 0xff) * alpha;
		int step = Graphics2D.width - width;
		int off = x + y * Graphics2D.width;
		for (int i = 0; i < height; i++) {
			for (int j = -width; j < 0; j++) {
				int srcRed = (Graphics2D.pixels[off] >> 16 & 0xff) * srcAlpha;
				int srcGreen = (Graphics2D.pixels[off] >> 8 & 0xff) * srcAlpha;
				int srcBlue = (Graphics2D.pixels[off] & 0xff) * srcAlpha;
				int destColor = (destRed + srcRed >> 8 << 16) + (destGreen + srcGreen >> 8 << 8) + (destBlue + srcBlue >> 8);
				Graphics2D.pixels[off++] = destColor;
			}
			off += step;
		}
	}

	public static void fillRect(int height, int y, int x, int color, int width) {
		if (x < Graphics2D.topX) {
			width -= Graphics2D.topX - x;
			x = Graphics2D.topX;
		}
		if (y < Graphics2D.topY) {
			height -= Graphics2D.topY - y;
			y = Graphics2D.topY;
		}
		if (x + width > Graphics2D.bottomX) {
			width = Graphics2D.bottomX - x;
		}
		if (y + height > Graphics2D.bottomY) {
			height = Graphics2D.bottomY - y;
		}
		int k1 = Graphics2D.width - width;
		int l1 = x + y * Graphics2D.width;
		for (int i2 = -height; i2 < 0; i2++) {
			for (int j2 = -width; j2 < 0; j2++) {
				Graphics2D.pixels[l1++] = color;
			}
			l1 += k1;
		}
	}

	public static void drawRect(int arg0, int arg1, int arg2, int arg3, int arg4) {
		Graphics2D.drawHorizontalLine(arg4, arg3, arg1, arg0);
		Graphics2D.drawHorizontalLine(arg4 + arg2 - 1, arg3, arg1, arg0);
		Graphics2D.drawVerticalLine(arg4, arg3, arg2, arg0);
		Graphics2D.drawVerticalLine(arg4, arg3, arg2, arg0 + arg1 - 1);
	}

	public static void drawRect(int arg0, int arg1, int arg3, int arg4, int arg5, int arg6) {
		Graphics2D.drawHorizontalLine(arg4, arg5, arg0, arg3, arg6);
		Graphics2D.drawHorizontalLine(arg4, arg5, arg0 + arg1 - 1, arg3, arg6);
		if (arg1 >= 3) {
			Graphics2D.method342(arg4, arg6, arg3, arg0 + 1, arg1 - 2);
			Graphics2D.method342(arg4, arg6 + arg5 - 1, arg3, arg0 + 1, arg1 - 2);
		}
	}

	public static void drawHorizontalLine(int y, int color, int width, int x) {
		if (y < Graphics2D.topY || y >= Graphics2D.bottomY) {
			return;
		}
		if (x < Graphics2D.topX) {
			width -= Graphics2D.topX - x;
			x = Graphics2D.topX;
		}
		if (x + width > Graphics2D.bottomX) {
			width = Graphics2D.bottomX - x;
		}
		int step = x + y * Graphics2D.width;
		for (int offX = 0; offX < width; offX++) {
			Graphics2D.pixels[step + offX] = color;
		}
	}

	private static void drawHorizontalLine(int color, int width, int y, int alpha, int x) {
		if (y < Graphics2D.topY || y >= Graphics2D.bottomY) {
			return;
		}
		if (x < Graphics2D.topX) {
			width -= Graphics2D.topX - x;
			x = Graphics2D.topX;
		}
		if (x + width > Graphics2D.bottomX) {
			width = Graphics2D.bottomX - x;
		}
		int srcAlpha = 256 - alpha;
		int destRed = (color >> 16 & 0xff) * alpha;
		int destGreen = (color >> 8 & 0xff) * alpha;
		int destBlue = (color & 0xff) * alpha;
		int step = x + y * Graphics2D.width;
		for (int j3 = 0; j3 < width; j3++) {
			int srcRed = (Graphics2D.pixels[step] >> 16 & 0xff) * srcAlpha;
			int srcGreen = (Graphics2D.pixels[step] >> 8 & 0xff) * srcAlpha;
			int srcBlue = (Graphics2D.pixels[step] & 0xff) * srcAlpha;
			int destColor = (destRed + srcRed >> 8 << 16) + (destGreen + srcGreen >> 8 << 8) + (destBlue + srcBlue >> 8);
			Graphics2D.pixels[step++] = destColor;
		}
	}

	public static void drawVerticalLine(int y, int color, int height, int x) {
		if (x < Graphics2D.topX || x >= Graphics2D.bottomX) {
			return;
		}
		if (y < Graphics2D.topY) {
			height -= Graphics2D.topY - y;
			y = Graphics2D.topY;
		}
		if (y + height > Graphics2D.bottomY) {
			height = Graphics2D.bottomY - y;
		}
		int step = x + y * Graphics2D.width;
		for (int offY = 0; offY < height; offY++) {
			Graphics2D.pixels[step + offY * Graphics2D.width] = color;
		}
	}

	private static void method342(int color, int x, int alpha, int y, int height) {
		if (x < Graphics2D.topX || x >= Graphics2D.bottomX) {
			return;
		}
		if (y < Graphics2D.topY) {
			height -= Graphics2D.topY - y;
			y = Graphics2D.topY;
		}
		if (y + height > Graphics2D.bottomY) {
			height = Graphics2D.bottomY - y;
		}
		int srcAlpha = 256 - alpha;
		int destRed = (color >> 16 & 0xff) * alpha;
		int destGreen = (color >> 8 & 0xff) * alpha;
		int destBlue = (color & 0xff) * alpha;
		int step = x + y * Graphics2D.width;
		for (int j3 = 0; j3 < height; j3++) {
			int j2 = (Graphics2D.pixels[step] >> 16 & 0xff) * srcAlpha;
			int k2 = (Graphics2D.pixels[step] >> 8 & 0xff) * srcAlpha;
			int l2 = (Graphics2D.pixels[step] & 0xff) * srcAlpha;
			int destColor = (destRed + j2 >> 8 << 16) + (destGreen + k2 >> 8 << 8) + (destBlue + l2 >> 8);
			Graphics2D.pixels[step] = destColor;
			step += Graphics2D.width;
		}
	}

	Graphics2D() {
	}
	public static int pixels[];
	public static int width;
	public static int height;
	public static int topY;
	public static int bottomY;
	public static int topX;
	public static int bottomX;
	public static int endX;
	public static int middleX;
	public static int middleY;
}
