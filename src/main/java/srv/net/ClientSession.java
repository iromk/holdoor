package srv.net;

import com.github.cliftonlabs.json_simple.JsonObject;
import common.Session;
import common.core.App;
import srv.SessionManager;
import srv.data.Name;

import java.io.*;
import java.net.Socket;

public class ClientSession extends Session {

    private SessionManager sessionManager;

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

        start();

    }

    @Override
    public void run() {

        Long size = null;
        try {
            boolean loop = true;
            boolean interrupted = false;
            InputStream socketInputStream = getInputStream();
            ObjectInputStream ois = new ObjectInputStream(socketInputStream);
            while(loop) {
                interrupted = interrupted();
                if(interrupted)
                    throw new InterruptedException();
                if(socketInputStream.available() > 0) {
                    Object receivedObject = ois.readObject();
                    JsonObject jsonObject = null;
                    if(receivedObject instanceof JsonObject) {
                        jsonObject = (JsonObject) receivedObject;
                    }
                    assert (jsonObject != null);
                    Name name = (Name)jsonObject.get("name");
                    System.out.println(name.getFirst() + " " + name.getLast());
                }
            }
        } catch (InterruptedException e) {
            App.log().fine("Session interrupted.\n" + App.getStackTrace(e));
        } catch (IOException e) {
            App.log().warning("Incoming session couldn't be connected (" +  App.getStackTrace(e) + ")");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        App.verbose("The ClientSession thread finished.");
    }

    @Override
    public void close() {
        try {
            interrupt();
            socket.close();
            sessionManager.removeClientSession(this);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
    }
}
