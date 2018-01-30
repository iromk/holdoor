package holdoor.srv;

import java.io.IOException;
import java.util.logging.*;

public class ServerLogger {

    private static final String DEFAULT_LOG_FILE = "./holdoor_server.log";

    private static Logger logger = null;

    public static Logger setup() {

        if(logger != null) return logger;

        logger = Logger.getLogger(Server.class.getName());

        Handler consoleHandler = null;
        Handler fileHandler  = null;

        try { // to create and attach console and file handlers

            /*
            // Setup here console handler if no default for some reason
            consoleHandler = new ConsoleHandler();
            logger.addHandler(consoleHandler);
            consoleHandler.setLevel(Level.ALL);
            */

            fileHandler  = new FileHandler(DEFAULT_LOG_FILE, true);
            fileHandler.setFormatter(new SimpleFormatter()); // plain text is enough for now
            logger.addHandler(fileHandler);
            fileHandler.setLevel(Level.ALL);

            logger.setLevel(Level.ALL);

        } catch(IOException e){
            // treat problem with logging to a file as not fatal, but report
            logger.log(Level.WARNING, "FileHandler cannot be initialized. Logging to console only.", e);
        }

        return logger;

    }


}
