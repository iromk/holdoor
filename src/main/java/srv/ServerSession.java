package srv;

import common.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

public class ServerSession extends Session {

    private ServerSocket serverSocket;

    /**
     * The server side session itself. Cannot be instantiated outside of the package.
     * @param serverSocket opened socket to listen on
     */
    ServerSession(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    @Override
    public void run() {

        /**
         *  Thread related objects. They should die together with the thread.
         */
        Logger LOG = App.log(); // Environment.getInstance().getLogger();
        Socket clientSocket;

        try {

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
            FileManager.receiveBinary(size, clientSocket.getInputStream());
//                }

        } catch (Server.SocketNotOpenedException e) {
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

    }

}
