
public final class Npc extends Mobile {

    private Model method450() {
        if (super.animationId >= 0 && super.animationDelay == 0) {
            int k = Sequence.sequenceCache[super.animationId].frames[super.anInt1527];
            int i1 = -1;
            if (super.anInt1517 >= 0 && super.anInt1517 != super.idleAnimation) {
                i1 = Sequence.sequenceCache[super.anInt1517].frames[super.anInt1518];
            }
            return desc.method164(i1, k, Sequence.sequenceCache[super.animationId].anIntArray357);
        }
        int l = -1;
        if (super.anInt1517 >= 0) {
            l = Sequence.sequenceCache[super.anInt1517].frames[super.anInt1518];
        }
        return desc.method164(-1, l, null);
    }

    @Override
    public Model getRotatedModel() {
        if (desc == null) {
            return null;
        }
        Model model = method450();
        if (model == null) {
            return null;
        }
        super.height = model.height;
        if (super.currentGraphic != -1 && super.anInt1521 != -1) {
            SpotAnimation spotAnimation = SpotAnimation.spotAnimationCache[super.currentGraphic];
            Model model_1 = spotAnimation.getModel();
            if (model_1 != null) {
                int j = spotAnimation.sequence.frames[super.anInt1521];
                Model model_2 = new Model(true, Animation.isNullFrame(j), false, model_1);
                model_2.translate(0, -super.anInt1524, 0);
                model_2.skin();
                model_2.transform(j);
                model_2.triangleSkin = null;
                model_2.vertexSkins = null;
                if (spotAnimation.scaleXY != 128 || spotAnimation.scaleZ != 128) {
                    model_2.scale(spotAnimation.scaleXY, spotAnimation.scaleXY, spotAnimation.scaleZ);
                }
                model_2.processLighting(64 + spotAnimation.lightness, 850 + spotAnimation.shading, -30, -50, -30, true);
                Model aModel[] = {model, model_2};
                model = new Model(aModel);
            }
        }
        if (desc.size == 1) {
            model.aBoolean1659 = true;
        }
        return model;
    }

    @Override
    public boolean isVisible() {
        return desc != null;
    }

    Npc() {
    }
    public NpcDefinition desc;
}
