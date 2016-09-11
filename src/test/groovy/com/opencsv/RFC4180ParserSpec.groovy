package com.opencsv

import spock.lang.Specification
import spock.lang.Unroll

class RFC4180ParserSpec extends Specification {
    private static final char SINGLE_QUOTE = '\''
    private static final char PERIOD = '.'

    def 'create a parser from the default constructor'() {
        when:
        RFC4180Parser parser = new RFC4180Parser();

        then:
        parser.getQuotechar() == ICSVParser.DEFAULT_QUOTE_CHARACTER;
        parser.getSeparator() == ICSVParser.DEFAULT_SEPARATOR;
    }

    def 'able to parse a simple line'() {
        given:
        RFC4180ParserBuilder builder = new RFC4180ParserBuilder()
        RFC4180Parser parser = builder.build()
        String testLine = "This,is,a,test"

        when:
        String[] values = parser.parseLine(testLine)

        then:
        values[0] == "This"
        values[1] == "is"
        values[2] == "a"
        values[3] == "test"
    }

    @Unroll
    def 'parsing #testLine yields values #expected1 #expected2 #expected3 and #expected4'(String testLine, String expected1, String expected2, String expected3, String expected4) {
        given:
        RFC4180ParserBuilder builder = new RFC4180ParserBuilder()
        RFC4180Parser parser = builder.build()

        expect:
        parser.parseLine(testLine) == [expected1, expected2, expected3, expected4]

        where:
        testLine                                               | expected1 | expected2 | expected3                          | expected4
        "This,is,a,test"                                       | "This"    | "is"      | "a"                                | "test"
        "7,seven,7.89,12/11/16"                                | "7"       | "seven"   | "7.89"                             | "12/11/16"
        "1,\"\\\"\"\",\"this is a quote \"\" character\",test" | "1"       | "\\\""    | "this is a quote \" character"     | "test"
        "2,\\ ,\"this is a comma , character\",two"            | "2"       | "\\ "     | "this is a comma , character"      | "two"
        "3,\\\\ ,this is a backslash \\ character,three"       | "3"       | "\\\\ "   | "this is a backslash \\ character" | "three"
        "5,\"21,34\",test comma,five"                          | "5"       | "21,34"   | "test comma"                       | "five"
        "8,\\',\"a big line with \n" +
                "multiple carriage returns\n" +
                "in it.\",eight"                               | "8"       | "\\'"     | "a big line with \n" +
                "multiple carriage returns\n" +
                "in it."                                                                                                    | "eight"
    }

    @Unroll
    def 'parsing #testLine with custom quote yields values #expected1 #expected2 #expected3 and #expected4'(String testLine, String expected1, String expected2, String expected3, String expected4) {
        given:
        RFC4180ParserBuilder builder = new RFC4180ParserBuilder()
        RFC4180Parser parser = builder.withQuoteChar(SINGLE_QUOTE).build()

        expect:
        parser.parseLine(testLine) == [expected1, expected2, expected3, expected4]

        where:
        testLine                                         | expected1 | expected2 | expected3                          | expected4
        "This,is,a,test"                                 | "This"    | "is"      | "a"                                | "test"
        "7,seven,7.89,12/11/16"                          | "7"       | "seven"   | "7.89"                             | "12/11/16"
        "1,'\\'','this is a quote '' character',test"    | "1"       | "\\'"     | "this is a quote ' character"      | "test"
        "2,\\ ,'this is a comma , character',two"        | "2"       | "\\ "     | "this is a comma , character"      | "two"
        "3,\\\\ ,this is a backslash \\ character,three" | "3"       | "\\\\ "   | "this is a backslash \\ character" | "three"
        "5,'21,34',test comma,five"                      | "5"       | "21,34"   | "test comma"                       | "five"
        "8,\\\",'a big line with \n" +
                "multiple carriage returns\n" +
                "in it.',eight"                          | "8"       | "\\\""    | "a big line with \n" +
                "multiple carriage returns\n" +
                "in it."                                                                                              | "eight"
    }

    @Unroll
    def 'parsing #testLine with custom separator yields values #expected1 #expected2 #expected3 and #expected4'(String testLine, String expected1, String expected2, String expected3, String expected4) {
        given:
        RFC4180ParserBuilder builder = new RFC4180ParserBuilder()
        RFC4180Parser parser = builder.withSeparator(PERIOD).build()

        expect:
        parser.parseLine(testLine) == [expected1, expected2, expected3, expected4]

        where:
        testLine                                               | expected1 | expected2 | expected3                          | expected4
        "This.is.a.test"                                       | "This"    | "is"      | "a"                                | "test"
        "7.seven.7,89.12/11/16"                                | "7"       | "seven"   | "7,89"                             | "12/11/16"
        "1.\"\\\"\"\".\"this is a quote \"\" character\".test" | "1"       | "\\\""    | "this is a quote \" character"     | "test"
        "2.\\ .\"this is a comma . character\".two"            | "2"       | "\\ "     | "this is a comma . character"      | "two"
        "3.\\\\ .this is a backslash \\ character.three"       | "3"       | "\\\\ "   | "this is a backslash \\ character" | "three"
        "5.\"21.34\".test comma.five"                          | "5"       | "21.34"   | "test comma"                       | "five"
        "8.\\'.\"a big line with \n" +
                "multiple carriage returns\n" +
                "in it.\".eight"                               | "8"       | "\\'"     | "a big line with \n" +
                "multiple carriage returns\n" +
                "in it."                                                                                                    | "eight"
    }

    def 'able to parse a field that has a single quote at the end'() {
        given:
        RFC4180ParserBuilder builder = new RFC4180ParserBuilder()
        RFC4180Parser parser = builder.build()
        String testLine = "line 1\""

        when:
        String[] values = parser.parseLine(testLine)

        then:
        values[0] == "line 1\""
        values.length == 1
    }
}
