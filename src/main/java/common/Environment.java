package common;

import srv.ServerLogger;

import java.util.logging.Logger;

public class Environment {

    public enum Id { TEST, DEV, PROD }
    public static final Id id;

    static {
        id = Id.TEST;
//        id = EnvironmentId.DEV;
//        id = EnvironmentId.PROD;
    }

    private static Environment ourInstance = new Environment();
    private Logger logger;

    public static Environment getInstance() {
        return ourInstance;
    }

    private Environment() {
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    public Logger getLogger() {
        return this.logger;
    }
}
