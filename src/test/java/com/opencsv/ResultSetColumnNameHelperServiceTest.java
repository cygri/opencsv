package com.opencsv;

import org.junit.Test;

import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

import static org.junit.Assert.assertArrayEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by scott on 12/31/15.
 */
public class ResultSetColumnNameHelperServiceTest {

    @Test
    public void canPrintColumnNames() throws SQLException {

        ResultSet resultSet = mock(ResultSet.class);

        String[] expectedNames = {"name1", "name2", "name3"};

        ResultSetMetaData metaData = MockResultSetMetaDataBuilder.buildMetaData(expectedNames);

        when(resultSet.getMetaData()).thenReturn(metaData);

        // end expects

        ResultSetColumnNameHelperService service = new ResultSetColumnNameHelperService();

        String[] columnNames = service.getColumnNames(resultSet);
        assertArrayEquals(expectedNames, columnNames);
    }

    @Test
    public void setColumnNames() throws SQLException {
        ResultSet resultSet = mock(ResultSet.class);

        String[] columnNames = {"name1", "name2", "name3"};
        String[] columnHeaders = {"Column Name 1", "Column Name 2", "Column Name 3"};

        ResultSetMetaData metaData = MockResultSetMetaDataBuilder.buildMetaData(columnNames);

        when(resultSet.getMetaData()).thenReturn(metaData);

        // end expects

        ResultSetColumnNameHelperService service = new ResultSetColumnNameHelperService();
        service.setColumnNames(columnNames, columnHeaders);
        String[] rsColumnNames = service.getColumnNames(resultSet);
        assertArrayEquals(columnHeaders, rsColumnNames);
    }

