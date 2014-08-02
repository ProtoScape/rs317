
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.zip.CRC32;
import java.util.zip.GZIPInputStream;

public final class OnDemandFetcher extends FileRequester implements Runnable {

    private boolean crcMatches(int fileVersion, int fileCrc, byte buf[]) {
        if (buf == null || buf.length < 2) {
            // return false;
            return true;
        }
        int len = buf.length - 2;
        // int version = ((buf[len] & 0xff) << 8) + (buf[len + 1] & 0xff);
        crc32.reset();
        crc32.update(buf, 0, len);
        // int crc = (int) crc32.getValue();
        // return version == fileVersion && crc == fileCrc;
        return true;
    }

    private void readData() {
        try {
            int available = inputStream.available();
            if (expectedSize == 0 && available >= 6) {
                waiting = true;
                for (int i = 0; i < 6; i += inputStream.read(ioBuffer, i, 6 - i)) {
                    ;
                }
                int cache = ioBuffer[0] & 0xff;
                int file = ((ioBuffer[1] & 0xff) << 8) + (ioBuffer[2] & 0xff);
                int size = ((ioBuffer[3] & 0xff) << 8) + (ioBuffer[4] & 0xff);
                int chunk = ioBuffer[5] & 0xff;
                current = null;
                for (OnDemandRequest onDemandRequest = (OnDemandRequest) requested.getFront(); onDemandRequest != null; onDemandRequest = (OnDemandRequest) requested.getNext()) {
                    if (onDemandRequest.dataType == cache && onDemandRequest.id == file) {
                        current = onDemandRequest;
                    }
                    if (current != null) {
                        onDemandRequest.loopCycle = 0;
                    }
                }
                if (current != null) {
                    loopCycle = 0;
                    if (size == 0) {
                        Signlink.reporterror("Rej: " + cache + "," + file);
                        current.buffer = null;
                        if (current.incomplete) {
                            synchronized (aClass19_1358) {
                                aClass19_1358.insertBack(current);
                            }
                        } else {
                            current.unlink();
                        }
                        current = null;
                    } else {
                        if (current.buffer == null && chunk == 0) {
                            current.buffer = new byte[size];
                        }
                        if (current.buffer == null && chunk != 0) {
                            throw new IOException("missing start of file");
                        }
                    }
                }
                completedSize = chunk * 500;
                expectedSize = 500;
                if (expectedSize > size - chunk * 500) {
                    expectedSize = size - chunk * 500;
                }
            }
            if (expectedSize > 0 && available >= expectedSize) {
                waiting = true;
                byte buf[] = ioBuffer;
                int off = 0;
                if (current != null) {
                    buf = current.buffer;
                    off = completedSize;
                }
                for (int i = 0; i < expectedSize; i += inputStream.read(buf, i + off, expectedSize - i)) {
                    ;
                }
                if (expectedSize + completedSize >= buf.length && current != null) {
                    if (clientInstance.cacheIndices[0] != null) {
                        clientInstance.cacheIndices[current.dataType + 1].put(buf.length, buf, current.id);
                    }
                    if (!current.incomplete && current.dataType == 3) {
                        current.incomplete = true;
                        current.dataType = 93;
                    }
                    if (current.incomplete) {
                        synchronized (aClass19_1358) {
                            aClass19_1358.insertBack(current);
                        }
                    } else {
                        current.unlink();
                    }
                }
                expectedSize = 0;
            }
        } catch (IOException ioexception) {
            try {
                socket.close();
            } catch (Exception exception) {
            }
            socket = null;
            inputStream = null;
            outputStream = null;
            expectedSize = 0;
        }
    }

