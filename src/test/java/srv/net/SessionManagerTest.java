package srv.net;

import common.Protocol;
import common.core.App;
import org.junit.*;
import srv.Server;
import srv.SessionManager;
import srv.TestsSetup;

import java.io.IOException;
import java.net.Socket;

public class SessionManagerTest {

    private final String SERVER_HOST = Protocol.DEFAULT_HOST;
    private final int SERVER_PORT = Protocol.DEFAULT_PORT;

    private Server server;

    @BeforeClass
    public static void setUpClass() {
        new TestsSetup();
    }


    @Before
    public void runServer() {
        server = new Server();
        server.start();
    }

    @Test
    public void connectionAcceptanceTest() {
        try (Socket testSocket = new Socket(SERVER_HOST, SERVER_PORT)) {
            testSocket.close();
        } catch (IOException e) {
            Assert.fail();
        } finally {
        }
    }

    private void doOpenThreeSessions() {
        try {
            Socket testSocket1 = new Socket(SERVER_HOST, SERVER_PORT);
            Socket testSocket2 = new Socket(SERVER_HOST, SERVER_PORT);
            Socket testSocket3 = new Socket(SERVER_HOST, SERVER_PORT);
        } catch (IOException e) {
            App.log().severe("Test preparation failed.\n");
            App.log().severe(App.getStackTrace(e));
            e.printStackTrace();
        }
    }

    private void waitASec() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void openThreeSessionsTest() {
            SessionManager sessionManager = server.getSessionManager();
            doOpenThreeSessions();
            waitASec();
            Assert.assertEquals(3, sessionManager.size());
    }

    @Test
    public void gentlyCloseTest() {
        SessionManager sessionManager = server.getSessionManager();
        doOpenThreeSessions();
        waitASec();
        sessionManager.stopGently();
        Assert.assertEquals(0, sessionManager.size());
    }

    @After
    public void stopServer() {
        server.stop();
    }


}
