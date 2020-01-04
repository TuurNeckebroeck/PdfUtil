package org.tuurneckebroeck.pdfutil.task;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.tuurneckebroeck.pdfutil.log.LogLevel;
import org.tuurneckebroeck.pdfutil.task.lib.Task;
import org.tuurneckebroeck.pdfutil.task.lib.TaskCallbackHandler;
import org.tuurneckebroeck.pdfutil.util.FileUtil;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class PasswordDisableTask extends Task {

    public PasswordDisableTask(File inputFile, String password, TaskCallbackHandler callbackHandler) {
        super(callbackHandler);
        this.inputFile = inputFile;
        this.password = password;
    }

    @Override
    public void run() {
        setStatus(TaskStatus.EXECUTING);
        getLogger().log(LogLevel.DEBUG, getClass(), "Status: " + getStatus());

        try {
            PDDocument doc = PDDocument.load(inputFile, password);
            doc.setAllSecurityToBeRemoved(true);

            File newFile = FileUtil.addToFileName(inputFile, "_decrypted");
            doc.save(newFile);
            doc.close();

            setStatus(TaskStatus.FINISHED);
            getLogger().log(LogLevel.DEBUG, getClass(),
                    String.format("Password successfully disabled, output file: '%s'.", newFile.getAbsolutePath()));
        } catch (InvalidPasswordException e) {
            setStatus(TaskStatus.FAILED);
            getLogger().log(LogLevel.WARNING, getClass(),
                    String.format("Invalid password supplied for file '%s'.", inputFile.getAbsolutePath()));
        } catch (IOException e) {
            setStatus(TaskStatus.FAILED);
            getLogger().log(LogLevel.ERROR, getClass(),
                    String.format("Exception occurred when trying to disable password protection (%s)", inputFile.getAbsolutePath()));
        }

        getLogger().log(LogLevel.ERROR, getClass(), String.format("Status: %s %s performing callback to: %s", getStatus(), "          ",getCallbackHandler().getClass().getSimpleName()));
        callback();
    }

    public static boolean isPasswordProtected(File file) throws IOException {
        try {
            PDDocument.load(file).close();
        } catch (InvalidPasswordException e) {
            return true;
        }

        return false;
    }

    private File inputFile;
    private String password;
}
