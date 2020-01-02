package org.tuurneckebroeck.pdfutil.task.lib;

/**
 * @author Tuur Neckebroeck
 */
@FunctionalInterface
public interface TaskCallbackHandler {

    void onCallback(Task.TaskStatus status);

}
