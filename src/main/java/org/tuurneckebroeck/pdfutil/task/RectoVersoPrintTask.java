package org.tuurneckebroeck.pdfutil.task;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.printing.PDFPageable;
import org.tuurneckebroeck.pdfutil.log.LogLevel;
import org.tuurneckebroeck.pdfutil.task.lib.Task;
import org.tuurneckebroeck.pdfutil.task.lib.TaskCallbackHandler;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.swing.*;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;

public class RectoVersoPrintTask extends Task {

    public RectoVersoPrintTask(File file, TaskCallbackHandler callbackHandler) {
        super(callbackHandler);
        this.file = file;
    }

    /**
     * Select printer based on name, containing the given string.
     *
     * @param partialName The subname of the printer to select
     */
    public void setPrinterByName(String partialName) {
        partialName = partialName.toLowerCase();

        PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
        for(PrintService service : printServices) {
            if(service.getName().toLowerCase().contains(partialName)) {
                useDefaultPrinter = false;
                selectedPrinterService = service;
                return;
            }
        }
    }

    @Override
    public void run() {
        setStatus(TaskStatus.EXECUTING);
        getLogger().log(LogLevel.DEBUG, getClass(), "Status: " + getStatus());
        try {
            PDDocument doc = PDDocument.load(file);
            PDDocument oddPagesDoc = composeOddPagesDoc(doc),
                    evenPagesDoc = composeEvenPagesDoc(doc);

            PrinterJob printerJob = PrinterJob.getPrinterJob();
            if(!useDefaultPrinter) {
                printerJob.setPrintService(selectedPrinterService);
            }
//            System.out.println("Selected: " + printerJob.getPrintService().getName());

            // PRINT ODD PAGES
            printerJob.setPageable(new PDFPageable(oddPagesDoc));
            printerJob.setJobName(String.format(" %s - odd pages", file.getName()));
            printerJob.print();
            oddPagesDoc.close();

            int result = JOptionPane.showConfirmDialog(null, "Move the printed pages into the input tray.");
            if(result == JOptionPane.OK_OPTION) {
                // WAIT FOR PRINTING - DIALOG OK

                printerJob = PrinterJob.getPrinterJob();
                if(!useDefaultPrinter) {
                    printerJob.setPrintService(selectedPrinterService);
                }

                // PRINT EVEN PAGES
                printerJob.setPageable(new PDFPageable(evenPagesDoc));
                printerJob.setJobName(String.format(" %s - even pages", file.getName()));
                printerJob.print();
                evenPagesDoc.close();
            } else {
                getLogger().log(LogLevel.WARNING, getClass(), String.format("Print task canceled via dialog."));
            }

            doc.close();
            setStatus(TaskStatus.FINISHED);
        } catch (IOException | PrinterException e) {
            setStatus(TaskStatus.FAILED);
//            e.printStackTrace();
            getLogger().log(LogLevel.ERROR, getClass(), String.format("Exception occurred during run: %s", e.getMessage()));
        }

        getLogger().log(LogLevel.DEBUG, getClass(), String.format("Status: %s %s performing callback to: %s", getStatus(), "          ", getCallbackHandler().getClass().getSimpleName()));
        callback();
    }

    private PDDocument composeEvenPagesDoc(PDDocument sourceDoc) {
        int nbPages = sourceDoc.getNumberOfPages();
        PDDocument evenPagesDoc = new PDDocument();

        if(nbPages % 2 != 0) {
            evenPagesDoc.addPage(new PDPage(PDRectangle.A4));
        }

        for(int pageInd = nbPages%2==0?nbPages-1:nbPages-2; pageInd > 0; pageInd -= 2) {
            PDPage page = sourceDoc.getPage(pageInd);
            page.setRotation(180);
            evenPagesDoc.addPage(page);
        }

        return evenPagesDoc;
    }

    private PDDocument composeOddPagesDoc(PDDocument sourceDoc) {
        int nbPages = sourceDoc.getNumberOfPages();
        PDDocument oddPagesDoc = new PDDocument();

        for(int pageInd = 0; pageInd < nbPages; pageInd += 2) {
            oddPagesDoc.addPage(sourceDoc.getPage(pageInd));
        }

        return oddPagesDoc;
    }

    private boolean useDefaultPrinter = true;
    private PrintService selectedPrinterService;
    private File file;
}
