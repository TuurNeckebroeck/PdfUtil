package org.tuurneckebroeck.pdfutil;

import org.tuurneckebroeck.pdfutil.log.NullLogger;
import org.tuurneckebroeck.pdfutil.log.VerbosityLogger;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Tuur Neckebroeck
 */
public final class Constant {

    // DESIGN Waarom singleton en geen static class?
    public static Constant getInstance(){
        if(instance == null) {
            instance = new Constant();
        }

        return instance;
    }


    private Constant() {
        initDirectories();
    }

    public void setLogger(VerbosityLogger logger) {
        this.logger = logger;
    }

    public VerbosityLogger getLogger() {
        return logger;
    }



    public static String getTimeStampedFileName(String name, String extension) {
        while (extension.startsWith(".")) extension = extension.substring(1);
        SimpleDateFormat format = new SimpleDateFormat("_yyyy-MMM-dd_HH-mm-ss.");
        return name + format.format(new Date()) + extension;
    }

    private static void initDirectories(){
        File appPathFile = new File(APP_PATH);
        if(!appPathFile.isDirectory()) {
            if(appPathFile.isFile()) {
                System.err.println("Could not initialize app home directory: file exists with name " + APP_PATH);
            }

            if(!appPathFile.mkdir()){
                System.err.println("Could not create app home directory " + APP_PATH);
            }
        }

        for(String subFolder : SUBFOLDERS) {
            File folder = new File(getAppPath() + getFileSeparator() + subFolder);
            if(!folder.isDirectory()) {
                if(folder.isFile()) {
                    System.err.println("Could not initialize app subfolder (file with same name exists): " + subFolder);
                }

                if(!folder.mkdir()){
                    System.err.println("Could not create app subfolder " + subFolder);
                }
            }
        }
    }

    public static final String getAppName() {
        return APP_NAME;
    }

    public static final String getFileSeparator() {
        return FILE_SEPARATOR;
    }

    public static final String getAppPath() {
        return APP_PATH;
    }

    public static final String getLogPath() {
        return APP_PATH + FILE_SEPARATOR + SUBFOLDER_LOG;
    }


    private VerbosityLogger logger = new NullLogger();
    private static Constant instance = null;

    private static final String FILE_SEPARATOR = System.getProperty("file.separator");
    private static final String HOME_PATH = System.getProperty("user.home");
    private static final String APP_FOLDER_NAME = "pdfutil";
    private static final String APP_PATH = HOME_PATH + FILE_SEPARATOR + "." + APP_FOLDER_NAME;

    public static final String APP_NAME = "PdfUtil";

    private static final String SUBFOLDER_LOG = "log";
    private static final String[] SUBFOLDERS = {SUBFOLDER_LOG};
}
