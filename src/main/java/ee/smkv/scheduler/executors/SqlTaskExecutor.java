package ee.smkv.scheduler.executors;


import ee.smkv.scheduler.model.Task;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class SqlTaskExecutor extends TaskExecutor {
    final DataSource dataSource;
    public SqlTaskExecutor(Task task, DataSource dataSource) {
        super(task);
        this.dataSource = dataSource;
    }

    @Override
    protected void executeCommand(String command) throws SQLException, ClassNotFoundException {
        try (Connection connection = dataSource.getConnection()) {
            try (Statement statement = connection.createStatement()) {
                log(command);
                boolean success = statement.execute(command);
                for (Throwable throwable : statement.getWarnings()) {
                    output("Warning: " + throwable.getMessage());
                }
                if (!success) throw new SQLException("SQL command didn't finished successfully");
            }
        }
    }
}
