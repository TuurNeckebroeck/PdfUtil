package org.tuurneckebroeck.pdfutil.task;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.tuurneckebroeck.pdfutil.log.LogLevel;
import org.tuurneckebroeck.pdfutil.task.lib.TaskCallbackHandler;
import org.tuurneckebroeck.pdfutil.task.lib.Task;

import java.io.File;
import java.io.IOException;

/**
 * @author Tuur Neckebroeck
 */
public final class SplitTask extends Task {

    /**
     *
     * @param parent
     * @param inputFile The PDF document to be splitted
     * @param splitPage The page after which the document will be splitted
     */
    public SplitTask(File inputFile, int splitPage, File firstOutputFile, File secondOutputFile, TaskCallbackHandler parent) {
        super(parent);
        this.file = inputFile;
        this.splitPage = splitPage;
        this.firstOutputFile = firstOutputFile;
        this.secondOutputFile = secondOutputFile;
    }

    @Override
    public void run() {
        setStatus(TaskStatus.EXECUTING);
        getLogger().log(LogLevel.DEBUG, getClass(), "Status: " + getStatus());
        try {
            PDDocument doc = PDDocument.load(file);
            if(!isValidSplitPage(doc, splitPage)) {
                throw new IllegalArgumentException(String.format("Illegal splitPage (%d) supplied for file '%s'", splitPage, file.getAbsolutePath()));
            }

            PDDocument firstPart = new PDDocument(),
                        secondPart = new PDDocument();
            PDDocumentInformation docInfo = doc.getDocumentInformation();
            // DESIGN creator ophalen via singleton?
            docInfo.setCreator("PdfUtil - Tuur Neckebroeck");
            firstPart.setDocumentInformation(docInfo);
            firstPart.setDocumentInformation(docInfo);

            secondPart.setDocumentInformation(doc.getDocumentInformation());
            for(int pageNb = 0; pageNb < doc.getNumberOfPages(); pageNb++) {
                PDDocument docToAdd = pageNb < splitPage ? firstPart : secondPart;
                docToAdd.addPage(doc.getPage(pageNb));
            }

            if(firstOutputFile != null) firstPart.save(firstOutputFile);
            if(secondOutputFile != null) secondPart.save(secondOutputFile);

            doc.close();
            firstPart.close();
            secondPart.close();

            setStatus(TaskStatus.FINISHED);
        } catch (InvalidPasswordException e) {
            setStatus(TaskStatus.FAILED);
            getLogger().log(LogLevel.ERROR, getClass(), String.format("Exception occurred during run: %s", e.getMessage()));
        } catch (IOException e) {
            setStatus(TaskStatus.FAILED);
            getLogger().log(LogLevel.ERROR, getClass(), String.format("Exception occurred during run: %s", e.getMessage()));
        } catch (IllegalArgumentException e) {
            //Illegal splitpage supplied
            setStatus(TaskStatus.FAILED);
            getLogger().log(LogLevel.ERROR, getClass(), String.format("Exception occurred during run: %s", e.getMessage()));
        }

        getLogger().log(LogLevel.DEBUG, getClass(), String.format("Status: %s %s performing callback to: %s", getStatus(), "          ",getCallbackHandler().getClass().getSimpleName()));
        callback();
    }

    private boolean isValidSplitPage(PDDocument doc, int page) {
        return (page > 0) && (page < doc.getNumberOfPages());
    }

    private File file, firstOutputFile, secondOutputFile;
    private int splitPage;
}
