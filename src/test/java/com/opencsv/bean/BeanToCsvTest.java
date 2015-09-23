package com.opencsv.bean;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.bean.mocks.MockBean;
import com.opencsv.bean.mocks.SimpleAnnotatedMockBean;
import org.junit.Before;
import org.junit.Test;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class BeanToCsvTest {

   private static final String TEST_STRING = "\"name\",\"orderNumber\",\"num\"\n"
         + "\"kyle\",\"abc123456\",\"123\"\n"
         + "\"jimmy\",\"def098765\",\"456\"\n";

   private static final String NULL_TEST_STRING = "\"name\",\"orderNumber\",\"num\"\n"
         + "\"null\",\"null\",\"1\"\n"
         + "\"null\",\"null\",\"2\"\n";

   private List<MockBean> testData;
   private List<SimpleAnnotatedMockBean> annotatedTestData;
   private List<MockBean> nullData;
   private List<SimpleAnnotatedMockBean> nullAnnotatedData;
   private BeanToCsv<MockBean> bean;
   private BeanToCsv<SimpleAnnotatedMockBean> annotatedBean;

   @Before
   public void setUp() {
      bean = new BeanToCsv<>();
      annotatedBean = new BeanToCsv<>();
   }

   @Before
   public void setTestData() {
      testData = new ArrayList<>();
      MockBean mb = new MockBean();
      mb.setName("kyle");
      mb.setOrderNumber("abc123456");
      mb.setNum(123);
      testData.add(mb);
      mb = new MockBean();
      mb.setName("jimmy");
      mb.setOrderNumber("def098765");
      mb.setNum(456);
      testData.add(mb);
   }

   @Before
   public void setAnnotatedTestData() {
      annotatedTestData = new ArrayList<SimpleAnnotatedMockBean>() {{
         add(new SimpleAnnotatedMockBean() {
            @Override
            public String getName() {
               return "kyle";
            }

            @Override
            public String getOrderNumber() {
               return "abc123456";
            }

            @Override
            public int getNum() {
               return 123;
            }
         });
         add(new SimpleAnnotatedMockBean() {
            @Override
            public String getName() {
               return "jimmy";
            }

            @Override
            public String getOrderNumber() {
               return "def098765";
            }

            @Override
            public int getNum() {
               return 456;
            }
         });
      }
      };
   }

   @Before
   public void setNullData() {
      nullData = new ArrayList<>();
      MockBean mb = new MockBean();
      mb.setName(null);
      mb.setOrderNumber(null);
      mb.setNum(1);
      nullData.add(mb);
      mb = new MockBean();
      mb.setName(null);
      mb.setOrderNumber(null);
      mb.setNum(2);
      nullData.add(mb);
   }

   @Before
   public void setNullAnnotatedData() {
      nullAnnotatedData  = new ArrayList<SimpleAnnotatedMockBean>() {{
         add(new SimpleAnnotatedMockBean() {
            @Override
            public String getName() {
               return null;
            }

            @Override
            public String getOrderNumber() {
               return null;
            }

            @Override
            public int getNum() {
               return 1;
            }
         });
         add(new SimpleAnnotatedMockBean() {
            @Override
            public String getName() {
               return null;
            }

            @Override
            public String getOrderNumber() {
               return null;
            }

            @Override
            public int getNum() {
               return 2;
            }
         });
      }
      };
   }

   private MappingStrategy createErrorMappingStrategy() {
      return new MappingStrategy() {

         public PropertyDescriptor findDescriptor(int col)
               throws IntrospectionException {
            throw new IntrospectionException("This is the test exception");
         }

         @Override
         public BeanField findField(int col) {
            return null;
         }

         public Object createBean() throws InstantiationException,
               IllegalAccessException {
            return null;
         }

         public void captureHeader(CSVReader reader) throws IOException {
         }

         public Integer getColumnIndex(String name) {
            return null;
         }

         @Override
         public boolean isAnnotationDriven() {
            return false;
         }
      };
   }

   @Test(expected = RuntimeException.class)
   public void throwRuntimeExceptionWhenExceptionIsThrown() {
      StringWriter sw = new StringWriter();
      CSVWriter writer = new CSVWriter(sw);
      bean.write(createErrorMappingStrategy(), writer, testData);
   }

   @Test
   public void beanReturnsFalseOnEmptyList() {
      ColumnPositionMappingStrategy<MockBean> strat = new ColumnPositionMappingStrategy<>();
      strat.setType(MockBean.class);
      String[] columns = new String[]{"name", "orderNumber", "num"};
      strat.setColumnMapping(columns);

      StringWriter sw = new StringWriter();

      assertFalse(bean.write(strat, sw, new ArrayList<>()));
   }

   @Test
   public void beanReturnsFalseOnNull() {
      ColumnPositionMappingStrategy<MockBean> strat = new ColumnPositionMappingStrategy<>();
      strat.setType(MockBean.class);
      String[] columns = new String[]{"name", "orderNumber", "num"};
      strat.setColumnMapping(columns);

      StringWriter sw = new StringWriter();

      assertFalse(bean.write(strat, sw, null));
   }

   @Test
   public void testWriteQuotes() throws IOException {
      ColumnPositionMappingStrategy<MockBean> strat = new ColumnPositionMappingStrategy<>();
      strat.setType(MockBean.class);
      String[] columns = new String[]{"name", "orderNumber", "num"};
      strat.setColumnMapping(columns);

      StringWriter sw = new StringWriter();

      boolean value = bean.write(strat, sw, testData);

      assertTrue(value);

      String content = sw.getBuffer().toString();
      assertNotNull(content);
      assertEquals(TEST_STRING, content);
   }

   @Test
   public void testWriteQuotesWithAnnotatedBean() throws IOException {
      ColumnPositionMappingStrategy<SimpleAnnotatedMockBean> strat = new ColumnPositionMappingStrategy<>();
      strat.setType(SimpleAnnotatedMockBean.class);
      String[] columns = new String[]{"name", "orderNumber", "num"};
      strat.setColumnMapping(columns);

      StringWriter sw = new StringWriter();

      boolean value = annotatedBean.write(strat, sw, annotatedTestData);

      assertTrue(value);

      String content = sw.getBuffer().toString();
      assertNotNull(content);
      assertEquals(TEST_STRING, content);
   }

   @Test
   public void testWriteNulls() throws IOException {
      ColumnPositionMappingStrategy<MockBean> strat = new ColumnPositionMappingStrategy<>();
      strat.setType(MockBean.class);
      String[] columns = new String[]{"name", "orderNumber", "num"};
      strat.setColumnMapping(columns);

      StringWriter sw = new StringWriter();

      boolean value = bean.write(strat, sw, nullData);

      assertTrue(value);

      String content = sw.getBuffer().toString();
      assertNotNull(content);
      assertEquals(NULL_TEST_STRING, content);
   }

   @Test
   public void testWriteNullsWithAnnotatedBean() throws IOException {
      ColumnPositionMappingStrategy<SimpleAnnotatedMockBean> strat = new ColumnPositionMappingStrategy<>();
      strat.setType(SimpleAnnotatedMockBean.class);
      String[] columns = new String[]{"name", "orderNumber", "num"};
      strat.setColumnMapping(columns);

      StringWriter sw = new StringWriter();

      boolean value = annotatedBean.write(strat, sw, nullAnnotatedData);

      assertTrue(value);

      String content = sw.getBuffer().toString();
      assertNotNull(content);
      assertEquals(NULL_TEST_STRING, content);
   }
}
