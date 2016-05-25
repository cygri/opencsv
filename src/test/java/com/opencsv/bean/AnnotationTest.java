/*
 * Copyright 2016 Andrew Rucker Jones.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.opencsv.bean;

import com.opencsv.CSVReader;
import com.opencsv.bean.mocks.*;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.apache.commons.beanutils.ConversionException;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

import static org.junit.Assert.*;

/**
 * This class tests all annotation-based mapping.
 *
 * @author Andrew Rucker Jones
 */
public class AnnotationTest {

    private static GregorianCalendar createDefaultTime() {
        return new GregorianCalendar(1978, Calendar.JANUARY, 15, 6, 32, 9);
    }

    @Test
    public void testGoodDataByName() throws FileNotFoundException {
        HeaderColumnNameMappingStrategy<AnnotatedMockBeanFull> strat =
                new HeaderColumnNameMappingStrategy<AnnotatedMockBeanFull>();
        strat.setType(AnnotatedMockBeanFull.class);
        FileReader fin = new FileReader("src/test/resources/testinputfullgood.csv");
        testGoodData(strat, fin);
    }

    @Test
    public void testGoodDataByPosition() throws FileNotFoundException {
        ColumnPositionMappingStrategy<AnnotatedMockBeanFull> strat =
                new ColumnPositionMappingStrategy<AnnotatedMockBeanFull>();
        strat.setType(AnnotatedMockBeanFull.class);
        FileReader fin = new FileReader("src/test/resources/testinputposfullgood.csv");
        testGoodData(strat, fin);
    }

