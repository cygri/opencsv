package com.opencsv;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MockResultSetMetaDataBuilder {

   public static ResultSetMetaData buildMetaData(String[] columnNames) throws SQLException {

      ResultSetMetaData metaData = mock(ResultSetMetaData.class);

      when(metaData.getColumnCount()).thenReturn(columnNames.length);
      for (int i = 0; i < columnNames.length; i++) {
         when(metaData.getColumnName(i + 1)).thenReturn(columnNames[i]);
         when(metaData.getColumnLabel(i + 1)).thenReturn(columnNames[i]);
         when(metaData.getColumnType(i + 1)).thenReturn(Types.VARCHAR);
      }

      return metaData;
   }

   public static ResultSetMetaData buildMetaData(String[] columnNames, int[] columnTypes) throws SQLException {

      ResultSetMetaData metaData = mock(ResultSetMetaData.class);

      when(metaData.getColumnCount()).thenReturn(columnNames.length);
      for (int i = 0; i < columnNames.length; i++) {
         when(metaData.getColumnName(i + 1)).thenReturn(columnNames[i]);
         when(metaData.getColumnLabel(i + 1)).thenReturn(columnNames[i]);
         when(metaData.getColumnType(i + 1)).thenReturn(columnTypes[i]);
      }

      return metaData;
   }
}