package com.opencsv;

import org.junit.Before;
import org.junit.Test;

import java.io.Reader;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class CSVReaderBuilderTest {

   private CSVReaderBuilder builder;
   private Reader reader;

   @Before
   public void setUp() throws Exception {
      reader = mock(Reader.class);
      builder = new CSVReaderBuilder(reader);
   }

   @Test
   public void testDefaultBuilder() {
      assertSame(reader, builder.getReader());
      assertNull(builder.getCsvParser());
      assertEquals(
            CSVReader.DEFAULT_SKIP_LINES,
              builder.getSkipLines());

      final CSVReader csvReader = builder.build();
      assertSame(
            CSVReader.DEFAULT_SKIP_LINES,
              csvReader.getSkipLines());
   }

   @Test(expected = IllegalArgumentException.class)
   public void testNullReader() {
      builder = new CSVReaderBuilder(null);
   }

   @Test
   public void testWithCSVParserNull() {
      builder.withCSVParser(mock(CSVParser.class));
      builder.withCSVParser(null);
      assertNull(builder.getCsvParser());
   }

   @Test
   public void testWithCSVParser() {
      final CSVParser csvParser = mock(CSVParser.class);

      builder.withCSVParser(csvParser);

      assertSame(csvParser, builder.getCsvParser());

      final CSVReader actual = builder.build();
      assertSame(csvParser, actual.getParser());
   }

   @Test
   public void testWithSkipLines() {
      builder.withSkipLines(99);

      assertEquals(99, builder.getSkipLines());

      final CSVReader actual = builder.build();
      assertSame(99, actual.getSkipLines());
   }

   @Test
   public void testWithSkipLinesZero() {
      builder.withSkipLines(0);

      assertEquals(0, builder.getSkipLines());

      final CSVReader actual = builder.build();
      assertSame(0, actual.getSkipLines());
   }

   @Test
   public void testWithSkipLinesNegative() {
      builder.withSkipLines(-1);

      assertEquals(0, builder.getSkipLines());

      final CSVReader actual = builder.build();
      assertSame(0, actual.getSkipLines());
   }
}
