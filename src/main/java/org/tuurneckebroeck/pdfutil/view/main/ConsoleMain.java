package org.tuurneckebroeck.pdfutil.view.main;

import org.tuurneckebroeck.pdfutil.controller.MainController;

import javax.swing.*;
import java.io.File;
import java.util.Scanner;

public class ConsoleMain implements MainView {


    @Override
    public void setController(MainController controller) {
        this.controller = controller;
    }

    @Override
    public void setSelectedFileIndex(int index) {
        this.selectedFileIndex = index;
    }

    @Override
    public void showMessage(String message) {
        printMessage(message);
    }

    @Override
    public void showView() {
        printMain();
        while(running) {
            printInputRequest();
            String input = scanner.nextLine();
            if(input.equals("ws")){
                printMessage(getWorkspaceString());
            } else if(input.equals("add1")) {
                controller.addToWorkspace(new File("/home/tuur/Downloads/splitted_1.pdf"));
            } else if(input.equals("add2")) {
                controller.addToWorkspace(new File("/home/tuur/Downloads/splitted_2.pdf"));
            } else if(input.equals("mergen")) {
                controller.mergePdfs(new int[]{0,1});
            }
        }
    }

    @Override
    public void setWaiting(boolean waiting) {}

    @Override
    public void setFileListModel(ListModel<String> model) {
        //System.out.println(model.toString());
        this.workspaceFilesModel = model;
    }

    private void printMain() {
        System.out.println("-------------------------------------------------------------");
        System.out.println("|             PDF UTILITIES by TUUR NECKEBROECK             |");
        System.out.println("-------------------------------------------------------------");
    }

    private void printMessage(String message) {
        System.out.println(String.format("        <<< %s" , message));
    }

    private void printInputRequest(){
        System.out.println(">>> COMMAND:");
    }

    private String getWorkspaceString() {
        StringBuilder sBuilder = new StringBuilder();
        for(int i = 0; i < workspaceFilesModel.getSize(); i++) {
            sBuilder.append("* ");
            sBuilder.append(workspaceFilesModel.getElementAt(i));
            sBuilder.append("\n");
        }

        return sBuilder.toString().trim();
    }

    private ListModel<String> workspaceFilesModel = new DefaultListModel<>();
    private boolean running = true;
    private Scanner scanner = new Scanner(System.in);
    private int selectedFileIndex = 0;
    private MainController controller;
}
