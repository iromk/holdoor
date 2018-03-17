package common;

import client.UserSession;

public interface Protocol {

    String DEFAULT_HOST = "localhost";
    int DEFAULT_PORT = 0x1D00;

    /**
     *  Magic word on session start. Two optional parameters may be specified
     *  delimited by space.
     *  First - requested protocol version, second one - name of the client app.
     *  Eg.: KNOCKNOCK 1 droidHoldoor
     */
    String HELLO_TOKEN = "KNOCKNOCK";

    /**
     * Response to the client if its session is authorized successfully.
     */
    String WELCOME_TOKEN = "WELCOME_TOKEN";

//    UserSession sessionProvider = new UserSession();

}
