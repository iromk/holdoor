package srv;

import common.core.App;
import common.Protocol;
import srv.data.Name;

import java.io.*;
import java.net.ServerSocket;

public class Server {

    private SessionManager sessionManager;

    private int port;
    private ServerSocket serverSocket;

    public class SocketNotOpenedException extends IOException {}

    public Server() {

        this(Protocol.DEFAULT_PORT);

    }

    public Server(int port) {

        serverSocket = null;
        this.port = port;

        try {
            openSocket(port);
            App.log().info("Socket opened on port " + port);
        } catch (SocketNotOpenedException e) {
            App.log().severe("Cannot open socket on port " + port);
            App.log().severe(App.getStackTrace(e));
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
        sessionManager = new SessionManager(serverSocket);
        sessionManager.start();
    }

    public SessionManager getSessionManager() {
        return sessionManager;
    }

    synchronized public void stop() {
        try {
            if (sessionManager == null) {
                App.log().warning("There is no active sessionManager on server.stop().");
            } else {
                App.verbose("Stopping session. Shutdown clients gently...");
                // wait for session to stop correctly
                App.verbose("sessionManager is interrupting.....");
                sessionManager.stopGently();
                sessionManager.join();
                App.verbose("sessionManager interrupted");
                // then do the rest stuffs
            }
            serverSocket.close();
            App.log().info("Sockets closed and server stopped.");
        } catch (IOException | InterruptedException e) {
            App.log().severe("oO Unpredictable exception.\n" + App.getStackTrace(e));
        }
    }
}
