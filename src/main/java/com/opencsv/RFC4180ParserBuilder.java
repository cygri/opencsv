package com.opencsv;


import com.opencsv.enums.CSVReaderNullFieldIndicator;

/**
 * Builder for creating a RFC4180Parser.
 * <p>Example code for using this class:<br><br>
 * <code>
 * final RFC4180Parser parser =<br>
 * new RFC4180ParserBuilder()<br>
 * .withSeparator('\t')<br>
 * .build();<br>
 * </code></p>
 *
 * @see RFC4180Parser
 */
public class RFC4180ParserBuilder {

    private char separator = ICSVParser.DEFAULT_SEPARATOR;
    private char quoteChar = ICSVParser.DEFAULT_QUOTE_CHARACTER;
    private char escapeChar = ICSVParser.DEFAULT_ESCAPE_CHARACTER;
    private boolean strictQuotes = ICSVParser.DEFAULT_STRICT_QUOTES;
    private boolean ignoreLeadingWhiteSpace = ICSVParser.DEFAULT_IGNORE_LEADING_WHITESPACE;
    private boolean ignoreQuotations = ICSVParser.DEFAULT_IGNORE_QUOTATIONS;
    private CSVReaderNullFieldIndicator nullFieldIndicator = CSVReaderNullFieldIndicator.NEITHER;

    /**
     * default constructor
     */
    public RFC4180ParserBuilder() {
    }

    /**
     * @return The defined separator.
     */
    public char getSeparator() {
        return separator;
    }

    /**
     * @return The defined quotation character.
     */
    public char getQuoteChar() {
        return quoteChar;
    }

    /**
     * @return The defined escape character.
     */
    public char getEscapeChar() {
        return escapeChar;
    }

    /**
     * @return The defined strict quotation setting.
     */
    public boolean isStrictQuotes() {
        return strictQuotes;
    }

    /**
     * @return The defined ignoreLeadingWhiteSpace setting.
     */
    public boolean isIgnoreLeadingWhiteSpace() {
        return ignoreLeadingWhiteSpace;
    }

    /**
     * @return The defined ignoreQuotation setting.
     */
    public boolean isIgnoreQuotations() {
        return ignoreQuotations;
    }

    /**
     * @return The null field indicator.
     */
    public CSVReaderNullFieldIndicator nullFieldIndicator() {
        return nullFieldIndicator;
    }

    /**
     * Constructs RFC4180Parser.
     *
     * @return A new RFC4180Parser with defined settings.
     */
    public RFC4180Parser build() {

        return new RFC4180Parser(quoteChar, separator);

    }

    /**
     * Sets the delimiter to use for separating entries.
     *
     * @param separator The delimiter to use for separating entries
     * @return The RFC4180ParserBuilder
     */
    public RFC4180ParserBuilder withSeparator(
            final char separator) {
        this.separator = separator;
        return this;
    }


    /**
     * Sets the character to use for quoted elements.
     *
     * @param quoteChar The character to use for quoted element.
     * @return The RFC4180ParserBuilder
     */
    public RFC4180ParserBuilder withQuoteChar(
            final char quoteChar) {
        this.quoteChar = quoteChar;
        return this;
    }

}
