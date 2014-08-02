/**
 * TextUtil [TextClass]
 * An utility class for player names and such
 */
final class TextUtil {
    
	public static long nameToLong(String name) {
		long encoded = 0L;
		for (int i = 0; i < name.length() && i < 12; i++) {
			char c = name.charAt(i);
			encoded *= 37L;
			if (c >= 'A' && c <= 'Z') {
				encoded += 1 + c - 65;
			} else if (c >= 'a' && c <= 'z') {
				encoded += 1 + c - 97;
			} else if (c >= '0' && c <= '9') {
				encoded += 27 + c - 48;
			}
		}
		for (; encoded % 37L == 0L && encoded != 0L; encoded /= 37L) {
			;
		}
		return encoded;
	}

	public static String longToName(long name) {
		try {
			if (name <= 0L || name >= 0x5b5b57f8a98a5dd1L) {
				return "invalid_name";
			}
			if (name % 37L == 0L) {
				return "invalid_name";
			}
			int i = 0;
			char ac[] = new char[12];
			while (name != 0L) {
				long l1 = name;
				name /= 37L;
				ac[11 - i++] = TextUtil.validChars[(int) (l1 - name * 37L)];
			}
			return new String(ac, 12 - i, i);
		} catch (RuntimeException runtimeexception) {
			Signlink.reporterror("81570, " + name + ", " + (byte) -99 + ", " + runtimeexception.toString());
		}
		throw new RuntimeException();
	}

	public static long imageToLong(String name) {
		name = name.toUpperCase();
		long encoded = 0L;
		for (int i = 0; i < name.length(); i++) {
			encoded = encoded * 61L + name.charAt(i) - 32L;
			encoded = encoded + (encoded >> 56) & 0xffffffffffffffL;
		}
		return encoded;
	}

	public static String decodeAddress(int ip) {
		return (ip >> 24 & 0xff) + "." + (ip >> 16 & 0xff) + "." + (ip >> 8 & 0xff) + "." + (ip & 0xff);
	}

	public static String formatName(String name) {
		if (name.length() > 0) {
			char nameChars[] = name.toCharArray();
			for (int i = 0; i < nameChars.length; i++) {
				if (nameChars[i] == '_') {
					nameChars[i] = ' ';
					if (i + 1 < nameChars.length && nameChars[i + 1] >= 'a' && nameChars[i + 1] <= 'z') {
						nameChars[i + 1] = (char) (nameChars[i + 1] + 65 - 97);
					}
				}
			}
			if (nameChars[0] >= 'a' && nameChars[0] <= 'z') {
				nameChars[0] = (char) (nameChars[0] + 65 - 97);
			}
			return new String(nameChars);
		} else {
			return name;
		}
	}

	public static String censorPassword(String password) {
		StringBuffer stringbuffer = new StringBuffer();
		for (int i = 0; i < password.length(); i++) {
			stringbuffer.append("*");
		}
		return stringbuffer.toString();
	}
	private static final char[] validChars = { '_', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
}
