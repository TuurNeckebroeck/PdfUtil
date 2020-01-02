package org.tuurneckebroeck.pdfutil.task;

import org.apache.pdfbox.multipdf.Overlay;
import org.tuurneckebroeck.pdfutil.log.LogLevel;
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
        getLogger().log(LogLevel.DEBUG, getClass(), "Status: " + getStatus());

        try {
            Overlay overlay = new Overlay();
            overlay.setInputFile(inputFile.getPath());
            overlay.setAllPagesOverlayFile(overlayFile.getPath());
            overlay.overlay(new HashMap<>()).save(outputFile);
            overlay.close();
            setStatus(TaskStatus.FINISHED);
        } catch(IOException e) {
            setStatus(TaskStatus.FAILED);
            getLogger().log(LogLevel.ERROR, getClass(), String.format("Exception occurred during run: %s", e.getMessage()));
        }

        // DESIGN run verplaatsen naar Task, in Task nieuwe abstracte funcie maken die functionaliteit moet implementeren (bv. executeTask)
        // in de run() in Task vervolgend deze executeTask aanroepen en vervolgens callback aanroepen,
        // dan moet callback niet meer handmatig aangeroepen worden in deze run.
        getLogger().log(LogLevel.DEBUG, getClass(), String.format("Status: %s %s performing callback to: %s", getStatus(), "          ",getCallbackHandler().getClass().getSimpleName()));
        callback();
    }

    private File inputFile, overlayFile, outputFile;
}