    private static void testGoodData(MappingStrategy strat, Reader fin) {
        CSVReader read = new CSVReader(fin, ';');
        CsvToBean ctb = new CsvToBean();
        List<AnnotatedMockBeanFull> beanList = ctb.parse(strat, read);
        AnnotatedMockBeanFull bean = beanList.get(0);
        assertTrue(bean.getBoolWrapped());
        assertFalse(bean.isBoolPrimitive());
        assertEquals(1L, (long) bean.getByteWrappedDefaultLocale());
        assertEquals(2L, (long) bean.getByteWrappedSetLocale());
        assertEquals(3L, (long) bean.getBytePrimitiveDefaultLocale());
        assertEquals(4L, (long) bean.getBytePrimitiveSetLocale());
        assertEquals((double) 123101.101, (double) bean.getDoubleWrappedDefaultLocale(), 0);
        assertEquals((double) 123202.202, (double) bean.getDoubleWrappedSetLocale(), 0);
        assertEquals(123303.303, bean.getDoublePrimitiveDefaultLocale(), 0);
        assertEquals(123404.404, bean.getDoublePrimitiveSetLocale(), 0);
        assertEquals((float) 123101.101, (float) bean.getFloatWrappedDefaultLocale(), 0);
        assertEquals((float) 123202.202, (float) bean.getFloatWrappedSetLocale(), 0);

        // There appear to be rounding errors when converting from Float to float.
        assertEquals(123303.303, bean.getFloatPrimitiveDefaultLocale(), 0.002);
        assertEquals(123404.404, bean.getFloatPrimitiveSetLocale(), 0.003);

        assertEquals(5000, (int) bean.getIntegerWrappedDefaultLocale());
        assertEquals(6000, (int) bean.getIntegerWrappedSetLocale());
        assertEquals(Integer.MAX_VALUE - 7000, bean.getIntegerPrimitiveDefaultLocale());
        assertEquals(8000, bean.getIntegerPrimitiveSetLocale());
        assertEquals(9000L, (long) bean.getLongWrappedDefaultLocale());
        assertEquals(10000L, (long) bean.getLongWrappedSetLocale());
        assertEquals(11000L, bean.getLongPrimitiveDefaultLocale());
        assertEquals(12000L, bean.getLongPrimitiveSetLocale());
        assertEquals((short) 13000, (short) bean.getShortWrappedDefaultLocale());
        assertEquals((short) 14000, (short) bean.getShortWrappedSetLocale());
        assertEquals(15000, bean.getShortPrimitiveDefaultLocale());
        assertEquals(16000, bean.getShortPrimitiveSetLocale());
        assertEquals('a', (char) bean.getCharacterWrapped());
        assertEquals('b', bean.getCharacterPrimitive());
        assertEquals(BigDecimal.valueOf(123101.101), bean.getBigdecimalDefaultLocale());
        assertEquals(BigDecimal.valueOf(123102.102), bean.getBigdecimalSetLocale());
        assertEquals(BigInteger.valueOf(101), bean.getBigintegerDefaultLocale());
        assertEquals(BigInteger.valueOf(102), bean.getBigintegerSetLocale());
        assertEquals(createDefaultTime().getTimeInMillis(), bean.getDateDefaultLocale().getTime());
        assertEquals(createDefaultTime().getTimeInMillis(), bean.getGcalDefaultLocale().getTimeInMillis());
        assertEquals(createDefaultTime().getTimeInMillis(), bean.getCalDefaultLocale().getTimeInMillis());
        assertEquals(createDefaultTime().getTimeInMillis(), bean.getXmlcalDefaultLocale().toGregorianCalendar().getTimeInMillis());
        assertEquals(createDefaultTime().getTimeInMillis(), bean.getSqltimeDefaultLocale().getTime());
        assertEquals(createDefaultTime().getTimeInMillis(), bean.getSqltimestampDefaultLocale().getTime());
        assertEquals(createDefaultTime().getTimeInMillis(), bean.getDateSetLocale().getTime());
        assertEquals(createDefaultTime().getTimeInMillis(), bean.getGcalSetLocale().getTimeInMillis());
        assertEquals(createDefaultTime().getTimeInMillis(), bean.getCalSetLocale().getTimeInMillis());
        assertEquals(createDefaultTime().getTimeInMillis(), bean.getXmlcalSetLocale().toGregorianCalendar().getTimeInMillis());
        assertEquals(createDefaultTime().getTimeInMillis(), bean.getSqltimeSetLocale().getTime());
        assertEquals(createDefaultTime().getTimeInMillis(), bean.getSqltimestampSetLocale().getTime());
        assertEquals("1978-01-15", bean.getSqldateDefaultLocale().toString());
        assertEquals("1978-01-15", bean.getSqldateSetLocale().toString());
        assertEquals("test string", bean.getStringClass());
        assertEquals(new GregorianCalendar(1978, 0, 15).getTimeInMillis(), bean.getGcalFormatDefaultLocale().getTimeInMillis());
        // https://issues.apache.org/jira/browse/BEANUTILS-486
//        assertEquals(new GregorianCalendar(2018, 11, 13).getTimeInMillis(), bean.getGcalFormatSetLocale().getTimeInMillis());
        assertEquals(1.01, bean.getFloatBadLocale(), 0.001);
        assertNull(bean.getColumnDoesntExist());
        assertNull(bean.getUnmapped());

        bean = beanList.get(1);
        assertNull(bean.getBoolWrapped());
        assertFalse(bean.isBoolPrimitive());
        GregorianCalendar gc = createDefaultTime();
        gc.set(Calendar.HOUR_OF_DAY, 16);
        assertEquals(gc.getTimeInMillis(), bean.getGcalDefaultLocale().getTimeInMillis());
    }

    @Test
    public void testGoodDataCustomByName() throws FileNotFoundException {
        HeaderColumnNameMappingStrategy<AnnotatedMockBeanCustom> strat =
                new HeaderColumnNameMappingStrategy<AnnotatedMockBeanCustom>();
        strat.setType(AnnotatedMockBeanCustom.class);
        FileReader fin = new FileReader("src/test/resources/testinputcustomgood.csv");
        testGoodDataCustom(strat, fin);
    }

    @Test
    public void testGoodDataCustomByPosition() throws FileNotFoundException {
        ColumnPositionMappingStrategy<AnnotatedMockBeanCustom> strat =
                new ColumnPositionMappingStrategy<AnnotatedMockBeanCustom>();
        strat.setType(AnnotatedMockBeanCustom.class);
        FileReader fin = new FileReader("src/test/resources/testinputposcustomgood.csv");
        testGoodDataCustom(strat, fin);
    }

