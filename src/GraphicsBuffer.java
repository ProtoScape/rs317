/**
 * GraphicsBuffer [RSImageProducer]
 * A buffer for 2d graphics that defines the canvas size
 */
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ColorModel;
import java.awt.image.DirectColorModel;
import java.awt.image.ImageConsumer;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;

final class GraphicsBuffer implements ImageProducer, ImageObserver {
	public GraphicsBuffer(int width, int height, Component component) {
		canvasWidth = width;
		canvasHeight = height;
		componentPixels = new int[width * height];
		colorModel = new DirectColorModel(32, 0xff0000, 65280, 255);
		image = component.createImage(this);
		method239();
		component.prepareImage(image, this);
		method239();
		component.prepareImage(image, this);
		method239();
		component.prepareImage(image, this);
		initDrawingArea();
	}

	public void initDrawingArea() {
		Graphics2D.init2dCanvas(canvasHeight, canvasWidth, componentPixels);
	}

	public void drawGraphics(int y, Graphics g, int x) {
		method239();
		g.drawImage(image, x, y, this);
	}

	@Override
	public synchronized void addConsumer(ImageConsumer imageconsumer) {
		imageConsumer = imageconsumer;
		imageconsumer.setDimensions(canvasWidth, canvasHeight);
		imageconsumer.setProperties(null);
		imageconsumer.setColorModel(colorModel);
		imageconsumer.setHints(14);
	}

	@Override
	public synchronized boolean isConsumer(ImageConsumer imageconsumer) {
		return imageConsumer == imageconsumer;
	}

	@Override
	public synchronized void removeConsumer(ImageConsumer imageconsumer) {
		if (imageConsumer == imageconsumer) {
			imageConsumer = null;
		}
	}

	@Override
	public void startProduction(ImageConsumer imageconsumer) {
		addConsumer(imageconsumer);
	}

	@Override
	public void requestTopDownLeftRightResend(ImageConsumer imageconsumer) {
		System.out.println("TDLR");
	}

	private synchronized void method239() {
		if (imageConsumer != null) {
			imageConsumer.setPixels(0, 0, canvasWidth, canvasHeight, colorModel, componentPixels, 0, canvasWidth);
			imageConsumer.imageComplete(2);
		}
	}

	@Override
	public boolean imageUpdate(Image image, int flags, int x, int y, int width, int height) {
		return true;
	}
	public final int[] componentPixels;
	private final int canvasWidth;
	private final int canvasHeight;
	private final ColorModel colorModel;
	private ImageConsumer imageConsumer;
	private final Image image;
}
