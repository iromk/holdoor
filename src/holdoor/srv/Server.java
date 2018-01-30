package holdoor.srv;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

public class Server {

    private static final int LISTENING_PORT = 0x1D00;
    public static final String HELLO_TOKEN = "HOLDTHEDOOR";
    public static final String OK_TOKEN = "OK";

    private final Logger LOG = ServerLogger.setup();

    private ServerSocket serverSocket;
    private Socket clientSocket;

    public Server() {

        this(LISTENING_PORT);

    }

    public Server(int port) {

        LOG.info("Holdoor server is starting...");

        serverSocket = null;
        clientSocket = null;

        try {

            openSocket(port);
            LOG.info("Server is ready to accept incoming connections");

            try {
                clientSocket = serverSocket.accept();
                LOG.info("Connected new session " + clientSocket.toString());

                DataInputStream in = new DataInputStream(clientSocket.getInputStream());
                DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());

                boolean loop = true;
                while (loop) {
                    String text = in.readUTF();
                    if(text.equals(HELLO_TOKEN)) {
                        out.writeUTF(OK_TOKEN);
                        LOG.info("Session authorized");
                        loop = false;
                    }
                }
                LOG.info("Waiting for incoming file");
                for(loop = true; loop;) {

                }

            } catch (IOException e) {
                LOG.warning("Incoming session couldn't be connected (" + e.toString() + ")");
            }

        } catch (IOException e) {
            LOG.severe("Server start failed");
        } finally {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void openSocket(int port) throws IOException {

        serverSocket = new ServerSocket(port);

    }

}
