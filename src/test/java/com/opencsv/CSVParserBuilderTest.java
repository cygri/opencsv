package com.opencsv;

import com.opencsv.enums.CSVReaderNullFieldIndicator;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CSVParserBuilderTest {

   private CSVParserBuilder builder;

   @Before
   public void setUp() throws Exception {
      builder = new CSVParserBuilder();
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

       CSVParser parser = builder.build();
      assertEquals(
              ICSVParser.DEFAULT_SEPARATOR,
              parser.getSeparator());
      assertEquals(
              ICSVParser.DEFAULT_QUOTE_CHARACTER,
              parser.getQuotechar());
      assertEquals(
              ICSVParser.DEFAULT_ESCAPE_CHARACTER,
              parser.getEscape());
      assertEquals(
              ICSVParser.DEFAULT_STRICT_QUOTES,
              parser.isStrictQuotes());
      assertEquals(
              ICSVParser.DEFAULT_IGNORE_LEADING_WHITESPACE,
              parser.isIgnoreLeadingWhiteSpace());
      assertEquals(
              ICSVParser.DEFAULT_IGNORE_QUOTATIONS,
              parser.isIgnoreQuotations());
       assertEquals(CSVReaderNullFieldIndicator.NEITHER, parser.nullFieldIndicator());
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

   @Test
   public void testWithEscapeChar() {
      final char expected = '3';
      builder.withEscapeChar(expected);
       assertEquals(expected, builder.getEscapeChar());
       assertEquals(expected, builder.build().getEscape());
   }

   @Test
   public void testWithStrictQuotes() {
      final boolean expected = true;
      builder.withStrictQuotes(expected);
       assertEquals(expected, builder.isStrictQuotes());
       assertEquals(expected, builder.build().isStrictQuotes());
   }

   @Test
   public void testWithIgnoreLeadingWhiteSpace() {
      final boolean expected = true;
      builder.withIgnoreLeadingWhiteSpace(expected);
       assertEquals(expected, builder.isIgnoreLeadingWhiteSpace());
       assertEquals(expected, builder.build().isIgnoreLeadingWhiteSpace());
   }

   @Test
   public void testWithIgnoreQuotations() {
      final boolean expected = true;
      builder.withIgnoreQuotations(expected);
       assertEquals(expected, builder.isIgnoreQuotations());
       assertEquals(expected, builder.build().isIgnoreQuotations());
   }
}
