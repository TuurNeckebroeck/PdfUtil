package org.tuurneckebroeck.pdfutil.task;

import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSString;
import org.apache.pdfbox.pdfparser.PDFStreamParser;
import org.apache.pdfbox.pdfwriter.ContentStreamWriter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.pdmodel.common.PDStream;
import org.tuurneckebroeck.pdfutil.log.LogLevel;
import org.tuurneckebroeck.pdfutil.log.NullLogger;
import org.tuurneckebroeck.pdfutil.log.VerbosityLogger;
import org.tuurneckebroeck.pdfutil.task.lib.Task;
import org.tuurneckebroeck.pdfutil.task.lib.TaskCallbackHandler;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Deprecated
public class RemoveWatermarkTask extends Task {

    public RemoveWatermarkTask(File inputFile, File outputFile, TaskCallbackHandler parent) {
        super(parent);
        this.inputFile = inputFile;
        this.outputFile = outputFile;
    }

    @Override
    public void run() {
        setStatus(TaskStatus.EXECUTING);
        getLogger().log(LogLevel.DEBUG, getClass(), "Started RemoveWatermarkTask, status = " + getStatus().toString());
        try {
            PDDocument inputDoc = PDDocument.load(inputFile);
            searchReplace("WATERMERK", "", "", true, inputDoc);
            inputDoc.save(outputFile);
            inputDoc.close();
            setStatus(TaskStatus.FINISHED);
        } catch (IOException e) {
            setStatus(TaskStatus.FAILED);
            e.printStackTrace();
        }

        getLogger().log(LogLevel.DEBUG, getClass(), "Before callback,  status = " + getStatus().toString());
        callback();
    }

    /**
     *
     * Source: http://ulfdittmer.com/view?PdfboxReplace
     *
     * @param search
     * @param replace
     * @param encoding
     * @param replaceAll
     * @param doc
     * @throws IOException
     */
    private void searchReplace (String search, String replace,
                                       String encoding, boolean replaceAll, PDDocument doc) throws IOException {
        PDPageTree pages = doc.getDocumentCatalog().getPages();
        List<Integer> remove = new ArrayList<>(); // CADD

        for (PDPage page : pages) {
            PDFStreamParser parser = new PDFStreamParser(page);
            parser.parse();
            List tokens = parser.getTokens();
            for (int j = 0; j < tokens.size(); j++) {
                Object next = tokens.get(j);
                if (next instanceof Operator) {
                    Operator op = (Operator) next;
                    // Tj and TJ are the two operators that display strings in a PDF
                    // Tj takes one operator and that is the string to display so lets update that operator

                    if (op.getName().equals("Tj")) {
                        COSString previous = (COSString) tokens.get(j-1);
                        String string = previous.getString();
                        getLogger().log(LogLevel.DEBUG, getClass(), "Tj string: " + string);
                        if (replaceAll)
                            string = string.replaceAll(search, replace);
                        else
                            string = string.replaceFirst(search, replace);
                        previous.setValue(string.getBytes());
                    } else if (op.getName().equals("TJ")) {
                        COSArray previous = (COSArray) tokens.get(j-1);
                        for (int k = 0; k < previous.size(); k++) {
                            Object arrElement = previous.getObject(k);
                            if (arrElement instanceof COSString) {
                                COSString cosString = (COSString) arrElement;
                                String string = cosString.getString();
                                getLogger().log(LogLevel.DEBUG, getClass(), "TJ string: " + string);

                                if (replaceAll)
                                    string = string.replaceAll(search, replace);
                                else
                                    string = string.replaceFirst(search, replace);
                                cosString.setValue(string.getBytes());
                            }
                        }
                    }
                }
            }
            // now that the tokens are updated we will replace the page content stream.
            PDStream updatedStream = new PDStream(doc);
            OutputStream out = updatedStream.createOutputStream();
            ContentStreamWriter tokenWriter = new ContentStreamWriter(out);
            tokenWriter.writeTokens(tokens);
            out.close();
            page.setContents(updatedStream);
        }
    }

//CADD
    // https://github.com/thebabush/pdf-strip-watermark/blob/master/src/main/java/it/fuck/kenoph/Main.java
    static int removeState(List<Object> tokens, List<Integer> remove, int i) {
        remove.add(i);
        for (int j=i-1; j >= 0; j--) {
            Object token = tokens.get(j);
            remove.add(j);
            if (token instanceof Operator) {
                if (((Operator) token).getName().equals("q")) {
//                    System.out.println("q");
                    break;
                }
            }
        }
        for (int j=i+1; j < tokens.size(); j++) {
            i = j;
            Object token = tokens.get(j);
            remove.add(j);
            if (token instanceof Operator) {
                if (((Operator) token).getName().equals("Q")) {
//                    System.out.println("Q");
                    break;
                }
            }
        }

        return i + 1;
    }

    private File inputFile, outputFile;
}
