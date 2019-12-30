package org.tuurneckebroeck.pdfutil.log;


/**
 *  Proxy for uninitialised VerbosityLogger objects.
 *
 * @author Tuur Neckebroeck
 *
 */
public class NullLogger extends VerbosityLogger {

    public NullLogger() {
        super(LogLevel.NONE);
    }

    @Override
    protected void logAll(LogLevel level, Class<?> parent, String content) {}

    @Override
    public void tearDown() {}
}
