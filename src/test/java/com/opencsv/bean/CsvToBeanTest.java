package com.opencsv.bean;

import com.opencsv.CSVReader;
import com.opencsv.bean.mocks.*;
import com.opencsv.exceptions.CsvBadConverterException;
import org.junit.Test;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class CsvToBeanTest {
   private static final String TEST_STRING = "name,orderNumber,num\n" +
         "kyle,abc123456,123\n" +
         "jimmy,def098765,456 ";

   private static final String TEST_STRING_WITHOUT_MANDATORY_FIELD = "name,orderNumber,num\n" +
           "kyle,abc123456,123\n" +
           "jimmy,def098765,";

   private static final String TEST_STRING_ALL_DATATYPES = "familyId,familyName,familySize,averageAge,averageIncome,numberOfPets,numberOfBedrooms,zipcodePrefix,hasBeenContacted\n" +
           "922337203685477580,Jones,5,18.77293748162537,32000.72937634,1,4,Z,true\n" +
           "238801727291675293,Smith,3,28.74826489578307,56643.82631345,2,2,A,false\n" +
           "882101432432123445,,3,38.48347628462843,74200.73912766,0,3,Z,false\n" +
           "619364584659026342,Woods,4,17.12739636774893,48612.12395295,1,,M,true";

   private static final String TEST_STRING_PRIVATE_FIELDS = "privateField1,privateField2\n" +
           "firstValue,secondValue";

   private static final String TEST_STRING_ALL_MODIFIER_TYPES = "publicField,privateField,protectedField,packagePrivateField\n" +
           "firstValue,secondValue,thirdValue,fourthValue";

   private static final String TEST_STRING_UNBINDABLE_TYPE = "date\n" +
           "\"Sat, 12 Aug 1995 13:30:00 GMT+0430\"";

   private CSVReader createReader() {
      return createReader(TEST_STRING);
   }

   private CSVReader createReader(String testString) {
      StringReader reader = new StringReader(testString);
      return new CSVReader(reader);
   }

   private MappingStrategy createErrorHeaderMappingStrategy() {
      return new MappingStrategy() {

         @Override
         public PropertyDescriptor findDescriptor(int col) throws IntrospectionException {
            return null;
         }

         @Override
         public BeanField findField(int col) {
            return null;
         }

         @Override
         public Object createBean() throws InstantiationException, IllegalAccessException {
            return null;
         }

         @Override
         public void captureHeader(CSVReader reader) throws IOException {
            throw new IOException("This is the test exception");
         }

         @Override
         public Integer getColumnIndex(String name) {
            return null;
         }

         @Override
         public boolean isAnnotationDriven() {
            return false;
         }
      };
   }

   private MappingStrategy createErrorLineMappingStrategy() {
      return new MappingStrategy() {

         @Override
         public PropertyDescriptor findDescriptor(int col) throws IntrospectionException {
            return null;
         }

         @Override
         public BeanField findField(int col) {
            return null;
         }

         @Override
         public Object createBean() throws InstantiationException, IllegalAccessException {
            throw new InstantiationException("this is a test Exception");
         }

         @Override
         public void captureHeader(CSVReader reader) throws IOException {
         }

         @Override
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
      CsvToBean bean = new CsvToBean();
      bean.parse(createErrorHeaderMappingStrategy(), createReader());
   }

   @Test(expected = RuntimeException.class)
   public void throwRuntimeExceptionLineWhenExceptionIsThrown() {
      CsvToBean bean = new CsvToBean();
      bean.parse(createErrorLineMappingStrategy(), createReader());
   }

   @Test
   public void parseBeanWithNoAnnotations() throws CsvBadConverterException {
      HeaderColumnNameMappingStrategy<MockBean> strategy = new HeaderColumnNameMappingStrategy<>();
      strategy.setType(MockBean.class);
      CsvToBean<MockBean> bean = new CsvToBean<>();

      List<MockBean> beanList = bean.parse(strategy, createReader());
      assertEquals(2, beanList.size());
      assertTrue(beanList.contains(createMockBean("kyle", "abc123456", 123)));
      assertTrue(beanList.contains(createMockBean("jimmy", "def098765", 456)));
   }

   private MockBean createMockBean(String name, String orderNumber, int num) {
      MockBean mockBean = new MockBean();
      mockBean.setName(name);
      mockBean.setOrderNumber(orderNumber);
      mockBean.setNum(num);
      return mockBean;
   }

   @Test
   public void parseBeanWithAnnotations() throws CsvBadConverterException {
      HeaderColumnNameMappingStrategy<SimpleAnnotatedMockBean> strategy = new HeaderColumnNameMappingStrategy<>();
      strategy.setType(SimpleAnnotatedMockBean.class);
      CsvToBean<SimpleAnnotatedMockBean> csvToBean = new CsvToBean<>();

      List<SimpleAnnotatedMockBean> beanList = csvToBean.parse(strategy, createReader());
      assertEquals(2, beanList.size());

      List<String> expectedNames = Arrays.asList("kyle", "jimmy");
      List<String> expectedOrderNumbers = Arrays.asList("abc123456", "def098765");
      List<Integer> expectedNums = Arrays.asList(123, 456);
      for (SimpleAnnotatedMockBean bean : beanList) {
         assertTrue(expectedNames.contains(bean.getName()));
         assertTrue(expectedOrderNumbers.contains(bean.getOrderNumber()));
         assertTrue(expectedNums.contains(bean.getNum()));
      }
   }

   @Test
   public void parseBeanWithSomeAnnotations() throws CsvBadConverterException {
      HeaderColumnNameMappingStrategy<SimplePartiallyAnnotatedMockBean> strategy = new HeaderColumnNameMappingStrategy<>();
      strategy.setType(SimplePartiallyAnnotatedMockBean.class);
      CsvToBean<SimplePartiallyAnnotatedMockBean> csvToBean = new CsvToBean<>();

      List<SimplePartiallyAnnotatedMockBean> beanList = csvToBean.parse(strategy, createReader());
      assertEquals(2, beanList.size());

      List<String> expectedNames = Arrays.asList("kyle", "jimmy");
      List<Integer> expectedNums = Arrays.asList(123, 456);
      for (SimplePartiallyAnnotatedMockBean bean : beanList) {
         assertTrue(expectedNames.contains(bean.getName()));
         assertTrue(expectedNums.contains(bean.getNum()));
         assertNull(bean.getOrderNumber());
      }
   }

   @Test
   public void parseAnnotatedBeanWithAllValidDataTypes() throws CsvBadConverterException {
      HeaderColumnNameMappingStrategy<AnnotatedMockBean> strategy = new HeaderColumnNameMappingStrategy<>();
      strategy.setType(AnnotatedMockBean.class);
      CsvToBean<AnnotatedMockBean> csvToBean = new CsvToBean<>();

      List<AnnotatedMockBean> beanList = csvToBean.parse(strategy, createReader(TEST_STRING_ALL_DATATYPES));

      assertEquals(4, beanList.size());
      AnnotatedMockBean bean = beanList.get(0);
      assertTrue(bean.getFamilyId() == 922337203685477580L);
      assertTrue(bean.getFamilyName().equals("Jones"));
      assertTrue(bean.getFamilySize() == 5);
      assertTrue(bean.getAverageAge() == 18.77293748162537D);
      assertTrue(bean.getAverageIncome() == 32000.729F);
      assertTrue(bean.getNumberOfPets() == 1);
      assertTrue(bean.getNumberOfBedrooms() == 4);
      assertTrue(bean.getZipcodePrefix() == 'Z');
      assertTrue(bean.isHasBeenContacted());
   }

   @Test
   public void parseAnnotatedBeanWithPrivateField() throws CsvBadConverterException {
      HeaderColumnNameMappingStrategy<SimpleAnnotatedMockBeanPrivateFields> strategy = new HeaderColumnNameMappingStrategy<>();
      strategy.setType(SimpleAnnotatedMockBeanPrivateFields.class);
      CsvToBean<SimpleAnnotatedMockBeanPrivateFields> csvToBean = new CsvToBean<>();

      List<SimpleAnnotatedMockBeanPrivateFields> beanList = csvToBean.parse(strategy, createReader(TEST_STRING_PRIVATE_FIELDS));
      assertEquals(1, beanList.size());

      SimpleAnnotatedMockBeanPrivateFields bean = beanList.get(0);
      assertTrue("firstValue".equals(bean.getPrivateField1()));
      assertTrue("secondValue".equals(bean.getPrivateField2()));
   }

   @Test
   public void parseAnnotatedBeanWithFieldsOfAllAccessModifierTypes() throws CsvBadConverterException {
      HeaderColumnNameMappingStrategy<SimpleAnnotatedMockBeanAllModifierTypes> strategy = new HeaderColumnNameMappingStrategy<>();
      strategy.setType(SimpleAnnotatedMockBeanAllModifierTypes.class);
      CsvToBean<SimpleAnnotatedMockBeanAllModifierTypes> csvToBean = new CsvToBean<>();

      List<SimpleAnnotatedMockBeanAllModifierTypes> beanList = csvToBean.parse(strategy, createReader(TEST_STRING_ALL_MODIFIER_TYPES));
      assertEquals(1, beanList.size());

      SimpleAnnotatedMockBeanAllModifierTypes bean = beanList.get(0);
      assertTrue("firstValue".equals(bean.getPublicField()));
      assertTrue("secondValue".equals(bean.getPrivateField()));
      assertTrue("thirdValue".equals(bean.getProtectedField()));
      assertTrue("fourthValue".equals(bean.getPackagePrivateField()));
   }

   @Test(expected = RuntimeException.class)
   public void throwRuntimeExceptionWhenUnsupportedDataTypeUsedInAnnotatedBean() throws CsvBadConverterException {
      HeaderColumnNameMappingStrategy<UnbindableMockBean> strategy = new HeaderColumnNameMappingStrategy<>();
      strategy.setType(UnbindableMockBean.class);
      CsvToBean<UnbindableMockBean> csvToBean = new CsvToBean<>();

      csvToBean.parse(strategy, createReader(TEST_STRING_UNBINDABLE_TYPE));
   }

   @Test(expected = RuntimeException.class)
   public void throwRuntimeExceptionWhenRequiredFieldNotProvidedInAnnotatedBean() throws CsvBadConverterException {
      HeaderColumnNameMappingStrategy<SimpleAnnotatedMockBean> strategy = new HeaderColumnNameMappingStrategy<>();
      strategy.setType(SimpleAnnotatedMockBean.class);
      CsvToBean<SimpleAnnotatedMockBean> csvToBean = new CsvToBean<>();

      csvToBean.parse(strategy, createReader(TEST_STRING_WITHOUT_MANDATORY_FIELD));
   }
}
