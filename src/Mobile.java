public class Mobile extends Entity {
	public final void updatePosition(int x, int y, boolean flag) {
		if (animationId != -1 && Sequence.sequenceCache[animationId].walkProperties == 1) {
			animationId = -1;
		}
		if (!flag) {
			int destX = x - walkQueueX[0];
			int destY = y - walkQueueY[0];
			if (destX >= -8 && destX <= 8 && destY >= -8 && destY <= 8) {
				if (walkQueueLocationIndex < 9) {
					walkQueueLocationIndex++;
				}
				for (int i1 = walkQueueLocationIndex; i1 > 0; i1--) {
					walkQueueX[i1] = walkQueueX[i1 - 1];
					walkQueueY[i1] = walkQueueY[i1 - 1];
					runningFlags[i1] = runningFlags[i1 - 1];
				}
				walkQueueX[0] = x;
				walkQueueY[0] = y;
				runningFlags[0] = false;
				return;
			}
		}
		walkQueueLocationIndex = 0;
		anInt1542 = 0;
		anInt1503 = 0;
		walkQueueX[0] = x;
		walkQueueY[0] = y;
		this.x = walkQueueX[0] * 128 + anInt1540 * 64;
		this.y = walkQueueY[0] * 128 + anInt1540 * 64;
	}

	public final void method446() {
		walkQueueLocationIndex = 0;
		anInt1542 = 0;
	}

	public final void addHit(int type, int damage, int cycle) {
		for (int i1 = 0; i1 < 4; i1++) {
			if (hitCycles[i1] <= cycle) {
				hitValues[i1] = damage;
				hitMarkTypes[i1] = type;
				hitCycles[i1] = cycle + 70;
				return;
			}
		}
	}

	public final void move(boolean running, int dir) {
		int x = walkQueueX[0];
		int y = walkQueueY[0];
		if (dir == 0) {
			x--;
			y++;
		}
		if (dir == 1) {
			y++;
		}
		if (dir == 2) {
			x++;
			y++;
		}
		if (dir == 3) {
			x--;
		}
		if (dir == 4) {
			x++;
		}
		if (dir == 5) {
			x--;
			y--;
		}
		if (dir == 6) {
			y--;
		}
		if (dir == 7) {
			x++;
			y--;
		}
		if (animationId != -1 && Sequence.sequenceCache[animationId].walkProperties == 1) {
			animationId = -1;
		}
		if (walkQueueLocationIndex < 9) {
			walkQueueLocationIndex++;
		}
		for (int i = walkQueueLocationIndex; i > 0; i--) {
			walkQueueX[i] = walkQueueX[i - 1];
			walkQueueY[i] = walkQueueY[i - 1];
			runningFlags[i] = runningFlags[i - 1];
		}
		walkQueueX[0] = x;
		walkQueueY[0] = y;
		runningFlags[0] = running;
	}
	public boolean isVisible() {
		return false;
	}

	Mobile() {
		walkQueueX = new int[10];
		walkQueueY = new int[10];
		interactingEntity = -1;
		defaultTurnValue = 32;
		runAnimation = -1;
		height = 200;
		idleAnimation = -1;
		turnAnimation = -1;
		hitValues = new int[4];
		hitMarkTypes = new int[4];
		hitCycles = new int[4];
		anInt1517 = -1;
		currentGraphic = -1;
		animationId = -1;
		combatCycle = -1000;
		textCycle = 100;
		anInt1540 = 1;
		aBoolean1541 = false;
		runningFlags = new boolean[10];
		walkAnimation = -1;
		anInt1555 = -1;
		anInt1556 = -1;
		anInt1557 = -1;
	}
	public final int[] walkQueueX;
	public final int[] walkQueueY;
	public int interactingEntity;
	int anInt1503;
	int defaultTurnValue;
	int runAnimation;
	public String textSpoken;
	public int height;
	public int turnDirection;
	int idleAnimation;
	int turnAnimation;
	int textColor;
	final int[] hitValues;
	final int[] hitMarkTypes;
	final int[] hitCycles;
	int anInt1517;
	int anInt1518;
	int anInt1519;
	int currentGraphic;
	int anInt1521;
	int anInt1522;
	int anInt1523;
	int anInt1524;
	int walkQueueLocationIndex;
	public int animationId;
	int anInt1527;
	int anInt1528;
	int animationDelay;
	int anInt1530;
	int textEffects;
	public int combatCycle;
	public int currentHealth;
	public int maxHealth;
	int textCycle;
	int lastUpdate;
	int anInt1538;
	int anInt1539;
	int anInt1540;
	boolean aBoolean1541;
	int anInt1542;
	int anInt1543;
	int anInt1544;
	int anInt1545;
	int anInt1546;
	int anInt1547;
	int anInt1548;
	int anInt1549;
	public int x;
	public int y;
	int anInt1552;
	final boolean[] runningFlags;
	int walkAnimation;
	int anInt1555;
	int anInt1556;
	int anInt1557;
}
