package ee.smkv.scheduler.executors;

import ee.smkv.scheduler.model.Task;
import org.apache.log4j.Logger;
import org.apache.log4j.MDC;
import org.apache.log4j.NDC;

import java.util.Date;

public abstract class TaskExecutor implements Runnable {
    final Logger logger = Logger.getLogger(getClass());
    final Long executionId;
    final Task task;
    TaskExecutionListener listener;

    TaskExecutor(Long executionId, Task task) {
        this.executionId = executionId;
        this.task = task;
    }

    @Override
    public void run() {
        try {
            NDC.push("TASK #" + task.getId());
            logger.info("Started");
            executeCommand(task.getCommand());
            done();
        } catch (Exception e) {
            handleError(e);
        }finally {
            NDC.pop();
        }
    }

    private void done() {
        logger.info("Done");
        listener.onFinish(this);
    }

    private void handleError(Exception e) {
        logger.error(e.getMessage() ,e);
        listener.onError(this, e);
    }

    protected abstract void executeCommand(String command) throws Exception;

    void output(String output) {
        logger.info(output);
        listener.onOutput(this, output);
    }

    public Long getExecutionId() {
        return executionId;
    }

    public void setListener(TaskExecutionListener listener) {
        this.listener = listener;
    }

}
