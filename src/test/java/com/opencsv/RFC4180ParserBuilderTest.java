package com.opencsv;


import com.opencsv.enums.CSVReaderNullFieldIndicator;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RFC4180ParserBuilderTest {

    private RFC4180ParserBuilder builder;

    @Before
    public void setUp() throws Exception {
        builder = new RFC4180ParserBuilder();
    }

    @Test
    public void testDefaultBuilder() {
        assertEquals(
                ICSVParser.DEFAULT_SEPARATOR,
                builder.getSeparator());
        assertEquals(
                ICSVParser.DEFAULT_QUOTE_CHARACTER,
                builder.getQuoteChar());
        assertEquals(
                ICSVParser.DEFAULT_ESCAPE_CHARACTER,
                builder.getEscapeChar());
        assertEquals(
                ICSVParser.DEFAULT_STRICT_QUOTES,
                builder.isStrictQuotes());
        assertEquals(
                ICSVParser.DEFAULT_IGNORE_LEADING_WHITESPACE,
                builder.isIgnoreLeadingWhiteSpace());
        assertEquals(
                ICSVParser.DEFAULT_IGNORE_QUOTATIONS,
                builder.isIgnoreQuotations());
        assertEquals(CSVReaderNullFieldIndicator.NEITHER, builder.nullFieldIndicator());

        ICSVParser parser = builder.build();
        assertEquals(
                ICSVParser.DEFAULT_SEPARATOR,
                parser.getSeparator());
        assertEquals(
                ICSVParser.DEFAULT_QUOTE_CHARACTER,
                parser.getQuotechar());
    }

    @Test
    public void testWithSeparator() {
        final char expected = '1';
        builder.withSeparator(expected);
        assertEquals(expected, builder.getSeparator());
        assertEquals(expected, builder.build().getSeparator());
    }

    @Test
    public void testWithQuoteChar() {
        final char expected = '2';
        builder.withQuoteChar(expected);
        assertEquals(expected, builder.getQuoteChar());
        assertEquals(expected, builder.build().getQuotechar());
    }
}
