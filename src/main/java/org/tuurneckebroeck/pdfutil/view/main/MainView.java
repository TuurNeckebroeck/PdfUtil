package org.tuurneckebroeck.pdfutil.view.main;

import org.tuurneckebroeck.pdfutil.controller.MainController;

import javax.swing.*;

public interface MainView {

    void setController(MainController controller);

    void setWaiting(boolean waiting);

    void setFileListModel(ListModel<String> model);

    void setSelectedFileIndex(int index);

    void showMessage(String message);

    void showView();

}
