package srv;

import common.Protocol;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

public class Server {

    private static final int LISTENING_PORT = 0x1D00;
    public static final String HELLO_TOKEN = "HOLDTHEDOOR";
    public static final String OK_TOKEN = "OK";

    private final Logger LOG = ServerLogger.setup();

    private Thread holdoorThread;

    private int port;
    private ServerSocket serverSocket;
    private Socket clientSocket;

    public class SocketNotOpenedException extends IOException {}

    public Server() {

        this(LISTENING_PORT);

    }

    public Server(int port) {

        LOG.info("Holdoor server is starting...");

        serverSocket = null;
        clientSocket = null;
        this.port = port;

//        run();

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
                if (totalReceived > expectedSize) throw new RuntimeException("Sent file is bigger than were announced");
            }
            if (totalReceived < expectedSize) throw new RuntimeException("Sent file is smaller than were announced");
            LOG.info("last length " + length);
            LOG.info("Received " + totalReceived);
            outTemp.close();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            LOG.severe("Bad client or intruder detected. (" + e.getMessage() + ")");
        }


    }

    private void openSocket(int port) throws SocketNotOpenedException {

        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            throw new SocketNotOpenedException();
        }

    }

    public void run() {

        holdoorThread =
        new Thread(() -> {
            try {

                openSocket(port);
                LOG.info("Server is ready to accept incoming connections");

                clientSocket = serverSocket.accept();
                LOG.info("Connected new session " + clientSocket.toString());

                DataInputStream in = new DataInputStream(clientSocket.getInputStream());
                DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());

                boolean loop = true;
                while (loop) {
                    String text = in.readUTF();
                    if (text.startsWith(Protocol.HELLO_TOKEN)) {
                        out.writeUTF(Protocol.OK_TOKEN);
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

            } catch (SocketNotOpenedException e) {
                LOG.severe("Server start failed");
            } catch (IOException e) {
                LOG.warning("Incoming session couldn't be connected (" + e.toString() + ")");
            }
            finally {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });
        holdoorThread.start();
    }

    public void stop() {
        holdoorThread.interrupt();
        try {
            serverSocket.close();
            LOG.info("Server stopped");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
