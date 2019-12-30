package org.tuurneckebroeck.pdfutil;

import java.io.File;
import java.io.IOException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.tuurneckebroeck.pdfutil.Model.FileType;

public final class FileUtil {

    /**
     * @author: https://www.journaldev.com/842/java-get-file-extension
     */
    public static String getFileExtension(File file) {
        String fileName = file.getName();
        if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0) {
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        } else {
            return "";
        }
    }

    public static File addToFileName(File file, String addition) {
        String fileName = file.getName();
        int index = fileName.indexOf(".");
        String name = fileName.substring(0, index) + addition;
        return new File(file.getParentFile().getAbsolutePath() + (OSDetector.isWindows() ? "\\" : "/") + name + "." + getFileExtension(file));
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

    /**
     *
     * @author https://www.mkyong.com/java/how-to-detect-os-in-java-systemgetpropertyosname/
     *
     */
    public static class OSDetector {

        private static String OS = System.getProperty("os.name").toLowerCase();


        public static boolean isWindows() {

            return (OS.indexOf("win") >= 0);

        }

        public static boolean isMac() {

            return (OS.indexOf("mac") >= 0);

        }

        public static boolean isUnix() {

            return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0);

        }

        public static boolean isSolaris() {

            return (OS.indexOf("sunos") >= 0);

        }

        public static String getDesktopPath() {
            String destFile = destFile = System.getProperty("user.home");
            if (OSDetector.isWindows()) {
                destFile += "\\Desktop\\";
            } else {
                destFile += "/Desktop/";
            }
            return destFile;
        }

        public static String getPathSeparator() {
            return File.separator;
        }
    }
}
