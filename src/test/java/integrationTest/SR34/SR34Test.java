package integrationTest.SR34;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.ICSVParser;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by scott on 3/6/15.
 */
public class SR34Test {
    private static final String TEST_FILE = "src/test/java/integrationTest/SR34/NULSpecialChar2.log";
    private static final String DOUBLE_NULL_FILE = "src/test/java/integrationTest/SR34/NULSpecialChar3.log";
    InputStreamReader inputStreamReader;

    @Before
    public void setUp() throws FileNotFoundException, UnsupportedEncodingException {
        inputStreamReader = new InputStreamReader(new FileInputStream(TEST_FILE), "UTF-8");
    }

    @Test
    public void defaultReaderInterpetsNullCorrectly() throws IOException {
        CSVReaderBuilder builder = new CSVReaderBuilder(inputStreamReader);
        CSVReader csvReader = builder.build();

        String[] line = csvReader.readNext();
        assertEquals(6, line.length);
        assertEquals("10", line[0]);
        assertEquals("IBM", line[1]);
        assertEquals("2015063", line[2]);
        assertEquals("064230733910", line[3]);
        assertEquals("\0", line[4]);
        assertEquals("01 ", line[5]);
    }

    @Test
    public void doubleBufferedReaderInterpetsNullCorrectly() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        CSVReaderBuilder builder = new CSVReaderBuilder(bufferedReader);
        CSVReader csvReader = builder.build();

        String[] line = csvReader.readNext();
        assertEquals(6, line.length);
        assertEquals("10", line[0]);
        assertEquals("IBM", line[1]);
        assertEquals("2015063", line[2]);
        assertEquals("064230733910", line[3]);
        assertEquals("\0", line[4]);
        assertEquals("01 ", line[5]);
    }

    @Test
    public void usingNullAsDelimeterWillFailBecauseYouAreEscapingTheQuote() throws IOException {
        BufferedReader bufferedStringReader1 = new BufferedReader(new InputStreamReader(new FileInputStream(TEST_FILE), "UTF-8"));
        CSVReader reader1 = new CSVReader(bufferedStringReader1, ICSVParser.DEFAULT_SEPARATOR, ICSVParser.DEFAULT_QUOTE_CHARACTER, '\0');

        List<String[]> rawTokens1;
        rawTokens1 = reader1.readAll();

        assertEquals(1, rawTokens1.size());

        String[] line = rawTokens1.get(0);
        assertEquals(4, line.length);
        assertEquals("10", line[0]);
        assertEquals("IBM", line[1]);
        assertEquals("2015063", line[2]);
        assertEquals("064230733910", line[3]);
    }

    @Test
    public void youNeedToEscapeTheNullCharactersIfUsingNullAsEscape() throws IOException {
        BufferedReader bufferedStringReader1 = new BufferedReader(new InputStreamReader(new FileInputStream(DOUBLE_NULL_FILE), "UTF-8"));
        CSVReader reader1 = new CSVReader(bufferedStringReader1, ICSVParser.DEFAULT_SEPARATOR, ICSVParser.DEFAULT_QUOTE_CHARACTER, '\0');

        List<String[]> rawTokens1;
        rawTokens1 = reader1.readAll();

        assertEquals(1, rawTokens1.size());

        String[] line = rawTokens1.get(0);
        assertEquals(6, line.length);
        assertEquals("10", line[0]);
        assertEquals("IBM", line[1]);
        assertEquals("2015063", line[2]);
        assertEquals("064230733910", line[3]);
        assertEquals("\0", line[4]);
        assertEquals("01 ", line[5]);
    }
}