    private void testGoodDataCustom(MappingStrategy strat, Reader fin) {
        CSVReader read = new CSVReader(fin, ';');
        CsvToBean ctb = new CsvToBean();
        List<AnnotatedMockBeanCustom> beanList = ctb.parse(strat, read);

        AnnotatedMockBeanCustom bean = beanList.get(0);
        assertTrue(bean.getBoolWrapped());
        assertFalse(bean.isBoolPrimitive());
        assertEquals(Byte.MAX_VALUE, (long) bean.getByteWrappedDefaultLocale());
        assertEquals(Byte.MAX_VALUE, (long) bean.getByteWrappedSetLocale());
        assertEquals(Byte.MAX_VALUE, (long) bean.getBytePrimitiveDefaultLocale());
        assertEquals(Byte.MAX_VALUE, (long) bean.getBytePrimitiveSetLocale());
        assertEquals(Double.MAX_VALUE, (double) bean.getDoubleWrappedDefaultLocale(), 0);
        assertEquals(Double.MAX_VALUE, (double) bean.getDoubleWrappedSetLocale(), 0);
        assertEquals(Double.MAX_VALUE, bean.getDoublePrimitiveDefaultLocale(), 0);
        assertEquals(Double.MAX_VALUE, bean.getDoublePrimitiveSetLocale(), 0);
        assertEquals(Float.MAX_VALUE, (float) bean.getFloatWrappedDefaultLocale(), 0);
        assertEquals(Float.MAX_VALUE, (float) bean.getFloatWrappedSetLocale(), 0);
        assertEquals(Float.MAX_VALUE, bean.getFloatPrimitiveDefaultLocale(), 0);
        assertEquals(Float.MAX_VALUE, bean.getFloatPrimitiveSetLocale(), 0);
        assertEquals(Integer.MAX_VALUE, (int) bean.getIntegerWrappedDefaultLocale());
        assertEquals(Integer.MAX_VALUE, (int) bean.getIntegerWrappedSetLocale());
        assertEquals(Integer.MAX_VALUE, bean.getIntegerPrimitiveDefaultLocale());
        assertEquals(Integer.MAX_VALUE, bean.getIntegerPrimitiveSetLocale());
        assertEquals(Long.MAX_VALUE, (long) bean.getLongWrappedDefaultLocale());
        assertEquals(Long.MAX_VALUE, (long) bean.getLongWrappedSetLocale());
        assertEquals(Long.MAX_VALUE, bean.getLongPrimitiveDefaultLocale());
        assertEquals(Long.MAX_VALUE, bean.getLongPrimitiveSetLocale());
        assertEquals(Short.MAX_VALUE, (short) bean.getShortWrappedDefaultLocale());
        assertEquals(Short.MAX_VALUE, (short) bean.getShortWrappedSetLocale());
        assertEquals(Short.MAX_VALUE, bean.getShortPrimitiveDefaultLocale());
        assertEquals(Short.MAX_VALUE, bean.getShortPrimitiveSetLocale());
        assertEquals(Character.MAX_VALUE, (char) bean.getCharacterWrapped());
        assertEquals(Character.MAX_VALUE, bean.getCharacterPrimitive());
        assertEquals(BigDecimal.TEN, bean.getBigdecimalDefaultLocale());
        assertEquals(BigDecimal.TEN, bean.getBigdecimalSetLocale());
        assertEquals(BigInteger.TEN, bean.getBigintegerDefaultLocale());
        assertEquals(BigInteger.TEN, bean.getBigintegerSetLocale());
        assertEquals("inside custom converter", bean.getStringClass());
        assertEquals(Arrays.asList("really", "long", "test", "string,", "yeah!"), bean.getComplexString());
        assertTrue(bean.getComplexClass1() instanceof ComplexClassForCustomAnnotation);
        assertFalse(bean.getComplexClass1() instanceof ComplexDerivedClassForCustomAnnotation);
        assertEquals(1, bean.getComplexClass1().i);
        assertEquals('a', bean.getComplexClass1().c);
        assertEquals("long,long.string1", bean.getComplexClass1().s);
        assertTrue(bean.getComplexClass2() instanceof ComplexClassForCustomAnnotation);
        assertFalse(bean.getComplexClass2() instanceof ComplexDerivedClassForCustomAnnotation);
        assertEquals(Integer.MAX_VALUE - 2, bean.getComplexClass2().i);
        assertEquals('z', bean.getComplexClass2().c);
        assertEquals("Inserted in setter methodlong,long.string2", bean.getComplexClass2().s);
        assertTrue(bean.getComplexClass3() instanceof ComplexClassForCustomAnnotation);
        assertTrue(bean.getComplexClass3() instanceof ComplexDerivedClassForCustomAnnotation);
        assertEquals(3, bean.getComplexClass3().i);
        assertEquals('c', bean.getComplexClass3().c);
        assertEquals("long,long.derived.string3", bean.getComplexClass3().s);
        assertEquals((float) 1.0, ((ComplexDerivedClassForCustomAnnotation) bean.getComplexClass3()).f, 0);

        bean = beanList.get(1);
        assertEquals(Arrays.asList("really"), bean.getComplexString());
        assertTrue(bean.getComplexClass2() instanceof ComplexClassForCustomAnnotation);
        assertTrue(bean.getComplexClass2() instanceof ComplexDerivedClassForCustomAnnotation);
        assertEquals(Integer.MAX_VALUE - 5, bean.getComplexClass2().i);
        assertEquals('z', bean.getComplexClass2().c);
        assertEquals("Inserted in setter methodlong,long.derived.string5", bean.getComplexClass2().s);
        assertEquals((float) 1.0, ((ComplexDerivedClassForCustomAnnotation) bean.getComplexClass2()).f, 0);

        bean = beanList.get(2);
        assertEquals(new ArrayList<String>(), bean.getComplexString());
        assertTrue(bean.getComplexClass1() instanceof ComplexClassForCustomAnnotation);
        assertTrue(bean.getComplexClass1() instanceof ComplexDerivedClassForCustomAnnotation);
        assertEquals(7, bean.getComplexClass1().i);
        assertEquals('g', bean.getComplexClass1().c);
        assertEquals("long,long.derived.string7", bean.getComplexClass1().s);
        assertEquals((float) 1.0, ((ComplexDerivedClassForCustomAnnotation) bean.getComplexClass1()).f, 0);

        for (AnnotatedMockBeanCustom cb : beanList.subList(1, 4)) {
            assertTrue(cb.getBoolWrapped());
            assertFalse(cb.isBoolPrimitive());
            assertFalse(cb.getBoolWrappedOptional());
            assertTrue(cb.isBoolPrimitiveOptional());
        }

        bean = beanList.get(5);
        assertNull(bean.getBoolWrappedOptional());
        assertFalse(bean.isBoolPrimitiveOptional());
        assertNull(bean.getComplexString());
    }

