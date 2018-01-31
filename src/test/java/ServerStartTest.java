import org.junit.Assert;
import org.junit.Test;
import srv.Server;

import java.io.IOException;
import java.net.Socket;

public class ServerStartTest {

    @Test
    public void ConnectionAcceptanceTest() {
        Server server = new Server(4242);
        server.run();

        try (Socket testSocket = new Socket("localhost", 4242)) {
            testSocket.close();
        } catch (IOException e) {
            Assert.fail();
        } finally {
            server = null;
        }
    }

}
