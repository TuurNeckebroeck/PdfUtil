package org.tuurneckebroeck.pdfutil.tmp.development;

import org.tuurneckebroeck.pdfutil.log.CompositeLogger;
import org.tuurneckebroeck.pdfutil.log.ConsoleLogger;
import org.tuurneckebroeck.pdfutil.log.FileLogger;
import org.tuurneckebroeck.pdfutil.log.LogLevel;
import org.tuurneckebroeck.pdfutil.task.ExtractTextTask;
import org.tuurneckebroeck.pdfutil.task.RemoveWatermarkTask;
import org.tuurneckebroeck.pdfutil.task.lib.NullTaskCallbackHandler;
import org.tuurneckebroeck.pdfutil.task.lib.Task;
import org.tuurneckebroeck.pdfutil.task.lib.TaskCallbackHandler;

import java.io.File;

public class ExtractTextTaskDevelopment {

    public static void main(String[] args) {
        new ExtractTextTaskDevelopment().run();
    }


    public void run() {
        try{
            logger.registerLogger(new ConsoleLogger(LogLevel.DEBUG));
            logger.registerLogger(new FileLogger(LogLevel.WARNING, new File(RESOURCES + "/warning_log.txt"), true));

            File inputFile = new File(RESOURCES + "/watermarked.pdf");
            File outputFile = new File(RESOURCES + "/extracted_text_watermarked.txt");
            ExtractTextTask removeTask = new ExtractTextTask(inputFile, outputFile, new NullTaskCallbackHandler());
            removeTask.setLogger(logger);
            new Thread(removeTask).start();
        } catch (Exception e) {
            logger.log(LogLevel.ERROR, getClass(), e.getMessage());
        }

    }

    private final CompositeLogger logger = new CompositeLogger(LogLevel.DEBUG);
    private final static String RESOURCES = "/home/tuur/IdeaProjects/PdfUtil/src/main/resources/org.tuurneckebroeck.pdfutil.task/ExtractTextTask";
}
