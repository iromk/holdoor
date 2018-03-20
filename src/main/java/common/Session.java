package common;

import common.net.SessionContext;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public abstract class Session extends Thread {

    protected Socket socket;

    public static final long TIMEOUT = 120_000;

    public void close() {

    }

    public SessionContext getSuitableContextController() { throw new RuntimeException("getSuitableContextController not implemented."); }
    public InputStream getInputStream() throws IOException { return socket.getInputStream(); }
    public OutputStream getOutputStream() throws IOException { return socket.getOutputStream(); }
}
