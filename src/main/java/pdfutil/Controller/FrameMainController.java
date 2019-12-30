package pdfutil.Controller;

import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;
import pdfutil.FileUtil;
import pdfutil.Model.FileList;
import pdfutil.Model.FileListElement;
import pdfutil.Model.FileType;
import pdfutil.View.FrameMain;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
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

    public void deleteElements(int[] indices) {
        Arrays.sort(indices);
        for (int i = indices.length - 1; i >= 0; i--) {
            fileList.remove(indices[i]);
        }
        view.setFileListModel(fileList.getDefaultListModel());
    }

    public void mergePdfs(int[] indices) {
        File[] files = indicesToFiles(indices);

        PDFMergerUtility PDFMerger = new PDFMergerUtility();
        File destFile = FileUtil.addToFileName(files[0], "_merged");

        PDFMerger.setDestinationFileName(destFile.getAbsolutePath());
        List<PDDocument> docs = new ArrayList<>();

        try {
            for (File f : files) {
                PDDocument p = PDDocument.load(f);
                docs.add(p);
                PDFMerger.addSource(f);
            }

            PDFMerger.mergeDocuments();

            for (PDDocument p : docs) {
                p.close();
            }

            JOptionPane.showMessageDialog(view, "File saved as " + destFile);
            fileList.clear();
            view.setFileListModel(fileList.getDefaultListModel());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private File[] indicesToFiles(int[] indices) {
        File files[] = new File[indices.length];
        for (int i = 0; i < indices.length; i++) {
            files[i] = fileList.get(indices[i]).getFile();
        }
        return files;
    }
}
