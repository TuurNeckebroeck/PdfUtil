package org.tuurneckebroeck.pdfutil.controller;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPageable;
import org.tuurneckebroeck.pdfutil.log.LogLevel;
import org.tuurneckebroeck.pdfutil.log.NullLogger;
import org.tuurneckebroeck.pdfutil.log.VerbosityLogger;
import org.tuurneckebroeck.pdfutil.task.RectoVersoPrintTask;
import org.tuurneckebroeck.pdfutil.task.lib.Task;
import org.tuurneckebroeck.pdfutil.task.lib.TaskCallbackHandler;

import java.io.File;

// TODO Refactor
public class PrintController {

    public void printPdf(File file) {
        Task task = new RectoVersoPrintTask(file, new TaskCallbackHandler() {
            @Override
            public void onCallback(Task.TaskStatus status) {
                //view.setWaiting(false);
                //view.showMessage(..);
                if(status == Task.TaskStatus.FAILED) {
                    logger.log(LogLevel.ERROR, getClass(), "RectoVersoPrintTask returned FAILED task status on callback.");
                }
            }
        });

        task.setLogger(logger);

        new Thread(task).run();
        //view.setWaiting(true);
    }

    public void setLogger(VerbosityLogger logger) {
        this.logger = logger;
    }

    private VerbosityLogger logger = NullLogger.instance();
}
