package com.opencsv.stream.reader;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import static org.junit.Assert.assertEquals;

public class LineReaderTest {
    private static final String ORIGINAL = "This is the original string\r\n";
    private static final String WITH_CR = "This is the original string\r";
    private static final String NO_CR = "This is the original string";
    private static final String EMPTY_STRING = "";
    private static final String NULL_STRING = null;

    private LineReader createLineReaderforString(String s, boolean keepCR) {
        StringReader sr = new StringReader(s);
        return new LineReader(new BufferedReader(sr), keepCR);
    }

    @Test
    public void lineReaderWillKeepCR() throws IOException {
        LineReader keepCRReader = createLineReaderforString(ORIGINAL, true);
        assertEquals(WITH_CR, keepCRReader.readLine());
    }

    @Test
    public void lineReaderWillRemoveCR() throws IOException {
        LineReader noCRReader = createLineReaderforString(ORIGINAL, false);
        assertEquals(NO_CR, noCRReader.readLine());
    }

    @Test
    public void lineReaderKeepingCRWillHandleStringWithNoLinefeed() throws IOException {
        LineReader reader = createLineReaderforString(NO_CR, true);
        assertEquals(NO_CR, reader.readLine());
    }

    @Test
    public void lineReaderNoCRWillHandleStringWithNoLinefeed() throws IOException {
        LineReader reader = createLineReaderforString(NO_CR, false);
        assertEquals(NO_CR, reader.readLine());
    }

    @Test
    public void lineReaderKeepingCRWillHandleEmptyString() throws IOException {
        LineReader reader = createLineReaderforString(EMPTY_STRING, true);
        assertEquals(NULL_STRING, reader.readLine());
    }

    @Test
    public void lineReaderNoCRWillHandleEmptyString() throws IOException {
        LineReader reader = createLineReaderforString(EMPTY_STRING, false);
        assertEquals(NULL_STRING, reader.readLine());
    }
}
