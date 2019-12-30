package org.tuurneckebroeck.pdfutil.controller;

import org.tuurneckebroeck.pdfutil.task.lib.CallbackHandler;
import org.tuurneckebroeck.pdfutil.task.SplitTask;
import org.tuurneckebroeck.pdfutil.view.FrameSplit;
import org.tuurneckebroeck.pdfutil.FileUtil;
import org.tuurneckebroeck.pdfutil.task.lib.Task;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * @author Tuur Neckebroeck
 */
public class SplitController {

    public SplitController(FrameSplit view, File inputFile) {
        this.view = view;
        this.inputFile = inputFile;

        initView();
    }

    private void initView() {
        checkView();
        view.setController(this);
        //view.setTitle...
    }

    public void showSplitView(){
        checkView();
        view.setVisible(true);
    }


    public void splitAfterPage(int page) {
        checkView();
        // TODO aanpassen
        String parent = inputFile.toPath().getParent().toString();
        File firstOutputFile = new File(parent + FileUtil.OSDetector.getPathSeparator() + "splitted_1.pdf");
        File secondOutputFile = new File(parent + FileUtil.OSDetector.getPathSeparator() + "splitted_2.pdf");
        System.out.println(firstOutputFile.getPath());
        System.out.println(secondOutputFile.getPath());
        Task splitTask = new SplitTask(inputFile, page, firstOutputFile, secondOutputFile, new CallbackHandler() {
            @Override
            public void onCallback(Task.TaskStatus status) {
                view.getContentPane().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                if(status == Task.TaskStatus.FINISHED) {
                    JOptionPane.showMessageDialog(view, "Finished succesfully");
                } else {
                    JOptionPane.showMessageDialog(view, "Split failed: " + status.toString());
                }
            }
        });

        new Thread(splitTask).start();
        view.getContentPane().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    }

    private void checkView() {
        if (view == null) {
            throw new IllegalStateException("The FrameSplit view of this controller should not be null.");
        }
    }

    private FrameSplit view;
    private File inputFile;
}
