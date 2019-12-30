package org.tuurneckebroeck.pdfutil.log;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * CompositeLogger is a VerbosityLogger which can contain multiple VerbosityLogger's,
 * each with their own verbosities.
 * The default verbosity level of the CompositeLogger is the highest (weakest) possible,
 * so by default, all logs will be passed to the subloggers.
 *
 * @author Tuur Neckebroeck
 */
public class CompositeLogger extends VerbosityLogger {


    public CompositeLogger(){
        super(LogLevel.DEBUG);
    }

    public CompositeLogger(LogLevel verbosityLevel) {
        super(verbosityLevel);
    }

    public void registerLogger(VerbosityLogger logger) {
        loggers.add(logger);
    }

    public void unregisterLogger(VerbosityLogger logger) {
        loggers.remove(logger);
    }

    @Override
    protected void logAll(LogLevel level, Class<?> parent, String content) {
        loggers.forEach(logger -> logger.log(level, parent, content));
    }

    @Override
    public void tearDown() throws IOException {
        boolean exceptionOccurred = false;
        for(VerbosityLogger logger : loggers) {
            try{
                logger.tearDown();
            }catch (IOException e) {
                exceptionOccurred = true;
            }
        }

        if(exceptionOccurred) throw new IOException("Exception occurred in composite logger teardown.");
    }

    private List<VerbosityLogger> loggers = new LinkedList<>();

}
