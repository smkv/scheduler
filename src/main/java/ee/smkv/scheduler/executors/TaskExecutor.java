package ee.smkv.scheduler.executors;

import ee.smkv.scheduler.model.Task;

import java.util.Date;

public abstract class TaskExecutor implements Runnable {

    Task task;
    TaskExecutionListener listener;

    public TaskExecutor(Task task) {
        this.task = task;
    }

    @Override
    public void run() {
        try {
            markStarted();
            executeCommand(task.getCommand());
            markFinished();
        } catch (Exception e) {
            handleError(e);
        }
    }

    protected void handleError(Exception e) {
        e.printStackTrace();
        log(e.getMessage());
        listener.onError(task, e);
    }

    private void markFinished() {
        log(String.format("Finished at %1$tF %1$tT", new Date()));
        listener.onFinish(task);
    }

    private void markStarted() {
        log(String.format("Started at %1$tF %1$tT", new Date()));
        listener.onStart(task);
    }

    protected abstract void executeCommand(String command) throws Exception;

    protected void output(String output) {
        log(output);
        listener.onOutput(task, output);
    }

    public void setListener(TaskExecutionListener listener) {
        this.listener = listener;
    }

    protected void log(String message){
        System.out.printf("TASK #%d >>> %s%n" , task.getId() , message );
    }
}
