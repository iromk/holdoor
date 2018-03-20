package srv.net;

import client.UserSession;
import common.Protocol;
import org.junit.*;
import srv.Server;
import srv.TestsSetup;

import java.io.*;

//@Ignore
public class ReceiveSampleFileTest {

    final int SERVER_PORT = Protocol.DEFAULT_PORT;
    final String SERVER_HOST = "localhost";

    Server server;

    @BeforeClass
    public static void setUpClass() {
        new TestsSetup();
    }

    @Before
    public void runServer() {
        server = new Server();
        server.start();
    }

    @Test @Ignore
    public void receiveSampleFile() {
        // preps
        final String sentFileName = "./data/sample/Starter Set - Characters.pdf";
        final String rcvdFileName = "./data/tmp/file.pdf";
        File rcvdFile = new File(rcvdFileName);
        if(rcvdFile.exists()) rcvdFile.delete();

        // simulate client behavior
        UserSession userSession = UserSession.get();
//        userSession.establish();
        try {
            userSession.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        userSession.send(sentFileName);
        // compare sent vs received
        Assert.assertTrue(rcvdFile.exists());
        try {
            // wait for buffers to flush
            Thread.sleep(2_000);
            BufferedInputStream sent = new BufferedInputStream(new FileInputStream(sentFileName));
            BufferedInputStream rcvd = new BufferedInputStream(new FileInputStream(rcvdFileName));
            // Actually, file sizes. Should be equal.
            Assert.assertTrue(sent.available() == rcvd.available());
            byte[] sentBuffer, rcvdBuffer;
            sentBuffer = new byte[10_240];
            rcvdBuffer = new byte[10_240];
            for(int sentReadBytes = sent.read(sentBuffer); sentReadBytes >=0; sentReadBytes = sent.read(sentBuffer) ) {
                int rcvdReadBytes = rcvd.read(rcvdBuffer);
                Assert.assertTrue(rcvdReadBytes == sentReadBytes);
                Assert.assertArrayEquals(sentBuffer, rcvdBuffer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if(rcvdFile.exists()) rcvdFile.delete();
        }
    }

    @After
    public void stopServer() {
//        server.stop();
    }

}
