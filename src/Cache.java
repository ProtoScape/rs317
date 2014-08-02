/**
 * Cache [MRUNodes]
 * The memory cache for a map region.
 * This includes configs for all of the objects on the scene.
 * Upon entering a new map region the cache is reset.
 */
public final class Cache {
	public Cache(int size) {
		emptyNode = new CacheableNode();
		recent = new Queue();
		srcCapacity = size;
		capacity = size;
		cache = new HashTable();
	}

	public CacheableNode get(long key) {
		CacheableNode cacheableNode = (CacheableNode) cache.get(key);
		if (cacheableNode != null) {
			recent.insertBack(cacheableNode);
		}
		return cacheableNode;
	}

	public void put(CacheableNode cacheableNode, long key) {
		try {
			if (capacity == 0) {
				CacheableNode node = recent.popFront();
				node.unlink();
				node.unlinkSub();
				if (node == emptyNode) {
					CacheableNode empty = recent.popFront();
					empty.unlink();
					empty.unlinkSub();
				}
			} else {
				capacity--;
			}
			cache.put(cacheableNode, key);
			recent.insertBack(cacheableNode);
			return;
		} catch (RuntimeException runtimeexception) {
			Signlink.reporterror("47547, " + cacheableNode + ", " + key + ", " + (byte) 2 + ", "
				+ runtimeexception.toString());
		}
		throw new RuntimeException();
	}

	public void clear() {
		do {
			CacheableNode cacheableNode = recent.popFront();
			if (cacheableNode != null) {
				cacheableNode.unlink();
				cacheableNode.unlinkSub();
			} else {
				capacity = srcCapacity;
				return;
			}
		} while (true);
	}
	private final CacheableNode emptyNode;
	private final int srcCapacity;
	private int capacity;
	private final HashTable cache;
	private final Queue recent;
}
