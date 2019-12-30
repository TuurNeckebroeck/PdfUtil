package org.tuurneckebroeck.pdfutil.View;

import org.tuurneckebroeck.pdfutil.Controller.SplitController;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FrameSplit extends JFrame {

    public FrameSplit() {
        initComponents();
    }

    public void setController(SplitController controller) {
        this.controller = controller;
    }

    private void initComponents() {
//        throw new IllegalStateException("FrameSplit components not initialised yet.");
        Logger.getLogger(getClass().getName()).log(Level.SEVERE, "FrameSplit GUI not implemented yet.");

        btnSplit = new JButton("Split");
        btnSplit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                btnSplitClicked();
            }
        });

        this.setSize(300,200);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.add(btnSplit);
    }

    private void btnSplitClicked() {
        checkController();
        int triesLeft = 3, page = -1;
        boolean validPage = false;
        do {
            try {
                page = Integer.parseInt(JOptionPane.showInputDialog("Split after page: "));
                if(page < 1) throw new NumberFormatException();
                validPage = true;
            } catch (NumberFormatException e) {
                triesLeft--;
            }
        } while (!validPage && triesLeft > 0);
        if(!validPage) return;
        controller.splitAfterPage(page);
    }

    /**
     * Throws an IllegalStateException if the controller is not set.
     */
    // DESIGN : duplicate code: FrameMain heeft deze functie ook, misschien interface view maken? functie is wel private ...
    private void checkController() {
        if(controller == null){
            throw new IllegalStateException("The FrameMainController has to be set in order to use the FrameMain view.");
        }
    }

    private JButton btnSplit;

    private SplitController controller;
}
