package com.opencsv

import spock.lang.Specification

class CSVParserBuilderSpec extends Specification {
    def 'builder creates a CSVParser by default'() {
        given:
        CSVParserBuilder builder = new CSVParserBuilder()

        when:
        ICSVParser parser = builder.build()

        then:
        parser instanceof CSVParser
    }


    def 'default RFC4180Parser has proper values'() {
        given:
        CSVParserBuilder builder = new CSVParserBuilder()

        when:
        CSVParser parser = builder.build()

        then:
        parser.getQuotechar() == ICSVParser.DEFAULT_QUOTE_CHARACTER;
        parser.getSeparator() == ICSVParser.DEFAULT_SEPARATOR;
    }
}
