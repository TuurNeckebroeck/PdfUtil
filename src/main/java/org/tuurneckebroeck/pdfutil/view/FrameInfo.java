package org.tuurneckebroeck.pdfutil.view;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;

/**
 *
 * @author Tuur Neckebroeck
 */
public class FrameInfo extends javax.swing.JFrame {

    private File file;

    /**
     * Creates new form FrameInfor
     */
    public FrameInfo() {
        initComponents();
    }

    public FrameInfo(File f) {
        try {
            initComponents();
            this.file = f;
            lblFile.setText(file.getName());
            InformationFieldCollection ifc = new InformationFieldCollection();
            ifc.addField("Full path", file.getAbsolutePath());
            PDDocument document = null;
            try {
                document = PDDocument.load(file);
                PDDocumentInformation pdd = document.getDocumentInformation();
                ifc.addField("Author", pdd.getAuthor());
                ifc.addField("Creator", pdd.getCreator());
                ifc.addField("Creator", pdd.getCreator());
                ifc.addField("Title", pdd.getTitle());
                ifc.addField("Subject", pdd.getSubject());
                ifc.addField("Number of pages", String.valueOf(document.getNumberOfPages()));
            } catch (InvalidPasswordException e) {
            }
            ifc.addField("Size (bytes)", String.valueOf(file.length()));
            lblInfo.setText(ifc.getText());
            if(document != null)document.close();
        } catch (IOException ex) {
            Logger.getLogger(FrameInfo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        lblFile = new javax.swing.JLabel();
        lblInfo = new javax.swing.JLabel();

        jLabel1.setText("jLabel1");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        lblFile.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        lblFile.setText("lblFile");

        lblInfo.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblInfo.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblInfo.setText("lblInfo");
        lblInfo.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        lblInfo.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(lblInfo, javax.swing.GroupLayout.DEFAULT_SIZE, 534, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblFile)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblFile)
                .addGap(18, 18, 18)
                .addComponent(lblInfo, javax.swing.GroupLayout.PREFERRED_SIZE, 352, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel lblFile;
    private javax.swing.JLabel lblInfo;
    // End of variables declaration//GEN-END:variables

    private class InformationFieldCollection {

        private Map<String, String> map = new LinkedHashMap<>();

        public void addField(String field, String value) {
            map.put(field, value);
        }

        public String getText() {
            StringBuilder sb = new StringBuilder();
            sb.append("<html>");
            for (Map.Entry<String, String> kp : map.entrySet()) {
                sb.append("<b>");
                sb.append(kp.getKey());
                sb.append("</b>:&#09;");
                sb.append(kp.getValue() == null ? "-" : kp.getValue());
                sb.append("<br>");
            }
            sb.append("</html>");

            return sb.toString();
        }
    }
}
