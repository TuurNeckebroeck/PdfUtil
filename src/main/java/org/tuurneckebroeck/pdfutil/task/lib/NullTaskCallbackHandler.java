package org.tuurneckebroeck.pdfutil.task.lib;

/**
 * An implementation of TaskCallbackHandler that can be used
 * if a Task requires no callback handling.
 *
 * @author Tuur Neckebroeck
 */
public class NullTaskCallbackHandler implements TaskCallbackHandler {
    @Override
    public void onCallback(Task.TaskStatus status) {}
}
