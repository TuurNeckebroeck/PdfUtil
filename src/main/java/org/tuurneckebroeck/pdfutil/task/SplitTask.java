package org.tuurneckebroeck.pdfutil.task;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
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
        try {
            PDDocument doc = PDDocument.load(file);
            if(!isValidSplitPage(doc, splitPage)) {
                throw new IllegalArgumentException("Illegal splitPage supplied");
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
        } catch (IOException e) {
            setStatus(TaskStatus.FAILED);
        } catch (IllegalArgumentException e) {
            //Illegal splitpage supplied
            setStatus(TaskStatus.FAILED);
        }

        callback();
    }

    private boolean isValidSplitPage(PDDocument doc, int page) {
        return (page > 0) && (page < doc.getNumberOfPages());
    }

    private File file, firstOutputFile, secondOutputFile;
    private int splitPage;
}
