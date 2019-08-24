package pdfutil;

import java.io.File;


public class FileUtil {
    
    /**
     * SOURCE: https://www.journaldev.com/842/java-get-file-extension
     */
    public static String getFileExtension(File file) {
        String fileName = file.getName();
        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
        return fileName.substring(fileName.lastIndexOf(".")+1);
        else return "";
    }
}
