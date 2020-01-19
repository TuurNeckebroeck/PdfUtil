package org.tuurneckebroeck.pdfutil.log;


import org.tuurneckebroeck.pdfutil.Constant;

/**
 *  Proxy for uninitialised VerbosityLogger objects.
 *
 * @author Tuur Neckebroeck
 *
 */
public class NullLogger extends VerbosityLogger {

    public static NullLogger instance() {
        if(instance == null) {
            instance = new NullLogger();
        }

        return instance;
    }

    private NullLogger() {
        super(LogLevel.NONE);
    }

    @Override
    protected void logAll(LogLevel level, Class<?> parent, String content) {}

    @Override
    public void tearDown() {}

    private static NullLogger instance;
}
