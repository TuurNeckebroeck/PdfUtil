package org.tuurneckebroeck.pdfutil.task;

import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.tuurneckebroeck.pdfutil.log.LogLevel;
import org.tuurneckebroeck.pdfutil.task.lib.TaskCallbackHandler;
import org.tuurneckebroeck.pdfutil.task.lib.Task;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Tuur Neckebroeck
 */
public final class MergeTask extends Task {

    public MergeTask(File[] files, File outputFile, TaskCallbackHandler parent) {
        super(parent);
        this.files = files;
        this.outputFile = outputFile;
    }

    @Override
    public void run() {
        setStatus(TaskStatus.EXECUTING);
        getLogger().log(LogLevel.DEBUG, getClass(), "Status: " + getStatus());

        PDFMergerUtility PDFMerger = new PDFMergerUtility();
        PDFMerger.setDestinationFileName(outputFile.getAbsolutePath());
        List<PDDocument> docs = new ArrayList<>();

        try {
            for (File f : files) {
                PDDocument p = PDDocument.load(f);
                docs.add(p);
                PDFMerger.addSource(f);
            }

            PDFMerger.mergeDocuments();

            for (PDDocument p : docs) {
                p.close();
            }
            setStatus(TaskStatus.FINISHED);

        } catch (IOException e) {
            setStatus(TaskStatus.FAILED);
            getLogger().log(LogLevel.ERROR, getClass(), String.format("Exception occurred during run: %s", e.getMessage()));
            e.printStackTrace();
        } catch (Exception e) {
            setStatus(TaskStatus.FAILED);
            getLogger().log(LogLevel.ERROR, getClass(), String.format("Exception occurred during run: %s", e.getMessage()));
            e.printStackTrace();
        } finally {
            getLogger().log(LogLevel.ERROR, getClass(), String.format("Status: %s %s performing callback to: %s", getStatus(), "          ",getCallbackHandler().getClass().getSimpleName()));
            callback();
        }
    }


    private File[] files;
    private File outputFile;
}
