package com.opencsv;

/*
 Copyright 2005 Bytecode Pty Ltd.

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

import com.opencsv.stream.reader.LineReader;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A very simple CSV reader released under a commercial-friendly license.
 *
 * @author Glen Smith
 */
public class CSVReader implements Closeable, Iterable<String[]> {

    public static final boolean DEFAULT_KEEP_CR = false;
    public static final boolean DEFAULT_VERIFY_READER = true;
    /**
     * The default line to start reading.
     */
    public static final int DEFAULT_SKIP_LINES = 0;
    public static final int READ_AHEAD_LIMIT = Character.SIZE / Byte.SIZE;
    protected ICSVParser parser;
    protected int skipLines;
    protected BufferedReader br;
    protected LineReader lineReader;
    protected boolean hasNext = true;
    protected boolean linesSkiped;
    protected boolean keepCR;
    protected boolean verifyReader;

    protected long linesRead = 0;
    protected long recordsRead = 0;

    /**
     * Constructs CSVReader using a comma for the separator.
     *
     * @param reader The reader to an underlying CSV source.
     */
    public CSVReader(Reader reader) {
        this(reader, ICSVParser.DEFAULT_SEPARATOR, ICSVParser.DEFAULT_QUOTE_CHARACTER, ICSVParser.DEFAULT_ESCAPE_CHARACTER);
    }

    /**
     * Constructs CSVReader with supplied separator.
     *
     * @param reader    The reader to an underlying CSV source.
     * @param separator The delimiter to use for separating entries.
     */
    public CSVReader(Reader reader, char separator) {
        this(reader, separator, ICSVParser.DEFAULT_QUOTE_CHARACTER, ICSVParser.DEFAULT_ESCAPE_CHARACTER);
    }

    /**
     * Constructs CSVReader with supplied separator and quote char.
     *
     * @param reader    The reader to an underlying CSV source.
     * @param separator The delimiter to use for separating entries
     * @param quotechar The character to use for quoted elements
     */
    public CSVReader(Reader reader, char separator, char quotechar) {
        this(reader, separator, quotechar, ICSVParser.DEFAULT_ESCAPE_CHARACTER, DEFAULT_SKIP_LINES, ICSVParser.DEFAULT_STRICT_QUOTES);
    }

    /**
     * Constructs CSVReader with supplied separator, quote char, and quote handling
     * behavior.
     *
     * @param reader       The reader to an underlying CSV source.
     * @param separator    The delimiter to use for separating entries
     * @param quotechar    The character to use for quoted elements
     * @param strictQuotes Sets if characters outside the quotes are ignored
     */
    public CSVReader(Reader reader, char separator, char quotechar, boolean strictQuotes) {
        this(reader, separator, quotechar, ICSVParser.DEFAULT_ESCAPE_CHARACTER, DEFAULT_SKIP_LINES, strictQuotes);
    }

    /**
     * Constructs CSVReader.
     *
     * @param reader    The reader to an underlying CSV source.
     * @param separator The delimiter to use for separating entries
     * @param quotechar The character to use for quoted elements
     * @param escape    The character to use for escaping a separator or quote
     */

    public CSVReader(Reader reader, char separator,
                     char quotechar, char escape) {
        this(reader, separator, quotechar, escape, DEFAULT_SKIP_LINES, ICSVParser.DEFAULT_STRICT_QUOTES);
    }

    /**
     * Constructs CSVReader.
     *
     * @param reader    The reader to an underlying CSV source.
     * @param separator The delimiter to use for separating entries
     * @param quotechar The character to use for quoted elements
     * @param line      The number of lines to skip before reading
     */
    public CSVReader(Reader reader, char separator, char quotechar, int line) {
        this(reader, separator, quotechar, ICSVParser.DEFAULT_ESCAPE_CHARACTER, line, ICSVParser.DEFAULT_STRICT_QUOTES);
    }

