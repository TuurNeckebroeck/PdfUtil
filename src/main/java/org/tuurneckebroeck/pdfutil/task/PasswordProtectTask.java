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
import java.util.LinkedList;
import java.util.List;

@Deprecated
public class PasswordProtectTask extends Task {

    public PasswordProtectTask(File[] files, String password, TaskCallbackHandler parent) {
        super(parent);
        this.files = files;
        this.password = password;
    }

    @Override
    public void run() {
        setStatus(TaskStatus.EXECUTING);

        List<Object[]> encryptedDocs= new LinkedList<>();

        for (File file : files) {
            try {
                PDDocument doc = PDDocument.load(file);

                // Define the length of the encryption key.
                // Possible values are 40 or 128 (256 will be available in PDFBox 2.0).
                int keyLength = 128;

                AccessPermission ap = new AccessPermission();

                ap.setCanAssembleDocument(false);

                // Owner password (to open the file with all permissions) is "12345"
                // User password (to open the file but with restricted permissions, is empty here)
                StandardProtectionPolicy spp = new StandardProtectionPolicy(password, password, ap);
                spp.setEncryptionKeyLength(keyLength);
                spp.setPermissions(ap);
                doc.protect(spp);

                encryptedDocs.add(new Object[]{file, doc});
            }catch (IOException e) {
                // TODO message meegeven aan taskstatus
                setStatus(TaskStatus.FAILED);
                getLogger().log(LogLevel.ERROR, getClass(),
                        String.format("Error when trying to encrypt '%s':\n\t\t%s", file.getAbsolutePath(), e.getMessage()));
            }
        }

        try {
            for(Object[] fileDocTuple : encryptedDocs) {
                PDDocument doc = (PDDocument) fileDocTuple[1];
                File newFile = FileUtil.addToFileName((File) fileDocTuple[0], "_encrypted");
                doc.save(newFile);
                doc.close();
            }
        } catch (Exception e) {
            getLogger().log(LogLevel.ERROR, getClass(),
                    String.format("Error when writing encrypted doc to file:\n%s", FileUtil.getExceptionStackTrace(e)));
        }
    }

    private File[] files;
    private String password;
}