    public void start(Archive archive, Client client) {
        String versionNames[] = {"model_version", "anim_version", "midi_version", "map_version"};
        for (int i = 0; i < 4; i++) {
            byte buf[] = archive.get(versionNames[i]);
            int len = buf.length / 2;
            Stream buffer = new Stream(buf);
            files[i] = new int[len];
            fileStatus[i] = new byte[len];
            for (int l = 0; l < len; l++) {
                files[i][l] = buffer.getUnsignedShort();
            }
        }
        String crcNames[] = {"model_crc", "anim_crc", "midi_crc", "map_crc"};
        for (int i = 0; i < 4; i++) {
            byte buf[] = archive.get(crcNames[i]);
            int len = buf.length / 4;
            Stream buffer = new Stream(buf);
            crcs[i] = new int[len];
            for (int l = 0; l < len; l++) {
                crcs[i][l] = buffer.getInt();
            }
        }
        byte buf[] = archive.get("model_index");
        int len = files[0].length;
        modelIndices = new byte[len];
        for (int i = 0; i < len; i++) {
            if (i < buf.length) {
                modelIndices[i] = buf[i];
            } else {
                modelIndices[i] = 0;
            }
        }
        buf = archive.get("map_index");
        Stream buffer = new Stream(buf);
        len = buf.length / 7;
        mapIndices1 = new int[len];
        mapIndices2 = new int[len];
        mapIndices3 = new int[len];
        mapIndices4 = new int[len];
        for (int i = 0; i < len; i++) {
            mapIndices1[i] = buffer.getUnsignedShort();
            mapIndices2[i] = buffer.getUnsignedShort();
            mapIndices3[i] = buffer.getUnsignedShort();
            mapIndices4[i] = buffer.getUnsignedByte();
        }
        buf = archive.get("anim_index");
        buffer = new Stream(buf);
        len = buf.length / 2;
        animationIndices = new int[len];
        for (int i = 0; i < len; i++) {
            animationIndices[i] = buffer.getUnsignedShort();
        }
        buf = archive.get("midi_index");
        buffer = new Stream(buf);
        len = buf.length;
        midiIndices = new int[len];
        for (int i = 0; i < len; i++) {
            midiIndices[i] = buffer.getUnsignedByte();
        }
        clientInstance = client;
        running = true;
        clientInstance.startRunnable(this, 2);
    }

    public int getRemaining() {
        synchronized (queue) {
            return queue.getSize();
        }
    }

    public void dispose() {
        running = false;
    }

    public void method554(boolean flag) {
        int j = mapIndices1.length;
        for (int k = 0; k < j; k++) {
            if (flag || mapIndices4[k] != 0) {
                method563((byte) 2, 3, mapIndices3[k]);
                method563((byte) 2, 3, mapIndices2[k]);
            }
        }
    }

    public int getFileCount(int id) {
        return files[id].length;
    }

    private void closeRequest(OnDemandRequest onDemandRequest) {
        try {
            if (socket == null) {
                long l = System.currentTimeMillis();
                if (l - openSocketTime < 4000L) {
                    return;
                }
                openSocketTime = l;
                socket = clientInstance.openSocket(43594 + Client.portOff);
                inputStream = socket.getInputStream();
                outputStream = socket.getOutputStream();
                outputStream.write(15);
                for (int j = 0; j < 8; j++) {
                    inputStream.read();
                }
                loopCycle = 0;
            }
            ioBuffer[0] = (byte) onDemandRequest.dataType;
            ioBuffer[1] = (byte) (onDemandRequest.id >> 8);
            ioBuffer[2] = (byte) onDemandRequest.id;
            if (onDemandRequest.incomplete) {
                ioBuffer[3] = 2;
            } else if (!clientInstance.loggedIn) {
                ioBuffer[3] = 1;
            } else {
                ioBuffer[3] = 0;
            }
            outputStream.write(ioBuffer, 0, 4);
            writeLoopCycle = 0;
            anInt1349 = -10000;
            return;
        } catch (IOException ioexception) {
        }
        try {
            socket.close();
        } catch (Exception exception) {
        }
        socket = null;
        inputStream = null;
        outputStream = null;
        expectedSize = 0;
        anInt1349++;
    }

    public int getAnimCount() {
        return animationIndices.length;
    }

