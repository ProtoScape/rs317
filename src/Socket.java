import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

final class Socket implements Runnable {

	public Socket(Applet applet, java.net.Socket socket) throws IOException {
		closed = false;
		isWriter = false;
		hasIOError = false;
		rsApplet = applet;
		this.socket = socket;
		socket.setSoTimeout(30000);
		socket.setTcpNoDelay(true);
		inputStream = socket.getInputStream();
		outputStream = socket.getOutputStream();
	}

	public void close() {
		closed = true;
		try {
			if (inputStream != null) {
				inputStream.close();
			}
			if (outputStream != null) {
				outputStream.close();
			}
			if (socket != null) {
				socket.close();
			}
		} catch (IOException exception) {
			System.out.println("Error closing stream");
		}
		isWriter = false;
		synchronized (this) {
			notify();
		}
		buffer = null;
	}

	public int read() throws IOException {
		if (closed) {
			return 0;
		} else {
			return inputStream.read();
		}
	}

	public int available() throws IOException {
		if (closed) {
			return 0;
		} else {
			return inputStream.available();
		}
	}

	public void flushInputStream(byte buf[], int len) throws IOException {
		int i = 0;
		if (closed) {
			return;
		}
		int k;
		for (; len > 0; len -= k) {
			k = inputStream.read(buf, i, len);
			if (k <= 0) {
				throw new IOException("EOF");
			}
			i += k;
		}
	}

	public void queueBytes(int len, byte buf[]) throws IOException {
		if (closed) {
			return;
		}
		if (hasIOError) {
			hasIOError = false;
			throw new IOException("Error in writer thread");
		}
		if (buffer == null) {
			buffer = new byte[5000];
		}
		synchronized (this) {
			for (int l = 0; l < len; l++) {
				buffer[buffIndex] = buf[l];
				buffIndex = (buffIndex + 1) % 5000;
				if (buffIndex == (writeIndex + 4900) % 5000) {
					throw new IOException("buffer overflow");
				}
			}
			if (!isWriter) {
				isWriter = true;
				rsApplet.startRunnable(this, 3);
			}
			notify();
		}
	}

	@Override
	public void run() {
		while (isWriter) {
			int i;
			int j;
			synchronized (this) {
				if (buffIndex == writeIndex) {
					try {
						wait();
					} catch (InterruptedException interruptedexception) {
					}
				}
				if (!isWriter) {
					return;
				}
				j = writeIndex;
				if (buffIndex >= writeIndex) {
					i = buffIndex - writeIndex;
				} else {
					i = 5000 - writeIndex;
				}
			}
			if (i > 0) {
				try {
					outputStream.write(buffer, j, i);
				} catch (IOException ioexception) {
					hasIOError = true;
				}
				writeIndex = (writeIndex + i) % 5000;
				try {
					if (buffIndex == writeIndex) {
						outputStream.flush();
					}
				} catch (IOException ioexception) {
					hasIOError = true;
				}
			}
		}
	}

	public void printDebug() {
		System.out.println("dummy:" + closed);
		System.out.println("tcycl:" + writeIndex);
		System.out.println("tnum:" + buffIndex);
		System.out.println("writer:" + isWriter);
		System.out.println("ioerror:" + hasIOError);
		try {
			System.out.println("available:" + available());
		} catch (IOException ioexception) {
		}
	}
	private InputStream inputStream;
	private OutputStream outputStream;
	private final java.net.Socket socket;
	private boolean closed;
	private final Applet rsApplet;
	private byte[] buffer;
	private int writeIndex;
	private int buffIndex;
	private boolean isWriter;
	private boolean hasIOError;
}
