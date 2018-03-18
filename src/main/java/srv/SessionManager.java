package srv;

import common.*;
import common.core.App;
import srv.net.ClientSession;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CopyOnWriteArrayList;


public class SessionManager extends Thread {

    volatile private ServerSocket serverSocket;
    volatile private CopyOnWriteArrayList<ClientSession> activeClientSessions = new CopyOnWriteArrayList<>();

    volatile private boolean active = true;

    /**
     * The server side session itself. Cannot be instantiated outside of the package.
     * @param serverSocket opened socket to listen on
     */
    SessionManager(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    @Override
    public void run() {

        try {

            App.log().info("Server is ready to accept incoming connections");


//            clientSocket = serverSocket.accept();

//            DataInputStream in = new DataInputStream(clientSocket.getInputStream());
//            DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());

            boolean interrupted = false;

            while(active) {
                interrupted = interrupted();
                if(interrupted)
                    throw new InterruptedException();

                Socket acceptedConnection = serverSocket.accept();
                if(active) {
                    App.log().info("Connected new session " + acceptedConnection.toString());
                    activeClientSessions.add(new ClientSession(acceptedConnection, this));
                } else {
                    App.verbose("Fake connection requested. Deactivating server.");
                }
            }
      /*
            while (loop) {
                interrupted = interrupted();
                if(interrupted)
                    throw new InterruptedException();
                if(in.available() > 0) {
                    String text = in.readUTF();
                    if (text.startsWith(Protocol.HELLO_TOKEN)) {
                        out.writeUTF(Protocol.WELCOME_TOKEN);
                        App.log().info("Session authorized");
                        loop = false;
                    }
                } else sleep(1);
            }

            App.log().info("Waiting for incoming file");
            loop = true;
            Long size = 0L;
            while (loop) {
                interrupted = interrupted();
                if(interrupted)
                    throw new InterruptedException();
                if(in.available() > 0) {
                    size = new Long(in.readUTF());
                    loop = false;
                } else sleep(1);
            }

            App.log().info("Incoming file size " + size);
            FileManager.receiveBinary(size, clientSocket.getInputStream());
*/
        } catch (InterruptedException e) {
            App.log().warning("Session interrupted.\n" + App.getStackTrace(e));
        } catch (Server.SocketNotOpenedException e) {
            App.log().severe("Server start failed");
        } catch (IOException e) {
            App.log().warning("Incoming session couldn't be connected (" + e.toString() + ")");
        }
        finally {
            stopGently();
        }
    }

    public int size() {
        return activeClientSessions.size();
    }

    synchronized public void stopGently() {
        if(!active) return;
        try {
            Socket fakeSocket = new Socket();
            App.verbose("Establishing fake connection to release serverSocket.accept()");
            active = false;
            fakeSocket.connect(serverSocket.getLocalSocketAddress());
            fakeSocket.close();
            closeClientSessions();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removeClientSession(ClientSession clientSession) {
        activeClientSessions.remove(clientSession);
    }

    synchronized private void closeClientSessions() {
        if(!activeClientSessions.isEmpty()) {
            int n = activeClientSessions.size();
            while(n-->0) activeClientSessions.get(n).close();
        }
    }

}