    /**
     * Constructs CSVReader.
     *
     * @param reader    The reader to an underlying CSV source.
     * @param separator The delimiter to use for separating entries
     * @param quotechar The character to use for quoted elements
     * @param escape    The character to use for escaping a separator or quote
     * @param line      The number of lines to skip before reading
     */
    public CSVReader(Reader reader, char separator, char quotechar, char escape, int line) {
        this(reader, separator, quotechar, escape, line, ICSVParser.DEFAULT_STRICT_QUOTES);
    }

    /**
     * Constructs CSVReader.
     *
     * @param reader       The reader to an underlying CSV source.
     * @param separator    The delimiter to use for separating entries
     * @param quotechar    The character to use for quoted elements
     * @param escape       The character to use for escaping a separator or quote
     * @param line      The number of lines to skip before reading
     * @param strictQuotes Sets if characters outside the quotes are ignored
     */
    public CSVReader(Reader reader, char separator, char quotechar, char escape, int line, boolean strictQuotes) {
        this(reader, separator, quotechar, escape, line, strictQuotes, ICSVParser.DEFAULT_IGNORE_LEADING_WHITESPACE);
    }

    /**
     * Constructs CSVReader with all data entered.
     *
     * @param reader                  The reader to an underlying CSV source.
     * @param separator               The delimiter to use for separating entries
     * @param quotechar               The character to use for quoted elements
     * @param escape                  The character to use for escaping a separator or quote
     * @param line                    The number of lines to skip before reading
     * @param strictQuotes            Sets if characters outside the quotes are ignored
     * @param ignoreLeadingWhiteSpace If true, parser should ignore white space before a quote in a field
     */
    public CSVReader(Reader reader, char separator, char quotechar, char escape, int line, boolean strictQuotes, boolean ignoreLeadingWhiteSpace) {
        this(reader,
                line,
                new CSVParser(separator, quotechar, escape, strictQuotes, ignoreLeadingWhiteSpace));
    }

    /**
     * Constructs CSVReader with all data entered.
     *
     * @param reader                  The reader to an underlying CSV source.
     * @param separator               The delimiter to use for separating entries
     * @param quotechar               The character to use for quoted elements
     * @param escape                  The character to use for escaping a separator or quote
     * @param line                    The number of lines to skip before reading
     * @param strictQuotes            Sets if characters outside the quotes are ignored
     * @param ignoreLeadingWhiteSpace If true, parser should ignore white space before a quote in a field
     * @param keepCR                  If true the reader will keep carriage returns, otherwise it will discard them.
     */
    public CSVReader(Reader reader, char separator, char quotechar, char escape, int line, boolean strictQuotes,
                     boolean ignoreLeadingWhiteSpace, boolean keepCR) {
        this(reader, line,
                new CSVParser(separator, quotechar, escape, strictQuotes, ignoreLeadingWhiteSpace), keepCR, DEFAULT_VERIFY_READER);
    }

    /**
     * Constructs CSVReader with supplied CSVParser.
     *
     * @param reader    The reader to an underlying CSV source.
     * @param line      The number of lines to skip before reading
     * @param icsvParser The parser to use to parse input
     */
    public CSVReader(Reader reader, int line, ICSVParser icsvParser) {
        this(reader, line, icsvParser, DEFAULT_KEEP_CR, DEFAULT_VERIFY_READER);
    }

    /**
     * Constructs CSVReader with supplied CSVParser.
     *
     * @param reader    The reader to an underlying CSV source.
     * @param line      The number of lines to skip before reading
     * @param icsvParser The parser to use to parse input
     * @param keepCR    True to keep carriage returns in data read, false otherwise
     * @param verifyReader   True to verify reader before each read, false otherwise
     */
    CSVReader(Reader reader, int line, ICSVParser icsvParser, boolean keepCR, boolean verifyReader) {
        this.br =
                (reader instanceof BufferedReader ?
                        (BufferedReader) reader :
                        new BufferedReader(reader));
        this.lineReader = new LineReader(br, keepCR);
        this.skipLines = line;
        this.parser = icsvParser;
        this.keepCR = keepCR;
        this.verifyReader = verifyReader;
    }
    /**
     * @return The CSVParser used by the reader.
     */
    public ICSVParser getParser() {
        return parser;
    }

