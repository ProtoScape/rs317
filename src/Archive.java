/**
 * Archive [StreamLoader]
 * Represents a data archive in the cache.
 * The archive is called on startup to unpack the data.
 */
final class Archive {

	public Archive(byte src[]) {
		Stream stream = new Stream(src);
		int decompressedSize = stream.get24BitInt();
		int compressedSize = stream.get24BitInt();
		if (compressedSize != decompressedSize) {
			byte dest[] = new byte[decompressedSize];
			Bzip2Decompressor.decompressBuffer(dest, decompressedSize, src, compressedSize, 6);
			finalBuffer = dest;
			stream = new Stream(finalBuffer);
			compressedAsWhole = true;
		} else {
			finalBuffer = src;
			compressedAsWhole = false;
		}
		totalFiles = stream.getUnsignedShort();
		identifiers = new int[totalFiles];
		decompressedSizes = new int[totalFiles];
		compressedSizes = new int[totalFiles];
		offsets = new int[totalFiles];
		int off = stream.offset + totalFiles * 10;
		for (int i = 0; i < totalFiles; i++) {
			identifiers[i] = stream.getInt();
			decompressedSizes[i] = stream.get24BitInt();
			compressedSizes[i] = stream.get24BitInt();
			offsets[i] = off;
			off += compressedSizes[i];
		}
	}

	public byte[] get(String name) {
		byte buf[] = null;
		int hash = 0;
		name = name.toUpperCase();
		for (int i = 0; i < name.length(); i++) {
			hash = hash * 61 + name.charAt(i) - 32;
		}
		for (int i = 0; i < totalFiles; i++) {
			if (identifiers[i] == hash) {
				if (buf == null) {
					buf = new byte[decompressedSizes[i]];
				}
				if (!compressedAsWhole) {
					Bzip2Decompressor.decompressBuffer(buf, decompressedSizes[i], finalBuffer, compressedSizes[i], offsets[i]);
				} else {
					System.arraycopy(finalBuffer, offsets[i], buf, 0, decompressedSizes[i]);
				}
				return buf;
			}
		}
		return null;
	}
	private final byte[] finalBuffer;
	private final int totalFiles;
	private final int[] identifiers;
	private final int[] decompressedSizes;
	private final int[] compressedSizes;
	private final int[] offsets;
	private final boolean compressedAsWhole;
}
