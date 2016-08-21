package com.opencsv;

import com.opencsv.enums.CSVReaderNullFieldIndicator;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This Parser is meant to parse according to the RFC4180 specification.
 * <p>
 * Since it shares the same interface with the CSVParser there are methods here that will do nothing.
 * For example the RFC4180 specification does not have an concept of an escape character so the getEscape method
 * will return char 0.  The methods that are not supported are noted in the JavaDocs.
 * <p>
 * Another departure from the CSVParser is that there is only two constructors and only one is available publicly.
 * The intent is that if you want to create anything other than a default RFC4180Parser you should use the
 * CSVParserBuilder.  This way the code will not become cluttered with constructors as the CSVParser did.
 * <p>
 * Examples:
 * <p>
 * ICSVParser parser = new RFC4180Parser();
 * <p>
 * or
 * <p>
 * CSVParserBuilder builder = new CSVParserBuilder()
 * ICSVParser parser = builder.withParserType(ParserType.RFC4180Parser).build()
 *
 * @author Scott Conway
 */

public class RFC4180Parser implements ICSVParser {
    /**
     * This is the character that the RFC4180Parser will treat as the separator.
     */
    private final char separator;
    /**
     * This is the character that the RFC4180Parser will treat as the quotation character.
     */
    private final char quotechar;

    /**
     * Default constructor for the RFC4180Parser.  Uses values from the ICSVParser
     */
    public RFC4180Parser() {
        this(ICSVParser.DEFAULT_QUOTE_CHARACTER, ICSVParser.DEFAULT_SEPARATOR);
    }

    /**
     * Constructor used by the CSVParserBuilder.
     *
     * @param separator The delimiter to use for separating entries
     * @param quoteChar The character to use for quoted elements
     */
    RFC4180Parser(char quoteChar, char separator) {
        this.quotechar = quoteChar;
        this.separator = separator;
    }

    /**
     * @return The default separator for this parser.
     */
    @Override
    public char getSeparator() {
        return separator;
    }

    /**
     * @return The default quotation character for this parser.
     */
    @Override
    public char getQuotechar() {
        return quotechar;
    }

    @Override
    public boolean isPending() {
        return false;
    }

    @Override
    public String[] parseLineMulti(String nextLine) throws IOException {
        return new String[0];
    }

    /**
     * Parses an incoming String and returns an array of elements.
     * This method is used when all data is contained in a single line.
     *
     * @param nextLine Line to be parsed.
     * @return The list of elements, or null if nextLine is null
     * @throws IOException If bad things happen during the read
     */
    @Override
    public String[] parseLine(String nextLine) throws IOException {
        String[] elements;
        if (!nextLine.contains(Character.toString(quotechar))) {
            elements = nextLine.split(Character.toString(separator));
        } else {
            elements = splitWhileNotInQuotes(nextLine);
            for (int i = 0; i < elements.length; i++) {
                elements[i] = handleQuotes(elements[i]);
            }
        }
        return elements;
    }

    private String[] splitWhileNotInQuotes(String nextLine) {
        int currentPosition = 0;
        List<String> elements = new ArrayList<String>();
        int nextSeparator;
        int nextQuote;

        while (currentPosition < nextLine.length()) {
            nextSeparator = nextLine.indexOf(separator, currentPosition);
            nextQuote = nextLine.indexOf(quotechar, currentPosition);

            if (nextSeparator == -1) {
                elements.add(nextLine.substring(currentPosition));
                currentPosition = nextLine.length();
            } else if (nextQuote == -1 || nextQuote > nextSeparator || nextQuote != currentPosition) {
                elements.add(nextLine.substring(currentPosition, nextSeparator));
                currentPosition = nextSeparator + 1;
            } else {
                int fieldEnd = findEndOfFieldFromPosition(nextLine, currentPosition);

                elements.add(fieldEnd >= nextLine.length() ? nextLine.substring(currentPosition) : nextLine.substring(currentPosition, fieldEnd));

                currentPosition = fieldEnd + 1;
            }

        }

        return elements.toArray(new String[elements.size()]);
    }

    private int findEndOfFieldFromPosition(String nextLine, int currentPosition) {
        int nextQuote = nextLine.indexOf(quotechar, currentPosition + 1);

        while (nextQuote != -1) {
            if (nextLine.charAt(nextQuote + 1) == separator) {
                return nextQuote + 1;
            }

            nextQuote = nextLine.indexOf(quotechar, nextQuote + 1);
        }

        return nextLine.length();
    }

    private String handleQuotes(String element) {
        String ret = element;
        String quoteCharString = Character.toString(getQuotechar());
        if (StringUtils.startsWith(ret, quoteCharString)) {
            ret = StringUtils.removeStart(ret, quoteCharString);

            if (StringUtils.endsWith(ret, quoteCharString)) {
                ret = StringUtils.removeEnd(ret, quoteCharString);
            }
        }
        ret = StringUtils.replace(ret, quoteCharString + quoteCharString, quoteCharString);
        return ret;
    }

    @Override
    public CSVReaderNullFieldIndicator nullFieldIndicator() {
        return null;
    }
}
