package org.tuurneckebroeck.pdfutil.log.lib;

import java.io.IOException;

/**
 * A logger to which logs with different verbosities can be logged.
 * The maximum verbosity level to log can be set,
 * so the logger will only log logs with an equal or lower verbosity.
 *
 * The verbosity levels are defined as the enum org.tuurneckebroeck.pdfutil.log.lib.LogLevel
 *
 */
public abstract class VerbosityLogger {
// DESIGN misschien static maken? ConsoleLogger.log(level ..) en dus 1 verbosity level voor elke consolelogger

    /**
     *  Creates a VerbosityLogger object, given the maximum verbosity level to log.
     *
     * @param verbosityLevel The maximum verbosity level to log.
     */
    public VerbosityLogger(LogLevel verbosityLevel) {
        setVerbosity(verbosityLevel);
    }

    /**
     * Function that logs if the logger verbosity level is equal to or greater than the given verbosity level.
     *
     * @param level The verbosity level to be logged
     * @param parent The parent class to be logged
     * @param content The custom message to be logged
     */
    public void log(LogLevel level, Class<?> parent, String content) {
        if(level.ordinal() <= verbosityLevel.ordinal()) {
            logAll(level, parent, content);
        }
    }


    /**
     * Set the maximum verbosity level to log.
     *
     * @param level The maximum verbosity level to log.
     */
    public final void setVerbosity(LogLevel level) {
        verbosityLevel = level;
    }

    /**
     * Function that logs all verbosity levels.
     * This function should be implemented by VerbosityLogger subclasses
     * but not be accessible outside this package by non-subclasses.
     *
     * @param level The verbosity level to be logged
     * @param parent The parent class to be logged
     * @param content The custom message to be logged
     */
    protected abstract void logAll(LogLevel level, Class<?> parent, String content);

    /**
     * Tears down this VerbosityLogger.
     * This function should close all open streams, writers ...
     * The logger should not be used afterwards.
     *
     * @throws IOException in case of eg. an IOException when closing a logfile.
     */
    public abstract void tearDown() throws IOException;

    private  LogLevel verbosityLevel;

}
