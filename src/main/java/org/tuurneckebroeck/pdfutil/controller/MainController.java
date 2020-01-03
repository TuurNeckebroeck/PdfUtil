package org.tuurneckebroeck.pdfutil.controller;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;
import org.tuurneckebroeck.pdfutil.Constant;
import org.tuurneckebroeck.pdfutil.log.NullLogger;
import org.tuurneckebroeck.pdfutil.log.LogLevel;
import org.tuurneckebroeck.pdfutil.log.VerbosityLogger;
import org.tuurneckebroeck.pdfutil.model.FileListElement;
import org.tuurneckebroeck.pdfutil.model.FileType;
import org.tuurneckebroeck.pdfutil.util.FileUtil;
import org.tuurneckebroeck.pdfutil.model.FileList;
import org.tuurneckebroeck.pdfutil.task.MergeTask;
import org.tuurneckebroeck.pdfutil.task.WatermarkTask;
import org.tuurneckebroeck.pdfutil.task.lib.Task;
import org.tuurneckebroeck.pdfutil.view.FrameInfo;
import org.tuurneckebroeck.pdfutil.view.FrameSplit;
import org.tuurneckebroeck.pdfutil.view.main.MainView;
import org.tuurneckebroeck.pdfutil.view.main.NullMainView;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Tuur Neckebroeck
 */
public class MainController {

    private MainView view = new NullMainView();
    private FileList fileList;
    private VerbosityLogger logger = new NullLogger();
    private final String LOCK_SYMBOL = "\uD83D\uDD12";

    public MainController(MainView view, FileList fileList) {
        this.view = view;
        view.setController(this);
        this.fileList = fileList;
        this.logger = Constant.getInstance().getLogger();
    }

    public void showMainView() {
        view.showView();
    }

    public void addToWorkspace(File[] files) {
        for (File file : files) {
            addFileToFileList(file);
        }

        view.setFileListModel(fileList.getDefaultListModel());
    }

    public void addToWorkspace(File file) {
        addFileToFileList(file);

        view.setFileListModel(fileList.getDefaultListModel());
    }

    private void addFileToFileList(File file) {
        FileType type = FileUtil.getFileType(file);
        if (type.isPdf()) {
            FileListElement fileListElement = new FileListElement(file);
            if (type == FileType.PDF_ENCRYPTED) {
                fileListElement.appendToDisplayText(String.format(" %s ", LOCK_SYMBOL));
            }
            fileList.add(fileListElement);
        }
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

    public void deleteFromWorkspace(int[] indices) {
        Arrays.sort(indices);
        for (int i = indices.length - 1; i >= 0; i--) {
            fileList.remove(indices[i]);
        }
        view.setFileListModel(fileList.getDefaultListModel());
    }

    public void mergePdfs(int[] indices) {
        mergePdfs(fileList.indicesToFiles(indices));
    }

    public void mergePdfs(File[] files) {
        File destFile = FileUtil.addToFileName(files[0], "_merged");

        Task mergeTask = new MergeTask(files, destFile, status -> {
                view.setWaiting(false);
                if(status == Task.TaskStatus.FINISHED) {
                    view.showMessage("File saved as " + destFile);
                    fileList.clear();
                    view.setFileListModel(fileList.getDefaultListModel());
                } else if (status == Task.TaskStatus.FAILED) {
                    // DESIGN mogelijkheid voorzien om bv. errormessage mee te geven.
                    logger.log(LogLevel.ERROR, getClass(), "MergeTask returned FAILED task status on callback.");
                    view.showMessage("Merge failed.");
                }
            });

        new Thread(mergeTask).start();
        view.setWaiting(true);
    }

    public void splitPdf(int index) {
        SplitController splitController = new SplitController(new FrameSplit(), fileList.get(index).getFile());
        splitController.setLogger(logger);
        splitController.showSplitView();
    }

    public void watermarkPdf(File[] files) {

        // TODO outputfile selecteren, watermerk bestand selecteren.
        String destFile = "/home/tuur/Desktop/watermerked.pdf";
        logger.log(LogLevel.DEBUG, getClass(), "Watermark task starting...");
        WatermarkTask watermarkTask = new WatermarkTask(files[0],
                new File("/home/tuur/Desktop/watermerk.pdf"),
                new File(destFile),
                status ->  {
                        view.setWaiting(false);
                        if(status == Task.TaskStatus.FINISHED) {
                            view.showMessage("File saved as " + destFile);
                            logger.log(LogLevel.DEBUG, getClass(), "Watermark task finished");
                        }else{
                            logger.log(LogLevel.ERROR, getClass(), "WatermarkTask returned FAILED task status on callback.");
                            view.showMessage("Watermark failed.");
                        }
                    });

        new Thread(watermarkTask).start();
        view.setWaiting(true);
    }

    public void addPasswordProtection(int[] indices, String password) {
        addPasswordProtection(fileList.indicesToFiles(indices), password);
    }

    // TODO REFACTOR
    public void addPasswordProtection(File[] files, String password) {
        List<File> encryptedFiles = new ArrayList<>();
        int nbSelectedFiles = files.length;

        try {

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
            e.printStackTrace();
            logger.log(LogLevel.ERROR, getClass(), "Not all files could be encrypted:\n" + FileUtil.getExceptionStackTrace(e));
            view.showMessage("An error occured, not all files have been encrypted.");
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
            view.showMessage(sb.toString());
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
                                view.showMessage(file.getAbsoluteFile() + "\nis not password protected. Proceeding to next file.");
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
                        view.showMessage("Invalid password.");
                    }

                } while (tries > 0);

                if (!correctPassword) {
                    view.showMessage("3 failed password attempts. Proceeding to next file.");
                    continue outer;
                }

                doc.setAllSecurityToBeRemoved(true);
                File newFile = FileUtil.addToFileName(file, "_decrypted");
                doc.save(newFile);
                doc.close();
                view.showMessage("Password stripped file saved as\n"+newFile.getAbsolutePath());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void showInfoFrame(int index) {
        FrameInfo fi = new FrameInfo(fileList.get(index).getFile());
        fi.setVisible(true);
    }

}
