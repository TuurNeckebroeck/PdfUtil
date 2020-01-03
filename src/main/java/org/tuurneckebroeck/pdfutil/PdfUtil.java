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

import java.io.File;

/**
 * Main class
 *
 * @author Tuur Neckebroeck
 */
public class PdfUtil {

    // CLI interactive shell proposition:
    // https://search.maven.org/artifact/org.beryx/text-io/3.4.0/jar

    public static void runApplication(String[] args) {
        CommandLine line = parseArguments(args);
        MainController mainController = null;

        try {
            CompositeLogger logger = new CompositeLogger();
            logger.registerLogger(new FileLogger(
                    LogLevel.DEBUG,
                    new File(Constant.getLogPath() + Constant.getFileSeparator() + Constant.getTimeStampedFileName("consolelog", "txt")),
                    true));
            Constant.getInstance().setLogger(logger);


            if (line.hasOption(OPTION_INTERFACE)) {
                switch (line.getOptionValue(OPTION_INTERFACE).toLowerCase()) {
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

            }


            if (line.hasOption(OPTION_INPUT)) {
                System.out.println(line.getOptionValue(OPTION_INPUT));
                String fileName = line.getOptionValue("filename");


                // TODO implement
            } else {
                //printAppHelp();
            }

        } catch (Exception e) {
            System.err.println("UNCAUGHT EXCEPTION");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        setLookAndFeel();

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> runApplication(args));
    }

    private static void setLookAndFeel(){
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
    }

    private static CommandLine parseArguments(String[] args) {
        CommandLine line = null;
        CommandLineParser parser = new DefaultParser();

        try {
            line = parser.parse(CLI_OPTIONS, args);
        } catch (ParseException ex) {
            System.err.println("Failed to parse command line arguments");
            System.err.println(ex.toString());
            //printAppHelp();
            System.exit(1);
        }

        return line;
    }

    private final static String OPTION_INTERFACE = "interface",
                                OPTION_INPUT = "input",
                                OPTION_OUTPUT = "output",
                                OPTION_ACTION = "action";

    private final static Options CLI_OPTIONS = new Options()
            .addOption("interface", OPTION_INTERFACE, true, "Interface to use")
            .addOption("i", OPTION_INPUT, true, "Input file")
            .addOption("o", OPTION_OUTPUT, true, "Output file")
            .addOption("a", OPTION_ACTION, true, "Define the action on the input file");

}
