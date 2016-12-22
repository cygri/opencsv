package com.opencsv;

import com.opencsv.enums.CSVReaderNullFieldIndicator;

import java.io.IOException;

import static com.opencsv.enums.CSVReaderNullFieldIndicator.NEITHER;

/**
 * Created by scott on 6/26/16.
 */
public interface ICSVParser {
    /**
     * The default separator to use if none is supplied to the constructor.
     */
    char DEFAULT_SEPARATOR = ',';
    /**
     * The average size of a line read by opencsv (used for setting the size of StringBuilders).
     */
    int INITIAL_READ_SIZE = 1024;
    /**
     * In most cases we know the size of the line we want to read.  In that case we will set the initial read
     * to that plus an buffer size.
     */
    int READ_BUFFER_SIZE = 128;
    /**
     * The default quote character to use if none is supplied to the
     * constructor.
     */
    char DEFAULT_QUOTE_CHARACTER = '"';
    /**
     * The default escape character to use if none is supplied to the
     * constructor.
     */
    char DEFAULT_ESCAPE_CHARACTER = '\\';
    /**
     * The default strict quote behavior to use if none is supplied to the
     * constructor.
     */
    boolean DEFAULT_STRICT_QUOTES = false;
    /**
     * The default leading whitespace behavior to use if none is supplied to the
     * constructor.
     */
    boolean DEFAULT_IGNORE_LEADING_WHITESPACE = true;
    /**
     * If the quote character is set to null then there is no quote character.
     */
    boolean DEFAULT_IGNORE_QUOTATIONS = false;
    /**
     * This is the "null" character - if a value is set to this then it is ignored.
     */
    char NULL_CHARACTER = '\0';
    /**
     * Multi-line fields with more than this number of lines are considered an
     * error. This is to fail faster in the case of a stray unmatched quote in
     * a very large file. 
     */
    int MAX_LINES_IN_MULTILINE = 10000;
    /**
     * Denotes what field contents will cause the parser to return null:  EMPTY_SEPARATORS, EMPTY_QUOTES, BOTH, NEITHER (default)
     */
    CSVReaderNullFieldIndicator DEFAULT_NULL_FIELD_INDICATOR = NEITHER;

    /**
     * @return The default separator for this parser.
     */
    char getSeparator();

    /**
     * @return The default quotation character for this parser.
     */
    char getQuotechar();

    /**
     * @return True if something was left over from last call(s)
     */
    boolean isPending();

    /**
     * Parses an incoming String and returns an array of elements.
     * This method is used when the data spans multiple lines.
     *
     * @param nextLine Current line to be processed
     * @return The comma-tokenized list of elements, or null if nextLine is null
     * @throws IOException If bad things happen during the read
     */
    String[] parseLineMulti(String nextLine) throws IOException;

    /**
     * Parses an incoming String and returns an array of elements.
     * This method is used when all data is contained in a single line.
     *
     * @param nextLine Line to be parsed.
     * @return The list of elements, or null if nextLine is null
     * @throws IOException If bad things happen during the read
     */
    String[] parseLine(String nextLine) throws IOException;

    /**
     * @return The null field indicator.
     */
    CSVReaderNullFieldIndicator nullFieldIndicator();
}
