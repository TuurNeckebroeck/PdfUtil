package org.tuurneckebroeck.pdfutil.log;

import org.tuurneckebroeck.pdfutil.log.lib.LogLevel;
import org.tuurneckebroeck.pdfutil.log.lib.VerbosityLogger;


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
