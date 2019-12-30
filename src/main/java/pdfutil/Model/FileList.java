package pdfutil.Model;

import pdfutil.View.FrameMain;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public final class FileList extends ArrayList<FileListElement> {

    public String[] getDisplayNames() {
        String[] displayNames = new String[size()];
        for (int i = 0; i < size(); i++) {
            displayNames[i] = get(i).getDisplayText();
        }
        return displayNames;
    }

    public String[] getFiles() {
        String[] files = new String[size()];
        for (int i = 0; i < size(); i++) {
            files[i] = get(i).getFile().getAbsolutePath();
        }
        return files;
    }

    public void moveDown(int index) {
        if (index < 0 || index >= size() - 1) {
            return;
        }

        FileListElement element = get(index);
        remove(index);
        add(index + 1, element);
    }

    public void moveUp(int index) {
        if (index <= 0 || index > size() - 1) {
            return;
        }

        FileListElement element = get(index);
        remove(index);
        add(index - 1, element);
    }

    public DefaultListModel getDefaultListModel() {
        DefaultListModel model = new DefaultListModel();
        for (FileListElement el : this) {
            model.addElement(el.getDisplayText());
        }
        return model;
    }

}