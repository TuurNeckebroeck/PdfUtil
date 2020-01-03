package org.tuurneckebroeck.pdfutil.util;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.tuurneckebroeck.pdfutil.model.FileType;

public final class FileUtil {

    public static String getFileExtension(File file) {
        String fileName = file.getName();
        int lastPointIndex = fileName.lastIndexOf(".");
        return (lastPointIndex != -1 && lastPointIndex != 0) ? fileName.substring(lastPointIndex + 1) : "";
    }

    public static File addToFileName(File file, String addition) {
        String fileName = file.getName();
        int index = fileName.indexOf(".");
        String name = fileName.substring(0, index) + addition;
        return new File(file.getParentFile().getAbsolutePath() + getPathSeparator() + name + "." + getFileExtension(file));
    }

    public static FileType getFileType(File file) {
        PDDocument doc = null;
        try {
            if(!file.exists()) return FileType.DOES_NOT_EXIST;
            if(!file.isFile()) return FileType.NOT_FILE;
            doc = PDDocument.load(file);
        } catch (InvalidPasswordException e) {
            return FileType.PDF_ENCRYPTED;
        } catch (IOException e){
            return FileType.NOT_PDF;
        } finally {
            try {
                if (doc != null) doc.close();
            } catch(IOException e) {
                e.printStackTrace();
            }
        }

        return FileType.PDF_NOT_ENCRYPTED;
    }

    // TODO verplaatsen naar andere util klasse
    public static String getExceptionStackTrace(Exception e) {
        StringWriter w = new StringWriter();
        PrintWriter p = new PrintWriter(w);
        e.printStackTrace(p);
        p.close();
        return w.toString();
    }

    public static String getOSName() {return System.getProperty("os.name").toLowerCase();}
    public static boolean isWindows() {return (getOSName().indexOf("win") >= 0);}
    public static boolean isMac() { return (getOSName().indexOf("mac") >= 0); }
    public static boolean isUnix() { return (getOSName().indexOf("nix") >= 0 || getOSName().indexOf("nux") >= 0 || getOSName().indexOf("aix") > 0); }
    public static boolean isSolaris() { return (getOSName().indexOf("sunos") >= 0);}

    public static String getPathSeparator() {
        return System.getProperty("path.separator");
    }

}
