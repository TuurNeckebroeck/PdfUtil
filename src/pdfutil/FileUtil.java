package pdfutil;

import java.io.File;
import java.io.IOException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;

/**
 * 
 * @author Tuur Neckebroeck
 */
public class FileUtil {

    /**
     * author: https://www.journaldev.com/842/java-get-file-extension
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

    public static FILE_TYPE getFileType(File file) {
        try {
            if(!file.exists()) return FILE_TYPE.DOES_NOT_EXIST;
            if(!file.isFile()) return FILE_TYPE.NOT_FILE;
            PDDocument doc = PDDocument.load(file);
            
        } catch (InvalidPasswordException e) {
            return FILE_TYPE.PDF_ENCRYPTED;
        } catch (IOException e){
            return FILE_TYPE.NOT_PDF;
        } 

        return FILE_TYPE.PDF_NOT_ENCRYPTED;
    }
}
