package srv.net;

import common.FileManager;
import common.Protocol;
import common.Session;
import common.core.App;
import srv.Server;
import srv.SessionManager;

import java.io.*;
import java.net.Socket;

public class ClientSession extends Session {

    private SessionManager sessionManager;
    private Socket socket;

//    private DataInputStream in;
    private DataOutputStream out;

    public ClientSession(Socket socket, SessionManager sessionManager) {
        this.socket = socket;
        this.sessionManager = sessionManager;

        try {
//            this.in = new DataInputStream(socket.getInputStream());
            this.out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            App.log().severe("Fatality while creating client session instance. \n");
            App.log().severe(App.getStackTrace(e));
            close();
        }

        run();

    }

    @Override
    public void run() {

        Long size = null;
        try {
            boolean loop = true;
            boolean interrupted = false;
            sleep(1000);
            InputStream socketInputStream = socket.getInputStream();
            DataInputStream dis = new DataInputStream(socketInputStream);

            ObjectInputStream ois = new ObjectInputStream(socketInputStream);
/*            while (loop) {
                interrupted = interrupted();
                if(interrupted)
                    throw new InterruptedException();
//                int ava = ino.available();
                if(socketInputStream.available() > 0) {
                    Object obj = ino.readObject();//readUTF();
                    String text = "";
                    if(obj instanceof String)
                        text = (String) obj;
                    if (text.startsWith(Protocol.HELLO_TOKEN)) {
                        out.writeUTF(Protocol.WELCOME_TOKEN);
                        App.log().info("Session authorized");
                        loop = false;
                    }
                } else sleep(1);
            }
*/
            App.log().info("Waiting for incoming file");
            loop = true;
            size = 0L;
            while (loop) {
                interrupted = interrupted();
                if(interrupted)
                    throw new InterruptedException();
                if(socketInputStream.available() > 0) {
                    Object o = ois.readObject();
                    if(o instanceof Long) size = (Long) o;
//                    size = ino.readLong(); // read/writeLong() doesn't want to work as I expect.
                    loop = false;
                } else sleep(1);
            }

            App.log().info("Incoming file size " + size);
            FileManager.receiveBinary(size, socketInputStream);

        } catch (InterruptedException e) {
            App.log().warning("Session interrupted.\n" + App.getStackTrace(e));
        } catch (IOException e) {
            App.log().warning("Incoming session couldn't be connected (" + e.toString() + ")");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        App.verbose("The ClientSession thread finished.");
    }

    @Override
    public void close() {
        try {
            socket.close();
            sessionManager.removeClientSession(this);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
    }
}
