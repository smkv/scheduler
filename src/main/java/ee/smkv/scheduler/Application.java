package ee.smkv.scheduler;

import ee.smkv.scheduler.utils.DataSourceBuilder;
import org.springframework.beans.factory.annotation.Qualifier;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import javax.sql.DataSource;

@ComponentScan
public class Application {

    public static final String SCHEDULER_DATA_SOURCE = "schedulerDataSource";
    public static final String TARGET_DATA_SOURCE = "targetDataSource";
    public static final String MACHINE_NAME_CONFIG_PARAMETER = "machine.name";

    @Bean
    @Qualifier(SCHEDULER_DATA_SOURCE)
    DataSource createSchedulerDataSource() {
        return DataSourceBuilder.createBasicDataSourceFromConfig("scheduler");
    }

    @Bean
    @Qualifier(TARGET_DATA_SOURCE)
    DataSource createTargetDataSource() {
        return DataSourceBuilder.createBasicDataSourceFromConfig("target");
    }

    @Bean
    TaskScheduler createTaskScheduler(){
        return new ThreadPoolTaskScheduler();
    }

}
