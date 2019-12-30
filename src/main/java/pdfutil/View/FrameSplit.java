package pdfutil.View;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.swing.*;
import java.io.File;

public class FrameSplit extends JFrame {

    public FrameSplit(File file) {
        this.file = file;
        initComponents();
    }

    private void initComponents() {
        throw new NotImplementedException();
    }


    private File file;
}
