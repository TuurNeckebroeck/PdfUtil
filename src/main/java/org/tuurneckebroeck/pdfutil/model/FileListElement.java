package org.tuurneckebroeck.pdfutil.model;

import java.io.File;

/**
 * @author Tuur Neckebroeck
 */
public class FileListElement {

    private final File file;
    private String displayText;

    public FileListElement(File f) {
        this.file = f;
        this.setDisplayText(f.getAbsolutePath());
    }

    public void setDisplayText(String text) {
        this.displayText = text;
    }

    public void appendToDisplayText(String s) {
        setDisplayText(displayText + s);
    }

    public String getDisplayText() {
        return this.displayText;
    }

    public File getFile() {
        return this.file;
    }

}