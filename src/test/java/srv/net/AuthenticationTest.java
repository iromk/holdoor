package srv.net;

import common.Protocol;
import org.junit.*;
import srv.Server;
import srv.TestsSetup;

import java.io.*;
import java.net.Socket;

@Ignore
public class AuthenticationTest {

    final int SERVER_PORT = 4242;
    final String SERVER_HOST = "localhost";

    Server server;
    Socket socket;

    @BeforeClass
    public static void setUpClass() {
        new TestsSetup();
    }

    @Before
    public void runServer() {
        server = new Server(SERVER_PORT);
        server.start();
        try {
            socket = new Socket(SERVER_HOST, SERVER_PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void simpleAuthenticationTest() {
        try {
            DataOutputStream os = new DataOutputStream(socket.getOutputStream());
            DataInputStream is = new DataInputStream(socket.getInputStream());

            final String helloRequest = Protocol.HELLO_TOKEN + " 1 test";
            os.writeUTF(helloRequest);
            final String response = is.readUTF();
            Assert.assertTrue(response.startsWith(Protocol.WELCOME_TOKEN));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @After
    public void stopServer() {
        server.stop();
    }
}
