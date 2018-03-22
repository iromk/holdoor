package srv;

import common.Protocol;
import org.junit.Before;

public class ServerTestSetup extends TestsSetup {

    final String SERVER_HOST = Protocol.DEFAULT_HOST;
    final int SERVER_PORT = Protocol.DEFAULT_PORT;

    protected Server server;

    @Before
    public void runServer() {
        server = new Server();
        server.start();
    }
}
