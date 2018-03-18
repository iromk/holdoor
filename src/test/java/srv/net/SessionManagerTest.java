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
            new Socket(SERVER_HOST, SERVER_PORT);
            new Socket(SERVER_HOST, SERVER_PORT);
            new Socket(SERVER_HOST, SERVER_PORT);
        } catch (IOException e) {
            App.log().severe("Test preparation failed.\n");
            App.log().severe(App.getStackTrace(e));
            e.printStackTrace();
        }
    }

    private void doOpenNSessions(int N) {
        try {
            while(N-->0)
                new Socket(SERVER_HOST, SERVER_PORT);
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
    public void openOneSessionsTest() {
        SessionManager sessionManager = server.getSessionManager();
        final int N = 1;
        doOpenNSessions(N);
        waitASec();
        Assert.assertEquals(N, sessionManager.size());
    }

    @Test
    public void open50SessionsTest() {
        SessionManager sessionManager = server.getSessionManager();
        final int N = 50;
        doOpenNSessions(N);
        waitASec();
        Assert.assertEquals(N, sessionManager.size());
    }

    @Test
    public void gentlyCloseTest() {
        SessionManager sessionManager = server.getSessionManager();
        doOpenThreeSessions();
        waitASec();
        sessionManager.stopGently();
        Assert.assertEquals(0, sessionManager.size());
    }

    @Test
    public void gentlyClose55Test() {
        SessionManager sessionManager = server.getSessionManager();
        final int N = 55;
        doOpenNSessions(N);
        waitASec();
        sessionManager.stopGently();
        Assert.assertEquals(0, sessionManager.size());
    }

    @After
    public void stopServer() {
        server.stop();
    }


}
