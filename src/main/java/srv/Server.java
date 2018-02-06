package srv;

import common.Environment;
import common.FileManager;
import common.Protocol;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

public class Server {

    private final Logger LOG = ServerLogger.setup();
    private final Environment environment =  Environment.getInstance();

    private ServerSession serverSession;

    private int port;
    private ServerSocket serverSocket;

    public class SocketNotOpenedException extends IOException {}

    public Server() {

        this(Protocol.DEFAULT_PORT);

    }

    public Server(int port) {

        environment.setLogger(LOG);

        serverSocket = null;
        this.port = port;

        try {
            openSocket(port);
            LOG.info("Socket opened on port " + port);
        } catch (SocketNotOpenedException e) {
            LOG.severe("Cannot open socket on port " + port);
            LOG.severe(e.getMessage());
        }
    }

    private void openSocket(int port) throws SocketNotOpenedException {

        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            serverSocket = null;
            throw new SocketNotOpenedException();
        }

    }

    public void start() {
        serverSession = new ServerSession(serverSocket);
        serverSession.start();
    }

    public void stop() {
        serverSession.interrupt();
        try {
            serverSocket.close();
            LOG.info("Server stopped");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
