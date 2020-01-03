package org.tuurneckebroeck.pdfutil.task;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;
import org.tuurneckebroeck.pdfutil.log.LogLevel;
import org.tuurneckebroeck.pdfutil.task.lib.TaskCallbackHandler;
import org.tuurneckebroeck.pdfutil.task.lib.Task;
import org.tuurneckebroeck.pdfutil.util.FileUtil;

import java.io.File;
import java.io.IOException;

/**
 *
 * @author Tuur Neckebroeck
 */
public class PasswordProtectTask extends Task {

    public PasswordProtectTask(File[] files, String password, TaskCallbackHandler parent) {
        super(parent);
        this.files = files;
        this.password = password;
    }

    @Override
    public void run() {
        setStatus(TaskStatus.EXECUTING);
        getLogger().log(LogLevel.DEBUG, getClass(), "Status: " + getStatus());

        int nbOfSuccessfulEncrypts = 0;

        for (File file : files) {
            try {
                PDDocument doc = PDDocument.load(file);

                int keyLength = 128;
                AccessPermission ap = new AccessPermission();
                ap.setCanAssembleDocument(false);

                // owner password = user password
                StandardProtectionPolicy spp = new StandardProtectionPolicy(password, password, ap);
                spp.setEncryptionKeyLength(keyLength);
                spp.setPermissions(ap);
                doc.protect(spp);

                File newFile = FileUtil.addToFileName(file, "_encrypted");
                doc.save(newFile);
                doc.close();
                nbOfSuccessfulEncrypts++;
                getLogger().log(LogLevel.DEBUG, getClass(), String.format("Successfully encrypted '%s'", newFile.getAbsolutePath()));
            }catch (IOException e) {
                getLogger().log(LogLevel.ERROR, getClass(),
                        String.format("Error when trying to encrypt '%s':\n\t\t%s", file.getAbsolutePath(), e.getMessage()));
            }

            if(nbOfSuccessfulEncrypts == 0){
                setStatus(TaskStatus.FAILED);
            } else if (nbOfSuccessfulEncrypts < files.length) {
                setStatus(TaskStatus.PARTIALLY_FAILED);
            } else {
                setStatus(TaskStatus.FINISHED);
            }

            getLogger().log(LogLevel.DEBUG, getClass(), String.format("Status: %s %s performing callback to: %s", getStatus(), "          ",getCallbackHandler().getClass().getSimpleName()));
            callback();
        }
    }

    private File[] files;
    private String password;
}
