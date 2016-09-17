package ee.smkv.scheduler;

import ee.smkv.scheduler.utils.DataSourceBuilder;
import org.springframework.beans.factory.annotation.Qualifier;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import javax.sql.DataSource;

@ComponentScan
public class Application {

    public static final String SCHEDULER_DATA_SOURCE = "schedulerDataSource";
    public static final String TARGET_DATA_SOURCE = "targetDataSource";
    public static final long CHECK_TASK_RATE = Config.getLongProperty("task.check.rate" , 5000L);

    @Bean(name = SCHEDULER_DATA_SOURCE)
    @Qualifier(SCHEDULER_DATA_SOURCE)
    DataSource createSchedulerDataSource() {
        return DataSourceBuilder.createBasicDataSourceFromConfig("scheduler");
    }

    @Bean(name = TARGET_DATA_SOURCE)
    @Qualifier(TARGET_DATA_SOURCE)
    DataSource createTargetDataSource() {
        return DataSourceBuilder.createBasicDataSourceFromConfig("task");
    }


}
