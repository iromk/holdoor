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
            if(!in.readUTF().equals(Server.OK_TOKEN))
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
