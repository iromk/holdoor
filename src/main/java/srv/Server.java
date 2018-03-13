package srv;

import common.App;
import common.Protocol;

import java.io.*;
import java.net.ServerSocket;

public class Server {

    private ServerSession serverSession;

    private int port;
    private ServerSocket serverSocket;

    public class SocketNotOpenedException extends IOException {}

    public Server() {

        this(Protocol.DEFAULT_PORT);

    }

    public Server(int port) {

        App.set().environment(App.Environment.DEV).init();

        serverSocket = null;
        this.port = port;

        try {
            openSocket(port);
            App.log().info("Socket opened on port " + port);
        } catch (SocketNotOpenedException e) {
            App.log().severe("Cannot open socket on port " + port);
            App.log().severe(e.getMessage());
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
            App.log().info("Server stopped");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
