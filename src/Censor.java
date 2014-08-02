final class Censor {
	public static void loadConfig(Archive archive) {
		Stream fragmentBuf = new Stream(archive.get("fragmentsenc.txt"));
		Stream langBuf = new Stream(archive.get("badenc.txt"));
		Stream domainsBuf = new Stream(archive.get("domainenc.txt"));
		Stream tldlistBuf = new Stream(archive.get("tldlist.txt"));
		Censor.readValues(fragmentBuf, langBuf, domainsBuf, tldlistBuf);
	}

	private static void readValues(Stream fragmentBuf, Stream langBuf, Stream domainBuf, Stream tldlistBuf) {
		Censor.readBadEnc(langBuf);
		Censor.readDomainEnc(domainBuf);
		Censor.readFragmentsEnc(fragmentBuf);
		Censor.readTldList(tldlistBuf);
	}

	private static void readTldList(Stream buffer) {
		int charCode = buffer.getInt();
		Censor.aCharArrayArray624 = new char[charCode][];
		Censor.anIntArray625 = new int[charCode];
		for (int j = 0; j < charCode; j++) {
			Censor.anIntArray625[j] = buffer.getUnsignedByte();
			char ac[] = new char[buffer.getUnsignedByte()];
			for (int k = 0; k < ac.length; k++) {
				ac[k] = (char) buffer.getUnsignedByte();
			}
			Censor.aCharArrayArray624[j] = ac;
		}
	}

	private static void readBadEnc(Stream buffer) {
		int charCode = buffer.getInt();
		Censor.aCharArrayArray621 = new char[charCode][];
		Censor.aByteArrayArrayArray622 = new byte[charCode][][];
		Censor.method493(buffer, Censor.aCharArrayArray621, Censor.aByteArrayArrayArray622);
	}

	private static void readDomainEnc(Stream buffer) {
		int charCode = buffer.getInt();
		Censor.aCharArrayArray623 = new char[charCode][];
		Censor.method494(Censor.aCharArrayArray623, buffer);
	}

	private static void readFragmentsEnc(Stream buffer) {
		Censor.anIntArray620 = new int[buffer.getInt()];
		for (int i = 0; i < Censor.anIntArray620.length; i++) {
			Censor.anIntArray620[i] = buffer.getUnsignedShort();
		}
	}

	private static void method493(Stream buffer, char arg1[][], byte arg2[][][]) {
		for (int chars = 0; chars < arg1.length; chars++) {
			char ac1[] = new char[buffer.getUnsignedByte()];
			for (int k = 0; k < ac1.length; k++) {
				ac1[k] = (char) buffer.getUnsignedByte();
			}
			arg1[chars] = ac1;
			byte byteArray[][] = new byte[buffer.getUnsignedByte()][2];
			for (int l = 0; l < byteArray.length; l++) {
				byteArray[l][0] = (byte) buffer.getUnsignedByte();
				byteArray[l][1] = (byte) buffer.getUnsignedByte();
			}
			if (byteArray.length > 0) {
				arg2[chars] = byteArray;
			}
		}
	}

	private static void method494(char arg0[][], Stream buffer) {
		for (int chars = 0; chars < arg0.length; chars++) {
			char ac[] = new char[buffer.getUnsignedByte()];
			for (int i = 0; i < ac.length; i++) {
				ac[i] = (char) buffer.getUnsignedByte();
			}
			arg0[chars] = ac;
		}
	}

	private static void method495(char arg0[]) {
		int off = 0;
		for (int chars = 0; chars < arg0.length; chars++) {
			if (Censor.method496(arg0[chars])) {
				arg0[off] = arg0[chars];
			} else {
				arg0[off] = ' ';
			}
			if (off == 0 || arg0[off] != ' ' || arg0[off - 1] != ' ') {
				off++;
			}
		}
		for (int chars = off; chars < arg0.length; chars++) {
			arg0[chars] = ' ';
		}
	}

	private static boolean method496(char arg0) {
		return arg0 >= ' ' && arg0 <= '\177' || arg0 == ' ' || arg0 == '\n' || arg0 == '\t' || arg0 == '\243' || arg0 == '\u20AC';
	}

	public static String doCensor(String input) {
		System.currentTimeMillis();
		char dest[] = input.toCharArray();
		Censor.method495(dest);
		String censoredInput = new String(dest).trim();
		dest = censoredInput.toLowerCase().toCharArray();
		String s2 = censoredInput.toLowerCase();
		Censor.method505(dest);
		Censor.method500(dest);
		Censor.method501(dest);
		Censor.method514(dest);
		for (String exception : Censor.exceptions) {
			for (int i = -1; (i = s2.indexOf(exception, i + 1)) != -1;) {
				char exceptions[] = exception.toCharArray();
				System.arraycopy(exceptions, 0, dest, i, exceptions.length);
			}
		}
		Censor.method498(censoredInput.toCharArray(), dest);
		Censor.method499(dest);
		System.currentTimeMillis();
		return new String(dest).trim();
	}

	private static void method498(char dest[], char src[]) {
		for (int chars = 0; chars < dest.length; chars++) {
			if (src[chars] != '*' && Censor.isUpperCaseLetter(dest[chars])) {
				src[chars] = dest[chars];
			}
		}
	}

	private static void method499(char arg0[]) {
		boolean flag = true;
		for (int chars = 0; chars < arg0.length; chars++) {
			char c = arg0[chars];
			if (Censor.isLetter(c)) {
				if (flag) {
					if (Censor.isLowerCaseLetter(c)) {
						flag = false;
					}
				} else if (Censor.isUpperCaseLetter(c)) {
					arg0[chars] = (char) (c + 97 - 65);
				}
			} else {
				flag = true;
			}
		}
	}

	private static void method500(char arg0[]) {
		for (int i = 0; i < 2; i++) {
			for (int chars = Censor.aCharArrayArray621.length - 1; chars >= 0; chars--) {
				Censor.method509(Censor.aByteArrayArrayArray622[chars], arg0, Censor.aCharArrayArray621[chars]);
			}
		}
	}

	private static void method501(char arg0[]) {
		char ac1[] = arg0.clone();
		char ac2[] = { '(', 'a', ')' };
		Censor.method509(null, ac1, ac2);
		char ac3[] = arg0.clone();
		char ac4[] = { 'd', 'o', 't' };
		Censor.method509(null, ac3, ac4);
		for (int i = Censor.aCharArrayArray623.length - 1; i >= 0; i--) {
			Censor.method502(arg0, Censor.aCharArrayArray623[i], ac3, ac1);
		}
	}

	private static void method502(char arg0[], char arg1[], char arg2[], char arg3[]) {
		if (arg1.length > arg0.length) {
			return;
		}
		int j;
		for (int k = 0; k <= arg0.length - arg1.length; k += j) {
			int l = k;
			int i1 = 0;
			j = 1;
			while (l < arg0.length) {
				int j1;
				char c = arg0[l];
				char c1 = '\0';
				if (l + 1 < arg0.length) {
					c1 = arg0[l + 1];
				}
				if (i1 < arg1.length && (j1 = Censor.method511(c, arg1[i1], c1)) > 0) {
					l += j1;
					i1++;
					continue;
				}
				if (i1 == 0) {
					break;
				}
				if ((j1 = Censor.method511(c, arg1[i1 - 1], c1)) > 0) {
					l += j1;
					if (i1 == 1) {
						j++;
					}
					continue;
				}
				if (i1 >= arg1.length || !Censor.method517(c)) {
					break;
				}
				l++;
			}
			if (i1 >= arg1.length) {
				boolean flag1 = false;
				int k1 = Censor.method503(arg0, arg3, k);
				int l1 = Censor.method504(arg2, l - 1, arg0);
				if (k1 > 2 || l1 > 2) {
					flag1 = true;
				}
				if (flag1) {
					for (int i2 = k; i2 < l; i2++) {
						arg0[i2] = '*';
					}
				}
			}
		}
	}

	private static int method503(char arg0[], char arg1[], int arg2) {
		if (arg2 == 0) {
			return 2;
		}
		for (int k = arg2 - 1; k >= 0; k--) {
			if (!Censor.method517(arg0[k])) {
				break;
			}
			if (arg0[k] == '@') {
				return 3;
			}
		}
		int l = 0;
		for (int i1 = arg2 - 1; i1 >= 0; i1--) {
			if (!Censor.method517(arg1[i1])) {
				break;
			}
			if (arg1[i1] == '*') {
				l++;
			}
		}
		if (l >= 3) {
			return 4;
		}
		return !Censor.method517(arg0[arg2 - 1]) ? 0 : 1;
	}

	private static int method504(char arg0[], int arg1, char arg2[]) {
		if (arg1 + 1 == arg2.length) {
			return 2;
		}
		for (int j = arg1 + 1; j < arg2.length; j++) {
			if (!Censor.method517(arg2[j])) {
				break;
			}
			if (arg2[j] == '.' || arg2[j] == ',') {
				return 3;
			}
		}
		int k = 0;
		for (int l = arg1 + 1; l < arg2.length; l++) {
			if (!Censor.method517(arg0[l])) {
				break;
			}
			if (arg0[l] == '*') {
				k++;
			}
		}
		if (k >= 3) {
			return 4;
		}
		return !Censor.method517(arg2[arg1 + 1]) ? 0 : 1;
	}

	private static void method505(char arg0[]) {
		char ac1[] = arg0.clone();
		char ac2[] = { 'd', 'o', 't' };
		Censor.method509(null, ac1, ac2);
		char ac3[] = arg0.clone();
		char ac4[] = { 's', 'l', 'a', 's', 'h' };
		Censor.method509(null, ac3, ac4);
		for (int i = 0; i < Censor.aCharArrayArray624.length; i++) {
			Censor.method506(ac3, Censor.aCharArrayArray624[i], Censor.anIntArray625[i], ac1, arg0);
		}
	}

	private static void method506(char arg0[], char arg1[], int arg2, char arg3[], char arg4[]) {
		if (arg1.length > arg4.length) {
			return;
		}
		int j;
		for (int k = 0; k <= arg4.length - arg1.length; k += j) {
			int l = k;
			int i1 = 0;
			j = 1;
			while (l < arg4.length) {
				int j1;
				char c = arg4[l];
				char c1 = '\0';
				if (l + 1 < arg4.length) {
					c1 = arg4[l + 1];
				}
				if (i1 < arg1.length && (j1 = Censor.method511(c, arg1[i1], c1)) > 0) {
					l += j1;
					i1++;
					continue;
				}
				if (i1 == 0) {
					break;
				}
				if ((j1 = Censor.method511(c, arg1[i1 - 1], c1)) > 0) {
					l += j1;
					if (i1 == 1) {
						j++;
					}
					continue;
				}
				if (i1 >= arg1.length || !Censor.method517(c)) {
					break;
				}
				l++;
			}
			if (i1 >= arg1.length) {
				boolean flag1 = false;
				int k1 = Censor.method507(arg4, k, arg3);
				int l1 = Censor.method508(arg4, arg0, l - 1);
				if (arg2 == 1 && k1 > 0 && l1 > 0) {
					flag1 = true;
				}
				if (arg2 == 2 && (k1 > 2 && l1 > 0 || k1 > 0 && l1 > 2)) {
					flag1 = true;
				}
				if (arg2 == 3 && k1 > 0 && l1 > 2) {
					flag1 = true;
				}
				if (flag1) {
					int i2 = k;
					int j2 = l - 1;
					if (k1 > 2) {
						if (k1 == 4) {
							boolean flag2 = false;
							for (int l2 = i2 - 1; l2 >= 0; l2--) {
								if (flag2) {
									if (arg3[l2] != '*') {
										break;
									}
									i2 = l2;
								} else if (arg3[l2] == '*') {
									i2 = l2;
									flag2 = true;
								}
							}
						}
						boolean flag3 = false;
						for (int i3 = i2 - 1; i3 >= 0; i3--) {
							if (flag3) {
								if (Censor.method517(arg4[i3])) {
									break;
								}
								i2 = i3;
							} else if (!Censor.method517(arg4[i3])) {
								flag3 = true;
								i2 = i3;
							}
						}
					}
					if (l1 > 2) {
						if (l1 == 4) {
							boolean flag4 = false;
							for (int j3 = j2 + 1; j3 < arg4.length; j3++) {
								if (flag4) {
									if (arg0[j3] != '*') {
										break;
									}
									j2 = j3;
								} else if (arg0[j3] == '*') {
									j2 = j3;
									flag4 = true;
								}
							}
						}
						boolean flag5 = false;
						for (int k3 = j2 + 1; k3 < arg4.length; k3++) {
							if (flag5) {
								if (Censor.method517(arg4[k3])) {
									break;
								}
								j2 = k3;
							} else if (!Censor.method517(arg4[k3])) {
								flag5 = true;
								j2 = k3;
							}
						}
					}
					for (int k2 = i2; k2 <= j2; k2++) {
						arg4[k2] = '*';
					}
				}
			}
		}
	}

	private static int method507(char arg0[], int arg1, char arg2[]) {
		if (arg1 == 0) {
			return 2;
		}
		for (int k = arg1 - 1; k >= 0; k--) {
			if (!Censor.method517(arg0[k])) {
				break;
			}
			if (arg0[k] == ',' || arg0[k] == '.') {
				return 3;
			}
		}
		int l = 0;
		for (int i1 = arg1 - 1; i1 >= 0; i1--) {
			if (!Censor.method517(arg2[i1])) {
				break;
			}
			if (arg2[i1] == '*') {
				l++;
			}
		}
		if (l >= 3) {
			return 4;
		}
		return !Censor.method517(arg0[arg1 - 1]) ? 0 : 1;
	}

	private static int method508(char arg0[], char arg1[], int arg2) {
		if (arg2 + 1 == arg0.length) {
			return 2;
		}
		for (int j = arg2 + 1; j < arg0.length; j++) {
			if (!Censor.method517(arg0[j])) {
				break;
			}
			if (arg0[j] == '\\' || arg0[j] == '/') {
				return 3;
			}
		}
		int k = 0;
		for (int l = arg2 + 1; l < arg0.length; l++) {
			if (!Censor.method517(arg1[l])) {
				break;
			}
			if (arg1[l] == '*') {
				k++;
			}
		}
		if (k >= 5) {
			return 4;
		}
		return !Censor.method517(arg0[arg2 + 1]) ? 0 : 1;
	}

	private static void method509(byte arg0[][], char arg1[], char arg2[]) {
		if (arg2.length > arg1.length) {
			return;
		}
		int j;
		for (int k = 0; k <= arg1.length - arg2.length; k += j) {
			int l = k;
			int i1 = 0;
			int j1 = 0;
			j = 1;
			boolean flag1 = false;
			boolean flag2 = false;
			boolean flag3 = false;
			while (l < arg1.length && (!flag2 || !flag3)) {
				int k1;
				char c = arg1[l];
				char c2 = '\0';
				if (l + 1 < arg1.length) {
					c2 = arg1[l + 1];
				}
				if (i1 < arg2.length && (k1 = Censor.method512(c2, c, arg2[i1])) > 0) {
					if (k1 == 1 && Censor.isDigit(c)) {
						flag2 = true;
					}
					if (k1 == 2 && (Censor.isDigit(c) || Censor.isDigit(c2))) {
						flag2 = true;
					}
					l += k1;
					i1++;
					continue;
				}
				if (i1 == 0) {
					break;
				}
				if ((k1 = Censor.method512(c2, c, arg2[i1 - 1])) > 0) {
					l += k1;
					if (i1 == 1) {
						j++;
					}
					continue;
				}
				if (i1 >= arg2.length || !Censor.method518(c)) {
					break;
				}
				if (Censor.method517(c) && c != '\'') {
					flag1 = true;
				}
				if (Censor.isDigit(c)) {
					flag3 = true;
				}
				l++;
				if (++j1 * 100 / (l - k) > 90) {
					break;
				}
			}
			if (i1 >= arg2.length && (!flag2 || !flag3)) {
				boolean flag4 = true;
				if (!flag1) {
					char c1 = ' ';
					if (k - 1 >= 0) {
						c1 = arg1[k - 1];
					}
					char c3 = ' ';
					if (l < arg1.length) {
						c3 = arg1[l];
					}
					byte byte0 = Censor.method513(c1);
					byte byte1 = Censor.method513(c3);
					if (arg0 != null && Censor.method510(byte0, arg0, byte1)) {
						flag4 = false;
					}
				} else {
					boolean flag5 = false;
					boolean flag6 = false;
					if (k - 1 < 0 || Censor.method517(arg1[k - 1]) && arg1[k - 1] != '\'') {
						flag5 = true;
					}
					if (l >= arg1.length || Censor.method517(arg1[l]) && arg1[l] != '\'') {
						flag6 = true;
					}
					if (!flag5 || !flag6) {
						boolean flag7 = false;
						int k2 = k - 2;
						if (flag5) {
							k2 = k;
						}
						for (; !flag7 && k2 < l; k2++) {
							if (k2 >= 0 && (!Censor.method517(arg1[k2]) || arg1[k2] == '\'')) {
								char ac2[] = new char[3];
								int j3;
								for (j3 = 0; j3 < 3; j3++) {
									if (k2 + j3 >= arg1.length || Censor.method517(arg1[k2 + j3]) && arg1[k2 + j3] != '\'') {
										break;
									}
									ac2[j3] = arg1[k2 + j3];
								}
								boolean flag8 = true;
								if (j3 == 0) {
									flag8 = false;
								}
								if (j3 < 3 && k2 - 1 >= 0 && (!Censor.method517(arg1[k2 - 1]) || arg1[k2 - 1] == '\'')) {
									flag8 = false;
								}
								if (flag8 && !Censor.method523(ac2)) {
									flag7 = true;
								}
							}
						}
						if (!flag7) {
							flag4 = false;
						}
					}
				}
				if (flag4) {
					int l1 = 0;
					int i2 = 0;
					int j2 = -1;
					for (int l2 = k; l2 < l; l2++) {
						if (Censor.isDigit(arg1[l2])) {
							l1++;
						} else if (Censor.isLetter(arg1[l2])) {
							i2++;
							j2 = l2;
						}
					}
					if (j2 > -1) {
						l1 -= l - 1 - j2;
					}
					if (l1 <= i2) {
						for (int i3 = k; i3 < l; i3++) {
							arg1[i3] = '*';
						}
					} else {
						j = 1;
					}
				}
			}
		}
	}

	private static boolean method510(byte arg0, byte arg1[][], byte arg2) {
		int i = 0;
		if (arg1[i][0] == arg0 && arg1[i][1] == arg2) {
			return true;
		}
		int j = arg1.length - 1;
		if (arg1[j][0] == arg0 && arg1[j][1] == arg2) {
			return true;
		}
		do {
			int k = (i + j) / 2;
			if (arg1[k][0] == arg0 && arg1[k][1] == arg2) {
				return true;
			}
			if (arg0 < arg1[k][0] || arg0 == arg1[k][0] && arg2 < arg1[k][1]) {
				j = k;
			} else {
				i = k;
			}
		} while (i != j && i + 1 != j);
		return false;
	}

	private static int method511(char arg0, char arg1, char arg2) {
		if (arg1 == arg0) {
			return 1;
		}
		if (arg1 == 'o' && arg0 == '0') {
			return 1;
		}
		if (arg1 == 'o' && arg0 == '(' && arg2 == ')') {
			return 2;
		}
		if (arg1 == 'c' && (arg0 == '(' || arg0 == '<' || arg0 == '[')) {
			return 1;
		}
		if (arg1 == 'e' && arg0 == '\u20AC') {
			return 1;
		}
		if (arg1 == 's' && arg0 == '$') {
			return 1;
		}
		return arg1 != 'l' || arg0 != 'i' ? 0 : 1;
	}

	private static int method512(char arg0, char arg1, char arg2) {
		if (arg2 == arg1) {
			return 1;
		}
		if (arg2 >= 'a' && arg2 <= 'm') {
			if (arg2 == 'a') {
				if (arg1 == '4' || arg1 == '@' || arg1 == '^') {
					return 1;
				}
				return arg1 != '/' || arg0 != '\\' ? 0 : 2;
			}
			if (arg2 == 'b') {
				if (arg1 == '6' || arg1 == '8') {
					return 1;
				}
				return (arg1 != '1' || arg0 != '3') && (arg1 != 'i' || arg0 != '3') ? 0 : 2;
			}
			if (arg2 == 'c') {
				return arg1 != '(' && arg1 != '<' && arg1 != '{' && arg1 != '[' ? 0 : 1;
			}
			if (arg2 == 'd') {
				return (arg1 != '[' || arg0 != ')') && (arg1 != 'i' || arg0 != ')') ? 0 : 2;
			}
			if (arg2 == 'e') {
				return arg1 != '3' && arg1 != '\u20AC' ? 0 : 1;
			}
			if (arg2 == 'f') {
				if (arg1 == 'p' && arg0 == 'h') {
					return 2;
				}
				return arg1 != '\243' ? 0 : 1;
			}
			if (arg2 == 'g') {
				return arg1 != '9' && arg1 != '6' && arg1 != 'q' ? 0 : 1;
			}
			if (arg2 == 'h') {
				return arg1 != '#' ? 0 : 1;
			}
			if (arg2 == 'i') {
				return arg1 != 'y' && arg1 != 'l' && arg1 != 'j' && arg1 != '1' && arg1 != '!' && arg1 != ':' && arg1 != ';' && arg1 != '|' ? 0 : 1;
			}
			if (arg2 == 'j') {
				return 0;
			}
			if (arg2 == 'k') {
				return 0;
			}
			if (arg2 == 'l') {
				return arg1 != '1' && arg1 != '|' && arg1 != 'i' ? 0 : 1;
			}
			if (arg2 == 'm') {
				return 0;
			}
		}
		if (arg2 >= 'n' && arg2 <= 'z') {
			if (arg2 == 'n') {
				return 0;
			}
			if (arg2 == 'o') {
				if (arg1 == '0' || arg1 == '*') {
					return 1;
				}
				return (arg1 != '(' || arg0 != ')') && (arg1 != '[' || arg0 != ']') && (arg1 != '{' || arg0 != '}') && (arg1 != '<' || arg0 != '>') ? 0 : 2;
			}
			if (arg2 == 'p') {
				return 0;
			}
			if (arg2 == 'q') {
				return 0;
			}
			if (arg2 == 'r') {
				return 0;
			}
			if (arg2 == 's') {
				return arg1 != '5' && arg1 != 'z' && arg1 != '$' && arg1 != '2' ? 0 : 1;
			}
			if (arg2 == 't') {
				return arg1 != '7' && arg1 != '+' ? 0 : 1;
			}
			if (arg2 == 'u') {
				if (arg1 == 'v') {
					return 1;
				}
				return (arg1 != '\\' || arg0 != '/') && (arg1 != '\\' || arg0 != '|') && (arg1 != '|' || arg0 != '/') ? 0 : 2;
			}
			if (arg2 == 'v') {
				return (arg1 != '\\' || arg0 != '/') && (arg1 != '\\' || arg0 != '|') && (arg1 != '|' || arg0 != '/') ? 0 : 2;
			}
			if (arg2 == 'w') {
				return arg1 != 'v' || arg0 != 'v' ? 0 : 2;
			}
			if (arg2 == 'x') {
				return (arg1 != ')' || arg0 != '(') && (arg1 != '}' || arg0 != '{') && (arg1 != ']' || arg0 != '[') && (arg1 != '>' || arg0 != '<') ? 0 : 2;
			}
			if (arg2 == 'y') {
				return 0;
			}
			if (arg2 == 'z') {
				return 0;
			}
		}
		if (arg2 >= '0' && arg2 <= '9') {
			if (arg2 == '0') {
				if (arg1 == 'o' || arg1 == 'O') {
					return 1;
				}
				return (arg1 != '(' || arg0 != ')') && (arg1 != '{' || arg0 != '}') && (arg1 != '[' || arg0 != ']') ? 0 : 2;
			}
			if (arg2 == '1') {
				return arg1 != 'l' ? 0 : 1;
			} else {
				return 0;
			}
		}
		if (arg2 == ',') {
			return arg1 != '.' ? 0 : 1;
		}
		if (arg2 == '.') {
			return arg1 != ',' ? 0 : 1;
		}
		if (arg2 == '!') {
			return arg1 != 'i' ? 0 : 1;
		} else {
			return 0;
		}
	}

	private static byte method513(char arg0) {
		if (arg0 >= 'a' && arg0 <= 'z') {
			return (byte) (arg0 - 97 + 1);
		}
		if (arg0 == '\'') {
			return 28;
		}
		if (arg0 >= '0' && arg0 <= '9') {
			return (byte) (arg0 - 48 + 29);
		} else {
			return 27;
		}
	}

	private static void method514(char arg0[]) {
		int j;
		int k = 0;
		int l = 0;
		int i1 = 0;
		while ((j = Censor.method515(arg0, k)) != -1) {
			boolean flag = false;
			for (int j1 = k; j1 >= 0 && j1 < j && !flag; j1++) {
				if (!Censor.method517(arg0[j1]) && !Censor.method518(arg0[j1])) {
					flag = true;
				}
			}
			if (flag) {
				l = 0;
			}
			if (l == 0) {
				i1 = j;
			}
			k = Censor.method516(arg0, j);
			int k1 = 0;
			for (int l1 = j; l1 < k; l1++) {
				k1 = k1 * 10 + arg0[l1] - 48;
			}
			if (k1 > 255 || k - j > 8) {
				l = 0;
			} else {
				l++;
			}
			if (l == 4) {
				for (int i2 = i1; i2 < k; i2++) {
					arg0[i2] = '*';
				}
				l = 0;
			}
		}
	}

	private static int method515(char arg0[], int arg1) {
		for (int k = arg1; k < arg0.length && k >= 0; k++) {
			if (arg0[k] >= '0' && arg0[k] <= '9') {
				return k;
			}
		}
		return -1;
	}

	private static int method516(char arg0[], int arg1) {
		for (int k = arg1; k < arg0.length && k >= 0; k++) {
			if (arg0[k] < '0' || arg0[k] > '9') {
				return k;
			}
		}
		return arg0.length;
	}

	private static boolean method517(char arg0) {
		return !Censor.isLetter(arg0) && !Censor.isDigit(arg0);
	}

	private static boolean method518(char arg0) {
		return arg0 < 'a' || arg0 > 'z' || arg0 == 'v' || arg0 == 'x' || arg0 == 'j' || arg0 == 'q' || arg0 == 'z';
	}

	private static boolean isLetter(char arg0) {
		return arg0 >= 'a' && arg0 <= 'z' || arg0 >= 'A' && arg0 <= 'Z';
	}

	private static boolean isDigit(char arg0) {
		return arg0 >= '0' && arg0 <= '9';
	}

	private static boolean isLowerCaseLetter(char arg0) {
		return arg0 >= 'a' && arg0 <= 'z';
	}

	private static boolean isUpperCaseLetter(char arg0) {
		return arg0 >= 'A' && arg0 <= 'Z';
	}

	private static boolean method523(char arg0[]) {
		boolean flag = true;
		for (int i = 0; i < arg0.length; i++) {
			if (!Censor.isDigit(arg0[i]) && arg0[i] != 0) {
				flag = false;
			}
		}
		if (flag) {
			return true;
		}
		int j = Censor.method524(arg0);
		int k = 0;
		int l = Censor.anIntArray620.length - 1;
		if (j == Censor.anIntArray620[k] || j == Censor.anIntArray620[l]) {
			return true;
		}
		do {
			int i1 = (k + l) / 2;
			if (j == Censor.anIntArray620[i1]) {
				return true;
			}
			if (j < Censor.anIntArray620[i1]) {
				l = i1;
			} else {
				k = i1;
			}
		} while (k != l && k + 1 != l);
		return false;
	}

	private static int method524(char arg0[]) {
		if (arg0.length > 6) {
			return 0;
		}
		int k = 0;
		for (int l = 0; l < arg0.length; l++) {
			char c = arg0[arg0.length - l - 1];
			if (c >= 'a' && c <= 'z') {
				k = k * 38 + c - 97 + 1;
			} else if (c == '\'') {
				k = k * 38 + 27;
			} else if (c >= '0' && c <= '9') {
				k = k * 38 + c - 48 + 28;
			} else if (c != 0) {
				return 0;
			}
		}
		return k;
	}
	private static int[] anIntArray620;
	private static char[][] aCharArrayArray621;
	private static byte[][][] aByteArrayArrayArray622;
	private static char[][] aCharArrayArray623;
	private static char[][] aCharArrayArray624;
	private static int[] anIntArray625;
	private static final String[] exceptions = { "cook", "cook's", "cooks", "seeks", "sheet", "woop", "woops", "faq", "noob", "noobs" };
}