    public void method558(int cache, int file) {
        if (cache < 0 || cache > files.length || file < 0 || file > files[cache].length) {
            return;
        }
        if (files[cache][file] == 0) {
            return;
        }
        synchronized (queue) {
            for (OnDemandRequest onDemandRequest = (OnDemandRequest) queue.getFront(); onDemandRequest != null; onDemandRequest = (OnDemandRequest) queue.getNext()) {
                if (onDemandRequest.dataType == cache && onDemandRequest.id == file) {
                    return;
                }
            }
            OnDemandRequest onDemandRequest = new OnDemandRequest();
            onDemandRequest.dataType = cache;
            onDemandRequest.id = file;
            onDemandRequest.incomplete = true;
            synchronized (aClass19_1370) {
                aClass19_1370.insertBack(onDemandRequest);
            }
            queue.insertBack(onDemandRequest);
        }
    }

    public int getModelIndex(int id) {
        return modelIndices[id] & 0xff;
    }

    @Override
    public void run() {
        try {
            while (running) {
                onDemandCycle++;
                int sleepTime = 20;
                if (anInt1332 == 0 && clientInstance.cacheIndices[0] != null) {
                    sleepTime = 50;
                }
                try {
                    Thread.sleep(sleepTime);
                } catch (Exception exception) {
                }
                waiting = true;
                for (int i = 0; i < 100; i++) {
                    if (!waiting) {
                        break;
                    }
                    waiting = false;
                    checkReceived();
                    // handleFailed();
                    if (incomplete == 0 && i >= 5) {
                        break;
                    }
                    getExtras();
                    if (inputStream != null) {
                        readData();
                    }
                }
                boolean incomplete = false;
                for (OnDemandRequest onDemandRequest = (OnDemandRequest) requested.getFront(); onDemandRequest != null; onDemandRequest = (OnDemandRequest) requested.getNext()) {
                    if (onDemandRequest.incomplete) {
                        incomplete = true;
                        onDemandRequest.loopCycle++;
                        if (onDemandRequest.loopCycle > 50) {
                            onDemandRequest.loopCycle = 0;
                            closeRequest(onDemandRequest);
                        }
                    }
                }
                if (!incomplete) {
                    for (OnDemandRequest onDemandRequest = (OnDemandRequest) requested.getFront(); onDemandRequest != null; onDemandRequest = (OnDemandRequest) requested.getNext()) {
                        incomplete = true;
                        onDemandRequest.loopCycle++;
                        if (onDemandRequest.loopCycle > 50) {
                            onDemandRequest.loopCycle = 0;
                            closeRequest(onDemandRequest);
                        }
                    }
                }
                if (incomplete) {
                    loopCycle++;
                    if (loopCycle > 750) {
                        try {
                            socket.close();
                        } catch (Exception exception) {
                        }
                        socket = null;
                        inputStream = null;
                        outputStream = null;
                        expectedSize = 0;
                    }
                } else {
                    loopCycle = 0;
                    statusString = "";
                }
                if (clientInstance.loggedIn && socket != null && outputStream != null && (anInt1332 > 0 || clientInstance.cacheIndices[0] == null)) {
                    writeLoopCycle++;
                    if (writeLoopCycle > 500) {
                        writeLoopCycle = 0;
                        ioBuffer[0] = 0;
                        ioBuffer[1] = 0;
                        ioBuffer[2] = 0;
                        ioBuffer[3] = 10;
                        try {
                            outputStream.write(ioBuffer, 0, 4);
                        } catch (IOException ioexception) {
                            loopCycle = 5000;
                        }
                    }
                }
            }
        } catch (Exception exception) {
            Signlink.reporterror("od_ex " + exception.getMessage());
        }
    }

    public void method560(int file, int cache) {
        if (clientInstance.cacheIndices[0] == null) {
            return;
        }
        if (files[cache][file] == 0) {
            return;
        }
        if (fileStatus[cache][file] == 0) {
            return;
        }
        if (anInt1332 == 0) {
            return;
        }
        OnDemandRequest onDemandRequest = new OnDemandRequest();
        onDemandRequest.dataType = cache;
        onDemandRequest.id = file;
        onDemandRequest.incomplete = false;
        synchronized (aClass19_1344) {
            aClass19_1344.insertBack(onDemandRequest);
        }
    }

