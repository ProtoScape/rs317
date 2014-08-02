/**
 * GroundTile [Ground]
 * Represents a tile on the scene
 */
public final class GroundTile extends Node {
	public GroundTile(int plane, int x, int y) {
		interactiveObjects = new InteractiveObject[5];
		anIntArray1319 = new int[5];
		anInt1310 = anInt1307 = plane;
		anInt1308 = x;
		anInt1309 = y;
	}
	int anInt1307;
	final int anInt1308;
	final int anInt1309;
	final int anInt1310;
	public ShapedTile aShapedTile_1311;
	public Tile aTile_1312;
	public WallObject wallObject;
	public WallDecoration wallDecoration;
	public GroundDecoration groundDecoration;
	public ItemPile itemPile;
	int anInt1317;
	public final InteractiveObject[] interactiveObjects;
	final int[] anIntArray1319;
	int anInt1320;
	int anInt1321;
	boolean aBoolean1322;
	boolean aBoolean1323;
	boolean aBoolean1324;
	int anInt1325;
	int anInt1326;
	int anInt1327;
	int anInt1328;
	public GroundTile aGroundTile_1329;
}
