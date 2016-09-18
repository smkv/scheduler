package ee.smkv.scheduler.dao;

import ee.smkv.scheduler.Application;
import ee.smkv.scheduler.Config;
import ee.smkv.scheduler.model.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
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

    public Long createExecutionId(Task task) {
        return ((Number) createSimpleJdbcCall()
                .withProcedureName("create_execution")
                .declareParameters(new SqlParameter("i_task_id", Types.INTEGER))
                .declareParameters(new SqlOutParameter("o_execution_id", Types.INTEGER))
                .execute(task.getId())
                .get("o_execution_id"))
                .longValue();
    }

    public void executionDone(Long executionId) {
        createSimpleJdbcCall()
                .withProcedureName("execution_done")
                .declareParameters(new SqlParameter("i_execution_id", Types.INTEGER))
                .execute(executionId);
    }

    public void executionFailed(Long executionId, String message) {
        createSimpleJdbcCall()
                .withProcedureName("execution_failed")
                .declareParameters(new SqlParameter("i_execution_id", Types.INTEGER))
                .declareParameters(new SqlParameter("i_message", Types.VARCHAR))
                .execute(executionId, message);
    }

    public void appendExecutionOutput(Long executionId, String output) {
        createSimpleJdbcCall()
                .withProcedureName("append_executing_log")
                .declareParameters(new SqlParameter("i_execution_id", Types.INTEGER))
                .declareParameters(new SqlParameter("i_log_record", Types.VARCHAR))
                .execute(executionId, output);
    }

}
