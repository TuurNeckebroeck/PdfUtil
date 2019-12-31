package org.tuurneckebroeck.pdfutil.log;


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
        String fullClassName = parent.getTypeName();
        int lastIndex = fullClassName.lastIndexOf(".") + 1;
        String className = fullClassName.substring(lastIndex == -1 ? 0 : lastIndex);
        System.out.println(String.format("%-10s %-25s %s", level.toString(), className, content));
    }

    @Override
    public void tearDown() {}

}
