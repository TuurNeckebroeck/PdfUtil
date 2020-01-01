package org.tuurneckebroeck.pdfutil.tmp.development;

import org.tuurneckebroeck.pdfutil.log.*;
import org.tuurneckebroeck.pdfutil.task.RemoveWatermarkTask;
import org.tuurneckebroeck.pdfutil.task.lib.Task;
import org.tuurneckebroeck.pdfutil.task.lib.TaskCallbackHandler;

import java.io.File;

public class RemoveWatermarkTaskDevelopment {


    public static void main(String[] args) {
        new RemoveWatermarkTaskDevelopment().run();
    }

    public void run() {
        try{
        logger.registerLogger(new ConsoleLogger(LogLevel.DEBUG));
        logger.registerLogger(new FileLogger(LogLevel.DEBUG, new File(RESOURCES + "/log.txt"), true));

        File inputFile = new File(RESOURCES + "/watermarked.pdf");
        File outputFile = new File(RESOURCES + "/removed_watermark.pdf");
        RemoveWatermarkTask removeTask = new RemoveWatermarkTask(inputFile, outputFile, new TaskCallbackHandler() {
            @Override
            public void onCallback(Task.TaskStatus status) {
                logger.log(LogLevel.DEBUG, getClass(), "FINISHED REMOVE WATERMARK TASK: " + status.toString());

            }
        });
        removeTask.setLogger(logger);

        new Thread(removeTask).start();
        } catch (Exception e) {
            logger.log(LogLevel.ERROR, getClass(), e.getMessage());
        }

    }

    private final CompositeLogger logger = new CompositeLogger(LogLevel.DEBUG);
    private final static String RESOURCES = "/home/tuur/IdeaProjects/PdfUtil/src/main/resources/org.tuurneckebroeck.pdfutil.task/RemoveWatermarkTask";
}
