package server;

import common.Protocol;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import srv.Server;

import java.io.IOException;
import java.net.Socket;

public class StartTest {

    final int SERVER_PORT = Protocol.DEFAULT_PORT;
    final String SERVER_HOST = "localhost";

    Server server;

    @Before
    public void RunServer() {
        server = new Server();
        server.run();
    }

    @Test
    public void ConnectionAcceptanceTest() {
        try (Socket testSocket = new Socket(SERVER_HOST, SERVER_PORT)) {
            testSocket.close();
        } catch (IOException e) {
            Assert.fail();
        } finally {
        }
    }

    @After
    public void StopServer() {
        server.stop();
    }

}
