package au.com.bytecode.opencsv;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CSVIteratorTest {
    private static final String[] STRINGS = {"test1", "test2"};
    private CSVIterator iterator;
    private CSVReader mockReader;

    @Before
    public void setUp() throws IOException {
        mockReader = mock(CSVReader.class);
        when(mockReader.readNext()).thenReturn(STRINGS);
        iterator = new CSVIterator(mockReader);
    }

    @Test(expected = RuntimeException.class)
    public void readerExceptionCausesRunTimeException() throws IOException {
        when(mockReader.readNext()).thenThrow(new IOException("reader threw test exception"));
        String[] stuff = iterator.next();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void removethrowsUnsupportedOperationException() {
        iterator.remove();
    }

    @Test
    public void initialReadReturnsStrings() {
        assertArrayEquals(STRINGS, iterator.next());
    }

    @Test
    public void hasNextWorks() throws IOException {
        when(mockReader.readNext()).thenReturn(null);
        assertTrue(iterator.hasNext()); // initial read from constructor
        String[] stuff = iterator.next();
        assertFalse(iterator.hasNext());
    }
}
