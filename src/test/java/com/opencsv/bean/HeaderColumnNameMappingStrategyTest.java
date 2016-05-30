package com.opencsv.bean;

/*
 Copyright 2007 Kyle Miller.

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

import com.opencsv.CSVReader;
import com.opencsv.bean.mocks.MockBean;
import org.junit.Before;
import org.junit.Test;

import java.beans.IntrospectionException;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import static org.junit.Assert.*;

public class HeaderColumnNameMappingStrategyTest {

   private static final String TEST_STRING = "name,orderNumber,num\n" +
         "kyle,abc123456,123\n" +
         "jimmy,def098765,456";

   private static final String TEST_QUOTED_STRING = "\"name\",\"orderNumber\",\"num\"\n" +
         "\"kyle\",\"abc123456\",\"123\"\n" +
         "\"jimmy\",\"def098765\",\"456\"";

   private HeaderColumnNameMappingStrategy<MockBean> strat;

   @Before
   public void setUp() {
      strat = new HeaderColumnNameMappingStrategy<MockBean>();
   }

   private List<MockBean> createTestParseResult(String parseString) {
      strat.setType(MockBean.class);
      CsvToBean<MockBean> csv = new CsvToBean<MockBean>();
      return csv.parse(strat, new StringReader(parseString));
   }

   @Test(expected = IllegalStateException.class)
   public void getColumnIndexWithoutHeaderThrowsException() {
      assertNull(strat.getColumnIndex("some index name"));
   }

   @Test
   public void getColumnIndexAfterParse() {
      List<MockBean> list = createTestParseResult(TEST_STRING);
      assertEquals(0, strat.getColumnIndex("name").intValue());
      assertEquals(1, strat.getColumnIndex("orderNumber").intValue());
      assertEquals(2, strat.getColumnIndex("num").intValue());
      assertNull(strat.getColumnIndex("unknown column"));
   }

   @Test
   public void testParse() {
      List<MockBean> list = createTestParseResult(TEST_STRING);
      assertNotNull(list);
      assertTrue(list.size() == 2);
      MockBean bean = list.get(0);
      assertEquals("kyle", bean.getName());
      assertEquals("abc123456", bean.getOrderNumber());
      assertEquals(123, bean.getNum());
   }

   @Test
   public void testQuotedString() {
      List<MockBean> list = createTestParseResult(TEST_QUOTED_STRING);
      assertNotNull(list);
      assertTrue(list.size() == 2);
      MockBean bean = list.get(0);
      assertEquals("kyle", bean.getName());
      assertEquals("abc123456", bean.getOrderNumber());
      assertEquals(123, bean.getNum());
   }

   @Test
   public void testParseWithSpacesInHeader() {
      List<MockBean> list = createTestParseResult(TEST_STRING);
      assertNotNull(list);
      assertTrue(list.size() == 2);
      MockBean bean = list.get(0);
      assertEquals("kyle", bean.getName());
      assertEquals("abc123456", bean.getOrderNumber());
      assertEquals(123, bean.getNum());
   }

   @Test
   public void verifyColumnNames() throws IOException, IntrospectionException {
      HeaderColumnNameMappingStrategy<MockBean> strat = new HeaderColumnNameMappingStrategy<MockBean>();
      strat.setType(MockBean.class);
      assertNull(strat.getColumnName(0));
      assertNull(strat.findDescriptor(0));

      StringReader reader = new StringReader(TEST_STRING);

      CSVReader csvReader = new CSVReader(reader);
      strat.captureHeader(csvReader);

      assertEquals("name", strat.getColumnName(0));
      assertEquals(strat.findDescriptor(0), strat.findDescriptor("name"));
      assertTrue(strat.matches("name", strat.findDescriptor("name")));
   }
}
