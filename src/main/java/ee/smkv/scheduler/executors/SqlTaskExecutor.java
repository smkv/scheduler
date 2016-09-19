package ee.smkv.scheduler.executors;


import ee.smkv.scheduler.model.Task;
import ee.smkv.scheduler.utils.ResultSetPrinter;
import ee.smkv.scheduler.utils.StringUtils;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.StringWriter;
import java.sql.*;

public class SqlTaskExecutor extends TaskExecutor {
    final DataSource dataSource;
    public SqlTaskExecutor(Long executionId , Task task, DataSource dataSource) {
        super(executionId, task);
        this.dataSource = dataSource;
    }

    @Override
    protected void executeCommand(String command) throws SQLException, IOException {
        try (Connection connection = dataSource.getConnection()) {
            try (Statement statement = connection.createStatement()) {
                logger.info(command);
                boolean hasResults = statement.execute(command);
                SQLWarning warnings = statement.getWarnings();
                if (warnings != null) {
                    for (Throwable throwable : warnings) {
                        output("Warning: " + throwable.getMessage());
                    }
                }

                while (hasResults){
                    ResultSetPrinter printer = new ResultSetPrinter(statement.getResultSet());
                    StringWriter writer = new StringWriter();
                    printer.printTo(writer);
                    StringUtils.splitLines(writer.toString()).forEach(this::output);
                    hasResults = statement.getMoreResults();
                }
            }
        }
    }
}
