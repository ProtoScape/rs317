/**
 * Stream
 * Jagex's implementation of the DataInput/OutputStream
 * By default all methods are signed and big endian
 * method400 is a duplicate of getLEShort, therefore I removed it
 */
import java.math.BigInteger;

public final class Stream extends CacheableNode {

	public static Stream allocate() { // create
		synchronized (Stream.queue) {
			Stream stream = null;
			if (Stream.pointer > 0) {
				Stream.pointer--;
				stream = (Stream) Stream.queue.popFront();
			}
			if (stream != null) {
				stream.offset = 0;
				return stream;
			}
		}
		Stream stream = new Stream();
		stream.offset = 0;
		stream.payload = new byte[5000];
		return stream;
	}

	private Stream() {
	}

	public Stream(byte buf[]) {
		payload = buf;
		offset = 0;
	}

	public void writeOpcode(int v) { // createFrame
		payload[offset++] = (byte) (v + cryption.getNextKey());
	}

	public void writeByte(int v) { // writeWordBigEndian
		payload[offset++] = (byte) v;
	}

	public void writeShort(int v) { // writeWord
		payload[offset++] = (byte) (v >> 8);
		payload[offset++] = (byte) v;
	}

	public void write24BitInt(int v) { // writeDWordBigEndian
		payload[offset++] = (byte) (v >> 16);
		payload[offset++] = (byte) (v >> 8);
		payload[offset++] = (byte) v;
	}

	public void writeInt(int v) { // writeDWord
		payload[offset++] = (byte) (v >> 24);
		payload[offset++] = (byte) (v >> 16);
		payload[offset++] = (byte) (v >> 8);
		payload[offset++] = (byte) v;
	}

	public void writeLEInt(int v) { // method403
		payload[offset++] = (byte) v;
		payload[offset++] = (byte) (v >> 8);
		payload[offset++] = (byte) (v >> 16);
		payload[offset++] = (byte) (v >> 24);
	}

	public void writeLong(long v) { // writeQWord
		try {
			payload[offset++] = (byte) (int) (v >> 56);
			payload[offset++] = (byte) (int) (v >> 48);
			payload[offset++] = (byte) (int) (v >> 40);
			payload[offset++] = (byte) (int) (v >> 32);
			payload[offset++] = (byte) (int) (v >> 24);
			payload[offset++] = (byte) (int) (v >> 16);
			payload[offset++] = (byte) (int) (v >> 8);
			payload[offset++] = (byte) (int) v;
		} catch (RuntimeException runtimeexception) {
			Signlink.reporterror("14395, " + 5 + ", " + v + ", " + runtimeexception.toString());
			throw new RuntimeException();
		}
	}

	public void writeString(String s) {
		System.arraycopy(s.getBytes(), 0, payload, offset, s.length());
		offset += s.length();
		payload[offset++] = 10;
	}

	public void writeBytes(byte buf[], int len, int off) {
		for (int i = off; i < off + len; i++) {
			payload[offset++] = buf[i];
		}
	}

	public void writeSizeByte(int v) { // writeBytes
		payload[offset - v - 1] = (byte) v;
	}

	public int getUnsignedByte() { // readUnsignedByte
		return payload[offset++] & 0xff;
	}

	public byte getByte() { // readSignedByte
		return payload[offset++];
	}

	public int getUnsignedShort() { // readUnsignedWord
		offset += 2;
		return ((payload[offset - 2] & 0xff) << 8) + (payload[offset - 1] & 0xff);
	}

	public int getShort() { // readSignedWord
		offset += 2;
		int v = ((payload[offset - 2] & 0xff) << 8) + (payload[offset - 1] & 0xff);
		if (v > 32767) {
			v -= 0x10000;
		}
		return v;
	}

	public int get24BitInt() { // read3Bytes
		offset += 3;
		return ((payload[offset - 3] & 0xff) << 16) + ((payload[offset - 2] & 0xff) << 8) + (payload[offset - 1] & 0xff);
	}

	public int getInt() { // readDWord
		offset += 4;
		return ((payload[offset - 4] & 0xff) << 24) + ((payload[offset - 3] & 0xff) << 16) + ((payload[offset - 2] & 0xff) << 8) + (payload[offset - 1] & 0xff);
	}

	public long getLong() { // readQWord
		long l = getInt() & 0xffffffffL;
		long l1 = getInt() & 0xffffffffL;
		return (l << 32) + l1;
	}

	public String getString() { // readString
		int i = offset;
		while (payload[offset++] != 10) {
			;
		}
		return new String(payload, i, offset - i - 1);
	}

	public byte[] getStringBytes() { // readBytes
		int off = offset;
		while (payload[offset++] != 10) {
			;
		}
		byte buf[] = new byte[offset - off - 1];
		System.arraycopy(payload, off, buf, off - off, offset - 1 - off);
		return buf;
	}

	public void getBytes(int len, int off, byte buf[]) { // readBytes
		for (int i = off; i < off + len; i++) {
			buf[i] = payload[offset++];
		}
	}

	public void beginBitBlock() { // initBitAccess
		bitOffset = offset * MAX_BIT_LEN;
	}

	public int getBits(int size) { // readBits
		int byteOff = bitOffset >> 3;
		int bitOff = MAX_BIT_LEN - (bitOffset & 7);
		int dest = 0;
		bitOffset += size;
		for (; size > bitOff; bitOff = MAX_BIT_LEN) {
			dest += (payload[byteOff++] & Stream.bitMask[bitOff]) << size - bitOff;
			size -= bitOff;
		}
		if (size == bitOff) {
			dest += payload[byteOff] & Stream.bitMask[bitOff];
		} else {
			dest += payload[byteOff] >> bitOff - size & Stream.bitMask[size];
		}
		return dest;
	}