    @Test
    public void testCase7() throws FileNotFoundException {
        HeaderColumnNameMappingStrategy<AnnotatedMockBeanFull> strat =
                new HeaderColumnNameMappingStrategy<AnnotatedMockBeanFull>();
        strat.setType(AnnotatedMockBeanFull.class);
        FileReader fin = new FileReader("src/test/resources/testinputcase7.csv");
        testCases7And51(strat, fin);
    }

    @Test
    public void testCase51() throws FileNotFoundException {
        ColumnPositionMappingStrategy<AnnotatedMockBeanFull> strat =
                new ColumnPositionMappingStrategy<AnnotatedMockBeanFull>();
        strat.setType(AnnotatedMockBeanFull.class);
        FileReader fin = new FileReader("src/test/resources/testinputcase51.csv");
        testCases7And51(strat, fin);
    }

    private void testCases7And51(MappingStrategy strat, Reader fin) {
        CSVReader read = new CSVReader(fin, ';');
        CsvToBean ctb = new CsvToBean();
        try {
            ctb.parse(strat, read);
            fail("The parse should have thrown an Exception.");
        } catch (RuntimeException e) {
            assertTrue(e.getCause() instanceof CsvRequiredFieldEmptyException);
            CsvRequiredFieldEmptyException csve = (CsvRequiredFieldEmptyException) e.getCause();
            assertEquals(1, csve.getLineNumber());
            assertEquals(AnnotatedMockBeanFull.class.getName(), csve.getBeanClass().getName());
            assertEquals("byteWrappedSetLocale", csve.getDestinationField().getName());
        }

    }

    @Test
    public void testCase11() throws FileNotFoundException {
        HeaderColumnNameMappingStrategy<AnnotatedMockBeanFull> strat =
                new HeaderColumnNameMappingStrategy<AnnotatedMockBeanFull>();
        strat.setType(AnnotatedMockBeanFull.class);
        FileReader fin = new FileReader("src/test/resources/testinputcase11.csv");
        testCases11And55(strat, fin);
    }

    @Test
    public void testCase55() throws FileNotFoundException {
        ColumnPositionMappingStrategy<AnnotatedMockBeanFull> strat =
                new ColumnPositionMappingStrategy<AnnotatedMockBeanFull>();
        strat.setType(AnnotatedMockBeanFull.class);
        FileReader fin = new FileReader("src/test/resources/testinputcase55.csv");
        testCases11And55(strat, fin);
    }

