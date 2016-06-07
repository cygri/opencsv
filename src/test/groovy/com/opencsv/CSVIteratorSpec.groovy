package com.opencsv

import spock.lang.Specification

class CSVIteratorSpec extends Specification {

    def 'reader exception should cause runtime exception'() {
        given:
        CSVReader reader = Mock(CSVReader)
        CSVIterator iterator = new CSVIterator(reader)

        and:
        def exceptionMsg = 'reader threw test exception'
        reader.readNext() >> {
            throw new IOException(exceptionMsg)
        }

        when:
        iterator.next()

        then:
        thrown(NoSuchElementException)
    }

    def 'call to remove() results in UnsupportedOperationException thrown'() {
        given:
        CSVIterator iterator = new CSVIterator(Mock(CSVReader))

        when:
        iterator.remove()

        then:
        thrown(UnsupportedOperationException)
    }

    def 'initial read should return string array'() {
        given:
        def output = ['test1', 'test2'] as String[]

        CSVReader reader = Mock(CSVReader) {
            readNext() >> output
        }
        CSVIterator iterator = new CSVIterator(reader)

        when:
        def read = iterator.next()

        then:
        read.toList() == output.toList()
    }

    def 'call to hasNext() returns correct results'() {
        given:
        CSVReader reader = Mock(CSVReader) {
            readNext() >>> [['a'], null]
        }

        and:
        CSVIterator iterator = new CSVIterator(reader)

        when:
        def hasNext = iterator.hasNext()

        then:
        hasNext

        when:
        iterator.next()
        hasNext = iterator.hasNext()

        then:
        !hasNext
    }
}
