package client;

import common.Protocol;
import common.Session;
import common.core.App;
import common.net.Listener;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class UserSession extends Session {

//    public DataOutputStream out;
//    public DataInputStream in;
//    ObjectOutputStream objectOutput;

    final private SocketAddress socketAddress = new InetSocketAddress(Protocol.DEFAULT_HOST, Protocol.DEFAULT_PORT);

    private static UserSession thisSession;

    public static UserSession get() {
        if(thisSession == null) {
            thisSession = new UserSession();
        }
        return thisSession;
    }

    private UserSession() { socket = new Socket(); }

    public void authenticate(String username, String password) throws IOException {

        if(!socket.isConnected()) {
            connect();
            establish();

            throw new IOException("Socket ");
        }
    }

    public void connect() throws IOException {
        socket.connect(socketAddress);
//        in = new DataInputStream(socket.getInputStream());
//        objectOutput= new ObjectOutputStream(socket.getOutputStream());

//        out = new DataOutputStream(socket.getOutputStream());
    }

    public void establish() {

//        try {

//            out.writeUTF(Protocol.HELLO_TOKEN);
//            if(!in.readUTF().equals(Protocol.WELCOME_TOKEN))
//                throw new RuntimeException("Unexpected response from server");

//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }

    public void send(String filename) {

        try {

            File file = new File(filename);

            OutputStream socketOutputStream = socket.getOutputStream();

            {
                ObjectOutputStream objectOutput = new ObjectOutputStream(socketOutputStream);
                objectOutput.writeObject(file.length());
            } // we don't need objectOutput anymore

            BufferedInputStream localInput = new BufferedInputStream(new FileInputStream(file));
            DataOutputStream dos = new DataOutputStream(socketOutputStream);

            final int blockSize = 22222;
            byte[] buffer = new byte[blockSize];
            long totalSent = 0;

            BufferedOutputStream remoteOutput = new BufferedOutputStream(dos);
            for (int length = localInput.read(buffer); length >= 0; length = localInput.read(buffer)) {
                remoteOutput.write(buffer, 0, length);
                totalSent += length;
            }
            App.verbose("Sent totally " + totalSent);

            remoteOutput.flush();
//            remoteOutput.close(); // DON'T! This will close the socket!

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
