final class MouseRecorder implements Runnable {
	@Override
	public void run() {
		while (running) {
			synchronized (lock) {
				if (cacheIndex < 500) {
					mouseXCache[cacheIndex] = client.mouseX;
					mouseYCache[cacheIndex] = client.mouseY;
					cacheIndex++;
				}
			}
			try {
				Thread.sleep(50L);
			} catch (Exception exception) {
			}
		}
	}

	public MouseRecorder(Client client) {
		lock = new Object();
		mouseYCache = new int[500];
		running = true;
		mouseXCache = new int[500];
		this.client = client;
	}
	private Client client;
	public final Object lock;
	public final int[] mouseYCache;
	public boolean running;
	public final int[] mouseXCache;
	public int cacheIndex;
}
