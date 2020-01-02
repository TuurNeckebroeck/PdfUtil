package org.tuurneckebroeck.pdfutil.log;

public enum LogLevel {
    NONE, FATAL, ERROR, WARNING, DEBUG;
    // the order is important for the compareTo function used in the VerbosityLogger
}
