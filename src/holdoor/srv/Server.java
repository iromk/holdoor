package holdoor.srv;

import java.net.ServerSocket;
import java.util.logging.Logger;

public class Server {

    private static final int LISTENING_PORT = 4242;

    private final Logger LOG = ServerLogger.setup();

    private ServerSocket serverSocket;



    public Server() {

        this(LISTENING_PORT);

    }

    public Server(int port) {

        LOG.info("Server is starting...");
        serverSocket = null;

    }

}
