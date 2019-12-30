package org.tuurneckebroeck.pdfutil.log;

import org.tuurneckebroeck.pdfutil.log.lib.LogLevel;
import org.tuurneckebroeck.pdfutil.log.lib.VerbosityLogger;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Tuur Neckebroeck
 */
public class FileLogger extends VerbosityLogger {

    /**
     * Initialises the FileLogger with default verbosity ERROR.
     *
     */
    public FileLogger(File logFile, boolean append) throws FileNotFoundException {
        super(LogLevel.ERROR);
        init(logFile, append);
    }

    public FileLogger(LogLevel verbosityLevel, File logFile, boolean append) throws FileNotFoundException {
        super(verbosityLevel);
        init(logFile, append);
    }

    private void init(File logFile, boolean append) throws FileNotFoundException {
        if(!isValidFile(logFile)) throw new IllegalArgumentException("Illegal log file supplied.");

        this.logFile = logFile;
        this.fileOutputStream = new FileOutputStream(logFile, append);
        this.printWriter = new PrintWriter(fileOutputStream);
        this.canWrite = true;
    }

    private static boolean isValidFile(File file) {
//        return file.isFile();
        return !file.isDirectory();
    }

    @Override
    protected void logAll(LogLevel level, Class<?> parent, String content) {
        if (!canWrite) return;

        Date now = new Date(System.currentTimeMillis());
        printWriter.println(dateFormatter.format(now));
        printWriter.println(String.format("%s%-10s %-25s %s", "    ", level.toString(), parent.getSimpleName(), content));
        printWriter.flush();
    }

    @Override
    public void tearDown() throws IOException {
        if(fileOutputStream != null) fileOutputStream.close();
        if(printWriter != null) fileOutputStream.close();
        canWrite = false;
    }

    private SimpleDateFormat dateFormatter = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
    private File logFile;
    private PrintWriter printWriter;
    private FileOutputStream fileOutputStream;
    private boolean canWrite = false;
}