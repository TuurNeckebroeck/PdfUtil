package org.tuurneckebroeck.pdfutil.log;


import org.tuurneckebroeck.pdfutil.log.lib.LogLevel;
import org.tuurneckebroeck.pdfutil.log.lib.VerbosityLogger;


/**
 * @author Tuur Neckebroeck
 */
public class ConsoleLogger extends VerbosityLogger {

    /**
     * Initialises the ConsoleLogger with default verbosity ERROR.
     *
     */
    public ConsoleLogger() {
        super(LogLevel.ERROR);
    }

    /**
     * Initialises the ConsoleLogger with the given verbosity level.
     *
     * @param verbosityLevel
     */
    public ConsoleLogger(LogLevel verbosityLevel) {
        super(verbosityLevel);
    }

    @Override
    protected void logAll(LogLevel level, Class<?> parent, String content){
        System.out.println(String.format("%-10s %-25s %s", level.toString(), parent.getSimpleName(), content));
    }

    @Override
    public void tearDown() {}

}
