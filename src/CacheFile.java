import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * CacheFile[Decompressor]
 * Handles the I/O operations for the cache and its indices.
 */
final class CacheFile {
	public CacheFile(RandomAccessFile dataFile, RandomAccessFile indexFile, int storeId) {
		this.storeId = storeId;
		this.dataFile = dataFile;
		this.indexFile = indexFile;
	}

	public synchronized byte[] get(int index) {
		try {
			seek(indexFile, index * 6);
			int off;
			for (int idx = 0; idx < 6; idx += off) {
				off = indexFile.read(CacheFile.buffer, idx, 6 - idx);
				if (off == -1) {
					return null;
				}
			}
			int fileSize = ((CacheFile.buffer[0] & 0xff) << 16) + ((CacheFile.buffer[1] & 0xff) << 8) + (CacheFile.buffer[2] & 0xff);
			int sector = ((CacheFile.buffer[3] & 0xff) << 16) + ((CacheFile.buffer[4] & 0xff) << 8) + (CacheFile.buffer[5] & 0xff);
			if (fileSize < 0) {
				return null;
			}
			if (sector <= 0) {
				return null;
			}
			byte buf[] = new byte[fileSize];
			int read = 0;
			for (int total = 0; read < fileSize; total++) {
				if (sector == 0) {
					return null;
				}
				seek(dataFile, sector * 520);
				int idx_ = 0;
				int unread = fileSize - read;
				if (unread > 512) {
					unread = 512;
				}
				int off_;
				for (; idx_ < unread + 8; idx_ += off_) {
					off_ = dataFile.read(CacheFile.buffer, idx_, unread + 8 - idx_);
					if (off_ == -1) {
						return null;
					}
				}
				int currentFile = ((CacheFile.buffer[0] & 0xff) << 8) + (CacheFile.buffer[1] & 0xff);
				int currentPart = ((CacheFile.buffer[2] & 0xff) << 8) + (CacheFile.buffer[3] & 0xff);
				int nextSector = ((CacheFile.buffer[4] & 0xff) << 16) + ((CacheFile.buffer[5] & 0xff) << 8) + (CacheFile.buffer[6] & 0xff);
				int currentCache = CacheFile.buffer[7] & 0xff;
				if (currentFile != index || currentPart != total || currentCache != storeId) {
					return null;
				}
				if (nextSector < 0 || nextSector > dataFile.length() / 520L) {
					return null;
				}
				for (int idx = 0; idx < unread; idx++) {
					buf[read++] = CacheFile.buffer[idx + 8];
				}
				sector = nextSector;
			}
			return buf;
		} catch (IOException ioexception) {
			return null;
		}
	}

	public synchronized boolean put(int len, byte buf[], int index) {
		boolean exists = put(true, index, len, buf);
		if (!exists) {
			exists = put(false, index, len, buf);
		}
		return exists;
	}

	private synchronized boolean put(boolean exists, int index, int len, byte buf[]) {
		try {
			int sector;
			if (exists) {
				seek(indexFile, index * 6);
				int off;
				for (int idx = 0; idx < 6; idx += off) {
					off = indexFile.read(CacheFile.buffer, idx, 6 - idx);
					if (off == -1) {
						return false;
					}
				}
				sector = ((CacheFile.buffer[3] & 0xff) << 16) + ((CacheFile.buffer[4] & 0xff) << 8) + (CacheFile.buffer[5] & 0xff);
				if (sector <= 0 || sector > dataFile.length() / 520L) {
					return false;
				}
			} else {
				sector = (int) ((dataFile.length() + 519L) / 520L);
				if (sector == 0) {
					sector = 1;
				}
			}
			CacheFile.buffer[0] = (byte) (len >> 16);
			CacheFile.buffer[1] = (byte) (len >> 8);
			CacheFile.buffer[2] = (byte) len;
			CacheFile.buffer[3] = (byte) (sector >> 16);
			CacheFile.buffer[4] = (byte) (sector >> 8);
			CacheFile.buffer[5] = (byte) sector;
			seek(indexFile, index * 6);
			indexFile.write(CacheFile.buffer, 0, 6);
			int written = 0;
			for (int total = 0; written < len; total++) {
				int nextSector = 0;
				if (exists) {
					seek(dataFile, sector * 520);
					int idx;
					int off;
					for (idx = 0; idx < 8; idx += off) {
						off = dataFile.read(CacheFile.buffer, idx, 8 - idx);
						if (off == -1) {
							break;
						}
					}
					if (idx == 8) {
						int currentFile = ((CacheFile.buffer[0] & 0xff) << 8) + (CacheFile.buffer[1] & 0xff);
						int currentPart = ((CacheFile.buffer[2] & 0xff) << 8) + (CacheFile.buffer[3] & 0xff);
						nextSector = ((CacheFile.buffer[4] & 0xff) << 16) + ((CacheFile.buffer[5] & 0xff) << 8) + (CacheFile.buffer[6] & 0xff);
						int currentCache = CacheFile.buffer[7] & 0xff;
						if (currentFile != index || currentPart != total || currentCache != storeId) {
							return false;
						}
						if (nextSector < 0 || nextSector > dataFile.length() / 520L) {
							return false;
						}
					}
				}
				if (nextSector == 0) {
					exists = false;
					nextSector = (int) ((dataFile.length() + 519L) / 520L);
					if (nextSector == 0) {
						nextSector++;
					}
					if (nextSector == sector) {
						nextSector++;
					}
				}
				if (len - written <= 512) {
					nextSector = 0;
				}
				CacheFile.buffer[0] = (byte) (index >> 8);
				CacheFile.buffer[1] = (byte) index;
				CacheFile.buffer[2] = (byte) (total >> 8);
				CacheFile.buffer[3] = (byte) total;
				CacheFile.buffer[4] = (byte) (nextSector >> 16);
				CacheFile.buffer[5] = (byte) (nextSector >> 8);
				CacheFile.buffer[6] = (byte) nextSector;
				CacheFile.buffer[7] = (byte) storeId;
				seek(dataFile, sector * 520);
				dataFile.write(CacheFile.buffer, 0, 8);
				int unwritten = len - written;
				if (unwritten > 512) {
					unwritten = 512;
				}
				dataFile.write(buf, written, unwritten);
				written += unwritten;
				sector = nextSector;
			}
			return true;
		} catch (IOException exception) {
			return false;
		}
	}

	private synchronized void seek(RandomAccessFile randomaccessfile, int pos) throws IOException {
		if (pos < 0 || pos > 0x3c00000) {
			System.out.println("Badseek - pos:" + pos + " len:" + randomaccessfile.length());
			pos = 0x3c00000;
			try {
				Thread.sleep(1000L);
			} catch (Exception exception) {
			}
		}
		randomaccessfile.seek(pos);
	}
	private static final byte[] buffer = new byte[520];
	private final RandomAccessFile dataFile;
	private final RandomAccessFile indexFile;
	private final int storeId;
}
