public class CacheableNode extends Node {
	public final void unlinkSub() {
		if (previousNode == null) {
		} else {
			previousNode.nextNode = nextNode;
			nextNode.previousNode = previousNode;
			nextNode = null;
			previousNode = null;
		}
	}

	public CacheableNode() {
	}
	public CacheableNode nextNode;
	CacheableNode previousNode;
}
