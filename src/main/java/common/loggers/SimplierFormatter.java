package common.loggers;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class SimplierFormatter extends Formatter {

    @Override
    public String format(LogRecord record) {

        StringBuilder builder = new StringBuilder(512);
        builder.append("[").append(record.getSourceClassName()).append(".");
        builder.append(record.getSourceMethodName()).append("] - ");
        builder.append("[").append(record.getLevel()).append("] - ");
        builder.append(formatMessage(record));
        builder.append("\n");
        return builder.toString();
    }


}

