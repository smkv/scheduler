package ee.smkv.scheduler.utils;

import java.io.IOException;
import java.io.Writer;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class ResultSetPrinter {

    final ResultSet resultSet;
    final ResultSetMetaData metaData;

    public ResultSetPrinter(ResultSet resultSet) throws SQLException {
        this.resultSet = resultSet;
        this.metaData = resultSet.getMetaData();
    }


    public void print(Writer writer) throws SQLException, IOException {
        printHeader(writer);
        while (resultSet.next()) {
            printDataRow(writer);
        }
    }

    private void printHeader(Writer writer) throws SQLException, IOException {
        for (int columnIndex = 1; columnIndex <= metaData.getColumnCount(); columnIndex++) {
            int columnDisplaySize = metaData.getColumnDisplaySize(columnIndex);
            String columnName = metaData.getColumnName(columnIndex);
            if (columnIndex > 1) writer.write(" | ");
            writer.write(String.format("%" + columnDisplaySize + "s", columnName));
        }
        writer.write('\n');
    }

    private void printDataRow(Writer writer) throws SQLException, IOException {
        for (int columnIndex = 1; columnIndex <= metaData.getColumnCount(); columnIndex++) {
            int columnDisplaySize = metaData.getColumnDisplaySize(columnIndex);
            String data = resultSet.getString(columnIndex);
            if (columnIndex > 1) writer.write(" | ");
            writer.write(String.format("%" + columnDisplaySize + "s", data));
        }
        writer.write('\n');
    }
}
