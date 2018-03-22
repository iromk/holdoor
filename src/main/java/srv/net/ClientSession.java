package srv.net;

import com.github.cliftonlabs.json_simple.JsonObject;
import common.Session;
import common.core.Action;
import common.core.App;
import srv.JPAFactory;
import srv.SessionManager;
import srv.data.Name;
import srv.data.User;

import javax.persistence.EntityManager;
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
                    assert (receivedObject != null);
                    JsonObject jsonObject = null;
                    if(receivedObject instanceof JsonObject) {
                        jsonObject = (JsonObject) receivedObject;
                        if(jsonObject.containsKey("action")) {
                            Object action = jsonObject.get("action");
                            assert (action != null);
                            int actionId = (int) action;
                            if(actionId == Action.REGISTER_USER) {
                                registerUser(jsonObject);
                                Name name = (Name)jsonObject.get("name");
                                System.out.println(name.getFirst() + " " + name.getLast());
                            }
                        }
                    }
                } else sleep(1);
            }
        } catch (InterruptedException e) {
            App.log().fine("Session interrupted.\n" + App.getStackTrace(e));
        } catch (IOException e) {
            App.log().warning("Incoming session couldn't be connected (" +  App.getStackTrace(e) + ")");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        App.verbose().finest("The ClientSession thread finished.");
    }

    private void registerUser(JsonObject request) {
        Name name = (Name) request.get("name");
        String login = (String) request.get("login");
        String password = (String) request.get("password");
        User newUser = new User(name.getFirst(), name.getLast(), login);
        // TODO set password
        // TODO validate given data
        EntityManager entityManager = JPAFactory.getEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(newUser);
        entityManager.getTransaction().commit();
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
