public final class IsaacCipher {
	public IsaacCipher(int seeds[]) {
		memory = new int[256];
		results = new int[256];
		System.arraycopy(seeds, 0, results, 0, seeds.length);
		init();
	}

	public int getNextKey() {
		if (count-- == 0) {
			isaac();
			count = 255;
		}
		return results[count];
	}

	private void isaac() {
		lastResult += ++counter;
		for (int i = 0; i < 256; i++) {
			int j = memory[i];
			if ((i & 3) == 0) {
				accumulator ^= accumulator << 13;
			} else if ((i & 3) == 1) {
				accumulator ^= accumulator >>> 6;
			} else if ((i & 3) == 2) {
				accumulator ^= accumulator << 2;
			} else if ((i & 3) == 3) {
				accumulator ^= accumulator >>> 16;
			}
			accumulator += memory[i + 128 & 0xff];
			int k;
			memory[i] = k = memory[(j & 0x3fc) >> 2] + accumulator + lastResult;
			results[i] = lastResult = memory[(k >> 8 & 0x3fc) >> 2] + j;
		}
	}

	public void init() {
		int a, b, c, d, e, f, g, h;
		a = b = c = d = e = f = g = h = 0x9e3779b9;
		for (int i = 0; i < 4; i++) {
			a ^= b << 11;
			d += a;
			b += c;
			b ^= c >>> 2;
			e += b;
			c += d;
			c ^= d << 8;
			f += c;
			d += e;
			d ^= e >>> 16;
			g += d;
			e += f;
			e ^= f << 10;
			h += e;
			f += g;
			f ^= g >>> 4;
			a += f;
			g += h;
			g ^= h << 8;
			b += g;
			h += a;
			h ^= a >>> 9;
			c += h;
			a += b;
		}

		for (int j = 0; j < 256; j += 8) {
			a += results[j];
			b += results[j + 1];
			c += results[j + 2];
			d += results[j + 3];
			e += results[j + 4];
			f += results[j + 5];
			g += results[j + 6];
			h += results[j + 7];
			a ^= b << 11;
			d += a;
			b += c;
			b ^= c >>> 2;
			e += b;
			c += d;
			c ^= d << 8;
			f += c;
			d += e;
			d ^= e >>> 16;
			g += d;
			e += f;
			e ^= f << 10;
			h += e;
			f += g;
			f ^= g >>> 4;
			a += f;
			g += h;
			g ^= h << 8;
			b += g;
			h += a;
			h ^= a >>> 9;
			c += h;
			a += b;
			memory[j] = a;
			memory[j + 1] = b;
			memory[j + 2] = c;
			memory[j + 3] = d;
			memory[j + 4] = e;
			memory[j + 5] = f;
			memory[j + 6] = g;
			memory[j + 7] = h;
		}

		for (int k = 0; k < 256; k += 8) {
			a += memory[k];
			b += memory[k + 1];
			c += memory[k + 2];
			d += memory[k + 3];
			e += memory[k + 4];
			f += memory[k + 5];
			g += memory[k + 6];
			h += memory[k + 7];
			a ^= b << 11;
			d += a;
			b += c;
			b ^= c >>> 2;
			e += b;
			c += d;
			c ^= d << 8;
			f += c;
			d += e;
			d ^= e >>> 16;
			g += d;
			e += f;
			e ^= f << 10;
			h += e;
			f += g;
			f ^= g >>> 4;
			a += f;
			g += h;
			g ^= h << 8;
			b += g;
			h += a;
			h ^= a >>> 9;
			c += h;
			a += b;
			memory[k] = a;
			memory[k + 1] = b;
			memory[k + 2] = c;
			memory[k + 3] = d;
			memory[k + 4] = e;
			memory[k + 5] = f;
			memory[k + 6] = g;
			memory[k + 7] = h;
		}

		isaac();
		count = 256;
	}

	private int count;
	private final int[] results;
	private final int[] memory;
	private int accumulator;
	private int lastResult;
	private int counter;
}
