package org.tuurneckebroeck.pdfutil.controller;

import org.tuurneckebroeck.pdfutil.Constant;
import org.tuurneckebroeck.pdfutil.log.NullLogger;
import org.tuurneckebroeck.pdfutil.log.LogLevel;
import org.tuurneckebroeck.pdfutil.log.VerbosityLogger;
import org.tuurneckebroeck.pdfutil.model.FileListElement;
import org.tuurneckebroeck.pdfutil.model.FileType;
import org.tuurneckebroeck.pdfutil.task.PasswordDisableTask;
import org.tuurneckebroeck.pdfutil.task.PasswordProtectTask;
import org.tuurneckebroeck.pdfutil.util.FileUtil;
import org.tuurneckebroeck.pdfutil.model.FileList;
import org.tuurneckebroeck.pdfutil.task.MergeTask;
import org.tuurneckebroeck.pdfutil.task.WatermarkTask;
import org.tuurneckebroeck.pdfutil.task.lib.Task;
import org.tuurneckebroeck.pdfutil.view.FrameInfo;
import org.tuurneckebroeck.pdfutil.view.FrameSplit;
import org.tuurneckebroeck.pdfutil.view.main.MainView;
import org.tuurneckebroeck.pdfutil.view.main.NullMainView;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

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

        mergeTask.setLogger(logger);
        view.setWaiting(true);
        new Thread(mergeTask).start();
    }

    public void splitPdf(int index) {
        SplitController splitController = new SplitController(new FrameSplit(), fileList.get(index).getFile());
        splitController.setLogger(logger);
        splitController.showSplitView();
    }

    // TODO outputfile selecteren, watermerk bestand selecteren.
    public void watermarkPdf(File[] files) {
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

        view.setWaiting(true);
        new Thread(watermarkTask).start();
    }

    public void addPasswordProtection(int[] indices, String password) {
        addPasswordProtection(fileList.indicesToFiles(indices), password);
    }

    public void addPasswordProtection(File[] files, String password) {
        Task passwordProtectTask = new PasswordProtectTask(files, password,
                status -> {
                    view.setWaiting(false);
                    String message;
                    switch(status) {
                        case FAILED:
                            message = "Password protection failed for each given file.";
                            break;
                        case PARTIALLY_FAILED:
                            message = "Password protection was partially successful.";
                            break;
                        case FINISHED:
                            message = "Password protection successful.";
                            break;
                        default:
                            message = "An error occurred when trying to encrypt the given files...";
                            logger.log(LogLevel.ERROR, getClass(),
                                    String.format("Unexpected returned ResultStatus (%s) of PasswordProtectTask. Files: \n%s",
                                            status,
                                            Arrays.stream(files).map(f->f.getAbsolutePath()).reduce((f, rest) -> f + ", " + rest)));
                    }
                    view.showMessage(message);
                });
        passwordProtectTask.setLogger(logger);

        view.setWaiting(true);
        new Thread(passwordProtectTask).start();
    }

    public void disablePasswordProtection(int index, String password) {
        File inputFile = fileList.get(index).getFile();

        try {
            if(!PasswordDisableTask.isPasswordProtected(inputFile)) {
                view.showMessage(String.format("The selected file '%s' is not password protected.", inputFile.getAbsolutePath()));
                return;
            }
        } catch (IOException e) {
            logger.log(LogLevel.ERROR, getClass(),
                    String.format("Exception on check if file '%s' is password protected:\n%s", inputFile.getAbsolutePath(), e.getMessage()));
            return;
        }

        Task passwordDisableTask = new PasswordDisableTask(inputFile, password,
                status -> {
                    view.setWaiting(false);
                    String message;
                    switch (status) {
                        case FAILED:
                            message = "Failed to disable password. Try again.";
                            break;
                        case FINISHED:
                            message = "Password disabled successfully.";
                            break;
                        default:
                            message = "An error occurred when trying to decrypt the given files...";
                            logger.log(LogLevel.ERROR, getClass(),
                                    String.format("Unexpected returned ResultStatus (%s) of PasswordDisableTask on file: \n%s",
                                            status, inputFile));
                    }

                    view.showMessage(message);
                });
        passwordDisableTask.setLogger(logger);

        view.setWaiting(true);
        new Thread(passwordDisableTask).run();
    }

    public void showInfoFrame(int index) {
        FrameInfo fi = new FrameInfo(fileList.get(index).getFile());
        fi.setVisible(true);
    }

}
