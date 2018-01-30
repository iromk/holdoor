package holdoor.client;

import holdoor.common.Session;
import holdoor.srv.Server;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class UserSession extends Session {

    public Socket socket;
    public DataOutputStream out;
    public DataInputStream in;


    public void authenticate() {}


    public void establish() {
        socket = new Socket();
        SocketAddress socketAddress = new InetSocketAddress("localhost", 0x1D00);


        try {
            socket.connect(socketAddress);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            out.writeUTF(Server.HELLO_TOKEN);
            while(in.readUTF().equals(Server.OK_TOKEN)) { }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void send(String file) {

        try {

            BufferedInputStream localInput = new BufferedInputStream(new FileInputStream(file));
            BufferedOutputStream remoteOutput = new BufferedOutputStream(out);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

}
