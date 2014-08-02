final class Queue {
	public Queue() {
		head = new CacheableNode();
		head.nextNode = head;
		head.previousNode = head;
	}

	public void insertBack(CacheableNode cacheableNode) {
		if (cacheableNode.previousNode != null) {
			cacheableNode.unlinkSub();
		}
		cacheableNode.previousNode = head.previousNode;
		cacheableNode.nextNode = head;
		cacheableNode.previousNode.nextNode = cacheableNode;
		cacheableNode.nextNode.previousNode = cacheableNode;
	}

	public CacheableNode popFront() {
		CacheableNode cacheableNode = head.nextNode;
		if (cacheableNode == head) {
			return null;
		} else {
			cacheableNode.unlinkSub();
			return cacheableNode;
		}
	}

	public CacheableNode getFront() {
		CacheableNode cacheableNode = head.nextNode;
		if (cacheableNode == head) {
			current = null;
			return null;
		} else {
			current = cacheableNode.nextNode;
			return cacheableNode;
		}
	}

	public CacheableNode getNext() {
		CacheableNode cacheableNode = current;
		if (cacheableNode == head) {
			current = null;
			return null;
		} else {
			current = cacheableNode.nextNode;
			return cacheableNode;
		}
	}

	public int getSize() {
		int i = 0;
		for (CacheableNode cacheableNode = head.nextNode; cacheableNode != head; cacheableNode = cacheableNode.nextNode) {
			i++;
		}
		return i;
	}
	private final CacheableNode head;
	private CacheableNode current;
}
