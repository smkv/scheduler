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

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.Types;
import java.util.List;

import static ee.smkv.scheduler.Application.MACHINE_NAME_CONFIG_PARAMETER;

@Component
public class SchedulerDao {

    @Autowired
    @Qualifier(Application.SCHEDULER_DATA_SOURCE)
    DataSource dataSource;
    String machineName = Config.getProperty(MACHINE_NAME_CONFIG_PARAMETER);

    SimpleJdbcCall getNextTaskToExecuteProcedure;
    SimpleJdbcCall createExecutionProcedure;
    SimpleJdbcCall executionDoneProcedure;
    SimpleJdbcCall executionFailedProcedure;
    SimpleJdbcCall appendExecutingLogProcedure;

    @PostConstruct
    void init() {
        getNextTaskToExecuteProcedure = createSimpleJdbcCall()
                .withProcedureName("get_next_task_to_execute")
                .returningResultSet("o_result", new TaskRowMapper())
                .declareParameters(new SqlParameter("i_machine", Types.VARCHAR));

        createExecutionProcedure = createSimpleJdbcCall()
                .withProcedureName("create_execution")
                .declareParameters(new SqlParameter("i_task_id", Types.INTEGER))
                .declareParameters(new SqlOutParameter("o_execution_id", Types.INTEGER));

        executionDoneProcedure = createSimpleJdbcCall()
                .withProcedureName("execution_done")
                .declareParameters(new SqlParameter("i_execution_id", Types.INTEGER));

        executionFailedProcedure = createSimpleJdbcCall()
                .withProcedureName("execution_failed")
                .declareParameters(new SqlParameter("i_execution_id", Types.INTEGER))
                .declareParameters(new SqlParameter("i_message", Types.VARCHAR));

        appendExecutingLogProcedure = createSimpleJdbcCall()
                .withProcedureName("append_executing_log")
                .declareParameters(new SqlParameter("i_execution_id", Types.INTEGER))
                .declareParameters(new SqlParameter("i_log_record", Types.VARCHAR));

        getNextTaskToExecuteProcedure.compile();
        createExecutionProcedure.compile();
        executionDoneProcedure.compile();
        executionFailedProcedure.compile();
        appendExecutingLogProcedure.compile();
    }


    public List<Task> getTasksForExecutingNow() {
        return (List<Task>) getNextTaskToExecuteProcedure.execute(machineName).get("o_result");
    }

    private SimpleJdbcCall createSimpleJdbcCall() {
        return new SimpleJdbcCall(dataSource).withoutProcedureColumnMetaDataAccess();
    }

    public Long createExecutionId(Task task) {
        return ((Number) createExecutionProcedure.execute(task.getId()).get("o_execution_id")).longValue();
    }

    public void executionDone(Long executionId) {
        executionDoneProcedure.execute(executionId);
    }

    public void executionFailed(Long executionId, String message) {
        executionFailedProcedure.execute(executionId, message);
    }

    public void appendExecutionOutput(Long executionId, String output) {
        appendExecutingLogProcedure.execute(executionId, output);
    }

}
