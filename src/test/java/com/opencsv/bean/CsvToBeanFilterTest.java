package com.opencsv.bean;

import com.opencsv.CSVReader;
import org.junit.Test;

import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class CsvToBeanFilterTest {

   private static final String TEST_STRING =
         "FEATURE_NAME,STATE,USER_COUNT\n" +
               "hello world,production,3228\n" +
               "calc age,beta,74\n" +
               "wash dishes,alpha,3";

   private CSVReader createReader() {
      StringReader reader = new StringReader(TEST_STRING);
      return new CSVReader(reader);
   }

   private MappingStrategy CreateMappingStrategy() {
      HeaderColumnNameTranslateMappingStrategy<Feature> strategy = new HeaderColumnNameTranslateMappingStrategy();
      Map<String, String> columnMap = new HashMap();
      columnMap.put("FEATURE_NAME", "name");
      columnMap.put("STATE", "state");
      strategy.setColumnMapping(columnMap);
      strategy.setType(Feature.class);
      return strategy;
   }

   public static class Feature {
      private String name;
      private String state;

      public void setName(String name) {
         this.name = name;
      }

      public void setState(String state) {
         this.state = state;
      }

      public String getName() {
         return name;
      }

      public String getState() {
         return state;
      }
   }

   private class NonProductionFilter implements CsvToBeanFilter {

      private final MappingStrategy strategy;

      public NonProductionFilter(MappingStrategy strategy) {
         this.strategy = strategy;
      }

      @Override
      public boolean allowLine(String[] line) {
         int index = strategy.getColumnIndex("STATE");
         String value = line[index];
         boolean result = !"production".equals(value);
         return result;
      }

   }

   @Test
   public void testColumnNameTranslationWithLineFiltering() {
      CsvToBean csvToBean = new CsvToBean();
      CSVReader reader = createReader();
      MappingStrategy strategy = CreateMappingStrategy();
      CsvToBeanFilter filter = new NonProductionFilter(strategy);
      List<Feature> list = csvToBean.parse(strategy, reader, filter);
      assertEquals("Parsing resulted in the wrong number of items.", 2, list.size());
      assertEquals("The first item has the wrong name.", "calc age", list.get(0).getName());
      assertEquals("The first item has the wrong state.", "beta", list.get(0).getState());
      assertEquals("The second item has the wrong name.", "wash dishes", list.get(1).getName());
      assertEquals("The second item has the wrong state.", "alpha", list.get(1).getState());
   }

   public List<Feature> parseCsv(InputStreamReader streamReader) {
      HeaderColumnNameTranslateMappingStrategy<Feature> strategy = new HeaderColumnNameTranslateMappingStrategy();
      Map<String, String> columnMap = new HashMap();
      columnMap.put("FEATURE_NAME", "name");
      columnMap.put("STATE", "state");
      strategy.setColumnMapping(columnMap);
      strategy.setType(Feature.class);
      CSVReader reader = new CSVReader(streamReader);
      CsvToBeanFilter filter = new NonProductionFilter(strategy);
      return new CsvToBean().parse(strategy, reader, filter);
   }

}
