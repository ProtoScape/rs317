import java.applet.Applet;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;

public final class Signlink implements Runnable {

	public static void startpriv(InetAddress inetaddress) {
		Signlink.threadliveid = (int) (Math.random() * 99999999D);
		if (Signlink.active) {
			try {
				Thread.sleep(500L);
			} catch (Exception exception) {
			}
			Signlink.active = false;
		}
		Signlink.socketreq = 0;
		Signlink.threadreq = null;
		Signlink.dnsreq = null;
		Signlink.savereq = null;
		Signlink.urlreq = null;
		Signlink.socketip = inetaddress;
		Thread thread = new Thread(new Signlink());
		thread.setDaemon(true);
		thread.start();
		while (!Signlink.active) {
			try {
				Thread.sleep(50L);
			} catch (Exception exception) {
			}
		}
	}

	@Override
	public void run() {
		Signlink.active = true;
		String s = Signlink.findcachedir();
		Signlink.uid = Signlink.getuid(s);
		try {
			File file = new File(s + "main_file_cache.dat");
			if (file.exists() && file.length() > 0x3200000L) {
				file.delete();
			}
			Signlink.cache_dat = new RandomAccessFile(s + "main_file_cache.dat", "rw");
			for (int j = 0; j < 5; j++) {
				Signlink.cache_idx[j] = new RandomAccessFile(s + "main_file_cache.idx" + j, "rw");
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		for (int i = Signlink.threadliveid; Signlink.threadliveid == i;) {
			if (Signlink.socketreq != 0) {
				try {
					Signlink.socket = new Socket(Signlink.socketip, Signlink.socketreq);
				} catch (Exception exception) {
					Signlink.socket = null;
				}
				Signlink.socketreq = 0;
			} else if (Signlink.threadreq != null) {
				Thread thread = new Thread(Signlink.threadreq);
				thread.setDaemon(true);
				thread.start();
				thread.setPriority(Signlink.threadreqpri);
				Signlink.threadreq = null;
			} else if (Signlink.dnsreq != null) {
				try {
					Signlink.dns = InetAddress.getByName(Signlink.dnsreq).getHostName();
				} catch (Exception exception) {
					Signlink.dns = "unknown";
				}
				Signlink.dnsreq = null;
			} else if (Signlink.savereq != null) {
				if (Signlink.savebuf != null) {
					try {
						FileOutputStream fileoutputstream = new FileOutputStream(s + Signlink.savereq);
						fileoutputstream.write(Signlink.savebuf, 0, Signlink.savelen);
						fileoutputstream.close();
					} catch (Exception exception) {
					}
				}
				if (Signlink.waveplay) {
					Signlink.waveplay = false;
				}
				if (Signlink.midiplay) {
					Signlink.midi = s + Signlink.savereq;
					Signlink.midiplay = false;
				}
				Signlink.savereq = null;
			} else if (Signlink.urlreq != null) {
				try {
					System.out.println("urlstream");
					Signlink.urlstream = new DataInputStream(new URL(Signlink.mainapp.getCodeBase(), Signlink.urlreq).openStream());
				} catch (Exception exception) {
					Signlink.urlstream = null;
				}
				Signlink.urlreq = null;
			}
			try {
				Thread.sleep(50L);
			} catch (Exception exception) {
			}
		}
	}

	public static String findcachedir() {
		return "cache" + System.getProperty("file.separator");
	}

	public static String findcachedirORIG() {
		String as[] = { "c:/windows/", "c:/winnt/", "d:/windows/", "d:/winnt/", "e:/windows/", "e:/winnt/", "f:/windows/", "f:/winnt/", "c:/", "~/", "/tmp/", "", "c:/rscache", "/rscache" };
		if (Signlink.storeid < 32 || Signlink.storeid > 34) {
			Signlink.storeid = 32;
		}
		String s = ".file_store_" + Signlink.storeid;
		for (String s1 : as) {
			try {
				if (s1.length() > 0) {
					File file = new File(s1);
					if (!file.exists()) {
						continue;
					}
				}
				File file1 = new File(s1 + s);
				if (file1.exists() || file1.mkdir()) {
					return s1 + s + "/";
				}
			} catch (Exception exception) {
			}
		}
		return null;
	}

	private static int getuid(String s) {
		try {
			File file = new File(s + "uid.dat");
			if (!file.exists() || file.length() < 4L) {
				DataOutputStream dataoutputstream = new DataOutputStream(new FileOutputStream(s + "uid.dat"));
				dataoutputstream.writeInt((int) (Math.random() * 99999999D));
				dataoutputstream.close();
			}
		} catch (Exception exception) {
		}
		try {
			DataInputStream datainputstream = new DataInputStream(new FileInputStream(s + "uid.dat"));
			int i = datainputstream.readInt();
			datainputstream.close();
			return i + 1;
		} catch (Exception exception) {
			return 0;
		}
	}

	public static synchronized Socket opensocket(int i) throws IOException {
		for (Signlink.socketreq = i; Signlink.socketreq != 0;) {
			try {
				Thread.sleep(50L);
			} catch (Exception exception) {
			}
		}
		if (Signlink.socket == null) {
			throw new IOException("could not open socket");
		} else {
			return Signlink.socket;
		}
	}

	public static synchronized DataInputStream openurl(String s) throws IOException {
		for (Signlink.urlreq = s; Signlink.urlreq != null;) {
			try {
				Thread.sleep(50L);
			} catch (Exception exception) {
			}
		}
		if (Signlink.urlstream == null) {
			throw new IOException("could not open: " + s);
		} else {
			return Signlink.urlstream;
		}
	}

	public static synchronized void dnslookup(String s) {
		Signlink.dns = s;
		Signlink.dnsreq = s;
	}

	public static synchronized void startthread(Runnable runnable, int i) {
		Signlink.threadreqpri = i;
		Signlink.threadreq = runnable;
	}

	public static synchronized boolean wavesave(byte abyte0[], int i) {
		if (i > 0x1e8480) {
			return false;
		}
		if (Signlink.savereq != null) {
			return false;
		} else {
			Signlink.wavepos = (Signlink.wavepos + 1) % 5;
			Signlink.savelen = i;
			Signlink.savebuf = abyte0;
			Signlink.waveplay = true;
			Signlink.savereq = "sound" + Signlink.wavepos + ".wav";
			return true;
		}
	}

	public static synchronized boolean wavereplay() {
		if (Signlink.savereq != null) {
			return false;
		} else {
			Signlink.savebuf = null;
			Signlink.waveplay = true;
			Signlink.savereq = "sound" + Signlink.wavepos + ".wav";
			return true;
		}
	}

	public static synchronized void midisave(byte abyte0[], int i) {
		if (i > 0x1e8480) {
			return;
		}
		if (Signlink.savereq != null) {
		} else {
			Signlink.midipos = (Signlink.midipos + 1) % 5;
			Signlink.savelen = i;
			Signlink.savebuf = abyte0;
			Signlink.midiplay = true;
			Signlink.savereq = "jingle" + Signlink.midipos + ".mid";
		}
	}

	public static void reporterror(String s) {
		System.out.println("Error: " + s);
	}

	private Signlink() {
	}
	public static final int clientversion = 317;
	public static int uid;
	public static int storeid = 32;
	public static RandomAccessFile cache_dat = null;
	public static final RandomAccessFile[] cache_idx = new RandomAccessFile[5];
	public static boolean sunjava;
	public static final Applet mainapp = null;
	private static boolean active;
	private static int threadliveid;
	private static InetAddress socketip;
	private static int socketreq;
	private static Socket socket = null;
	private static int threadreqpri = 1;
	private static Runnable threadreq = null;
	private static String dnsreq = null;
	public static String dns = null;
	private static String urlreq = null;
	private static DataInputStream urlstream = null;
	private static int savelen;
	private static String savereq = null;
	private static byte[] savebuf = null;
	private static boolean midiplay;
	private static int midipos;
	public static String midi = null;
	public static int midivol;
	public static int midifade;
	private static boolean waveplay;
	private static int wavepos;
	public static int wavevol;
	public static boolean reporterror = true;
	public static String errorname = "";
}
