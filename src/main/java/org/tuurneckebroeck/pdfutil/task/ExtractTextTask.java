package org.tuurneckebroeck.pdfutil.task;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.text.PDFTextStripper;
import org.tuurneckebroeck.pdfutil.log.LogLevel;
import org.tuurneckebroeck.pdfutil.task.lib.Task;
import org.tuurneckebroeck.pdfutil.task.lib.TaskCallbackHandler;

import java.io.*;

@Deprecated
public class ExtractTextTask extends Task {

    public ExtractTextTask(File inputFile, File outputFile, TaskCallbackHandler parent) {
        super(parent);
        this.inputFile = inputFile;
        this.outputFile = outputFile;
    }

    @Override
    public void run() {
        setStatus(TaskStatus.EXECUTING);
        getLogger().log(LogLevel.DEBUG, getClass(), "ExtractTextTask :: EXECUTING");

        PDFTextStripper stripper = null;
        try {
            PDDocument input = PDDocument.load(inputFile);

            stripper = new PDFTextStripper();
            stripper.setStartPage(0);
            stripper.setEndPage(input.getNumberOfPages());
            Writer fileWriter = new FileWriter(outputFile);
            stripper.writeText(input, fileWriter);
            fileWriter.close();
            input.close();

            setStatus(TaskStatus.FINISHED);
        } catch (IOException e) {
            setStatus(TaskStatus.FAILED);
            e.printStackTrace();
        }

        getLogger().log(LogLevel.DEBUG, getClass(), "ExtractTextTask :: " + getStatus());
        callback();
    }

    private File inputFile, outputFile;
}
