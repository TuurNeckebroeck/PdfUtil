package org.tuurneckebroeck.pdfutil.Model;

/**
 *
 * @author Tuur Neckebroeck
 */
public enum FileType {
    PDF_ENCRYPTED(true), PDF_NOT_ENCRYPTED(true), DOES_NOT_EXIST(false), NOT_PDF(false), NOT_FILE(false);

    private FileType(boolean isPdf) {
        this.isPdf = isPdf;
    }

    public boolean isPdf(){
        return isPdf;
    }

    private boolean isPdf = false;

}
