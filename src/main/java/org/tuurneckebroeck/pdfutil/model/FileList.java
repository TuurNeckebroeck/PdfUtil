package org.tuurneckebroeck.pdfutil.model;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;

/**
 * @author Tuur Neckebroeck
 */
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

    public File[] indicesToFiles(int[] indices) {
        File files[] = new File[indices.length];
        for (int i = 0; i < indices.length; i++) {
            files[i] = get(indices[i]).getFile();
        }
        return files;
    }

}