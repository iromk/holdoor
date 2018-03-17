package common.loggers;

import java.io.IOException;
import java.util.logging.*;

public class AppLogger {

    private static Logger logger = null;

    private Handler consoleHandler = null;
    private Handler fileHandler  = null;

    public AppLogger(String loggerName, String logFile, Level logLevel) {
        setup(loggerName, logFile, logLevel);
    }

    public void setup(String loggerName, String logFile, Level logLevel) {

        if(logger != null) return;

        logger = Logger.getLogger(loggerName);

        // Replace formatter of the handlers of the global logger
        // with custom SimplerFormatter
        simplifyGlobalLogger();

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
            fileHandler.setLevel(Level.ALL);//logLevel);

//            globalLogger.addHandler(fileHandler);

            logger.setLevel(Level.ALL);//logLevel);

        } catch(IOException e){
            // treat problem with logging to a file as not fatal, but report
            logger.log(Level.WARNING, "FileHandler cannot be initialized. Logging to console only.", e);
        }

//        return logger;

    }

    private void simplifyGlobalLogger() {
        Logger globalLogger = Logger.getLogger(""); // Logger.getGlobal(); // why doesn't work?
        Handler[] handlers = globalLogger.getHandlers();
        for(Handler handler : handlers) {
            handler.setFormatter(new SimplerFormatter());
        }
    }

    public Logger getLogger() {
        if(logger == null) throw new RuntimeException("Application was not initiated correctly. Logger not set.");
        return logger;
    }

    public Handler getConsoleHandler() {
        return consoleHandler;
    }

    public void setConsoleHandler(Handler consoleHandler) {
        this.consoleHandler = consoleHandler;
    }

    public Handler getFileHandler() {
        return fileHandler;
    }

    public void setFileHandler(Handler fileHandler) {
        this.fileHandler = fileHandler;
    }



}
