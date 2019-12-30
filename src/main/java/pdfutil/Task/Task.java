package pdfutil.Task;

public abstract class Task implements Runnable{

    public Task(CallbackHandler parent) {
        this.parent = parent;
    }

    void callback(){
        if(parent == null) return;
        parent.onCallback(status);
    }

    void setStatus(TaskStatus status) {
        this.status = status;
    }

    public TaskStatus getStatus(){
        return this.status;
    }

    //private int taskId = -1;
    private CallbackHandler parent;
    private volatile TaskStatus status = TaskStatus.NOT_STARTED;

    public enum TaskStatus {
        NOT_STARTED, EXECUTING, FINISHED, FAILED
    }
}
