package common.loggers;

import common.core.App;

public class LogContext {

    private StringBuilder collectedLog = new StringBuilder(512);
    private int resetPosition = 0;

    public LogContext() {}

    public LogContext(String text) {
        if(App.isContextEnabled()) {
            collectedLog.append("\n[Context: ").append(text).append("]");
            resetPosition = collectedLog.length();
        }
    }

    public LogContext add(String text) {
        if(App.isContextEnabled()) {
            collectedLog.append("\n  ").append(text);
        }
        return this;
    }

    public LogContext reset() {
        if(App.isContextEnabled())
            collectedLog.setLength(resetPosition);
        return this;
    }

    public LogContext renew() {
        if(App.isContextEnabled())
            collectedLog.setLength(0);
        return this;
    }

    public String produce() {
        if (App.isContextEnabled()) {
            return collectedLog.append("\n[/Context]").toString();
        }
        return "";
    }
}