    @Test
    public void getColumnNamesWithSubsetOutOfOrder() throws SQLException {
        ResultSet resultSet = mock(ResultSet.class);
        String[] realColumnNames = {"name1", "name2", "name3"};
        String[] desiredColumnNames = {"name3", "name1"};
        String[] columnHeaders = {"Column Name 3", "Column Name 1"};

        ResultSetMetaData metaData = MockResultSetMetaDataBuilder.buildMetaData(realColumnNames);

        when(resultSet.getMetaData()).thenReturn(metaData);

        // end expects

        ResultSetColumnNameHelperService service = new ResultSetColumnNameHelperService();
        service.setColumnNames(desiredColumnNames, columnHeaders);
        String[] rsColumnNames = service.getColumnNames(resultSet);
        assertArrayEquals(columnHeaders, rsColumnNames);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void numberOfColumnsNamesMustMatchNumberOfHeaders() {
        String[] desiredColumnNames = {"name3", "name1"};
        String[] columnHeaders = {"Column Name 1", "Column Name 2", "Column Name 3"};

        ResultSetColumnNameHelperService service = new ResultSetColumnNameHelperService();
        service.setColumnNames(desiredColumnNames, columnHeaders);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void cannotHaveNullColumnName() {
        String[] desiredColumnNames = {"name3", null, "name1"};
        String[] columnHeaders = {"Column Name 1", "Column Name 2", "Column Name 3"};

        ResultSetColumnNameHelperService service = new ResultSetColumnNameHelperService();
        service.setColumnNames(desiredColumnNames, columnHeaders);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void cannotHaveEmptyColumnName() {
        String[] desiredColumnNames = {"name3", "", "name1"};
        String[] columnHeaders = {"Column Name 1", "Column Name 2", "Column Name 3"};

        ResultSetColumnNameHelperService service = new ResultSetColumnNameHelperService();
        service.setColumnNames(desiredColumnNames, columnHeaders);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void cannotHaveSpaceColumnName() {
        String[] desiredColumnNames = {"name3", "    ", "name1"};
        String[] columnHeaders = {"Column Name 1", "Column Name 2", "Column Name 3"};

        ResultSetColumnNameHelperService service = new ResultSetColumnNameHelperService();
        service.setColumnNames(desiredColumnNames, columnHeaders);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void cannotHaveNullHeaderName() {
        String[] desiredColumnNames = {"name3", "name2", "name1"};
        String[] columnHeaders = {"Column Name 1", null, "Column Name 3"};

        ResultSetColumnNameHelperService service = new ResultSetColumnNameHelperService();
        service.setColumnNames(desiredColumnNames, columnHeaders);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void cannotHaveEmptyHeaderName() {
        String[] desiredColumnNames = {"name3", "name2", "name1"};
        String[] columnHeaders = {"Column Name 1", "", "Column Name 3"};

        ResultSetColumnNameHelperService service = new ResultSetColumnNameHelperService();
        service.setColumnNames(desiredColumnNames, columnHeaders);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void cannotHaveSpaceHeaderName() {
        String[] desiredColumnNames = {"name3", "name2", "name1"};
        String[] columnHeaders = {"Column Name 1", "     ", "Column Name 3"};

        ResultSetColumnNameHelperService service = new ResultSetColumnNameHelperService();
        service.setColumnNames(desiredColumnNames, columnHeaders);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void getColumnNamesThrowsExceptionIfColumnDoesNotExist() throws SQLException {
        String[] desiredColumnNames = {"name1", "name2", "badname"};
        String[] columnHeaders = {"Column Name 1", "Column Name 2", "Column Name 3"};

        ResultSetColumnNameHelperService service = new ResultSetColumnNameHelperService();
        service.setColumnNames(desiredColumnNames, columnHeaders);

        String[] realColumnNames = {"name1", "name2", "name3"};

        ResultSet resultSet = mock(ResultSet.class);
        ResultSetMetaData metaData = MockResultSetMetaDataBuilder.buildMetaData(realColumnNames);

        when(resultSet.getMetaData()).thenReturn(metaData);

        // end expects

        String[] columnNames = service.getColumnNames(resultSet);
    }

    @Test
    public void getBooleanFromResultSet() throws SQLException, IOException {
        String[] expectedNames = {"true", "false", "TRUE", "FALSE", "Null"};
        String[] realValues = {"true", "false", "TRUE", "FALSE", null};
        String[] expectedValues = {"true", "false", "true", "false", "false"};
        int[] expectedTypes = {Types.BOOLEAN, Types.BOOLEAN, Types.BOOLEAN, Types.BOOLEAN, Types.BOOLEAN};

        ResultSetMetaData metaData = MockResultSetMetaDataBuilder.buildMetaData(expectedNames, expectedTypes);
        ResultSet resultSet = MockResultSetBuilder.buildResultSet(metaData, realValues, expectedTypes);

        ResultSetColumnNameHelperService service = new ResultSetColumnNameHelperService();

        String[] columnValues = service.getColumnValues(resultSet);
        assertArrayEquals(expectedValues, columnValues);
    }

    @Test
    public void getBooleanSubsetFromResultSet() throws SQLException, IOException {
        String[] realColumnNames = {"true", "false", "TRUE", "FALSE", "Null"};
        String[] realValues = {"true", "false", "TRUE", "FALSE", null};

        String[] desiredColumnNames = {"FALSE", "true"};
        String[] desiredColumnHeaders = {"Some false", "Some true"};

        String[] expectedValues = {"false", "true"};
        int[] expectedTypes = {Types.BOOLEAN, Types.BOOLEAN, Types.BOOLEAN, Types.BOOLEAN, Types.BOOLEAN};

        ResultSetMetaData metaData = MockResultSetMetaDataBuilder.buildMetaData(realColumnNames, expectedTypes);
        ResultSet resultSet = MockResultSetBuilder.buildResultSet(metaData, realValues, expectedTypes);

        ResultSetColumnNameHelperService service = new ResultSetColumnNameHelperService();
        service.setColumnNames(desiredColumnNames, desiredColumnHeaders);

        String[] columnValues = service.getColumnValues(resultSet);
        assertArrayEquals(expectedValues, columnValues);
    }

    @Test
    public void getSubsetWithTrim() throws SQLException, IOException {

        String[] realColumnNames = {"longvarchar", "varchar", "char", "Null"};
        String[] realValues = {"a", "b ", "c", null};

        String[] desiredColumnNames = {"varchar", "Null"};
        String[] desiredColumnHeaders = {"some varchar", "expect empty string"};
        String[] expectedValues = {"b", ""};
        int[] expectedTypes = {Types.LONGVARCHAR, Types.VARCHAR, Types.CHAR, Types.CHAR};

        ResultSetMetaData metaData = MockResultSetMetaDataBuilder.buildMetaData(realColumnNames, expectedTypes);
        ResultSet resultSet = MockResultSetBuilder.buildResultSet(metaData, realValues, expectedTypes);

        ResultSetColumnNameHelperService service = new ResultSetColumnNameHelperService();
        service.setColumnNames(desiredColumnNames, desiredColumnHeaders);

        String[] columnValues = service.getColumnValues(resultSet, true);
        assertArrayEquals(expectedValues, columnValues);
    }

    @Test
    public void getCharSetWithNullAndTrim() throws SQLException, IOException {

        String[] expectedNames = {"longvarchar", "varchar", "char", "Null"};
        String[] realValues = {"a", "b ", "c", null};
        String[] expectedValues = {"a", "b", "c", ""};
        int[] expectedTypes = {Types.LONGVARCHAR, Types.VARCHAR, Types.CHAR, Types.CHAR};

        ResultSetMetaData metaData = MockResultSetMetaDataBuilder.buildMetaData(expectedNames, expectedTypes);
        ResultSet resultSet = MockResultSetBuilder.buildResultSet(metaData, realValues, expectedTypes);

        ResultSetColumnNameHelperService service = new ResultSetColumnNameHelperService();

        String[] columnValues = service.getColumnValues(resultSet, true);
        assertArrayEquals(expectedValues, columnValues);
    }

    @Test
    public void getTimestampFromResultSetWithCustomFormat() throws SQLException, IOException {
        Timestamp date = new Timestamp(new GregorianCalendar(2009, 11, 15, 12, 0, 0).getTimeInMillis());
        long dateInMilliSeconds = date.getTime();
        String customFormat = "mm/dd/yy HH:mm:ss";
        SimpleDateFormat timeFormat = new SimpleDateFormat(customFormat);

        String[] expectedNames = {"Timestamp", "Null"};
        String[] realValues = {Long.toString(dateInMilliSeconds), null};
        String[] expectedValues = {timeFormat.format(date), ""};
        int[] expectedTypes = {Types.TIMESTAMP, Types.TIMESTAMP};

        ResultSetMetaData metaData = MockResultSetMetaDataBuilder.buildMetaData(expectedNames, expectedTypes);
        ResultSet resultSet = MockResultSetBuilder.buildResultSet(metaData, realValues, expectedTypes);

        ResultSetColumnNameHelperService service = new ResultSetColumnNameHelperService();

        String[] columnValues = service.getColumnValues(resultSet, false, null, customFormat);
        assertArrayEquals(expectedValues, columnValues);
    }

    @Test
    public void getSubsetFromResultSetWithCustomFormat() throws SQLException, IOException {
        Timestamp date = new Timestamp(new GregorianCalendar(2009, 11, 15, 12, 0, 0).getTimeInMillis());
        long dateInMilliSeconds = date.getTime();
        String customFormat = "mm/dd/yy HH:mm:ss";
        SimpleDateFormat timeFormat = new SimpleDateFormat(customFormat);

        String[] realColumnNames = {"Timestamp", "Null"};
        String[] realValues = {Long.toString(dateInMilliSeconds), null};

        String[] desiredColumnNames = {"Timestamp"};
        String[] desiredColumnHeaders = {"A timestamp"};

        String[] expectedValues = {timeFormat.format(date)};
        int[] expectedTypes = {Types.TIMESTAMP, Types.TIMESTAMP};

        ResultSetMetaData metaData = MockResultSetMetaDataBuilder.buildMetaData(realColumnNames, expectedTypes);
        ResultSet resultSet = MockResultSetBuilder.buildResultSet(metaData, realValues, expectedTypes);

        ResultSetColumnNameHelperService service = new ResultSetColumnNameHelperService();
        service.setColumnNames(desiredColumnNames, desiredColumnHeaders);

        String[] columnValues = service.getColumnValues(resultSet, false, null, customFormat);
        assertArrayEquals(expectedValues, columnValues);
    }
}
