package pdfutil.Task;

import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class MergeTask extends Task {

    public MergeTask(File[] files, File outputFile, CallbackHandler parent) {
        super(parent);
        this.files = files;
        this.outputFile = outputFile;
    }

    @Override
    public void run() {
        setStatus(TaskStatus.EXECUTING);

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
            e.printStackTrace();
        } catch (Exception e) {
            setStatus(TaskStatus.FAILED);
            e.printStackTrace();
        } finally {
            callback();
        }
    }


    private File[] files;
    private File outputFile;
}
