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

//            new Listener(this).run();
            BufferedInputStream localInput = new BufferedInputStream(new FileInputStream(file));
            OutputStream os = socket.getOutputStream();
            DataOutputStream dos = new DataOutputStream(os);
            ObjectOutputStream objectOutput = new ObjectOutputStream(os);
                objectOutput.writeObject(file.length());
                final int blockSize = 10240;
                byte[] buffer = new byte[blockSize];
                long totalSent = 0;
//            objectOutput = null;

            BufferedOutputStream remoteOutput = new BufferedOutputStream(os);//socket.getOutputStream());
                for (int length = localInput.read(buffer); length >= 0; length = localInput.read(buffer)) {
                    remoteOutput.write(buffer, 0, length);
                    totalSent += length;
                }
                App.verbose("Sent totally " + totalSent);
//            try {
//                sleep(2222);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
