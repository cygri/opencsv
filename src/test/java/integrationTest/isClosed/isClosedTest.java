package integrationTest.isClosed;

import com.opencsv.CSVReader;
import org.junit.Test;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * I created this class to test out all the issues that is being seen with the isClosed
 * that was introduced in the CSVReader for version 3.
 * <p>Created by scott on 7/28/15.</p>
 */
public class isClosedTest {

    private static final String ONE_ROW = "col\tcol\tcol\tcol\tcol\tcol\tcol\r\n"; // 10byte
    private static final int ROWS = 10001;

    @Test
    public void issue115StandardReadFailsAfterALargeNumberOfReads() throws IOException {
        StringBuilder all = new StringBuilder();
        for (int i = 0; i < ROWS; i++) {
            all.append(ONE_ROW);
        }

        Reader reader = new StringReader(all.toString());
        CSVReader csvr = new CSVReader(reader, '\t');
        List<String[]> rows = csvr.readAll();
        csvr.close();

        assertEquals(ROWS, rows.size());
    }
}
