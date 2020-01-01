package org.tuurneckebroeck.pdfutil.task;

import org.apache.pdfbox.multipdf.Overlay;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.tuurneckebroeck.pdfutil.task.lib.Task;
import org.tuurneckebroeck.pdfutil.task.lib.TaskCallbackHandler;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class WatermarkTask extends Task {
    public WatermarkTask(File inputFile, File overlayFile, File outputFile, TaskCallbackHandler parent) {
        super(parent);
        this.inputFile = inputFile;
        this.overlayFile = overlayFile;
        this.outputFile = outputFile;
    }

    @Override
    public void run() {
        setStatus(TaskStatus.EXECUTING);
        try {
            Overlay overlay = new Overlay();
            overlay.setInputFile(inputFile.getPath());
            overlay.setAllPagesOverlayFile(overlayFile.getPath());
            overlay.overlay(new HashMap<>()).save(outputFile);
            overlay.close();
            setStatus(TaskStatus.FINISHED);
        } catch(IOException e) {
            setStatus(TaskStatus.FAILED);
        }

        // DESIGN run verplaatsen naar Task, in Task nieuwe abstracte funcie maken die functionaliteit moet implementeren (bv. executeTask)
        // in de run() in Task vervolgend deze executeTask aanroepen en vervolgens callback aanroepen,
        // dan moet callback niet meer handmatig aangeroepen worden in deze run.
        callback();
    }

    private File inputFile, overlayFile, outputFile;
}