    /**
     * Returns the number of lines in the CSV file to skip before processing.
     * This is useful when there are miscellaneous data at the beginning of a file.
     *
     * @return The number of lines in the CSV file to skip before processing.
     */
    public int getSkipLines() {
        return skipLines;
    }

    /**
     * Returns if the reader will keep carriage returns found in data or remove them.
     *
     * @return True if reader will keep carriage returns, false otherwise.
     */
    public boolean keepCarriageReturns() {
        return keepCR;
    }

    /**
     * Reads the entire file into a List with each element being a String[] of
     * tokens.
     *
     * @return A List of String[], with each String[] representing a line of the
     * file.
     * @throws IOException If bad things happen during the read
     */
    public List<String[]> readAll() throws IOException {

        List<String[]> allElements = new ArrayList<String[]>();
        while (hasNext) {
            String[] nextLineAsTokens = readNext();
            if (nextLineAsTokens != null) {
                allElements.add(nextLineAsTokens);
            }
        }
        return allElements;

    }

    /**
     * Reads the next line from the buffer and converts to a string array.
     *
     * @return A string array with each comma-separated element as a separate
     * entry.
     * @throws IOException If bad things happen during the read
     */
    public String[] readNext() throws IOException {

        String[] result = null;
        do {
            String nextLine = getNextLine();
            if (!hasNext) {
                if (parser.isPending()) {
                    throw new IOException("Un-terminated quoted field at end of CSV file");
                }
                return validateResult(result);
            }
            String[] r = parser.parseLineMulti(nextLine);
            if (r.length > 0) {
                if (result == null) {
                    result = r;
                } else {
                    result = combineResultsFromMultipleReads(result, r);
                }
            }
        } while (parser.isPending());
        return validateResult(result);
    }

    /**
     * Increments the number of records read if the result passed in is not null.
     *
     * @param result The result of the read operation
     * @return Result that was passed in.
     */
    protected String[] validateResult(String[] result) {
        if (result != null) {
            recordsRead++;
        }
        return result;
    }

    /**
     * For multi-line records this method combines the current result with the result from previous read(s).
     * @param buffer Previous data read for this record
     * @param lastRead Latest data read for this record.
     * @return String array with union of the buffer and lastRead arrays.
     */
    protected String[] combineResultsFromMultipleReads(String[] buffer, String[] lastRead) {
        String[] t = new String[buffer.length + lastRead.length];
        System.arraycopy(buffer, 0, t, 0, buffer.length);
        System.arraycopy(lastRead, 0, t, buffer.length, lastRead.length);
        return t;
    }

    /**
     * Reads the next line from the file.
     *
     * @return The next line from the file without trailing newline
     * @throws IOException If bad things happen during the read
     */
    protected String getNextLine() throws IOException {
        if (isClosed()) {
            hasNext = false;
            return null;
        }

        if (!this.linesSkiped) {
            for (int i = 0; i < skipLines; i++) {
                lineReader.readLine();
                linesRead++;
            }
            this.linesSkiped = true;
        }
        String nextLine = lineReader.readLine();
        if (nextLine == null) {
            hasNext = false;
        } else {
            linesRead++;
        }

        return hasNext ? nextLine : null;
    }

    /**
     * Checks to see if the file is closed.
     * @return True if the reader can no longer be read from.
     */
    protected boolean isClosed() {
        if (!verifyReader) {
            return false;
        }
        try {
            br.mark(READ_AHEAD_LIMIT);
            int nextByte = br.read();
            br.reset(); // resets stream position, possible because its buffered
            return nextByte == -1; // read() returns -1 at end of stream
        } catch (IOException e) {
            return true;
        }
    }

