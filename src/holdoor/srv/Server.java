package holdoor.srv;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

public class Server {

    private static final int LISTENING_PORT = 0x1D00;
    public static final String HELLO_TOKEN = "HOLDTHEDOOR";
    public static final String OK_TOKEN = "OK";

    private final Logger LOG = ServerLogger.setup();

    private ServerSocket serverSocket;
    private Socket clientSocket;

    public Server() {

        this(LISTENING_PORT);

    }

    public Server(int port) {

        LOG.info("Holdoor server is starting...");

        serverSocket = null;
        clientSocket = null;

        try {

            openSocket(port);
            LOG.info("Server is ready to accept incoming connections");

            try {
                clientSocket = serverSocket.accept();
                LOG.info("Connected new session " + clientSocket.toString());

                DataInputStream in = new DataInputStream(clientSocket.getInputStream());
                DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());

                boolean loop = true;
                while (loop) {
                    String text = in.readUTF();
                    if(text.equals(HELLO_TOKEN)) {
                        out.writeUTF(OK_TOKEN);
                        LOG.info("Session authorized");
                        loop = false;
                    }
                }
                LOG.info("Waiting for incoming file");
//                for(loop = true; loop;) {
                    Long size = new Long(in.readUTF());
                    LOG.info("Incoming file size " + size);
                    receiveBinary(size);
//                }

            } catch (IOException e) {
                LOG.warning("Incoming session couldn't be connected (" + e.toString() + ")");
            }

        } catch (IOException e) {
            LOG.severe("Server start failed");
        } finally {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void receiveBinary(Long expectedSize) {
        File tempFile = new File("./data/tmp/file.pdf");
        try {
            BufferedOutputStream outTemp = new BufferedOutputStream(new FileOutputStream(tempFile));
            BufferedInputStream inputStream = new BufferedInputStream(clientSocket.getInputStream());

            final int blockSize = 10240;
            byte[] buffer = new byte[blockSize];
            long totalReceived = 0;

            int length;
            for (length = inputStream.read(buffer); length >= 0; length = inputStream.read(buffer)) {
                outTemp.write(buffer, 0, length);
                LOG.info("Block of size received " + length);
                totalReceived += length;
            }
            LOG.info("last length " + length);
            LOG.info("Received " + totalReceived);
            outTemp.close();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void openSocket(int port) throws IOException {

        serverSocket = new ServerSocket(port);

    }

}
