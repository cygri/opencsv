package com.opencsv.bean;

import com.opencsv.CSVReader;
import com.opencsv.bean.mocks.MockBean;
import org.junit.Test;

import java.io.StringReader;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * This test was created based on an question posted on stack overflow
 * on how to handle formatted doubles.
 * Created by scott on 11/15/15.
 */
public class CsvToBeanDoubleTest {

    private static final double DOUBLE_NUMBER = 10023000000000d;

    private static final String TEST_STRING = "name,orderNumber,doubleNum\n" +
            "kyle,abc123456,10023000000000\n" +
            "jimmy,def098765,1.0023E+13 ";

    private CSVReader createReader() {
        return createReader(TEST_STRING);
    }

    private CSVReader createReader(String testString) {
        StringReader reader = new StringReader(testString);
        return new CSVReader(reader);
    }

    private MockBean createMockBean(String name, String orderNumber, double num) {
        MockBean mockBean = new MockBean();
        mockBean.setName(name);
        mockBean.setOrderNumber(orderNumber);
        mockBean.setDoubleNum(num);
        return mockBean;
    }

    @Test
    public void parseBeanWithNoAnnotations() {
        HeaderColumnNameMappingStrategy<MockBean> strategy = new HeaderColumnNameMappingStrategy<MockBean>();
        strategy.setType(MockBean.class);
        CsvToBean<MockBean> bean = new CsvToBean<MockBean>();

        List<MockBean> beanList = bean.parse(strategy, createReader());
        assertEquals(2, beanList.size());
        assertTrue(beanList.contains(createMockBean("kyle", "abc123456", DOUBLE_NUMBER)));
        assertTrue(beanList.contains(createMockBean("jimmy", "def098765", DOUBLE_NUMBER)));
    }
}
