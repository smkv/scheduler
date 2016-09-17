package ee.smkv.scheduler.executors;

import ee.smkv.scheduler.model.Task;

public interface TaskExecutionListener {
    void onStart(Task task);

    void onFinish(Task task);

    void onError(Task task, Throwable throwable);

    void onOutput(Task task, String output);
}
