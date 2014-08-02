public final class OnDemandRequest extends CacheableNode {
	public OnDemandRequest() {
		incomplete = true;
	}
	int dataType;
	byte buffer[];
	int id;
	boolean incomplete;
	int loopCycle;
}
