package ee.smkv.scheduler.executors;

import ee.smkv.scheduler.Application;
import ee.smkv.scheduler.model.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class TaskExecutorFactory {

    @Autowired
    @Qualifier(Application.TARGET_DATA_SOURCE)
    DataSource dataSource;

    public TaskExecutor createExecutorForTask(Task task) {
        switch (task.getType()) {
            case SQL:
                return new SqlTaskExecutor(task, dataSource);
            case SHELL:
                return new ShellTaskExecutor(task);
        }
        throw new IllegalArgumentException("Unsupported  type of task: " + task.getType());
    }
}
