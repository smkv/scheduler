package ee.smkv.scheduler.utils;

import ee.smkv.scheduler.Config;
import org.apache.commons.dbcp2.BasicDataSource;

import javax.sql.DataSource;

public class DataSourceBuilder {
    public static DataSource createBasicDataSourceFromConfig(String databaseName) {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(Config.getProperty("database." + databaseName + ".driver"));
        dataSource.setUrl(Config.getProperty("database." + databaseName + ".url"));
        dataSource.setUsername(Config.getProperty("database." + databaseName + ".username"));
        dataSource.setPassword(Config.getProperty("database." + databaseName + ".password"));
        dataSource.setDefaultAutoCommit(Boolean.valueOf(Config.getProperty("database." + databaseName + ".autocommit", "false")));
        return dataSource;

    }
}
