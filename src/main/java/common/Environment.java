package common;

import java.util.logging.Logger;

public class Environment {

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
