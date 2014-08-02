final class HashTable {
	public HashTable() {
		int i = 1024;
		size = i;
		cache = new Node[i];
		for (int k = 0; k < i; k++) {
			Node node = cache[k] = new Node();
			node.next = node;
			node.previous = node;
		}
	}

	public Node get(long key) {
		Node previous = cache[(int) (key & size - 1)];
		for (Node next = previous.next; next != previous; next = next.next) {
			if (next.hash == key) {
				return next;
			}
		}
		return null;
	}

	public void put(Node node, long key) {
		try {
			if (node.previous != null) {
				node.unlink();
			}
			Node previous = cache[(int) (key & size - 1)];
			node.previous = previous.previous;
			node.next = previous;
			node.previous.next = node;
			node.next.previous = node;
			node.hash = key;
			return;
		} catch (RuntimeException runtimeexception) {
			Signlink.reporterror("91499, " + node + ", " + key + ", " + (byte) 7 + ", " + runtimeexception.toString());
		}
		throw new RuntimeException();
	}
	private final int size;
	private final Node[] cache;
}
