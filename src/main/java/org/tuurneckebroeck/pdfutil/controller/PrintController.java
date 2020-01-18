package org.tuurneckebroeck.pdfutil.controller;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPageable;
import org.tuurneckebroeck.pdfutil.task.RectoVersoPrintTask;
import org.tuurneckebroeck.pdfutil.task.lib.Task;
import org.tuurneckebroeck.pdfutil.task.lib.TaskCallbackHandler;

import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttribute;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.PageRanges;
import java.awt.print.PageFormat;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;

// TODO Refactor
public class PrintController {

    public void printPdf(File file) {
        Task task = new RectoVersoPrintTask(file, new TaskCallbackHandler() {
            @Override
            public void onCallback(Task.TaskStatus status) {
                //view.setWaiting(false);
                //view.showMessage(..);
                System.out.println("PrintTask finished: " + status.toString());
            }
        });

        new Thread(task).run();
        //view.setWaiting(true);
    }
}
