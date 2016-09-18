package ee.smkv.scheduler;

import ee.smkv.scheduler.dao.SchedulerDao;
import ee.smkv.scheduler.executors.TaskExecutionListener;
import ee.smkv.scheduler.executors.TaskExecutor;
import ee.smkv.scheduler.executors.TaskExecutorFactory;
import ee.smkv.scheduler.model.Task;
import ee.smkv.scheduler.utils.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@EnableScheduling
@Component
public class Scheduler implements TaskExecutionListener {

    ExecutorService executorService = Executors.newCachedThreadPool();

    @Autowired
    TaskExecutorFactory taskExecutorFactory;

    @Autowired
    SchedulerDao schedulerDao;


    @Scheduled(fixedRate = 5000)
    public void checkForNewTasks() {
        System.out.println("Checking for new tasks");
        List<Task> tasks = schedulerDao.getTasksForExecutingNow();
        for (Task task : tasks) {
            executeTask(task);
        }
        if (tasks.isEmpty()) {
            System.out.println("There are no tasks for execute now");
        }
    }

    private void executeTask(Task task) {
        Long executionId = schedulerDao.createExecutionId(task);
        TaskExecutor executorForTask = taskExecutorFactory.createExecutorForTask(executionId, task);
        executorForTask.setListener(this);
        executorService.execute(executorForTask);
    }

    @Override
    public void onFinish(TaskExecutor taskExecutor) {
        schedulerDao.executionDone(taskExecutor.getExecutionId());
    }

    @Override
    public void onError(TaskExecutor taskExecutor, Throwable throwable) {
        schedulerDao.executionFailed(taskExecutor.getExecutionId(), ExceptionUtils.getStackTrace(throwable));
    }

    @Override
    public void onOutput(TaskExecutor taskExecutor, String output) {
        schedulerDao.appendExecutionOutput(taskExecutor.getExecutionId(), output);
    }
}
