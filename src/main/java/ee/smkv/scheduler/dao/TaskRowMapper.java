package ee.smkv.scheduler.dao;

import ee.smkv.scheduler.model.Task;
import ee.smkv.scheduler.model.Type;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TaskRowMapper implements RowMapper<Task> {
    @Override
    public Task mapRow(ResultSet rs, int rowNum) throws SQLException {

        long id = rs.getLong("ID");
        Type type = Type.valueOf(rs.getString("TYPE"));
        String command = rs.getString("COMMAND");

        return new Task(id, type, command);
    }
}
