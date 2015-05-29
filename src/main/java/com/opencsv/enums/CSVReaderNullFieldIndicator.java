package com.opencsv.enums;

/**
 * Enumeration used to tell the CSVParser what to consider null.
 * <p/>
 * EMPTY - two sequential separators are null.
 * EMPTY_DELIMITED - two sequential quotes are null
 * BOTH - both are null
 * NEITHER - default.  Both are considered empty string.
 */
public enum CSVReaderNullFieldIndicator {
    EMPTY,
    EMPTY_DELIMITED,
    BOTH,
    NEITHER;
}
