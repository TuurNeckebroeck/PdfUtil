package org.tuurneckebroeck.pdfutil.log;

import org.tuurneckebroeck.pdfutil.log.lib.LogLevel;
import org.tuurneckebroeck.pdfutil.log.lib.VerbosityLogger;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class CompositeLogger extends VerbosityLogger {


    public CompositeLogger(){
        super(LogLevel.DEBUG);
    }

    private CompositeLogger(LogLevel verbosityLevel) {
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
        boolean exceptionOccured = false;
        for(VerbosityLogger logger : loggers) {
            try{
                logger.tearDown();
            }catch (IOException e) {
                exceptionOccured = true;
            }
        }

        if(exceptionOccured) throw new IOException("Exception occured in composite logger teardown.");
    }

    private List<VerbosityLogger> loggers = new LinkedList<>();

}
