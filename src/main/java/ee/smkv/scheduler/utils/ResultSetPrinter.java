package ee.smkv.scheduler.utils;

import java.io.*;
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


    public void printTo(Writer writer) throws SQLException, IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(writer);
        printHeader(bufferedWriter);
        while (resultSet.next()) {
            printDataRow(bufferedWriter);
        }
        bufferedWriter.flush();
    }

    private void printHeader(BufferedWriter writer) throws SQLException, IOException {
        for (int columnIndex = 1; columnIndex <= metaData.getColumnCount(); columnIndex++) {
            int columnDisplaySize = metaData.getColumnDisplaySize(columnIndex);
            String columnName = metaData.getColumnName(columnIndex);
            if (columnIndex > 1) writer.write(" | ");
            writer.write(String.format("%-" + columnDisplaySize + "s", columnName));
        }
        writer.newLine();
    }

    private void printDataRow(BufferedWriter writer) throws SQLException, IOException {
        for (int columnIndex = 1; columnIndex <= metaData.getColumnCount(); columnIndex++) {
            int columnDisplaySize = metaData.getColumnDisplaySize(columnIndex);
            String data = resultSet.getString(columnIndex);
            if (columnIndex > 1) writer.write(" | ");
            writer.write(String.format("%-" + columnDisplaySize + "s", data));
        }
        writer.newLine();
    }
}
