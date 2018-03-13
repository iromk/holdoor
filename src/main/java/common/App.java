package common;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *  Application configuration class.
 */
public class App {

    static protected App instance;

    private Logger logger;

    /**
     * Builder "bricks"
     */
    private Level logLevel;
    private Class mainClass;
    private String logFile;
    private Environment environment;


    public enum Environment {

        TEST, DEV, PROD
//    Environment() {}

    }

    private static App getInstance() {
        if(instance == null)
            instance = new App();
        return instance;
    }

    public static App set() {

        return getInstance();

    }

    public static App get() {

        return getInstance();

    }

    public App defaults() {
        return init();
    }

    public App logLevel(Level logLevel) {
        this.logLevel = logLevel;
        return this;
    }

    public App environment(Environment env) {
        this.environment = env;
        return this;
    }

    public Environment environment() {
        return this.environment;
    }


    public App logFile(String logFile) {
        this.logFile = logFile;
        return this;
    }

    public App init() {
        if(environment == null) environment = Environment.DEV;

        switch (environment) {
            case DEV:
            case TEST: logLevel = Level.ALL; break;
            case PROD: logLevel = Level.SEVERE; break;
        }

        if(mainClass == null)
            mainClass = App.class;

        if(logFile == null)
            logFile = "./data/log/holdoor_" + mainClass.getName() + "_" + environment + ".log";

        logger = new AppLogger(mainClass, logFile, logLevel).getLogger();

        return this;
    }

    static public Logger log() {
        return instance.logger;
    }

}
