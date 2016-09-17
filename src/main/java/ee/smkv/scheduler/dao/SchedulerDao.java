package ee.smkv.scheduler.dao;

import ee.smkv.scheduler.Application;
import ee.smkv.scheduler.Config;
import ee.smkv.scheduler.model.Task;
import ee.smkv.scheduler.model.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

@Component
public class SchedulerDao {
    @Autowired
    @Qualifier(Application.SCHEDULER_DATA_SOURCE)
    DataSource dataSource;
    private String machineName = Config.getProperty("machine.name");


    public List<Task> getTasksForExecutingNow() {

        return (List<Task>) createSimpleJdbcCall()
                .withProcedureName("get_next_task_to_execute")
                .returningResultSet("o_result", new TaskRowMapper())
                .declareParameters(new SqlParameter("i_machine", Types.VARCHAR))
                .execute(machineName)
                .get("o_result");
    }

    private SimpleJdbcCall createSimpleJdbcCall() {
        return new SimpleJdbcCall(dataSource).withoutProcedureColumnMetaDataAccess();
    }

    public void markTaskStarted(Task task) {
        createSimpleJdbcCall()
                .withProcedureName("set_executing_status")
                .declareParameters(new SqlParameter("i_task_id", Types.INTEGER))
                .execute(task.getId());
    }

    public void markTaskFinishedSuccessfully(Task task) {
        createSimpleJdbcCall()
                .withProcedureName("set_done_status")
                .declareParameters(new SqlParameter("i_task_id", Types.INTEGER))
                .execute(task.getId());
    }

    public void markTaskExecutionFailed(Task task, String message) {
        createSimpleJdbcCall()
                .withProcedureName("set_fail_status")
                .declareParameters(new SqlParameter("i_task_id", Types.INTEGER))
                .declareParameters(new SqlParameter("i_message", Types.VARCHAR))
                .execute(task.getId() , message);
    }

    public void appendExecutionOutput(Task task, String output) {
        createSimpleJdbcCall()
                .withProcedureName("append_executing_log")
                .declareParameters(new SqlParameter("i_task_id", Types.INTEGER))
                .declareParameters(new SqlParameter("i_log_record", Types.VARCHAR))
                .execute(task.getId() , output);
    }

    private static class TaskRowMapper implements RowMapper<Task> {
        @Override
        public Task mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Task(rs.getLong("ID"), Type.valueOf(rs.getString("TYPE")), rs.getString("COMMAND"));
        }
    }
}
