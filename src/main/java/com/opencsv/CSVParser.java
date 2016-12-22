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

import com.opencsv.enums.CSVReaderNullFieldIndicator;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A very simple CSV parser released under a commercial-friendly license.
 * This just implements splitting a single line into fields.
 *
 * The purpose of the CSVParser is to take a single string and parse it into
 * its elements based on the delimiter, quote and escape characters.
 *
 * The CSVParser has grown organically based on user requests and does not truely match
 * any current requirements (though it can be configured to match or come close).  There
 * is no plans to change this as it will break existing requirements.  Consider using
 * the RFC4180Parser for less configurablility but closer match to the RFC4180 requirements.
 *
 * @author Glen Smith
 * @author Rainer Pruy
 */
public class CSVParser implements ICSVParser {

    /**
     * This is the character that the CSVParser will treat as the separator.
     */
    private final char separator;
    /**
     * This is the character that the CSVParser will treat as the quotation character.
     */
    private final char quotechar;
    /**
     * This is the character that the CSVParser will treat as the escape character.
     */
    private final char escape;
    /**
     * Determines if the field is between quotes (true) or between separators (false).
     */
    private final boolean strictQuotes;
    /**
     * Ignore any leading white space at the start of the field.
     */
    private final boolean ignoreLeadingWhiteSpace;
    /**
     * Skip over quotation characters when parsing.
     */
    private final boolean ignoreQuotations;
    private final CSVReaderNullFieldIndicator nullFieldIndicator;
    private StringBuilder pending;
    private boolean inField = false;
    private int multiLineCount = 0;

    /**
     * Constructs CSVParser using a comma for the separator.
     */
    public CSVParser() {
        this(DEFAULT_SEPARATOR, DEFAULT_QUOTE_CHARACTER, DEFAULT_ESCAPE_CHARACTER);
    }

    /**
     * Constructs CSVParser with supplied separator.
     *
     * @param separator The delimiter to use for separating entries.
     */
    public CSVParser(char separator) {
        this(separator, DEFAULT_QUOTE_CHARACTER, DEFAULT_ESCAPE_CHARACTER);
    }


    /**
     * Constructs CSVParser with supplied separator and quote char.
     *
     * @param separator The delimiter to use for separating entries
     * @param quotechar The character to use for quoted elements
     */
    public CSVParser(char separator, char quotechar) {
        this(separator, quotechar, DEFAULT_ESCAPE_CHARACTER);
    }

    /**
     * Constructs CSVParser with supplied separator and quote char.
     *
     * @param separator The delimiter to use for separating entries
     * @param quotechar The character to use for quoted elements
     * @param escape    The character to use for escaping a separator or quote
     */
    public CSVParser(char separator, char quotechar, char escape) {
        this(separator, quotechar, escape, DEFAULT_STRICT_QUOTES);
    }

    /**
     * Constructs CSVParser with supplied separator and quote char.
     * Allows setting the "strict quotes" flag.
     *
     * @param separator    The delimiter to use for separating entries
     * @param quotechar    The character to use for quoted elements
     * @param escape       The character to use for escaping a separator or quote
     * @param strictQuotes If true, characters outside the quotes are ignored
     */
    public CSVParser(char separator, char quotechar, char escape, boolean strictQuotes) {
        this(separator, quotechar, escape, strictQuotes, DEFAULT_IGNORE_LEADING_WHITESPACE);
    }

    /**
     * Constructs CSVParser with supplied separator and quote char.
     * Allows setting the "strict quotes" and "ignore leading whitespace" flags.
     *
     * @param separator               The delimiter to use for separating entries
     * @param quotechar               The character to use for quoted elements
     * @param escape                  The character to use for escaping a separator or quote
     * @param strictQuotes            If true, characters outside the quotes are ignored
     * @param ignoreLeadingWhiteSpace If true, white space in front of a quote in a field is ignored
     */
    public CSVParser(char separator, char quotechar, char escape, boolean strictQuotes, boolean ignoreLeadingWhiteSpace) {
        this(separator, quotechar, escape, strictQuotes, ignoreLeadingWhiteSpace, DEFAULT_IGNORE_QUOTATIONS);
    }

