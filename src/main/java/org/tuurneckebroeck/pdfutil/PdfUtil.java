package org.tuurneckebroeck.pdfutil;

import org.apache.commons.cli.*;
import org.tuurneckebroeck.pdfutil.controller.MainController;
import org.tuurneckebroeck.pdfutil.log.CompositeLogger;
import org.tuurneckebroeck.pdfutil.log.ConsoleLogger;
import org.tuurneckebroeck.pdfutil.log.FileLogger;
import org.tuurneckebroeck.pdfutil.log.LogLevel;
import org.tuurneckebroeck.pdfutil.model.FileList;
import org.tuurneckebroeck.pdfutil.util.FileUtil;
import org.tuurneckebroeck.pdfutil.view.main.ConsoleMain;
import org.tuurneckebroeck.pdfutil.view.main.FrameMain;


import java.awt.*;
import java.io.File;

/**
 * Main class
 *
 * @author Tuur Neckebroeck
 */
public class PdfUtil {

    public static void runApplication(String[] args) {
        CommandLine line = parseArguments(args);
        MainController mainController = null;

        if(line.hasOption("interface")) {
            CompositeLogger logger = new CompositeLogger();
            try {
                logger.registerLogger(new FileLogger(
                        LogLevel.DEBUG,
                        new File(FileUtil.OSDetector.getDesktopPath()
                                + FileUtil.OSDetector.getPathSeparator() + "console_log.txt"),
                        true));

                switch(line.getOptionValue("interface").toLowerCase()) {
                    case "gui":
                        logger.registerLogger(new ConsoleLogger(LogLevel.DEBUG));
                        mainController = new MainController(new FrameMain(), new FileList());
                        break;
                    case "cli":
                        mainController = new MainController(new ConsoleMain(), new FileList());
                        break;
                }
                mainController.setLogger(logger);
                mainController.showMainView();
                return;
            }catch (Exception e) {
                e.printStackTrace();
            }
        }


        if (line.hasOption("input")) {
            System.out.println(line.getOptionValue("input"));
            String fileName = line.getOptionValue("filename");


            // TODO implement
        } else {
            //printAppHelp();
        }
    }

    public static void main(String[] args) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FrameMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrameMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrameMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrameMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> runApplication(args));
    }

    public static Options getOptions() {
        Options options = new Options();
        options.addOption("interface", "interface", true, "Interface to use");

        options.addOption("i", "input", true, "Input file");
        options.addOption("o", "output", true, "Output file");
        options.addOption("a", "action", true, "Define the action on the input file");
        return options;
    }

    private static CommandLine parseArguments(String[] args) {
        Options options = getOptions();
        CommandLine line = null;
        CommandLineParser parser = new DefaultParser();

        try {
            line = parser.parse(options, args);
        } catch (ParseException ex) {
            System.err.println("Failed to parse command line arguments");
            System.err.println(ex.toString());
            //printAppHelp();
            System.exit(1);
        }

        return line;
    }

}