    private void testCases11And55(MappingStrategy strat, Reader fin) {
        CSVReader read = new CSVReader(fin, ';');
        CsvToBean ctb = new CsvToBean();
        try {
            ctb.parse(strat, read);
            assertTrue(false); // Not reached
        } catch (RuntimeException e) {
            assertTrue(e.getCause() instanceof CsvDataTypeMismatchException);
            CsvDataTypeMismatchException csve = (CsvDataTypeMismatchException) e.getCause();
            assertEquals(1, csve.getLineNumber());
            assertTrue(csve.getSourceObject() instanceof String);
            assertEquals("mismatchedtype", (String) csve.getSourceObject());
            assertEquals(Byte.class, csve.getDestinationClass());
            assertTrue(csve.getCause() instanceof ConversionException);
        }
    }

    @Test
    public void testCase21() throws FileNotFoundException {
        HeaderColumnNameMappingStrategy<TestCases21And63> strat =
                new HeaderColumnNameMappingStrategy<TestCases21And63>();
        strat.setType(TestCases21And63.class);
        CSVReader read = new CSVReader(new StringReader("list\ntrue false true"));
        testCases21And63(strat, read);
    }

    @Test
    public void testCase63() throws FileNotFoundException {
        ColumnPositionMappingStrategy<TestCases21And63> strat =
                new ColumnPositionMappingStrategy<TestCases21And63>();
        strat.setType(TestCases21And63.class);
        CSVReader read = new CSVReader(new StringReader("true false true"));
        testCases21And63(strat, read);
    }

    private void testCases21And63(MappingStrategy strat, CSVReader read) {
        CsvToBean ctb = new CsvToBean();
        try {
            ctb.parse(strat, read);
            assertTrue(false); // Not reached
        } catch (RuntimeException e) {
            assertTrue(e.getCause() instanceof CsvDataTypeMismatchException);
            CsvDataTypeMismatchException csve = (CsvDataTypeMismatchException) e.getCause();
            assertEquals(1, csve.getLineNumber());
            assertTrue(csve.getSourceObject() instanceof String);
            assertEquals("true false true", (String) csve.getSourceObject());
            assertEquals(List.class, csve.getDestinationClass());
        }
    }

    @Test
    public void testBadDataByName() throws FileNotFoundException {
        HeaderColumnNameMappingStrategy<AnnotatedMockBeanFull> strat =
                new HeaderColumnNameMappingStrategy<AnnotatedMockBeanFull>();
        strat.setType(AnnotatedMockBeanFull.class);
        Reader fin = new FileReader("src/test/resources/testinputcase78.csv");
        CSVReader read = new CSVReader(fin, ';');
        CsvToBean ctb = new CsvToBean();
        try {
            ctb.parse(strat, read);
            fail("Expected parse to throw exception.");
        } catch (RuntimeException e) {
            assertTrue(e.getCause() instanceof CsvRequiredFieldEmptyException);
            CsvRequiredFieldEmptyException csve = (CsvRequiredFieldEmptyException) e.getCause();
            assertEquals(1, csve.getLineNumber());
            assertEquals(AnnotatedMockBeanFull.class, csve.getBeanClass());
            assertEquals("dateDefaultLocale", csve.getDestinationField().getName());
        }

        HeaderColumnNameMappingStrategy<TestCase34> strat34 =
                new HeaderColumnNameMappingStrategy<TestCase34>();
        strat34.setType(TestCase34.class);
        read = new CSVReader(new StringReader("isnotdate\n19780115T063209"));
        try {
            ctb.parse(strat34, read);
            fail("Expected parse to throw exception.");
        } catch (RuntimeException e) {
            assertTrue(e.getCause() instanceof CsvDataTypeMismatchException);
            CsvDataTypeMismatchException csve = (CsvDataTypeMismatchException) e.getCause();
            assertEquals(1, csve.getLineNumber());
            assertTrue(csve.getSourceObject() instanceof String);
            assertEquals("19780115T063209", (String) csve.getSourceObject());
            assertEquals(String.class, csve.getDestinationClass());
        }

        // For test case 73
        read = new CSVReader(new StringReader("isnotdate\n19780115T063209"));
        ctb.parse(strat34, read, false);
        List<CsvException> exlist = ctb.getCapturedExceptions();
        assertEquals(1, exlist.size());
        assertTrue(exlist.get(0) instanceof CsvDataTypeMismatchException);
        CsvDataTypeMismatchException innere = (CsvDataTypeMismatchException) exlist.get(0);
        assertEquals(1, innere.getLineNumber());
        assertTrue(innere.getSourceObject() instanceof String);
        assertEquals("19780115T063209", (String) innere.getSourceObject());
        assertEquals(String.class, innere.getDestinationClass());

    }

