package common.core;

import common.loggers.AppLogger;
import common.loggers.LogContext;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *  Application configuration class.
 */
public class App {

    private static String baseProjectName = "Holdoor";

    static protected App instance;

    private Boolean initialized = false;

    private Logger logger;
    private Boolean verboseEnabled;
    private Boolean contextEnabled;

    /**
     * Builder "bricks"
     */
    private Level logLevel;
//    private Class mainClass;
    private String logFile;
    private String projectName;
    private Mode mode;


    private static App getInstance() {
        if(instance == null)
            instance = new App();
        return instance;
    }

    public static class Config {

        public Config(String name) { this.name = name; }

        private String name = null;

        /**
         * Run mode. Default is Mode.DEV.
         */
        private Mode mode = Mode.DEV;

        public Config setMode(Mode mode) { this.mode = mode; return this; }
        public Config set(Mode mode) { setMode(mode); return this; }

        /**
         * Log levels.
         * Its default value cannot be predefined because it depends on mode value.
         */
        private Optional<Level> logLevel = Optional.empty();
        private Optional<Level> consoleLogLevel;
        private Optional<Level> fileLogLevel;

        private String logFileLocation = "./data/log/";

        public Config setLogLevel(Level level) { this.logLevel = Optional.of(level); return this; }
        public Config set(Level level) { setLogLevel(level); return this; }
        public Config set(Level logLevel, Level consoleLogLevel, Level fileLogLevel) {
            this.logLevel = Optional.of(logLevel);
            this.consoleLogLevel = Optional.of(consoleLogLevel);
            this.fileLogLevel = Optional.of(fileLogLevel);
            return this;
        }

        /**
         * Logging tools switchers.
         */
        private Optional<Boolean> contextEnabled = Optional.empty();
        private Optional<Boolean> verboseEnabled = Optional.empty();

        public Config context(Boolean context) { contextEnabled = Optional.of(context); return this; }
        public Config verbose(Boolean verbose) { verboseEnabled = Optional.of(verbose); return this; }

        public void init() {

            App.instance.projectName = App.baseProjectName + "." + this.name;

            App.instance.mode = this.mode;

            // setup values provided or use default values for current mode.
            switch (App.instance.mode) {
                case DEV:
                case TEST:
                    App.instance.logLevel = logLevel.orElse(Level.ALL);
                    App.instance.contextEnabled = contextEnabled.orElse(true);
                    App.instance.verboseEnabled = verboseEnabled.orElse(true);
                    break;
                case PROD:
                    App.instance.logLevel = logLevel.orElse(Level.WARNING);
                    App.instance.contextEnabled = contextEnabled.orElse(false);
                    App.instance.verboseEnabled = verboseEnabled.orElse(false);
            }

            final String logFileName = App.instance.projectName + "_" + App.instance.mode + ".log";
            final String logFile = logFileLocation + logFileName;

            App.instance.logger = new AppLogger(name, logFile, App.instance.logLevel).getLogger();

            App.instance.initialized = true;

        }

    }

    public static Config config(String name) {
        try {
            if(getInstance().initialized && instance.mode != Mode.TEST) {
                final String msg = "Second attempt to initialize the app detected. Aborted to prevent an unpredictable behaviour.";
                throw new RuntimeException(msg);
            }
        } catch (RuntimeException e) {
            App.log().warning(App.getStackTrace(e));
            throw e;
        }
        return new Config(name);
    }

    public Mode mode() { return mode; }

    public static App get() { return getInstance(); }

    public static String getStackTrace(Exception e) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }

    static public LogContext context(String contextDescription) {
        if(instance.contextEnabled)
            return new LogContext(contextDescription);
        else
            return new LogContext();
    }

    public static Boolean isContextEnabled() { return instance.contextEnabled; }

    static public Logger log() { return instance.logger; }

    static public LogContext log(LogContext context) { return context; }

    static public Logger verbose() { return instance.logger; }

}
