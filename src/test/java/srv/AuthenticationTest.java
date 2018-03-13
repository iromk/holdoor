package srv;

import common.App;
import common.Protocol;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.net.Socket;

public class AuthenticationTest {

    final int SERVER_PORT = 4242;
    final String SERVER_HOST = "localhost";

    Server server;
    Socket socket;

    @Before
    public void RunServer() {
        App.set().init();
        server = new Server(SERVER_PORT);
        server.start();
        try {
            socket = new Socket(SERVER_HOST, SERVER_PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void SimpleAuthenticationTest() {
        try {
            DataOutputStream os = new DataOutputStream(socket.getOutputStream());
            DataInputStream is = new DataInputStream(socket.getInputStream());

            os.writeUTF(Protocol.HELLO_TOKEN + " 1 test");
            final String response = is.readUTF();
            Assert.assertTrue(response.startsWith(Protocol.OK_TOKEN));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @After
    public void StopServer() {
        server.stop();
    }
}
