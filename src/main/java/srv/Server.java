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

    private Thread holdoorThread;

    private int port;
    private ServerSocket serverSocket;
    private Socket clientSocket;

    public class SocketNotOpenedException extends IOException {}

    public Server() {

        this(Protocol.DEFAULT_PORT);

    }

    public Server(int port) {

        environment.setLogger(LOG);

        serverSocket = null;
        clientSocket = null;
        this.port = port;

    }

    private void openSocket(int port) throws SocketNotOpenedException {

        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            throw new SocketNotOpenedException();
        }

    }

    public void run() {

        LOG.info("Holdoor server is starting...");

        holdoorThread = new Thread(() -> {
            try {

                openSocket(port);
                LOG.info("Server is ready to accept incoming connections");

                clientSocket = serverSocket.accept();
                LOG.info("Connected new session " + clientSocket.toString());

                DataInputStream in = new DataInputStream(clientSocket.getInputStream());
                DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());

                boolean loop = true;
                while (loop) {
                    String text = in.readUTF();
                    if (text.startsWith(Protocol.HELLO_TOKEN)) {
                        out.writeUTF(Protocol.OK_TOKEN);
                        LOG.info("Session authorized");
                        loop = false;
                    }
                }


                LOG.info("Waiting for incoming file");
//                for(loop = true; loop;) {
                Long size = new Long(in.readUTF());
                LOG.info("Incoming file size " + size);
                FileManager.receiveBinary(size, clientSocket.getInputStream());
//                }

            } catch (SocketNotOpenedException e) {
                LOG.severe("Server start failed");
            } catch (IOException e) {
                LOG.warning("Incoming session couldn't be connected (" + e.toString() + ")");
            }
            finally {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });
        holdoorThread.start();
    }

    public void stop() {
        holdoorThread.interrupt();
        try {
            serverSocket.close();
            LOG.info("Server stopped");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