    @Test
    public void testCase16() throws FileNotFoundException {
        HeaderColumnNameMappingStrategy<AnnotatedMockBeanCustom> strat =
                new HeaderColumnNameMappingStrategy<AnnotatedMockBeanCustom>();
        strat.setType(AnnotatedMockBeanCustom.class);
        Reader fin = new FileReader("src/test/resources/testinputcase16.csv");
        testCases16And60(strat, fin);
    }

    @Test
    public void testCase60() throws FileNotFoundException {
        ColumnPositionMappingStrategy<AnnotatedMockBeanCustom> strat =
                new ColumnPositionMappingStrategy<AnnotatedMockBeanCustom>();
        strat.setType(AnnotatedMockBeanCustom.class);
        Reader fin = new FileReader("src/test/resources/testinputcase60.csv");
        testCases16And60(strat, fin);
    }

    private void testCases16And60(MappingStrategy strat, Reader fin) {
        CSVReader read = new CSVReader(fin, ';');
        CsvToBean ctb = new CsvToBean();
        try {
            ctb.parse(strat, read);
            fail("Expected parse to throw exception.");
        } catch (RuntimeException e) {
            assertTrue(e.getCause() instanceof CsvDataTypeMismatchException);
            CsvDataTypeMismatchException csve = (CsvDataTypeMismatchException) e.getCause();
            assertEquals(1, csve.getLineNumber());
            assertTrue(csve.getSourceObject() instanceof String);
            assertEquals("Mismatched data type", (String) csve.getSourceObject());
            assertEquals(ComplexClassForCustomAnnotation.class, csve.getDestinationClass());
            assertTrue(csve.getCause() instanceof IllegalArgumentException);
        }

    }

    @Test
    public void testBadDataCustomByName() throws FileNotFoundException {
        HeaderColumnNameMappingStrategy<AnnotatedMockBeanCustom> strat =
                new HeaderColumnNameMappingStrategy<AnnotatedMockBeanCustom>();
        strat.setType(AnnotatedMockBeanCustom.class);

        FileReader fin = new FileReader("src/test/resources/testinputcase38.csv");
        CSVReader read = new CSVReader(fin, ';');
        CsvToBean ctb = new CsvToBean();
        try {
            ctb.parse(strat, read);
            fail("Expected parse to throw exception.");
        } catch (RuntimeException e) {
            assertTrue(e.getCause() instanceof CsvDataTypeMismatchException);
            CsvDataTypeMismatchException csve = (CsvDataTypeMismatchException) e.getCause();
            assertEquals(1, csve.getLineNumber());
            assertTrue(csve.getSourceObject() instanceof String);
            assertEquals("invalidstring", (String) csve.getSourceObject());
            assertEquals(Boolean.class, csve.getDestinationClass());
            assertTrue(csve.getCause() instanceof ConversionException);
        }

        fin = new FileReader("src/test/resources/testinputcase40.csv");
        read = new CSVReader(fin, ';');
        try {
            ctb.parse(strat, read);
            fail("Expected parse to throw exception.");
        } catch (RuntimeException e) {
            assertTrue(e.getCause() instanceof CsvRequiredFieldEmptyException);
            CsvRequiredFieldEmptyException csve = (CsvRequiredFieldEmptyException) e.getCause();
            assertEquals(1, csve.getLineNumber());
            assertEquals(AnnotatedMockBeanCustom.class, csve.getBeanClass());
            assertEquals("boolWrapped", csve.getDestinationField().getName());
        }

        fin = new FileReader("src/test/resources/testinputcase41.csv");
        read = new CSVReader(fin, ';');
        try {
            ctb.parse(strat, read);
            fail("Expected parse to throw exception.");
        } catch (RuntimeException e) {
            assertTrue(e.getCause() instanceof CsvDataTypeMismatchException);
            CsvDataTypeMismatchException csve = (CsvDataTypeMismatchException) e.getCause();
            assertEquals(1, csve.getLineNumber());
            assertEquals("invaliddatum", csve.getSourceObject());
            assertEquals(Boolean.class, csve.getDestinationClass());
        }

    }

}
