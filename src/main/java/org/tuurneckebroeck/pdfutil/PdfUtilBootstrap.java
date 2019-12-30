package org.tuurneckebroeck.pdfutil;

import jdk.nashorn.internal.runtime.logging.DebugLogger;
import org.tuurneckebroeck.pdfutil.controller.MainController;
import org.tuurneckebroeck.pdfutil.log.CompositeLogger;
import org.tuurneckebroeck.pdfutil.log.ConsoleLogger;
import org.tuurneckebroeck.pdfutil.log.FileLogger;
import org.tuurneckebroeck.pdfutil.log.NullLogger;
import org.tuurneckebroeck.pdfutil.log.lib.LogLevel;
import org.tuurneckebroeck.pdfutil.log.lib.VerbosityLogger;
import org.tuurneckebroeck.pdfutil.model.FileList;
import org.tuurneckebroeck.pdfutil.view.FrameMain;

import java.io.Console;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Tuur Neckebroeck
 */
public class PdfUtilBootstrap {

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
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                init();
            }
        });
    }

    private static void init() {
        try {
            CompositeLogger compositeLogger = new CompositeLogger();
            compositeLogger.registerLogger(new FileLogger(
                    LogLevel.ERROR,
                    new File(FileUtil.OSDetector.getDesktopPath()
                            + FileUtil.OSDetector.getPathSeparator() + "log.txt"),
                    true));
            compositeLogger.registerLogger(new ConsoleLogger(LogLevel.DEBUG));

            MainController mainController = new MainController(new FrameMain(), new FileList());
            mainController.setLogger(compositeLogger);
            mainController.showMainView();
        } catch (Exception e) {
            //Logger.getLogger("BOOTSTRAP").log(Level.SEVERE, "UNCATCHED EXCEPTION OCCURED");
            e.printStackTrace();
        }
    }
}
