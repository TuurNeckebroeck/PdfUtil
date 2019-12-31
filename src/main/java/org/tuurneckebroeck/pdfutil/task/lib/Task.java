package org.tuurneckebroeck.pdfutil.task.lib;

/**
 * @author Tuur Neckebroeck
 */
public abstract class Task implements Runnable{

    public Task(TaskCallbackHandler parent) {
        this.parent = parent;
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
    private volatile TaskStatus status = TaskStatus.NOT_STARTED;

    public enum TaskStatus {
        NOT_STARTED, EXECUTING, FINISHED, FAILED
    }
}
