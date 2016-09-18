package ee.smkv.scheduler.executors;

public interface TaskExecutionListener {

    void onFinish(TaskExecutor taskExecutor);

    void onError(TaskExecutor taskExecutor, Throwable throwable);

    void onOutput(TaskExecutor taskExecutor, String output);
}
