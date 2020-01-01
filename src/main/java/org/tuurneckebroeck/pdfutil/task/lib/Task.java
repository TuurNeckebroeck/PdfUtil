package org.tuurneckebroeck.pdfutil.task.lib;

import org.tuurneckebroeck.pdfutil.log.NullLogger;
import org.tuurneckebroeck.pdfutil.log.VerbosityLogger;

/**
 * @author Tuur Neckebroeck
 */
public abstract class Task implements Runnable {

    public Task(TaskCallbackHandler parent) {
        this.parent = parent;
    }

    public void setLogger(VerbosityLogger logger) {
        this.logger = logger;
    }

    protected VerbosityLogger getLogger() {
        return this.logger;
    }

    protected void callback(){
        if(parent == null) return;
        parent.onCallback(status);
    }

    protected void setStatus(TaskStatus status) {
        this.status = status;
    }

    public TaskStatus getStatus(){
        return this.status;
    }

    //private int taskId = -1;
    private TaskCallbackHandler parent;
    private VerbosityLogger logger = new NullLogger();
    private volatile TaskStatus status = TaskStatus.NOT_STARTED; // TODO moet dit volatile zijn?

    public enum TaskStatus {
        NOT_STARTED, EXECUTING, FINISHED, FAILED
    }
}
