package org.tuurneckebroeck.pdfutil.log.lib;

import java.io.IOException;

// DESIGN misschien static maken? ConsoleLogger.log(level ..) en dus 1 verbosity level voor elke consolelogger
public abstract class VerbosityLogger {

    public VerbosityLogger(LogLevel verbosityLevel) {
        setVerbosity(verbosityLevel);
    }

    public void log(LogLevel level, Class<?> parent, String content) {
        if(level.ordinal() <= verbosityLevel.ordinal()) {
            logAll(level, parent, content);
        }
    }

    public final void setVerbosity(LogLevel level) {
        verbosityLevel = level;
    }

    protected abstract void logAll(LogLevel level, Class<?> parent, String content);

    public abstract void tearDown() throws IOException;

    private  LogLevel verbosityLevel;

}
