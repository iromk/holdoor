package client;

import common.Protocol;
import common.Session;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class UserSession extends Session {

    public DataOutputStream out;
    public DataInputStream in;

    final private SocketAddress socketAddress = new InetSocketAddress(Protocol.DEFAULT_HOST, Protocol.DEFAULT_PORT);
    final private Socket socket = new Socket();

    private static UserSession thisSession;

    public static UserSession get() {
        if(thisSession == null) {
            thisSession = new UserSession();
        }
        return thisSession;
    }

    private UserSession() {}

    public void authenticate(String username, String password) throws IOException {

        if(!socket.isConnected()) {
            establish();

            throw new IOException("Socket ");
        }
    }


    public void establish() {

        try {
            socket.connect(socketAddress);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            out.writeUTF(Protocol.HELLO_TOKEN);
            if(!in.readUTF().equals(Protocol.WELCOME_TOKEN))
                throw new RuntimeException("Unexpected response from server");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void send(String filename) {

        try {

            File file = new File(filename);

            BufferedInputStream localInput = new BufferedInputStream(new FileInputStream(file));
            BufferedOutputStream remoteOutput = new BufferedOutputStream(out);

            try {
                out.writeUTF(Long.toString(file.length()));

                final int blockSize = 10240;
                byte[] buffer = new byte[blockSize];
                long totalSent = 0;

                for (int length = localInput.read(buffer); length >= 0; length = localInput.read(buffer)) {
                    remoteOutput.write(buffer, 0, length);
                    totalSent += length;
                }
                System.out.println("Sent totally " + totalSent);
                remoteOutput.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

}
