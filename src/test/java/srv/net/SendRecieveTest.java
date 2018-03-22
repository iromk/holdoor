package srv.net;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import srv.TestsSetup;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class SendRecieveTest {


    @BeforeClass
    public static void setUpClass() {
        new TestsSetup();
    }

    @Test
    @Ignore
    public void SendAndReceivePieceOfData() {
        try {
            final Byte[] dataRcvd = new Byte[]{0, 0, 0, 0};
            final Byte[] dataSent = new Byte[]{1, 2, 3, 4};
            ServerSocket serverSocket = new ServerSocket(3322);
            new Thread(() -> {
                try {
                    InputStream is;
                    is = serverSocket.accept().getInputStream();
                } catch (IOException e) {
                }
            }).start();

            OutputStream os = new Socket("localhost", 3322).getOutputStream();
            Assert.assertArrayEquals(dataRcvd, dataSent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
