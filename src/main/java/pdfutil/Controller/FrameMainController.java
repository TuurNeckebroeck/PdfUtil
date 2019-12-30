package pdfutil.Controller;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;
import pdfutil.FileUtil;
import pdfutil.Model.FileList;
import pdfutil.Model.FileListElement;
import pdfutil.Model.FileType;
import pdfutil.Task.MergeTask;
import pdfutil.Task.Task;
import pdfutil.Task.CallbackHandler;
import pdfutil.View.FrameInfo;
import pdfutil.View.FrameMain;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FrameMainController {

    private FrameMain view;
    private FileList fileList;
    private final String LOCK_SYMBOL = "\uD83D\uDD12";

    public FrameMainController(FrameMain view, FileList fileList) {
        this.view = view;
        view.setController(this);
        this.fileList = fileList;
    }

    public void addDroppedFiles(File[] files) {
        for (File file : files) {
            FileType type = FileUtil.getFileType(file);
            if (type.isPdf()) {
                FileListElement fileListElement = new FileListElement(file);
                if (type == FileType.PDF_ENCRYPTED) {
                    fileListElement.appendToDisplayText(String.format(" %s ", LOCK_SYMBOL));
                }
                fileList.add(fileListElement);
            }

        }

        view.setFileListModel(fileList.getDefaultListModel());
    }

    public void moveUpElement(int index) {
        fileList.moveUp(index);
        view.setFileListModel(fileList.getDefaultListModel());
        view.setSelectedFileIndex(index-1);
    }

    public void moveDownElement(int index) {
        fileList.moveDown(index);
        view.setFileListModel(fileList.getDefaultListModel());
        view.setSelectedFileIndex(index + 1);
    }

    public void deleteElementsFromGui(int[] indices) {
        Arrays.sort(indices);
        for (int i = indices.length - 1; i >= 0; i--) {
            fileList.remove(indices[i]);
        }
        view.setFileListModel(fileList.getDefaultListModel());
    }

    public void mergePdfs(int[] indices) {
        File[] files = indicesToFiles(indices);
        File destFile = FileUtil.addToFileName(files[0], "_merged");

        Task mergeTask = new MergeTask(files, destFile, new CallbackHandler() {
            @Override
            public void onCallback(Task.TaskStatus status) {
                view.getContentPane().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                if(status == Task.TaskStatus.FINISHED) {
                    JOptionPane.showMessageDialog(view, "File saved as " + destFile);
                    fileList.clear();
                    view.setFileListModel(fileList.getDefaultListModel());
                } else if (status == Task.TaskStatus.FAILED) {
                    // TODO mogelijkheid voorzien om bv. errormessage mee te geven.
                    JOptionPane.showMessageDialog(view, "Merge failed.");
                }
            }
        });

        Thread pdfMergeThread = new Thread(mergeTask);
        pdfMergeThread.start();
        view.getContentPane().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    }

    // TODO REFACTOR
    public void addPasswordProtection(int[] indices, String password) {
        List<File> encryptedFiles = new ArrayList<>();
        int nbSelectedFiles = indices.length;

        try {
            File files[] = new File[indices.length];
            for (int i = 0; i < indices.length; i++) {
                files[i] = fileList.get(indices[i]).getFile();
            }

            for (File file : files) {
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

                File newFile = FileUtil.addToFileName(file, "_encrypted");
                doc.save(newFile);
                doc.close();
                encryptedFiles.add(newFile);
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(view, "An error occured, not all files have been encrypted.");
        } finally {
            StringBuilder sb = new StringBuilder();
            if (nbSelectedFiles == encryptedFiles.size()) {
                sb.append("All of the files have been encrypted:");
            } else {
                sb.append(encryptedFiles.size());
                sb.append(" of the ");
                sb.append(nbSelectedFiles);
                sb.append(" selected files have been encrypted:");
            }

            for (File f : encryptedFiles) {
                sb.append("\n");
                sb.append(f.getAbsolutePath());
            }
            JOptionPane.showMessageDialog(view, sb.toString());
        }
    }

    // TODO REFACTOR
    public void disablePasswordProtection(int[] indices) {
        outer:
        for (int index : indices) {
            File file = fileList.get(index).getFile();
            int tries = 3, maxTries = 3;
            try {
                PDDocument doc = null;
                boolean correctPassword = false;
                inner:
                do {
                    try {
                        if (tries == maxTries) {
                            try {
                                doc = PDDocument.load(file);
                                doc.close();
                                JOptionPane.showMessageDialog(view, file.getAbsoluteFile() + "\nis not password protected. Proceeding to next file.");
                                continue outer;
                            } catch (InvalidPasswordException e) {
                            }
                        }
                        String password = JOptionPane.showInputDialog(view, file.getAbsolutePath() + "\n is secured with a password. Please enter it:");
                        if (password.equals("")) {
                            break outer;
                        }
                        tries--;
                        doc = PDDocument.load(file, password);

                        correctPassword = true;
                        break inner;
                    } catch (InvalidPasswordException e) {
                        JOptionPane.showMessageDialog(view, "Invalid password.");
                    }

                } while (tries > 0);

                if (!correctPassword) {
                    JOptionPane.showMessageDialog(view, "3 failed password attempts. Proceeding to next file.");
                    continue outer;
                }

                doc.setAllSecurityToBeRemoved(true);
                File newFile = FileUtil.addToFileName(file, "_decrypted");
                doc.save(newFile);
                doc.close();
                JOptionPane.showMessageDialog(view, "Password stripped file saved as\n"+newFile.getAbsolutePath());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void showInfoFrame(int index) {
        FrameInfo fi = new FrameInfo(fileList.get(index).getFile());
        fi.setVisible(true);
    }


    private File[] indicesToFiles(int[] indices) {
        File files[] = new File[indices.length];
        for (int i = 0; i < indices.length; i++) {
            files[i] = fileList.get(indices[i]).getFile();
        }
        return files;
    }

}