package common.loggers;

import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class SimplerFormatter extends Formatter {

    @Override
    public String format(LogRecord record) {

        StringBuilder builder = new StringBuilder(512);
        if(record.getLevel() == Level.FINEST) {
            final String[] classPath = record.getSourceClassName().split("\\.");
            final String className = classPath[classPath.length-1];
            builder.append("> ")
                    .append(formatMessage(record))
                    .append(" @")
                    .append(className).append(".")
                    .append(record.getSourceMethodName());
        } else {
            builder.append("[").append(record.getSourceClassName()).append(".");
            builder.append(record.getSourceMethodName()).append("] - ");
            builder.append("[").append(record.getLevel()).append("] - ");
            builder.append(formatMessage(record));
        }
        builder.append("\n");
        return builder.toString();
    }


}

