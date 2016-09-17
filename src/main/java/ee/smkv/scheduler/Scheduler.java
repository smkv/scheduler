package ee.smkv.scheduler;

import ee.smkv.scheduler.dao.SchedulerDao;
import ee.smkv.scheduler.executors.TaskExecutionListener;
import ee.smkv.scheduler.executors.TaskExecutor;
import ee.smkv.scheduler.executors.TaskExecutorFactory;
import ee.smkv.scheduler.model.Task;
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
            TaskExecutor executorForTask = taskExecutorFactory.createExecutorForTask(task);
            executorForTask.setListener(this);
            executorService.execute(executorForTask);
        }
        if(tasks.isEmpty()){
            System.out.println("There are no tasks for execute now");
        }
    }


    @Override
    public void onStart(Task task) {
        schedulerDao.markTaskStarted(task);
    }

    @Override
    public void onFinish(Task task) {
        schedulerDao.markTaskFinishedSuccessfully(task);
    }

    @Override
    public void onError(Task task, Throwable throwable) {
        schedulerDao.markTaskExecutionFailed(task, throwable.getMessage());
    }

    @Override
    public void onOutput(Task task, String output) {
        schedulerDao.appendExecutionOutput(task, output);
    }
}