    /**
     * Constructs CSVParser with supplied separator and quote char.
     * Allows setting the "strict quotes" and "ignore leading whitespace" flags.
     *
     * @param separator               The delimiter to use for separating entries
     * @param quotechar               The character to use for quoted elements
     * @param escape                  The character to use for escaping a separator or quote
     * @param strictQuotes            If true, characters outside the quotes are ignored
     * @param ignoreLeadingWhiteSpace If true, white space in front of a quote in a field is ignored
     * @param ignoreQuotations        If true, treat quotations like any other character.
     */
    public CSVParser(char separator, char quotechar, char escape, boolean strictQuotes, boolean ignoreLeadingWhiteSpace,
                     boolean ignoreQuotations) {
        this(separator, quotechar, escape, strictQuotes, ignoreLeadingWhiteSpace, ignoreQuotations, DEFAULT_NULL_FIELD_INDICATOR);
    }

    /**
     * Constructs CSVParser with supplied separator and quote char.
     * Allows setting the "strict quotes" and "ignore leading whitespace" flags.
     *
     * @param separator               The delimiter to use for separating entries
     * @param quotechar               The character to use for quoted elements
     * @param escape                  The character to use for escaping a separator or quote
     * @param strictQuotes            If true, characters outside the quotes are ignored
     * @param ignoreLeadingWhiteSpace If true, white space in front of a quote in a field is ignored
     * @param ignoreQuotations        If true, treat quotations like any other character.
     * @param nullFieldIndicator      Which field content will be returned as null: EMPTY_SEPARATORS, EMPTY_QUOTES,
     *                                BOTH, NEITHER (default)
     */
    CSVParser(char separator, char quotechar, char escape, boolean strictQuotes, boolean ignoreLeadingWhiteSpace,
              boolean ignoreQuotations, CSVReaderNullFieldIndicator nullFieldIndicator) {
        if (anyCharactersAreTheSame(separator, quotechar, escape)) {
            throw new UnsupportedOperationException("The separator, quote, and escape characters must be different!");
        }
        if (separator == NULL_CHARACTER) {
            throw new UnsupportedOperationException("The separator character must be defined!");
        }
        this.separator = separator;
        this.quotechar = quotechar;
        this.escape = escape;
        this.strictQuotes = strictQuotes;
        this.ignoreLeadingWhiteSpace = ignoreLeadingWhiteSpace;
        this.ignoreQuotations = ignoreQuotations;
        this.nullFieldIndicator = nullFieldIndicator;
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

    /**
     * @return The default escape character for this parser.
     */
    public char getEscape() {
        return escape;
    }

    /**
     * @return The default strictQuotes setting for this parser.
     */
    public boolean isStrictQuotes() {
        return strictQuotes;
    }

    /**
     * @return The default ignoreLeadingWhiteSpace setting for this parser.
     */
    public boolean isIgnoreLeadingWhiteSpace() {
        return ignoreLeadingWhiteSpace;
    }

    /**
     * @return The default ignoreQuotation setting for this parser.
     */
    public boolean isIgnoreQuotations() {
        return ignoreQuotations;
    }

    /**
     * Checks to see if any two of the three characters are the same.
     * This is because in opencsv the separator, quote, and escape characters
     * must the different.
     *
     * @param separator The defined separator character
     * @param quotechar The defined quotation cahracter
     * @param escape    The defined escape character
     * @return True if any two of the three are the same.
     */
    private boolean anyCharactersAreTheSame(char separator, char quotechar, char escape) {
        return isSameCharacter(separator, quotechar) || isSameCharacter(separator, escape) || isSameCharacter(quotechar, escape);
    }

    /**
     * Checks that the two characters are the same and are not the defined NULL_CHARACTER.
     * @param c1 First character
     * @param c2 Second character
     * @return True if both characters are the same and are not the defined NULL_CHARACTER
     */
    private boolean isSameCharacter(char c1, char c2) {
        return c1 != NULL_CHARACTER && c1 == c2;
    }

    /**
     * @return True if something was left over from last call(s)
     */
    @Override
    public boolean isPending() {
        return pending != null;
    }

    /**
     * Parses an incoming String and returns an array of elements.
     * This method is used when the data spans multiple lines.
     *
     * @param nextLine Current line to be processed
     * @return The comma-tokenized list of elements, or null if nextLine is null
     * @throws IOException If bad things happen during the read
     */
    @Override
    public String[] parseLineMulti(String nextLine) throws IOException {
        return parseLine(nextLine, true);
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
        return parseLine(nextLine, false);
    }

    /**
     * Parses an incoming String and returns an array of elements.
     *
     * @param nextLine The string to parse
     * @param multi Does it take multiple lines to form a single record.
     * @return The list of elements, or null if nextLine is null
     * @throws IOException If bad things happen during the read
     */
    protected String[] parseLine(String nextLine, boolean multi) throws IOException {

        if (!multi && pending != null) {
            pending = null;
        }

        if (nextLine == null) {
            if (pending != null) {
                String[] result = new String[]{pending.toString()};
                pending = null;
                return result;
            }
            return null;
        }

        List<String> tokensOnThisLine = new ArrayList<String>();
        boolean inQuotes = false;
        boolean fromQuotedField = false;
        StringBuilder sb;
        if (pending == null) {
            sb = new StringBuilder(nextLine.length() + READ_BUFFER_SIZE);
        } else {
            sb = pending;
            pending = null;
            inQuotes = !this.ignoreQuotations;
        }
        for (int i = 0; i < nextLine.length(); i++) {

            char c = nextLine.charAt(i);
            if (c == this.escape) {
                if (isNextCharacterEscapable(nextLine, inQuotes(inQuotes), i)) {
                    i = appendNextCharacterAndAdvanceLoop(nextLine, sb, i);
                }
            } else if (c == quotechar) {
                if (isNextCharacterEscapedQuote(nextLine, inQuotes(inQuotes), i)) {
                    i = appendNextCharacterAndAdvanceLoop(nextLine, sb, i);
                } else {

                    inQuotes = !inQuotes;
                    if (atStartOfField(sb)) {
                        fromQuotedField = true;
                    }

                    // the tricky case of an embedded quote in the middle: a,bc"d"ef,g
                    if (!strictQuotes) {
                        if (i > 2 //not on the beginning of the line
                                && nextLine.charAt(i - 1) != this.separator //not at the beginning of an escape sequence
                                && nextLine.length() > (i + 1) &&
                                nextLine.charAt(i + 1) != this.separator //not at the	end of an escape sequence
                                ) {

                            if (ignoreLeadingWhiteSpace && sb.length() > 0 && StringUtils.isWhitespace(sb)) {
                                sb.setLength(0);
                            } else {
                                sb.append(c);
                            }

                        }
                    }
                }
                inField = !inField;
            } else if (c == separator && !(inQuotes && !ignoreQuotations)) {
                tokensOnThisLine.add(convertEmptyToNullIfNeeded(sb.toString(), fromQuotedField));
                fromQuotedField = false;
                sb.setLength(0);
                inField = false;
            } else {
                if (!strictQuotes || (inQuotes && !ignoreQuotations)) {
                    sb.append(c);
                    inField = true;
                    fromQuotedField = true;
                }
            }

        }
        // line is done - check status
        if (inQuotes && !ignoreQuotations) {
            if (multi) {
                // continuing a quoted section, re-append newline
                sb.append('\n');
                pending = sb;
                sb = null; // this partial content is not to be added to field list yet
                multiLineCount++;
                if (multiLineCount > MAX_LINES_IN_MULTILINE) {
                    throw new IOException(
                        "Multi-line field with more than " + MAX_LINES_IN_MULTILINE + 
                        " lines. Possible stray quote?");
                }
            } else {
                throw new IOException("Un-terminated quoted field at end of CSV line");
            }
            if (inField) {
                fromQuotedField = true;
            }
        } else {
            multiLineCount = 0;
            inField = false;
        }

        if (sb != null) {
            tokensOnThisLine.add(convertEmptyToNullIfNeeded(sb.toString(), fromQuotedField));
        }
        return tokensOnThisLine.toArray(new String[tokensOnThisLine.size()]);

    }

    private boolean atStartOfField(StringBuilder sb) {
        return sb.length() == 0;
    }

    private String convertEmptyToNullIfNeeded(String s, boolean fromQuotedField) {
        if (s.isEmpty() && shouldConvertEmptyToNull(fromQuotedField)) {
            return null;
        }
        return s;
    }

    private boolean shouldConvertEmptyToNull(boolean fromQuotedField) {
        switch (nullFieldIndicator) {
            case BOTH:
                return true;
            case EMPTY_SEPARATORS:
                return !fromQuotedField;
            case EMPTY_QUOTES:
                return fromQuotedField;
            default:
                return false;
        }
    }

    /**
     * Appends the next character in the line to the string buffer.
     *
     * @param line Line to process
     * @param sb Contains the processed character
     * @param i Current position in the line.
     * @return New position in the line.
     */
    private int appendNextCharacterAndAdvanceLoop(String line, StringBuilder sb, int i) {
        sb.append(line.charAt(i + 1));
        i++;
        return i;
    }

    /**
     * Determines if we can process as if we were in quotes.
     *
     * @param inQuotes Are we currently in quotes.
     * @return True if we should process as if we are inside quotes.
     */
    private boolean inQuotes(boolean inQuotes) {
        return (inQuotes && !ignoreQuotations) || inField;
    }

    /**
     * Checks to see if the character after the index is a quotation character.
     *
     * Precondition: the current character is a quote or an escape.
     *
     * @param nextLine The current line
     * @param inQuotes True if the current context is quoted
     * @param i        Current index in line
     * @return True if the following character is a quote
     */
    private boolean isNextCharacterEscapedQuote(String nextLine, boolean inQuotes, int i) {
        return inQuotes  // we are in quotes, therefore there can be escaped quotes in here.
                && nextLine.length() > (i + 1)  // there is indeed another character to check.
                && isCharacterQuoteCharacter(nextLine.charAt(i + 1));
    }

    /**
     * Checks to see if the passed in character is the defined quotation character.
     *
     * @param c Source character
     * @return True if c is the defined quotation character
     */
    private boolean isCharacterQuoteCharacter(char c) {
        return c == quotechar;
    }

    /**
     * Checks to see if the character is the defined escape character.
     *
     * @param c Source character
     * @return True if the character is the defined escape character
     */
    private boolean isCharacterEscapeCharacter(char c) {
        return c == escape;
    }

    /**
     * Checks to see if the character passed in could be escapable.
     * Escapable characters for opencsv are the quotation character or the
     * escape character.
     *
     * @param c Source character
     * @return True if the character could be escapable.
     */
    private boolean isCharacterEscapable(char c) {
        return isCharacterQuoteCharacter(c) || isCharacterEscapeCharacter(c);
    }

    /**
     * Checks to see if the character after the current index in a String is an
     * escapable character.
     * Meaning the next character is either a quotation character or the escape
     * char and you are inside quotes.
     *
     * Precondition: the current character is an escape.
     *
     * @param nextLine The current line
     * @param inQuotes True if the current context is quoted
     * @param i        Current index in line
     * @return True if the following character is a quote
     */
    protected boolean isNextCharacterEscapable(String nextLine, boolean inQuotes, int i) {
        return inQuotes  // we are in quotes, therefore there can be escaped quotes in here.
                && nextLine.length() > (i + 1)  // there is indeed another character to check.
                && isCharacterEscapable(nextLine.charAt(i + 1));
    }

    /**
     * @return The null field indicator.
     */
    @Override
    public CSVReaderNullFieldIndicator nullFieldIndicator() {
        return nullFieldIndicator;
    }
}