	public void endBitBlock() { // finishBitAccess
		offset = (bitOffset + 7) / MAX_BIT_LEN;
	}

	public int getUnsignedSmart() { // method421
		int i = payload[offset] & 0xff;
		if (i < 0x80) {
			return getUnsignedByte() - 0x40;
		} else {
			return getUnsignedShort() - 0xC000;
		}
	}

	public int getSmart() { // method422
		int i = payload[offset] & 0xff;
		if (i < 0x80) {
			return getUnsignedByte();
		} else {
			return getUnsignedShort() - 0x8000;
		}
	}

	public void encodeRsa() { // doKeys
		int i = offset;
		offset = 0;
		byte buf[] = new byte[i];
		getBytes(i, 0, buf);
		BigInteger bigInt1 = new BigInteger(buf);
		BigInteger bigInt2 = bigInt1;
		byte abyte1[] = bigInt2.toByteArray();
		offset = 0;
		writeByte(abyte1.length);
		writeBytes(abyte1, abyte1.length, 0);
	}

	public void writeByteC(int v) { // method424
		payload[offset++] = (byte) (-v);
	}

	public void writeByteS(int v) { // method425
		payload[offset++] = (byte) (128 - v);
	}

	public int getUnsignedByteA() { // method426
		return payload[offset++] - 128 & 0xff;
	}

	public int getUnsignedByteC() { // method427
		return -payload[offset++] & 0xff;
	}

	public int getUnsignedByteS() { // method428
		return 128 - payload[offset++] & 0xff;
	}

	public byte getByteC() { // method429
		return (byte) (-payload[offset++]);
	}

	public byte getByteS() { // method430
		return (byte) (128 - payload[offset++]);
	}

	public void writeLEShort(int v) { // method431
		payload[offset++] = (byte) v;
		payload[offset++] = (byte) (v >> 8);
	}

	public void writeShortA(int v) { // method432
		payload[offset++] = (byte) (v >> 8);
		payload[offset++] = (byte) (v + 128);
	}

	public void writeLEShortA(int v) { // method433
		payload[offset++] = (byte) (v + 128);
		payload[offset++] = (byte) (v >> 8);
	}

	public int getUnsignedLEShort() { // method434
		offset += 2;
		return ((payload[offset - 1] & 0xff) << 8) + (payload[offset - 2] & 0xff);
	}

	public int getUnsignedLEShortA() { // method435/getValue
		offset += 2;
		return ((payload[offset - 2] & 0xff) << 8) + (payload[offset - 1] - 128 & 0xff);
	}

	public int getUnsignedShortA() { // method436
		offset += 2;
		return ((payload[offset - 1] & 0xff) << 8) + (payload[offset - 2] - 128 & 0xff);
	}

	public int getLEShort() { // method437
		offset += 2;
		int v = ((payload[offset - 1] & 0xff) << 8) + (payload[offset - 2] & 0xff);
		if (v > 32767) {
			v -= 0x10000;
		}
		return v;
	}

	public int getLEShortA() { // method438
		offset += 2;
		int v = ((payload[offset - 1] & 0xff) << 8) + (payload[offset - 2] - 128 & 0xff);
		if (v > 32767) {
			v -= 0x10000;
		}
		return v;
	}

	/**
	 * Gets an int with the offset sequence 2, 1, 4, 3
	 * The opposite of getInt1
	 * 
	 * @return
	 *         The int value
	 */
	public int getInt2() { // method439
		offset += 4;
		return ((payload[offset - 2] & 0xff) << 24) + ((payload[offset - 1] & 0xff) << 16) + ((payload[offset - 4] & 0xff) << 8) + (payload[offset - 3] & 0xff);
	}

	/**
	 * Gets an int with the offset sequence 3, 4, 1, 2
	 * The opposite of getInt2
	 * 
	 * @return
	 */
	public int getInt1() { // method440
		offset += 4;
		return ((payload[offset - 3] & 0xff) << 24) + ((payload[offset - 4] & 0xff) << 16) + ((payload[offset - 1] & 0xff) << 8) + (payload[offset - 2] & 0xff);
	}

	public void writeCBytesA(int len, byte buf[], int off) { // method441
		for (int i = (len + off) - 1; i >= len; i--) {
			payload[offset++] = (byte) (buf[i] + 128);
		}
	}

	public void writeBytesC(int off, int len, byte buf[]) { // method442
		for (int i = (len + off) - 1; i >= len; i--) {
			buf[i] = payload[offset++];
		}
	}
	public byte payload[];
	public int offset;
	public int bitOffset;
	private static final int[] bitMask = { 0, 1, 3, 7, 15, 31, 63, 127, 255, 511, 1023, 2047, 4095, 8191, 16383, 32767, 65535, 0x1ffff, 0x3ffff, 0x7ffff, 0xfffff, 0x1fffff, 0x3fffff, 0x7fffff, 0xffffff, 0x1ffffff, 0x3ffffff, 0x7ffffff, 0xfffffff, 0x1fffffff, 0x3fffffff, 0x7fffffff, -1 };
	public IsaacCipher cryption;
	private static int pointer;
	private static final Queue queue = new Queue();
	private static int MAX_BIT_LEN = 8;
}
