package com.opencsv.bean;

import com.opencsv.CSVReader;
import com.opencsv.bean.mocks.MockBean;
import org.junit.Before;
import org.junit.Test;

import java.beans.IntrospectionException;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.Assert.*;

public class IterableCSVToBeanTest {

    private static final String TEST_STRING = "name,orderNumber,num\n" +
            "kyle,abc123456,123\n" +
            "jimmy,def098765,456 ";

    private IterableCSVToBeanBuilder<MockBean> builder;
    private HeaderColumnNameMappingStrategy<MockBean> strategy;
    private IterableCSVToBean<MockBean> bean;

    @Before
    public void setUp() {
        builder = new IterableCSVToBeanBuilder<MockBean>();
        strategy = new HeaderColumnNameMappingStrategy<MockBean>();
        strategy.setType(MockBean.class);
        bean = builder.withReader(createReader())
                .withMapper(strategy)
                .build();
    }

    private CSVReader createReader() {
        StringReader reader = new StringReader(TEST_STRING);
        return new CSVReader(reader);
    }

    private CsvToBeanFilter createFilter() {
        return new CsvToBeanFilter() {
            @Override
            public boolean allowLine(String[] line) {
                int index = strategy.getColumnIndex("num");
                return Integer.parseInt(line[index].trim()) > 200;
            }
        };
    }

    @Test
    public void nextLine() throws InvocationTargetException, IOException, IntrospectionException, InstantiationException, IllegalAccessException {
        MockBean mockBean = bean.nextLine();
        assertEquals("kyle", mockBean.getName());
        assertEquals("abc123456", mockBean.getOrderNumber());
        assertEquals(123, mockBean.getNum());

        mockBean = bean.nextLine();
        assertEquals("jimmy", mockBean.getName());
        assertEquals("def098765", mockBean.getOrderNumber());
        assertEquals(456, mockBean.getNum());

        mockBean = bean.nextLine();
        assertNull(mockBean);
    }

    @Test
    public void nextLineWithFilter() throws InvocationTargetException, IOException, IntrospectionException, InstantiationException, IllegalAccessException {
        bean = builder.withReader(createReader())
                .withMapper(strategy)
                .withFilter(createFilter())
                .build();

        MockBean mockBean = bean.nextLine();
        assertEquals("jimmy", mockBean.getName());
        assertEquals("def098765", mockBean.getOrderNumber());
        assertEquals(456, mockBean.getNum());

        mockBean = bean.nextLine();
        assertNull(mockBean);
    }

    @Test
    public void readWithIterator() {
        Iterator<MockBean> iterator = bean.iterator();

        try {
            iterator.remove();
            assertTrue(false); // not reached
        } catch (UnsupportedOperationException e) {
            // Good
        }

        assertTrue(iterator.hasNext());
        MockBean mockBean = iterator.next();
        assertEquals("kyle", mockBean.getName());
        assertEquals("abc123456", mockBean.getOrderNumber());
        assertEquals(123, mockBean.getNum());

        assertTrue(iterator.hasNext());
        mockBean = iterator.next();
        assertEquals("jimmy", mockBean.getName());
        assertEquals("def098765", mockBean.getOrderNumber());
        assertEquals(456, mockBean.getNum());

        assertFalse(iterator.hasNext());
        try {
            iterator.next();
            assertFalse(true);
        } catch (NoSuchElementException e) {
        }
        assertFalse(iterator.hasNext());
    }

    @Test
    public void readWithIteratorAndFilter() {
        bean = builder.withReader(createReader())
                .withMapper(strategy)
                .withFilter(createFilter())
                .build();

        Iterator<MockBean> iterator = bean.iterator();

        assertTrue(iterator.hasNext());
        MockBean mockBean = iterator.next();
        assertEquals("jimmy", mockBean.getName());
        assertEquals("def098765", mockBean.getOrderNumber());
        assertEquals(456, mockBean.getNum());

        assertFalse(iterator.hasNext());
        try {
            iterator.next();
            assertFalse(true);
        } catch (NoSuchElementException e) {
        }
        assertFalse(iterator.hasNext());
    }
}
