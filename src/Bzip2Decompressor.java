final class Bzip2Decompressor {
	public static int decompressBuffer(byte outputBuf[], int decompressedSize, byte inputBuf[], int compressedSize, int offset) {
		synchronized (Bzip2Decompressor.block) {
			Bzip2Decompressor.block.input = inputBuf;
			Bzip2Decompressor.block.nextIn = offset;
			Bzip2Decompressor.block.output = outputBuf;
			Bzip2Decompressor.block.availOut = 0;
			Bzip2Decompressor.block.compressedSize = compressedSize;
			Bzip2Decompressor.block.decompressedSize = decompressedSize;
			Bzip2Decompressor.block.bsLive = 0;
			Bzip2Decompressor.block.bsBuff = 0;
			Bzip2Decompressor.block.totalInLo32 = 0;
			Bzip2Decompressor.block.totalInHi32 = 0;
			Bzip2Decompressor.block.totalOutLo32 = 0;
			Bzip2Decompressor.block.totalOutHi32 = 0;
			Bzip2Decompressor.block.blockNo = 0;
			Bzip2Decompressor.decompress(Bzip2Decompressor.block);
			decompressedSize -= Bzip2Decompressor.block.decompressedSize;
			return decompressedSize;
		}
	}

	private static void getNextFileHeader(Bzip2Block archive) {
		byte state_out_ch = archive.state_out_ch;
		int state_out_len = archive.state_out_len;
		int nBlock_used = archive.nBlock_used;
		int k0 = archive.k0;
		int out[] = Bzip2Block.ll8;
		int nextOut = archive.nextOut;
		byte outBuf[] = archive.output;
		int availOut = archive.availOut;
		int decompressedSize = archive.decompressedSize;
		int decompSize_ = decompressedSize;
		int nBlock_pp = archive.nBlock_pp + 1;
		label0: do {
			if (state_out_len > 0) {
				do {
					if (decompressedSize == 0) {
						break label0;
					}
					if (state_out_len == 1) {
						break;
					}
					outBuf[availOut] = state_out_ch;
					state_out_len--;
					availOut++;
					decompressedSize--;
				} while (true);
				if (decompressedSize == 0) {
					state_out_len = 1;
					break;
				}
				outBuf[availOut] = state_out_ch;
				availOut++;
				decompressedSize--;
			}
			boolean flag = true;
			while (flag) {
				flag = false;
				if (nBlock_used == nBlock_pp) {
					state_out_len = 0;
					break label0;
				}
				state_out_ch = (byte) k0;
				nextOut = out[nextOut];
				byte byte0 = (byte) (nextOut & 0xff);
				nextOut >>= 8;
				nBlock_used++;
				if (byte0 != k0) {
					k0 = byte0;
					if (decompressedSize == 0) {
						state_out_len = 1;
					} else {
						outBuf[availOut] = state_out_ch;
						availOut++;
						decompressedSize--;
						flag = true;
						continue;
					}
					break label0;
				}
				if (nBlock_used != nBlock_pp) {
					continue;
				}
				if (decompressedSize == 0) {
					state_out_len = 1;
					break label0;
				}
				outBuf[availOut] = state_out_ch;
				availOut++;
				decompressedSize--;
				flag = true;
			}
			state_out_len = 2;
			nextOut = out[nextOut];
			byte byte1 = (byte) (nextOut & 0xff);
			nextOut >>= 8;
			if (++nBlock_used != nBlock_pp) {
				if (byte1 != k0) {
					k0 = byte1;
				} else {
					state_out_len = 3;
					nextOut = out[nextOut];
					byte byte2 = (byte) (nextOut & 0xff);
					nextOut >>= 8;
					if (++nBlock_used != nBlock_pp) {
						if (byte2 != k0) {
							k0 = byte2;
						} else {
							nextOut = out[nextOut];
							byte byte3 = (byte) (nextOut & 0xff);
							nextOut >>= 8;
							nBlock_used++;
							state_out_len = (byte3 & 0xff) + 4;
							nextOut = out[nextOut];
							k0 = (byte) (nextOut & 0xff);
							nextOut >>= 8;
							nBlock_used++;
						}
					}
				}
			}
		} while (true);
		int i2 = archive.totalOutLo32;
		archive.totalOutLo32 += decompSize_ - decompressedSize;
		if (archive.totalOutLo32 < i2) {
			archive.totalOutHi32++;
		}
		archive.state_out_ch = state_out_ch;
		archive.state_out_len = state_out_len;
		archive.nBlock_used = nBlock_used;
		archive.k0 = k0;
		Bzip2Block.ll8 = out;
		archive.nextOut = nextOut;
		archive.output = outBuf;
		archive.availOut = availOut;
		archive.decompressedSize = decompressedSize;
	}

	private static void decompress(Bzip2Block block) {
		int tMinLen = 0;
		int tLimit[] = null;
		int tBase[] = null;
		int tPerm[] = null;
		block.blockSize_100k = 1;
		if (Bzip2Block.ll8 == null) {
			Bzip2Block.ll8 = new int[block.blockSize_100k * 100000];
		}
		boolean reading = true;
		while (reading) {
			byte head = Bzip2Decompressor.readUChar(block);
			if (head == 23) {
				return;
			}
			/* Magic numbers */
			head = Bzip2Decompressor.readUChar(block); // 0x41
			head = Bzip2Decompressor.readUChar(block); // 0x59
			head = Bzip2Decompressor.readUChar(block); // 0x26
			head = Bzip2Decompressor.readUChar(block); // 0x53
			head = Bzip2Decompressor.readUChar(block); // 0x59
			/* CRC checksums */
			head = Bzip2Decompressor.readUChar(block);
			head = Bzip2Decompressor.readUChar(block);
			head = Bzip2Decompressor.readUChar(block);
			head = Bzip2Decompressor.readUChar(block);
			/* Randomized block, 1 = randomized */
			head = Bzip2Decompressor.readBit(block);
			block.randomized = head != 0;
			if (block.randomized) {
				System.out.println("PANIC! RANDOMIZED BLOCK!");
			}
			block.origPtr = 0;
			head = Bzip2Decompressor.readUChar(block);
			block.origPtr = block.origPtr << 8 | head & 0xff;
			head = Bzip2Decompressor.readUChar(block);
			block.origPtr = block.origPtr << 8 | head & 0xff;
			head = Bzip2Decompressor.readUChar(block);
			block.origPtr = block.origPtr << 8 | head & 0xff;
			for (int i = 0; i < 16; i++) {
				byte used = Bzip2Decompressor.readBit(block);
				block.inUse16[i] = used == 1;
			}
			for (int i = 0; i < 256; i++) {
				block.inUse[i] = false;
			}
			for (int i = 0; i < 16; i++) {
				if (block.inUse16[i]) {
					for (int j = 0; j < 16; j++) {
						byte v = Bzip2Decompressor.readBit(block);
						if (v == 1) {
							block.inUse[i * 16 + j] = true;
						}
					}
				}
			}
			Bzip2Decompressor.makeMaps(block);
			int alphaSize = block.nInUse + 2;
			int groups = Bzip2Decompressor.getBits(3, block);
			int selectors = Bzip2Decompressor.getBits(15, block);
			for (int i = 0; i < selectors; i++) {
				int selectorValue = 0;
				do {
					byte v = Bzip2Decompressor.readBit(block);
					if (v == 0) {
						break;
					}
					selectorValue++;
				} while (true);
				block.selectorMtf[i] = (byte) selectorValue;
			}
			byte pos[] = new byte[6];
			for (byte i = 0; i < groups; i++) {
				pos[i] = i;
			}
			for (int selectorIdx = 0; selectorIdx < selectors; selectorIdx++) {
				byte selectorMtf = block.selectorMtf[selectorIdx];
				byte curSelectorMtf = pos[selectorMtf];
				for (; selectorMtf > 0; selectorMtf--) {
					pos[selectorMtf] = pos[selectorMtf - 1];
				}
				pos[0] = curSelectorMtf;
				block.selector[selectorIdx] = curSelectorMtf;
			}
			for (int i = 0; i < groups; i++) {
				int curr = Bzip2Decompressor.getBits(5, block);
				for (int j = 0; j < alphaSize; j++) {
					do {
						byte flag = Bzip2Decompressor.readBit(block);
						if (flag == 0) {
							break;
						}
						flag = Bzip2Decompressor.readBit(block);
						if (flag == 0) {
							curr++;
						} else {
							curr--;
						}
					} while (true);
					block.len[i][j] = (byte) curr;
				}
			}
			for (int i = 0; i < groups; i++) {
				byte minLen = 32;
				int maxLen = 0;
				for (int j = 0; j < alphaSize; j++) {
					if (block.len[i][j] > maxLen) {
						maxLen = block.len[i][j];
					}
					if (block.len[i][j] < minLen) {
						minLen = block.len[i][j];
					}
				}
				Bzip2Decompressor.createDecodeTables(block.limit[i], block.base[i], block.perm[i], block.len[i], minLen, maxLen, alphaSize);
				block.minLens[i] = minLen;
			}
			int endOfBlock = block.nInUse + 1;
			int groupNo = -1;
			int groupPos = 0;
			for (int i = 0; i <= 255; i++) {
				block.unzftab[i] = 0;
			}
			int kk = 4095;
			for (int i = 15; i >= 0; i--) {
				for (int j = 15; j >= 0; j--) {
					block.yy[kk] = (byte) (i * 16 + j);
					kk--;
				}
				block.mtf16[i] = kk + 1;
			}
			int last = 0;
			if (groupPos == 0) {
				groupNo++;
				groupPos = 50;
				byte zt = block.selector[groupNo];
				tMinLen = block.minLens[zt];
				tLimit = block.limit[zt];
				tPerm = block.perm[zt];
				tBase = block.base[zt];
			}
			groupPos--;
			int zt = tMinLen;
			int zvec;
			byte bit;
			for (zvec = Bzip2Decompressor.getBits(zt, block); zvec > tLimit[zt]; zvec = zvec << 1 | bit) {
				zt++;
				bit = Bzip2Decompressor.readBit(block);
			}
			for (int nextSym = tPerm[zvec - tBase[zt]]; nextSym != endOfBlock;) {
				if (nextSym == 0 || nextSym == 1) {
					int es = -1;
					int n = 1;
					do {
						if (nextSym == 0) {
							es += n;
						} else if (nextSym == 1) {
							es += 2 * n;
						}
						n *= 2;
						if (groupPos == 0) {
							groupNo++;
							groupPos = 50;
							byte tSelector = block.selector[groupNo];
							tMinLen = block.minLens[tSelector];
							tLimit = block.limit[tSelector];
							tPerm = block.perm[tSelector];
							tBase = block.base[tSelector];
						}
						groupPos--;
						zt = tMinLen;
						byte zj;
						for (zvec = Bzip2Decompressor.getBits(zt, block); zvec > tLimit[zt]; zvec = zvec << 1
							| zj) {
							zt++;
							zj = Bzip2Decompressor.readBit(block);
						}
						nextSym = tPerm[zvec - tBase[zt]];
					} while (nextSym == 0 || nextSym == 1);
					es++;
					byte ch = block.seqToUnseq[block.yy[block.mtf16[0]] & 0xff];
					block.unzftab[ch & 0xff] += es;
					for (; es > 0; es--) {
						Bzip2Block.ll8[last] = ch & 0xff;
						last++;
					}
				} else {
					int nn = nextSym - 1;
					byte tmp;
					if (nn < 16) {
						int pp = block.mtf16[0];
						tmp = block.yy[pp + nn];
						for (; nn > 3; nn -= 4) {
							int k11 = pp + nn;
							block.yy[k11] = block.yy[k11 - 1];
							block.yy[k11 - 1] = block.yy[k11 - 2];
							block.yy[k11 - 2] = block.yy[k11 - 3];
							block.yy[k11 - 3] = block.yy[k11 - 4];
						}
						for (; nn > 0; nn--) {
							block.yy[pp + nn] = block.yy[pp + nn - 1];
						}
						block.yy[pp] = tmp;
					} else {
						int lno = nn / 16;
						int of = nn % 16;
						int pp = block.mtf16[lno] + of;
						tmp = block.yy[pp];
						for (; pp > block.mtf16[lno]; pp--) {
							block.yy[pp] = block.yy[pp - 1];
						}
						block.mtf16[lno]++;
						for (; lno > 0; lno--) {
							block.mtf16[lno]--;
							block.yy[block.mtf16[lno]] = block.yy[block.mtf16[lno - 1] + 16 - 1];
						}
						block.mtf16[0]--;
						block.yy[block.mtf16[0]] = tmp;
						if (block.mtf16[0] == 0) {
							kk = 4095;
							for (int k9 = 15; k9 >= 0; k9--) {
								for (int l9 = 15; l9 >= 0; l9--) {
									block.yy[kk] = block.yy[block.mtf16[k9] + l9];
									kk--;
								}
								block.mtf16[k9] = kk + 1;
							}
						}
					}
					block.unzftab[block.seqToUnseq[tmp & 0xff] & 0xff]++;
					Bzip2Block.ll8[last] = block.seqToUnseq[tmp & 0xff] & 0xff;
					last++;
					if (groupPos == 0) {
						groupNo++;
						groupPos = 50;
						byte gsel = block.selector[groupNo];
						tMinLen = block.minLens[gsel];
						tLimit = block.limit[gsel];
						tPerm = block.perm[gsel];
						tBase = block.base[gsel];
					}
					groupPos--;
					zt = tMinLen;
					byte zj;
					for (zvec = Bzip2Decompressor.getBits(zt, block); zvec > tLimit[zt]; zvec = zvec << 1
						| zj) {
						zt++;
						zj = Bzip2Decompressor.readBit(block);
					}
					nextSym = tPerm[zvec - tBase[zt]];
				}
			}
			block.state_out_len = 0;
			block.state_out_ch = 0;
			block.cftab[0] = 0;
			for (int i = 1; i <= 256; i++) {
				block.cftab[i] = block.unzftab[i - 1];
			}
			for (int i = 1; i <= 256; i++) {
				block.cftab[i] += block.cftab[i - 1];
			}
			for (int i = 0; i < last; i++) {
				byte ch = (byte) (Bzip2Block.ll8[i] & 0xff);
				Bzip2Block.ll8[block.cftab[ch & 0xff]] |= i << 8;
				block.cftab[ch & 0xff]++;
			}
			block.nextOut = Bzip2Block.ll8[block.origPtr] >> 8;
			block.nBlock_used = 0;
			block.nextOut = Bzip2Block.ll8[block.nextOut];
			block.k0 = (byte) (block.nextOut & 0xff);
			block.nextOut >>= 8;
			block.nBlock_used++;
			block.nBlock_pp = last;
			Bzip2Decompressor.getNextFileHeader(block);
			reading = block.nBlock_used == block.nBlock_pp + 1 && block.state_out_len == 0;
		}
	}

	private static byte readUChar(Bzip2Block bzip2Block) {
		return (byte) Bzip2Decompressor.getBits(8, bzip2Block);
	}

	private static byte readBit(Bzip2Block bzip2Block) {
		return (byte) Bzip2Decompressor.getBits(1, bzip2Block);
	}

	private static int getBits(int i, Bzip2Block bzip2Block) {
		int dest;
		do {
			if (bzip2Block.bsLive >= i) {
				int tmp = bzip2Block.bsBuff >> bzip2Block.bsLive - i & (1 << i) - 1;
				bzip2Block.bsLive -= i;
				dest = tmp;
				break;
			}
			bzip2Block.bsBuff = bzip2Block.bsBuff << 8 | bzip2Block.input[bzip2Block.nextIn] & 0xff;
			bzip2Block.bsLive += 8;
			bzip2Block.nextIn++;
			bzip2Block.compressedSize--;
			bzip2Block.totalInLo32++;
			if (bzip2Block.totalInLo32 == 0) {
				bzip2Block.totalInHi32++;
			}
		} while (true);
		return dest;
	}

	private static void makeMaps(Bzip2Block bzip2Block) {
		bzip2Block.nInUse = 0;
		for (int i = 0; i < 256; i++) {
			if (bzip2Block.inUse[i]) {
				bzip2Block.seqToUnseq[bzip2Block.nInUse] = (byte) i;
				bzip2Block.nInUse++;
			}
		}
	}

	private static void createDecodeTables(int limit[], int base[], int perm[], byte len[], int minLen, int maxLen, int alphaSize) {
		int pp = 0;
		for (int i = minLen; i <= maxLen; i++) {
			for (int i_ = 0; i_ < alphaSize; i_++) {
				if (len[i_] == i) {
					perm[pp] = i_;
					pp++;
				}
			}
		}
		for (int i = 0; i < 23; i++) {
			base[i] = 0;
		}
		for (int i = 0; i < alphaSize; i++) {
			base[len[i] + 1]++;
		}
		for (int i = 1; i < 23; i++) {
			base[i] += base[i - 1];
		}
		for (int i = 0; i < 23; i++) {
			limit[i] = 0;
		}
		int vec = 0;
		for (int i = minLen; i <= maxLen; i++) {
			vec += base[i + 1] - base[i];
			limit[i] = vec - 1;
			vec <<= 1;
		}
		for (int i = minLen + 1; i <= maxLen; i++) {
			base[i] = (limit[i - 1] + 1 << 1) - base[i];
		}
	}
	private static final Bzip2Block block = new Bzip2Block();
}
