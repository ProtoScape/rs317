import java.awt.Graphics;
import java.awt.Insets;

final class Frame extends java.awt.Frame {

	private static final long serialVersionUID = 2694260829071998795L;
	public Frame(Applet applet, int width, int height) {
		this.applet = applet;
		setTitle("Jagex");
		setResizable(false);
		setVisible(true);
		toFront();
		setSize(width + 6, height + 28);
	}

	@Override
	public Graphics getGraphics() {
		Graphics g = super.getGraphics();
		Insets i = super.getInsets();
		g.translate(i.left, i.top);
		return g;
	}

	@Override
	public void update(Graphics g) {
		applet.update(g);
	}

	@Override
	public void paint(Graphics g) {
		applet.paint(g);
	}
	private final Applet applet;
}