    /**
     * Closes the underlying reader.
     *
     * @throws IOException If the close fails
     */
    @Override
    public void close() throws IOException {
        br.close();
    }

    /**
     * Creates an Iterator for processing the CSV data.
     * @return A String[] iterator.
     */
    @Override
    public Iterator<String[]> iterator() {
        try {
            return new CSVIterator(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns if the CSVReader will verify the reader before each read.
     * <p>
     * By default the value is true, which is the functionality for version 3.0.
     * If set to false the reader is always assumed ready to read - this is the functionality
     * for version 2.4 and before.
     * </p>
     * <p>
     * The reason this method was needed was that certain types of readers would return
     * false for their ready() methods until a read was done (namely readers created using Channels).
     * This caused opencsv not to read from those readers.
     * </p>
     *
     * @return True if CSVReader will verify the reader before reads.  False otherwise.
     * @see <a href="https://sourceforge.net/p/opencsv/bugs/108/">Bug 108</a>
     * @since 3.3
     */
    public boolean verifyReader() {
        return this.verifyReader;
    }

    /**
     * Used for debugging purposes, this method returns the number of lines that
     * has been read from the reader passed into the CSVReader.
     * <p>
     * Given the following data:</p>
     * <pre>
     * First line in the file
     * some other descriptive line
     * a,b,c
     *
     * a,"b\nb",c
     * </pre>
     * <p>
     * With a CSVReader constructed like so:<br>
     * <code>
     * CSVReader c = builder.withCSVParser(new CSVParser())<br>
     *                      .withSkipLines(2)<br>
     *                      .build();<br>
     * </code><br>
     * The initial call to getLinesRead() will be 0. After the first call to
     * readNext() then getLinesRead() will return 3 (because header was read).
     * After the second call to read the blank line then getLinesRead() will
     * return 4 (still a read). After third call to readNext() getLinesRead()
     * will return 6 because it took two line reads to retrieve this record.
     * Subsequent calls to readNext() (since we are out of data) will not
     * increment the number of lines read.</p>
     * <p>
     * An example of this is in the linesAndRecordsRead() test in CSVReaderTest.
     * </p>
     *
     * @return The number of lines read by the reader (including skip lines).
     * @see <a href="https://sourceforge.net/p/opencsv/feature-requests/73/">Feature Request 73</a>
     * @since 3.6
     */
    public long getLinesRead() {
        return linesRead;
    }

    /**
     * Used for debugging purposes, this method returns the number of records
     * that has been read from the CSVReader.
     * <p>
     * Given the following data:</p>
     * <pre>
     * First line in the file
     * some other descriptive line
     * a,b,c
     * a,"b\nb",c
     * </pre><p>
     * With a CSVReader constructed like so:<br>
     * <code>
     * CSVReader c = builder.withCSVParser(new CSVParser())<br>
     *                      .withSkipLines(2)<br>
     *                      .build();<br>
     * </code><br>
     * The initial call to getRecordsRead() will be 0. After the first call to
     * readNext() then getRecordsRead() will return 1. After the second call to
     * read the blank line then getRecordsRead() will return 2 (a blank line is
     * considered a record with one empty field). After third call to readNext()
     * getRecordsRead() will return 3 because even though it reads to retrieve
     * this record, it is still a single record read. Subsequent calls to
     * readNext() (since we are out of data) will not increment the number of
     * records read.
     * </p>
     * <p>
     * An example of this is in the linesAndRecordsRead() test in CSVReaderTest.
     * </p>
     *
     * @return The number of records (array of Strings[]) read by the reader.
     * @see <a href="https://sourceforge.net/p/opencsv/feature-requests/73/">Feature Request 73</a>
     * @since 3.6
     */
    public long getRecordsRead() {
        return recordsRead;
    }
}
