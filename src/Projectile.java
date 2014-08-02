
/**
 * Projectile [Animable_Sub4]
 * Represents a projectile on the scene
 */
final class Projectile extends Entity {

    public void method455(int time, int destY, int drawHeight, int destX) {
        if (!aBoolean1579) {
            double deltaX = destX - startX;
            double deltaY = destY - startY;
            double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
            aDouble1585 = startX + deltaX * angle / distance;
            aDouble1586 = startY + deltaY * angle / distance;
            aDouble1587 = anInt1582;
        }
        double d1 = speed + 1 - time;
        aDouble1574 = (destX - aDouble1585) / d1;
        aDouble1575 = (destY - aDouble1586) / d1;
        aDouble1576 = Math.sqrt(aDouble1574 * aDouble1574 + aDouble1575 * aDouble1575);
        if (!aBoolean1579) {
            aDouble1577 = -aDouble1576 * Math.tan(slope * 0.02454369D);
        }
        aDouble1578 = 2D * (drawHeight - aDouble1587 - aDouble1577 * d1) / (d1 * d1);
    }

    @Override
    public Model getRotatedModel() {
        Model model = spotAnimation.getModel();
        if (model == null) {
            return null;
        }
        int frameId = -1;
        if (spotAnimation.sequence != null) {
            frameId = spotAnimation.sequence.frames[relapsedFrames];
        }
        Model model_1 = new Model(true, Animation.isNullFrame(frameId), false, model);
        if (frameId != -1) {
            model_1.skin();
            model_1.transform(frameId);
            model_1.triangleSkin = null;
            model_1.vertexSkins = null;
        }
        if (spotAnimation.scaleXY != 128 || spotAnimation.scaleZ != 128) {
            model_1.scale(spotAnimation.scaleXY, spotAnimation.scaleXY, spotAnimation.scaleZ);
        }
        model_1.method474(anInt1596);
        model_1.processLighting(64 + spotAnimation.lightness, 850 + spotAnimation.shading, -30, -50, -30, true);
        return model_1;
    }

    public Projectile(int slope, int endHeight, int time, int speed, int angle, int arg5, int arg6, int startY, int startX, int target, int graphicId) {
        aBoolean1579 = false;
        spotAnimation = SpotAnimation.spotAnimationCache[graphicId];
        plane = arg5;
        this.startX = startX;
        this.startY = startY;
        anInt1582 = arg6;
        this.time = time;
        this.speed = speed;
        this.slope = slope;
        this.angle = angle;
        this.target = target;
        this.endHeight = endHeight;
        aBoolean1579 = false;
    }

    public void method456(int timePassed) {
        aBoolean1579 = true;
        aDouble1585 += aDouble1574 * timePassed;
        aDouble1586 += aDouble1575 * timePassed;
        aDouble1587 += aDouble1577 * timePassed + 0.5D * aDouble1578 * timePassed * timePassed;
        aDouble1577 += aDouble1578 * timePassed;
        anInt1595 = (int) (Math.atan2(aDouble1574, aDouble1575) * 325.94900000000001D) + 1024 & 0x7ff;
        anInt1596 = (int) (Math.atan2(aDouble1577, aDouble1576) * 325.94900000000001D) & 0x7ff;
        if (spotAnimation.sequence != null) {
            for (duration += timePassed; duration > spotAnimation.sequence.getFrameLength(relapsedFrames);) {
                duration -= spotAnimation.sequence.getFrameLength(relapsedFrames) + 1;
                relapsedFrames++;
                if (relapsedFrames >= spotAnimation.sequence.length) {
                    relapsedFrames = 0;
                }
            }
        }
    }
    public final int time;
    public final int speed;
    private double aDouble1574;
    private double aDouble1575;
    private double aDouble1576;
    private double aDouble1577;
    private double aDouble1578;
    private boolean aBoolean1579;
    private final int startX;
    private final int startY;
    private final int anInt1582;
    public final int endHeight;
    public double aDouble1585;
    public double aDouble1586;
    public double aDouble1587;
    private final int slope;
    private final int angle;
    public final int target;
    private final SpotAnimation spotAnimation;
    private int relapsedFrames;
    private int duration;
    public int anInt1595;
    private int anInt1596;
    public final int plane;
}
