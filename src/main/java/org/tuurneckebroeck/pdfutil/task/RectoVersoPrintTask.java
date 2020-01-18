package org.tuurneckebroeck.pdfutil.task;

import jdk.nashorn.internal.scripts.JO;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPageable;
import org.tuurneckebroeck.pdfutil.task.lib.Task;
import org.tuurneckebroeck.pdfutil.task.lib.TaskCallbackHandler;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.PageRanges;
import javax.swing.*;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public class RectoVersoPrintTask extends Task {

    public RectoVersoPrintTask(File file, TaskCallbackHandler callbackHandler) {
        super(callbackHandler);
        this.file = file;
    }

    @Override
    public void run() {
        setStatus(TaskStatus.EXECUTING);
        try {
            PDDocument doc = PDDocument.load(file);
            PDDocument evenPages = new PDDocument(), oddPages = new PDDocument();
            for(int page = 0; page < doc.getNumberOfPages(); page ++) {
                if (page % 2 == 0) {
                    // page index 0 = page 1 = odd
                    oddPages.addPage(doc.getPage(page));
                } else {
                    evenPages.addPage(doc.getPage(page));
                }
            }
            PrinterJob printerJob = PrinterJob.getPrinterJob();
            System.out.println(printerJob.getPrintService().getName());



            printerJob.setPageable(new PDFPageable(oddPages));
            System.out.println("Print odd");
            //printerJob.print();
            oddPages.close();


            JOptionPane.showMessageDialog(null, "Klik op OK als de de geprinte paginas opnieuw ingevoerd zijn.");

            // TODO paginas van even omdraaien (en eventueel van laatste naar eerste laten printen?)
            printerJob = PrinterJob.getPrinterJob();

            // todo herschrijven
            PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
            for(PrintService service : printServices) {
                if(service.getName().contains("1102")) {
                    printerJob.setPrintService(service);
                    break;
                }
                System.out.println(service.getName());
            }

            printerJob.setPageable(new PDFPageable(evenPages));
            System.out.println("Print even");
            printerJob.print();
            evenPages.close();


            doc.close();
            setStatus(TaskStatus.FINISHED);
            callback();
        } catch (IOException | PrinterException e) {
            // TODO logger initialiseren
            setStatus(TaskStatus.FAILED);
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    private File file;
}
