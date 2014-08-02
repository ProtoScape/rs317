/**
 * StillGraphic [Animable_Sub3]
 * Represents a static graphic that belongs on the scene,
 * I.e. fire on a torch/fireplace.
 */
final class StillGraphic extends Entity {
	public StillGraphic(int plane, int arg1, int delay, int spotAnimId, int arg4, int y, int x) {
		aBoolean1567 = false;
		spotAnimation = SpotAnimation.spotAnimationCache[spotAnimId];
		anInt1560 = plane;
		anInt1561 = x;
		anInt1562 = y;
		anInt1563 = arg4;
		anInt1564 = arg1 + delay;
		aBoolean1567 = false;
	}

	@Override
	public Model getRotatedModel() {
		Model model = spotAnimation.getModel();
		if (model == null) {
			return null;
		}
		int j = spotAnimation.sequence.frames[anInt1569];
		Model model_1 = new Model(true, Animation.isNullFrame(j), false, model);
		if (!aBoolean1567) {
			model_1.skin();
			model_1.transform(j);
			model_1.triangleSkin = null;
			model_1.vertexSkins = null;
		}
		if (spotAnimation.scaleXY != 128 || spotAnimation.scaleZ != 128) {
			model_1.scale(spotAnimation.scaleXY, spotAnimation.scaleXY, spotAnimation.scaleZ);
		}
		if (spotAnimation.rotation != 0) {
			if (spotAnimation.rotation == 90) {
				model_1.method473();
			}
			if (spotAnimation.rotation == 180) {
				model_1.method473();
				model_1.method473();
			}
			if (spotAnimation.rotation == 270) {
				model_1.method473();
				model_1.method473();
				model_1.method473();
			}
		}
		model_1.processLighting(64 + spotAnimation.lightness, 850 + spotAnimation.shading, -30, -50, -30, true);
		return model_1;
	}

	public void method454(int arg0) {
		for (anInt1570 += arg0; anInt1570 > spotAnimation.sequence.getFrameLength(anInt1569);) {
			anInt1570 -= spotAnimation.sequence.getFrameLength(anInt1569) + 1;
			anInt1569++;
			if (anInt1569 >= spotAnimation.sequence.length && (anInt1569 < 0 || anInt1569 >= spotAnimation.sequence.length)) {
				anInt1569 = 0;
				aBoolean1567 = true;
			}
		}
	}
	public final int anInt1560;
	public final int anInt1561;
	public final int anInt1562;
	public final int anInt1563;
	public final int anInt1564;
	public boolean aBoolean1567;
	private final SpotAnimation spotAnimation;
	private int anInt1569;
	private int anInt1570;
}
