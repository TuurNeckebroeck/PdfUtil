package org.tuurneckebroeck.pdfutil.view.main;

import org.tuurneckebroeck.pdfutil.controller.MainController;

import javax.swing.*;

public class NullMainView implements MainView {
    @Override
    public void setController(MainController controller) {

    }

    @Override
    public void setWaiting(boolean waiting) {

    }

    @Override
    public void setFileListModel(ListModel<String> model) {

    }

    @Override
    public void setSelectedFileIndex(int index) {

    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public void showView() {

    }
}
