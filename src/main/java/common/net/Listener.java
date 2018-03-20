package common.net;

import common.Session;
import common.core.App;
import org.json.simple.JsonArray;
import srv.data.Name;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class Listener extends Thread {

    private Session session;

    public Listener(Session session) {
        this.session = session;
    }

    @Override
    public void run() {

        try {

            JsonArray ja = new JsonArray();
            ja.add(new Name("aaa","bbbb"));
            DataInputStream inputStream = new DataInputStream(session.getInputStream());
            while(!interrupted()) {
                if(inputStream.available() > 0) {
                    SessionContext sc = session.getSuitableContextController();

                     inputStream.readUTF();
                } else sleep(1);
            }

        } catch (IOException | InterruptedException e) {
            App.log().severe("Unexpected error in Listener");
            App.log().severe(App.getStackTrace(e));
            throw new RuntimeException(e);
        } finally {
            App.verbose("leave listener");
        }

    }
}
