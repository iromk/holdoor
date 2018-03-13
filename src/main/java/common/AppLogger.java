package common;

import java.io.IOException;
import java.util.logging.*;

public class AppLogger {

    private static Logger logger = null;


    public AppLogger(Class appClass, String logFile, Level logLevel) {
        setup(appClass, logFile, logLevel);
    }

    public void setup(Class appClass, String logFile, Level logLevel) {

        if(logger != null) return;

        logger = Logger.getLogger(appClass.getName());

        Handler consoleHandler = null;
        Handler fileHandler  = null;

        try { // to create and attach console and file handlers

            /*
            // Setup here console handler if no default for some reason
            consoleHandler = new ConsoleHandler();
            logger.addHandler(consoleHandler);
            consoleHandler.setLevel(Level.ALL);
            */

            fileHandler  = new FileHandler(logFile, true);
            fileHandler.setFormatter(new SimpleFormatter()); // plain text is enough for now
            logger.addHandler(fileHandler);
            fileHandler.setLevel(logLevel);

            logger.setLevel(logLevel);

        } catch(IOException e){
            // treat problem with logging to a file as not fatal, but report
            logger.log(Level.WARNING, "FileHandler cannot be initialized. Logging to console only.", e);
        }

//        return logger;

    }

    public Logger getLogger() {
        if(logger == null) throw new RuntimeException("Application was not initiated correctly. Logger not set.");
        return logger;
    }


}