    public OnDemandRequest getNextNode() {
        OnDemandRequest onDemandRequest;
        synchronized (aClass19_1358) {
            onDemandRequest = (OnDemandRequest) aClass19_1358.popFront();
        }
        if (onDemandRequest == null) {
            return null;
        }
        synchronized (queue) {
            onDemandRequest.unlinkSub();
        }
        if (onDemandRequest.buffer == null) {
            return onDemandRequest;
        }
        int off = 0;
        try {
            GZIPInputStream gzipinputstream = new GZIPInputStream(new ByteArrayInputStream(onDemandRequest.buffer));
            do {
                if (off == gzipInputBuffer.length) {
                    throw new RuntimeException("buffer overflow!");
                }
                int len = gzipinputstream.read(gzipInputBuffer, off, gzipInputBuffer.length - off);
                if (len == -1) {
                    break;
                }
                off += len;
            } while (true);
        } catch (IOException exception) {
            throw new RuntimeException("error unzipping");
        }
        onDemandRequest.buffer = new byte[off];
        System.arraycopy(gzipInputBuffer, 0, onDemandRequest.buffer, 0, off);
        return onDemandRequest;
    }

    public int getMapCount(int arg0, int arg1, int arg2) {
        int id = (arg2 << 8) + arg1;
        for (int i = 0; i < mapIndices1.length; i++) {
            if (mapIndices1[i] == id) {
                if (arg0 == 0) {
                    return mapIndices2[i];
                } else {
                    return mapIndices3[i];
                }
            }
        }
        return -1;
    }

    @Override
    public void get(int id) {
        method558(0, id);
    }

    public void method563(byte arg0, int cache, int file) {
        if (clientInstance.cacheIndices[0] == null) {
            return;
        }
        if (files[cache][file] == 0) {
            return;
        }
        byte abyte0[] = clientInstance.cacheIndices[cache + 1].get(file);
        if (crcMatches(files[cache][file], crcs[cache][file], abyte0)) {
            return;
        }
        fileStatus[cache][file] = arg0;
        if (arg0 > anInt1332) {
            anInt1332 = arg0;
        }
        totalFiles++;
    }

