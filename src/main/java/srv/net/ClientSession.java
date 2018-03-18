package srv.net;

import common.Session;
import common.core.App;
import srv.SessionManager;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientSession extends Session {

    private SessionManager sessionManager;
    private Socket socket;

    private DataInputStream in;
    private DataOutputStream out;

    public ClientSession(Socket socket, SessionManager sessionManager) {
        this.socket = socket;
        this.sessionManager = sessionManager;

        try {
            this.in = new DataInputStream(socket.getInputStream());
            this.out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            App.log().severe("Fatality while creating client session instance. \n");
            App.log().severe(App.getStackTrace(e));
            close();
        }
    }

    @Override
    public void close() {
        try {
            if(in != null) in.close();
            if(out != null) out.close();
            socket.close();
            sessionManager.removeClientSession(this);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
    }
}
