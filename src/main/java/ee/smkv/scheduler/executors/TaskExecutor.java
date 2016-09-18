package ee.smkv.scheduler.executors;

import ee.smkv.scheduler.model.Task;

import java.util.Date;

public abstract class TaskExecutor implements Runnable {
    final Long executionId;
    final Task task;
    TaskExecutionListener listener;

    public TaskExecutor(Long executionId, Task task) {
        this.executionId = executionId;
        this.task = task;
    }

    @Override
    public void run() {
        try {
            executeCommand(task.getCommand());
            markFinished();
        } catch (Exception e) {
            handleError(e);
        }
    }

    protected void handleError(Exception e) {
        e.printStackTrace();
        log(e.getMessage());
        listener.onError(this, e);
    }

    private void markFinished() {
        log(String.format("Finished at %1$tF %1$tT", new Date()));
        listener.onFinish(this);
    }

    protected abstract void executeCommand(String command) throws Exception;

    protected void output(String output) {
        log(output);
        listener.onOutput(this, output);
    }

    public Long getExecutionId() {
        return executionId;
    }

    public void setListener(TaskExecutionListener listener) {
        this.listener = listener;
    }

    protected void log(String message){
        System.out.printf("TASK #%d >>> %s%n" , task.getId() , message );
    }
}
