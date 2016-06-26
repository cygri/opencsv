package com.opencsv;

import com.opencsv.enums.CSVReaderNullFieldIndicator;
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
       assertEquals(
               CSVReader.DEFAULT_SKIP_LINES,
              csvReader.getSkipLines());
       assertEquals(CSVReader.DEFAULT_KEEP_CR, csvReader.keepCarriageReturns());
       assertEquals(CSVReader.DEFAULT_VERIFY_READER, csvReader.verifyReader());
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
       final ICSVParser csvParser = mock(CSVParser.class);

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
    public void testWithKeepCR() {
        builder.withKeepCarriageReturn(true);
        assertTrue(builder.keepCarriageReturn());

        final CSVReader actual = builder.build();
        assertTrue(actual.keepCarriageReturns());
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

    @Test
    public void testWithVerifyReader() {
        final CSVReader reader = builder.withVerifyReader(false).build();
        assertFalse(reader.verifyReader());
    }

    @Test
    public void builderWithNullFieldIndicator() {
        final CSVReader reader = builder.withFieldAsNull(CSVReaderNullFieldIndicator.EMPTY_SEPARATORS).build();

        assertEquals(CSVReaderNullFieldIndicator.EMPTY_SEPARATORS, reader.getParser().nullFieldIndicator());
    }
}
