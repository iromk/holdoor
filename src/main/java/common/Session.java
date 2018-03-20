package common;

import common.net.SessionContext;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public abstract class Session extends Thread {

    public static final long TIMEOUT = 120_000;

    public void close() {

    }

    public SessionContext getSuitableContextController() { throw new NotImplementedException(); }

}