    public boolean method564(int id) {
        for (int i = 0; i < mapIndices1.length; i++) {
            if (mapIndices3[i] == id) {
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings("unused")
    private void handleFailed() {
        incomplete = 0;
        complete = 0;
        for (OnDemandRequest onDemandRequest = (OnDemandRequest) requested.getFront(); onDemandRequest != null; onDemandRequest = (OnDemandRequest) requested.getNext()) {
            if (onDemandRequest.incomplete) {
                incomplete++;
            } else {
                complete++;
            }
        }
        while (incomplete < 10) {
            OnDemandRequest onDemandRequest = (OnDemandRequest) next.popFront();
            if (onDemandRequest == null) {
                break;
            }
            if (fileStatus[onDemandRequest.dataType][onDemandRequest.id] != 0) {
                filesLoaded++;
            }
            fileStatus[onDemandRequest.dataType][onDemandRequest.id] = 0;
            requested.insertBack(onDemandRequest);
            incomplete++;
            closeRequest(onDemandRequest);
            waiting = true;
        }
    }

    public void method566() {
        synchronized (aClass19_1344) {
            aClass19_1344.clear();
        }
    }

    private void checkReceived() {
        OnDemandRequest onDemandRequest;
        synchronized (aClass19_1370) {
            onDemandRequest = (OnDemandRequest) aClass19_1370.popFront();
        }
        while (onDemandRequest != null) {
            waiting = true;
            byte buf[] = null;
            if (clientInstance.cacheIndices[0] != null) {
                buf = clientInstance.cacheIndices[onDemandRequest.dataType + 1].get(onDemandRequest.id);
            }
            if (!crcMatches(files[onDemandRequest.dataType][onDemandRequest.id], crcs[onDemandRequest.dataType][onDemandRequest.id], buf)) {
                buf = null;
            }
            synchronized (aClass19_1370) {
                if (buf == null) {
                    next.insertBack(onDemandRequest);
                } else {
                    onDemandRequest.buffer = buf;
                    synchronized (aClass19_1358) {
                        aClass19_1358.insertBack(onDemandRequest);
                    }
                }
                onDemandRequest = (OnDemandRequest) aClass19_1370.popFront();
            }
        }
    }

    private void getExtras() {
        while (incomplete == 0 && complete < 10) {
            if (anInt1332 == 0) {
                break;
            }
            OnDemandRequest onDemandRequest;
            synchronized (aClass19_1344) {
                onDemandRequest = (OnDemandRequest) aClass19_1344.popFront();
            }
            while (onDemandRequest != null) {
                if (fileStatus[onDemandRequest.dataType][onDemandRequest.id] != 0) {
                    fileStatus[onDemandRequest.dataType][onDemandRequest.id] = 0;
                    requested.insertBack(onDemandRequest);
                    closeRequest(onDemandRequest);
                    waiting = true;
                    if (filesLoaded < totalFiles) {
                        filesLoaded++;
                    }
                    statusString = "Loading extra files - " + filesLoaded * 100 / totalFiles + "%";
                    complete++;
                    if (complete == 10) {
                        return;
                    }
                }
                synchronized (aClass19_1344) {
                    onDemandRequest = (OnDemandRequest) aClass19_1344.popFront();
                }
            }
            for (int i = 0; i < 4; i++) {
                byte buf[] = fileStatus[i];
                int len = buf.length;
                for (int j = 0; j < len; j++) {
                    if (buf[j] == anInt1332) {
                        buf[j] = 0;
                        OnDemandRequest extras = new OnDemandRequest();
                        extras.dataType = i;
                        extras.id = j;
                        extras.incomplete = false;
                        requested.insertBack(extras);
                        closeRequest(extras);
                        waiting = true;
                        if (filesLoaded < totalFiles) {
                            filesLoaded++;
                        }
                        statusString = "Loading extra files - " + filesLoaded * 100 / totalFiles + "%";
                        complete++;
                        if (complete == 10) {
                            return;
                        }
                    }
                }
            }
            anInt1332--;
        }
    }

    public boolean getMidiIndex(int id) {
        return midiIndices[id] == 1;
    }

    public OnDemandFetcher() {
        requested = new Deque();
        statusString = "";
        crc32 = new CRC32();
        ioBuffer = new byte[500];
        fileStatus = new byte[4][];
        aClass19_1344 = new Deque();
        running = true;
        waiting = false;
        aClass19_1358 = new Deque();
        gzipInputBuffer = new byte[65000];
        queue = new Queue();
        files = new int[4][];
        crcs = new int[4][];
        next = new Deque();
        aClass19_1370 = new Deque();
    }
    private int totalFiles;
    private final Deque requested;
    private int anInt1332;
    public String statusString;
    private int writeLoopCycle;
    private long openSocketTime;
    private int[] mapIndices3;
    private final CRC32 crc32;
    private final byte[] ioBuffer;
    public int onDemandCycle;
    private final byte[][] fileStatus;
    private Client clientInstance;
    private final Deque aClass19_1344;
    private int completedSize;
    private int expectedSize;
    private int[] midiIndices;
    public int anInt1349;
    private int[] mapIndices2;
    private int filesLoaded;
    private boolean running;
    private OutputStream outputStream;
    private int[] mapIndices4;
    private boolean waiting;
    private final Deque aClass19_1358;
    private final byte[] gzipInputBuffer;
    private int[] animationIndices;
    private final Queue queue;
    private InputStream inputStream;
    private Socket socket;
    private final int[][] files;
    private final int[][] crcs;
    private int incomplete;
    private int complete;
    private final Deque next;
    private OnDemandRequest current;
    private final Deque aClass19_1370;
    private int[] mapIndices1;
    private byte[] modelIndices;
    private int loopCycle;
}
