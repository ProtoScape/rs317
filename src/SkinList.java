/**
 * SkinList [Class18]
 * A list of skins
 */
public final class SkinList {
	public SkinList(Stream buffer) {
		int len = buffer.getUnsignedByte();
		opcodes = new int[len];
		anIntArrayArray343 = new int[len][];
		for (int j = 0; j < len; j++) {
			opcodes[j] = buffer.getUnsignedByte();
		}
		for (int i = 0; i < len; i++) {
			int len_ = buffer.getUnsignedByte();
			anIntArrayArray343[i] = new int[len_];
			for (int j = 0; j < len_; j++) {
				anIntArrayArray343[i][j] = buffer.getUnsignedByte();
			}
		}
	}
	public final int[] opcodes;
        
	public final int[][] anIntArrayArray343;
}
