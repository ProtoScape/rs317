/**
 * PlayerInput [TextInput]
 * A utility class that unpacks and repacks player chat
 */
final class PlayerInput {

	public static String unpackMessage(int length, Stream buffer) { // method525
		int off = 0;
		int k = -1;
		for (int i = 0; i < length; i++) {
			int v = buffer.getUnsignedByte();
			int j1 = v >> 4 & 0xf;
			if (k == -1) {
				if (j1 < 13) {
					PlayerInput.messageChars[off++] = PlayerInput.VALID_CHARACTERS[j1];
				} else {
					k = j1;
				}
			} else {
				PlayerInput.messageChars[off++] = PlayerInput.VALID_CHARACTERS[(k << 4) + j1 - 195];
				k = -1;
			}
			j1 = v & 0xf;
			if (k == -1) {
				if (j1 < 13) {
					PlayerInput.messageChars[off++] = PlayerInput.VALID_CHARACTERS[j1];
				} else {
					k = j1;
				}
			} else {
				PlayerInput.messageChars[off++] = PlayerInput.VALID_CHARACTERS[(k << 4) + j1 - 195];
				k = -1;
			}
		}
		boolean flag = true;
		for (int i = 0; i < off; i++) {
			char c = PlayerInput.messageChars[i];
			if (flag && c >= 'a' && c <= 'z') {
				PlayerInput.messageChars[i] += '\uFFE0';
				flag = false;
			}
			if (c == '.' || c == '!' || c == '?') {
				flag = true;
			}
		}
		return new String(PlayerInput.messageChars, 0, off);
	}

	public static void packMessage(String message, Stream buffer) { // method526
		if (message.length() > 80) {
			message = message.substring(0, 80);
		}
		message = message.toLowerCase();
		int tmp = -1;
		for (int i = 0; i < message.length(); i++) {
			char c = message.charAt(i);
			int k = 0;
			for (int j = 0; j < PlayerInput.VALID_CHARACTERS.length; j++) {
				if (c != PlayerInput.VALID_CHARACTERS[j]) {
					continue;
				}
				k = j;
				break;
			}
			if (k > 12) {
				k += 195;
			}
			if (tmp == -1) {
				if (k < 13) {
					tmp = k;
				} else {
					buffer.writeByte(k);
				}
			} else if (k < 13) {
				buffer.writeByte((tmp << 4) + k);
				tmp = -1;
			} else {
				buffer.writeByte((tmp << 4) + (k >> 4));
				tmp = k & 0xf;
			}
		}
		if (tmp != -1) {
			buffer.writeByte(tmp << 4);
		}
	}

	public static String processText(String text) {
		PlayerInput.buffer.offset = 0;
		PlayerInput.packMessage(text, PlayerInput.buffer);
		int off = PlayerInput.buffer.offset;
		PlayerInput.buffer.offset = 0;
		String dest = PlayerInput.unpackMessage(off, PlayerInput.buffer);
		return dest;
	}
	private static final char[] messageChars = new char[100];
	private static final Stream buffer = new Stream(new byte[100]);
	private static final char[] VALID_CHARACTERS = { ' ', 'e', 't', 'a', 'o', 'i', 'h', 'n', 's', 'r', 'd', 'l', 'u', 'm', 'w', 'c', 'y', 'f', 'g', 'p', 'b', 'v', 'k', 'x', 'j', 'q', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', ' ', '!', '?', '.', ',', ':', ';', '(', ')', '-', '&', '*', '\\', '\'', '@', '#', '+', '=', '\243', '$', '%', '"', '[', ']' };
}
